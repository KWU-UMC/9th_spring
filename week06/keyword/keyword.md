# WEEK 6 - 💧나미/이나영
## QueryDSL에서 FetchJoin 하는 법

    QMember member = QMember.member;
    QTeam team = QTeam.team;
    
    Member found = queryFactory
        .selectFrom(member)
        .join(member.team, team).fetchJoin()  // fetchJoin
        .where(member.username.eq("son"))
        .fetchOne();
### 주의할 점
 1. join(entity.association, alias).fetchJoin() 형태로 쓰여야 QueryDSL이 JPA의 fetch join으로 변환함
2. 페이징(.offset(), limit())과 함께 사용 시 Hibernate에 따라 중복 결과나 예상치 못한 동작이 생길 수 있음
## DTO 매핑 방식 (+ DTO 안에 DTO)
### DTO를 사용하는 이유
    엔티티 그대로 노출하지 않고 필요한 필드만 뽑아내거나 중첩 구조, 혹은 계층 구조의 데이터를 반환 위함
### QueryDSL에서 DTO 매핑하는 방식
1. Projections.constructor(Class<T> dtoClass, …)
2. Projections.bean() 또는 Projections.fields()
3. @QueryProjection 어노테이션 + QDTO 생성
### DTO 안에 또 다른 DTO를 포함하는 경우
QueryDSL 프로젝션이나 groupBy + transform 등을 활용해 계층 구조를 만들 수 있음

예)

    QReview review = QReview.review;
    QStore store = QStore.store;
    
    List<ReviewResponse> responses = queryFactory
        .select(Projections.constructor(
            ReviewResponse.class,
            review.id,
            review.content,
            Projections.constructor(StoreInfo.class,
                store.id,
                store.name,
                store.address
            )
        ))
        .from(review)
        .join(review.store, store)
        .fetch();

## 커스텀 페이지네이션
    QueryDSL에서 페이징을 적용하려면 
    - Pageable / Page 반환을 함께 사용하거나
    - offset(), limit() 메서드를 활용해 
    수동으로 처리하는 법이 있다. 

Spring Data JPA + QueryDSL을 같이 사용할 경우, 

QuerydslRepositorySupport나 
Querydsl + PageImpl 등을 사용해서 Page<T>형태로 반환하도록 설계 가능
---
    JPAQuery<Review> query = queryFactory
        .selectFrom(review)
        .join(review.store, store).fetchJoin()
        .where(builder)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(review.createdAt.desc());
    
    List<Review> content = query.fetch();
    long total = query.fetchCount();  

    return new PageImpl<>(content, pageable, total);

## transform - groupBy
transform() 메서드와 GroupBy 클래스를 사용하면 여러 레코드를 그룹화해 맵이나 리스트 형태로 반환 가능

DTO 내부에 컬렉션이 있는 경우 유용하다.
아래와 같은 예시처럼 PostWithCommentsDto는 Long id, String title, List<CommentDto> comments 형태라고 가정하면
DTO 안에 DTO 리스트 형태 데이터 구조를 만들 수도 있다. 

    QPost post = QPost.post;
    QComment comment = QComment.comment;
    
    Map<Long, PostWithCommentsDto> result = queryFactory
        .from(post)
        .leftJoin(post.comments, comment)
        .transform(
            GroupBy.groupBy(post.id)
                .as(Projections.constructor(PostWithCommentsDto.class,
                    post.id,
                    post.title,
                    GroupBy.list(Projections.constructor(CommentDto.class,
                        comment.id, comment.content)))
            )
        );

## order by null
    MySQL 등에서 정렬을 하지 않도록 하기 위함
    -> 대량 데이터를 조회하면서 정렬이 필요 없을 때 사용해 성능을 최적화하기도 함
    
    QueryDSL 내부에서는 직접 orderBy(…) 대신 
    orderBy(ExpressionUtils.constant(1)) 또는 
    orderBy(new OrderSpecifier<>(Order.NONE, …)) 등의 우회 방식이 될 수 있음

    JPA/QueryDSL에서 정렬이 별도로 필요 없을 경우 orderBy()를 아예 생략하는 것이 일반적
    
    *하지만 SQL 생성 시 “ORDER BY null”을 직접 넣고 싶을 경우 
    native query 형태 또는 Expressions.stringTemplate("null") 등 사용 가능
