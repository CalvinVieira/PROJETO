protect();
function renderSidebar(active='dashboard') {
  const user = getUser();
  const menu = [['dashboard','Dashboard','dashboard.html'],['empresas','Empresas','empresas.html'],['questoes','Questões','questoes.html'],['servicos','Serviços TI','servicos.html'],['incidentes','Incidentes','incidentes.html'],['avaliacao','Avaliação','avaliacao.html'],['relatorio','Relatórios','relatorio.html']]
    .map(([key,label,href]) => `<a class="${active===key?'active':''}" href="${href}">${label}</a>`).join('');
  document.getElementById('sidebar').innerHTML = `<a href="index.html"><img src="assets/logo-branca.png" class="sidebar-logo"></a><div class="muted" style="color:#bfdbfe;margin-bottom:12px">Olá, ${user?.nome||''}</div><div>${menu}</div><a href="#" onclick="logout()">Sair</a>`;
}
async function apiFetch(path, options={}) { const res = await fetch(`${API_BASE}${path}`, {headers:{'Content-Type':'application/json'},...options}); if(!res.ok){const err=await res.json().catch(()=>({error:'Erro inesperado'})); throw new Error(err.error||'Erro inesperado')} return res.status===204?null:res.json();}
async function carregarEmpresasSelect(id){ const user=getUser(); const empresas=await apiFetch(`/empresas?usuarioId=${user.id}`); document.getElementById(id).innerHTML = empresas.map(e=>`<option value="${e.id}">${e.nome}</option>`).join(''); }