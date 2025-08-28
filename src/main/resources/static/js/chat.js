(() => {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    // URL/스토리지에서 메타 정보 로드
    const qs = new URLSearchParams(location.search);
    const roomId = Number(qs.get('roomId'));
    const userId = Number(localStorage.getItem('userId'));
    let roomCode = qs.get('roomCode') || sessionStorage.getItem(`roomCode:${roomId}`);
    const nickname = localStorage.getItem('nickname');

    if (!roomId || !userId || !nickname) {
      location.assign('/');
      return;
    }
    if (roomCode) {
      sessionStorage.setItem(`roomCode:${roomId}`, roomCode);
    }

    const metaEl  = document.getElementById('meta');
    const msgsEl  = document.getElementById('msgs');
    const formEl  = document.getElementById('send');
    const inputEl = document.getElementById('text');
    const leaveEl = document.getElementById('leave');

    metaEl.textContent = `roomId=${roomId}, roomCode=${roomCode ?? '-'}, userId=${userId}, nickname=${nickname}`;

    // 메시지 렌더링
    function addMsg(m) {
      const el = document.createElement('div');
      el.className = 'msg';
      el.innerHTML = `<b>${m.nickname}</b> <span class="dim">${m.messageType}</span><br/>${m.content}`;
      msgsEl.appendChild(el);
      el.scrollIntoView();
    }

    // STOMP 연결
    // SockJS, Stomp는 CDN으로 먼저 로드됨(HTML에서 defer 순서 준수)
    const socket = new SockJS('/ws-stomp');
    const stomp  = Stomp.over(socket);
    stomp.debug  = () => {};

    stomp.connect(
      { 'user-id': String(userId), 'login': String(userId) },
      () => {
        // 구독
        stomp.subscribe(`/topic/room.${roomId}`, (frame) => {
          try {
            const payload = JSON.parse(frame.body);
            addMsg(payload);
          } catch (e) {
            console.error('Payload parse error:', e, frame.body);
          }
        });

        // 입장 브로드캐스트
        stomp.send('/app/chat.join', {}, JSON.stringify({
          messageType: 'JOIN',
          roomId,
          senderId: userId,
          nickname,
          content: `${nickname}님이 입장했습니다.`
        }));
      },
      (err) => {
        alert('웹소켓 연결 실패');
        console.error(err);
      }
    );

    // 메시지 전송
    formEl.addEventListener('submit', (e) => {
      e.preventDefault();
      const text = inputEl.value.trim();
      if (!text) return;
      stomp.send('/app/chat.send', {}, JSON.stringify({
        messageType: 'TALK',
        roomId,
        senderId: userId,
        nickname,
        content: text
      }));
      inputEl.value = '';
    });

    // 나가기
    leaveEl.addEventListener('click', async () => {
      try {
        const res = await fetch('/rooms/leave', {
          method: 'POST',
          headers: { 'content-type': 'application/json' },
          body: JSON.stringify({ roomId, userId })
        });
        if (!res.ok) console.warn('leave API not ok:', res.status);
      } catch (e) {
        console.warn('leave API error:', e);
      } finally {
        try { stomp.disconnect(() => {}); } catch {}
        location.assign('/rooms');
      }
    });

    // 페이지 종료시(탭 닫기/새로고침) 서버에 퇴장 알림
    window.addEventListener('beforeunload', () => {
      try {
        const data = JSON.stringify({ roomId, userId });
        const blob = new Blob([data], { type: 'application/json' });
        navigator.sendBeacon('/rooms/leave', blob);
      } catch (_) {}
    });
  });
})();
