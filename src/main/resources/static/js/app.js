// 代码质量检测系统 - 前端交互

const BASE_URL = '/api/code-check';

// ========== Tab切换 ==========
function switchTab(name) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
    document.querySelector(`.tab[onclick*="'${name}'"]`).classList.add('active');
    document.getElementById(`tab-${name}`).classList.add('active');

    if (name === 'records') loadRecords();
    if (name === 'stats') loadStats();
}

// ========== 代码检测 ==========
async function submitCheck() {
    const fileName = document.getElementById('fileName').value.trim() || 'Unknown.java';
    const fileType = document.getElementById('fileType').value;
    const content = document.getElementById('codeInput').value.trim();

    if (!content) {
        alert('请粘贴要检测的源代码');
        return;
    }

    const btn = document.querySelector('.btn-primary');
    btn.disabled = true;
    btn.textContent = '⏳ 检测中...';

    try {
        const res = await fetch(`${BASE_URL}/check`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fileName, fileType, content })
        });
        const json = await res.json();

        if (json.code === 200) {
            displayResult(json.data);
        } else {
            alert('检测失败：' + json.message);
        }
    } catch (err) {
        alert('请求失败：' + err.message);
    } finally {
        btn.disabled = false;
        btn.textContent = '🚀 开始检测';
    }
}

function displayResult(data) {
    document.getElementById('resultArea').classList.remove('hidden');

    const scoreClass = data.codeScore >= 90 ? 'score-excellent'
        : data.codeScore >= 75 ? 'score-good'
        : data.codeScore >= 60 ? 'score-pass' : 'score-fail';

    const statusBadge = data.checkStatus === 'PASSED'
        ? '<span class="badge badge-passed">通过</span>'
        : '<span class="badge badge-failed">未通过</span>';

    const summaryHtml = `
        <div style="display:flex;gap:20px;flex-wrap:wrap;margin-bottom:16px;">
            <div class="stat-item"><span class="stat-value ${scoreClass}">${data.codeScore.toFixed(1)}</span><div class="stat-label">代码评分</div></div>
            <div class="stat-item"><span class="stat-value">${data.issueCount}</span><div class="stat-label">问题总数</div></div>
            <div class="stat-item"><span class="stat-value" style="color:#ff4d4f">${data.criticalCount}</span><div class="stat-label">致命</div></div>
            <div class="stat-item"><span class="stat-value" style="color:#faad14">${data.majorCount}</span><div class="stat-label">严重</div></div>
            <div class="stat-item"><span class="stat-value" style="color:#13c2c2">${data.minorCount}</span><div class="stat-label">一般</div></div>
            <div class="stat-item">${statusBadge}<div class="stat-label">状态</div></div>
        </div>
    `;

    let issuesHtml = '<h4 style="margin-top:16px;margin-bottom:8px;">📋 检测问题列表</h4>';
    if (data.issues && data.issues.length > 0) {
        data.issues.forEach((issue, i) => {
            issuesHtml += `
                <div class="issue-item severity-${issue.severity}">
                    <div class="issue-header">
                        <span class="issue-msg">#${i+1} [${issue.severity}] ${issue.message}</span>
                        <span style="font-size:12px;color:#999;">第${issue.line}行 · ${issue.ruleName}</span>
                    </div>
                    ${issue.snippet ? `<div class="issue-snippet">${escapeHtml(issue.snippet)}</div>` : ''}
                    ${issue.suggestion ? `<div class="issue-suggestion">💡 ${escapeHtml(issue.suggestion)}</div>` : ''}
                </div>
            `;
        });
    } else {
        issuesHtml += '<p style="color:#52c41a;font-size:16px;">🎉 未发现任何代码质量问题！</p>';
    }

    document.getElementById('resultSummary').innerHTML = summaryHtml;
    document.getElementById('resultIssues').innerHTML = issuesHtml;
    document.getElementById('resultArea').scrollIntoView({ behavior: 'smooth' });
}

// ========== 检测记录 ==========
async function loadRecords() {
    try {
        const res = await fetch(`${BASE_URL}/records`);
        const json = await res.json();

        if (json.code !== 200) {
            document.getElementById('recordsBody').innerHTML = '<tr><td colspan="7">加载失败</td></tr>';
            return;
        }

        const records = json.data || [];
        if (records.length === 0) {
            document.getElementById('recordsBody').innerHTML = '<tr><td colspan="7">暂无检测记录</td></tr>';
            return;
        }

        let html = '';
        records.forEach(r => {
            const badge = r.checkStatus === 'PASSED'
                ? '<span class="badge badge-passed">通过</span>'
                : r.checkStatus === 'FAILED'
                    ? '<span class="badge badge-failed">未通过</span>'
                    : '<span class="badge badge-running">检测中</span>';

            const scoreClass = r.codeScore >= 90 ? 'score-excellent'
                : r.codeScore >= 75 ? 'score-good'
                : r.codeScore >= 60 ? 'score-pass' : 'score-fail';

            html += `<tr>
                <td>${r.recordId}</td>
                <td>${r.fileName}</td>
                <td>${badge}</td>
                <td>${r.issueCount}</td>
                <td class="${scoreClass}">${r.codeScore.toFixed(1)}</td>
                <td>${r.createTime ? new Date(r.createTime).toLocaleString() : '-'}</td>
                <td><button class="btn-secondary" onclick="viewResult(${r.recordId})">查看</button></td>
            </tr>`;
        });
        document.getElementById('recordsBody').innerHTML = html;
    } catch (err) {
        document.getElementById('recordsBody').innerHTML = '<tr><td colspan="7">请求失败：' + err.message + '</td></tr>';
    }
}

async function viewResult(id) {
    try {
        const res = await fetch(`${BASE_URL}/result/${id}`);
        const json = await res.json();
        if (json.code === 200 && json.data) {
            switchTab('check');
            displayResult(json.data);
        }
    } catch (err) {
        alert('获取结果失败');
    }
}

// ========== 统计概览 ==========
async function loadStats() {
    try {
        const res = await fetch(`${BASE_URL}/stats`);
        const json = await res.json();

        if (json.code !== 200) {
            document.getElementById('statsContent').innerHTML = '<p>加载失败</p>';
            return;
        }

        const s = json.data;
        const avgClass = s.averageScore >= 90 ? 'score-excellent'
            : s.averageScore >= 75 ? 'score-good'
            : s.averageScore >= 60 ? 'score-pass' : 'score-fail';

        document.getElementById('statsContent').innerHTML = `
            <div class="stat-item"><span class="stat-value">${s.totalFiles}</span><div class="stat-label">检测文件数</div></div>
            <div class="stat-item"><span class="stat-value">${s.totalChecks}</span><div class="stat-label">总检测次数</div></div>
            <div class="stat-item"><span class="stat-value" style="color:#13c2c2">${s.passedFiles}</span><div class="stat-label">通过文件</div></div>
            <div class="stat-item"><span class="stat-value" style="color:#ff4d4f">${s.failedFiles}</span><div class="stat-label">未通过文件</div></div>
            <div class="stat-item"><span class="stat-value">${s.totalIssues}</span><div class="stat-label">累计问题数</div></div>
            <div class="stat-item"><span class="stat-value ${avgClass}">${s.averageScore.toFixed(1)}</span><div class="stat-label">平均评分</div></div>
        `;
    } catch (err) {
        document.getElementById('statsContent').innerHTML = '<p>请求失败</p>';
    }
}

// ========== 工具 ==========
function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;').replace(/'/g, '&#039;');
}

// 页面加载时自动加载统计
document.addEventListener('DOMContentLoaded', () => {
    loadStats();
});
