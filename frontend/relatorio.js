renderSidebar('relatorio');

let radarChart;
let gapChart;
let currentReport = null;

const fmt = n => Number(n || 0).toFixed(2);
const todayBr = () => new Date().toLocaleDateString('pt-BR');

(async () => {
  try {
    await carregarEmpresasSelect('empresaSelect');
    const last = localStorage.getItem('stratec_last_empresa');
    if (last && empresaSelect.querySelector(`option[value="${last}"]`)) empresaSelect.value = last;
  } catch (e) {
    reportContent.innerHTML = `<div class="card error-box">Não foi possível carregar as empresas: ${e.message}</div>`;
  }
})();

function destroyCharts() {
  [radarChart, gapChart].forEach(c => c && c.destroy());
}

function chartPalette() {
  return ['rgba(11,99,206,0.88)','rgba(34,211,238,0.88)','rgba(22,163,74,0.88)','rgba(245,158,11,0.88)','rgba(220,38,38,0.88)','rgba(99,102,241,0.88)','rgba(168,85,247,0.88)','rgba(20,184,166,0.88)'];
}

function commonChartOptions() {
  return {
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    plugins: {
      legend: { labels: { color: '#334155', font: { size: 12, weight: '600' } } },
      tooltip: { backgroundColor: '#081a36', titleColor: '#fff', bodyColor: '#fff' }
    }
  };
}

function getPriorityText(score) {
  const value = Number(score || 0);
  if (value <= 2) return 'Prioridade crítica';
  if (value <= 3.5) return 'Prioridade alta';
  if (value <= 4.5) return 'Prioridade moderada';
  return 'Acompanhamento contínuo';
}

function buildList(items, emptyText) {
  return items && items.length ? items.map(i => `<p>• ${i}</p>`).join('') : `<p class="muted">${emptyText}</p>`;
}

function buildReportHtml(r) {
  const scoreRows = (r.scorePorCategoria || []).map(c => `
    <div class="score-item"><strong>${c.categoria}</strong><span>${fmt(c.score)}</span></div>
  `).join('');

  const tipoItems = Object.entries(r.scorePorTipoAvaliacao || {}).map(([k, v]) => `
    <div class="score-item"><strong>${k}</strong><span>${fmt(v)}</span></div>
  `).join('') || '<p class="muted">Sem leitura por tipo de avaliação.</p>';

  const dimItems = Object.entries(r.distribuicaoDimensoes || {}).map(([k, v]) => `
    <div class="score-item"><strong>${k}</strong><span>${v}</span></div>
  `).join('') || '<p class="muted">Nenhuma categoria percebida foi registrada.</p>';

  const acoes = (r.planoAcaoAutomatico || []).filter((acao, index, arr) => {
    const key = `${acao.titulo || ''}::${acao.descricao || ''}`;
    return index === arr.findIndex(other => `${other.titulo || ''}::${other.descricao || ''}` === key);
  });

  const operacional = r.possuiDadosOperacionais ? `
    <section class="report-columns-2 pdf-block">
      <div class="card report-section">
        <h3>Evidências operacionais</h3>
        <div class="table-like">
          <div class="score-item"><strong>Serviços cadastrados</strong><span>${r.totalServicos ?? 0}</span></div>
          <div class="score-item"><strong>Incidentes registrados</strong><span>${r.totalIncidentes ?? 0}</span></div>
          <div class="score-item"><strong>Incidentes abertos</strong><span>${r.incidentesAbertos ?? 0}</span></div>
          <div class="score-item"><strong>Incidentes resolvidos</strong><span>${r.incidentesResolvidos ?? 0}</span></div>
          <div class="score-item"><strong>Dentro do SLA</strong><span>${r.incidentesDentroSla ?? 0}</span></div>
          <div class="score-item"><strong>Fora do SLA</strong><span>${r.incidentesForaSla ?? 0}</span></div>
          <div class="score-item"><strong>Cumprimento de SLA</strong><span>${r.percentualCumprimentoSla != null ? fmt(r.percentualCumprimentoSla) + '%' : '-'}</span></div>
        </div>
      </div>
      <div class="card report-section">
        <h3>Análise operacional</h3>
        <p>${r.analiseOperacional || 'Sem análise operacional disponível.'}</p>
      </div>
    </section>` : '';

  return `
    <div id="pdfArea" class="report-shell modern-report">
      <header class="report-header pdf-block">
        <div class="report-header-left"><img src="assets/logo.png" alt="Stratec TI" class="report-logo-small"></div>
        <div class="report-header-right"><p><strong>Empresa:</strong> ${r.empresa}</p><p><strong>Data:</strong> ${todayBr()}</p></div>
      </header>

      <section class="report-top-metrics pdf-block">
        <div class="card kpi-card kpi-primary"><h4>Score Geral</h4><div class="kpi-value">${fmt(r.scoreGeral)}</div><p class="kpi-helper">${r.nivel || '-'}</p></div>
        <div class="card kpi-card kpi-danger"><h4>Categoria crítica</h4><div class="kpi-value">${r.piorCategoria || '-'}</div><p class="kpi-helper">${getPriorityText(r.scoreGeral)}</p></div>
      </section>

      <section class="report-grid-main pdf-block">
        <div class="card report-list-card">
          <h3>Leitura por categoria</h3>
          <div class="report-score-panel">${scoreRows || '<div class="empty">Nenhum score por categoria disponível.</div>'}</div>
        </div>
        <div class="card report-chart-card">
          <h3>Radar de maturidade</h3>
          <p class="chart-caption">Visão do equilíbrio entre as frentes avaliadas.</p>
          <div class="chart-wrap"><canvas id="radarChart"></canvas></div>
        </div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section"><h3>Resumo executivo</h3><p>${r.justificativa || '-'}</p></div>
        <div class="card report-section"><h3>Direcionamento estratégico</h3><p>${r.recomendacao || '-'}</p></div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section"><h3>Governança vs. Gestão</h3><div class="table-like">${tipoItems}</div></div>
        <div class="card report-chart-card"><h3>Gap para excelência</h3><p class="chart-caption">Diferença entre o score atual e a maturidade máxima.</p><div class="chart-wrap"><canvas id="gapChart"></canvas></div></div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section"><h3>Pontos fortes</h3><div class="bullet-list">${buildList(r.pontosFortes || [], 'Nenhum ponto forte destacado.')}</div></div>
        <div class="card report-section"><h3>Pontos críticos</h3><div class="bullet-list">${buildList(r.pontosCriticos || [], 'Nenhum ponto crítico destacado.')}</div></div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section"><h3>Conclusão</h3><p>${r.conclusao || '-'}</p></div>
        <div class="card report-section"><h3>Distribuição das causas percebidas</h3><div class="table-like">${dimItems}</div></div>
      </section>

      ${operacional}

      <section class="report-columns-2 pdf-block page-break-before">
        <div class="card report-section action-block"><h3>Plano de ação automático</h3><div class="table-like">${acoes.length ? acoes.map(a => `<div class="score-item action-item"><strong>${a.titulo}</strong><span>${a.descricao}</span></div>`).join('') : '<p class="muted">Nenhuma ação automática foi gerada.</p>'}</div></div>
        <div class="card report-section"><h3>Roadmap de evolução</h3><ol class="roadmap">${(r.roadmap || []).map(x => `<li>${x}</li>`).join('')}</ol></div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section"><h3>Evidências registradas</h3><div class="bullet-list">${buildList(r.evidencias || [], 'Sem evidências registradas.')}</div></div>
        <div class="card report-section"><h3>Planos de ação manuais</h3><div class="bullet-list">${buildList(r.planosManuais || [], 'Nenhum plano manual registrado.')}</div></div>
      </section>
    </div>`;
}

function renderCharts(r) {
  destroyCharts();
  const categoriaScores = r.scorePorCategoria || [];
  const labels = categoriaScores.map(c => c.categoria);
  const values = categoriaScores.map(c => Number(fmt(c.score)));
  const colors = chartPalette();
  const options = commonChartOptions();

  radarChart = new Chart(document.getElementById('radarChart'), {
    type: 'radar',
    data: { labels, datasets: [{ label: 'Maturidade', data: values, backgroundColor: 'rgba(11,99,206,0.18)', borderColor: 'rgba(11,99,206,0.9)', pointBackgroundColor: 'rgba(34,211,238,1)', pointBorderColor: '#fff', pointRadius: 4 }] },
    options: { ...options, scales: { r: { min: 0, max: 5, ticks: { stepSize: 1, color: '#64748b', showLabelBackdrop: false }, angleLines: { color: 'rgba(100,116,139,.2)' }, grid: { color: 'rgba(100,116,139,.2)' }, pointLabels: { color: '#0f172a', font: { size: 12, weight: '700' } } } } }
  });


  gapChart = new Chart(document.getElementById('gapChart'), {
    type: 'bar',
    data: { labels: ['Score atual', 'Gap restante'], datasets: [{ data: [Number(fmt(r.scoreGeral)), Number(fmt(Math.max(0, 5 - Number(r.scoreGeral || 0))))], backgroundColor: ['rgba(22,163,74,0.85)', 'rgba(226,232,240,1)'], borderRadius: 10 }] },
    options: { ...options, plugins: { ...options.plugins, legend: { display: false } }, scales: { y: { min: 0, max: 5, ticks: { stepSize: 1, color: '#64748b' }, grid: { color: 'rgba(148,163,184,.15)' } }, x: { ticks: { color: '#0f172a', font: { weight: '700' } }, grid: { display: false } } } }
  });
}

carregarRelatorio.addEventListener('click', async () => {
  try {
    const empresaId = empresaSelect.value;
    if (!empresaId) {
      reportContent.innerHTML = `<div class="empty">Selecione uma empresa para gerar o relatório.</div>`;
      return;
    }
    localStorage.setItem('stratec_last_empresa', empresaId);
    const r = await apiFetch(`/relatorios/empresa/${empresaId}`);
    currentReport = r;
    localStorage.setItem('stratec_relatorios_count', String((Number(localStorage.getItem('stratec_relatorios_count')) || 0) + 1));
    reportContent.innerHTML = buildReportHtml(r);
    renderCharts(r);
  } catch (e) {
    reportContent.innerHTML = `<div class="card error-box">Erro ao gerar relatório: ${e.message}</div>`;
  }
});

exportarPdf.addEventListener('click', async () => {
  if (!currentReport) return alert('Gere um relatório antes de exportar.');
  const { jsPDF } = window.jspdf;
  const pdf = new jsPDF('p', 'mm', 'a4');
  const el = document.getElementById('pdfArea');
  const canvas = await html2canvas(el, { scale: 2, useCORS: true, backgroundColor: '#ffffff' });
  const imgData = canvas.toDataURL('image/png');
  const imgWidth = 190;
  const pageHeight = 297;
  const imgHeight = canvas.height * imgWidth / canvas.width;
  let heightLeft = imgHeight;
  let position = 10;
  pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
  heightLeft -= (pageHeight - 20);
  while (heightLeft > 0) {
    position = heightLeft - imgHeight + 10;
    pdf.addPage();
    pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
    heightLeft -= (pageHeight - 20);
  }
  pdf.save(`relatorio-stratec-${todayBr().replaceAll('/', '-')}.pdf`);
});
