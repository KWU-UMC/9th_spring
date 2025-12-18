1. **GitHub 저장소 주소**
> [Feat/Chapter10](https://github.com/choehyeonjin/UMC-9th-spring-study/commits/Feat/Chapter10/)
2. **실습 1: Spring Security를 활용한 로그인 및 회원가입 구현 = Session 방식**
    - 회원가입
        - Swagger 테스트

          ![image.png](./mission_10(1).png)

        - DB 저장
            - DB에 솔트된 비밀번호가 저장된다.

              ![image.png](./mission_10(2).png)

    - 로그인
        - 테스트
            - 회원가입 시 등록한 이메일과 솔트 전 비밀번호로 로그인 시, Swagger 페이지로 접속된다.

              ![image.png](./mission_10(3).png)

              ![image.png](./mission_10(4).png)

2. **실습 2: Spring Security를 활용한 로그인 및 회원가입 구현 = JWT 방식**
    - Swagger 테스트
        - JWT 없이 요청 시
            - 커스텀 에러가 발생한다.

              ![image.png](./mission_10(5).png)

        - 잘못된 JWT(hello) 입력 시
            - 마찬가지로 커스텀 에러가 발생한다.

              ![image.png](./mission_10(6).png)

        - 정상적으로 JWT 헤더를 붙였을 시
            - 로그인하여 액세스 토큰을 발급받는다.
                
                ![image.png](./mission_10(7).png)
                
            - 스웨거 내 Authorize 기능을 통해 발급 받은 액세스 토큰을 입력하고, 모든 요청에 JWT 헤더를 붙인다.
                
                ![image.png](./mission_10(8).png)
                
            - 성공 메시지를 확인했다.
                
                ![image.png](./mission_10(9).png)



