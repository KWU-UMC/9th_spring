# 🎯 핵심 키워드
## DB Join이란?
**서로 다른 각각의** 테이블 속 데이터를 **동시에 보여주려고 할 때** 사용하는 SQL문

## Join 종류들
- Inner Join(내부 조인): 두 테이블을 연결할 때 가장 많이 사용하는  것(교집합)

```jsx
SELECT <열 목록>
FROM <첫 번째 테이블>
    INNER JOIN <두 번째 테이블> // JOIN이라고 써도 무방
    ON <조인 조건>
[WHERE 검색 조건]
```

- outer join(외부 조인) : 한쪽에만 데이터가 있어도 결과가 나온다.
    - join 불가능한 테이블의 경우 값이 NULL로 채워진다.

```jsx
SELECT <열 목록>
FROM <첫 번째 테이블(LEFT 테이블)>
    <LEFT | RIGHT | FULL> OUTER JOIN <두 번째 테이블(RIGHT 테이블)>
     ON <조인 조건>
[WHERE 검색 조건]

// LEFT OUTER JOIN: 왼쪽 테이블의 모든 값이 출력
// RIGHT OUTER JOIN: 오른쪽 테이블의 모든 값이 출력
// FULL OUTER JOIN: 왼쪽 외부 조인과 오른쪽 외부 조인이 합쳐진 것
```

- cross join(상호 조인) : 한쪽 테이블의 모든 행과 다른 쪽 테이블의 모든 행을 조인시키는 기능 ⇒ **카티션 곱(CARTESIAN PRODUCT)**
## 트랜잭션이란?
트랜잭션은 데이터베이스에서 하나의 논리적 작업 단위를 의미한다. 여러 개의 SQL 문이 하나의 묶음으로 실행되고, 이 단위 전체가 성공하거나 실패해야 한다.

트랜잭션은 ACID 특성을 따른다:

Atomicity (원자성): 모두 수행되거나, 전혀 수행되지 않아야 한다.

Consistency (일관성): 트랜잭션 전후에 데이터는 항상 유효한 상태를 유지해야 한다.

Isolation (고립성): 동시에 여러 트랜잭션이 실행되어도 서로 간섭하지 않아야 한다.
## Join on 과 where의 차이
### (1) JOIN ON : 매칭 조건
=> 조인할 때 매칭 조건을 걸어, JOIN의 성격(OUTER 유지 여부)에 영향을 줌.

테이블을 결합할 조건을 명시한다.

조인 단계에서 레코드가 필터링됨.

OUTER JOIN에서도 조건을 적용하면 LEFT/RIGHT 유지 가능.

### (2) WHERE : 최종 필터링

=> 조인 후 결과에서 필터링, OUTER JOIN도 INNER처럼 만들어버릴 수 있음.

조인 후 결과 집합에서 조건을 거른다.

OUTER JOIN이라도 WHERE에서 NULL을 거르면 사실상 INNER JOIN처럼 동작한다.