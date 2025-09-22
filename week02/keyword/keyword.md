- **SOLID**

  SRP: 하나의 클래스는 하나의 책임만을 가져야한다. 즉 한 클래스에서 여러기능을 수행하기 보다는 하나의 기능을 다루게 따로 구성해야한다는 의미이다.

  OCP: 기존의 구조 변경없이 기능을 확장 할 수 있어야 한다.

    ```java
    public class ShapeDrawer {
        public void drawCircle() {
            // 동그라미을 그리는 코드
        }
        public void drawSquare() {
            // 네모를 그리는 코드
        }
        public void drawTriangle() {
            // 세모를 그리는 코드
        }
    }
    ```

  위 예시 처럼 코드를 구성하게 되면  다른 도형을 추가하기 위해서 ShaperDrawer를 수정해야한다.

  즉, 전체적인 로직을 건드려야 함.

    ```java
    public interface Shape {
        void draw();
    }
    
    public class Circle implements Shape {
        @Override
        public void draw() {
            // 동그라미을 그리는 코드
        }
    }
    
    public class Square implements Shape {
        @Override
        public void draw() {
            // 네모을 그리는 코드
        }
    }
    
    public class Triangle implements Shape {
        @Override
        public void draw() {
            // 세모를 그리는 코드
        }
    }
    위 예시처럼 구성하면은 도형이 추가할때 내부적인 로직을 건드리지 않아도됨
    ```

  LSP:  프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀수 있어야한다. 즉 상위 클래스는 모든 클래스의 부분 집합이기에 최소한의 공통된 특성을 담아 구성해야한다는 것이다.

  예시) 펭귄은 조류이지만 날지 못함 조류 클래스를 구성할때는 날 수 있는 속성과 날지 못하는 속성을 따로 구성해야함

  ISP:  자신이 이용하지 않는 메소드에 의존하지 않아야한다. 즉 한 클래스에서 불필요한 기능을 강제로 구현하게 하지 않아야한다.

  DIP:어떠한 객체를 참조하기 위해서는 직접적인 참조가 아니라, 그 객체의 상위 요소를 참조해야한다. 이는 스프링의 DI와 구현되는 원칙으로 `UserRepository`라는 인터페이스에 의존하도록 만들면, 실제로 어떤 구현체가 들어올지는 스프링의 의존성 주입(DI)이 알아서 처리해주게 되고 그러면 서비스 로직은 DB 종류가 바뀌더라도 영향받지 않고 그대로 사용할 수 있게 된다는 장점이 있다.

- **DI**

  DI는 객체가 필요로 하는 의존성을 외부에서 주입해주는 방식으로 클래스가 스스로 객체를 new 로 만들지 않고 스프링 같은 외부 컨테이너가 대신 생성해서 넣어주는 방식이다. DI를 사용하게 되면 코드가 인터페이스에만 의존하고 실제 구현체는 외부에서 주입하면 되기에 변경에 유연하다는 장점이 있다.

- **IoC**

  객체의 생성과 관리 주체가 개발자가 아니라 외부 컨테이너로 넘어가는 개념으로 기존의 방식은 개발자가 new를 통해서 객체를 만들고 직접 제어를 하였지만 스프링 프레임워크에서는 대신 객체를 만들고 관리하기 때문에 제어의 흐름이 개발자가 아닌 컨테이너로 넘어갔다고 해서 제어의 역전으로 불린다.

- **생성자 주입 vs 수정자, 필드 주입 차이**

  생성자 주입 : 생성자를 통해 의존 관계를 주입 하는 방식으로 주입받은 객체가 변하지 않거나, 반드시 객체의 주입이 필요한 경우에 강제하기 위해서 사용 할 수 있다.

    ```java
    @Service
    public class UserService {
    
        private UserRepository userRepository;
        private MemberService memberService;
    
        @Autowired
        public UserService(UserRepository userRepository, MemberService memberService) {
            this.userRepository = userRepository;
            this.memberService = memberService;
        }
    
    }
    ```

  수정자 주입 : 수정자 주입은 필드 값을 변경하는 Setter 를 통해서 의존관계를 주입하는 방법으로 Setter 주입은 생성자 주입과 다르게 주입받는 객체가 변경될 가능성이 있는 경우 사용한다.

    ```java
    @Service
    public class UserService {
    
        private UserRepository userRepository;
        private MemberService memberService;
    
        @Autowired
        public void setUserRepository(UserRepository userRepository) {
            this.userRepository = userRepository;
        }
    
        @Autowired
        public void setMemberService(MemberService memberService) {
            this.memberService = memberService;
        }
    }
    ```

  필드 주입 : 필드 주입은 필드에 바로 의존관계를 주입하는 방식으로 코드가 간결해진다는 장점이 존재하지만 외부에서 접근하지 못하다는 단점도 존재한다. 필드 주입 방식은 필드의 객체를 수정할 수 없기 때문에 거의 사용되지 않는다.

    ```java
    @Service
    public class UserService {
    
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private MemberService memberService;
    
    }
    ```

- **AOP**

  스프링 프레임워크에서 제공하는 기능 중 하나로 관점 지향 프로그래밍을 지원하는 기술로 공통적인 요소를 모듈화 하여 코드 중복을 줄이고 유지 보수성을 향상하는데 도움을 준다. 여기서 말하는 관점 지향 프로그래밍은 메소드나 객체의 기능을 핵심 관심사와 공통 관심사로 나누어 프로그래밍 하는 방식으로 핵심 관심사는 각 객체가 가져야 할 본래의 기능이고 공통 관심사는 여러 객체에서 공통적으로 사용되는 코드이다. 즉 여러개의 클래스에서 반복해서 사용하는 코드는 공통관심사에 모듈화하여 분리하면 된다.

- **서블릿**

  동적 웹 페이지를 위하여 HttpRequest를 받아 처리하여 웹 페이지를 동적으로 생성하여 HttpResponse로 반환하는 자바 클래스로 여기서 서블릿은 하나의 기능만 수행하기에 동적인 웹 페이지를 위해서는 여러개의 서블릿 클래스가 있고 클라이언트에서는 웹 컨테이너를 사용하여 어떤 서블릿을 사용할지 결정한다. 이때 웹 컨테이너에서는 web.xml설정파일을 이용하고 설정파일에는 미리 어떤 사용자의 url에 어떤 서블릿을 호출할지 정의해놓기에 웹 컨테이너가 이를 활용하여 해당 서블릿을 생성하는 원리이다.