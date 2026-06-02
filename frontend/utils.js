protect();

function renderSidebar(active = 'dashboard') {
  const user = getUser();
  const menu = [
    ['dashboard', 'Dashboard', 'dashboard.html'],
    ['empresas', 'Empresas', 'empresas.html'],
    ['questoes', 'Questões', 'questoes.html'],
    ['servicos', 'Serviços TI', 'servicos.html'],
    ['incidentes', 'Incidentes', 'incidentes.html'],
    ['avaliacao', 'Avaliação', 'avaliacao.html'],
    ['relatorio', 'Relatório Executivo', 'relatorio.html'],
    ['governanca', 'Governança & PDTI', 'governanca.html'],
    ['governanca-resultados', 'Resultados de Governança', 'governanca-resultados.html'],
    ['servicos-resultados', 'Resultados de Serviços', 'servicos-resultados.html'],
    ['seguranca-resultados', 'Resultados de Segurança', 'seguranca-resultados.html'],
    ['riscos-resultados', 'Resultados de Riscos', 'riscos-resultados.html']
  ].map(([key, label, href]) => `<a class="${active === key ? 'active' : ''}" href="${href}">${label}</a>`).join('');

  const badge = user?.perfil ? `<div class="user-badge">${user.perfil}</div>` : '';
  document.getElementById('sidebar').innerHTML = `
    <a href="index.html"><img src="assets/logo-branca.png" class="sidebar-logo" alt="Stratec TI"></a>
    <div class="muted sidebar-user">Olá, ${user?.nome || ''}</div>
    ${badge}
    <div>${menu}</div>
    <a href="#" onclick="logout()">Sair</a>`;
}

async function apiFetch(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({ error: 'Erro inesperado' }));
    throw new Error(err.error || 'Erro inesperado');
  }

  return res.status === 204 ? null : res.json();
}

async function carregarEmpresasSelect(id) {
  const user = getUser();
  const empresas = await apiFetch(`/empresas?usuarioId=${user.id}`);
  document.getElementById(id).innerHTML = empresas.map(e => `<option value="${e.id}">${e.nome}</option>`).join('');
}
