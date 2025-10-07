# 🎯 핵심 키워드
## 계층형 구조 vs 도메인형 구조

  ### **1️⃣ 계층형 구조 (Layered Architecture)**

  **전통적인 구조** — 기능(역할)에 따라 코드를 나누는 방식

  📁 **예시 구조**

    ```
    controller/
    service/
    repository/
    entity/
    dto/
    
    ```

  ### **2️⃣ 도메인형 구조 (Domain-Oriented Architecture)**

  **DDD (Domain-Driven Design)** 스타일 — **도메인(업무 단위)** 중심으로 구성

  📁 **예시 구조**

    ```
    user/
     ┣ controller/
     ┣ service/
     ┣ repository/
     ┗ entity/
    order/
     ┣ controller/
     ┣ service/
     ┗ entity/
    
    ```

## JPA

  **`JPA`는 `Java Persistence API`의 약자**로 **Java진영에서 제공**하는 **관계형 데이터베이스 모델과 객체 모델간의 패러다임 불일치를 해결해주는 ORM 기술 표준**이다.

  *ORM : Object-Relational Mapping*

  지금까지 **객체 모델링을 세밀하게 진행**할 수록 **객체를 DB에 저장 조회하기 어려워 졌고**,

  **객체와 관계형 DB 차이를 위해 더 많은 SQL 작성이 필요**했다.

  그렇게 **점점 객체 모델은 데이터 중심 모델로 변해갔다.**

  **`JPA`를 활용**함으로써 **기존 코드에서 적은 수정으로 DB를 손쉽게 변경**할 수 있다.

  ### Not Using JPA, SQL

  **DB는 객체 구조와 다른 Data 중심 구조**를 가지고 있다.

  **객체를 DB에 CRUD 하기 위해선 많은 SQL과 JDBC API 코드 작성이 필요**하다.

  **기존 로직을 수정하기 위해서 `DAO`를 열어 `SQL`을 확인하고 나서야 로직 에러등을 검출**해 내고,

  **데이터 접근 계층을 사용해 `SQL`을 숨겨도** **로직이나 코드 수정시**에 **`DAO`를 확인**해 **어떠한 `SQL`이 실행 되는지 확인**해야 했다.

  **비즈니스 요구사항을 모델링한 객체를 `엔티티`**라 하는데,

  **`SQL`에 의존하는 상황에서는 이 `엔티티`를 신뢰하고 사용할 수 없다.**

  ***DAO를 열어 어떤 SQL이 실행되고 어떤 객체들이 조회되는지 일일이 확인***

  **Application에서 SQL을 직접 다루게 될 경우 발생하는 문제점은 아래 세 가지로 요약**할 수 있다.

    - 진정한 의미의 계층 분할이 어렵다.
    - 엔티티를 신뢰할 수 없다.
    - SQL 의존적인 개발을 하게 된다.

  ### Using JPA, SQL

  **`JPA`를 사용**하면 **객체를 DB에 저장하고 관리 할 때 SQL을 작성하는 것이 아닌**,

  **`JPA`에서 제공하는 API를 사용**하면 된다.

  ### 저장, jpa.persist(member)

  **`persist()` method는 객체를 DB에 저장**한다.

  정확히 말하면 **`JPA`가 객체와 매핑 정보(@Entity)를 확인**하고,

  **적절한 `Insert SQL`을 생성해 보관했다가 `flush()`하는 경우에 DB에 반영**시킨다.

  ### 조회, jpa.find(entity.class, id)

  **`find()` method는 DB에서 조회한 정보를 객체로 전달**해준다.

  **`JPA`는 객체와 매핑 정보(@Entity)를 확인**하고,

  **적절한 `Select SQL`을 생성해 DB에 전달하고 그 결과로 매핑된 객체를 생성해 반환**한다.

    ```java
    Member member = jpa.find(Member.class, "gillog");
    ```

  ### 수정, member.setName("변경이름")

  **`JPA`는 별도의 수정 method가 존재하지 않는다.**

  대신 **조회한(영속화된) 객체의 값을 변경하는 경우**,

  **`Transaction`을 `commit`하는 때에(`flush()`) 적절한 `Update SQL`을 DB에 전달하여 반영**한다

- N+1 문제

  **N+1 : 조회 시 1개의 쿼리를 생각하고 설계를 했으나 나오지 않아도 되는 조회의 쿼리가 N개가 더 발생하는 문제.**

    - 처음이 1
    - member agree를 호출했을때가 n
    - JPA가 등장함에 따라 자동화된 쿼리문들이 생겨나면서 어쩔 수 없이 발생하는 문제

  ### **JPA에서의 즉시로딩과 지연로딩의 개념**

    - **즉시로딩 (xxToxx(fetch = fetchType.EAGER): 데이터를 조회할 때, 연관된 모든 객체의 데이터까지 한 번에 불러오는 것이다.**
        - 실무에서 가장 사용하지 않아야 할 로딩방식
    - **지연로딩 : @xxToxx(fetch = fetchType.LAZY)** :  필요한 시점에 연관된 객체의 데이터를 불러오는 것이다.

  ⇒ 즉시 로딩에서는 member와 연관된 Team이 1개여서 Team을 조회하는 쿼리가 나갔지만, 만약 Member를 조회하는 JPQL을 날렸는데 연관된 Team이 1000개라면? Member를 조회하는 쿼리를 하나 날렸을 뿐인데 Team을 조회하는 SQL 쿼리 1000개가 추가로 나가게 된다.

  그러므로, 지연로딩을 사용하는 것이 좋다.

  ### 기대했던 sql문

    ```java
    SELECT * FROM user;
    ```

  ### JPA가 실행한 sql문

    ```java
    1. SELECT * FROM user;   (모든 유저 조회) ➔ 1번
    2. SELECT * FROM article WHERE user_id = 1; ➔ 1번
    3. SELECT * FROM article WHERE user_id = 2; ➔ 1번
    4. SELECT * FROM article WHERE user_id = 3; ➔ 1번
    ...
    ```

  ### 해결 방법

    - jpql에서 fetch join 사용!
        - 하드코딩을 막기 위해서는 `@EntityGraph`를 사용하면 된다.

    ```java
    @EntityGraph(attributePaths = {"articles"}, type = EntityGraphType.FETCH)
    List<User> findAllEntityGraph();
    ```

  출처

  [https://velog.io/@jinyoungchoi95/JPA-모든-N1-발생-케이스과-해결책](https://velog.io/@jinyoungchoi95/JPA-%EB%AA%A8%EB%93%A0-N1-%EB%B0%9C%EC%83%9D-%EC%BC%80%EC%9D%B4%EC%8A%A4%EA%B3%BC-%ED%95%B4%EA%B2%B0%EC%B1%85)

  [https://velog.io/@jinyeong-afk/기술-면접-즉시-로딩과-지연-로딩의-차](https://velog.io/@jinyeong-afk/%EA%B8%B0%EC%88%A0-%EB%A9%B4%EC%A0%91-%EC%A6%89%EC%8B%9C-%EB%A1%9C%EB%94%A9%EA%B3%BC-%EC%A7%80%EC%97%B0-%EB%A1%9C%EB%94%A9%EC%9D%98-%EC%B0%A8)
-  
- ## 기본 키 생성 전략

  JPA는 **기본 키를 직접 할당**하거나, **자동 생성**하는 두 가지 방식을 제공한다.
    
  ---

  ### 1️⃣ 직접 할당 방식

    - 개발자가 직접 기본 키를 지정하는 방식
    - `@Id`만 사용
    - `em.persist()` 호출 전에 **키를 반드시 직접 지정**해야 함

    ```java
    @Id
    private Long id;
    
    ```

  📌 사용 가능한 타입

  `int`, `long`, `String`, `Date`, `BigDecimal`, `BigInteger` 등
    
  ---

  ### 2️⃣ 자동 생성 방식 (`@GeneratedValue`)

  JPA가 **DB에 기본 키 생성을 위임**하거나 자체적으로 생성하는 방식

  → `strategy` 속성으로 전략 지정

    ```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    ```

  ### (1) **IDENTITY**

    - DB의 Auto Increment 기능 사용
    - MySQL, PostgreSQL 등에서 사용
    - **Insert 시점에 키 생성 → 즉시 DB에 저장됨**
    - ❌ 쓰기 지연(버퍼링) 불가능

  ### (2) **SEQUENCE**

    - DB의 Sequence 객체를 사용 (Oracle, PostgreSQL 등)
    - `@SequenceGenerator`로 시퀀스 등록
    - **persist() 시 시퀀스 조회 후 메모리에서 키 할당 → flush 시 저장**
    - `allocationSize`로 **시퀀스 호출 최소화** (기본 50)

  ### (3) **TABLE**

    - 별도의 테이블로 시퀀스 기능 흉내냄
    - `@TableGenerator` 사용
    - **SELECT + UPDATE 쿼리 2번 필요 → 성능은 다소 떨어짐**
    - `allocationSize`로 최적화 가능

  ### (4) **AUTO**

    - DB 방언에 따라 JPA가 자동 선택
        - MySQL → IDENTITY
        - Oracle → SEQUENCE
        - H2 → SEQUENCE
    - 개발 초기나 DB 미정 시 편리

  https://velog.io/@gillog/JPA-%EA%B8%B0%EB%B3%B8-%ED%82%A4-%EC%83%9D%EC%84%B1-%EC%A0%84%EB%9E%B5IDENTITY-SEQUENCE-TABLE