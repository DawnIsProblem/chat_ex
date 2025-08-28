## 📋 Chatting Example
이 프로젝트는 **WebSocket과** **Stomp를** 이용하여 채팅방을 생성하고 실시간 채팅을 할 수 있는 예제를 다룬 프로젝트입니다.
프론트 페이지는 **thymeleaf를** 사용하여 간단하게 작성하였습니다.

## 🚀 How To Run
`./gradlew bootRun`

## 🛠️ Skill
| 구분            | 스택/도구                                                                                                    |
| ------------- | -------------------------------------------------------------------------------------------------------- |
| **Backend**   | `Spring Boot 3`<br>`Spring Web`<br>`Spring Messaging (WebSocket/STOMP)`<br>`Spring Data JPA`<br>`Lombok` |
| **DB**        | `MySQL` |
| **Frontend**  | 정적 `HTML` + 바닐라 `JS`<br>`SockJS`<br>`STOMP.js`|
| **옵션(확장 준비)** | `StompAuthInterceptor` (추후 `JWT` 연동 대비)|

## 📍 Flow
1. 닉네임 입력
- POST /users 성공 → localStorage(userId, nickname) 저장 → /rooms로 이동

2. 방 생성/입장
- 생성: POST /rooms → {roomId, roomCode} → /chat?roomId=..&roomCode=..
- 입장: POST /rooms/join → {roomId, roomCode} → 채팅 페이지 이동

3. 채팅
- STOMP 연결 → /topic/room.{roomId} 구독
- 메시지 전송은 /app/chat.send 로 발행
- 나가기는 POST /rooms/leave 후 /rooms로 복귀
- 입장/퇴장/대화 메시지는 모두 DB에 저장되고 구독자에게 브로드캐스트
