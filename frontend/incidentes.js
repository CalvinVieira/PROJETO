renderSidebar('incidentes');

async function carregarServicos() {
  const empresa = Number(document.getElementById('empresaId').value);
  servicoId.innerHTML = '<option value="">Sem serviço vinculado</option>';
  if (!empresa) return;
  const servicos = await apiFetch(`/servicos?empresaId=${empresa}`);
  servicoId.innerHTML += servicos.map(s => `<option value="${s.id}">${s.nome}</option>`).join('');
}

async function listar() {
  const empresa = Number(document.getElementById('empresaId').value);
  if (!empresa) return;

  const data = await apiFetch(`/incidentes?empresaId=${empresa}`);
  incidenteTabela.innerHTML = data.map(i => `
    <tr>
      <td>${i.titulo}</td>
      <td>${i.servicoNome || '-'}</td>
      <td>${i.prioridade}</td>
      <td>${i.status}</td>
      <td>${i.slaHoras ? i.slaHoras + 'h' : '-'}</td>
      <td>${i.dentroSla === null ? '-' : (i.dentroSla ? 'Sim' : 'Não')}</td>
      <td>
        <button class="btn btn-secondary" onclick='editar(${JSON.stringify(JSON.stringify(i))})'>Editar</button>
        <button class="btn btn-danger" onclick='excluirIncidente(${i.id})'>Excluir</button>
      </td>
    </tr>
  `).join('') || `<tr><td colspan="7" class="empty">Nenhum incidente cadastrado.</td></tr>`;
}

function editar(payload) {
  const i = JSON.parse(payload);
  incidenteId.value = i.id;
  empresaId.value = i.empresaId;
  titulo.value = i.titulo;
  prioridade.value = i.prioridade;
  status.value = i.status;
  slaHoras.value = i.slaHoras || '';
  descricao.value = i.descricao || '';

  carregarServicos().then(() => {
    servicoId.value = i.servicoId || '';
  });
}

async function excluirIncidente(id) {
  await apiFetch(`/incidentes/${id}`, { method: 'DELETE' });
  listar();
}

incidenteForm.addEventListener('submit', async ev => {
  ev.preventDefault();

  const body = JSON.stringify({
    empresaId: Number(empresaId.value),
    servicoId: servicoId.value ? Number(servicoId.value) : null,
    titulo: titulo.value,
    descricao: descricao.value,
    prioridade: prioridade.value,
    status: status.value,
    slaHoras: slaHoras.value ? Number(slaHoras.value) : null
  });

  if (incidenteId.value) {
    await apiFetch(`/incidentes/${incidenteId.value}`, { method: 'PUT', body });
  } else {
    await apiFetch('/incidentes', { method: 'POST', body });
  }

  incidenteForm.reset();
  incidenteId.value = '';
  status.value = 'Aberto';
  await carregarServicos();
  await listar();
});

empresaId.addEventListener('change', async () => {
  await carregarServicos();
  await listar();
});

(async function init() {
  await carregarEmpresasSelect('empresaId');
  status.value = 'Aberto';
  await carregarServicos();
  await listar();
})();