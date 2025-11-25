# WEEK 8 - 💧나미/이나영
---
## java의 Exception 종류들
    Checked Exception
    Unchecked Exception

    이 둘의 핵심적인 차이는 '반드시 예외 처리를 해야 하는가?'
### Checked Exception

    컴파일러가 체크함 
    -> 컴파일 시점에 반드시 처리 (try-catch / throws)해야 하는 예외
    -> 개발자가 대비해야 하는 "예상 가능한 문제" 범주
    -> 네트워크, 파일I/O, DB 등 외부 요인에 의해 발생하는 경우가 많음

| 예외 클래스                     |설명|
|----------------------------|---|
| **IOException**            |파일/입출력 오류|
| **FileNotFoundException**  |파일을 찾을 수 없을 때|
| **SQLException**           |DB 관련 오류|
| **ClassNotFoundException** |클래스를 찾지 못함|
| **InterruptedException**   |스레가 중단될 때|
| **ParseException**         |문자열 파싱 실패|

### UnChecked Exception(Runtime Exception)

    실행 중 예외
    -> 컴파일러가 강제하지 않음
    -> 개발 실수로 발생하는 경우가 대부분
    -> Null, 연산 오류, 잘못된 인덱스 등

| 예외 클래스                         | 설명              |
| ------------------------------ | --------------- |
| **NullPointerException (NPE)** | null 접근         |
| **IndexOutOfBoundsException**  | 배열/리스트 범위 초과    |
| **IllegalArgumentException**   | 잘못된 인자 전달       |
| **ArithmeticException**        | 0으로 나누기 등 연산 오류 |
| **ClassCastException**         | 타입 캐스팅 실패       |
| **NumberFormatException**      | 문자열을 숫자로 변환 실패  |
| **IllegalStateException**      | 객체 상태가 잘못됨      |

### + Error
    
    프롤그램이 복구 불가
    -> 개발자가 일반적으로 처리하지 않음
    -> 시스템 레벨 문제, 메모리 부족, 스택 오버플로우 등

| Error 종류                 | 설명                  |
| ------------------------ | ------------------- |
| **OutOfMemoryError**     | JVM 메모리 부족          |
| **StackOverflowError**   | 재귀 폭주 등으로 스택 메모리 초과 |
| **NoClassDefFoundError** | 클래스 로딩 실패           |
| **InternalError**        | JVM 내부 오류           |

---
## @Valid
### 개념
    요청으로 들어온 객체의 필드 값이 유효한지 검사해주는 트리거 역할
    -> DTO 안에 있는 검증 어노테이션들(@NotNull, @Size, @Email, @Pattern 등)을 실제로 수행하게 만드는 키워드 
    
---

    public class SignupRequest {
        @NotNull
        @Size(min = 5, max = 20)
        private String username;

        @NotNull
        private String password;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @Valid @RequestBody SignupRequest request
    ) {
        ...
    }

    -> @Valid 덕분에 username 길이가 5~20 사이인지, null인지, password가 비어있는지 등을 자동으로 체크

---
### @Valid 가 없으면?
    DTO 안에 검증 어노테이션을 달아놓아도 아무것도 검사되지 않음
    즉, 잘못된 값이 들어와도 컨트롤러까지 그대로 전달됨

### 오류 내용을 직접 받고 싶을 때
    @Valid + BindingResult

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @Valid @RequestBody SignupRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok("success");
    }
