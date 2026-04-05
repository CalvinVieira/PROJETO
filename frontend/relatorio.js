renderSidebar('relatorio');

let radarChart;
let barChart;
let dimensionChart;
let gapChart;
let currentReport = null;

const fmt = n => Number(n || 0).toFixed(2);
const todayBr = () => new Date().toLocaleDateString('pt-BR');

(async () => {
  try {
    await carregarEmpresasSelect('empresaSelect');
    const last = localStorage.getItem('stratec_last_empresa');
    if (last && empresaSelect.querySelector(`option[value="${last}"]`)) {
      empresaSelect.value = last;
    }
  } catch (e) {
    reportContent.innerHTML = `<div class="card" style="border-color:#fecaca;color:#991b1b">Não foi possível carregar as empresas: ${e.message}</div>`;
  }
})();

function destroyCharts() {
  [radarChart, barChart, dimensionChart, gapChart].forEach(c => c && c.destroy());
}

function chartPalette() {
  return [
    'rgba(11,99,206,0.88)',
    'rgba(34,211,238,0.88)',
    'rgba(22,163,74,0.88)',
    'rgba(245,158,11,0.88)',
    'rgba(220,38,38,0.88)',
    'rgba(99,102,241,0.88)',
    'rgba(168,85,247,0.88)',
    'rgba(20,184,166,0.88)'
  ];
}

function commonChartOptions() {
  return {
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    plugins: {
      legend: {
        labels: {
          color: '#334155',
          font: { size: 12, weight: '600' }
        }
      },
      tooltip: {
        backgroundColor: '#081a36',
        titleColor: '#fff',
        bodyColor: '#fff'
      }
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

function buildReportHtml(r) {
  const categoriaScores = r.scorePorCategoria || [];

  const scoreRows = categoriaScores.map(c => `
    <div class="score-item">
      <strong>${c.categoria}</strong>
      <span>${fmt(c.score)}</span>
    </div>
  `).join('');

  const acoes = (r.planoAcaoAutomatico || []).filter((acao, index, arr) => {
    const key = `${acao.titulo || ''}::${acao.descricao || ''}`;
    return index === arr.findIndex(other => `${other.titulo || ''}::${other.descricao || ''}` === key);
  });

  const evidencias = r.evidencias || [];
  const planosManuais = r.planosManuais || [];
  const dims = r.distribuicaoDimensoes || {};

  const dimItems = Object.keys(dims).length
    ? Object.entries(dims).map(([k, v]) => `
        <div class="score-item">
          <strong>${k}</strong>
          <span>${v}</span>
        </div>
      `).join('')
    : '<p class="muted">Nenhuma categoria percebida foi registrada.</p>';

  return `
    <div id="pdfArea" class="report-shell modern-report">
      <header class="report-header pdf-block">
        <div class="report-header-left">
          <img src="assets/logo.png" alt="Stratec TI" class="report-logo-small">
        </div>
        <div class="report-header-right">
          <p><strong>Empresa:</strong> ${r.empresa}</p>
          <p><strong>Data:</strong> ${todayBr()}</p>
        </div>
      </header>

      <section class="report-top-metrics pdf-block">
        <div class="card kpi-card kpi-primary">
          <h4>Score Geral</h4>
          <div class="kpi-value">${fmt(r.scoreGeral)}</div>
          <p class="kpi-helper">${r.nivel || '-'}</p>
        </div>

        <div class="card kpi-card kpi-danger">
          <h4>Categoria mais vulnerável</h4>
          <div class="kpi-value">${r.piorCategoria || '-'}</div>
          <p class="kpi-helper">${getPriorityText(r.scoreGeral)}</p>
        </div>
      </section>

      <section class="card report-radar-section pdf-block">
        <div class="report-radar-header">
          <div>
            <h3>Radar de Maturidade</h3>
            <p class="chart-caption">Visão consolidada do equilíbrio entre as categorias avaliadas.</p>
          </div>
          <div class="badge-row">
            <span class="badge badge-blue">Melhor categoria: ${r.melhorCategoria || '-'}</span>
          </div>
        </div>

        <div class="report-radar-wrap">
          <canvas id="radarChart"></canvas>
        </div>
      </section>

      <section class="report-cards pdf-block">
        <div class="report-card">
          <h3>Resumo Executivo</h3>
          <p>${r.justificativa || '-'}</p>
        </div>

        <div class="report-card">
          <h3>Conclusão</h3>
          <p>${r.conclusao || '-'}</p>
        </div>

        <div class="report-card">
          <h3>Recomendação</h3>
          <p>${r.recomendacao || '-'}</p>
        </div>
      </section>

      <section class="report-grid-main pdf-block">
        <div class="card report-list-card">
          <h3>Score por categoria</h3>
          <div class="report-score-panel">
            ${scoreRows || '<div class="empty">Nenhum score por categoria disponível.</div>'}
          </div>
        </div>

        <div class="card report-chart-card">
          <h3>Comparativo por categoria</h3>
          <p class="chart-caption">Quanto maior a barra, maior a maturidade observada naquela frente.</p>
          <div class="chart-wrap">
            <canvas id="barChart"></canvas>
          </div>
        </div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-chart-card">
          <h3>Causas percebidas das notas</h3>
          <p class="chart-caption">Pessoas, processos ou tecnologia: onde está o principal gargalo.</p>
          <div class="chart-wrap">
            <canvas id="dimensionChart"></canvas>
          </div>
        </div>

        <div class="card report-chart-card">
          <h3>Gap para excelência</h3>
          <p class="chart-caption">Diferença entre o score atual e o score máximo possível.</p>
          <div class="chart-wrap">
            <canvas id="gapChart"></canvas>
          </div>
        </div>
      </section>

      <section class="report-columns-2 pdf-block page-break-before">
        <div class="card report-section action-block">
          <h3>Plano de ação automático</h3>
          <div class="table-like">
            ${acoes.length
              ? acoes.map(a => `
                  <div class="score-item action-item">
                    <strong>${a.titulo}</strong>
                    <span>${a.descricao}</span>
                  </div>
                `).join('')
              : '<p class="muted">Nenhuma ação automática foi gerada.</p>'
            }
          </div>
        </div>

        <div class="card report-section action-block">
          <h3>Distribuição das categorias percebidas</h3>
          <div class="table-like">${dimItems}</div>
        </div>
      </section>

      <section class="report-columns-2 pdf-block">
        <div class="card report-section">
          <h3>Evidências registradas</h3>
          <div class="bullet-list">
            ${evidencias.length
              ? evidencias.map(e => `<p>• ${e}</p>`).join('')
              : '<p class="muted">Sem evidências registradas.</p>'
            }
          </div>
        </div>

        <div class="card report-section">
          <h3>Planos de ação manuais</h3>
          <div class="bullet-list">
            ${planosManuais.length
              ? planosManuais.map(p => `<p>• ${p}</p>`).join('')
              : '<p class="muted">Nenhum plano manual registrado.</p>'
            }
          </div>
        </div>
      </section>

      <section class="card report-section pdf-block">
        <h3>Roadmap de evolução</h3>
        <ol class="roadmap">
          ${(r.roadmap || []).map(x => `<li>${x}</li>`).join('')}
        </ol>
      </section>
    </div>
  `;
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
    data: {
      labels,
      datasets: [{
        label: 'Maturidade',
        data: values,
        backgroundColor: 'rgba(11,99,206,0.18)',
        borderColor: 'rgba(11,99,206,0.9)',
        pointBackgroundColor: 'rgba(34,211,238,1)',
        pointBorderColor: '#fff',
        pointRadius: 4
      }]
    },
    options: {
      ...options,
      scales: {
        r: {
          min: 0,
          max: 5,
          ticks: {
            stepSize: 1,
            color: '#64748b',
            showLabelBackdrop: false
          },
          angleLines: { color: 'rgba(100,116,139,.2)' },
          grid: { color: 'rgba(100,116,139,.2)' },
          pointLabels: {
            color: '#0f172a',
            font: { size: 12, weight: '700' }
          }
        }
      }
    }
  });

  barChart = new Chart(document.getElementById('barChart'), {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: 'Score',
        data: values,
        backgroundColor: colors,
        borderRadius: 10
      }]
    },
    options: {
      ...options,
      plugins: { ...options.plugins, legend: { display: false } },
      scales: {
        y: {
          min: 0,
          max: 5,
          ticks: { stepSize: 1, color: '#64748b' },
          grid: { color: 'rgba(148,163,184,.15)' }
        },
        x: {
          ticks: { color: '#0f172a', font: { weight: '700' } },
          grid: { display: false }
        }
      }
    }
  });

  const dims = r.distribuicaoDimensoes || {};
  const dimLabels = Object.keys(dims);
  const dimValues = Object.values(dims);

  dimensionChart = new Chart(document.getElementById('dimensionChart'), {
    type: 'doughnut',
    data: {
      labels: dimLabels.length ? dimLabels : ['Sem dados'],
      datasets: [{
        data: dimValues.length ? dimValues : [1],
        backgroundColor: dimValues.length
          ? ['rgba(11,99,206,0.85)', 'rgba(34,211,238,0.85)', 'rgba(22,163,74,0.85)']
          : ['rgba(203,213,225,1)'],
        borderWidth: 0
      }]
    },
    options: {
      ...options,
      cutout: '64%'
    }
  });

  gapChart = new Chart(document.getElementById('gapChart'), {
    type: 'bar',
    data: {
      labels: ['Score atual', 'Gap restante'],
      datasets: [{
        data: [
          Number(fmt(r.scoreGeral)),
          Number(fmt(Math.max(0, 5 - Number(r.scoreGeral || 0))))
        ],
        backgroundColor: ['rgba(22,163,74,0.85)', 'rgba(226,232,240,1)'],
        borderRadius: 10
      }]
    },
    options: {
      ...options,
      plugins: { ...options.plugins, legend: { display: false } },
      scales: {
        y: {
          min: 0,
          max: 5,
          ticks: { stepSize: 1, color: '#64748b' },
          grid: { color: 'rgba(148,163,184,.15)' }
        },
        x: {
          ticks: { color: '#0f172a', font: { weight: '700' } },
          grid: { display: false }
        }
      }
    }
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

    localStorage.setItem(
      'stratec_relatorios_count',
      String((Number(localStorage.getItem('stratec_relatorios_count')) || 0) + 1)
    );

    reportContent.innerHTML = buildReportHtml(r);
    renderCharts(r);
  } catch (e) {
    reportContent.innerHTML = `<div class="card" style="border-color:#fecaca;color:#991b1b">Erro ao gerar relatório: ${e.message}</div>`;
  }
});

async function exportBlockToPdf(pdf, selector, y, options = {}) {
  const el = document.querySelector(selector);
  if (!el) return y;

  const canvas = await html2canvas(el, {
    scale: 3,
    dpi: 300,
    useCORS: true,
    allowTaint: true,
    logging: false,
    backgroundColor: '#ffffff'
  });

  const imgData = canvas.toDataURL('image/jpeg', 0.95);

  const pageWidth = pdf.internal.pageSize.getWidth();
  const pageHeight = pdf.internal.pageSize.getHeight();
  const margin = 10;
  const usableWidth = pageWidth - (margin * 2);
  const imgHeight = (canvas.height * usableWidth) / canvas.width;

  let currentY = y;

  if (options.forceNewPage || currentY + imgHeight > pageHeight - margin) {
    pdf.addPage();
    currentY = margin;
  }

  pdf.addImage(imgData, 'JPEG', margin, currentY, usableWidth, imgHeight, undefined, 'FAST');
  return currentY + imgHeight + 6;
}

exportarPdf.addEventListener('click', async () => {
  try {
    if (!currentReport) {
      alert('Gere o relatório antes de exportar o PDF.');
      return;
    }

    if (typeof html2canvas === 'undefined' || !window.jspdf) {
      alert('Bibliotecas de exportação não carregadas corretamente.');
      return;
    }

    await waitForChartsToRender();

    if (!chartReady('radarChart')) {
      alert('O gráfico Radar ainda não terminou de renderizar. Tente novamente em alguns segundos.');
      return;
    }

    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF('p', 'mm', 'a4');
    let y = 10;
    let firstPageUsed = false;

    const blocks = [
      { selector: '.report-header', forceNewPage: false },
      { selector: '.report-top-metrics', forceNewPage: false },
      { selector: '.report-radar-section', forceNewPage: false },
      { selector: '.report-cards', forceNewPage: false },
      { selector: '.report-grid-main', forceNewPage: false },
      { selector: '.report-columns-2.page-break-before', forceNewPage: true },
      { selector: '.report-columns-2:not(.page-break-before)', forceNewPage: false },
      { selector: '.report-section.pdf-block:last-of-type', forceNewPage: false }
    ];

    for (const block of blocks) {
      const el = document.querySelector(block.selector);
      if (!el) continue;

      if (block.forceNewPage && firstPageUsed) {
        pdf.addPage();
        y = 10;
      }

      const canvas = await html2canvas(el, {
        scale: 3,
        dpi: 300,
        useCORS: true,
        allowTaint: true,
        logging: false,
        backgroundColor: '#ffffff'
      });

      const imgData = canvas.toDataURL('image/jpeg', 0.95);

      const pageWidth = pdf.internal.pageSize.getWidth();
      const pageHeight = pdf.internal.pageSize.getHeight();
      const margin = 10;
      const usableWidth = pageWidth - margin * 2;
      const imgHeight = (canvas.height * usableWidth) / canvas.width;

      if (y + imgHeight > pageHeight - margin) {
        pdf.addPage();
        y = 10;
      }

      pdf.addImage(imgData, 'JPEG', margin, y, usableWidth, imgHeight, undefined, 'FAST');
      y += imgHeight + 6;
      firstPageUsed = true;
    }

    pdf.save(`relatorio-stratec-ti-${(currentReport.empresa || 'empresa').toLowerCase().replace(/\s+/g, '-')}.pdf`);
  } catch (e) {
    alert(`Não foi possível exportar o PDF: ${e.message}`);
  }


    function waitForChartsToRender() {
  return new Promise(resolve => {
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        setTimeout(resolve, 250);
      });
    });
    });
  }

        function chartReady(canvasId) {
        const canvas = document.getElementById(canvasId);
        return canvas && canvas.width > 0 && canvas.height > 0;
      }

      function chartReady(canvasId) {
      const canvas = document.getElementById(canvasId);
      return canvas && canvas.width > 0 && canvas.height > 0;
    }

});