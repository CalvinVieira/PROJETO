renderSidebar('dashboard');

(async () => {
  const user = getUser();
  const data = await apiFetch(`/dashboard?usuarioId=${user.id}`);
  document.getElementById('stats').innerHTML = `
    <div class="card"><div class="muted">Empresas</div><div class="stat-number">${data.totalEmpresas ?? 0}</div></div>
    <div class="card"><div class="muted">Questões</div><div class="stat-number">${data.totalQuestoes ?? 0}</div></div>
    <div class="card"><div class="muted">Avaliações registradas</div><div class="stat-number">${data.totalAvaliacoes ?? 0}</div></div>
    <div class="card"><div class="muted">Relatórios gerados</div><div class="stat-number">${data.totalRelatorios ?? 0}</div></div>
  `;
  document.getElementById('dashboardSubtext').textContent = data.ultimaAvaliacao || 'Nenhuma avaliação concluída';
})();
