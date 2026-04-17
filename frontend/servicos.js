renderSidebar('servicos');

async function listar() {
  const empresaId = Number(document.getElementById('empresaId').value);
  if (!empresaId) return;

  const data = await apiFetch(`/servicos?empresaId=${empresaId}`);
  servicoTabela.innerHTML = data.map(s => `
    <tr>
      <td>${s.nome}</td>
      <td>${s.categoria}</td>
      <td>${s.responsavel || '-'}</td>
      <td>${s.slaHoras ? s.slaHoras + 'h' : '-'}</td>
      <td>${s.status}</td>
      <td>
        <button class="btn btn-secondary" onclick='editar(${JSON.stringify(JSON.stringify(s))})'>Editar</button>
        <button class="btn btn-danger" onclick='excluirServico(${s.id})'>Excluir</button>
      </td>
    </tr>
  `).join('') || `<tr><td colspan="6" class="empty">Nenhum serviço cadastrado.</td></tr>`;
}

function editar(payload) {
  const s = JSON.parse(payload);
  servicoId.value = s.id;
  empresaId.value = s.empresaId;
  nome.value = s.nome;
  categoria.value = s.categoria;
  responsavel.value = s.responsavel || '';
  slaHoras.value = s.slaHoras || '';
  status.value = s.status || 'Ativo';
  descricao.value = s.descricao || '';
}

async function excluirServico(id) {
  await apiFetch(`/servicos/${id}`, { method: 'DELETE' });
  listar();
}

servicoForm.addEventListener('submit', async ev => {
  ev.preventDefault();

  const body = JSON.stringify({
    empresaId: Number(empresaId.value),
    nome: nome.value,
    descricao: descricao.value,
    categoria: categoria.value,
    responsavel: responsavel.value,
    slaHoras: slaHoras.value ? Number(slaHoras.value) : null,
    status: status.value
  });

  if (servicoId.value) {
    await apiFetch(`/servicos/${servicoId.value}`, { method: 'PUT', body });
  } else {
    await apiFetch('/servicos', { method: 'POST', body });
  }

  servicoForm.reset();
  servicoId.value = '';
  status.value = 'Ativo';
  listar();
});

empresaId.addEventListener('change', listar);

(async function init() {
  await carregarEmpresasSelect('empresaId');
  status.value = 'Ativo';
  await listar();
})();