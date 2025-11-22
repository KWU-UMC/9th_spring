# WEEK 8 - 제이/한종서

## 미션

### 본문

1. 특정 지역에 가게 추가하기 API 구현하기

```java
package com.example.demo.domain.store.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StoreReqDTO {
    // private final 필드, 생성자, getter, toString, equals, hashCode가 전부 자동 생성된다.
    public record CreateDTO(
            @NotNull Long localId,
            @NotBlank String name,
            @NotNull Long bossNumber,
            @NotBlank String address
    ) {}
}
```

```java
package com.example.demo.domain.store.dto.res;

import lombok.Builder;

public class StoreResDTO {
    @Builder
    public record CreateDTO(
            Long storeId,
            String storeName
    ) {}
}
```

`StoreReqDTO`와 `StoreResDTO`를 만들어주고,

```java
package com.example.demo.domain.store.converter;

import com.example.demo.domain.store.dto.req.StoreReqDTO;
import com.example.demo.domain.store.dto.res.StoreResDTO;
import com.example.demo.domain.store.entity.Local;
import com.example.demo.domain.store.entity.Store;

public class StoreConverter {
    // DTO -> 객체
    public static Store toStore(StoreReqDTO.CreateDTO dto, Local local) {
        return Store.builder()
                .local(local)
                .name(dto.name())
                .bossNumber(dto.bossNumber())
                .address(dto.address())
                .build();
    }

    // 객체 -> DTO
    public static StoreResDTO.CreateDTO toCreateDTO(Store store) {
        return StoreResDTO.CreateDTO.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .build();
    }
}
```

Converter를 구현한다.

```java
package com.example.demo.domain.store.exception;

import com.example.demo.global.apiPayload.code.BaseErrorCode;
import com.example.demo.global.apiPayload.exception.GeneralException;

public class StoreException extends GeneralException {
    public StoreException(BaseErrorCode code) {
        super(code);
    }
}
```

```java
package com.example.demo.domain.store.exception.code;

import com.example.demo.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
    LOCAL_NOT_FOUND(HttpStatus.NOT_FOUND,
            "STORE404_1",
            "해당 지역을 찾을 수 없습니다."),
    STORE_NAME_DUPLICATED(HttpStatus.BAD_REQUEST,
            "STORE400_1",
            "이미 존재하는 가게 이름입니다."),
    BOSS_NUMBER_DUPLICATED(HttpStatus.BAD_REQUEST,
            "STORE400_2",
            "이미 등록된 사업자 번호입니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

예외 코드를 만들고,

```java
package com.example.demo.domain.store.repository;

import com.example.demo.domain.store.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
}
```

```java
package com.example.demo.domain.store.repository;

import com.example.demo.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 중복 검사 메서드
    boolean existsByName(String name);
    boolean existsByBossNumber(Long bossNumber);
}
```

필요한 레포지토리 계층을 둔다.

```java
package com.example.demo.domain.store.service.command;

import com.example.demo.domain.store.dto.req.StoreReqDTO;
import com.example.demo.domain.store.dto.res.StoreResDTO;

public interface StoreCommandService {
    StoreResDTO.CreateDTO createStore(StoreReqDTO.CreateDTO dto);
}
```

```java
package com.example.demo.domain.store.service.command;

import com.example.demo.domain.store.converter.StoreConverter;
import com.example.demo.domain.store.dto.req.StoreReqDTO;
import com.example.demo.domain.store.dto.res.StoreResDTO;
import com.example.demo.domain.store.entity.Local;
import com.example.demo.domain.store.entity.Store;
import com.example.demo.domain.store.exception.StoreException;
import com.example.demo.domain.store.exception.code.StoreErrorCode;
import com.example.demo.domain.store.repository.LocalRepository;
import com.example.demo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreCommandServiceImpl implements StoreCommandService {
    private final LocalRepository localRepository;
    private final StoreRepository storeRepository;

    @Override
    public StoreResDTO.CreateDTO createStore(StoreReqDTO.CreateDTO dto) {
        // 지역 존재 여부 검증
        Local local = localRepository.findById(dto.localId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.LOCAL_NOT_FOUND));

        // 가게 이름 중복 검사
        if (storeRepository.existsByName(dto.name())) {
            throw new StoreException(StoreErrorCode.STORE_NAME_DUPLICATED);
        }

        // 사업자 번호 중복 검사
        if (storeRepository.existsByBossNumber(dto.bossNumber())) {
            throw new StoreException(StoreErrorCode.BOSS_NUMBER_DUPLICATED);
        }

        Store store = StoreConverter.toStore(dto, local);
        storeRepository.save(store);

        return StoreConverter.toCreateDTO(store);
    }
}
```

리소스를 생성하는 행위는 POST이므로 command 디렉토리에서 서비스의 인터페이스와 구현체를 구현한다.

```java
package com.example.demo.domain.store.controller;

import com.example.demo.domain.store.dto.req.StoreReqDTO;
import com.example.demo.domain.store.dto.res.StoreResDTO;
import com.example.demo.domain.store.service.command.StoreCommandService;
import com.example.demo.global.apiPayload.ApiResponse;
import com.example.demo.global.apiPayload.code.GeneralSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreCommandService storeCommandService;

    @PostMapping
    public ApiResponse<StoreResDTO.CreateDTO> createStore(@RequestBody @Valid StoreReqDTO.CreateDTO dto) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, storeCommandService.createStore(dto));
    }
}
```

이후 REST API를 구현한다.

![Image](/week08/mission/mission_1.png)

![Image](/week08/mission/mission_2.png)

나머지 코드들을 해당 md 파일에 넣으면 너무 내용이 길어지기 때문에 해당 기능을 구현한 커밋 URL만 제출하고자 한다.

2. 가게에 리뷰 추가하기 API 구현하기

파일 첨부 기능은 구현하지 않았다...

[2번 미션 커밋 보러가기](https://github.com/dosp74/demo/commit/a7143d0f471bf1cec7c8cf50ffb475235cdad2fa)

![Image](/week08/mission/mission_3.png)

![Image](/week08/mission/mission_4.png)

3. 가게에 미션 추가하기 API 구현하기

[3번 미션 커밋 보러가기](https://github.com/dosp74/demo/commit/9805b13b5c694dabf38eff585ed7ddc479e4cf7d)

![Image](/week08/mission/mission_5.png)

![Image](/week08/mission/mission_6.png)

4. 가게의 미션을 도전 중인 미션에 추가(미션 도전하기) API 구현하기

```java
package com.example.demo.domain.mission.controller;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.mission.dto.res.MissionResDTO;
import com.example.demo.domain.mission.service.command.MissionCommandService;
import com.example.demo.global.apiPayload.ApiResponse;
import com.example.demo.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MemberMissionController {
    private final MissionCommandService missionCommandService;
    private final MemberRepository memberRepository; // 사용자 임시 조회용

    @PostMapping("/{missionId}/challenge")
    public ApiResponse<MissionResDTO.MemberMissionChallengeDTO> challengeMission(
            @PathVariable Long missionId
    ) {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        MissionResDTO.MemberMissionChallengeDTO response = missionCommandService.challengeMission(member, missionId);

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, response);
    }
}
```

회원가입, 로그인 기능이 없기 때문에 미리 생성해둔 1번 유저를 테스트에 사용하였다.

[4번 미션 커밋 보러가기](https://github.com/dosp74/demo/commit/9f802860719256752f5376bc45f21c26b866df49)

![Image](/week08/mission/mission_7.png)

![Image](/week08/mission/mission_8.png)

[미션 레포지토리](https://github.com/dosp74/demo/tree/feature/chapter8)