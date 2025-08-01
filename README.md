# project1

## 📍 한예진 담당 영역

### 1. 가게 등록 API

## 🔧 현재 상태

- JWT 인증 없음
- DB 연동 없음 (추후 PostgreSQL 예정)
- 기본 가게 등록 API만 구현

```json
{
  "id": "store-001",
  "name": "김밥천국",
  "address1": "서울시 종로구",
  "address2": "새문안로3길 36",
  "category": "한식"
}
```

## 📍payments Postman Test 방법

### 1. 로그인 후 Beerer Toekn 발급

## Post: localhost:8080/api/user/login

```json
{
  "loginId": "gil123",
  "password": "password123"
}
```

### 2. 인증결제

## Post: localhost:8080/api/payment/ready

Authorization => Bearer Token에 로그인시 발급받은 Token 입력

````
{
  "orderId": "3f8a45a1-01b3-4e7e-9406-b1d972a6e7df",
  "amount": 10000,
  "orderName": "아메리카노 1잔",
  "customerEmail": "gildong@example.com",
  "successUrl": "http://localhost:8080/api/payment/success",
  "failUrl": "http://localhost:8080/api/payment/fail"
}
````
