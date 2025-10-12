# WEEK 4 - 💧나미/이나영
## 계층형 구조 vs 도메인 구조

    계층형 구조 (Layered Structure)
    : 애플리케이션에서 사용하는 계층별로 패키지를 구성하는 방식
    
    장점 
    1. 역할 분리가 명확하다 : 각 계층이 명확한 역할을 가져서 코드를 이해하기 쉬움      
    2. 테스트가 편리하다 : 각 계층별로 독립적인 테스트가 가능해 편리함
    3. 익숙하다 : 대중적임
    
    단점
    1. 계층 간 의존성 : 한 계층의 변경이 다른 계층에 영향을 미칠 수 있음
    2. 복잡성이 증가한다 : 큰 프로젝트의 경우 계층의 수가 늘어나 복잡해질 수 있음
    3. 비즈니스 로직의 분산 : 비즈니스 로직이 여러 계층에 나뉘어 있을 수 있음

[예시]

    controller      // 프레젠테이션 계층
	⎿ ProductController
	⎿ MemberController
	⎿ CartController

    service         // 서비스 계층
    ⎿ ProductService
    ⎿ MemberService
    ⎿ CartService
    
    repository      // 리포지토리 계층
    ⎿ ProductRepository
    ⎿ MemberRepository
    ⎿ CartRepository

    model          // 도메인 계층
    ⎿ Product
    ⎿ Member
    ⎿ Cart


---
    도메인형 구조 (Domain-Oriented Structure)
    : 비즈니스 로직에 중점을 둔 아키텍처
    
    장점
    1. 비즈니스 중심이다 : 비즈니스 로직이 명확히 드러나 이해하기 쉬움
    2. 유연하다 : 새로운 기능 추가 시 독립적으로 추가할 수 있음
    3. 변경에 강하다 : 한 도메인의 변경이 다른 도메인에 영향을 미치지 않음

    단점
    1.초기 학습 곡선 : 익숙하지 않으면 배우기 어려울 수도 있음
    2. 작은 프로젝트에 부적합 : 작은 프로젝트에선 부적절하게 복잡해 보일 수 있음
    3. 도메인 간 협력 어려움 : 여러 도메인이 협력해야할 때 구조가 복잡해질 수 있음
[예시]

    product
	⎿ controller
	⎿ service
	⎿ dao
	⎿ dto

    member
    ⎿ controller
    ⎿ service
    ⎿ dao
    ⎿ dto
    
    cart
    ⎿ controller
    ⎿ service
    ⎿ dao
    ⎿ dto
---
    Q. 어떤 구조를 선택해야 할까?
    계층형 : 전반적인 애플리케이션의 구조를 이해하기 쉬운 전통적이고 안정적인 방식 선호 시
    도메인형 : 각 도메인에 대한 의존성을 줄이고, 도메인 자체의 응집도를 높여 유지보수성과 확장성을 높이고 싶을 때
    
## JPA 
    Java Persistence API , 자바 진영의 ORM(Object-Relational Mapping) 기술 표준
    객체 지향 애플리케이션의 데이터를 관계형 데이터베이스에 영구적으로 저장하고 관리하는 방식을 정의하는 인터페이스 모음


    주요 특징 
    1. 표준화 : 자바 ORM 기술의 표준으로서, 다양한 ORM 프레임워크에 적용할 수 있음
    2. 객체와 데이터베이스 매핑 : 자바의 객체를 데이터베이스 테이블의 행으로, 객체의 필드를 테이블의 컬럼으로 매핑해
                                개발자가 객체 중심으로 데이터를 다룰 수 있게 함
    3. 추상화 : 데이터베이스 연동에 필요한 복잡한 SQL 쿼리 작성 대신, 객체 지향적인 방식으로 데이터를 처리할 수 있도록 추상화해 제공함
    4. 구현체 : 실제 JPA 표준을 구현하는 ORM 프레임워크를 통해 DB와 상호작용함

## N + 1 문제
    ORM 기술에서 특정 객체를 대상으로 수행한 쿼리가 
    하나의 엔티티를 조회할 때, 연관관계를 조회하게 되면서 N번의 추가 쿼리가 발생하는 현상

[예시 상황]

    // Member와 Team은 다대일 관계
    @Entity
    class Member {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    }
[예제 코드]

    List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                         .getResultList();

    for (Member member : members) {
        System.out.println(member.getTeam().getName());
    }
[실행되는 SQL]

    -- 1) Member 전체 조회
    SELECT * FROM member;
    
    -- 2) 각 Member의 Team 조회 (N번)
    SELECT * FROM team WHERE team_id = 1;
    SELECT * FROM team WHERE team_id = 2;
    SELECT * FROM team WHERE team_id = 3;
    ...

-> 이렇게 총 1 + N 번의 쿼리가 발생해서 성능이 급격히 나빠짐

[원인]

    1. 지연 로딩 (Lazy Loading) | @ManyToOne(fetch = FetchType.LAZY)로 설정된 관계에서 실제 접근할 때마다 쿼리를 날림
    2. JPQL 기본 동작           | 연관 객체를 즉시 join 안함
    3. 컬렉션 조회 시 반복 접근   | for문, stream 등에서 연관 엔티티 접근 시 추가 쿼리 발생
[해결방안]    
    
    1. Fetch Join
        JPQL에 join fetch 추가해서 한 번에 연관 데이터 로딩 

    장점: 쿼리 1번으로 Member + Team 한 번에 로드
    단점: 페이징 시 @OneToMany 관계에서는 문제 발생할 수 있음

    List<Member> members = em.createQuery(
    "SELECT m FROM Member m JOIN FETCH m.team", Member.class)
    .getResultList();

    실행되는 SQL
    SELECT m.*, t.*
    FROM member m
    JOIN team t ON m.team_id = t.id;
---
    2. EntityGraph 사용 (Spring Data JPA) 
        리포지토리 메서드에서 @EntityGraph 로 fetch join 효과를 낼 수 있음
    
    장점: JPQL 수정 없이 fetch join 효과, 깔끔하고 유지보수성 높음
    단점: 단일 쿼리 한정임

    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT m FROM Member m")
    List<Member> findAllWithTeam();

---
    3. Batch Size (IN 절 최적화)
        @BatchSize 혹은 hibernate.default_batch_fetch_size 설정
        이렇게 하면, 지연 로딩은 유지하면서 N번의 쿼리를 IN 절로 묶어 실행함

    장점: Lazy 유지하면서 성능 개선
    단점: 완벽히 1쿼리로 줄진 않음

    @BatchSize(size = 100)
    private List<Member> members;
    or
    spring:
        jpa:
            properties:
                hibernate.default_batch_fetch_size: 100

---
    즉시 로딩(EAGER)같은 방안은 간단하나, 모든 상황에서 join, 예측 불가, 성능 불안정 등의 이유로 비추
## 기본 키 생성 전략 
    1. 직접 할당 방식
        애플리케이션에서 기본 키를 개발자가 직접 할당하는 방식

        방법 : Entity 생성 시 Key Column에 @Id 를 사용하면 됨!  
                em.persist()로 Entity를 저장하기 전, Application에서 직접 기본 키를 할당해줘야 함..

    2. 자동 생성 방식
        DB에 할당을 위임하거나 기본 키를 생성해 주는 별도의 수단을 통함
    
        방법 : @Id와 @GeneratedValue를 사용
        1. IDENTITY
            DB가 AUTO_INCREMENT으로 기본키를 자동 생성
            장점 : 설정이 간단하고, MySQL과 잘 맞으며 성능 안정적
            단점 : 영속성 컨텍스트에 ID 미리 없음, 대량 insert 불리

        2. SEQUENCE 
            DB의 시퀀스 객체를 통해 ID 값을 미리 가져옴
            장점 : Insert 전에 ID를 미리 알 수 있어, 영속성 컨텍스트 관리가 효율적임
                    AlloctionSize로 ID를 미리 캐싱하면 성능 향상됨
            단점 : MySQL처럼 시퀀스가 없는 DB에서는 사용이 불가함

        3. TABLE 
            별도의 키 저장용 테이블
            장점 : DB가 독립적임 -> 시퀀스 기능이 없는 DB 환경에서도 사용할 수 있음
            단점 : ID를 가져오기 위해 매번 select -> update 쿼리 2번 수행해야함
                    성능 저하 가능성 존재

        4. AUTO 
            JPA가 DB에 맞게 IDENTITY/SEQUENCE/TABLE 중 자동 선택
            
    3. 기타 방식 
        UUID : Universally Unique Identifier, 전 세계적으로 고유한 128비트 식별자를 생성
        ULID : Universally Unique Lexicographically Sortable Identifier, 시간 동기화 식별자로 UUID보다 짧고 시간순으로 정렬 가능