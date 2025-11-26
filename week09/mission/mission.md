# WEEK 9 - 💧나미/이나영
## 작업 repository
### 내가 진행 중인 미션 목록 조회
https://github.com/na311ng/umc9th-na311ng/tree/feat/my-mission-list
### 가게별 미션 목록 조회 
https://github.com/na311ng/umc9th-na311ng/tree/feat/store-mission-list
### 내 리뷰 미션 목록 조회
https://github.com/na311ng/umc9th-na311ng/tree/feat/review-list

## 공통 
### /global/annotaion/ValidPage
    package com.example.umc9th.global.annotation;

    import java.lang.annotation.*;
    
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface ValidPage {}
### /global/config/WebConfig
    package com.example.umc9th.global.config;

    import com.example.umc9th.global.resolver.PageArgumentResolver;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.method.support.HandlerMethodArgumentResolver;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    
    import java.util.List;
    
    @Configuration
    @RequiredArgsConstructor
    public class WebConfig implements WebMvcConfigurer {

        private final PageArgumentResolver pageArgumentResolver;
    
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList){
            resolverList.add(pageArgumentResolver);
        }
    }
### /global/resolver/PageArgumentResolver
    package com.example.umc9th.global.resolver;

    import com.example.umc9th.global.annotation.ValidPage;
    import com.example.umc9th.global.apiPayload.code.GeneralErrorCode;
    import com.example.umc9th.global.apiPayload.exception.GeneralException;
    import org.springframework.core.MethodParameter;
    import org.springframework.stereotype.Component;
    import org.springframework.web.bind.support.WebDataBinderFactory;
    import org.springframework.web.context.request.NativeWebRequest;
    import org.springframework.web.method.support.HandlerMethodArgumentResolver;
    import org.springframework.web.method.support.ModelAndViewContainer;
    
    @Component
    public class PageArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter){
            return parameter.hasParameterAnnotation(ValidPage.class)
                    && parameter.getParameterType().equals(Integer.class);
        }
    
        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mvContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            String pageStr = webRequest.getParameter("page");
    
            if(pageStr == null){
                throw new GeneralException(GeneralErrorCode.PAGE_NOT_FOUND);
            }
    
            int page;
    
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                throw new GeneralException(GeneralErrorCode.INVALID_PAGE);
            }
    
            if(page < 1){
                throw new GeneralException(GeneralErrorCode.INVALID_PAGE);
            }
    
            return page;
        }
    }
### /global/apiPayload/code/GeneralErrorCode
    package com.example.umc9th.global.apiPayload.code;
    
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import org.springframework.http.HttpStatus;
    
    @Getter
    @AllArgsConstructor
    public enum GeneralErrorCode implements BaseErrorCode{
    
        BAD_REQUEST(HttpStatus.BAD_REQUEST,
                "COMMON400_1",
                "잘못된 요청입니다."),
        PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST,
                "PAGE400_1",
                "page 파라미터가 존재하지 않습니다. "),
        INVALID_PAGE(HttpStatus.BAD_REQUEST,
                "PAGE400_2",
                "page 값은 1 이상의 정수여야 합니다."),
        UNAUTHORIZED(HttpStatus.UNAUTHORIZED,
                "AUTH401_1",
                "인증이 필요합니다."),
        FORBIDDEN(HttpStatus.FORBIDDEN,
                "AUTH403_1",
                "요청이 거부되었습니다."),
        NOT_FOUND(HttpStatus.NOT_FOUND,
                "COMMON404_1",
                "요청한 리소스를 찾을 수 없습니다."),
        DUPLICATE_RESOUCE(HttpStatus.CONFLICT,
                "COMMON409_1",
                "이미 존재하는 리소스입니다."),
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
                "COMMON500_1",
                "예기치 않은 서버 에러가 발생했습니다."),
    
        ;
    
        private final HttpStatus status;
        private final String code;
        private final String message;
    }
### /global/apiPayload/handler/GeneralExceptionAdvice
    package com.example.umc9th.global.apiPayload.handler;
    
    import com.example.umc9th.global.apiPayload.ApiResponse;
    import com.example.umc9th.global.apiPayload.code.BaseErrorCode;
    import com.example.umc9th.global.apiPayload.code.GeneralErrorCode;
    import com.example.umc9th.global.apiPayload.exception.GeneralException;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    
    @RestControllerAdvice
    public class GeneralExceptionAdvice {
    
        // 애플리케이션에서 발생하는 커스텀 예외를 처리
        @ExceptionHandler(GeneralException.class)
        public ResponseEntity<ApiResponse<Void>> handleGeneralException(
                GeneralException ex
        ) {
    
            BaseErrorCode errorCode = ex.getCode();
    
            return ResponseEntity.status(errorCode.getStatus())
                    .body(ApiResponse.onFailure(
                            errorCode.getCode(),
                            errorCode.getMessage(),
                            null
                    )
            );
        }
    
        // 그 외의 정의되지 않은 모든 예외 처리
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<?>> handleException(
                Exception ex
        ){
            BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;
    
            return ResponseEntity.status(code.getStatus())
                    .body(ApiResponse.onFailure(
                            code.getCode(),
                            code.getMessage(),
                            ex.getMessage()
                    )
            );
        }
    }

## 미션 api
### 내가 진행 중인 미션 목록 조회
    // 내가 진행중인 미션 목록 API 구현
    @GetMapping("/my-progress")
    public ApiResponse<MyMissionListDTO> getMyProgressMissions(
            @ValidPage @RequestParam(required = false) Integer page
    ){
        Long loginMemberId = 1L;

        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                memberMissionService.getMyProgressMissions(loginMemberId, page)
        );
    }

### 가게별 미션 목록 조회
    // 가게별 미션 목록 조회
    @GetMapping("/stores/{storeId}")
    public ApiResponse<StoreMissionListDTO> getStoreMissions(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page
    ){
        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                missionService.getMissionByStore(storeId, page)
        );
    }

### 내 리뷰 미션 목록 조회
    // 내가 작성한 미션 목록 조회
    @GetMapping("/myReviewList")
    public ApiResponse<ReviewListDTO> getMyReviews(
            @ValidPage @RequestParam Integer page
    ){
        Long loginMemberId = 1L;

        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                reviewService.getMyReviews(loginMemberId, page)
        );
    }
