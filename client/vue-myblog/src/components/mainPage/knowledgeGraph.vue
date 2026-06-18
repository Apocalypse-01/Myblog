<template>
    <div class="kg-container">
        <div class="kg-header">
            <h2 style="color:black;">知识图谱</h2>
            <p style="color:black;" >每篇文章是一个节点，共享关键词的文章自动关联。拖拽节点查看知识结构。</p>
            <div class="kg-legend">
                <span style="color:black;" class="legend-item"><span class="legend-dot article"></span> 文章</span>
                <span style="color:black;" class="legend-item"><span class="legend-dot tag"></span> 分类标签</span>
            </div>
        </div>
        <div v-if="loading" class="kg-loading">正在生成知识图谱...</div>
        <div v-else-if="nodes.length === 0" class="kg-empty">
            <p> 还没有文章，去写一篇吧！</p>
        </div>
        <div v-else ref="chartRef" class="kg-chart"></div>
    </div>
</template>

<script setup lang="ts" name="KnowledgeGraph">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getAllByAuthor } from '@/api/article.ts'

const chartRef = ref<HTMLElement | null>(null)
const loading = ref(true)
const nodes = ref<any[]>([])
let chart: echarts.ECharts | null = null

onMounted(async () => {
    const authorId = localStorage.getItem('authorId')
    if (!authorId) return

    try {
        const res = await getAllByAuthor(Number(authorId))
        if (res.code === 1 && res.data) {
            const articles = res.data as any[]
            if (articles.length === 0) {
                loading.value = false
                return
            }
            buildGraph(articles)
        }
    } catch (e) {
        loading.value = false
    }
})

function buildGraph(articles: any[]) {
    const graphNodes: any[] = []
    const graphEdges: any[] = []
    const tagSet = new Set<string>()
    const kwMap = new Map<string, number[]>()

    const stopWords = new Set([
        '的', '是', '在', '和', '了', '与', '及', '或',
        '一个', '一种', '都是', '就是', '这个', '那个', '可以', '没有',
        '使用', '进行', '通过', '基于', '利用', '实现', '设计', '开发',
        '笔记', '文章', '测试', '学习', '记录', '总结'
    ])

    for (const a of articles) {
        const articleId = 'article_' + a.id
        graphNodes.push({
            id: articleId, name: a.articleTitle, category: 0,
            symbolSize: 28, itemStyle: { color: '#6366f1' }
        })

        const tagId = 'tag_' + a.articleSign
        if (!tagSet.has(tagId)) {
            tagSet.add(tagId)
            graphNodes.push({
                id: tagId, name: a.articleSign, category: 1,
                symbolSize: 40, itemStyle: { color: '#ec4899' }
            })
        }
        graphEdges.push({ source: articleId, target: tagId })

        const title: string = a.articleTitle || ''
        const clean = title.replace(/[^\u4e00-\u9fa5a-zA-Z0-9]/g, '')
        for (let len = 2; len <= 4; len++) {
            for (let i = 0; i <= clean.length - len; i++) {
                const word = clean.substring(i, i + len)
                if (!stopWords.has(word)) {
                    if (!kwMap.has(word)) kwMap.set(word, [])
                    kwMap.get(word)!.push(a.id)
                }
            }
        }
    }

    const addedPairs = new Set<string>()
    for (const [, ids] of kwMap) {
        if (ids.length >= 2) {
            for (let i = 0; i < ids.length; i++) {
                for (let j = i + 1; j < ids.length; j++) {
                    const key = ids[i] + '_' + ids[j]
                    if (!addedPairs.has(key)) {
                        addedPairs.add(key)
                        graphEdges.push({
                            source: 'article_' + ids[i],
                            target: 'article_' + ids[j],
                            lineStyle: { color: '#c4b5fd', opacity: 0.4 }
                        })
                    }
                }
            }
        }
    }

    nodes.value = graphNodes
    loading.value = false

    nextTick(() => renderChart(graphNodes, graphEdges))
}

function renderChart(graphNodes: any[], graphEdges: any[]) {
    if (!chartRef.value) return
    if (chart) chart.dispose()

    chart = echarts.init(chartRef.value)
    chart.setOption({
        tooltip: {
            formatter: (params: any) => {
                if (params.dataType === 'node') {
                    return params.data.category === 1
                        ? '<b>标签：' + params.name + '</b>'
                        : '<b>文章：' + params.name + '</b>'
                }
                return ''
            }
        },
        series: [{
            type: 'graph',
            layout: 'force',
            roam: true,
            draggable: true,
            force: { repulsion: 300, edgeLength: [120, 300], gravity: 0.08 },
            data: graphNodes,
            edges: graphEdges,
            label: { show: true, fontSize: 12, color: '#333' },
            emphasis: { focus: 'adjacency', label: { fontSize: 16, fontWeight: 'bold' } },
            lineStyle: { color: '#c4b5fd', curveness: 0.2, opacity: 0.5, width: 1.5 }
        }]
    })

    window.addEventListener('resize', () => chart?.resize())
}

onUnmounted(() => {
    chart?.dispose()
})
</script>

<style scoped>
.kg-container {
    width: 85vw;
    height: calc(100vh - 60px);
    display: flex;
    flex-direction: column;
    padding: 20px 40px;
    box-sizing: border-box;
    background-color: rgba(255, 182, 213, 0.25);
    backdrop-filter: blur(8px);
    border: 2px solid rgba(255, 182, 213, 0.4);
    border-radius: 12px;
    margin: 0 auto;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, 0.1);
}
.kg-header {
    text-align: center;
    flex-shrink: 0;
    margin-bottom: 12px;
}
.kg-header h2 {
    color: #fff;
    text-shadow: 0 1px 3px rgba(0,0,0,0.2);
    margin: 0 0 4px 0;
    font-size: 22px;
}
.kg-header p {
    color: rgba(255,255,255,0.75);
    font-size: 13px;
    margin: 0;
}
.kg-legend {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 8px;
}
.legend-item {
    display: flex;
    align-items: center;
    gap: 6px;
    color: rgba(255,255,255,0.85);
    font-size: 13px;
}
.legend-dot {
    width: 12px;
    height: 12px;
    border-radius: 50%;
}
.legend-dot.article { background: #6366f1; }
.legend-dot.tag { background: #ec4899; }
.kg-loading, .kg-empty {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    color: rgba(255,255,255,0.7);
    font-size: 16px;
}
.kg-chart {
    flex: 1;
    min-height: 400px;
    border-radius: 8px;
    background: #fdf4f6;
    border: 1px solid rgba(255, 182, 213, 0.35);
}
</style>
