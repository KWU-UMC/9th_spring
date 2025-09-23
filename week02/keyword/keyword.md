# WEEK 1 - 💧나미/이나영
## 🔑 핵심 키워드
### SOLID
    <<객체 지향 설계의 5원칙 SOLID!!>>
    S : SRP 단일 책임 원칙 (Single responsibility principle)
    O : OCP 개방-폐쇄 원칙 (Open/closed principle)
    L : LSP 리스코프 치환 원칙 (Liskov substitution principle)
    I : ISP 인터페이스 분리 원칙 (Interface segregation principle)
    D : DIP 의존관계 역전 원칙 (Dependency inversion principle)

    SPR : 한 클래스(객체)는 단 하나의 책임만 가져야 한다. 
         -> 하나의 클래스는 하나의 기능을 담당하여 하나의 책임을 수행하는데 집중되도록
            클래스를 따로따로 여러개 설계하라. 
            프로그램의 유지보수성을 높이기 위한 설계 기법
    OCP : 소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.
         -> 기능 추가 요청 시, 유연하게 코드를 추가함으로써 애플리케이션의 기능을 확장하고
            객체를 직접적으로 수정하는 건 제한.
            다형성과 확장을 가능케하는 설계 기법
    LSP : 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다. 
         -> 서브 타입은 언제나 부모 타입으로 교체할 수 있어야 함
            다형성 원리를 이용하기 위한 원칙..
    ISP : 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
         -> 인터페이스를 사용하는 클라이언트를 기준으로 분리함으로써, 
            클라이언트의 목적과 용도에 적합한 인터페이스 만을 제공하는 것이 목표.
            한 번 인터페이스를 분리해 구성해놓고, 나중에 수정사항이 생겨서 또 분리해선 안됨
    DIP : 프로그래머는 추상화에 의존해야지, 구체화에 의존하면 안된다. 
         -> 어떤 Class를 참조해서 사용해야하는 상황이 생긴다면, 
            그 Class를 직접 참조하는 것이 아니라 그 대상의 상위 요소(추상 클래스/인터페이스)로 참조하기.
### DI
    의존성 주입 Dependency Injection : 객체가 필요로 하는 것(종속성)을 직접 만들지 않고 외부에서 주입받는 방식    

    Q. 의존성(Dependency)이란?
    A. 한 객체가 다른 객체를 사용할 때 의존성이 있다고 합니다. . .
    ~~> 그리고 두 객체 간 관계(의존성)를 맺어주는 것을 의존성 주입이라고 한다!!!!

    Q. 의존성 주입이 필요한 이유?
    A. 강하게 결합된 클래스들 분리, 객체 간의 관계를 결정해 줌으로써 
       결합도를 낮추고 유연성을 확보하기 위함! (이는 상속보다 훨씬 유연)
    
    다양한 의존성 주입 방법
    1. 생성자 주입 Constructor Injection
    2. 수정자 주입 Setter Injection
    3. 필드 주입 Field Injection
    4. 일반 메소드 주입 Method Injection
### IoC
    제어의 역전 Inversion of Control : 프로그램의 제어 흐름을 직접 제어하는 게 아닌, 외부에서 관리하는 것!
    
    ~ 대부분의 소프트웨어는 개발자가 코드의 흐름을 **제어** (직접 객체 생성, 메서드 호출, ...)
    ~ But, 시스템이 커지면 유지보수와 확장이 어려워짐 ㅠㅠ! 
    ~ 따라서! 위와 같은 문제를 해결하기 위해 등장한 것. 
    
    스프링이 IoC를 구현하는 곳
    1. 객체 생성과 관리
    2. 의존성 주입
    3. 애플리케이션의 생명 주기 관리
    4. AOP(관점 지향 프로그래밍)
    5. 웹 요청 처리 

    장점 : 유연성과 유지보수성이 좋아짐. 테스트 용이성 또한 크게 향상됨
### 생성자 주입 VS 수정자, 필드 주입 차이
    생성자 주입 Constructor Injection : 생성자를 통해 의존 관계를 주입하는 방법 (필수 의존성)
    예제 
    @Component
    class ServiceA {
        private final Repository repo;
    
        // 생성자 주입
        public ServiceA(Repository repo) {
            this.repo = repo;
        }
    
        public void doSomething() {
            repo.save();
        }
    }
    
    @Component
    class Repository {
        public void save() {
            System.out.println("Saved!");
        }
    }

    수정자 주입 Setter Injection : 필드 값을 변경하는 setter를 통해 의존 관계를 주입하는 방법 (선택적 의존성)
    예제
    @Component
    class ServiceB {
        private Repository repo;
    
        // setter 주입
        @Autowired
        public void setRepository(Repository repo) {
            this.repo = repo;
        }
    
        public void doSomething() {
            repo.save();
        }
    }

    필드 주입 Field Injection : 필드에 의존관계를 주입하는 방법 (테스트/유지보수 어려워서 거의 지양)
    예제
    @Component
    class ServiceC {
        @Autowired
        private Repository repo;
    
        public void doSomething() {
            repo.save();
        }
    }

### AOP
    관점 지향 프로그래밍 Aspect-Oriented Programming 
    : OOP를 보완하며, 횡단 관심사를 분리하여 모듈성을 높이는 프로그래밍 패러다임
    **AOP의 주요 개념**
    1. 관점 Aspect : 부가 기능과 그 적용처를 정의하고 하나의 모듈로 합친 것
        Asepect = Advice + Pointcut
        Advice : 실제 동작 코드
        Pointcut : 작성한 advice가 실제로 적용된 메소드 
        따라서, 부가기능을 나타내는 공통 관심사에 대한 추상적인 명칭 
    2. 횡단 관심사 Cross-Cutting Concern : 애플리케이션의 여러 모듈에서 공통적으로 필요한 기능
    3. 핵심 로직 Core Logic : 개발자가 구현하고자 하는 주요 비즈니스 로직
    4. 모듈화 : 핵심 로직과 횡단 관심사를 분리해 각각 별개의 모듈로 관리하는 것 

    AOP의 장점
    1. 코드 중복 감소 : 횡단 관심사를 모듈화함으로써 중복되는 코드가 줄어든다. 
    2. 유지보수성 향상 : 공통 기능은 한 곳에서 관리 -> 수정 필요 시 해당 모둘만 변경.
    3. 핵심 로직 집중 
    4. 모듈성 증대 : 애플리케이션의 구조 깔끔•체계 관리
    
    Cross - cutting Concern
    1. 로깅(Logging)
    2. 보안(Security)
    3. 트랜잭션 관리 
### 서블릿
    Servlet : 웹 애플리케이션(WAS)에서 요청을 처리하는 자바 클래스 
    -> 사용하는 이유 : WAS가 비즈니스 로직 처리만을 수행할 수 있는 환경을 제공하기 때문. 
    -> 역할 : WAS가 수행하는 HTTP 처리 및 가공 작업
             (웹 서버로부터 받은 HTTP request를 parsing하고 결과물을 response로 parsing하는 것)을 대신 수행함
    -> 장점 : 개발자가 HTTP 메시지를 처리하는 로직을 작성할 필요가 없어져, 
              WAS의 비즈니스 로직을 작성하는 것에만 집중할 수 있게 됨. -> 효율 UP!