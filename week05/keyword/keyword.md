# WEEK 5 - 💧나미/이나영
## 지연로딩과 즉시로딩의 차이 
    JPA에서 데이터 조회 시의 두 가지 방식
|       | 지연로딩 (Lazy Loading) | 즉시로딩 (Eager Loading)                     |
|-------|---------------------|------------------------------------------|
| 로딩 시점 | 데이터를 실제로 사용할 때      | 데이터 조회 시점에, 연관된 모든 객체의 데이터까지 한 번에 불러오는 것 |
|성능|초기 로딩은 빠르나, 연관 데이터를 사용할 때 추가 쿼리 발생 가능|초기 로딩은 느릴 수 있으나, 연관 데이터를 사용할 때 추가 쿼리 없음|
 | 장점    |필요한 데이터만 가져오므로 성능 최적화 가능 |연관된 데이터를 즉시 확인할 수 있음|
|단점|실제 데이터를 사용하려고 할 때 추가 쿼리가 발생할 수 있고, @Transactional 어노테이션 등 별도 처리가 필요할 수도 있음| 불필요한 데이터까지 로드해 성능 저하를 유발할 수 있음. N+1문제 발생가능|
## Fetch Join
### 기능 
    - JPQL에서 성능 최적화를 위해 제공하는 기능
    - 연관된 엔티티나 컬렉션을 한 번에 같이 조회할 수 있는 기능
### 명령어 
    JOIN FETCH
### 특징 
    - 글로벌 로딩 전략보다 우선 적용됨 -> LAZY로 설정돼 있어도 Fetch JOIN을 사용하면 즉시 로딩됨
    - 객체 그래프 유지에 효과적 -> 연관된 엔티티를 함께 가져와 엔티티 간 관계를 그대로 유지
    - DTO 조회에는 부적합 -> 여러 테이블을 조인해 특정 형태의 데이터를 만들 때는 일반 조인 + DTO 사용이 더 효율적
### 한계
    - 별칭 사용 불가 -> SELECT, WHERE, 서브쿼리 에서 FETCH JOIN 대상을 직접 사용할 수 없음
    - 다중 컬렉션 fetch 불가 -> 컬렉션 x 컬렉션 형태로 조회 시 데이터 중복 문제 발생
    - 페이징 불가능
    - 단일값 연관(@ManyToOne, @OneToOne)은 페이징 사용 가능

## @EntityGraph
    연관관계가 지연 로딩으로 되어있을 경우 fetch 조인을 사용해 여러 번의 쿼리를 한 번에 해결가능하다.
    @EntityGraph는 Data JPA에서 fetch 조인을 어노테이션으로 사용할 수 있도록 만들어준 기능
### 예시 시나리오
    Member ↔ Team : 다대일(@ManyToOne) 관계
---
    // 엔티티 정의
    @Entity
    public class Member {
        @Id @GeneratedValue
        private Long id;
    
        private String username;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    
        // getters, setters
    }
    
    @Entity
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
        
        // getters, setters
    }
---
    // Repository Interface
    public interface MemberRepository extends JpaRepository<Member, Long> {

        // EntityGraph를 사용해서 Member와 연관된 Team을 한 번에 가져옴
        @EntityGraph(attributePaths = {"team"})
        @Query("select m from Member m where m.username = :username")
        Member findWithTeamByUsername(@Param("username") String username);
    }
---
    // 사용 예
    @Service
    @RequiredArgsConstructor
    public class MemberService {
    private final MemberRepository memberRepository;
    
        public void printMember(String username) {
            Member member = memberRepository.findWithTeamByUsername(username);
    
            // Lazy 초기화 없이 바로 접근 가능 (이미 함께 조회됨)
            System.out.println("member = " + member.getUsername());
            System.out.println("team = " + member.getTeam().getName());
        }
    }

## commit과 flush 차이점
|            | Commit                                          | Flush                                                                                               |
|------------|-------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| 역할         | 트랜잭션을 최종적으로 확정함                                 | 영속성 컨텍스트의 변경 사항을 데이터베이스에 동기화함 (일시반영)                                                                |
| 작업 방식      | flush 작업이 완료된 후, <br>DB에 대한 모든 변경 사항을 영구적으로 저장함 | 1. 영속성 컨텍스트의 변경 내용을 감지<br>2. 변경된 엔티티에 대한 SQL을 생성하여 쓰기 지연 SQL 저장소에 저장함<br>3. 저장된 SQL 쿼리들을 DB로 전송해 실행 |
| 되돌리기 가능 여부 | 불가능 (트랜잭션 종료)                                   |가능 (트랜잭션 내에서 롤백)|
|관계| flush를 먼저 수행한 후 최정 완료                           |commit 전에 내부적으로 수행됨|
## QueryDSL, OpenFeign의 QueryDSL
|| QueryDSL                                                       | OpenFeign QueryDSL                           |
|-|----------------------------------------------------------------|----------------------------------------------|
|기능| 문자열 기반 쿼리 대신 자바 코드로 SQL을 작성할 수 있게 해주는 프레임워크                    | 기존 QueryDSL을 포크하여 OpenFeign 진영에서 활발하게 유지보수하는 라이브러리 |
|특징| 정적 타입 체크로 컴파일 시점에 오류를 발견할 수 있음<br>SQL 문법을 그대로 코드로 작성할 수 있어 직관적 | 기존 QueryDSL의 모든 기능을 포함함                      
|장점||최신 기술에 대한 호환성 제공<br>보안 취약점을 해결해 더 안전한 코딩 지원|
|단점| 프로젝트의 공식적인 업데이트가 멈춰서 최신 기술이나 보안 문제에 대한 지원이 부족함                 |                                              |
## N+1 문제 해결할 수 있는 여러 방안들
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

## 영속 상태의 종류 
### 영속 상태
| 상태               |설명|
|------------------|-|
| 영속 상태            |영속성 컨텍스트에서 관리되는 상태로, 데이터베이스의 영구 저장소에 저장될 수 있음|
| 준영속 상태 Detached  |영속성 컨텍스트에서 분리된 상태로, 더 이상 컨텍스트의 관리를 받지 않음|
| 비영속 상태 Transient |영속성 컨텍스트와 관련 없는, 순수한 자바 객체 상태|

### 준영속 상태와 비영속 상태의 차이점
    // 비영속 상태
    Member member = new Member();  // 비영속 상태
    member.setName("나영");
    → 이 시점의 member는 단순히 메모리에 존재할 뿐, DB나 영속성 컨텍스트에 아무런 영향 없음.
---
    // 준영속 상태
    Member member = em.find(Member.class, 1L); // 영속 상태
    em.detach(member);                         // 준영속 상태
    member.setName("수정됨");                  // DB에는 반영되지 않음
    -> DB에 존재하는 데이터지만 현재 EntityManager가 관리하지 않음