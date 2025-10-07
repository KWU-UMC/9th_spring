## 계층형 구조 vs 도메인 구조
- 계층형 구조
  - 전통적인 소프트웨어 구조 
  - Presentation -> Service -> Repository -> Domain
  - 관심사 분리, 유지보수 용이
  - 단점: 계층 간 호출이 많으면 성능 저하 가능
- 도메인형 구조
  - Domain 중심 설계
  - Entity, Value Object, Aggregate, Repository
  - 비즈니스 로직 도메인 안에 집중
  - 복잡한 비즈니스 규칙 구현에 유리


## JPA
- Java 표쥰 ORM 인터페이스
- 객체와 RDB 테이블 매핑
- 주요 기능
  - Entity 관리 (영속성 컨텍스트)
  - CRUD 지원
  - JPQL을 통한 객체지향 쿼리
  - 연관관계 매핑(OneToMany, ManyToOne)
- SQL 직접 작성 최소화, DB 독립성



## N+1 문제
- 1개의 쿼리로 부모 엔티티 조회 -> 연관된 자식 엔티티 조회 시 N번 추가 쿼리 발생
  - 회원 10명 조회 후 각 회원의 주문 조회 -> 1 + 10 = 11번 쿼리 발생
- 해결 방법
  - Fetch Join
  - Batch Fetch Size 설정
  - EntityGraph 사용
- 성능 저하, DB 부하 증가



## 기본 키 생성 전략
- IDNETITY 
  - DB에서 자동 증가
  - 장점: 간단하고 즉시 PK 확보 가능
  - 단점: Batch Insert에 불리, 영속성 컨텍스트 flush 필요
- SEQUENCE 
  - DB 시퀀스 사용
  - 장점: Batch Insert 효율적, 미리 PK 확보 가능
- TABLE 
  - 키 전용 테이블에서 생성
  - DB 독립적이지만 성능 저하 가능
- AUTO 
  - DB 벤더에 따라 자동 선택
- 용도 : 엔티티 식별 및 참조 무결성 유지
- 개발자 PK 관리 불필요, 안정적