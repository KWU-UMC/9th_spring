# 1. SOLID
## 정의

    - 변화에 유연하고 유지보수가 쉬운 코드 구조를 위해 객체지향 개발에서 자주 참조되는 5가지 설계 원칙
    - 어떤 언어/프레임워크에도 국한되지 않음, 요구사항 변화에 강한 소프트웨어 설계를 위한 가이드
 
---

## 원칙별 요약

  | 원칙 | 정의 | 체크포인트 |
      | --- | --- | --- |
  | **SRP - Single Responsibility Principle** | 하나의 클래스/모듈은 하나의 책임만 가져야 함 | 기능이 여러 개인가? 변경 시 여럿 수정해야 하나? |
  | **OCP - Open-Closed Principle** | 확장에는 열려 있고, 변경(수정)에는 닫혀 있어야 함 | 기존 코드 수정 없이 새 기능 추가 가능한 구조인가? |
  | **LSP - Liskov Substitution Principle** | 자식 클래스가 부모 클래스를 대체해도 정상 동작해야 함 | 부모 조건/예외/결과 호환되는가? |
  | **ISP - Interface Segregation Principle** | 클라이언트가 필요 없는 메서드가 없는 인터페이스 제공 | 인터페이스 기능이 사용자 목적에 맞추어 분리되었는가? |
  | **DIP - Dependency Inversion Principle** | 상위 모듈/저위 모듈 모두 추상화에 의존해야 함 | 구현체보다는 추상화 중심으로 설계되었는가? |
    
---

## 실무 팁

    - 모든 원칙을 무리해서 적용하면 오히려 복잡해질 수 있음
    - 코드를 리팩터링할 때 위 체크리스트 활용
    - 코드 리뷰 때 “요구사항 변경 → 영향 범위”를 생각해 설계 판단
    - 추상화, 인터페이스, 의존성 주입 등 패턴 활용 연습

---

# 2. DI

## 정의

    - 객체 지향 설계에서, 객체가 필요로 하는 다른 객체를 외부에서 주입(injection) 하여 의존성을 관리하는 기법
    - 내부에서 직접 생성하지 않고 외부에서 제공받음 → 결합도 낮추고 유지보수성/테스트 용이성 증가

---

  ## 주입 방식

- **생성자 주입 (Constructor Injection)**

      객체 생성 시점에 의존성 전달
        - 장점: 필수 의존성 보장, NPE 방지, 테스트 용이
        - 단점: 파라미터 많아지면 복잡
- **세터 주입 (Setter Injection)**

    객체 생성 후 setter 메서드로 의존성 설정
        - 장점: 선택적 의존성 처리 가능, 유연함
        - 단점: 주입 누락 시 NPE 가능성, 초기화 순서 제어 어려움
- **필드 주입 (Field Injection)**

    클래스 필드에 직접 주입
        - 장점: 코드 간단
        - 단점: 테스트 어려움, DI 컨테이너에 강한 의존 발생, 캡슐화 약화

  ---

  ## 장점

  1. **결합도 감소** – 객체 간 직접 의존 줄어듦
  2. **단위 테스트 용이** – Mock 쉽게 주입 가능
  3. **재사용성 증가** – 구현체 교체 시 영향 최소화
  4. **유연성 확보** – 런타임에 다른 구현체 주입 가능
  5. **프레임워크 활용** – Spring 등 DI 컨테이너 자동 관리

  ---

  ## 실무 팁 / 체크리스트

  - **필수 vs 선택 의존성 구분**: 필수는 생성자 주입, 선택은 세터 주입
  - **주입 시점 관리**: 의존성 누락/초기화 전 사용 방지
  - **컨테이너 의존 최소화**: 필드 주입 남발 지양
  - **구현체 관리 명확히**: 여러 구현체 존재 시 설정 확실히
  - **테스트 용이성 확보**: 모킹(Mock) 쉽게 할 수 있도록 경계 설계
  - **팀 내 DI 스타일 통일**: 생성자/세터/필드 주입 기준 합의

---

# 3. IOC

  ## 정의

    - 전통적 방식: 개발자가 객체 생성, 의존성 연결, 메서드 호출 순서 등 프로그램 흐름을 직접 제어
    - IoC: 이런 제어권을 프레임워크/컨테이너가 대신 관리하는 설계 원칙
    - 즉, 개발자는 “무엇(What)”을 할지 정의하고, “언제/어떻게(When/How)” 실행할지는 컨테이너가 담당

---

  ## 특징

    - 객체 생성, 라이프사이클 관리, 의존성 주입 등을 컨테이너(Spring IoC Container)가 대신 수행
    - 개발자는 구체적 제어 흐름이 아닌, **구성(설정)** 과 **의존 관계** 정의에 집중
    - 스프링에서는 Bean 등록 → 컨테이너가 Bean 생성·초기화·주입·소멸까지 관리

---

  ## 장점

    1. 결합도 감소 – 객체 간 강한 의존 줄어듦
    2. 유연성 – 런타임 시점에 구현체 바꾸기 쉬움
    3. 테스트 용이성 – Mock 객체 주입 가능
    4. 재사용성/유지보수성 향상 – 변경 시 파급효과 최소화

---

  ## IoC 구현 방식

    - DI (Dependency Injection): 외부에서 의존성 주입 (생성자/세터/필드 등)
    - Service Locator 패턴: 컨테이너에 의존성 요청 (요즘은 DI 방식이 주로 쓰임)

---

  ## 요약

  IoC는 **프로그램의 제어권을 개발자가 아닌 컨테이너가 관리하는 것**.

  DI는 그 IoC를 실현하는 대표적인 방법.

# 4. 생성자 주입 vs 수정자, 필드 주입 차이

## 1️⃣ 생성자 주입 (Constructor Injection)

- **방식**: 객체 생성 시점에 생성자를 통해 의존성을 모두 주입

    ```java
    @Service
    public class MemberService {
        private final MemberRepository memberRepository;
        
        @Autowired
        public MemberService(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    }
   ```

- **특징**
    - 객체가 생성될 때 필수 의존성을 반드시 초기화
    - `final` 필드 사용 가능 → **불변성(immutability)** 보장
    - Lombok `@RequiredArgsConstructor` 로 보일러플레이트 줄일 수 있음
- **장점**
    - NPE 발생 가능성 거의 없음 (의존성 누락 불가)
    - 의존성이 없으면 객체 자체를 만들 수 없음 → 강력한 안정성
    - 테스트 시 Mock 객체를 쉽게 주입 가능
    - 순환 참조 문제를 빠르게 드러내어 조기 발견 가능
- **단점**
    - 생성자 파라미터가 많아지면 복잡해질 수 있음 (→ 리팩터링 시 고려)

---

## 2️⃣ 수정자 주입 (Setter Injection)

  - **방식**: 객체 생성 후 `setter` 메서드를 통해 의존성을 주입

      ```java
      @Service
      public class MemberService {
          private MemberRepository memberRepository;
        
          @Autowired
          public void setMemberRepository(MemberRepository memberRepository) {
              this.memberRepository = memberRepository;
          }
      }
      ```

  - **특징**
      - 선택적(optional) 의존성 주입에 적합
      - 런타임에 의존성 변경 가능
  - **장점**
      - 유연성 높음 (테스트나 상황에 따라 다른 의존성 세팅 가능)
      - 의존성 주입 시점 제어 가능
  - **단점**
      - 의존성 누락 시에도 객체 생성은 가능 → NPE 위험
      - 필수 의존성 강제 불가
      - 객체의 상태가 불완전할 수 있음

  ---

  ## 3️⃣ 필드 주입 (Field Injection)

  - **방식**: `@Autowired` 를 이용해 필드에 직접 주입

      ```java
      @Service
      public class MemberService {
          @Autowired
          private MemberRepository memberRepository;
      }
      ```

  - **특징**
      - 코드가 가장 간결
      - 런타임 시 컨테이너가 리플렉션으로 필드에 바로 주입
  - **장점**
      - 코드 짧고 직관적
  - **단점**
      - DI 컨테이너 없이는 테스트하기 힘듦
      - 의존성 구조가 명시적으로 드러나지 않아 파악 어려움
      - 불변성, 캡슐화 깨짐
      - **순환 참조 문제** 발생 시 원인 추적이 까다로움

  ---

  ## 생성자 vs 수정자(Setter) vs 필드 주입 비교

  | 구분 | 생성자 주입 | Setter 주입 | 필드 주입 |
    | --- | --- | --- | --- |
  | **주입 시점** | 객체 생성 시 | 객체 생성 후 | 런타임(리플렉션) |
  | **의존성 필수성** | ✅ 필수 강제 가능 | ❌ 강제 불가 | ❌ 강제 불가 |
  | **불변성 보장** | ✅ 가능 (`final`) | ❌ 불가능 | ❌ 불가능 |
  | **테스트 용이성** | ✅ 용이 | ✅ 용이 | ❌ 어렵다 |
  | **코드 간결성** | 중간 | 다소 장황 | 가장 간단 |
  | **권장 여부** | ⭐️ 권장 | 보조적 사용 | 지양 |
    
  ---

  ## 결론

  - **생성자 주입**: 기본 선택, 가장 안전하고 권장되는 방식
  - **Setter 주입**: 선택적 의존성이나 테스트/상황별 변경 필요 시 사용
  - **필드 주입**: 코드가 짧지만 테스트와 유지보수에 불리해 지양

  → 따라서 **Spring에서는 생성자 주입을 기본으로 사용하고, Setter 주입은 특수한 경우 보조적으로, 필드 주입은 되도록 피하는 것이 좋다.

---

# 5. AOP

  ## 개념

    - AOP = 관점 지향 프로그래밍 (Aspect Oriented Programming)
    - 공통 관심사(cross-cutting concern)를 비즈니스 로직과 분리해 모듈화
    - 핵심 로직은 본연의 책임만 수행하고, 로깅, 트랜잭션, 보안, 캐싱 등의 부가기능을 Aspect로 분리함
    - 흩어진 관심사(aspects)들을 횡단으로 끼워 넣는 방식

---

  ## 주요 용어

  | 용어 | 의미 |
      | --- | --- |
  | **Aspect** | 부가기능(로깅, 보안, 트랜잭션 등)을 모듈화한 것 |
  | **Target** | Aspect가 적용될 대상 클래스 또는 메서드 |
  | **Advice** | 실제 실행되는 부가기능 로직 |
  | **JointPoint** | Advice가 개입 가능한 지점 (메서드 호출, 생성자 실행 등) |
  | **PointCut** | JointPoint 중에서 Advice를 적용할 구체적인 조건이나 범위 지정 |
  | **Weaving** | Advice를 타겟 메서드에 적용(끼워넣기)하는 과정 |
    
---

  ## 스프링 AOP 특징

    - 프록시 기반 구현으로 동작
    - 스프링 빈(bean) 객체에만 AOP 적용 가능
    - IoC 컨테이너와 연동되어 빈 생성/관리 흐름에 통합됨
    - 모든 JointPoint를 지원하는 것은 아니고, 일반적으로 메서드 실행 지점 중심

---

  ## Advice 종류 / 실행 시점

    - `@Before` : 타겟 메서드 실행 전에 실행
    - `@After` : 메서드 실행 완료 후 (성공/예외 관계없이)
    - `@AfterReturning` : 메서드가 정상적으로 반환된 이후
    - `@AfterThrowing` : 메서드 실행 중 예외가 발생했을 때
    - `@Around` : 메서드 실행 전후 모두 감쌈 (Advice 내부에서 `proceed()` 호출)

---

  ## 스프링 AOP 사용 예시

```java
@Aspect
@Component
public class AnnotationAspect {
    
    @Around("@annotation(MyLog)")
    public Object aroundWithAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        // MyLog 어노테이션 붙은 메서드에만 적용
        return pjp.proceed();
    }
}
  ```
---
# 6. 서블릿

  ## 정의

    - 클라이언트 요청을 받아 처리하고, 응답을 돌려주는 자바 기반 웹 프로그래밍 기술

---

  ## 특징

    - 클라이언트 요청에 대해 동적으로 동작하는 웹 컴포넌트
    - `HttpServlet` 클래스를 상속해서 사용함
    - Java thread를 이용해 요청들을 병렬 처리

---

  ## 동작 흐름

    1. 클라이언트가 URL 요청 → HTTP Request가 서블릿 컨테이너로 전달됨
    2. 컨테이너가 `HttpServletRequest` / `HttpServletResponse` 객체 생성
    3. URL 매핑 정보(web.xml 또는 annotation 기반)로 어떤 서블릿이 요청을 처리할지 결정
    4. 해당 서블릿의 `service()` 메서드가 호출되고, 내부에서 `doGet()` 또는 `doPost()` 등이 실행됨
    5. 응답 생성 후 `HttpServletResponse` 객체로 클라이언트에게 전송
    6. 요청 처리 끝나면 Request/Response 객체 소멸됨

---

  ## 서블릿 컨테이너(Servlet Container)

    - 서블릿을 관리하고 실행 환경을 제공하는 서버 쪽 컴포넌트 (예: Tomcat)
    - 역할
        - 웹서버와의 통신 연결
        - 서블릿의 생성, 초기화, 소멸 생명주기 관리
        - 멀티 쓰레드 요청 처리 관리
        - 선언적 보안 처리 등

---

  ## 생명 주기 (Lifecycle)

    1. 서블릿 인스턴스 생성 / `init()` 호출
        - 최초 요청 시 또는 서버 시작 시 로드
        - `init()`은 한 번만 호출됨
    2. `service()` → `doGet()` / `doPost()` 호출
        - 클라이언트 요청이 들어올 때마다 `service()` 호출
        - HTTP 메서드(GET, POST 등)에 따라 적절한 메서드 반환 실행
    3. `destroy()` 호출 및 소멸 처리
        - 컨테이너가 종료되거나 서블릿이 제거될 때 `destroy()` 실행
        - 자원 해제 또는 정리 작업 수행

---

  ## JSP와의 관계

    - JSP는 내부적으로 컴파일되어 서블릿 클래스로 변환됨
    - JSP는 HTML 중심에 자바 코드를 삽입하는 방식 → 최종적으로는 서블릿이 실행됨