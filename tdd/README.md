## API 목록

### 판매자 회원 가입

요청

- 메서드 : POST
- URL : /api/v1/seller/signup
- 헤더 : Content-Type : application/json

- 본문

```
    CreateSellerCommand {
    email:string,
    username:string,
    password:string
    }
```

성공 응답
- 상태코드 : 204 No Content

정책

테스트
- [X] 올바르게 요청하면 204 No Content 상태코드를 반환한다.
- [X] email 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] email 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] username 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] username 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] password 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] password 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다.
- [X] email 속성에 이미 존재하는 이메일 주소가 지정되면 400 Bad Request 상태코드를 반환한다.
- [X] username 속성에 이미 존재하는 사용자이름이 지정되면 400 Bad Request 상태코드를 반환한다.
- [X] 비밀번호를 올바르게 암호화한다.