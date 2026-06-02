renderSidebar('governanca');

const fmt2 = n => Number(n || 0).toFixed(2);
const todayBr = () => new Date().toLocaleDateString('pt-BR');
let govData = null;

(async () => {
  try {
    await carregarEmpresasSelect('empresaSelect');
    const last = localStorage.getItem('stratec_last_empresa');
    if (last && document.getElementById('empresaSelect').querySelector(`option[value="${last}"]`)) {
      document.getElementById('empresaSelect').value = last;
    }
  } catch (e) {
    document.getElementById('govContent').innerHTML = `<div class="card error-box">Erro ao carregar empresas: ${e.message}</div>`;
  }
})();

document.getElementById('btnGerar').addEventListener('click', async () => {
  const empresaId = document.getElementById('empresaSelect').value;
  if (!empresaId) { alert('Selecione uma empresa.'); return; }
  localStorage.setItem('stratec_last_empresa', empresaId);
  const status = document.getElementById('genStatus');
  status.style.display = 'inline-block';
  status.textContent = '⏳ Gerando governança completa...';
  try {
    govData = await apiFetch(`/governanca/gerar/${empresaId}`, { method: 'POST' });
    renderGovContent(govData, empresaId);
    status.textContent = '✅ Gerado com sucesso!';
    setTimeout(() => status.style.display = 'none', 3000);
  } catch (e) {
    status.style.display = 'none';
    document.getElementById('govContent').innerHTML = `<div class="card error-box">Erro: ${e.message}</div>`;
  }
});

function renderGovContent(data, empresaId) {
  const cont = document.getElementById('govContent');
  cont.innerHTML = `
    <div class="kpi-row">
      <div class="kpi-mini"><h5>Score Geral</h5><div class="kval">${fmt2(data.scoreGeral)}</div><div class="klab">${data.nivel || ''}</div></div>
      <div class="kpi-mini"><h5>Ações 5W2H</h5><div class="kval">${(data.planoAcao || []).length}</div><div class="klab">ações geradas</div></div>
      <div class="kpi-mini"><h5>Riscos</h5><div class="kval">${(data.riscos || []).length}</div><div class="klab">riscos mapeados</div></div>
      <div class="kpi-mini"><h5>Críticos</h5><div class="kval text-danger">${(data.riscos || []).filter(r => r.nivelRisco === 'Crítico').length}</div><div class="klab">riscos críticos</div></div>
    </div>

    <div class="gov-tabs">
      <button class="gov-tab active" onclick="showTab('tab-plano', this)">📋 Plano 5W2H</button>
      <button class="gov-tab" onclick="showTab('tab-riscos', this)">⚠️ Matriz de Riscos</button>
      <button class="gov-tab" onclick="showTab('tab-pdti', this)">📄 PDTI</button>
      <button class="gov-tab" onclick="showTab('tab-export', this)">📤 Exportar</button>
    </div>

    <div id="tab-plano" class="gov-panel active">
      ${renderPlano5w2h(data.planoAcao || [])}
    </div>
    <div id="tab-riscos" class="gov-panel">
      ${renderMatrizRiscos(data.riscos || [])}
    </div>
    <div id="tab-pdti" class="gov-panel">
      ${renderPdtiForm(data.pdtiConfig || {}, empresaId)}
    </div>
    <div id="tab-export" class="gov-panel">
      ${renderExportPanel(data, empresaId)}
    </div>
  `;
}

function showTab(id, btn) {
  document.querySelectorAll('.gov-panel').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.gov-tab').forEach(b => b.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  btn.classList.add('active');
}

// ============================================================
// PLANO 5W2H
// ============================================================
function renderPlano5w2h(plano) {
  if (!plano.length) return '<div class="gov-empty">Nenhum plano gerado.</div>';
  const rows = plano.map(p => `
    <tr>
      <td><span class="badge-prioridade badge-${p.prioridade}">${p.prioridade}</span></td>
      <td><strong>${p.oQue || '-'}</strong><br><small class="muted">${p.categoriaOrigem || ''} ${p.scoreOrigem != null ? '| Score: ' + fmt2(p.scoreOrigem) : ''}</small></td>
      <td>${p.porQue || '-'}</td>
      <td>${p.onde || '-'}</td>
      <td><strong>${p.quando || '-'}</strong></td>
      <td>${p.quem || '-'}</td>
      <td>${p.como || '-'}</td>
      <td>${p.quanto || '-'}</td>
    </tr>
  `).join('');
  return `
    <div class="card">
      <h3>Plano de Ação 5W2H</h3>
      <p class="muted">Gerado automaticamente com base no diagnóstico de maturidade.</p>
      <div class="table-scroll">
        <table class="plano-table">
          <thead><tr><th>Prioridade</th><th>O Quê</th><th>Por Quê</th><th>Onde</th><th>Quando</th><th>Quem</th><th>Como</th><th>Quanto</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
    </div>`;
}

// ============================================================
// MATRIZ DE RISCOS
// ============================================================
function renderMatrizRiscos(riscos) {
  if (!riscos.length) return '<div class="gov-empty">Nenhum risco mapeado.</div>';
  const rows = riscos.map(r => `
    <tr>
      <td>${r.descricao || '-'}</td>
      <td>${r.tipo || '-'}</td>
      <td>${r.ativo || '-'}</td>
      <td>${r.causa || '-'}</td>
      <td>${r.consequencia || '-'}</td>
      <td class="text-center"><strong>${r.impacto || '-'}</strong></td>
      <td>${r.probabilidade || '-'}</td>
      <td><span class="badge-prioridade badge-risco-${r.nivelRisco}">${r.nivelRisco || '-'}</span></td>
      <td>${r.tratamento || '-'}</td>
      <td>${r.responsavel || '-'}</td>
    </tr>
  `).join('');
  return `
    <div class="card">
      <h3>Matriz de Gestão de Riscos</h3>
      <p class="muted">Riscos identificados e classificados pelo motor automático de governança.</p>
      <div class="table-scroll">
        <table class="risco-table">
          <thead><tr><th>Descrição</th><th>Tipo</th><th>Ativo</th><th>Causa</th><th>Consequência</th><th>Impacto</th><th>Probabilidade</th><th>Nível</th><th>Tratamento</th><th>Responsável</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
    </div>`;
}

// ============================================================
// PDTI FORM
// ============================================================
function renderPdtiForm(cfg, empresaId) {
  return `
    <div class="card">
      <h3>PDTI – Configuração</h3>
      <p class="muted">Revise e complemente as informações geradas automaticamente.</p>
      <div class="pdti-form" id="pdtiForm">
        <div class="form-row">
          <div>
            <label class="label">Responsável</label>
            <input class="input" id="p_responsavel" value="${esc(cfg.responsavel)}">
          </div>
          <div>
            <label class="label">Patrocinador Executivo</label>
            <input class="input" id="p_patrocinador" value="${esc(cfg.patrocinadorExecutivo)}">
          </div>
        </div>
        <div class="form-row">
          <div>
            <label class="label">Vigência – Início</label>
            <input class="input" id="p_inicio" type="month" value="${esc(cfg.periodoVigenciaInicio)}">
          </div>
          <div>
            <label class="label">Vigência – Fim</label>
            <input class="input" id="p_fim" type="month" value="${esc(cfg.periodoVigenciaFim)}">
          </div>
        </div>
        <div>
          <label class="label">Missão de TI</label>
          <textarea class="input" id="p_missao">${esc(cfg.missao)}</textarea>
        </div>
        <div>
          <label class="label">Visão de TI</label>
          <textarea class="input" id="p_visao">${esc(cfg.visao)}</textarea>
        </div>
        <div>
          <label class="label">Objetivos Estratégicos</label>
          <textarea class="input" id="p_objetivos" style="min-height:110px">${esc(cfg.objetivosEstrategicos)}</textarea>
        </div>
        <div>
          <label class="label">Metas Estratégicas</label>
          <textarea class="input" id="p_metas" style="min-height:110px">${esc(cfg.metasEstrategicas)}</textarea>
        </div>
        <div>
          <label class="label">Contexto Organizacional</label>
          <textarea class="input" id="p_org" style="min-height:90px">${esc(cfg.contextoOrganizacional)}</textarea>
        </div>
        <div>
          <label class="label">Contexto Tecnológico</label>
          <textarea class="input" id="p_tec" style="min-height:90px">${esc(cfg.contextoTecnologico)}</textarea>
        </div>
        <div>
          <label class="label">Análise Situacional</label>
          <textarea class="input" id="p_situac" style="min-height:90px">${esc(cfg.analiseSituacional)}</textarea>
        </div>
        <div style="display:flex;gap:10px">
          <button class="btn btn-primary" onclick="salvarPdti(${empresaId})">💾 Salvar PDTI</button>
          <button class="btn btn-secondary" onclick="exportarPdtiPdf(${empresaId})">📄 Exportar PDTI PDF</button>
        </div>
      </div>
    </div>`;
}

// ============================================================
// PAINEL DE EXPORTAÇÃO
// ============================================================
function renderExportPanel(data, empresaId) {
  return `
    <div class="card">
      <h3>📤 Exportar Documentos</h3>
      <p class="muted">Gere todos os documentos corporativos em PDF e Excel.</p>
      <div class="export-row">
        <button class="btn btn-primary" onclick="exportarDiagnosticoPdf(${empresaId})">📊 PDF – Diagnóstico Completo</button>
        <button class="btn btn-primary" onclick="exportarPdtiPdf(${empresaId})">📄 PDF – PDTI</button>
        <button class="btn btn-secondary" onclick="exportarPlanoExcel()">📗 Excel – Plano 5W2H</button>
        <button class="btn btn-secondary" onclick="exportarRiscosExcel()">📗 Excel – Matriz de Riscos</button>
      </div>
      <div style="margin-top:16px;padding:16px;background:var(--soft-blue);border-radius:12px;">
        <strong>Documentos disponíveis:</strong>
        <ul style="margin:8px 0 0;padding-left:20px;color:var(--muted);">
          <li>Diagnóstico Completo com gráficos, scores, evidências, plano e roadmap</li>
          <li>PDTI formal com estrutura ABNT: Introdução, Análise Situacional, Diagnóstico, Riscos, Plano e Conclusão</li>
          <li>Plano 5W2H completo em planilha Excel</li>
          <li>Matriz de Riscos completa em planilha Excel</li>
        </ul>
      </div>
    </div>`;
}

// ============================================================
// SALVAR PDTI
// ============================================================
async function salvarPdti(empresaId) {
  const dto = {
    missao: val('p_missao'), visao: val('p_visao'),
    objetivosEstrategicos: val('p_objetivos'), metasEstrategicas: val('p_metas'),
    periodoVigenciaInicio: val('p_inicio'), periodoVigenciaFim: val('p_fim'),
    responsavel: val('p_responsavel'), patrocinadorExecutivo: val('p_patrocinador'),
    contextoOrganizacional: val('p_org'), contextoTecnologico: val('p_tec'),
    analiseSituacional: val('p_situac')
  };
  try {
    await apiFetch(`/governanca/pdti/${empresaId}`, { method: 'POST', body: JSON.stringify(dto) });
    if (govData) govData.pdtiConfig = dto;
    alert('✅ PDTI salvo com sucesso!');
  } catch (e) {
    alert('Erro ao salvar: ' + e.message);
  }
}

// ============================================================
// EXPORTAR EXCEL – PLANO 5W2H
// ============================================================
function exportarPlanoExcel() {
  if (!govData || !govData.planoAcao) { alert('Gere o plano antes de exportar.'); return; }
  const wb = XLSX.utils.book_new();
  const dados = [
    ['PLANO DE AÇÃO 5W2H', '', '', '', '', '', '', ''],
    ['Empresa:', govData.empresa || '', 'Data:', todayBr(), '', '', '', ''],
    ['', '', '', '', '', '', '', ''],
    ['PRIORIDADE', 'O QUÊ', 'POR QUÊ', 'ONDE', 'QUANDO', 'QUEM', 'COMO', 'QUANTO'],
    ...govData.planoAcao.map(p => [
      p.prioridade, p.oQue, p.porQue, p.onde, p.quando, p.quem, p.como, p.quanto
    ])
  ];
  const ws = XLSX.utils.aoa_to_sheet(dados);
  ws['!cols'] = [12,30,25,15,12,20,30,15].map(w => ({ wch: w }));
  ws['!merges'] = [{ s:{r:0,c:0}, e:{r:0,c:7} }];
  XLSX.utils.book_append_sheet(wb, ws, '5W2H');
  XLSX.writeFile(wb, `plano-5w2h-${todayBr().replaceAll('/','-')}.xlsx`);
}

// ============================================================
// EXPORTAR EXCEL – MATRIZ DE RISCOS
// ============================================================
function exportarRiscosExcel() {
  if (!govData || !govData.riscos) { alert('Gere os riscos antes de exportar.'); return; }
  const wb = XLSX.utils.book_new();
  const dados = [
    ['MATRIZ DE GESTÃO DE RISCOS', '', '', '', '', '', '', '', '', ''],
    ['Empresa:', govData.empresa || '', 'Data:', todayBr(), '', '', '', '', '', ''],
    ['', '', '', '', '', '', '', '', '', ''],
    ['DESCRIÇÃO', 'TIPO', 'ATIVO', 'CAUSA', 'CONSEQUÊNCIA', 'IMPACTO', 'PROBABILIDADE', 'NÍVEL', 'TRATAMENTO', 'RESPONSÁVEL'],
    ...govData.riscos.map(r => [
      r.descricao, r.tipo, r.ativo, r.causa, r.consequencia,
      r.impacto, r.probabilidade, r.nivelRisco, r.tratamento, r.responsavel
    ])
  ];
  const ws = XLSX.utils.aoa_to_sheet(dados);
  ws['!cols'] = [30,10,20,25,25,8,12,12,12,20].map(w => ({ wch: w }));
  ws['!merges'] = [{ s:{r:0,c:0}, e:{r:0,c:9} }];
  XLSX.utils.book_append_sheet(wb, ws, 'Matriz de Riscos');
  XLSX.writeFile(wb, `matriz-riscos-${todayBr().replaceAll('/','-')}.xlsx`);
}

// ============================================================
// EXPORTAR PDF – DIAGNÓSTICO COMPLETO
// ============================================================
async function exportarDiagnosticoPdf(empresaId) {
  if (!govData) { alert('Gere o diagnóstico antes de exportar.'); return; }
  let relatorio = null;
  try { relatorio = await apiFetch(`/relatorios/empresa/${empresaId}`); } catch {}

  const { jsPDF } = window.jspdf;
  const pdf = new jsPDF('p', 'mm', 'a4');
  const W = 210, M = 20, CW = W - 2*M;

  let y = 0;
  const addPage = () => { pdf.addPage(); y = 20; };
  const checkY = (h = 20) => { if (y + h > 270) addPage(); };

  // CAPA
  pdf.setFillColor(8, 26, 54);
  pdf.rect(0, 0, W, 297, 'F');
  pdf.setTextColor(255, 255, 255);
  pdf.setFontSize(28); pdf.setFont('helvetica', 'bold');
  pdf.text('DIAGNÓSTICO COMPLETO', W/2, 80, { align: 'center' });
  pdf.setFontSize(18); pdf.setFont('helvetica', 'normal');
  pdf.text('Maturidade e Governança de TI', W/2, 95, { align: 'center' });
  pdf.setFontSize(16); pdf.setFont('helvetica', 'bold');
  pdf.text(govData.empresa || '', W/2, 130, { align: 'center' });
  pdf.setFontSize(12); pdf.setFont('helvetica', 'normal');
  pdf.text('Gerado por Stratec TI', W/2, 150, { align: 'center' });
  pdf.text(todayBr(), W/2, 162, { align: 'center' });

  // SEPARADOR
  pdf.setDrawColor(34, 211, 238);
  pdf.setLineWidth(1);
  pdf.line(M, 170, W-M, 170);

  if (relatorio) {
    pdf.setFontSize(14); pdf.setFont('helvetica', 'bold');
    pdf.text(`Score Geral: ${fmt2(relatorio.scoreGeral)} / 5,00`, W/2, 185, { align: 'center' });
    pdf.setFontSize(12); pdf.setFont('helvetica', 'normal');
    pdf.text(relatorio.nivel || '', W/2, 195, { align: 'center' });
  }

  pdf.addPage();
  pdf.setTextColor(8, 26, 54);
  y = 20;

  // RESUMO EXECUTIVO
  sec(pdf, '1. RESUMO EXECUTIVO', M, y); y += 8;
  if (relatorio) {
    addKvLine(pdf, 'Score Geral:', fmt2(relatorio.scoreGeral) + ' / 5,00', M, y); y += 7;
    addKvLine(pdf, 'Nível de Maturidade:', relatorio.nivel || '-', M, y); y += 7;
    addKvLine(pdf, 'Categoria Crítica:', relatorio.piorCategoria || '-', M, y); y += 7;
    addKvLine(pdf, 'Melhor Categoria:', relatorio.melhorCategoria || '-', M, y); y += 7;
    y += 4;
    if (relatorio.justificativa) {
      pdf.setFontSize(10); pdf.setFont('helvetica', 'normal');
      const lines = pdf.splitTextToSize(relatorio.justificativa, CW);
      checkY(lines.length * 5 + 5);
      pdf.text(lines, M, y); y += lines.length * 5 + 6;
    }
  }

  // SCORES POR CATEGORIA
  checkY(30);
  sec(pdf, '2. SCORES POR CATEGORIA', M, y); y += 8;
  if (relatorio && relatorio.scorePorCategoria) {
    relatorio.scorePorCategoria.forEach(c => {
      checkY(8);
      barScore(pdf, c.categoria, c.score, M, y, CW); y += 9;
    });
  }
  y += 4;

  // PONTOS FORTES E CRÍTICOS
  checkY(30);
  sec(pdf, '3. PONTOS FORTES & CRÍTICOS', M, y); y += 8;
  if (relatorio) {
    subSec(pdf, 'Pontos Fortes:', M, y); y += 6;
    (relatorio.pontosFortes || []).forEach(p => { checkY(7); bullet(pdf, p, M, y); y += 6; });
    y += 3;
    subSec(pdf, 'Pontos Críticos:', M, y); y += 6;
    (relatorio.pontosCriticos || []).forEach(p => { checkY(7); bullet(pdf, p, M, y); y += 6; });
    y += 4;
  }

  // PLANO 5W2H
  checkY(20);
  sec(pdf, '4. PLANO DE AÇÃO 5W2H', M, y); y += 8;
  if (govData.planoAcao && govData.planoAcao.length) {
    govData.planoAcao.forEach((p, i) => {
      checkY(40);
      pdf.setFillColor(241, 245, 249);
      pdf.roundedRect(M, y, CW, 34, 3, 3, 'F');
      pdf.setFontSize(9); pdf.setFont('helvetica', 'bold');
      pdf.setTextColor(8, 26, 54);
      pdf.text(`[${p.prioridade}] ${p.oQue || ''}`.substring(0, 80), M+4, y+6);
      pdf.setFont('helvetica', 'normal'); pdf.setTextColor(100, 116, 139);
      pdf.text(`Por quê: ${(p.porQue || '').substring(0,60)}`, M+4, y+12);
      pdf.text(`Quando: ${p.quando || '-'} | Quem: ${(p.quem || '-').substring(0,30)} | Quanto: ${p.quanto || '-'}`, M+4, y+18);
      pdf.setFontSize(8);
      pdf.text(`Como: ${(p.como || '').substring(0,80)}`, M+4, y+24);
      y += 38;
    });
  }
  y += 4;

  // MATRIZ DE RISCOS
  checkY(20);
  sec(pdf, '5. MATRIZ DE RISCOS', M, y); y += 8;
  if (govData.riscos && govData.riscos.length) {
    govData.riscos.forEach(r => {
      checkY(28);
      const cor = r.nivelRisco === 'Crítico' ? [220,38,38] : r.nivelRisco === 'Alto' ? [234,88,12] : r.nivelRisco === 'Moderado' ? [217,119,6] : [22,163,74];
      pdf.setFillColor(...cor);
      pdf.roundedRect(M, y, 18, 8, 2, 2, 'F');
      pdf.setFontSize(7); pdf.setFont('helvetica', 'bold'); pdf.setTextColor(255,255,255);
      pdf.text(r.nivelRisco || '', M+2, y+5.5);
      pdf.setFontSize(9); pdf.setFont('helvetica', 'bold'); pdf.setTextColor(8,26,54);
      pdf.text((r.descricao || '').substring(0, 80), M+22, y+5.5);
      pdf.setFontSize(8); pdf.setFont('helvetica', 'normal'); pdf.setTextColor(100,116,139);
      pdf.text(`Tratamento: ${r.tratamento || '-'} | Responsável: ${r.responsavel || '-'} | Impacto: ${r.impacto || '-'} | Prob.: ${r.probabilidade || '-'}`, M+22, y+13);
      pdf.text(`Causa: ${(r.causa || '').substring(0,80)}`, M+4, y+20);
      y += 26;
    });
  }
  y += 4;

  // ROADMAP
  checkY(20);
  sec(pdf, '6. ROADMAP TECNOLÓGICO', M, y); y += 8;
  if (relatorio && relatorio.roadmap) {
    relatorio.roadmap.forEach((item, i) => {
      checkY(8);
      bullet(pdf, `${i+1}. ${item}`, M, y); y += 6;
    });
  }

  // CONCLUSÃO
  checkY(20);
  y += 4;
  sec(pdf, '7. CONCLUSÃO', M, y); y += 8;
  if (relatorio && relatorio.conclusao) {
    pdf.setFontSize(10); pdf.setFont('helvetica', 'normal'); pdf.setTextColor(15,23,42);
    const lines = pdf.splitTextToSize(relatorio.conclusao, CW);
    checkY(lines.length * 5);
    pdf.text(lines, M, y); y += lines.length * 5 + 4;
  }

  // Rodapé em cada página
  const pages = pdf.internal.getNumberOfPages();
  for (let i = 1; i <= pages; i++) {
    pdf.setPage(i);
    pdf.setFillColor(8,26,54);
    pdf.rect(0, 285, W, 12, 'F');
    pdf.setFontSize(8); pdf.setFont('helvetica','normal'); pdf.setTextColor(255,255,255);
    pdf.text('Stratec TI – Plataforma de Maturidade e Governança de TI', M, 292);
    pdf.text(`Página ${i} de ${pages}`, W-M, 292, { align: 'right' });
  }

  pdf.save(`diagnostico-completo-${todayBr().replaceAll('/','-')}.pdf`);
}

// ============================================================
// EXPORTAR PDF – PDTI FORMAL
// ============================================================
async function exportarPdtiPdf(empresaId) {
  if (!govData) { alert('Gere o PDTI antes de exportar.'); return; }
  const cfg = govData.pdtiConfig || {};
  let relatorio = null;
  try { relatorio = await apiFetch(`/relatorios/empresa/${empresaId}`); } catch {}

  const { jsPDF } = window.jspdf;
  const pdf = new jsPDF('p', 'mm', 'a4');
  const W = 210, M = 30, CW = W - 2*M; // Margens ABNT
  let y = 0;
  let pgNum = 0;

  const addPage = () => {
    pdf.addPage(); pgNum++;
    y = 25;
    // cabeçalho
    pdf.setFontSize(8); pdf.setFont('times','normal'); pdf.setTextColor(100,116,139);
    pdf.text(`${govData.empresa || 'Empresa'} – PDTI ${cfg.periodoVigenciaInicio || ''} / ${cfg.periodoVigenciaFim || ''}`, M, 15);
    pdf.setDrawColor(200,200,200); pdf.setLineWidth(0.3);
    pdf.line(M, 17, W-M, 17);
  };
  const checkY = (h=20) => { if (y + h > 275) addPage(); };

  const pdfSec = (titulo, nivel=1) => {
    checkY(16);
    if (nivel===1) {
      pdf.setFontSize(13); pdf.setFont('times','bold'); pdf.setTextColor(8,26,54);
    } else {
      pdf.setFontSize(11); pdf.setFont('times','bold'); pdf.setTextColor(15,23,42);
    }
    pdf.text(titulo, M, y); y += nivel===1 ? 8 : 7;
    if (nivel===1) { pdf.setDrawColor(11,99,206); pdf.setLineWidth(0.5); pdf.line(M, y-3, W-M, y-3); y += 3; }
  };

  const pdfPara = (texto, recuo=0) => {
    if (!texto) return;
    pdf.setFontSize(11); pdf.setFont('times','normal'); pdf.setTextColor(15,23,42);
    const lines = pdf.splitTextToSize(texto, CW - recuo);
    checkY(lines.length * 6.5);
    pdf.text(lines, M + recuo, y);
    y += lines.length * 6.5 + 3;
  };

  // ===== CAPA =====
  pdf.setFillColor(8,26,54);
  pdf.rect(0, 0, W, 80, 'F');
  pdf.setTextColor(255,255,255);
  pdf.setFontSize(11); pdf.setFont('times','normal');
  pdf.text('STRATEC TI', W/2, 25, { align:'center' });
  pdf.text('Plataforma de Maturidade e Governança de TI', W/2, 35, { align:'center' });
  pdf.setDrawColor(34,211,238); pdf.setLineWidth(0.8);
  pdf.line(M, 42, W-M, 42);

  pdf.setTextColor(15,23,42);
  pdf.setFontSize(11); pdf.setFont('times','normal');
  const pes = val('p_responsavel') || cfg.responsavel || '';
  if (pes) { pdf.text(pes.toUpperCase(), W/2, 105, { align:'center' }); }

  pdf.setFontSize(20); pdf.setFont('times','bold'); pdf.setTextColor(8,26,54);
  pdf.text('PLANO DIRETOR DE TECNOLOGIA', W/2, 130, { align:'center' });
  pdf.text('DA INFORMAÇÃO (PDTI)', W/2, 143, { align:'center' });

  pdf.setFontSize(14); pdf.setFont('times','normal');
  pdf.text(govData.empresa || '', W/2, 165, { align:'center' });

  pdf.setFontSize(12);
  const per = `${cfg.periodoVigenciaInicio || '2025'} – ${cfg.periodoVigenciaFim || '2026'}`;
  pdf.text(`Período de Vigência: ${per}`, W/2, 180, { align:'center' });

  pdf.setFontSize(11);
  pdf.text(`Salvador, ${new Date().getFullYear()}`, W/2, 250, { align:'center' });

  // ===== FOLHA DE ROSTO =====
  pdf.addPage(); pgNum = 1; y = 40;
  pdf.setTextColor(15,23,42);
  pdf.setFontSize(12); pdf.setFont('times','normal');
  if (pes) { pdf.text(pes, W/2, y, { align:'center' }); y+=30; }

  pdf.setFontSize(16); pdf.setFont('times','bold');
  pdf.text('PLANO DIRETOR DE TECNOLOGIA DA INFORMAÇÃO (PDTI):', W/2, y, { align:'center' }); y+=8;
  pdf.setFontSize(13);
  pdf.text(((govData.empresa || '') + ' – REESTRUTURAÇÃO E GOVERNANÇA'), W/2, y, { align:'center' }); y+=40;

  pdf.setFontSize(11); pdf.setFont('times','normal');
  const descTrab = 'Documento elaborado com base no diagnóstico de maturidade de TI. Aborda análise situacional, diagnóstico de riscos e plano de ação estratégica.';
  const linDesc = pdf.splitTextToSize(descTrab, 100);
  pdf.text(linDesc, W/2, y, { align:'center' }); y += linDesc.length * 6 + 10;

  if (val('p_patrocinador') || cfg.patrocinadorExecutivo) {
    pdf.text(`Patrocinador Executivo: ${val('p_patrocinador') || cfg.patrocinadorExecutivo}`, W/2, y, { align:'center' });
    y += 8;
  }
  pdf.text(`Salvador, ${new Date().getFullYear()}`, W/2, 265, { align:'center' });

  // ===== SUMÁRIO =====
  addPage();
  pdf.setFontSize(14); pdf.setFont('times','bold'); pdf.setTextColor(8,26,54);
  pdf.text('SUMÁRIO', W/2, y, { align:'center' }); y += 12;
  pdf.setDrawColor(8,26,54); pdf.setLineWidth(0.5);
  pdf.line(M, y-4, W-M, y-4); y += 4;

  const sumItems = [
    ['1', 'INTRODUÇÃO', '4'],
    ['2', 'ANÁLISE SITUACIONAL', '5'],
    ['3', 'DIAGNÓSTICO SITUACIONAL', '6'],
    ['4', 'ANÁLISE DE RISCOS', '7'],
    ['5', 'PLANO DE AÇÃO (5W2H)', '8'],
    ['6', 'CONCLUSÃO', '9'],
    ['', 'REFERÊNCIAS BIBLIOGRÁFICAS', '10'],
  ];
  pdf.setFont('times','normal'); pdf.setFontSize(12); pdf.setTextColor(15,23,42);
  sumItems.forEach(([num, titulo, pg]) => {
    const label = num ? `${num} ${titulo}` : titulo;
    pdf.text(label, M, y);
    pdf.text(pg, W-M, y, { align:'right' });
    y += 8;
  });

  // ===== SEÇÃO 1 – INTRODUÇÃO =====
  addPage();
  pdfSec('1 INTRODUÇÃO');
  pdfPara(`O presente Plano Diretor de Tecnologia da Informação (PDTI) é direcionado à organização ${govData.empresa || ''}, com base no diagnóstico de maturidade de TI realizado pela plataforma Stratec TI.`);
  pdfSec('1.1 Objetivos', 2);
  pdfPara('Este PDTI visa alinhar os recursos de Tecnologia da Informação aos objetivos estratégicos do negócio, transformando a TI em um ativo estratégico capaz de sustentar crescimento, mitigar riscos e garantir conformidade regulatória.');
  pdfSec('1.2 Escopo', 2);
  pdfPara('O escopo deste documento abrange toda a infraestrutura, sistemas, processos, pessoas e governança da área de Tecnologia da Informação da organização, contemplando o período ' + per + '.');
  pdfSec('1.3 Metodologia', 2);
  pdfPara('O diagnóstico foi realizado por meio de questionário estruturado de maturidade, analisando as dimensões de Governança, Segurança, Infraestrutura, Serviços, Processos, Estratégia e Pessoas, com base nos frameworks COBIT e ITIL.');

  // ===== SEÇÃO 2 – ANÁLISE SITUACIONAL =====
  addPage();
  pdfSec('2 ANÁLISE SITUACIONAL');

  if (val('p_missao') || cfg.missao) {
    pdfSec('2.1 Missão de TI', 2);
    pdfPara(val('p_missao') || cfg.missao);
  }
  if (val('p_visao') || cfg.visao) {
    pdfSec('2.2 Visão de TI', 2);
    pdfPara(val('p_visao') || cfg.visao);
  }
  if (val('p_org') || cfg.contextoOrganizacional) {
    pdfSec('2.3 Contexto Organizacional', 2);
    pdfPara(val('p_org') || cfg.contextoOrganizacional);
  }
  if (val('p_tec') || cfg.contextoTecnologico) {
    pdfSec('2.4 Contexto Tecnológico', 2);
    pdfPara(val('p_tec') || cfg.contextoTecnologico);
  }
  if (val('p_situac') || cfg.analiseSituacional) {
    pdfSec('2.5 Análise Situacional', 2);
    pdfPara(val('p_situac') || cfg.analiseSituacional);
  }

  // ===== SEÇÃO 3 – DIAGNÓSTICO =====
  addPage();
  pdfSec('3 DIAGNÓSTICO SITUACIONAL');

  if (relatorio) {
    pdfSec('3.1 Resultado do Diagnóstico', 2);
    pdfPara(`Score Geral: ${fmt2(relatorio.scoreGeral)} / 5,00 – ${relatorio.nivel || ''}`);
    pdfPara(relatorio.justificativa || '');

    if (relatorio.scorePorCategoria && relatorio.scorePorCategoria.length) {
      pdfSec('3.2 Indicadores por Categoria', 2);
      relatorio.scorePorCategoria.forEach(c => {
        pdf.setFont('times','normal'); pdf.setFontSize(11);
        checkY(7);
        pdf.text(`• ${c.categoria}: ${fmt2(c.score)} / 5,00`, M+5, y); y += 6.5;
      });
      y += 3;
    }

    pdfSec('3.3 Pontos Fortes', 2);
    (relatorio.pontosFortes || ['Nenhum ponto forte identificado.']).forEach(p => {
      checkY(7); pdf.setFont('times','normal'); pdf.setFontSize(11);
      pdf.text(`• ${p}`, M+5, y); y += 6.5;
    });
    y += 3;

    pdfSec('3.4 Pontos Fracos', 2);
    (relatorio.pontosCriticos || ['Nenhum ponto fraco identificado.']).forEach(p => {
      checkY(7); pdf.setFont('times','normal'); pdf.setFontSize(11);
      pdf.text(`• ${p}`, M+5, y); y += 6.5;
    });
  }

  // ===== SEÇÃO 4 – RISCOS =====
  addPage();
  pdfSec('4 ANÁLISE DE RISCOS');
  pdfPara('Os riscos foram mapeados e classificados com base no produto Impacto × Probabilidade, resultando nos níveis: Crítico (≥20), Alto (12-19), Moderado (6-11), Baixo (3-5) e Desprezível (<3).');
  y += 4;

  if (govData.riscos && govData.riscos.length) {
    govData.riscos.forEach((r, i) => {
      checkY(35);
      pdf.setFillColor(248,250,252);
      pdf.roundedRect(M, y, CW, 30, 2, 2, 'F');
      pdf.setFontSize(10); pdf.setFont('times','bold'); pdf.setTextColor(8,26,54);
      pdf.text(`${i+1}. ${(r.descricao || '').substring(0,70)}`, M+3, y+6);
      pdf.setFont('times','normal'); pdf.setFontSize(9); pdf.setTextColor(100,116,139);
      pdf.text(`Nível: ${r.nivelRisco || '-'} | Impacto: ${r.impacto || '-'} | Probabilidade: ${r.probabilidade || '-'} | Tratamento: ${r.tratamento || '-'}`, M+3, y+13);
      pdf.text(`Causa: ${(r.causa || '').substring(0,80)}`, M+3, y+19);
      pdf.text(`Responsável: ${r.responsavel || '-'}`, M+3, y+25);
      y += 34;
    });
  }

  // ===== SEÇÃO 5 – PLANO 5W2H =====
  addPage();
  pdfSec('5 PLANO DE AÇÃO (5W2H)');
  pdfPara('O plano de ação foi gerado automaticamente com base nos scores de maturidade por categoria, aplicando o motor de recomendações da plataforma Stratec TI.');
  y += 4;

  if (govData.planoAcao && govData.planoAcao.length) {
    govData.planoAcao.forEach((p, i) => {
      checkY(40);
      pdf.setFillColor(248,250,252);
      pdf.roundedRect(M, y, CW, 36, 2, 2, 'F');
      pdf.setFontSize(10); pdf.setFont('times','bold'); pdf.setTextColor(8,26,54);
      pdf.text(`${i+1}. [${p.prioridade}] ${(p.oQue || '').substring(0,70)}`, M+3, y+6);
      pdf.setFont('times','normal'); pdf.setFontSize(9); pdf.setTextColor(100,116,139);
      pdf.text(`Por quê: ${(p.porQue || '').substring(0,80)}`, M+3, y+12);
      pdf.text(`Onde: ${p.onde || '-'} | Quando: ${p.quando || '-'}`, M+3, y+18);
      pdf.text(`Quem: ${(p.quem || '-').substring(0,40)} | Quanto: ${p.quanto || '-'}`, M+3, y+24);
      pdf.text(`Como: ${(p.como || '').substring(0,85)}`, M+3, y+30);
      y += 40;
    });
  }

  // ===== SEÇÃO 6 – CONCLUSÃO =====
  addPage();
  pdfSec('6 CONCLUSÃO');
  const conc = (relatorio && relatorio.conclusao) ? relatorio.conclusao :
    'A elaboração deste PDTI evidencia a necessidade de transformação da TI em um ativo estratégico. A implementação das ações propostas visa elevar a maturidade tecnológica, garantir a continuidade operacional e alinhar a TI aos objetivos do negócio.';
  pdfPara(conc);
  y += 6;
  pdfSec('6.1 Benefícios Esperados', 2);
  ['Elevação do nível de maturidade de TI para o patamar gerenciado',
   'Redução significativa dos incidentes críticos e indisponibilidades',
   'Conformidade com a LGPD e frameworks internacionais (COBIT/ITIL)',
   'Alinhamento dos investimentos de TI à estratégia corporativa',
   'Aumento da confiança dos stakeholders na gestão tecnológica'].forEach(b => {
    checkY(7); pdf.setFont('times','normal'); pdf.setFontSize(11);
    pdf.text(`• ${b}`, M+5, y); y += 6.5;
  });
  y += 6;
  pdfSec('6.2 Próximos Passos', 2);
  if (val('p_objetivos') || cfg.objetivosEstrategicos) {
    const obj = (val('p_objetivos') || cfg.objetivosEstrategicos || '').split('\n');
    obj.filter(o => o.trim()).forEach(o => {
      checkY(7); pdf.setFont('times','normal'); pdf.setFontSize(11);
      pdf.text(`• ${o.trim().replace(/^[\d\.\-\*]+\s*/,'')}`, M+5, y); y += 6.5;
    });
  }

  // ===== REFERÊNCIAS =====
  addPage();
  pdfSec('REFERÊNCIAS BIBLIOGRÁFICAS');
  const refs = [
    'ASSOCIAÇÃO BRASILEIRA DE NORMAS TÉCNICAS. NBR 14724: Informação e documentação — Trabalhos acadêmicos — Apresentação. Rio de Janeiro: ABNT, 2011.',
    'ISACA. COBIT 2019 Framework: Introduction and Methodology. Schaumburg: ISACA, 2018.',
    'AXELOS. ITIL Foundation: ITIL 4 Edition. London: TSO, 2019.',
    'BRASIL. Lei nº 13.709, de 14 de agosto de 2018. Lei Geral de Proteção de Dados Pessoais (LGPD). Brasília, DF, 2018.',
    'STRATEC TI. Plataforma de Maturidade e Governança de TI. Relatório Gerado Automaticamente, 2026.',
  ];
  refs.forEach(ref => {
    checkY(12);
    pdf.setFont('times','normal'); pdf.setFontSize(10); pdf.setTextColor(15,23,42);
    const lines = pdf.splitTextToSize(ref, CW);
    pdf.text(lines, M, y); y += lines.length * 5.5 + 5;
  });

  // RODAPÉ COM PAGINAÇÃO
  const total = pdf.internal.getNumberOfPages();
  for (let i = 2; i <= total; i++) {
    pdf.setPage(i);
    pdf.setFontSize(9); pdf.setFont('times','normal'); pdf.setTextColor(100,116,139);
    pdf.text(`${govData.empresa || ''} – PDTI`, M, 287);
    pdf.text(`${i - 1}`, W/2, 287, { align:'center' });
    pdf.setDrawColor(200,200,200); pdf.setLineWidth(0.2);
    pdf.line(M, 283, W-M, 283);
  }

  pdf.save(`PDTI-${(govData.empresa || 'empresa').replace(/\s+/g,'-')}-${new Date().getFullYear()}.pdf`);
}

// ============================================================
// HELPERS DE PDF
// ============================================================
function sec(pdf, txt, x, y) {
  pdf.setFontSize(12); pdf.setFont('helvetica','bold'); pdf.setTextColor(8,26,54);
  pdf.text(txt, x, y);
  pdf.setDrawColor(11,99,206); pdf.setLineWidth(0.4);
  pdf.line(x, y+2, x+170, y+2);
}
function subSec(pdf, txt, x, y) {
  pdf.setFontSize(10); pdf.setFont('helvetica','bold'); pdf.setTextColor(15,23,42);
  pdf.text(txt, x, y);
}
function bullet(pdf, txt, x, y) {
  pdf.setFontSize(9); pdf.setFont('helvetica','normal'); pdf.setTextColor(15,23,42);
  pdf.text('•', x, y);
  const lines = pdf.splitTextToSize(txt, 165);
  pdf.text(lines, x+5, y);
}
function addKvLine(pdf, key, val, x, y) {
  pdf.setFontSize(10); pdf.setFont('helvetica','bold'); pdf.setTextColor(8,26,54);
  pdf.text(key, x, y);
  pdf.setFont('helvetica','normal'); pdf.setTextColor(15,23,42);
  pdf.text(val, x+45, y);
}
function barScore(pdf, label, score, x, y, cw) {
  pdf.setFontSize(9); pdf.setFont('helvetica','normal'); pdf.setTextColor(15,23,42);
  pdf.text(label, x, y+4);
  const bw = cw - 60, bx = x+55, by = y;
  pdf.setFillColor(226,232,240); pdf.roundedRect(bx, by, bw, 6, 2, 2, 'F');
  const fill = Math.min(score/5, 1) * bw;
  const cor = score < 2 ? [220,38,38] : score < 3.5 ? [245,158,11] : [22,163,74];
  pdf.setFillColor(...cor); pdf.roundedRect(bx, by, fill, 6, 2, 2, 'F');
  pdf.setFontSize(8); pdf.setFont('helvetica','bold');
  pdf.text(fmt2(score), bx+bw+3, y+5);
}
function esc(s) { return (s || '').replace(/&/g,'&amp;').replace(/"/g,'&quot;'); }
function val(id) { const el = document.getElementById(id); return el ? el.value : ''; }
