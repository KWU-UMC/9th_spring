# WEEK 9 - 💧나미/이나영
## 객체 그래프 탐색 Object Graph Navigation
    JPA에서 엔티티 간의 연관관계를 점(.)으로 타고 들어가며 접근하는 방식
### 주의할 점
    1. LAZY 로딩 주의
        - @ManyToOne(fetch = FectchType.LAZY)인 경우, 실제 DB 접근은 해당 필드를 사용할 때 일어남
    2. 그래프 깊이 제한
        - A→B→C 처럼 계속 탐색은 가능하지만 너무 깊으면 성능 저하
        - 필요 데이터만 join fetch로 제한하는 게 좋음
    3. 경로 탐색 불가능한 경우
        - null이 중간에 있으면 탐색 불가 (즉 NullPointerException 유발 가능)
        - JPQL 에서는 내부 join으로 안전하게 처리 가능 (left join 등)