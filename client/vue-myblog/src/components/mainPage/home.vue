<template>
    <div class="home-container">
        <!-- 全屏背景层 -->
        <div class="background-layer"></div>

        <!-- 左侧栏 -->
        <div class="panel left-panel">
            <div class="panel-content">
                <div class="section-placeholder" @click="goToMy">
                    <div class="placeholder-icon"> </div>
                    <h3>我的</h3>
                    <p>my</p>
                </div>

                <div class="section-placeholder" @click="goToGraph">
                    <div class="placeholder-icon"> </div>
                    <h3>图谱</h3>
                    <p> graph</p>
                </div>
                <div class="section-placeholder" @click="goToDashboard">
                    <div class="placeholder-icon"></div>
                    <h3>数据中心</h3>
                    <p>data center</p>
                </div>
            </div>
        </div>

        <!-- 中间主内容区 -->
        <div class="panel center-panel">
            <div class="panel-content">
                <div class="hero-section">
                    <div  class="placeholder-icon large">✨</div>
                    <h2 style="color:black">欢迎体验智能笔记</h2>
                    <p style="color:black">Here is My System</p>
                </div>

            </div>
        </div>

        <!-- 右侧栏 -->
        <div class="panel right-panel">
            <div class="panel-content">
                <div class="section-placeholder" @click="goToBrowse">
                    <div class="placeholder-icon"> </div>
                    <h3>浏览</h3>
                    <p>list</p>
                </div>
                <div class="section-placeholder" @click="goToWrite">
                    <div class="placeholder-icon"></div>
                    <h3>创作</h3>
                    <p>creation</p>
                </div>
                <div class="section-placeholder" @click="goToDashboard">
                    <div class="placeholder-icon"></div>
                    <h3>数据中心</h3>
                    <p>data center</p>
                </div>
                <!-- <div class="section-placeholder">
                    <div class="placeholder-icon">💬</div>
                    <h3>最新评论</h3>
                    <p>Recent Comments</p>
                </div> -->

            </div>
        </div>

        <!-- 音乐律动条组件 -->
        <div class="music-visualizer">
            <div
                v-for="(bar, index) in 32"
                :key="index"
                class="visualizer-bar"
                :ref="el => { if (el) visualizerBars[index] = el }"
                :style="{ height: barHeights[index] + '%' }"
            ></div>
        </div>
    </div>
</template>

<script setup name="Home">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function goToMy() { router.push('/main/infor/dashboard') }
function goToBrowse() { router.push('/main/articleList') }
function goToWrite() { router.push('/main/release') }
function goToGraph() { router.push('/main/knowledgeGraph') }
function goToDashboard() { router.push('/main/infor/dashboard') }

// 律动条相关状态
const visualizerBars = ref([])
const barHeights = ref(Array(32).fill(20)) // 默认高度20%
const audioContext = ref(null)
const analyser = ref(null)
let animationId = null

// 初始化音频可视化
const initAudioVisualizer = () => {
    try {
        // 创建音频上下文
        audioContext.value = new (window.AudioContext || window.webkitAudioContext)()
        analyser.value = audioContext.value.createAnalyser()
        analyser.value.fftSize = 64 // 32个柱子需要64的FFT大小
        analyser.value.smoothingTimeConstant = 0.8

        // 创建模拟音频数据（用于演示）
        // 实际使用时可以连接真实的音频源
        startVisualization()
    } catch (error) {
        console.error('音频可视化初始化失败:', error)
    }
}

// 启动可视化动画
const startVisualization = () => {
    const dataArray = new Uint8Array(analyser.value.frequencyBinCount)

    const animate = () => {
        animationId = requestAnimationFrame(animate)

        // 生成模拟音频数据（正弦波+随机噪声，模拟真实音乐节奏）
        const time = Date.now() / 1000
        for (let i = 0; i < dataArray.length; i++) {
            // 基础频率 + 节拍调制 + 随机变化
            const baseFreq = Math.sin(time * 2 + i * 0.3) * 30 + 40
            const beatModulation = Math.sin(time * 4) * 20 // 模拟节拍
            const randomNoise = (Math.random() - 0.5) * 15
            dataArray[i] = Math.max(10, Math.min(100, baseFreq + beatModulation + randomNoise))
        }

        // 更新律动条高度（取前32个数据点）
        barHeights.value = Array.from(dataArray).slice(0, 32)
    }

    animate()
}

// 组件挂载时初始化
onMounted(() => {
    initAudioVisualizer()
})

// 组件卸载时清理资源
onUnmounted(() => {
    if (animationId) {
        cancelAnimationFrame(animationId)
    }
    if (audioContext.value) {
        audioContext.value.close()
    }
})
</script>

<style scoped>
/* ========== 容器布局 ========== */
.home-container {
    width: 100vw;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 30px;
    padding: 60px 80px;
    box-sizing: border-box;
    position: relative;
    overflow: hidden;
    background: linear-gradient(
        135deg,
        rgba(100, 50, 150, 0.15) 0%,
        rgba(50, 100, 200, 0.12) 50%,
        rgba(150, 50, 100, 0.15) 100%
    );
    backdrop-filter: blur(20px) saturate(1.2);
    -webkit-backdrop-filter: blur(20px) saturate(1.2);
}

/* ========== 全屏背景层 ========== */
.background-layer {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* background-image: url('/ARTI_01_卡哇伊_右.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat; */
    z-index: -2;
    filter: brightness(1.1) contrast(1.05) saturate(1.1);
}

/* ========== 面板通用样式 ========== */
.panel {
    border-radius: 12px;
    position: relative;
    overflow: hidden;
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
}

.panel-content {

    padding: 25px;
    height: 100%;
    display: flex;
    flex-direction: column;
    gap: 20px;
    overflow-y: auto;
}

/* 自定义滚动条 */
.panel-content::-webkit-scrollbar {
    width: 6px;
}

.panel-content::-webkit-scrollbar-track {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 3px;
}

.panel-content::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 3px;
}

.panel-content::-webkit-scrollbar-thumb:hover {
    background: rgba(255, 255, 255, 0.3);
}

/* ========== 左侧栏样式 ========== */
.left-panel {
    flex: 0 0 320px;
    height: 80vh;
    background: rgba(255, 255, 255, 0);
   /* */
    border: none;
    outline: none;
    box-shadow: none;
    animation: fadeInLeft 0.6s ease 0.1s both;
    transform: skewY(12deg) translateY(-5%);
    margin-left: -30px;
    overflow: visible;
}

.left-panel .panel-content {
    transform: skewY(-12deg);
    padding-top: 35px;
}

/* ========== 中间主内容区样式 ========== */
.center-panel {
    flex: 1;
    max-width: 600px;
    height: 75vh;
    background: rgba(255, 255, 255, 0);
    border: none;
    outline: none;
    box-shadow: none;
    animation: fadeInCenter 0.6s ease 0.2s both;
}

/* ========== 右侧栏样式 ========== */
.right-panel {
    flex: 0 0 320px;
    height: 80vh;
    background: rgba(255, 255, 255, 0);
    border: none;
    outline: none;
    box-shadow: none;
    animation: fadeInRight 0.6s ease 0.3s both;
    transform: skewY(-12deg) translateY(-5%);
    margin-right: -30px;
    overflow: visible;
}

.right-panel .panel-content {
    transform: skewY(12deg);
    padding-top: 35px;
}

/* ========== 内容占位符样式 ========== */
.section-placeholder {
    background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.18) 0%,
        rgba(255, 255, 255, 0.08) 50%,
        rgba(200, 220, 255, 0.12) 100%
    );
    backdrop-filter: blur(15px) saturate(1.3);
    -webkit-backdrop-filter: blur(15px) saturate(1.3);
    border: 1px solid transparent;
    border-radius: 16px;
    padding: 26px;
    text-align: center;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.section-placeholder::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.2),
        transparent
    );
    transition: left 0.6s ease;
}

.section-placeholder:hover {
    background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.25) 0%,
        rgba(255, 255, 255, 0.15) 50%,
        rgba(220, 240, 255, 0.18) 100%
    );
    border-color: rgba(255, 255, 255, 0.4);
    transform: translateY(-8px) scale(1.03);
    box-shadow:
        0 20px 40px rgba(0, 0, 0, 0.3),
        0 10px 20px rgba(100, 150, 255, 0.2),
        inset 0 1px 0 rgba(255, 255, 255, 0.3),
        inset 0 -1px 0 rgba(0, 0, 0, 0.1);
}

.section-placeholder:hover::before {
    left: 100%;
}

/* 左侧区域小区域渐变阴影效果 */
.left-panel .section-placeholder:nth-child(1) {
    box-shadow:
        0 8px 24px rgba(100, 50, 200, 0.2),
        0 4px 12px rgba(0, 0, 0, 0.15),
        inset 0 1px 0 rgba(255, 255, 255, 0.15);
}

.left-panel .section-placeholder:nth-child(2) {
    box-shadow:
        0 16px 40px rgba(150, 80, 200, 0.35),
        0 8px 20px rgba(0, 0, 0, 0.25),
        inset 0 2px 0 rgba(255, 255, 255, 0.2),
        inset 0 -2px 0 rgba(150, 50, 150, 0.1);
}

.left-panel .section-placeholder:nth-child(3) {
    box-shadow:
        0 8px 24px rgba(100, 50, 200, 0.2),
        0 4px 12px rgba(0, 0, 0, 0.15),
        inset 0 1px 0 rgba(255, 255, 255, 0.15);
}

/* 右侧区域小区域渐变阴影效果（暖色调） */
.right-panel .section-placeholder:nth-child(1) {
    box-shadow:
        0 8px 24px rgba(200, 100, 50, 0.25),
        0 4px 12px rgba(0, 0, 0, 0.15),
        inset 0 1px 0 rgba(255, 220, 180, 0.2);
}

.right-panel .section-placeholder:nth-child(2) {
    box-shadow:
        0 16px 40px rgba(230, 120, 80, 0.4),
        0 8px 20px rgba(0, 0, 0, 0.25),
        inset 0 2px 0 rgba(255, 240, 200, 0.25),
        inset 0 -2px 0 rgba(200, 100, 50, 0.1);
}

.right-panel .section-placeholder:nth-child(3) {
    box-shadow:
        0 8px 24px rgba(200, 100, 50, 0.25),
        0 4px 12px rgba(0, 0, 0, 0.15),
        inset 0 1px 0 rgba(255, 220, 180, 0.2);
}

.right-panel .section-placeholder:nth-child(4) {
    box-shadow:
        0 8px 24px rgba(200, 100, 50, 0.25),
        0 4px 12px rgba(0, 0, 0, 0.15),
        inset 0 1px 0 rgba(255, 220, 180, 0.2);
}

.placeholder-icon {
    font-size: 36px;
    margin-bottom: 12px;
    opacity: 1;
    filter: drop-shadow(0 2px 8px rgba(255, 255, 255, 0.3));
    transition: all 0.3s ease;
}

.placeholder-icon.large {
    font-size: 56px;
    margin-bottom: 18px;
    filter: drop-shadow(0 4px 16px rgba(255, 255, 255, 0.4));
}

.section-placeholder h3 {
    color: rgba(255, 255, 255, 1);
    font-size: 18px;
    font-weight: 700;
    margin: 10px 0 6px 0;
    letter-spacing: 1px;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.section-placeholder p {
    color: rgba(255, 255, 255, 0.75);
    font-size: 14px;
    margin: 0;
    font-weight: 400;
    letter-spacing: 0.5px;
}

/* Hero区域特殊样式 */
.hero-section {
    text-align: center;
    padding: 32px 24px;
    background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.2) 0%,
        rgba(200, 220, 255, 0.15) 50%,
        rgba(255, 200, 220, 0.18) 100%
    );
    backdrop-filter: blur(12px) saturate(1.4);
    -webkit-backdrop-filter: blur(12px) saturate(1.4);
    border: 1px solid rgba(255, 255, 255, 0.25);
    border-radius: 16px;
    margin-bottom: 12px;
    box-shadow:
        0 8px 24px rgba(0, 0, 0, 0.2),
        inset 0 1px 0 rgba(255, 255, 255, 0.25);
}

.hero-section h2 {
    color: rgba(255, 255, 255, 1);
    font-size: 24px;
    font-weight: 800;
    margin: 15px 0 8px 0;
    letter-spacing: 1px;
}

.hero-section p {
    color: rgba(255, 255, 255, 0.7);
    font-size: 14px;
    margin: 0;
}

.content-section {
    display: flex;
    flex-direction: column;
    gap: 15px;
    flex: 1;
}

/* ========== 入场动画 ========== */
@keyframes fadeInLeft {
    from {
        opacity: 0;
        transform: translateX(-40px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

@keyframes fadeInCenter {
    from {
        opacity: 0;
        transform: translateY(30px) scale(0.95);
    }
    to {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

@keyframes fadeInRight {
    from {
        opacity: 0;
        transform: translateX(40px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

/* ========== 响应式设计：平板端（900px-1200px）========== */
@media (max-width: 1200px) {
    .home-container {
        padding: 40px 30px;
        gap: 20px;
    }

    .left-panel,
    .right-panel {
        flex: 0 0 320px;
        height: 70vh;
    }

    .left-panel {
        transform: skewY(10deg);
        margin-left: -24px;
    }

    .left-panel .panel-content {
        transform: skewY(-10deg);
    }

    .right-panel {
        transform: skewY(-10deg);
        margin-right: -24px;
    }

    .right-panel .panel-content {
        transform: skewY(10deg);
    }

    .center-panel {
        max-width: 400px;
        height: 72vh;
    }

    .panel-content {
        padding: 20px;
        gap: 15px;
    }

    .hero-section {
        padding: 25px 15px;
    }

    .hero-section h2 {
        font-size: 20px;
    }

    .section-placeholder {
        padding: 15px;
    }

    .placeholder-icon {
        font-size: 24px;
    }

    .placeholder-icon.large {
        font-size: 40px;
    }

    .section-placeholder h3 {
        font-size: 14px;
    }
}

/* ========== 响应式设计：移动端（<900px）========== */
@media (max-width: 900px) {
    .home-container {
        flex-direction: column;
        padding: 20px 15px;
        gap: 15px;
        height: auto;
        min-height: 100vh;
        justify-content: flex-start;
        padding-top: 30px;
        padding-bottom: 30px;
    }

    .left-panel,
    .center-panel,
    .right-panel {
        flex: 1;
        width: 100%;
        max-width: none;
        height: auto;
        min-height: 280px;
        animation: fadeInUp 0.5s ease both;
    }

    .left-panel {
        animation-delay: 0.1s;
        min-height: 250px;
        transform: skewY(0);
        margin-right: 0;
    }

    .left-panel .panel-content {
        transform: skewY(0);
    }

    .center-panel {
        animation-delay: 0.2s;
        min-height: 350px;
    }

    .right-panel {
        animation-delay: 0.3s;
        min-height: 300px;
        transform: skewY(0);
        margin-right: 0;
    }

    .right-panel .panel-content {
        transform: skewY(0);
    }

    .panel-content {
        padding: 18px;
        gap: 12px;
    }

    .hero-section {
        padding: 20px 15px;
    }

    .hero-section h2 {
        font-size: 18px;
    }

    .section-placeholder {
        padding: 12px;
    }

    .placeholder-icon {
        font-size: 22px;
        margin-bottom: 8px;
    }

    .placeholder-icon.large {
        font-size: 36px;
        margin-bottom: 12px;
    }

    .section-placeholder h3 {
        font-size: 13px;
        margin: 6px 0 4px 0;
    }

    .section-placeholder p {
        font-size: 12px;
    }

    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(25px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
}

/* ========== 超小屏幕优化（<600px）========== */
@media (max-width: 600px) {
    .home-container {
        padding: 15px 10px;
        gap: 12px;
    }

    .left-panel,
    .center-panel,
    .right-panel {
        min-height: 220px;
        border-radius: 10px;
    }

    .center-panel {
        min-height: 300px;
    }

    .panel-content {
        padding: 15px;
        gap: 10px;
    }

    .panel:hover {
        transform: translateY(-3px);
    }
}

/* ========== 音乐律动条组件 ========== */
.music-visualizer {
    position: fixed;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    width: 600px; /* 与中间区域宽度一致 */
    max-width: 90vw;
    height: 60px;
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    gap: 4px;
    padding: 14px 28px;
    background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.12) 0%,
        rgba(200, 200, 200, 0.08) 50%,
        rgba(180, 180, 180, 0.10) 100%
    );
    backdrop-filter: blur(20px) saturate(1.2);
    -webkit-backdrop-filter: blur(20px) saturate(1.2);
    border: 1px solid rgba(200, 200, 200, 0.18);
    border-radius: 30px;
    box-shadow:
        0 8px 32px rgba(0, 0, 0, 0.25),
        0 4px 16px rgba(128, 128, 128, 0.15),
        inset 0 1px 0 rgba(255, 255, 255, 0.2),
        inset 0 -1px 0 rgba(0, 0, 0, 0.08);
    z-index: 1000;
}

.visualizer-bar {
    width: 18px; /* 固定宽度 */
    max-width: 22px; /* 最大宽度限制 */
    height: 20%; /* 默认高度，由JS动态控制 */
    max-height: 36px;
    min-height: 6px;
    background: linear-gradient(
        to top,
        rgba(140, 140, 140, 0.7) 0%,
        rgba(160, 160, 160, 0.85) 50%,
        rgba(180, 180, 180, 0.95) 100%
    );
    border-radius: 3px;
    transition: height 0.08s ease-out; /* 平滑过渡 */
    position: relative;
}

/* 律动条悬停效果 */
.music-visualizer:hover {
    box-shadow:
        0 12px 48px rgba(0, 0, 0, 0.35),
        0 6px 24px rgba(128, 128, 128, 0.25),
        inset 0 1px 0 rgba(255, 255, 255, 0.3),
        inset 0 -1px 0 rgba(0, 0, 0, 0.12);
    transform: translateX(-50%) translateY(-3px);
}

/* 响应式：平板端 */
@media (max-width: 1200px) {
    .music-visualizer {
        width: 500px;
        height: 55px;
        padding: 12px 24px;
        bottom: 25px;
        gap: 4px;
    }

    .visualizer-bar {
        max-height: 32px;
        min-height: 5px;
    }
}

/* 响应式：移动端 */
@media (max-width: 900px) {
    .music-visualizer {
        width: 90vw;
        max-width: 400px;
        height: 50px;
        padding: 10px 18px;
        bottom: 20px;
        gap: 3px;
    }

    .visualizer-bar {
        max-height: 28px;
        min-height: 5px;
        border-radius: 2px;
    }
}
</style>
