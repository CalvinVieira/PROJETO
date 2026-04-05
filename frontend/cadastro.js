document.getElementById('cadastroForm').addEventListener('submit', async e => {
  e.preventDefault();
  const msg = document.getElementById('msg');
  try {
    const user = await fetch(`${API_BASE}/auth/register`, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({nome:nome.value,email:email.value,senha:senha.value})}).then(async r=>{const j=await r.json(); if(!r.ok) throw new Error(j.error||'Erro'); return j;});
    setUser(user); location.href='dashboard.html';
  } catch (err) { msg.innerHTML = `<div class="card" style="border-color:#fecaca;color:#991b1b">${err.message}</div>`; }
});