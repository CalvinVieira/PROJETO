(function () {
  const user = getUser();
  const heroActions = document.getElementById('heroActions');
  const homeMenu = document.getElementById('homeMenu');
  if (!heroActions || !homeMenu) return;

  if (user) {
    heroActions.innerHTML = `
      <a class="btn btn-primary" href="dashboard.html">Voltar ao Dashboard</a>
      <button class="btn btn-secondary" onclick="logout()">Sair</button>`;

    const authLinks = homeMenu.querySelectorAll('.auth-link');
    authLinks.forEach(a => a.remove());
    const dash = document.createElement('a');
    dash.className = 'btn btn-primary';
    dash.href = 'dashboard.html';
    dash.textContent = 'Voltar ao Dashboard';
    homeMenu.appendChild(dash);
  }
})();
