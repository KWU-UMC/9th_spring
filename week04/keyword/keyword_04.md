## 계층형 구조 vs 도메인형 구조

  ### 1. 계층형 구조 (Layered Architecture)

  ```
  com.example.umc9th
   ┣ controller
   ┣ service
   ┣ repository
   ┣ entity
   ┗ dto
  ```

  ### 특징

- 기능(역할) 기준으로 폴더를 나눔 (Controller / Service / Repository 등)
- 계층 간 의존성 방향이 명확함 (Controller → Service → Repository)
- Spring 공식 문서나 기본 템플릿이 이 구조 기반이라 학습 곡선이 낮음

  ### 장점

  | 항목 | 설명 |
      | --- | --- |
  | 명확한 계층 분리 | 비즈니스 로직, 데이터 접근, API 레이어를 구분하기 쉬움 |
  | 단일 기능 수정 용이 | 특정 계층(예: Repository) 변경 시 영향이 제한적 |
  | 초기 구조 단순함 | 작은 규모 프로젝트에서 빠르게 개발 가능 |

  ### 단점

  | 항목 | 설명 |
      | --- | --- |
  | ERD 기반 도메인 추적 어려움 | 하나의 도메인(Member, Mission 등)에 대한 로직이 여러 폴더(controller/service/repo)에 흩어짐 |
  | 협업 시 충돌 증가 | 여러 개발자가 같은 service/controller 디렉토리 작업 시 충돌 가능 |
  | 유지보수 어려움 | 프로젝트가 커질수록 “어떤 파일이 어떤 도메인 관련인지” 파악이 어려워짐 |
    
  ---

  ### 2. 도메인형 구조 (Package by Domain / Feature-based Architecture)

```
com.example.umc9th.domain
 ┣ member
 ┃ ┣ controller
 ┃ ┣ service
 ┃ ┣ entity
 ┃ ┗ repository
 ┣ mission
 ┃ ┣ controller
 ┃ ┣ service
 ┃ ┗ entity
 ┣ store
 ┗ review
```

  ### 특징

    - 도메인(ERD 기준 Entity 그룹) 단위로 패키지를 구성
    - 각 도메인 패키지 내부에서 Controller / Service / Entity / Repository를 모두 포함
    - DDD(Domain-Driven Design)의 기초적인 형태

  ### 장점

  | 항목 | 설명 |
      | --- | --- |
  | ERD 기반 도메인 매핑 용이 | Member, Mission, Store 등 엔티티 단위로 구조가 그대로 반영됨 |
  | 협업 시 역할 분담 명확 | 각 팀원이 도메인 단위로 맡아 개발 가능 → 충돌 최소화 |
  | 확장성 높음 | 신규 도메인 추가 시, 기존 코드에 영향 없이 패키지 추가만으로 확장 가능 |
  | 코드 응집도 향상 | 관련된 비즈니스 로직과 엔티티가 같은 패키지에 위치하여 관리 용이 |
  | 리뷰와 디버깅 용이 | “Member” 관련 기능은 member 폴더만 보면 됨 (문맥 이해 쉬움) |

  ### 단점

  | 항목 | 설명 |
      | --- | --- |
  | 초기 구조 복잡 | 패키지가 많아지고, 초반엔 구조가 낯설 수 있음 |
  | 중복 가능성 | 공통 로직/유틸을 각 도메인에서 중복 구현할 수 있음 (→ global 패키지로 분리 필요) |
  | DDD 개념 이해 필요 | 도메인 간 경계나 의존성 설정에 대한 설계 고민이 필요함 |
---
## JPA

  ### 1. JPA란?

  JPA(Java Persistence API)는 **자바에서 객체(Object)와 관계형 데이터베이스(Relational DB)** 사이의 데이터를 **매핑(ORM, Object-Relational Mapping)** 해주는 기술 표준이다.

  즉, SQL을 직접 작성하지 않고 **자바 객체 중심으로 데이터베이스를 다룰 수 있게 해주는 ORM 표준 인터페이스**이다.
    
  ---

  ### 2. ORM이란?

  ORM(Object Relational Mapping)은 **객체(Object)** 와 **데이터베이스의 테이블(Table)** 을 매핑하여

  SQL 대신 **객체 지향적인 방식으로 데이터 조작을 가능하게 하는 기술**이다.
    
  ---

  ### 3. JPA 사용 시 개발 흐름

1. Entity 정의 (`@Entity`, `@Table`, `@Id` 등)
2. Repository 생성 (Spring Data JPA 활용)
3. Service에서 트랜잭션 관리 및 비즈니스 로직 작성
4. Controller에서 Service 호출
5. JPA 구현체(대표적으로 Hibernate)가 SQL 자동 생성 및 DB 반영

---

## N+1 문제

  ### 1. 개념

  JPA에서 연관된 엔티티를 **지연 로딩(LAZY)** 으로 조회할 때,

  **한 번의 조회(1)** 이후 연관된 엔티티를 각각 **N번 추가 조회**하는 비효율적 쿼리 현상.

  > 부모 1번 + 자식 N번 → 총 N+1개의 SQL 실행
  >
    
  ---

  ### 2. 예시

```java
List<Member> members = memberRepository.findAll();
for (Member m : members) {
    System.out.println(m.getMissions().size());
}
```

  → 실행 SQL

```sql
SELECT * FROM member;              -- 1번
SELECT * FROM mission WHERE member_id = 1;  -- N번
```
    
  ---

  ### 3. 원인

- `@OneToMany`, `@ManyToOne` 관계의 **LAZY 로딩**
- 부모만 먼저 조회하고, 자식은 접근할 때마다 쿼리 발생

    ---

  ### 4. 해결 방법

  | 방법 | 설명 |
      | --- | --- |
  | **Fetch Join** | 한 번의 JOIN으로 연관 데이터 함께 조회 (`JOIN FETCH`) |
  | **EntityGraph** | `@EntityGraph(attributePaths = {"missions"})` 사용, JPQL 없이 fetch join |
  | **Batch Size 설정** | `hibernate.default_batch_fetch_size: 100` → 여러 건 묶어서 조회 |
  | **EAGER 로딩** | 항상 함께 로딩하지만, 불필요한 데이터까지 가져와 비추천 |

---
## 기본 키 생성 전략

  JPA는 `@GeneratedValue` 어노테이션을 통해

  **기본 키(PK)를 자동 생성**하는 여러 가지 전략을 지원한다.
    
  ---

  ### 1. IDENTITY

  **DB의 AUTO_INCREMENT 기능**을 이용해 PK 자동 생성.

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

- 예: MySQL, MariaDB
- DB가 직접 키 값을 생성
- JPA가 insert 이후에 키 값을 조회함
- **장점:** 설정 간단, DB 자동 증가
- **단점:** persist 시점에 insert 실행 → **배치 처리 불가**

    ---

  ### 2. SEQUENCE

  DB의 **시퀀스 객체(Sequence)** 를 사용해 PK 생성.

```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
@SequenceGenerator(name = "member_seq_gen", sequenceName = "member_seq", allocationSize = 1)
private Long id;
```

- 예: Oracle, PostgreSQL
- JPA가 시퀀스에서 ID를 미리 가져옴
- **장점:** 미리 ID 확보 가능, 배치 처리 유리
- **단점:** 시퀀스 지원 DB에서만 사용 가능

  ---

  ### 3. TABLE

  DB의 **별도 키 관리용 테이블**을 만들어 PK 생성.

    ```java
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
    @TableGenerator(name = "table_gen", table = "id_gen", pkColumnValue = "member_seq", allocationSize = 1)
    private Long id;
    ```

    - DB 종류와 무관하게 사용 가능
    - **장점:** 모든 DB 호환
    - **단점:** 별도 테이블 접근 필요 → 성능 저하

    ---

  ### 4. AUTO (기본값)

  DB Dialect에 따라 **자동으로 적절한 전략 선택**

  (MySQL → IDENTITY, Oracle → SEQUENCE 등)

    ```java
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    ```

    - **장점:** DB 독립성 높음
    - **단점:** DB 변경 시 키 생성 방식이 달라질 수 있음