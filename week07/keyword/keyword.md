# WEEK 7 - 💧나미/이나영
## RestControllerAdvice
    - @Controller + @ResponseBody 
    - 주로 REST API 에서 **컨트롤러 전반에서 발생하는 예외를 중앙집중형으로 처리**하기 위해 사용됨
    - 컨트롤러마다 예외 처리 코드를 반복 작성하는 대신, 한 곳에서 예외 타입별로 처리해서 코드 중복 제거 + 응답 통일성 확보가 가능
    - Spring 프레임워크에서 권장되는 예외 처리 방법 중 하나 

## lombok
    - 어노테이션 기반의 JAVA 라이브러리 
    - 보일러플레이트 코드(반복적이고 별로 의미 없는 코드)를 줄이기 위해 사용됨
    - getter, setter, toString(), equals()/hashCode(), 생성자 등을 일일히 작성하지 않고, 어노테이션 한 줄로 대체 가능
    - 작동방식: 컴파일 시점에 Annotation Processor가 작동해 실제 .class 파일에는 코드가 생성되지만, 소스에는 보이지 않음

|어노테이션| 설명  |
|--------|-----|
|@Getter / @Setter|필드에 대해 자동으로 getter / setter 메서드 생성
|@NoArgsConstructor / @AllArgsConstructor|인자가 없는 생성자 또는 모든 필드를 인자로 가진 생성자 자동 생성
|@RequiredArgsConstructor|final이나 @NonNull이 붙은 필드만 인자로 가진 생성자 자동 생성
|@ToString|toString() 메서드를 자동 생성, 출력 필드 제어 가능
|@EqualsAndHashCode|equals() 및 hashCode() 메서를 자동 생성
|@Data|위 여러 어노테이션을 한 번에 (@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor) 포함
|@Builder|빌더 패턴을 자동으로 구현할 수 있게 해줌
|@Value|불변(immutable) 객체를 쉽게 만들 때 사용
|@Slf4j, @Log4j2 등|로깅용 필드(log)를 자동으로 생성

### 장점
    - 코드 라인이 줄어들어 가독성 증가   
    - 반복적 메서드 작성 시간 절약  
    - 유지보수성 향상
### 주의사항
    - IDE나 빌드툴 설정이 제대로 되어 있어야만 정상 작동
    - 과도하게 사용시 외부에서 메서드가 보이지 않아 이해하기 어려울 수 있음

## dto 형식 public static VS record 비교하기
### public static class DTO 방식
예제

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
    }   
특징

    - 일반적인 클래스 기반 DTO 구조 
    - Lombok으로 반복 줄임    
    - public static으로 선언하면 바깥 클래스 내부의 정적 중첩 DTO로 사용 가능

장점
    
    - JPA/Jackson 직렬화에 완벽 호환
    - Spring에서 자주 쓰는 @Builder, @JsonProperty 등과 유연하게 조합 가능
    - Nesting 구조로 파일 개수 줄이기 용이
단점
    
    - 불변 객체가 아님
    - 필드 추가 시 Gette/Constructor 수정 필요
    - 너무 많은 Lombok 조합 시 IDE가 코드 분석하기 어려워짐

### record 방식
예제

    public record UserResponse(
        Long id,
        String name,
        String email
    ) {}
특징

    - Java 14에서 미리보기로, Java16부터 정식 도입된 불변 데이터 전용 클래스
    - 컴파일 시 자동으로 다음 생성:
        - 모든 필드의 private final
        - 생성자
        - equals(), hashCode(), toString()
        - getter 역할의 메서드(.id(), .name(), .email())
장점

    - 완벽한 불변성
    - Lombok 필요 없음
    - 간결하고 DTO 목적에 딱 맞음
    - 직렬화도 잘 작동
    - equals/hashCode/toString 자동 구현
단점

    - 필드명으로 바로 getter가 만들어지므로, record.id() 처럼 써야 함 -> getId() 아님
    - @Builder, @NoArgsConstructor 같은 Lombok 기능 X
    - JPA Entity로는 사용 불가 (프록시 초기화, setter, 빈 생성자 필요)
    - Jackson 버전에 따라 record 지원 문제 발생 가능