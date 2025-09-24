## SOLID
- 객체지향 설계의 5대 원칙으로, 유지보수성과 확장성을 높이는 핵심 원리
  1. S - 단일 책임 원칙 (SRP, Single Responsibility Principle)
     클래스는 오직 하나의 책임(변화 이유)만 가져야 한다.
  예: UserService가 비즈니스 로직만 담당하고, DB 접근은 UserRepository가 담당.

  2. O - 개방-폐쇄 원칙 (OCP, Open-Closed Principle)
         확장에는 열려 있고, 수정에는 닫혀 있어야 한다.

     인터페이스, 추상화 사용 → 새로운 기능 추가 시 기존 코드 수정 최소화.

  3. L - 리스코프 치환 원칙 (LSP, Liskov Substitution Principle)
         부모 타입 객체를 자식 타입으로 바꿔도 정상 동작해야 한다.
  예: Rectangle 대신 Square를 넣어도 문제없이 작동해야 함.

  4. I - 인터페이스 분리 원칙 (ISP, Interface Segregation Principle)
         클라이언트는 자신이 사용하지 않는 메서드에 의존하지 않아야 한다.
  예: Bird 인터페이스에 fly() 강제하지 말고, Flyable, Walkable 등 분리.

  5. D - 의존 역전 원칙 (DIP, Dependency Inversion Principle)
         고수준 모듈은 저수준 모듈에 의존하지 않고, 추상화에 의존해야 한다.
  예: UserService가 MysqlUserRepository 구체 클래스가 아니라 UserRepository 인터페이스에 의존.

## DI(의존성 주입)
- 객체가 사용할 의존성을 외부에서 주입받는 것.
- 장점: 결합도 낮아짐, 테스트 용이, 확장성 증가.
- 방식: 생성자 주입, 수정자(setter) 주입, 필드 주입이 대표적.

## IoC(제어의 역전)
- 프로그램의 제어 흐름을 개발자가 아닌 Spring Container 같은 외부 프레임워크가 관리하는 것. 
- 예: 객체 생성, 의존성 주입을 우리가 new 하지 않고, 스프링이 대신 관리.

## 생성자 주입 vs 수정자/필드 주입 차이
1. 생성자 주입 (Constructor Injection)
- 의존성을 생성자 파라미터로 받음.
- 장점: 불변성 보장(final 가능), 테스트 편리, 순환 참조 방지.


2. 수정자 주입 (Setter Injection)
- setter 메서드로 의존성 주입.
- 장점: 런타임에 변경 가능.
- 단점: 불변성 보장 어려움.

3. 필드 주입 (Field Injection)
- @Autowired를 필드에 직접 붙임.
- 장점: 코드 간단.
- 단점: 테스트 어렵고, DI 컨테이너 없으면 의존성 주입 불가 → 지양

## AOP (Aspect-Oriented Programming, 관점 지향 프로그래밍)
- 공통 관심사(부가 기능)를 핵심 로직과 분리하여 재사용하는 기법.
- 예: 로깅, 트랜잭션, 보안, 성능 모니터링 등.
- 핵심 비즈니스 로직에 반복되는 코드 섞이지 않게 함.
- Spring에서는 @Aspect, @Around, @Before 같은 어노테이션 사용.

## 서블릿 (Servlet)
- 자바 기반 웹 애플리케이션에서 HTTP 요청과 응답을 처리하는 클래스.
- 동작원리:
  - 1. 클라이언트가 URL로 요청 → 서블릿 컨테이너(Tomcat 등)가 요청 수신.
  - 2. HttpServletRequest 객체에 요청 정보 저장.
  - 3. 서블릿이 비즈니스 로직 처리 후 HttpServletResponse로 응답 작성.
  - 4. 클라이언트에 HTML/JSON 등 결과 반환.
- 역할: JSP/스프링 MVC 같은 웹 프레임워크의 기반

