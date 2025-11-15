# WEEK 7 - 제이/한종서

## 핵심 키워드

### @RestControllerAdvice

스프링 부트가 전역적으로 예외를 가로채서 처리할 수 있게 해주는 어노테이션이다.

일반 컨트롤러에 흩어져 있는 `try-catch`를 한곳으로 모은다.

내부적으로는 `@ControllerAdvice` + `@ResponseBody`이다.

`@ExceptionHandler`를 붙인 메서드와 함께 써서 **특정 예외 발생 시 어떤 JSON 응답을 줄지** 정의한다.

여기서 `@ResponseBody`는 Spring MVC에서 **이 메서드의 반환값을 HTTP 응답 본문(Body)에 직접 넣어라**라는 뜻을 가진 어노테이션이다.

기본적으로 Spring MVC 컨트롤러(`@Controller`)의 메서드는 리턴값을 **뷰 이름**으로 해석한다.

예를 들어 `return "index";`라면 templates/index.html 렌더링을 시도한다. 즉 `"index"`는 문자열 데이터가 아니라 렌더링할 템플릿 이름이다.

따라서 Spring은 `index.html`을 찾아 렌더링하려고 한다.

`@ResponseBody`는 이 동작을 바꿔주는데, 뷰 이름이 아니라 HTTP 응답 본문으로 그대로 전송한다.

`@ResponseBody`가 붙으면 리턴값에 대해 HTML 뷰로 렌더링하지 말고, 그냥 데이터 자체를 응답하게 한다.

Spring은 `@ResponseBody`가 붙어 있고, 반환 타입이 객체일 경우 자동으로 객체를 **JSON**으로 변환한다.

클래스 레벨에서 `@RestController`를 사용하면 모든 메서드에 `@ResponseBody`를 붙인 것과 같은 효과가 난다.

#### 요약

`@RestControllerAdvice`는 `@ControllerAdvice` + `@ResponseBody`이고 `@ControllerAdvice`는 모든 컨트롤러에서 발생한 예외를 여기로 모아라, `@ResponseBody`는 리턴값을 JSON으로 변환해서 응답해라라는 뜻이다.

이 어노테이션이 붙은 클래스는 프로젝트 어딘가에서 예외가 터진 것을 받아 JSON 응답으로 바꿔주는 역할을 하고, 이를 통해 컨트롤러에서 `try-catch`를 안 해도 되고, 에러 메시지 형식을 전체 API에서 통일할 수 있다.

<br>

### Lombok

`Lombok`은 스프링 개발자 대부분이 쓰는 `코드 자동 생성기`이다.

정확하게는, 자바 클래스의 반복되는 보일러플레이트 코드를 자동으로 생성해주는 라이브러리이다.

필드의 getter, setter, 생성자, Builder 패턴, toString(), equals(), hashCode(), 로그 객체 생성 등 반복적으로 작성해야 할 필요가 있는 코드들을 자동으로 생성해준다.

이에 해당하는 어노테이션은 `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`, `@ToString`, `@Slf4j` 등이 있다.

Lombok은 IDE에서 코드가 있는 것처럼 보이게 하지만 실제로는 **컴파일 시점에 코드가 삽입**된다.

그래서 실제 .class 파일에는 getter, builder 등이 존재한다.

<br>

### DTO 형식 - public static class VS record

#### public static class DTO

우리가 워크북을 진행하면서 많이 만났던 DTO 형식이다.

```java
package com.example.demo.domain.test.dto.res;

import lombok.Builder;
import lombok.Getter;

public class TestResDTO {
    @Builder
    @Getter
    public static class Testing {
        private String testString;
    }

    @Builder
    @Getter
    public static class Exception {
        private String testString;
    }
}
```

DTO들은 큰 묶음으로 클래스를 만들고, 내부적으로 static 클래스를 만드는 것이 좋다.

DTO 자체는 수많은 곳에서 사용이 될 수 있기 때문에 static class로 만들면 매번 class 파일을 만들 필요도 없고, 범용적으로 DTO를 사용할 수 있다.

<br>

#### record DTO

Java에서 공식 지원하는 `불변` 전용 DTO이다.

```java
public record Testing(String testString) {}
```

이렇게만 코드를 적으면 자동으로 **private final 필드**, **생성자**, **getter**, **toString**, **equals**, **hashCode**가 전부 생성된다.

즉, 불변 DTO를 아주 간단하게 만들어 준다.

불변인만큼 record는 setter가 없다. 따라서 재할당도 불가능한 반면 class는 setter를 사용할 수 있고 재할당도 가능하다.

데이터가 절대 변하지 않아야 할 때, 즉 Response DTO에 record를 사용하면 된다.

Builder가 필요하거나 중첩 구조가 많은 DTO는 public static class를 사용하면 되고, Request DTO는 상황에 따라 둘 다 가능하지만 class를 사용하는 것이 더 유연하다고 한다.

<br>

### HTTP 상태 코드

상태 코드는 클라이언트가 보낸 요청의 처리 상태를 응답에서 알려주는 기능이다.

기본적으로 다음과 같은 구조를 가지고 있다.

**1xx** (Informational): 요청이 수신되어 처리중

**2xx** (Successful): 요청 정상 처리

**3xx** (Redirection): 요청을 완료하려면 추가 행동이 필요함

**4xx** (Client Error): 클라이언트 오류, 잘못된 문법 등으로 서버가 요청을 수행할 수 없음

**5xx** (Server Error): 서버 오류, 서버가 정상 요청을 처리하지 못함

<br>

만약 클라이언트가 인식할 수 없는 상태 코드를 서버가 반환한다면?

그럴 때는 상위 상태 코드로 해석해서 처리하면 된다.

299라면 2xx니까 Successful이구나, 451이면 4xx니까 Client Error구나, 599라면 5xx니까 Server Error구나~ 이런 식이다.

<br>

#### 세부 상태 코드

**2xx (Successful)**

클라이언트의 요청을 성공적으로 처리함

<br>

- 200 OK

요청 성공

- 201 Created

요청이 성공하여 새로운 리소스가 생성됨

- 202 Accepted

요청이 접수되었으나 처리가 완료되지 않았음

- 204 No Content

서버가 요청을 성공적으로 수행했지만, 응답 페이로드 본문에 보낼 데이터가 없음

<br>

**3xx (Redirection)**

#### 리다이렉트란?

웹 브라우저가 3xx 응답의 결과에 Location 헤더가 있을 때, Location 위치로 자동 이동되는 것

<br>

#### 종류

1. 영구 리다이렉션: 특정 리소스의 URI가 영구적으로 이동됨
- ex) /members -> /users
- ex) /event -> /new-event

2. 일시 리다이렉션: 일시적인 변경
- ex) 주문 완료 후 주문 내역 화면으로 이동
- PRG: Post/Redirect/Get

3. 특수 리다이렉션
- 결과 대신 캐시를 사용

<br>

#### 영구 리다이렉션

301, 308

- 리소스의 URI가 영구적으로 이동됨
- 원래의 URI를 사용하지 않음. 검색 엔진 등에서도 해당 변경을 인지함

<br>

301 Moved Permanently

- 리다이렉트 시 요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음

308 Permanent Redirect

- 301과 기능은 같음
- 리다이렉트 시 요청 메서드와 본문 유지(처음 POST를 보내면 리다이렉트도 POST 유지)

<br>

#### 일시 리다이렉션

302, 307, 303

- 리소스의 URI가 일시적으로 변경됨
- 따라서 검색 엔진 등에서 URL을 변경하면 안 됨

<br>

302 Found

- 리다이렉트 시 `요청 메서드가 GET으로 변할 수 있음`

307 Temporary Redirect

- 302와 기능은 같음
- 리다이렉트 시 요청 메서드와 본문 유지(`요청 메서드를 변경하면 안 됨`)

303 See Other

- 302와 기능은 같음
- 리다이렉트 시 `요청 메서드가 GET으로 변경됨`

<br>

#### PRG: Post/Redirect/Get

일시적인 리다이렉션의 예시

POST로 주문 후에 웹 브라우저를 새로고침하면?

새로고침은 다시 요청이므로 중복 주문이 될 수 있다.

따라서 POST로 주문 후에 새로고침으로 인한 중복 주문을 막을 필요가 있다. POST로 주문 후에 주문 결과 화면을 GET 메서드로 리다이렉트하면 된다.

새로고침해도 결과 화면을 GET으로 조회하므로 중복 주문 대신에 결과 화면만 GET으로 다시 요청된다.

<br>

#### 역사와 현실

처음 302 스펙의 의도는 HTTP 메서드를 유지하는 것이었다. 그런데 웹 브라우저들이 대부분 GET으로 바꾸어버렸다.

물론 일부는 다르게 동작하지만, 그래서 모호한 302를 대신하는 명확한 307, 303이 등장하였다.

따라서 307, 303을 권장하지만 현실적으로 이미 많은 애플리케이션 라이브러리들이 302를 기본값으로 사용하고 있다.

자동 리다이렉션 시 GET으로 변해도 되면 그냥 302를 사용해도 큰 문제가 없다고 한다.

<br>

#### 기타 리다이렉션

300 304

300 Multiple Choices

- 쓰지 않음

304 Not Modified

- 캐시를 목적으로 사용
- 클라이언트에게 리소스가 수정되지 않았음을 알려준다. 따라서 클라이언트는 로컬 PC에서 저장된 캐시를 재사용한다. 즉 캐시로 리다이렉트한다.
- 304 응답은 응답에 메시지 Body를 포함하면 안 된다. 로컬 캐시를 사용해야 하기 때문이다.
- 조건부 GET, HEAD 요청 시 사용

<br>

**4xx (Client Error)**

클라이언트 오류

- 클라이언트의 요청에 잘못된 문법 등으로 서버가 요청을 수행할 수 없음
- **오류의 원인이 클라이언트에게 있음**
- **클라이언트가 이미 잘못된 요청, 데이터를 보내고 있기 때문에 똑같이 재시도해도 실패함**

<br>

400 Bad Request

클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음

- 요청 구문, 메시지 등 오류
- 클라이언트는 요청 내용을 다시 검토하고 보내야 함
- ex) 요청 파라미터가 잘못되거나, API 스펙이 맞지 않을 때 등

401 Unauthorized

클라이언트의 해당 리소스에 대한 인증이 필요함

- 인증(Authentication)되지 않음
- 401 오류 발생 시 응답에 WWW-Authenticate 헤더와 함께 인증 방법을 설명

참고

- 인증(Authentication): 본인이 누구인지 확인(로그인)
- 인가(Authorization): 권한 부여(admin 권한처럼 특정 리소스에 접근할 수 있는 권한, 인증이 있어야 인가가 있음)
- 오류 메시지가 Unauthorized이지만 인증되지 않음을 의미함. 이름이 아쉽다.

403 Forbidden

서버가 요청을 이해했지만 승인을 거부함

- 주로 인증 자격 증명은 되었지만, 접근 권한이 불충분한 경우
- ex) admin 등급이 아닌 사용자가 로그인은 했지만, admin 등급의 리소스에 접근하는 경우

404 Not Found

요청 리소스를 찾을 수 없음

- 요청 리소스가 서버에 없음
- 클라이언트가 권한이 부족한 리소스에 접근할 때, 해당 리소스를 숨기고 싶을 때

<br>

**5xx (Server Error)**

서버 오류

- 서버 문제로 오류 발생
- 서버에 문제가 있기 때문에 재시도하면 성공할 수도 있음(복구가 되거나 등)

<br>

500 Internal Server Error

서버 내부 문제로 오류 발생, 애매하면 500 오류

503 Service Unavailable

서비스 이용 불가

- 서버가 일시적인 과부하 또는 예정된 작업으로 잠시 요청을 처리할 수 없음
- Retry-After 헤더 필드로 얼마 뒤에 복구되는지 보낼 수 있음
