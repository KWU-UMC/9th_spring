# WEEK 8 - 제이/한종서

## 핵심 키워드

### java의 Exception 종류

1. Error

JVM 레벨에서 발생하는 심각한 문제로, 개발자가 해결할 수 없는 예외이다.

try-catch로 잡는 것 자체가 무의미하며, 문제를 복구할 수 없다.

JVM 메모리가 부족할 때 `OutOfMemoryError`, 무한 루프를 돌며 재귀 호출이 이루어질 때 `StackOverflowError`, 클래스를 로딩하는 데 실패할 때 `LinkageError`, 스레드가 강제 종료될 때 `ThreadDeath` 등의 Error가 발생할 수 있다.

2. Exception

**Error**와 다르게 catch 할 수 있는 오류인 Exception에는 크게 `Checked Exception`과 `Unchecked Exception`이 있다.

#### Checked Exception

컴파일러가 try-catch 또는 throws를 강제하는 예외로, 반드시 처리해야 하는 예외이다.

Checked Exception은 코드 작성 단계에서 처리하도록 강제되며 네트워크, 파일, 데이터베이스 등 외부 자원에 관한 작업 중 자주 발생한다.

대표적인 예시로는 파일 입출력이 실패했을 때 `IOException`, 파일을 찾지 못할 때, 파일이 없을 때 `FileNotFoundException`, DB SQL 관련 문제가 발생했을 때 `SQLException` 등이 있다.

이외에도 클래스를 찾지 못했을 때 `ClassNotFoundException`, 스레드가 중단되었을 때 `InterruptedException` 등이 있다.

#### Unchecked Exception

Java에서 가장 자주 볼 수 있는 예외로, 컴파일러가 체크하지 않으며 프로그램 실행 중 갑자기 터진다.

대부분 프로그래밍 실수로 인해 발생하며 RuntimeException이다. 이 예외는 잡을 수 있지만 대부분 잡지 않는다.

왜냐하면 대부분이 개발자 코드에 의한 실수이기 때문에 근본 원인을 해결해야지, catch로 덮으면 더 큰 문제가 발생할 수 있다.

스프링 환경에서는 전역 예외 처리(@ControllerAdvice)로 한 번에 처리하는 것이 일반적이다.

대표적인 예시로는 null 접근 시 `NullPointerException`, 잘못된 파라미터로 인한 `IllegalArgumentException`, 배열이나 리스트의 범위를 초과하여 발생하는 `IndexOutOfBoundsException`, 0으로 나누어서 발생하는 `ArithmeticException`이 있고,

캐스팅을 잘못해서 발생하는 `ClassCastException`, 문자열에서 숫자로 변환할 때 실패하여 발생하는 `NumberFormatException`, 리스트 순회 중에 수정하여 발생하는 `ConcurrentModificationException` 등이 있다.

<br>

### @Valid

**@Valid**는 DTO 또는 객체에 대해 **Bean Validation**, 즉 유효성 검사를 수행하라는 트리거이자 신호이다.

이 어노테이션이 붙어 있는 객체 안의 모든 검증 어노테이션(@NotNull, @Size, @ExistFoods 등)을 실행해달라는 의미이다.

DispatcherServlet이 HTTP 요청을 받고, 컨트롤러 메서드 실행 전 ArgumentResolver로 파라미터를 생성한다.

파라미터에 @RequestBody 어노테이션이 붙어 있으면 JSON -> DTO 변환이 이루어진다.

변환 후에는 @Valid 어노테이션이 붙어 있는지 확인한다. 붙어 있으면 즉시 검증을 수행하고, 만약 검증에 실패하면 컨트롤러 메서드를 실행시키지 않는다.

이후 MethodArgumentNotValidException이 발생하고 예외 핸들러로 이동하여 예외를 처리하게 된다.

즉, @Valid 어노테이션을 사용하면 DTO를 항상 검증을 통과한 상태의 안전한 객체로 컨트롤러에 전달할 수 있다.

만약 검증에 통과하면 정상적으로 컨트롤러 메서드가 실행된다.

#### 요약

@Valid는 스프링 MVC의 ArgumentResolver가 DTO를 만들 때 수행되는 검증 트리거이다.

@Valid가 붙은 DTO는 JSON 변환이 끝난 직후 자동으로 검증되며 DTO 내부의 모든 검증 어노테이션이 실행된다. 이때 커스텀 Validator도 함께 동작한다.

실패하면 컨트롤러 메서드는 실행되지 않고 즉시 예외가 발생하며 이 예외는 @ControllerAdvice에서 처리하는 것이 일반적인 흐름이다.
