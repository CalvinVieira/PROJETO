renderSidebar('empresas');
const user = getUser();
async function listar() {
 const data = await apiFetch(`/empresas?usuarioId=${user.id}`);
 empresaTabela.innerHTML = data.map(e => `<tr><td>${e.nome}</td><td>${e.segmento||'-'}</td><td>${e.porte||'-'}</td><td><button class="btn btn-secondary" onclick='editar(${JSON.stringify(JSON.stringify(e))})'>Editar</button> <button class="btn btn-danger" onclick='excluirEmpresa(${e.id})'>Excluir</button></td></tr>`).join('') || `<tr><td colspan="4" class="empty">Nenhuma empresa cadastrada.</td></tr>`;
}
function editar(payload){ const e = JSON.parse(payload); empresaId.value=e.id; nome.value=e.nome; segmento.value=e.segmento||''; porte.value=e.porte||'Médio'; }
async function excluirEmpresa(id){ await apiFetch(`/empresas/${id}?usuarioId=${user.id}`, {method:'DELETE'}); listar(); }
empresaForm.addEventListener('submit', async ev => {
 ev.preventDefault();
 const body = JSON.stringify({nome:nome.value,segmento:segmento.value,porte:porte.value,usuarioId:user.id});
 if (empresaId.value) await apiFetch(`/empresas/${empresaId.value}`, {method:'PUT',body}); else await apiFetch('/empresas',{method:'POST',body});
 empresaForm.reset(); empresaId.value=''; listar();
});
listar();
