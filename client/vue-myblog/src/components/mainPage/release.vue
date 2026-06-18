<template>
    <div class="release-page">

        <div class="release-container"> 

            <div class="header-bar">
                <input  type="text" 
                        class="title-input"
                        placeholder="请输入文章标题"
                        v-model="articleTitle"
                        >
                
                <div class="tag-area"> 
                    <span class="tag-label">标签：</span>
                    <el-radio-group v-model="aricleIntroduction">
                        <el-radio value="日常">日常</el-radio>
                        <el-radio value="技术">技术</el-radio>
                        <el-radio value="杂谈">杂谈</el-radio>
                        <el-radio value="其他">其他</el-radio>
                    </el-radio-group>
                </div>

                <a class="markdown-tutorial" href="https://markdown.com.cn/basic-syntax/" target="_blank">Markdown教程</a>

                <div class="ai-tools">
                    <el-tooltip content="AI 根据当前内容续写文章" placement="bottom">
                        <el-button class="ai-btn" @click="aiContinue" :loading="aiLoading"> 续写</el-button>
                    </el-tooltip>
                    <el-tooltip content="AI 概括文章核心内容" placement="bottom">
                        <el-button class="ai-btn" @click="aiSummarize" :loading="aiLoading">总结</el-button>
                    </el-tooltip>
                    <el-tooltip content="AI 生成更有吸引力的标题" placement="bottom">
                        <el-button class="ai-btn" @click="aiPolishTitle" :loading="aiLoading">优化标题</el-button>
                    </el-tooltip>
                    <el-tooltip content="AI 检查错别字和语法问题" placement="bottom">
                        <el-button class="ai-btn" @click="aiCheckTypos" :loading="aiLoading">检查错误</el-button>
                    </el-tooltip>
                </div>
            </div>

            <div class="editor-area">
                <MdEditor
                    toolbarsExclude="['link', 'mermaid', 'katex', 'github']"
                    v-model="articleContent"
                    @onUploadImg="onUploadImg"
                    @onSave="codeSave"
                    placeholder="请输入文章内容"      
                />
            </div>

            <div class="floating-actions">
                <el-tooltip content="将当前内容暂存到本地，下次打开可继续编辑" placement="top">
                    <el-button class="action-btn draft-btn" @click="saveDraft()">
                        保存草稿
                    </el-button>
                </el-tooltip>
                    
                <el-tooltip :content="isEdit ? '提交修改并更新文章' : '发布文章到博客，发布后可在文章列表中查看'" placement="top">
                    <el-button class="action-btn publish-btn" @click="submit()">
                        {{ isEdit ? '更新文章' : '发布文章' }}
                    </el-button>
                </el-tooltip>
            </div>

        </div>

    </div>
    
</template>


<script setup lang="ts">
import 'md-editor-v3/lib/style.css';
import { MdEditor } from 'md-editor-v3'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {Article} from '@/class/class'
import {releaseArticle, updateArticle, getArticle} from '@/api/article'
import { aiAssist } from '@/api/ai'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

const articleTitle=ref('')
const aricleIntroduction=ref('')
const articleContent=ref('')
const isEdit=ref(false)
const editArticleId=ref(null)

onMounted(async ()=>{
    if(route.query.isEdit === 'true'){
        isEdit.value=true
        editArticleId.value=Number(route.query.id)

        const editData = localStorage.getItem('editArticleData')
        if(editData){
            try {
                const data = JSON.parse(editData)
                articleTitle.value=data.articleTitle || ''
                aricleIntroduction.value=data.articleSign || ''
            } catch(e) {
                articleTitle.value=''
                aricleIntroduction.value=''
            }
        }

        try {
            const res = await getArticle(Number(route.query.id))
            if(res && res.code == 1 && res.data){
                articleContent.value = res.data.articleContent || ''
                if(!articleTitle.value){
                    articleTitle.value = res.data.articleTitle || ''
                }
                if(!aricleIntroduction.value){
                    aricleIntroduction.value = res.data.articleSign || ''
                }
            }
        } catch(e) {}
    } else {
        const draft = localStorage.getItem('articleDraft')
        if(draft){
            try {
                const draftData = JSON.parse(draft)
                articleTitle.value=draftData.articleTitle || ''
                aricleIntroduction.value=draftData.articleSign || ''
                articleContent.value=draftData.articleContent || ''
            } catch(e) {}
        }
    }
})

const aiLoading = ref(false)

async function handleAiAction(action: string, successMsg: string) {
    if (!articleContent.value.trim()) {
        ElMessage.warning('请先输入文章内容')
        return
    }
    aiLoading.value = true
    try {
        const res = await aiAssist(action, articleContent.value, articleTitle.value)
        if (res.code === 1 && res.data) {
            const result = res.data.result
            if (action === 'polishTitle') {
                ElMessageBox.alert(result, 'AI 建议标题', {
                    confirmButtonText: '关闭'
                })
            } else if (action === 'checkTypos') {
                ElMessageBox.alert(result, '错别字检查结果', {
                    confirmButtonText: '知道了'
                })
            } else {
                articleContent.value = articleContent.value + '\n\n' + result
                ElMessage.success(successMsg)
            }
        } else {
            ElMessage.error(res.msg || 'AI 返回异常')
        }
    } catch (e) {
        ElMessage.error('AI 服务调用失败')
    } finally {
        aiLoading.value = false
    }
}

function aiContinue()   { handleAiAction('continue', '续写内容已添加到文末') }
function aiSummarize()  { handleAiAction('summarize', '摘要已添加到文末') }
function aiPolishTitle(){ handleAiAction('polishTitle', '') }
function aiCheckTypos() { handleAiAction('checkTypos', '') }

function saveDraft(){
    const draftData = {
        articleTitle: articleTitle.value,
        articleSign: aricleIntroduction.value,
        articleContent: articleContent.value,
        savedAt: new Date().toLocaleString()
    }
    localStorage.setItem('articleDraft', JSON.stringify(draftData))
    ElMessage.success('草稿已保存')
}

async function submit(){
    const authorId=localStorage.getItem('authorId')
    const authorName=localStorage.getItem('authorName')

    if(!articleTitle.value.trim()){
        ElMessage.warning('请输入文章标题')
        return
    }
    if(!aricleIntroduction.value){
        ElMessage.warning('请选择文章标签')
        return
    }
    if(!articleContent.value.trim()){
        ElMessage.warning('请输入文章内容')
        return
    }

    if(isEdit.value){
        const res = await updateArticle({
            id: editArticleId.value,
            articleTitle: articleTitle.value,
            articleSign: aricleIntroduction.value,
            articleContent: articleContent.value,
            authorId: Number(authorId)
        })
        if(res.code == 1){
            localStorage.removeItem('articleDraft')
            try {
                await ElMessageBox.confirm('文章已成功更新！是否查看？', '更新成功', {
                    confirmButtonText: '查看',
                    cancelButtonText: '继续编辑',
                    type: 'success'
                })
                router.push({ name: 'ReadArticle', query: { id: editArticleId.value } })
            } catch {}
        } else {
            ElMessage.error('修改失败')
        }
    } else {
        const article=new Article(articleTitle.value,aricleIntroduction.value,articleContent.value,authorId,authorName)
        const res = await releaseArticle(article)
        if(res.code == 1){
            localStorage.removeItem('articleDraft')
            const newId = res.data
            try {
                await ElMessageBox.confirm('文章已成功发布！是否查看？', '发布成功', {
                    confirmButtonText: '查看',
                    cancelButtonText: '继续编辑',
                    type: 'success'
                })
                router.push({ name: 'ReadArticle', query: { id: newId } })
            } catch {}
        } else {
            ElMessage.error('发表失败')
        }
    }
}

</script>

<style scoped>
.release-page {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    background: #f5f5f5;
}

.release-container {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
}

.header-bar {
    width: 100%;
    height: 50px;
    background: #fff;
    display: flex;
    align-items: center;
    padding: 0 20px;
    box-sizing: border-box;
    border-bottom: 1px solid rgba(255, 182, 213, 0.3);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
    flex-shrink: 0;
    gap: 16px;
    position: relative;
    z-index: 10;
}

.title-input {
    width: 40%;
    height: 32px;
    font-size: 17px;
    color: #333;
    caret-color: #ff69b4;
    border: none;
    outline: none;
    padding: 0 8px;
    border-radius: 4px;
    background: #fafafa;
    transition: background 0.2s;
}

.title-input:focus {
    background: #fff;
    box-shadow: 0 0 0 2px rgba(255, 105, 180, 0.12);
}

.title-input::placeholder {
    color: #bbb;
}

.tag-area {
    display: flex;
    align-items: center;
    gap: 6px;
    flex-shrink: 0;
}

.tag-label {
    font-size: 14px;
    color: #666;
    white-space: nowrap;
}

.markdown-tutorial {
    position: absolute;
    right: 20px;
    font-size: 13px;
    color: #d63384;
    text-decoration: none;
    opacity: 0.7;
    transition: opacity 0.2s;
}

.markdown-tutorial:hover {
    opacity: 1;
}

.ai-tools {
    position: absolute;
    right: 130px;
    display: flex;
    align-items: center;
    gap: 8px;
}

.ai-btn {
    height: 30px;
    font-size: 12px;
    border-radius: 14px;
    padding: 0 12px;
    background: linear-gradient(135deg, rgba(99, 102, 241, 0.08), rgba(168, 85, 247, 0.08));
    border: 1px solid rgba(99, 102, 241, 0.25);
    color: #6366f1;
    transition: all 0.3s;
}

.ai-btn:hover {
    background: linear-gradient(135deg, rgba(99, 102, 241, 0.18), rgba(168, 85, 247, 0.18));
    border-color: rgba(99, 102, 241, 0.5);
    transform: translateY(-1px);
}

.editor-area {
    flex: 1;
    overflow: hidden;
    border: 1px solid #e0e0e0;
    border-top: none;
}

.editor-area :deep(.md-editor) {
    height: 100% !important;
    border: none !important;
}

.editor-area :deep(.md-editor-content) {
    height: 100% !important;
}

.editor-area :deep(.md-editor-preview-wrapper) {
    padding-bottom: 8%;
    box-sizing: border-box;
}

.editor-area :deep(.md-editor-preview) {
    padding-bottom: 5%;
    box-sizing: border-box;
}

.editor-area :deep(.CodeMirror-scroll),
.editor-area :deep(.cm-scroller),
.editor-area :deep(.cm-editor) {
    padding-bottom: 10% !important;
    box-sizing: border-box;
}

.floating-actions {
    position: fixed;
    right: 28px;
    bottom: 80px;
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 14px;
    z-index: 100;
}

.action-btn {
    min-width: 100px;
    height: 40px;
    border: none;
    border-radius: 22px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    letter-spacing: 0.5px;
    padding: 0 20px;
    white-space: nowrap;
}

.draft-btn {
    background: #fff !important;
    color: #d63384 !important;
    border: 1px solid rgba(255, 182, 213, 0.4) !important;
}

.draft-btn:hover {
    background: rgba(255, 105, 180, 0.08) !important;
    border-color: rgba(255, 105, 180, 0.5) !important;
    box-shadow: 0 2px 10px rgba(255, 105, 180, 0.15);
}

.publish-btn {
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.85), rgba(255, 160, 200, 0.85)) !important;
    color: #fff !important;
    border: none !important;
}

.publish-btn:hover {
    background: linear-gradient(135deg, rgba(255, 105, 180, 1), rgba(255, 130, 180, 1)) !important;
    box-shadow: 0 2px 12px rgba(255, 105, 180, 0.35);
}

.publish-btn:active,
.draft-btn:active {
    transform: scale(0.97);
}
</style>

