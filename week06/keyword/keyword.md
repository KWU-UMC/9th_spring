# WEEK 6 - 제이/한종서

## 핵심 키워드

### QueryDSL에서 FetchJoin 하는 법

`FetchJoin`은 N + 1 문제를 해결할 때 QueryDSL에서 자주 쓰이는 핵심 기능이다.

JPA에서 연관된 엔티티를 가져올 때, 지연 로딩(LAZY)이면 연관 객체를 필요할 때마다 추가로 조회하게 된다.

그래서 N + 1 문제가 생기는데, `FetchJoin`은 이걸 하나의 SQL로 미리 묶어 가져오는 기능이다.

#### 예시

```sql
SELECT m.*, t.*
FROM member m
JOIN team t ON m.team_id = t.id;
```

JPA에서는

```java
select m from Member m join fetch m.team
```

QueryDSL에서는

```java
List<Member> members = queryFactory
    .selectFrom(member)
    .join(member.team, team).fetchJoin()
    .where(team.name.eq("TeamA"))
    .fetch();
```

`.join(member.team, team)`: Member와 Team을 조인한다.

`.fetchJoin()`: 실제 SQL에서 **JOIN FETCH**로 번역된다.

`.where()`: 조건

`.fetch()`: 결과 리스트 반환

join() 뒤에 `.fetchJoin()`만 붙이면 끝이다.

<br>

### DTO 매핑 방식(+ DTO 안에 DTO)

JPA 엔티티를 그대로 반환할 경우 불필요한 데이터가 노출될 우려가 있고, 엔티티 변경 시 API 응답도 함께 변경되어 캡슐화가 깨질 수 있다.

그래서 보통 별도 클래스에 필요한 필드만 담아 반환하는데, 이 클래스를 `DTO`라 한다.

<br>

#### 기본 DTO 매핑(단일 DTO)

**DTO 클래스**

```java
@Getter
@AllArgsConstructor
public class MemberDto {
    private String name;
    private int age;
}
```

**QueryDSL 코드**

```java
List<MemberDto> result = queryFactory
    .select(Projections.constructor(MemberDto.class,
            member.name,
            member.age))
    .from(member)
    .fetch();
```

`Projections.constructor()`는 생성자 기반으로 DTO 매핑을 수행하는 코드이다.

<br>

#### DTO 안에 DTO(Nested DTO)

**구조 예시**
```java
@Getter
@AllArgsConstructor
public class TeamDto {
    private String teamName;
    private List<MemberDto> members;
}

@Getter
@AllArgsConstructor
public class MemberDto {
    private String name;
    private int age;
}
```

**데이터 조회**

```java
List<Tuple> result = queryFactory
    .select(team.name, member.name, member.age)
    .from(team)
    .join(team.members, member)
    .fetch();
```

`.select(...)`: 조회하고자 하는 최종 필드들을 **단일 목록**으로 지정한다. 여기서는 Team의 이름과 관련된 모든 Member의 이름, 나이를 함께 선택한다.

`.from(team)`: 쿼리의 시작점(FROM 절)을 Team 엔티티로 설정한다.

`.join(team.members, member)`: Team 엔티티 내부의 members 컬렉션을 기반으로 Member 엔티티와 Join한다. 이는 SQL의 `INNER JOIN`과 유사하게 동작하여, 팀과 멤버가 매핑된 데이터만 가져온다.

`.fetch()`: 결과 리스트 반환

<br>

### 커스텀 페이지네이션

Spring Data JPA의 `Pageable`을 그대로 쓰는 방법도 있지만, 복잡한 조회 쿼리나 동적 검색에서는 직접 구현해야 더 유연하게 동작하는 경우가 있다.

커스텀 페이징의 목표는 **데이터 조회 쿼리**와 **전체 개수 조회 쿼리**라는 두 개의 쿼리를 개발자가 직접, 효율적으로 제어하는 것이다.

**데이터 조회 쿼리**

```sql
SELECT *
FROM member
WHERE age > 30
ORDER BY name ASC
LIMIT 10 OFFSET 20;
```

**실제 사용자에게 보여줄** N개의 데이터를 가져온다.

**전체 개수 조회 쿼리**

```sql
SELECT count(*)
FROM member
WHERE age > 30;
```

**전체 페이지 수**를 계산하기 위해 필터링 조건에 맞는 **총 데이터 개수**를 가져온다.

`QueryDSL`은 이 두 쿼리를 직접 작성할 수 있도록 지원한다.

- 데이터 조회 쿼리: `.limit(10).offset(20)` 메서드를 사용하여 **페이징 조건(LIMIT/OFFSET)** 을 적용한다.
- 전체 개수 쿼리: `count()` 또는 `fetchCount()`를 사용하는데, 이때 필요 없는 JOIN 구문을 COUNT 쿼리에서 제거할 수 있다.

예를 들어, 데이터 조회 시 `Team` → `Member` → `Order` 테이블까지 JOIN이 필요하지만, COUNT를 할 때 `Team` 테이블만 세도 전체 개수를 알 수 있다면, QueryDSL을 사용하여 COUNT 쿼리에서는 `Order` 테이블과의 JOIN을 제외하고 실행할 수 있다.

이는 성능 최적화를 도모한다.

```java
// QueryDSL을 이용한 데이터 조회
List<MemberDto> content = queryFactory
    .select(Projections.constructor(MemberDto.class, member.name, member.age)) // DTO Projection
    .from(member)
    .where(member.age.gt(30))
    .orderBy(member.name.asc())
    .limit(10) // SQL의 LIMIT
    .offset(20) // SQL의 OFFSET
    .fetch();

// QueryDSL을 이용한 전체 개수 조회
Long totalCount = queryFactory
    .select(member.count())
    .from(member)
    .where(member.age.gt(30)) // where 조건은 동일
    .fetchOne(); // COUNT는 단일 결과
```

이처럼 개발자가 **데이터 조회에 필요한 것**과 **COUNT에 필요한 것**을 분리하여 작성할 수 있기에, Spring Data의 기본 `Pageable` 사용 시 발생하는 불필요한 JOIN 문제를 회피하고 최적화할 수 있다.

<br>

### transform - groupBy

QueryDSL의 `transform - groupBy` 기능은 복잡한 1:N 관계의 데이터를 한 번의 쿼리로 가져온 후, 애플리케이션 메모리 단에서 원하는 계층적 DTO 구조로 깔끔하게 묶어주는 **결과 집합 변환(Result Aggregation)** 이다.

이는 특히 **Fetch Join**을 사용하면서 생기는 **중복 데이터 문제**를 해결하고, 원하는 모양의 DTO로 바로 매핑할 때 유용하다.

설명이 복잡한데, 예를 들어보자.

`Parent`와 `Child`가 1:N 관계일 때, `Parent`를 기준으로 조인하면 `Child`의 개수만큼 `Parent` 데이터가 중복되어 조회된다.

이 중복된 결과를 **transform**과 **groupBy**를 사용하여 Parent 키를 기준으로 그룹화하고, 중복 없이 하나의 Parent 밑에 모든 Child 리스트를 묶어 최종 DTO를 생성할 수 있다.

다만, `transform`에 사용되는 `groupBy`는 SQL의 `GROUP BY` 절이 아님을 알아두어야 한다.

데이터베이스가 아닌 애플리케이션 메모리에서 로직을 수행하며, 데이터베이스에서 조회된 중복된 레코드들을 QueryDSL이 내부적으로 처리하여 지정된 키를 기준으로 그룹핑하고 리스트로 만들어준다.

```java
import static com.querydsl.core.group.GroupBy.*;
```

위의 import 문을 사용하면 transform - groupBy를 사용할 수 있다.

`Post`(1)와 `Comment`(N) 관계에서, 한 번의 쿼리로 `Post`별 `Comment` 리스트를 Map 형태로 가져오는 예시를 가져와봤다.

```java
// 결과 타입: Post ID(Long)를 Key로, List<Comment>를 Value로 하는 Map
Map<Long, List<Comment>> results = queryFactory
    .from(post)
    .leftJoin(post.comments, comment) // 1:N 조인
    .where(...)
    // transform 시작
    .transform(
        // Post ID를 기준으로 그룹화(Key)
        groupBy(post.id)
            // Value: List<Comment>를 만듦
            .as(list(comment))
    );
```

다음 예시는 DTO로 매핑하는 예시인데, 그룹화된 결과를 DTO로 매핑할 때 `list()` 함수 내부에 `Projections`를 활용할 수 있다.

```java
// Post ID를 Key로, PostInfoDTO를 Value로 하는 Map
Map<Long, PostInfoDTO> results = queryFactory
    .from(post)
    .leftJoin(post.comments, comment)
    .transform(
        groupBy(post.id) // Key
        .as(
            // Value: PostInfoDTO의 생성자로 매핑
            Projections.constructor(PostInfoDTO.class,
                post.title,
                // Comment List도 DTO로 변환하여 묶음
                list(
                    Projections.constructor(CommentDTO.class,
                        comment.content,
                        comment.author
                    )
                ).as("commentList") // DTO 필드 이름과 일치시켜야 함
            )
        )
    );
```

`transform - groupBy`는 1:N 관계를 DTO로 매핑하는 데 있어 깔끔한 코드를 제공한다.

하지만 메모리에서 모든 결과를 처리하기 때문에, 데이터의 양(N의 크기)이 매우 커지면 애플리케이션 서버의 메모리(Heap)에 부담을 주어 `Out Of Memory`가 발생할 수 있다.

따라서 해당 기능은 실시간 서비스에서 수십, 수백 건 이내의 적은 데이터를 처리할 때 가장 효과적이며, 대규모 데이터를 다룰 때는 `LIMIT`과 `OFFSET`을 통한 페이징 처리를 고려해야 한다.

<br>

### order by null

`ORDER BY NULL`은 말 그대로 **정렬하지 않겠다**라는 명령이다.

즉, DB가 결과를 정렬하지 않고 그대로 반환하도록 강제한다.

보통 쿼리에 `ORDER BY`가 있으면 DB는 정렬용 temporary memory를 사용한다.

하지만 정렬이 불필요한 경우에는 오히려 불필요한 오버헤드가 발생하게 된다.

이런 경우 `ORDER BY NULL`을 사용하면 정렬용 메모리를 쓰지 말고, 테이블에서 읽히는 순서대로 결과를 반환하도록 강제할 수 있다.

<br>

#### 대표적인 사용 예시

MySQL, MariaDB에서는 `GROUP BY`가 내부적으로 `ORDER BY`와 비슷하게 동작한다.

즉 그룹화 결과가 자동으로 정렬되는 경향이 있다고 한다.

하지만, 정렬이 필요 없을 때

```sql
SELECT team, COUNT(*)
FROM member
GROUP BY team
ORDER BY NULL;
```

이렇게 `ORDER BY NULL`을 붙이면 정렬을 하지 않아서 속도가 조금 더 빨라진다.

또 다른 예시로, `UNION`, `UNION ALL` 이후 불필요한 정렬을 제거하여 성능을 향상시키거나 **정렬 안 함**을 명시하고 싶을 때 의도를 명확하게 표현하는 용도로도 사용할 수 있다.