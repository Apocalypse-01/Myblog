<template>
    <div class="db-container">
        <div class="db-header">
            <h2>数据中心</h2>
            <p style="color: bisque;">你的写作数据全景视图</p>
        </div>

        <div v-if="loading" class="db-loading">正在加载数据...</div>
        <div v-else class="db-body">
            <div class="stats-row">
                <div class="stat-card">
                    <div class="stat-num">{{ data.totalArticles }}</div>
                    <div class="stat-label">文章总数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num">{{ formatNum(data.totalWords) }}</div>
                    <div class="stat-label">总字数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num">{{ data.totalLikes }}</div>
                    <div class="stat-label">收获点赞</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num">{{ data.totalFavorites }}</div>
                    <div class="stat-label">被收藏数</div>
                </div>
            </div>

            <div class="charts-row">
                <div class="chart-card">
                    <h3>每日发文趋势</h3>
                    <div ref="trendRef" class="chart-box"></div>
                </div>
                <div class="chart-card">
                    <h3>分类占比</h3>
                    <div ref="pieRef" class="chart-box"></div>
                </div>
            </div>

            <div class="top-card">
                <h3>最受欢迎文章 Top 5</h3>
                <div v-if="data.topArticles && data.topArticles.length > 0" class="top-list">
                    <div class="top-item" v-for="(item, idx) in data.topArticles" :key="item.id">
                        <span class="top-rank">{{ idx + 1 }}</span>
                        <span class="top-title">{{ item.title }}</span>
                        <span class="top-stats">👍 {{ item.likes }} &nbsp; ⭐ {{ item.favorites }}</span>
                    </div>
                </div>
                <div v-else class="db-empty">暂无数据</div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts" name="Dashboard">
import { ref, onMounted, nextTick, watch } from 'vue'
import * as echarts from 'echarts'

const loading = ref(true)
const trendRef = ref<HTMLElement | null>(null)
const pieRef = ref<HTMLElement | null>(null)
const data = ref<any>({})

function formatNum(n: number) {
    if (n >= 10000) return (n / 10000).toFixed(1) + '万'
    if (n >= 1000) return (n / 1000).toFixed(1) + 'k'
    return String(n)
}

onMounted(async () => {
    const authorId = localStorage.getItem('authorId')
    if (!authorId) { loading.value = false; return }
    try {
        const resp = await fetch(`http://localhost:8081/article/dashboard?authorId=${authorId}`)
        const json = await resp.json()
        if (json.code === 1) {
            data.value = json.data
        }
    } catch (e) { console.error(e) }
    loading.value = false
})

watch(loading, (val) => {
    if (!val) {
        nextTick(() => {
            if (data.value.dailyTrend && data.value.dailyTrend.length > 0) {
                renderTrend(data.value.dailyTrend)
            }
            if (data.value.categoryDistribution && data.value.categoryDistribution.length > 0) {
                renderPie(data.value.categoryDistribution)
            }
        })
    }
})

function renderTrend(monthly: any[]) {
    if (!trendRef.value) return
    const chart = echarts.init(trendRef.value)
    chart.setOption({
        grid: { top: 20, right: 20, bottom: 30, left: 40 },
        xAxis: { type: 'category', data: monthly.map((m: any) => m.month), axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
            type: 'bar', data: monthly.map((m: any) => m.count),
            itemStyle: { color: '#6366f1', borderRadius: [4, 4, 0, 0] },
            barWidth: '50%'
        }]
    })
}

function renderPie(categories: any[]) {
    if (!pieRef.value) return
    const chart = echarts.init(pieRef.value)
    chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
            type: 'pie', radius: ['40%', '70%'],
            data: categories.map((c: any) => ({ name: c.category, value: c.count })),
            label: { fontSize: 12 },
            color: ['#6366f1', '#ec4899', '#f59e0b', '#10b981']
        }]
    })
}
</script>

<style scoped>
.db-container {
    width: 80%;
    height: 100%;
    max-height: calc(100vh - 60px);
    overflow-y: auto;
    padding: 0;
    box-sizing: border-box;
    background-color:rgba(255, 182, 213, 0.25);
    backdrop-filter: blur(8px);
    border: 2px solid rgba(255, 182, 213, 0.4);
    border-radius: 12px;
    box-shadow: 2px 2px 3px rgba(0,0,0,0.1);
}

.db-body {
    flex-direction: column;
    margin: 5% auto 5%;
    width: 90%;
}

.db-header { text-align: center; margin-bottom: 14px; }
.db-header h2 { color: #333; font-size: 16px; margin: 0 0 2px; }
.db-header p { color: #888; font-size: 12px; margin: 0; }
.db-loading { text-align: center; color: #999; padding: 40px 0; font-size: 14px; }

.stats-row { display: flex; gap: 8px; margin-bottom: 12px; flex-wrap: wrap; }
.stat-card {
    flex: 1; min-width: 80px; background: linear-gradient(135deg, #6366f1, #8b5cf6);
    border-radius: 8px; padding: 10px 6px; text-align: center; color: #fff;
}
.stat-card:nth-child(2) { background: linear-gradient(135deg, #ec4899, #f472b6); }
.stat-card:nth-child(3) { background: linear-gradient(135deg, #f59e0b, #fbbf24); }
.stat-card:nth-child(4) { background: linear-gradient(135deg, #10b981, #34d399); }
.stat-num { font-size: 20px; font-weight: 700; }
.stat-label { font-size: 11px; opacity: 0.85; margin-top: 2px; }

.charts-row { display: flex; gap: 8px; margin-bottom: 12px; }
.chart-card { flex: 1; background: #fff; border-radius: 8px; padding: 10px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.chart-card h3 { color: #333; font-size: 12px; margin: 0 0 4px; }
.chart-box { width: 100%; height: 160px; }

.top-card { background: #fff; border-radius: 8px; padding: 10px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.top-card h3 { color: #333; font-size: 12px; margin: 0 0 6px; }
.top-list { display: flex; flex-direction: column; gap: 3px; }
.top-item { display: flex; align-items: center; gap: 6px; padding: 4px 8px; border-radius: 6px; background: #f8f9fa; }
.top-rank { width: 18px; height: 18px; border-radius: 50%; background: #6366f1; color: #fff; text-align: center; line-height: 18px; font-size: 10px; font-weight: 700; flex-shrink: 0; }
.top-item:nth-child(1) .top-rank { background: #f59e0b; }
.top-item:nth-child(2) .top-rank { background: #94a3b8; }
.top-item:nth-child(3) .top-rank { background: #d97706; }
.top-title { flex: 1; font-size: 14px; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.top-stats { font-size: 12px; color: #888; flex-shrink: 0; }
.db-empty { text-align: center; color: #aaa; padding: 20px; font-size: 13px; }
</style>
