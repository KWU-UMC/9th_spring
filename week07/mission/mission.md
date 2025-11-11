# WEEK 7 - 💧나미/이나영
## mission1: RestControllerAdvice
### 장점
|장점|설명|
|---|---|
|중복제거| 각 컨트롤러 마다 try-catch 안 써도 됨!<br>예외를 한 곳에서 처리한다
|응답 통일| 모든 에러 응답을 동일한 형식(JSON)으로 반환 가능|
|가독성 향상|비즈니스 로직에만 집중할 수 있고, 에러 핸들링 코드는 한 곳에 모임
|유지보수 용이|새로운 예외나 에러코드 추가 시, 컨트롤러 수정 없이 중앙에서 수정 가능
|에러 로깅 일원화|로그를 한 곳에서 남겨서 모니터링/디버깅이 쉬워짐
|세밀한 예외 분류 가능|@Exception(CustomException.class)로 커스텀 예외 구체적으로 처리 가능
### 없다면 생기는 불편한 점
|불편한 점|구체적 예시|
|-------|----------|
|컨트롤러마다 try-catch 중복|try { ... } catch(Exception e) { return new ResponseEntity(...); } 이런 코드가 모든 컨트롤러마다 반복됨
|응답 포맷 불일치|어떤 컨트롤러는 {error: "fail"} 형식, 어떤 곳은 {status: 500} 형식 → 프론트엔드에서 처리하기 복잡
|로깅 누락|일부 컨트롤러에서는 예외를 잡고 로그 안 남길 수도 있음 (디버깅 어려움)
|유지보수 지옥|에러 처리 로직 수정할 때, 모든 컨트롤러를 다 찾아 수정해야 함
|코드 가독성 저하|핵심 로직보다 예외 처리 코드가 더 많아져서 가독성 떨어짐
## mission2: API 응답 처리 통일하기 
### 깃허브 주소 
https://github.com/na311ng/umc9th/tree/feat/Chapter7
## mission3: 성공 메서드, 성공 ENUM
### 성공 메서드
    package com.example.umc9th.global.apiPayload.code;
    
    import org.springframework.http.HttpStatus;
    
    public interface BaseSuccessCode {
        HttpStatus getStatus();
        String getCode();
        String getMessage();
    }

### 성공 ENUM
    package com.example.umc9th.global.apiPayload.code;
    
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import org.springframework.http.HttpStatus;
    
    @Getter
    @AllArgsConstructor
    public enum GeneralSuccessCode implements BaseSuccessCode {
    
        OK(HttpStatus.OK,
                "COMMAND200",
                "성공적으로 요청이 처리되었습니다."),
        CREATED(HttpStatus.CREATED,
                "COMMON201",
                "성공적으로 리소스가 생성되었습니다."),
        ACCEPTED(HttpStatus.ACCEPTED,
                "COMMON202" ,
                "요청이 수락되었습니다."),
        NO_CONTENT(HttpStatus.NO_CONTENT,
                "COMMON204",
                "성공했지만 반환할 데이터가 없습니다."),
        ;
        private final HttpStatus status;
        private final String code;
        private final String message;
    }
