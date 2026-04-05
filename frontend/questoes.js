renderSidebar('questoes');
async function listar() {
 const data = await apiFetch('/questoes');
 questaoTabela.innerHTML = data.map(q => `<tr><td>${q.categoria}</td><td>${q.pergunta}</td><td>${q.peso}</td><td><button class="btn btn-secondary" onclick='editar(${JSON.stringify(JSON.stringify(q))})'>Editar</button> <button class="btn btn-danger" onclick='excluirQuestao(${q.id})'>Excluir</button></td></tr>`).join('');
}
function editar(payload){ const q = JSON.parse(payload); questaoId.value=q.id; pergunta.value=q.pergunta; categoria.value=q.categoria; peso.value=q.peso; }
async function excluirQuestao(id){ await apiFetch(`/questoes/${id}`, {method:'DELETE'}); listar(); }
questaoForm.addEventListener('submit', async ev => {
 ev.preventDefault(); const body = JSON.stringify({pergunta:pergunta.value,categoria:categoria.value,peso:Number(peso.value)});
 if (questaoId.value) await apiFetch(`/questoes/${questaoId.value}`, {method:'PUT',body}); else await apiFetch('/questoes', {method:'POST',body});
 questaoForm.reset(); questaoId.value=''; listar();
});
listar();
