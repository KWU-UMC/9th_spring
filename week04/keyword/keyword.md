# WEEK 4 - 하지명

### keyword
- 계층형 구조 vs 도메인형 구조
- JPA
- N+1 문제
- 기본 키 생성 전략

---
## 1. 계층형 구조 vs 도메인형 구조
프로젝트의 코드 구조를 어떻게 조직하느냐는 생산성과 유지보수성에 큰 영향을 미침 </br>
### 계층형 구조
애플리케이션을 기능별로 나누어 각 기능이 독립적으로 동작하는 방식 </br>
`Controller`는 모두 `Controller` 패키지에 넣고, `Service`는 모두 `Service` 패키지에 넣는 방식 </br>
```text
com.example.projectname
├── controller
├── service
├── repository
├── domain
├── dto
├── config
└── exception
```
#### 패키지 설명
- Controller: 사용자 요청을 처리하고, 응답을 반환
- Service: 비즈니스 로직을 처리
- Repository: 데이터베이스와 상호작용
- Domain: 애플리케이션의 핵심 도메인 모델을 정의
- DTO: 데이터 전송 객체들의 정의
- Config: 설정 관련 클래스를 포함
- Exception: 커스텀 예외 클래스를 포함

#### 장점
- 각 레이어별 역할이 명확해 코드의 가독성이 높음
- 동일한 레이어 내에서 코드 중복이 줄어듦

#### 단점
- 대규모 프로젝트에서는 패키지가 복잡해질 수 있음
- 특정 기능과 관련된 코드를 찾기 어려움

### 도메인형 구조
도메인에 초점을 맞추어 코드를 구성하는 방식 </br>
이 방식에서는 관련된 기능들을 도메인 단위로 그룹화함.</br>
사용자 관련 클래스는 `user`패키지에, 제품 관련 클래스는 `product`패키지에 넣는 식 </br>
```text
com.example.projectname
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   ├── dto
│   └── exception
├── product
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   ├── dto
│   └── exception
└── config
```
#### 패키지 설명
- user: 사용자 도메인과 관련된 모든 클래스가 포함됨
- product: 제품 도메인과 관련된 모든 클래스가 포함됨

#### 장점
- 특정 도메인의 코드를 한 곳에 모아둬서 코드 탐색이 용이함
- 도메인 단위로 개발하고, 유지보수하기 용이함
- 새로운 도메인 추가 시 다른 곳에 영향을 주지 않음

#### 단점
- 도메인 간의 의존성 관리가 어려울 수 있음
- 도메인 간 코드 중복이 발생할 수 있음

### 정리
|  구분  |       레이어형 구조        |    도메인형 구조    |
|:----:|:--------------------:|:-------------:|
|  구조  |     기능별로 패키지를 나눔     | 도메인별로 패키지를 나눔 |
|장점|    각 레이어별로 역할이 명확    | 각 도메인의 독립성 유지 |
||      코드 중복이 줄어듦      | 각 도메인의 독립성 유지 |
|단점| 대규모 프로젝트에서 패키지가 복잡해짐 |코드 중복이 발생할 수 있음|
|| 도메인 간의 의존성이 높아질 수 있음 | 도메인 간의 의존성 관리가 어려울 수 있음|

- **레이어형 구조** : 각 레이어별로 역할이 명확하고, 코드 중복을 줄이는 데 유리함. 작은 규모의 프로젝트나 단순한 애플리케이션에 적합함
- **도메인형 구조** : 모듈화가 잘 되어있고, 대규모 프로젝트에서 각 도메인의 독립성을 유지하고, 기능 단위로 개발과 유지보수가 용이하고, 협업에 유리

---
## 2. JPA
- Java 진영에서 ORM(Object-Relational Mapping) 기술 표준으로 사용하는 인터페이스 모음
- 자바 애플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스
- 인터페이스이기 때문에, Hibernate, OpenJPA 등이 JPA를 구현함

### 동작 과정
- JPA는 애플리케이션과 JDBC API 사이에서 동작
- 개발자가 JPA를 사용하면, JPA 내부에서는 JDBC API를 사용해 SQL을 생성해 DB와 통신
  - JPA에서의 CRUD
- JPA에서의 CRUD 메소드 예시
    ```java
    jpaEm.persist(member);	  // 저장
    jpaEm.find(memberId);     // 조회
    jpaEm.remove(member);     // 삭제
    
    // 수정(조회 이후 해당 객체에 변경하면 DB에도 변경된다.)
    // 영속성 컨텍스트
    Member member = jpaEm.find(memberId);
    member.setName("변경할 이름");
    ```
- JPA에서는 수정 메소드를 제공하지 않음
- 수정은 필요한 동작이기 때문에, JPA에서는 데이터 수정 시, 매핑된 객체(테이블 데이터)를 조회해서 값을 변경 후 커밋하면 DB서버에서 UPDATE문으로 바꿔 UPDATE를 실행

### JPA 어노테이션 종류
|어노테이션|                              설명                               
|:---:|:-------------------------------------------------------------:|
|@Entity| 데이터베이스의 테이블과 일대일로 매칭되는 객체 단위이며 </br> 클래스를 테이블과 매핑한다고 JPA에게 알림 |
|@Table| @Entity 선언된 클래스에 매핑할 테이블 정보를 알려줌 </br> 이 어노테이션이 생략되면 클래스이름을 테이블이름으로 매핑|
|@Column| 데이터베이스의 테이블에 있는 컬럼에 필드(변수)를 매핑함. </br> 별다른 옵션을 설정하지 않는다면 기본적으로는 생략 가능|
|@Id|@Entity 선언된 클래스의 필드를 테이블의 기본키(PK)에 매핑|
|@GeneratedValue|새로운 레코드가 생성될 때마다 마지막에 PK 값에서 자동으로 +1을 해줘야하는 `auto increment` 컬럼인 것을 알려줌|
|@EmbeddedId|복합키로서 정의된 값을 정의하고자 할 때 사용함|
|@Enumerated|Java의 Enum형태로 되어있는 미리 정의되어 있는 코드 값이나 구분값을 데이터 타입으로 사용할 때 사용|
|@Transient|Entity 객체에 속성으로 지정되어 있지만, 데이터베이스에서는 필요없는 속성일 때 사용|



#### JPA 장점
1. 생산성
   - JPA에 객체를 전달만 하면 되므로, SQL을 작성하고, JDBC API를 사용하는 반복적인 일을 JPA가 대신 처리해줘 생산성이 향상됨
   - DDL도 JPA가 자동으로 생성해주기 때문에, `DB 설계 중심`을 `객체 설계 중심`으로 변경 가능
2. 유지 보수
   - SQL을 직접 다룰 때는 필드 하나를 추가/삭제해도 관련된 SQL과 JDBC 코드를 전부 수정해야 했음
   - JPA는 이를 대신 처리해주어 유지보수가 줄어듦
3. 패러다임의 불일치 해결
   - JPA는 연관된 객체를 사용하는 시점에 SQL을 전달할 수 있고, 같은 트랜잭션 내에서 조회할 때 동일성도 보장하기 때문에, 다양한 패러다임의 불일치를 해결
4. 성능
   - 애플리케이션과 데이터베이스 사이에서 성능 최적화 기능을 제공함
   - 같은 트랜잭션 안에서는 같은 엔티티를 반환하기 때문에 통신 횟수를 줄일 수 있음
5. 데이터 접근 추상화와 벤더 독립성
   - RDB느 같은 기능이라도 벤더마다 사용법이 다르기 때문에, 처음 선택한 DB에 종속되고 변경이 어렵
   - JPA는 애플리케이션과 데이터베이스 사이에서 추상화된 데이터 접근을 제공해 종속되지 않도록 도와줌

#### JPA 단점
1. 학습 곡선이 높음
   - JPA를 사용하려면 객체와 관계형 데이터베이스를 어떻게 매핑해야 하는지 학습한 후에 JPA의 핵심 개념들을 이해해야 함
   - JPA의 핵심 개념인 영속성 컨텍스트에 대한 이해가 부족하면 SQL을 직접 사용해서 개발하는 것보다 못한 상황이 발생할 수 있음
2. 속도 저하 가능성이 있음
   - 프로젝트의 규모가 크고, 복잡해 설계가 잘못된 경우, 속도 저하 및 일관성을 무너뜨릴 수 있음
   - 복잡하고 무거운 Query는 속도를 위해 별도의 튜닝이 필요하기 때문에 SQL문을 써야 할 수 있음

---
## 3. N+1 문제
### N+1 문제란?
연관 관계에서 발생하는 이슈로 연관 관계가 설정된 엔티티를 조회할 경우에
조회된 데이터 갯수(n)만큼 연관관계의 조회 쿼리가 추가로 발생해 데이터를 읽어오게 됨
즉, 1번의 쿼리를 날렸을 때, 의도하지 않는 N번의 쿼리가 추가적으로 실행되는 문제임


**When?** : JPA Repository를 활용해 인터페이스 메소드를 호출할 때 </br>
**Who?** : 1:N 또는 N:1 관계를 가진 엔티티를 조회할 때 발생 </br>
**How?**
- JPA Fetch 전략이 EAGER 전략으로 데이터를 조회하는 경우
- JPA Fetch 전략이 LAZY 전략으로 데이터를 가져온 이후에 연관 관계인 하위 엔티티를 다시 조회하는 경우

**Why?**
- JPA Repository로 find시 실행하는 첫 쿼리에서 하위 엔티티까지 한 번에 가져오지 않고, 하위 엔티티를 사용할 때 추가로 조회하기 때문
- JPQL은 기본저긍로 글로벌 Fetch 전략을 무시하고, JPQL만 가지고 SQL을 생성하기 때문


### N+1 문제 해결 방법
#### **1. Fetch join**
N+1자체가 발생하는 이유는 한쪽 테이블만 조회하고 연결된 다른 테이블은 **따로 조회**하기 때문  
미리 두 테이블을 JOIN해 한 번에 모든 데이터를 가져올 수 있다면, 애초에 N+1 문제가 발생하지 않을 것  
`FetchJoin` 방법은 jpaRepository에서 제공해주는 것은 아니고, JPQL로 작성해야 함
```sql
@Query("select o from Owner o join fetch o.cats")
List<Owner> findAllJoinFetch();
```

##### Fetch Join의 단점
- 연관관계 설정해놓은 FetchType을 사용할 수 없음.
  - Fetch Join을 사용하게 되면, 데이터 호출 시점에 모든 연관 관계의 데이터를 가져오기 때문에 FetchType을 Lazy로 해놓는 것이 무의미함  
- 페이징 쿼리를 사용할 수 없음
  - 하나의 쿼리문으로 가져오다 보니 페이징 단위로 데이터를 가져오는 것이 불가능함

#### 정리
- 쿼리 한번에 모든 데이터를 가져오기 때문에, JPA가 제공하는 Paging API 사용 불가능
- 1:N 관계가 두 개 이상인 경우 사용 불가
- 패치 조인 대상에게 as(별칭)부여 불가능
- 번거롭게 쿼리문을 작성해야 함


#### **2. @EntityGraph**
@EntityGraph의 attributePaths에 쿼리 수행시 바로 가져올 필드명을 지정하면
Lazy가 아닌 Eager 조회로 가져오게 됨  
Fetch join과 동일하게 JPQL을 사용해 query문을 작성하고, 필요한 연관관계를 EntityGraph에 설정하면 됨  
그리고, Fetch join과 다르게 join문이 `outer join`으로 실행됨
```sql
@EntityGraph(attributePaths = "cats")
@Query("select o from Owner o")
List<Owner> findAllEntityGraph();
```
- Fetch join의 경우 inner join을 하는 반면에 EntityGraph는 outer join을 기본으로 함


#### **3. Batch Size**
이 옵션은 정확히는 N+1 문제를 안 일어나게 하는 방법은 아니고, N+1 문제가 발생하더라도,
`select * from user where team_id=?`이 아닌 `select * from user where team_id in (?,?,?)` 방식으로
N+1 문제가 발생하게 하는 방법
이렇게 하면 100번 일어날 N+1문제를 1번만 더 조회하는 방식으로 성능을 최적화할 수 있음  

##### application.yml
```text
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 1000
```
##### application.properties
```text
spring.jpa.properties.hibernate.default_batch_fetch_size=1000
```


### 실무에서 N+1문제로 DB가 죽어버리는 문제를 방지하려면?
연관관계에 대한 설정이 필요하다면 FetchType을 성능 최적화를 하기 어려운 즉시 로딩(EAGER)을 사용하는 게 아니라  
지연 로딩(LAZY)모드로 사용을 하고 성능 최적화가 필요한 부분에서는 Fetch 조인을 사용함  
또한, 기본적으로 Batch Size의 값을 1000이하로 설정 (대부분의 DB에서 IN절의 최대 개수 값; 1000)  

꼭 연관 관계 설정이 필요 없으면 N+1 문제로 인해 DB가 죽어버리는 불상사를 막기 위해 연관관계를 끊어버리고 사용하는 것도 방법임


---
## 4. 기본 키 생성 전략
JPA가 제공하는 기본 키 할당 방법은 `직접 할당 방식`과 `자동 생성 방식`이 존재 </br>
- `직접 힐당 방식`: 애플리케이션에서 기본 키를 직접 할당하는 방식
- `자동 생성 방식`: DB에 할당을 위임하거나 기본 키를 생성해주는 별도의 수단을 통해 할당하는 방식

### 직접 할당 방식
Entity를 생성할 때, Key Column @Id를 사용
```java
@Getter
@Entity
@NoArgsConstructor
public class Member{
    @Id
    private Long id;
}
```
해당 전략은 EntityManager.persist()로 Entity를 1차 캐시에 저장하기 전에
애플리케이션에서 직접 기본 키를 할당해줘야 함
```java
Member member=Member.builder()
                    .id(1)
                    .name("kim")
                    .build();
```

### 자동 생성 방식
자동 생성 바식은 @Id와 @GeneratedValue를 사용  
@GeneratedValue의 Strategy 속성을 통해 4가지 전략을 선택할 수 있음  
**_직접 기본 키를 할당하면 x_**

#### 1. AUTO
선택한 DB 방언에 따라 자동으로 IDENTITY, SEQUENCE, TABLE 전략을 자동으로 선택하는 전략
- DB를 변경해도 코드를 수정하지 않아도 됨
- 키 생성 전략이 정해지지 않은 개발 초기 단계나 프로토타입 개발 시에 편리하게 사용 가능
- SEQUENCE나 TABLE이 선택될 경우, Sequence나 키 생성 Table을 미리 생성해 둬야 함
  - 만약 DDL 자동 생성 기능을 사용하면, Hibernate 기본 값을 사용해 자동으로 생성해줌

#### 2. IDENTITY
기본 키 생성을 DB에 위임하는 전략
- MySQL의 경우, AUTO_INCREMENT 기능을 통해 기본 키 제공
- 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용
- 지연 쓰기 SQL 저장소를 사용하지 않음
```java
@Getter
@Entity
@NoArgsConstructor
public class Member{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
}
```

#### 3. SEQUENCE
DB의 시퀀스 오브젝트를 사용해 기본 키를 생성하는 전략
- DB 시퀀스는 유일한 값을 순서대로 생성하는 특별한 DB 오브젝트
- 주로 Oracle, PostgreSQL, DB2, H2에서 사용
- @SequenceGenerator를 통해 시퀀스 생성기를 등록할 수 있음
- 쓰기 지연 SQL 저장소 사용
```java
@Getter
@Entity
@SequenceGenerator(
        name="MEMBER_SEQ_GENERATOR",
        sequenceName="MEMBER_SEQ",
        initialValue=1, allocationSize=1
)
@NoArgsConstructor
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEMBER_SEQ_GENERATOR")
    @Column(name="member_id")
    private Long id;
}
```
**사용 가능한 옵션과 성능 최적화**

|속성|설명|        기본값         |
|:---:|:---:|:------------------:|
|name|식별자 생성기 이름|     없음. 지정 필수      |
|sequencName|DB에 등록되어 있는 시퀀스 이름 | hibernate_sequence |
|initialValue|DDL 생성 시에만 사용 </br> Sequence DDL 생성 시 처음 시작 value를 설정|         1          |
|allocationSize|시퀀스 한번 호출 시 증가하는 수 **(성능 최적화에 사용)** |         50         |
|catalog, schema| DB catalog, schema 이름|                    |

allocationSize를 통해 성능 최적화 가능
```text
allocationSize에 설정한 값만큼 시퀀스를 가져올 수 있음.  
그래서 필요한 만큼 미리 DB에 시퀀스를 올려놓고 식별자로 사용할 수 있어, JPA가 시퀀스에 접근하는 횟수를 줄일 수 있음
```

#### 4. TABLE
키 생성 테이블을 사용해 기본 키를 생성하는 전략
- SEQUENCE 전략과 동작 방식 동일
- @TableGenerator 필요
- 값을 조회하면서 Select 쿼리를, 그 후 값 증가를 위해 Update 쿼리 사용
- SEQUENCE 전략과 비교하여 DB와 한번 더 통신해 성능상 단점이 존재
- SEQUENCE 전략처럼 **allocationSize를 통해 최적화 가능**
```java
@Entity
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumValue = "MEMBER_SEQ", allocationSize = 1
)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long id;
    
}
```
**사용 가능한 옵션**

|속성|설명|         기본값         |
|:--:|:--:|:-------------------:|
|name|식별자 생성기 이름|      없음, 지정 필수      |
|table|키 생성 테이블 명| hibernate_sequences |
|pkColumnName|시퀀스 컬럼 명|    sequence_name    |
|valueColumnName|시퀀스 값 컬럼 명|      next_val       |
|pkColumnValue|키로 사용할 값 이름|      Entity 이름      |
|initialValue|초기 값, 마지막 생성된 값이 기준|          0          |
|allocationSize|시퀀스 호출 시 증가 값(성능 최적화에 사용)|         50          |
|catalog, schema|DB catalog, schema 이름|                     |
|uniqueConstraints(DDL)|유니크 제약 조건을 지정|                     |

