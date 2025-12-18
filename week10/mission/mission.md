# WEEK 10 - 제이/한종서

## 미션

### 본문

1. 실습 1: Spring Security를 활용한 로그인 및 회원가입 구현 = Session 방식

![Image](/week10/mission/mission_1.png)

회원가입 요청

![Image](/week10/mission/mission_2.png)

회원가입 성공 응답

![Image](/week10/mission/mission_3.png)

MemberCommandServiceImpl 클래스의 PasswordEncoder 객체에 의해 회원가입 로직에서 비밀번호가 암호화되어 DB에 저장된다.

```java
String salt = passwordEncoder.encode(dto.password());
```

encode 메서드를 통해 암호화한 값을 사용자의 비밀번호로 저장하고

```java
Member member = MemberConverter.toMember(dto, salt, Role.ROLE_USER);
```

해당 정보를 바탕으로 Member 객체를 생성한다.

![Image](/week10/mission/mission_4.png)

회원가입한 정보로 로그인을 해보면,

![Image](/week10/mission/mission_5.png)

설정해둔 경로로 잘 접속되는 것을 볼 수 있다.

<br>

2. 실습 2: Spring Security를 활용한 로그인 및 회원가입 구현 = JWT 방식

![Image](/week10/mission/mission_6.png)

회원가입 요청

![Image](/week10/mission/mission_7.png)

회원가입 성공 응답

![Image](/week10/mission/mission_8.png)

가입한 정보로 로그인 시도

![Image](/week10/mission/mission_9.png)

로그인 성공 응답

![Image](/week10/mission/mission_10.png)

받은 JWT Token을 Swagger에 등록해두고,

![Image](/week10/mission/mission_11.png)

test API를 요청하면 성공하는 것을 볼 수 있다.

[미션 레포지토리]
(https://github.com/dosp74/demo/tree/feature/chapter10)
