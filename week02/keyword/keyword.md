# 🎯 핵심 키워드
# SOLID
    - S (Single Responsibility Principle, 단일 책임 원칙) : 하나의 클래스는 하나의 책임만 가져야 한다.
        - 하나의 책임을 위해 여러 메소드가 사용될 수도 있지만 클래스가 이들을 통해 수행하는 대표적인 업무는 하나여야 한다.
    - O (Open/Closed Principle, 개방-폐쇄 원칙) : 확장에는 열려 있고, 수정에는 닫혀 있어야 한다.
        - 인터페이스를 만들고 메소드의 이름과 형태만 정의한 뒤 각 클래스가 인터페이스 기반 메소드 오버라이딩으로 기능을 쉽게 확장할 수 있음.
    - L (Liskov Substitution Principle, 리스코프 치환 원칙) : 자식 클래스는 언제나 부모 클래스 타입으로 교체 가능해야 한다.
        - 부모클래스에서는 정상 동작하던 메소드가 자식 클래스에는 예외 발생 ⇒ 틀린 설계
            - 독립된 인터페이스로 두어 별개의 클래스를 설계하는 것이 맞다.
    - I (Interface Segregation Principle, 인터페이스 분리 원칙) : 특정 클라이언트를 위한 작은 인터페이스 여러 개가, 범용 인터페이스 하나보다 낫다.
        - 클래스는 자신이 사용하지 않을 메소드를 구현하도록 강요받지 말아야 한다는 것이다.
        - 인터페이스를 기능별로 쪼개서 구현하고 메소드에 인터페이스를 다중 상속하는 방식
    - D (Dependency Inversion Principle, 의존 역전 원칙) : 구체 클래스가 아니라 추상(인터페이스)에 의존해야 한다.
# DI

  ### **의존성 주입 = Dependency Injection = DI**

  의존성이란 **파라미터나 리턴값 또는 지역변수 등으로 다른 객체를 참조하는 것(부품의 개념)**

  Spring Bean(Controller, Service, Repository)를 스프링 컨테이너에서 관리하게 된다.

    - 클래스와 클래스 간의 연관관계(Controller, Service)
- # IoC

  ### **IoC (Inversion Of Control) = 제어의 역전**

    - 객체의 생성, 생명주기, 관리까지 객체의 주도권을 프레임워크가 관리하는 방식
    - Spring 에서 IoC를 담당하는 컨테이너 = spring container(IoC Container)

  ### **IoC 구현 방법 4가지**

    - Factory Pattern : 객체 생성을 전담하는 팩토리 클래스를 두어 객체를 생성하는 방식
    - Template Method Pattern : 객체 생성 과정을 추상화하여, 하위 클래스에서 구체적인 구현을 담당하는 방식
    - Service Locator Pattern : 객체 생성 및 관리를 위한 서비스 위치자 패턴
    - **Dependency Injection (DI)** : 객체 생성 및 관리에 대한 책임을 IoC 컨테이너가 가지며, 필요한 객체를 직접 생성하거나 외부에서 주입받는 방식(생성자 주입, 속성 주입, 메소드 주입 등 다양한 방식으로 이루어짐)

  ⇒ 이 중에서 Spring Framework 및 Spring Boot는 DI (Dependency Injection) 방식을 IoC 구현 방식으로 채택하고 있다.

  ### **IoC 장점**

    - 의존성을 관리하기 쉬워져 코드 변경이 쉬워진다.
    - 의존성 주입을 통해 테스트용 객체를 쉽게 만들 수 있으므로 테스트 코드 작성에 유용하다.
    - 의존성을 관리하는 작업이 자동화되니, 객체 간의 결합도가 낮아지므로 유지보수에 용이해진다.

  출처 : [https://velog.io/@kwontae1313/제어-역전IoC과-의존성-주입DI](https://velog.io/@kwontae1313/%EC%A0%9C%EC%96%B4-%EC%97%AD%EC%A0%84IoC%EA%B3%BC-%EC%9D%98%EC%A1%B4%EC%84%B1-%EC%A3%BC%EC%9E%85DI)

# 생성자 주입 vs 수정자, 필드 주입 차이

  ### 의존성 주입 방법

    - **생성자 주입(가장 권장하는 방식)**
        - 생성자를 통해 의존관계를 주입받는 방법, 기존 자바 코드와 다른 점 없음
            - @Autowired 어노테이션을 붙이는 것이 정석이나, 생성자가 1개인 경우 생략 가능

        ```jsx
        @Service
        public class StationConstructorService {
            private final StationRepository stationRepository;
        
            @Autowired
            public StationConstructorService(final StationRepository stationRepository) {
                this.stationRepository = stationRepository;
            }
        
            public String sayHi() {
                return stationRepository.sayHi();
            }
        }
        ```

    - **setter 주입**
        - 필드의 값을 변경하는 setter에 의존성을 주입하는 방식
            - 변경 가능성이 있을때 사용
            - 생성자 호출 후 변수 변경이 일어나야하므로 final 붙일 수 없음.

        ```jsx
        @Service
        public class StationSetterService {
            private StationRepository stationRepository;
        
            @Autowired
            public void setStationRepository(final StationRepository stationRepository) {
                this.stationRepository = stationRepository;
            }
        
            public String sayHi() {
                return stationRepository.sayHi();
            }
        }
        ```

    - 필드 주입 **(사용하지 않는 것을 권장)**
        - 코드가 간결하지만, 클래스 외부 접근이 불가해 테스트하기 어려움
        - DI 프레임워크가 없으면 사용할 수 없게 된다.

    ```jsx
    @Service
    public class StationFieldService {
        @Autowired
        private StationRepository stationRepository;
    
        public String sayHi() {
            return stationRepository.sayHi();
        }
    }
    ```

  출처 : https://engineerinsight.tistory.com/46

# AOP

  전반적인 처리를 모듈화한다.

  ### **AOP (Aspect-Oriented Programming) / 관점 지향 프로그래밍**

    - 객체지향 프로그래밍을 **보완**하기 위해 쓰이는 것
    - **컴파일 시점 적용 / 클래스 로딩 시점 적용 / 런타임 시점 적용 즉 3가지로 나뉨**

  ### **Spring AOP**

    - **런타임 시점에 적용하는 방식을 사용**
        - 왜? 컴파일 시점과 클래스 로딩 시점에 적용하려면 별도의 컴파일러와 클래스로더 조작기를 써야 하는데, 이것을 정하고 사용 및 유지하는 과정이 매우 어렵고 복잡하기 때문이다.
    - 소스 코드상에서 다른 부분에 계속 반복해서 쓰는 코드 : 흩어진 관심사
        - 흩어진 관심사를 Aspect로 모듈화하고 핵심적인 비즈니스 로직에서 분리하여 재사용하겠다는 것이 AOP의 취지다.

  ### **AOP 주요 개념**

    - Aspect : 위에서 설명한 흩어진 관심사를 모듈화 한 것. 주로 부가기능을 모듈화함.
    - Target : Aspect를 적용하는 곳 (클래스, 메서드 .. )
    - Advice : 실질적으로 어떤 일을 해야할 지에 대한 것, 실질적인 부가기능을 담은 구현체
    - JointPoint : Advice가 적용될 위치, 끼어들 수 있는 지점. 메서드 진입 지점, 생성자 호출 시점, 필드에서 값을 꺼내올 때 등 다양한 시점에 적용가능
    - PointCut : JointPoint의 상세한 스펙을 정의한 것. 'A란 메서드의 진입 시점에 호출할 것'과 같이 더욱 구체적으로 Advice가 실행될 지점을 정할 수 있음

  출처 : [https://velog.io/@kai6666/Spring-Spring-AOP-개념](https://velog.io/@kai6666/Spring-Spring-AOP-%EA%B0%9C%EB%85%90)

  [https://engkimbs.tistory.com/entry/스프링AOP](https://engkimbs.tistory.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81AOP) [새로비:티스토리]

# 서블릿

  ### 서블릿의 정의(***Servlet)***

  : 클라이언트 요청을 처리하고, 그 결과를 다시 클라이언트에게 전송하는 Servlet 클래스의 구현 규칙을 지킨 자바 프로그램 ⇒ 서블릿에서 view에 뿌려준다.

    - 필터와 서블릿과 혼동 x

    - 웹페이지를 동적으로 생성하여 클라이언트에게 반환해주는 역할
    - main메서드가 아닌, 웹 컨테이너(Servlet Container)에 의해 실행된다.

    ```jsx
    @WebServlet(name = "helloServlet", urlPatterns = "/hello")
    public class HelloServlet extends HttpServlet {
    
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // 애플리케이션 로직
        }
    }
    ```

    - urlPatterns("/hello") 의 URL이 호출되면 서블릿 코드가 실행된다.
        - HttpServletRequest : HTTP 요청 정보를 사용
        - HttpServletResponse : HTTP 응답 정보를 사용

      ⇒ http를 편하게 꺼내 쓸 수 있도록 도와 준다.

      request, response 객체는 새로 만들어지는게 맞지만, helloServlet은 재사용(최초 로딩 시점에 서블릿 객체를 미리 만들고 실행[싱글톤 관리])


    ### 서블릿의 동작방식(***Servlet)***
    
    사용자가 URL을 입력 ⇒ was는 request, response 객체를 새로 만듬⇒ Http Request, HttpResponse 객체를 생성 ⇒ 사용자가 요청한 URL이 어느 서블릿에 대한 요청인지 찾기(예제에서는  helloServlet 찾음)  ⇒ 서블릿의 `service()` 메소드를 호출 ⇒ 클라이언트가 GET일 경우  `doGet()`, POST일 경우 `doPost()` 메소드 호출 ⇒ 동적 페이지를 생성한 후 HttpServletResponse 객체에 응답보냄 ⇒ 응답 후 request, response 객체를 소멸한다.