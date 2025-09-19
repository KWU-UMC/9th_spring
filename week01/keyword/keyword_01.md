### DB Join이란?
- **정의**: 두 개 이상의 테이블을 공통된 컬럼을 기준으로 묶어 하나의 결과 집합을 만드는 것.
- **필요성**:
    - 데이터가 정규화되어 분리 저장되어 있으므로, 원하는 정보를 얻으려면 테이블 간 관계를 합쳐야 함.
    - 예: `user` 테이블과 `order` 테이블을 join하여 “어떤 유저가 어떤 주문을 했는지” 조회.

---

### Join 종류들
- **INNER JOIN**
    - 두 테이블 모두에서 조건에 맞는 행만 출력.
    - 교집합.

- **LEFT JOIN (LEFT OUTER JOIN)**
    - 왼쪽 테이블의 모든 행을 출력 + 매칭되는 오른쪽 행.
    - 오른쪽에 매칭이 없으면 NULL.

- **RIGHT JOIN (RIGHT OUTER JOIN)**
    - 오른쪽 테이블의 모든 행을 출력 + 매칭되는 왼쪽 행.
    - 왼쪽에 매칭이 없으면 NULL.

- **FULL OUTER JOIN** (MySQL 기본 미지원, UNION으로 구현)
    - 양쪽 모두의 전체 행을 출력, 매칭이 없으면 NULL.

- **CROSS JOIN**
    - 두 테이블의 모든 행을 곱집합으로 결합 (N×M).

- **SELF JOIN**
    - 같은 테이블을 자기 자신과 JOIN. 계층 구조나 비교 시 활용.

---

### 트랜잭션이란?
- **정의**: 데이터베이스에서 하나의 논리적 작업 단위. 여러 쿼리를 묶어서 모두 성공하거나 모두 실패하게 보장.

- **특징 (ACID)**:
    - **Atomicity (원자성)**: 전부 수행되거나 전부 취소됨.
    - **Consistency (일관성)**: 트랜잭션 전후 DB 상태가 일관성을 유지.
    - **Isolation (고립성)**: 동시에 여러 트랜잭션이 실행돼도 서로 간섭하지 않음.
    - **Durability (지속성)**: 성공적으로 커밋된 결과는 영구적으로 저장.

- **연산**:
    - `BEGIN / START TRANSACTION` : 트랜잭션 시작
    - `READ` : 데이터 읽기
    - `WRITE` : 데이터 변경 (INSERT, UPDATE, DELETE)
    - `COMMIT` : 지금까지의 연산을 확정 → DB에 영구 반영
    - `ROLLBACK` : 지금까지의 연산을 취소 → 트랜잭션 시작 전 상태로 복구

- **상태**:
    1. **Active (실행 중)**
        - 트랜잭션이 시작되어 SQL 연산을 수행 중
    2. **Partially Committed (부분 완료)**
        - 모든 SQL이 실행되었으나, 아직 COMMIT은 안 한 상태
    3. **Committed (완료됨)**
        - COMMIT 성공 → 변경 내용이 영구 저장
    4. **Failed (실패)**
        - 실행 중 오류나 장애로 더 이상 진행 불가
    5. **Aborted (철회됨)**
        - ROLLBACK 수행, 원래 상태로 되돌아감

- **예시: 은행 송금 (A→B 계좌 1만원 송금)**

  ```sql
  START TRANSACTION;
  -- 1) A 계좌에서 1만원 출금
  UPDATE account SET balance = balance - 10000 WHERE id = 'A';
  -- 2) B 계좌에 1만원 입금
  UPDATE account SET balance = balance + 10000 WHERE id = 'B';
  -- 3) 성공 시 확정
  COMMIT;
  ```
  
---

### Join ON 과 WHERE의 차이
- **JOIN … ON**

    - JOIN 조건을 지정하는 구문.
    - 두 테이블을 어떤 키로 연결할지 정의.
    ```sql
    SELECT *
    FROM user u
    JOIN order o ON u.id = o.user_id;
    ```

- **WHERE**

    - JOIN 이후 결과에 대해 추가 필터링을 적용하는 구문.
    - 예: 특정 지역 유저만 보고 싶을 때:
    ```sql
    SELECT *
    FROM user u
    JOIN order o ON u.id = o.user_id
    WHERE u.region = 'Seoul';
    ```