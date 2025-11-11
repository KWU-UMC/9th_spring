# Week 7 
### 핵심 키워드
```text
1. RestControllerAdvice
2. lombok
3. dto 형식 public static VS record 비교
```

## 1. RestControllerAdvice
### @ControllerAdvice
Spring 3.2에서 도입된 어노테이션이고,
모든 @Controller 클래스가 공유하는 공통 로직을 정의할 때 사용</br>
주로 예외 처리와 바인딩 설정, 모델 객체 등에 사용됨.

### @RestControllerAdvice
@ControllerAdvice와 매우 흡사하지만, 해당 어노테이션을 사용하면 @RequestBody가 암시적으로
추가된다는 차이점이 있음</br>
따라서 JSON 형태로 바로 응답을 보낼 수 있음

#### 예제코드
NotFoundException이 발생했을 때 Http 상태코드와 Body에 에러 메시지를 담아 응답하도록 되어있음</br>
여기서 반환값은 ResponseEntity임
```java
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```
### 작동원리
Spring의 @ControllerAdvice와 @RestControllerAdvice는 AOP 원리를 기반으로 동작</br>
즉, 어노페이션은 공통 관심사인 예외 처리 로직을 분리해 관리함. 이를 통해 코드의 중복을 줄이고, 유지보수성을 향상시킴</br>
스프링 부트는 내부적으로 클래스패스를 스캔하여 이러한 어노테이션을 가진 클래스를 자동으로 등록하고 적용


#### @ControllerAdvice와 @RestControllerAdvice의 차이점
`@ControllerAdvice`의 경우 ModelAndView 객체를 반환하여 HTML 뷰를 렌더링할 수 있는데,
당연히 @ControllerAdvice는 주로 전통적인 Spring MVC와 함께 사용됨</br>
`@RestControllerAdvice`의 경우 ResponseEntity난 POJO 객체를 반환하여 JSON 형태로 응답할 수 있고,
주로 RESTful 웹 서비스에서 주로 사용됨</br>

물론 @ControllerAdvice 어노테이션도 @ResponseBody를 명시적으로 추가해 JSON으로 반환할 수 있음

## 2. lombok
Lombok이란 어노테이션 기반으로 코드를 자동완성 해주는 라이브러리임</br>
Lombok을 이용하면 Getter, Setter, Equals, ToString 등 다양한 방면의 코드를 자동완성 시킬 수 있음 </br>
Store에서 생성자도 필요하고, toString 함수도 Override 해야하는 등 추가적인 작업이 필요하다면 1개의 VO 클래스만 해도 상당히 길어지고,
코드의 가독성은 떨어지는 등 단점이 있음</br>
### Lombok의 장점
- 어노테이션 기반의 코드 자동 생성을 통한 생산성 향상
- 반복되는 코드 다이어트를 통한 가독성 및 유지보수성 향상
- Getter, Setter 외에 빌더 패턴이나 로그 생성 등 다양한 방면으로 활용 가능
Lombok을 사용하면 정말 많은 이점을 얻을 수 있음. 과거에는 IntelliJ에서 롬복 플러그인을 모든 팀원이 설치해줘야 한다는 단점도 있음
#### @Getter, @Setter
Lombok에서 가장 자주 활용하는 어노테이션임</br>
@Getter와 @Setter를 클래스 이름 위에 적용시키면 모든 변수들에 적용이 가능하고, 변수 이름 위에 적용시키면 해당 변수들만 적용 가능함

#### @AllArgsConstructor
@AllArgsConstructor는 모든 변수를 사용하는 생성자를 자동완성 시켜주는 어노테이션</br>

#### @NoArgsConstructor
@NoArgsConstructor는 어떤한 변수도 사용하지 않는 기본 생성자를 자동완성 시켜주는 어노이션


## 3. dto 형식 public static VS record 비교
public static 내부 클래스 DTO와 record 타입 DTO의 주요 차이점은 코드 간결성, 불변성,
그리고 부수적인 메서드 자동 생성 여부에 있음

### 주요 차이점 비교
|특징|public static 내부 클래스 DTO|record 타입 DTO|
|:---|:---|:---|
|선언 방식|일반 클래스 문법 사용(필드, 생성자, Getter/Setter 명시)|record 키워드와 축약된 문법 사용|
|코드 양|상용구 코드(Boilerplate code: Getter, Setter, equals(), hasCode(), toString() 등)가 많음| 상용구 코드가 없어 코드가 매우 간결|
|불변성|설계에 따라 가변적 또는 불변적으로 만들 수 있음</br> (보통 final 필드와 생성자로 불변 구현)| 기본적으로 불변 객체로 설계됨(모든 필드가 final)|
|접근자 메서드|직접 정의하거나 Lombok 등으로 자동 생성| fieldName() 형식의 접근자 메서드가 자동 생성|
|상속 및 확장|다른 클래스를 상소갛거나 인터페이스 구현 가능| 다른 클래스를 상속받을 수 없으며(final), 인터페이스 구현 가능|
|주요 목적|데이터 전달뿐 아니라 복잡한 로직 및 다양한 동작 정의 가능|순수하게 데이터 보유가 주된 목적|

