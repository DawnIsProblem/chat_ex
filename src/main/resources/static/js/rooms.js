(() => {
  'use strict';

  const $ = (sel) => document.querySelector(sel);

  const userId = Number(localStorage.getItem('userId'));
  const nickname = localStorage.getItem('nickname');
  if (!userId || !nickname) {
    window.location.replace('/');
    return;
  }
  $('#me').textContent = userId;
  $('#nick').textContent = nickname;

  // 방 만들기
  $('#create-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const res  = await fetch('/rooms', {
      method: 'POST',
      headers: { 'content-type': 'application/json' },
      body: JSON.stringify({ userId })
    });
    const json = await res.json();
    const out  = $('#create-result');

    // 서버 CommonResponse 기준( status/message/data )
    const ok = res.ok && json && json.data;
    if (!ok) {
      out.textContent = '실패: ' + (json.message || res.status);
      return;
    }

    const { roomId, roomCode } = json.data;
    out.textContent = `생성됨 • 코드 ${roomCode}`;
    window.location.assign(`/chat?roomId=${roomId}&roomCode=${encodeURIComponent(roomCode)}`);
  });

  // 코드 입력 후 입장
  $('#join-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const codeInput = $('#roomCode').value.trim();
    if (!codeInput) return;

    const out = $('#join-result');

    try {
      const res  = await fetch('/rooms/join', {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({ roomCode: codeInput, userId })
      });
      const json = await res.json();
      console.log('[JOIN RES]', json);

      if (!res.ok || !json || !json.data) {
        out.textContent = '실패: ' + ((json && json.message) || res.status);
        return;
      }

      const { roomId, roomCode } = json.data;
      if (!roomId || !roomCode) {
        out.textContent = '실패: 잘못된 서버 응답';
        return;
      }

      window.location.assign(`/chat?roomId=${roomId}&roomCode=${encodeURIComponent(roomCode)}`);
    } catch (err) {
      console.error(err);
      out.textContent = '실패: 네트워크 오류';
    }
  });
})();
