# WEEK 7 - 제이/한종서

## 미션

### 본문

1. @RestControllerAdvice

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

<br>

#### 요약

`@RestControllerAdvice`는 `@ControllerAdvice` + `@ResponseBody`이고 `@ControllerAdvice`는 모든 컨트롤러에서 발생한 예외를 여기로 모아라, `@ResponseBody`는 리턴값을 JSON으로 변환해서 응답해라라는 뜻이다.

이 어노테이션이 붙은 클래스는 프로젝트 어딘가에서 예외가 터진 것을 받아 JSON 응답으로 바꿔주는 역할을 하고, 이를 통해 컨트롤러에서 `try-catch`를 안 해도 되고, 에러 메시지 형식을 전체 API에서 통일할 수 있다.

<br>

#### @RestControllerAdvice가 없을 경우 불편한 점

일단, 모든 예외를 각 컨트롤러에서 직접 처리해야 한다. 즉 try-catch 문이 굉장히 많아지게 된다.

API 하나가 추가될 때마다 같은 코드가 복붙된다. 이는 유지보수 측면에서 최악이다.

또한, 예외 응답 형식이 제멋대로 될 위험이 있다. 실패 시 HTTP 상태 코드, 실패 시 응답 JSON 구조, 메시지 형식, 로그 메시지 등 응답 형식이 통일되지 않을 수 있고, 프론트엔드 개발자의 불만이 폭발할 것이다.

공통 에러 처리 로직도 사라진다.

NullPointerException, IllegalArgumentException, DB 예외, Validation 실패 예외 등 공통적인 예외를 통일된 JSON으로 처리할 수 없다.

어떤 API는 HTML 에러, 어떤 API는 JSON 에러, 어떤 API는 서버 로그에만 찍히거나... 클라이언트는 처리 불가능한 데다가 디버깅하기도 어렵다.

이외에도 HTTP 상태 코드가 엉망이 되거나 개발 속도가 떨어지는 등 개발 난이도가 상승하게 된다.

적고 보니 반드시 필요한 어노테이션이라는 생각이 든다!

<br>

2. API 응답 통일 처리

```java
package com.example.demo.domain.review.controller;

import com.example.demo.domain.review.dto.ReviewResponseDto;
import com.example.demo.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/test")
    public List<ReviewResponseDto> getMyReviews(
            @RequestParam Long memberId,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) Integer starGroup
    ) {
        return reviewService.getMyReviews(memberId, storeName, starGroup);
    }
}
```

기존 ReviewController의 getMyReviews API를 응답 통일 처리하였다.

```java
package com.example.demo.domain.review.controller;

import com.example.demo.domain.review.dto.ReviewResponseDto;
import com.example.demo.domain.review.service.ReviewService;
import com.example.demo.global.apiPayload.ApiResponse;
import com.example.demo.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/test")
    public ApiResponse<List<ReviewResponseDto>> getMyReviews(
            @RequestParam Long memberId,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) Integer starGroup
    ) {
        List<ReviewResponseDto> reviews = reviewService.getMyReviews(memberId, storeName, starGroup);

        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                reviews
        );
    }
}
```

`List<ReviewResponseDto>>`를 ApiResponse로 감싸고 ApiResponse의 onSuccess 메서드의 리턴값을 반환하였다.

![Image](/week07/mission/mission.png)

<br>

3. 성공 Enum, 성공 메서드

```java
package com.example.demo.global.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
```

`BaseErrorCode`와 같이 `BaseSuccessCode` 인터페이스를 정의해주고

```java
package com.example.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {
    OK(HttpStatus.OK,
            "COMMON200_1",
            "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED,
            "COMMON201_1",
            "리소스가 성공적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED,
            "COMMON202_1",
            "요청이 접수되었습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

성공 Enum을 만들어준다.

```java
// 성공한 경우 (result 포함)
public static <T> ApiResponse<T> onSuccess(BaseSuccessCode code, T result) {
    return new ApiResponse<>(true, code.getCode(), code.getMessage(), result);
}
```

그리고 성공 메서드를 작성해준다.
