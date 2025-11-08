# WEEK 6 - 제이/한종서

## 미션

QueryDSL로 내가 작성한 리뷰 보기 API 구현하기

### 본문

1. QueryDSL 설치 과정 인증하기

```yaml
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

  // QueryDSL : OpenFeign
  implementation "io.github.openfeign.querydsl:querydsl-jpa:7.0"
  implementation "io.github.openfeign.querydsl:querydsl-core:7.0"
  annotationProcessor "io.github.openfeign.querydsl:querydsl-apt:7.0:jpa"
  annotationProcessor "jakarta.persistence:jakarta.persistence-api"
  annotationProcessor "jakarta.annotation:jakarta.annotation-api"
}

tasks.named('test') {
	useJUnitPlatform()
}

// QueryDSL 관련 설정
// generated/querydsl 폴더 생성 & 삽입
def querydslDir = layout.buildDirectory.dir("generated/querydsl").get().asFile

// 소스 세트에 생성 경로 추가 (구체적인 경로 지정)
sourceSets {
    main.java.srcDirs += [ querydslDir ]
}

// 컴파일 시 생성 경로 지정
tasks.withType(JavaCompile).configureEach {
    options.generatedSourceOutputDirectory.set(querydslDir)
}

// clean 태스크에 생성 폴더 삭제 로직 추가
clean.doLast {
    file(querydslDir).deleteDir()
}
```

워크북의 해당 코드를 `build.gradle`에 넣어주고 동기화해준 뒤 프로젝트를 실행하였다.

Windows의 경우 OneDrive 동기화를 꺼야 디렉토리 생성 권한이 허용되는 듯 하다.

![Image](/week06/mission/mission_1.png)

![Image](/week06/mission/mission_2.png)

<br>

2. QueryDSL로 내가 작성한 리뷰 보기 API 구현하기

```java
package com.example.demo.domain.review.repository;

import com.example.demo.domain.review.entity.Review;

import java.util.List;

public interface ReviewQueryDsl {
    List<Review> findReviewsByFilter(Long memberId, String storeName, Integer starGroup);
}
```

QueryDSL을 사용하겠다는 별도의 인터페이스를 정의하고,

```java
package com.example.demo.domain.review.repository;

import com.example.demo.domain.review.entity.QReview;
import com.example.demo.domain.review.entity.Review;
import com.example.demo.domain.store.entity.QStore;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewQueryDslImpl implements ReviewQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findReviewsByFilter(Long memberId, String storeName, Integer starGroup) {
        QReview review = QReview.review;
        QStore store = QStore.store;

        // 기본 쿼리: 내가 작성한 리뷰
        var query = queryFactory
                .selectFrom(review)
                .join(review.store, store).fetchJoin()
                .where(review.member.id.eq(memberId));

        // 가게별
        if (storeName != null && !storeName.isEmpty()) {
            query.where(store.name.eq(storeName));
        }

        // 별점별
        if (starGroup != null) {
            double minStar = starGroup;
            double maxStar = starGroup + 0.9;
            query.where(review.star.between(minStar, maxStar));
        }

        // 최신순으로 정렬
        return query
                .orderBy(review.createdAt.desc())
                .fetch();
    }
}
```

인터페이스의 메서드를 요구사항에 맞게 QueryDSL로 구현한다.

여기서 `JPAQueryFactory`는 스프링 빈으로 등록해주어야 스프링이 의존성 주입을 해줄 수 있다.

`demo/global/config/` 디렉토리(프로젝트 전역 설정 패키지)에

```java
package com.example.demo.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
```

와 같이 @Configuration, @Bean 애노테이션으로 스프링 빈으로 등록해준다.

기존에 JpaRepository<Review, Long>을 상속하던 `ReviewRepository` 인터페이스가 `ReviewQueryDsl` 인터페이스도 상속하도록 코드를 추가해준 뒤,

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

컨트롤러 계층을 만들고 API를 구현하였다.

여기서 API 엔드포인트는 `GET /api/reviews/test`로 설정해주었다.

![Image](/week06/mission/mission_3.png)

이후 다음과 같이 가상의 리뷰 데이터를 DB에 추가해주었다.

물론, 연관관계 때문에 리뷰 데이터를 추가하기 전에 member와 store에 대한 데이터를 먼저 생성해주어야 한다.

![Image](/week06/mission/mission_4.png)

![Image](/week06/mission/mission_5.png)

`Postman` 툴로 request를 날려보니 잘 응답하는 것을 확인할 수 있었다.

이는 최종 결과이고, 사실 Review → Store → Local → Store → Review ...로 순환 관계가 양쪽에서 계속 직렬화되면서 JSON이 계속 중첩되는 문제가 발생했었다.

```java
package com.example.demo.domain.review.dto;

import com.example.demo.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private String storeName;
    private Float star;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getStore().getName(),
                review.getStar(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
```

그래서 다음과 같은 DTO를 두어 엔티티를 그대로 반환하지 않게 하여 문제를 해결하였다.

```java
public List<ReviewResponseDto> getMyReviews(Long memberId, String storeName, Integer starGroup) {
        List<Review> reviews = reviewRepository.findReviewsByFilter(memberId, storeName, starGroup);

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .toList();
}
```

위처럼 메서드를 수정하였고, 서비스 계층에서 레포지토리 계층으로부터 리뷰 데이터들을 가져온 뒤 DTO로 변환하여 컨트롤러에게 넘겨준다.

[미션 레포지토리](https://github.com/Dosp74/demo/tree/feature/chapter6)