const RESULT_CONFIG = window.RESULT_CONFIG || { active: 'relatorio', title: 'Resultados', subtitle: '', type: null, categories: [] };
renderSidebar(RESULT_CONFIG.active || 'relatorio');

const empresaSelect = document.getElementById('empresaSelect');
const sectionTitle = document.getElementById('sectionTitle');
const sectionSubtitle = document.getElementById('sectionSubtitle');
const carregarBtn = document.getElementById('carregarResultado');
const content = document.getElementById('resultContent');

sectionTitle.textContent = RESULT_CONFIG.title;
sectionSubtitle.textContent = RESULT_CONFIG.subtitle || 'Análise segmentada do relatório executivo.';

(async () => {
  try {
    await carregarEmpresasSelect('empresaSelect');
    const last = localStorage.getItem('stratec_last_empresa');
    if (last && empresaSelect.querySelector(`option[value="${last}"]`)) empresaSelect.value = last;
  } catch (e) {
    content.innerHTML = `<div class="card error-box">Não foi possível carregar as empresas: ${e.message}</div>`;
  }
})();

function fmt(n) { return Number(n || 0).toFixed(2); }

function matchByConfig(item) {
  const cat = item.categoria || '';
  if (RESULT_CONFIG.type && item.tipoAvaliacao && item.tipoAvaliacao !== RESULT_CONFIG.type) return false;
  if (RESULT_CONFIG.categories && RESULT_CONFIG.categories.length) return RESULT_CONFIG.categories.includes(cat);
  return true;
}

function buildInsightBox(title, items, emptyText) {
  return `
    <div class="card result-box">
      <h3>${title}</h3>
      <div class="bullet-list">
        ${items.length ? items.map(i => `<p>• ${i}</p>`).join('') : `<p class="muted">${emptyText}</p>`}
      </div>
    </div>`;
}

carregarBtn.addEventListener('click', async () => {
  try {
    const empresaId = empresaSelect.value;
    if (!empresaId) return;
    localStorage.setItem('stratec_last_empresa', empresaId);
    const r = await apiFetch(`/relatorios/empresa/${empresaId}`);
    const scorePorCategoria = (r.scorePorCategoria || []).filter(matchByConfig);
    const mediaSegmento = scorePorCategoria.length
      ? scorePorCategoria.reduce((acc, c) => acc + Number(c.score || 0), 0) / scorePorCategoria.length
      : null;

    const pontosFortes = (r.pontosFortes || []).filter(p => RESULT_CONFIG.categories.length === 0 || RESULT_CONFIG.categories.some(c => p.startsWith(c)));
    const pontosCriticos = (r.pontosCriticos || []).filter(p => RESULT_CONFIG.categories.length === 0 || RESULT_CONFIG.categories.some(c => p.startsWith(c)));
    const plano = (r.planoAcaoAutomatico || []).filter(a => {
      const d = `${a.titulo} ${a.descricao}`.toLowerCase();
      return RESULT_CONFIG.categories.length === 0 || RESULT_CONFIG.categories.some(c => d.includes(c.toLowerCase()));
    });

    content.innerHTML = `
      <div class="stats stats-3">
        <div class="card"><div class="muted">Empresa</div><div class="stat-number text-small">${r.empresa}</div></div>
        <div class="card"><div class="muted">Score do segmento</div><div class="stat-number">${mediaSegmento != null ? fmt(mediaSegmento) : '-'}</div></div>
        <div class="card"><div class="muted">Categorias analisadas</div><div class="stat-number">${scorePorCategoria.length}</div></div>
      </div>

      <div class="result-grid">
        <div class="card result-box">
          <h3>Categorias do segmento</h3>
          <div class="table-like">
            ${scorePorCategoria.length ? scorePorCategoria.map(c => `<div class="score-item"><strong>${c.categoria}</strong><span>${fmt(c.score)}</span></div>`).join('') : '<p class="muted">Nenhuma categoria correspondente neste relatório.</p>'}
          </div>
        </div>
        ${buildInsightBox('Pontos fortes', pontosFortes, 'Sem pontos fortes destacados para este segmento.')}
        ${buildInsightBox('Pontos críticos', pontosCriticos, 'Sem pontos críticos destacados para este segmento.')}
      </div>

      <div class="card result-box">
        <h3>Direcionamento estratégico</h3>
        <p>${r.recomendacao || '-'}</p>
      </div>

      <div class="card result-box">
        <h3>Ações prioritárias</h3>
        <div class="table-like">
          ${plano.length ? plano.map(a => `<div class="score-item action-item"><strong>${a.titulo}</strong><span>${a.descricao}</span></div>`).join('') : '<p class="muted">Nenhuma ação específica foi identificada para este segmento.</p>'}
        </div>
      </div>
    `;
  } catch (e) {
    content.innerHTML = `<div class="card error-box">Erro ao gerar análise segmentada: ${e.message}</div>`;
  }
});
