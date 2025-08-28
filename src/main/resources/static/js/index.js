document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('user-form');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const nickname = document.getElementById('nickname').value.trim();
    if (!nickname) return;

    const res = await fetch('/users', {
      method: 'POST',
      headers: { 'content-type': 'application/json' },
      body: JSON.stringify({ nickname })
    });

    const json = await res.json();
    const ok =
      res.ok &&
      json &&
      typeof json.status === 'number' &&
      (json.status === 200 || json.status === 201) &&
      json.status === res.status &&
      json.data &&
      typeof json.data.id !== 'undefined' &&
      typeof json.data.nickname === 'string';

    console.log(ok);

    if (!ok) {
      alert('유저 생성 실패: ' + (json.message || res.status));
      return;
    }

    const { id, nickname: nick } = json.data;
    localStorage.setItem('userId', id);
    localStorage.setItem('nickname', nick);

    window.location.assign('/rooms');
  });
});