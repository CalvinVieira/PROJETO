renderSidebar('avaliacao');

let grupos = {}, categorias = [], current = 0, respostas = {};

carregarEmpresasSelect('empresaSelect');

carregarBtn.addEventListener('click', async () => {
  const questoes = await apiFetch('/questoes');
  grupos = questoes.reduce((acc, q) => ((acc[q.categoria] = (acc[q.categoria] || []).concat(q)), acc), {});
  categorias = Object.keys(grupos);
  current = 0;
  respostas = {};
  renderSteps();
  renderPerguntas();
});

function respostaCompleta(r) {
  return !!(r && r.valor !== undefined && r.dimensao && String(r.dimensao).trim());
}

function categoriaCompleta(cat) {
  return (grupos[cat] || []).every(q => respostaCompleta(respostas[q.id]));
}

function renderSteps() {
  wizardNav.innerHTML = categorias.map((c, i) =>
    `<button type="button" onclick="goCat(${i})" class="${i === current ? 'active' : ''} ${categoriaCompleta(c) ? '' : 'incomplete'}">${c}</button>`
  ).join('');
}

function goCat(i) {
  current = i;
  renderSteps();
  renderPerguntas();
}

function updateQuestionCardState(id) {
  const card = document.querySelector(`[data-question-id="${id}"]`);
  const r = respostas[id] || {};
  if (!card) return;

  card.querySelectorAll('[data-value]').forEach(btn => {
    const isActive = Number(btn.dataset.value) === Number(r.valor);
    btn.classList.toggle('active', isActive);
    btn.setAttribute('aria-pressed', isActive ? 'true' : 'false');
  });

  card.querySelectorAll('[data-dimensao]').forEach(btn => {
    const isActive = btn.dataset.dimensao === r.dimensao;
    btn.classList.toggle('active', isActive);
    btn.classList.toggle('active-category', isActive);
    btn.setAttribute('aria-pressed', isActive ? 'true' : 'false');
  });
}

function renderPerguntas() {
  const cat = categorias[current];
  perguntasArea.innerHTML = (grupos[cat] || []).map(q => `
    <div class="card question-card" data-question-id="${q.id}" style="margin-bottom:14px">
      <strong>${q.pergunta}</strong>
      <div class="muted">Peso ${q.peso}</div>

      <label>Nota de maturidade <span class="required-dot">*</span></label>
      <div class="scale">
        ${[0,1,2,3,4,5].map(v => `
          <button
            type="button"
            data-value="${v}"
            class="${respostas[q.id]?.valor === v ? 'active' : ''}"
            aria-pressed="${respostas[q.id]?.valor === v ? 'true' : 'false'}"
            onclick="setValor(${q.id}, ${v})">${v}</button>
        `).join('')}
      </div>

      <label>Categoria percebida da resposta <span class="required-dot">*</span></label>
      <div class="scale category-pills response-category-group">
        ${['Pessoas','Processos','Tecnologia'].map(v => `
          <button
            type="button"
            data-dimensao="${v}"
            class="${respostas[q.id]?.dimensao === v ? 'active active-category' : ''}"
            aria-pressed="${respostas[q.id]?.dimensao === v ? 'true' : 'false'}"
            onclick="setCampo(${q.id}, 'dimensao', '${v}')">${v}</button>
        `).join('')}
      </div>

      <textarea class="input" placeholder="Evidência" onchange="setCampo(${q.id}, 'evidencia', this.value)">${respostas[q.id]?.evidencia || ''}</textarea>
      <textarea class="input" placeholder="Plano de ação manual" onchange="setCampo(${q.id}, 'planoAcao', this.value)">${respostas[q.id]?.planoAcao || ''}</textarea>
    </div>
  `).join('');

  prevBtn.style.display = current === 0 ? 'none' : 'inline-block';
  nextBtn.style.display = current === categorias.length - 1 ? 'none' : 'inline-block';
  finishBtn.style.display = current === categorias.length - 1 ? 'inline-block' : 'none';
  renderSteps();
}

window.setValor = (id, valor) => {
  respostas[id] = { ...(respostas[id] || {}), valor };
  updateQuestionCardState(id);
  renderSteps();
};

window.setCampo = (id, campo, valor) => {
  respostas[id] = { ...(respostas[id] || {}), [campo]: valor };
  if (campo === 'dimensao') updateQuestionCardState(id);
  renderSteps();
};

prevBtn.onclick = () => {
  current = Math.max(0, current - 1);
  renderPerguntas();
};

nextBtn.onclick = () => {
  if (!categoriaCompleta(categorias[current])) {
    return alert('Existem perguntas sem nota ou sem categoria percebida nesta categoria.');
  }
  current = Math.min(categorias.length - 1, current + 1);
  renderPerguntas();
};

finishBtn.onclick = async () => {
  const faltantes = categorias.filter(c => !categoriaCompleta(c));
  if (faltantes.length) {
    renderSteps();
    return alert('Há categorias com perguntas sem nota ou sem categoria percebida: ' + faltantes.join(', '));
  }

  const empresaId = empresaSelect.value;
  const payload = Object.entries(respostas).map(([questaoId, v]) => ({
    empresaId: +empresaId,
    questaoId: +questaoId,
    valor: v.valor,
    evidencia: v.evidencia || '',
    planoAcao: v.planoAcao || '',
    dimensao: v.dimensao || ''
  }));

  await apiFetch('/respostas/lote', { method: 'POST', body: JSON.stringify(payload) });
  localStorage.setItem('stratec_last_empresa', empresaId);
  localStorage.setItem('stratec_avaliacoes_count', String((Number(localStorage.getItem('stratec_avaliacoes_count')) || 0) + 1));
  alert('Avaliação salva com sucesso.');
  location.href = 'dashboard.html';
};
