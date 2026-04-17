renderSidebar('dashboard');

(async () => {
  const user = getUser();
  const data = await apiFetch(`/dashboard?usuarioId=${user.id}`);

  document.getElementById('stats').innerHTML = `
    <div class="card"><div class="muted">Empresas</div><div class="stat-number">${data.totalEmpresas ?? 0}</div></div>
    <div class="card"><div class="muted">Questões</div><div class="stat-number">${data.totalQuestoes ?? 0}</div></div>
    <div class="card"><div class="muted">Avaliações registradas</div><div class="stat-number">${data.totalAvaliacoes ?? 0}</div></div>
    <div class="card"><div class="muted">Serviços TI</div><div class="stat-number">${data.totalServicosTI ?? 0}</div></div>
    <div class="card"><div class="muted">Incidentes</div><div class="stat-number">${data.totalIncidentes ?? 0}</div></div>
    <div class="card"><div class="muted">Incidentes abertos</div><div class="stat-number">${data.incidentesAbertos ?? 0}</div></div>
    <div class="card"><div class="muted">Fora do SLA</div><div class="stat-number">${data.incidentesForaSla ?? 0}</div></div>
    <div class="card"><div class="muted">Relatórios gerados</div><div class="stat-number">${data.totalRelatorios ?? 0}</div></div>
  `;

  document.getElementById('dashboardSubtext').textContent =
    `${data.ultimaAvaliacao || 'Nenhuma avaliação concluída'} • ${data.totalServicosTI ?? 0} serviço(s) mapeado(s) • ${data.totalIncidentes ?? 0} incidente(s) registrado(s)`;

  const insights = [];
  if ((data.incidentesForaSla ?? 0) > 0) insights.push(`Há ${data.incidentesForaSla} incidente(s) fora do SLA. Priorize a estabilização de serviços críticos.`);
  if ((data.incidentesAbertos ?? 0) > 3) insights.push(`Existem ${data.incidentesAbertos} incidente(s) abertos. Reforce triagem, escalonamento e resolução.`);
  if ((data.totalServicosTI ?? 0) === 0) insights.push('Ainda não há serviços de TI catalogados. Estruture o catálogo para fortalecer governança operacional.');
  if ((data.totalAvaliacoes ?? 0) === 0) insights.push('Ainda não há avaliações concluídas. Gere o primeiro diagnóstico para orientar a evolução da TI.');
  if (!insights.length) insights.push('O ambiente está estável. Use os relatórios segmentados para aprofundar a análise de governança, segurança, riscos e serviços.');

  document.getElementById('dashboardInsights').innerHTML = insights.map(i => `<p>• ${i}</p>`).join('');
})();
