
<template>
    
    <div class="read-article-layout">
            
            <!-- 作者详细信息 -->
            <div class="sidebar">

                <div style="width: 100%;height: 20%;display: flex;flex-direction: column">

                    <div style="margin-top: 15%;font-size:x-large;margin-left: 15%;margin-bottom: 15%;margin-right: 10%;color:#fff;text-shadow: 0 1px 3px rgba(0,0,0,0.2);">
                        作者:{{ article.authorName }}

                    </div>
                    
                    <hr style="border-color:rgba(255,255,255,0.3);">

                    <ul style="list-style-type:none;text-align: left;margin-top: 6%;margin-left: 15%;padding: 0;margin-right: 10%">
                            <div style="text-align: left;font-size: x-large;color:#fff;text-shadow: 0 1px 3px rgba(0,0,0,0.2);">作品列表</div>

                                <li v-for="item in authorArticles" :key="item.id" style=" margin-top:4%; border-radius:6px;  cursor:pointer;padding:4px 8px;" :class="String(item.id) === String(article.id) ? 'currentArticle' : ''" @click="skipToArticle(item)">
                                    <span :style="String(item.id) === String(article.id) ? 'color:#fff;font-weight:bold;' : 'color:rgba(255,255,255,0.85);'">{{ item.articleTitle }}</span>
                                </li>
                                <li v-if="authorArticles.length === 0" style="margin-top:4%;">
                                    <span style="color:rgba(255,255,255,0.5);font-size:small;">暂无其他作品</span>
                                </li>

                    </ul>

    
                </div>

        
            </div>
            
            <!-- 文章内容 和 评论-->
            <div class="content-area">

                <!-- 文章内容 -->
                <div style="width: 100% ;">

                    <div style="margin-left: 4%;margin-top: 2%;">
                        <h1 style="font-weight:600;color:#333;">{{ article.articleTitle }}</h1>
                    </div>
                    
                    <div style="margin-left: 4%;display:flex;align-items:center;flex-wrap:wrap;">  
                        <span style="color:#666;">作者:{{ article.authorName }}</span>
                        <span class="article-tag-inline">{{ article.articleSign }}</span>
                        <span style="font-size:small;color:#999;margin-left:20px;">{{ data }}</span>
                    </div>

                    <div style="width: 100%;margin-bottom: 10%;">

                        <MdPreview
                        :modelValue="article.articleContent"
                        style="margin-left: 50px;width: 90%;background-color:rgba(0,0,0,0);"
                        />

                    </div>
                </div>

                <!-- 评论区域   -->
                <div class="comment-section">
                    <!-- 评论输入框 -->
                    <div class="comment-input-area">
                        <div class="comment-input-row">
                            <div class="comment-avatar-small">
                                <span>{{ (currentUserName || '?').charAt(0) }}</span>
                            </div>
                            <textarea class="comment-textarea"
                                v-model="commentContent" rows="2" placeholder="说点什么吧..." 
                                @click="showSubmitBtn()">
                            </textarea>
                        </div>
                        <div class="comment-submit-wrap" v-if="showSubmit">
                            <button class="comment-submit-btn" @click="submitComment()">发表评论</button>
                        </div>
                    </div>

                    <!-- 评论列表 -->
                    <div class="comment-list-area">
                        <div class="comment-divider"></div>
                        <div v-if="commentList.length === 0" class="comment-empty">暂无评论，快来抢沙发~</div>
                        <ul class="comment-list">
                            <li class="comment-item" v-for="comment in commentList" :key="comment.id">
                                <div class="comment-item-header">
                                    <div class="comment-avatar-small">
                                        <span>{{ (comment.userName || '?').charAt(0) }}</span>
                                    </div>
                                    <div class="comment-meta">
                                        <span class="comment-username">{{ comment.userName }}</span>
                                        <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                                    </div>
                                    <span v-if="comment.userId == currentUserId" class="comment-delete" @click="handleDeleteComment(comment.id)">删除</span>
                                </div>
                                <p class="comment-body">{{ comment.content }}</p>
                                <div class="comment-divider-inner"></div>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- 底部导航栏 --> 
                <div  class="bottom">

                    <div class="bottom-author">
                        <span style="margin-left: 10px;cursor: pointer;">作者:{{ article.authorName  }} </span>
                        <!-- <span style="cursor: pointer;margin-left: 20px;margin-right: 20px;">关注</span> -->

                    </div>
                        
                    <ul class="bottom-actions">
                        
                        <li class="action-item like-item" @click="getLikes" :class="likeShow ? 'laterLikeClass' : 'newLikeClass'" >
                                <img src="/static/icon/like_icon.png" alt="点赞" class="img-icon">
                                <span class="action-text">点赞</span>
                                <span class="action-count">{{like}}</span>
                        </li>

                        <li  class="action-item favorite-item" @click="getFavorites" :class="favotiteShow ? 'laterFavoriteClass' : 'newFavoriteClass'" >
                                <img src="/static/icon/star_icon.png" alt="收藏" class="img-icon">
                                <span class="action-text">收藏</span>
                                <span class="action-count">{{favorite}}</span>
                        </li>

                        <li  class="action-item download-item download-btn-class" @click="showDownloadDialog">
                                <img src="/static/icon/download_icon.png" alt="" class="img-icon">
                                <span class="action-text">下载</span>
                        </li>
                            
                    </ul>
           
                </div>


            </div>

    </div>

    <!-- 下载格式选择对话框 -->
    <el-dialog v-model="downloadDialogVisible" title="选择下载格式" width="420px" :close-on-click-modal="false" class="download-dialog">
        <div class="download-format-options">
            <div class="format-option" @click="selectFormat('markdown')">
                <div class="format-icon">📝</div>
                <div class="format-info">
                    <div class="format-name">Markdown</div>
                    <div class="format-desc">.md 格式，保留原始排版</div>
                </div>
            </div>
            <div class="format-option" @click="selectFormat('txt')">
                <div class="format-icon">📄</div>
                <div class="format-info">
                    <div class="format-name">纯文本</div>
                    <div class="format-desc">.txt 格式，简洁通用</div>
                </div>
            </div>
            <div class="format-option" @click="selectFormat('epub')">
                <div class="format-icon">📚</div>
                <div class="format-info">
                    <div class="format-name">EPUB</div>
                    <div class="format-desc">.epub 格式，电子书阅读器兼容</div>
                </div>
            </div>
        </div>
    </el-dialog>

    <!-- 下载进度对话框 -->
    <el-dialog v-model="progressDialogVisible" title="正在下载" width="400px" :close-on-click-modal="false" :show-close="downloading" class="progress-dialog">
        <div class="download-progress-content">
            <el-progress :percentage="downloadProgress" :status="downloadStatus" :stroke-width="18" />
            <div class="progress-text">{{ progressText }}</div>
            <div class="progress-actions">
                <el-button v-if="downloading" type="danger" size="small" @click="cancelDownload">取消下载</el-button>
                <el-button v-if="!downloading && downloadSuccess" type="primary" size="small" @click="closeProgressDialog">完成</el-button>
                <el-button v-if="!downloading && !downloadSuccess" type="warning" size="small" @click="retryDownload">重试</el-button>
            </div>
        </div>
    </el-dialog>


</template>


<script setup lang="ts" name="ReadArticle">
import {useRoute} from 'vue-router'
import {onMounted, ref, watch} from 'vue'
import {articleYesLike,articleNoLike,articleYesFavorites,articleNoFavorites,isLiked,isFavorited,getArticle,getArticleListById, getAllByAuthor} from '@/api/article.ts'
import {addComment, getCommentList, deleteComment, getCommentCount} from '@/api/comment.ts'
import router from '@/router/indext'
import 'md-editor-v3/lib/style.css';
import { MdPreview} from 'md-editor-v3'
import { ElMessage } from 'element-plus'
import likeIcon from './static/诶嘿.png';

const articleRoute = useRoute()

const article = ref<any>({})
const data=ref('')

const likeShow=ref(false)
const like=ref(0)
const favotiteShow=ref(false)
const favorite=ref(0)

const commentContent=ref('')
const showSubmit=ref(false)
const commentList=ref<any>([])
const commentCount=ref(0)
const currentUserId=ref<string|null>(localStorage.getItem('authorId'))
const currentUserName=ref<string|null>(localStorage.getItem('authorName'))
const authorArticles=ref<any[]>([])

const downloadDialogVisible = ref(false)
const progressDialogVisible = ref(false)
const downloadProgress = ref(0)
const progressText = ref('')
const downloading = ref(false)
const downloadSuccess = ref(false)
const selectedFormat = ref('')
let abortController: AbortController | null = null

function formatTime(timeStr: string) {
    if (!timeStr) return ''
    return timeStr.substr(0, 10)
}

async function loadArticleData(articleId: any) {
    if (!articleId) return
    try {
        const res = await getArticle(Number(articleId))
        if (res.code == 1) {
            article.value = res.data
            data.value = res.data.createTime ? String(res.data.createTime).substr(0, 10) : ''
            like.value = Number(res.data.likes) || 0
            favorite.value = Number(res.data.favorites) || 0
            likeShow.value = false
            favotiteShow.value = false
            loadComments(res.data.id)
            loadAuthorArticles(res.data.authorId)
            checkLikeStatus(res.data.id)
        }
    } catch(e) {}
}

async function loadComments(articleId: any) {
    if (!articleId) return
    const res = await getCommentList(articleId)
    if (res.code == 1) {
        commentList.value = res.data
    }
    const countRes = await getCommentCount(articleId)
    if (countRes.code == 1) {
        commentCount.value = countRes.data
    }
}

function showSubmitBtn() {
    const isLogin = localStorage.getItem("isLogin")
    if (isLogin === 'true') {
        showSubmit.value = true
    } else {
        alert('请先登录后再发表评论！')
    }
}

async function submitComment() {
    const content = commentContent.value.trim()
    if (!content) {
        alert('评论内容不能为空！')
        return
    }

    const res = await addComment({
        articleId: Number(article.value.id),
        userId: Number(currentUserId.value || 0),
        userName: currentUserName.value || '',
        content: content
    })

    if (res.code == 1) {
        commentContent.value = ''
        showSubmit.value = false
        await loadComments(article.value.id)
    } else {
        alert('评论发表失败')
    }
}

async function handleDeleteComment(id: number) {
    if (!confirm('确定要删除这条评论吗？')) return
    const res = await deleteComment(id, Number(currentUserId.value || 0))
    if (res.code == 1) {
        await loadComments(article.value.id)
    } else {
        alert('删除失败')
    }
}

async function getLikes(){
    const isLogin = localStorage.getItem("isLogin")
    if (isLogin !== 'true') {
        alert('请先登录后再点赞！')
        return
    }

    likeShow.value=!likeShow.value

    if(likeShow.value==true){
        const temp = await articleYesLike(article.value.id, Number(currentUserId.value || 0), currentUserName.value || '')
        
        if(temp.code!=1){
            likeShow.value=false
            alert(temp.msg || '点赞失败')
            return
        }

        if(temp.code==1){
            like.value=temp.data
        }
    }

    if(likeShow.value==false){
        const temp = await articleNoLike(article.value.id, Number(currentUserId.value || 0), currentUserName.value || '')
        if(temp.code!=1 || temp.data==-1){
            likeShow.value=true
            alert('尚未点赞，无法取消')
            return
        }
        like.value=temp.data 
    }
}

async function getFavorites(){
    const isLogin = localStorage.getItem("isLogin")
    if (isLogin !== 'true') {
        alert('请先登录后再收藏！')
        return
    }

    favotiteShow.value=!favotiteShow.value

    if(favotiteShow.value==true){
        const temp = await articleYesFavorites(article.value.id, Number(currentUserId.value || 0), currentUserName.value || '')
        
        if(temp.code!=1){
            favotiteShow.value=false
            alert(temp.msg || '收藏失败')
            return
        }

        if(temp.code==1){
            favorite.value=temp.data
        }
    }

    if(favotiteShow.value==false){
        const temp_2 = await articleNoFavorites(article.value.id, Number(currentUserId.value || 0), currentUserName.value || '')
        if(temp_2.code!=1 || temp_2.data==-1){
            favotiteShow.value=true
            alert('尚未收藏，无法取消')
            return
        }
        favorite.value=temp_2.data 
    }
}

async function loadAuthorArticles(authorId: any) {
    if (!authorId) return
    try {
        const res = await getAllByAuthor(authorId)
        if (res.code == 1) {
            authorArticles.value = res.data || []
        }
    } catch(e) {}
}

async function checkLikeStatus(articleId: any) {
    if (!currentUserId.value || !articleId) return
    try {
        const likeRes = await isLiked(Number(articleId), Number(currentUserId.value))
        if (likeRes.code == 1) {
            likeShow.value = likeRes.data
        }
        const favoriteRes = await isFavorited(Number(articleId), Number(currentUserId.value))
        if (favoriteRes.code == 1) {
            favotiteShow.value = favoriteRes.data
        }
    } catch(e) {}
}

async function skipToArticle(item: any) {
    router.push({ name: 'ReadArticle', query: { id: item.id } })
}

function showDownloadDialog() {
    downloadDialogVisible.value = true
}

function selectFormat(format: string) {
    selectedFormat.value = format
    downloadDialogVisible.value = false
    startDownload(format)
}

async function startDownload(format: string) {
    progressDialogVisible.value = true
    downloading.value = true
    downloadSuccess.value = false
    downloadProgress.value = 0
    progressText.value = '正在准备下载...'
    
    abortController = new AbortController()
    
    try {
        const token = localStorage.getItem('token')
        const articleId = article.value.id
        
        progressText.value = '正在连接服务器...'
        downloadProgress.value = 20
        
        const response = await fetch(`http://localhost:8081/article/download?articleId=${articleId}&format=${format}`, {
            method: 'GET',
            signal: abortController.signal
        })
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        progressText.value = '正在接收文件数据...'
        downloadProgress.value = 60
        
        const blob = await response.blob()
        
        progressText.value = '正在生成文件...'
        downloadProgress.value = 80
        
        const contentDisposition = response.headers.get('Content-Disposition')
        let filename = `${article.value.articleTitle || '未命名文章'}_${new Date().toISOString().split('T')[0]}`
        
        const extMap: Record<string, string> = {
            'markdown': '.md',
            'txt': '.txt',
            'epub': '.epub'
        }
        filename += extMap[format] || '.txt'
        
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = filename
        document.body.appendChild(a)
        a.click()
        window.URL.revokeObjectURL(url)
        document.body.removeChild(a)
        
        downloadProgress.value = 100
        progressText.value = `✓ 下载完成！文件：${filename}（${formatFileSize(blob.size)}）`
        downloading.value = false
        downloadSuccess.value = true
        
        ElMessage.success('文件下载成功！')
        
    } catch (error: any) {
        if (error.name === 'AbortError') {
            progressText.value = '下载已取消'
        } else {
            console.error('Download error:', error)
            progressText.value = `✗ 下载失败：${error.message || '网络异常，请检查网络连接'}`
            downloading.value = false
            downloadSuccess.value = false
            ElMessage.error('下载失败，请重试')
        }
    }
}

function cancelDownload() {
    if (abortController) {
        abortController.abort()
        downloading.value = false
        progressText.value = '正在取消下载...'
    }
}

function retryDownload() {
    if (selectedFormat.value) {
        startDownload(selectedFormat.value)
    }
}

function closeProgressDialog() {
    progressDialogVisible.value = false
    downloadProgress.value = 0
    progressText.value = ''
}

function formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const downloadStatus = ref<'' | 'success' | 'exception'>('')

watch(() => articleRoute.query.id, (newId) => {
    if (newId) {
        loadArticleData(newId)
    }
})

onMounted(()=>{
    const articleId = articleRoute.query.id
    if (articleId) {
        loadArticleData(articleId)
    }
})

</script>


<style scoped>

.read-article-layout{
    display: flex;
    flex-direction: row;
    width: 80vw;
    height: calc(100vh - 60px);
    backdrop-filter: blur(10px);
}
.sidebar{
    width: 20%;
    overflow-y: auto;
    background-color:rgba(255, 182, 213, 0.35);
    backdrop-filter: blur(8px);
    border: 2px solid rgba(255, 182, 213, 0.4);border-radius: 10px;
}
.sidebar::-webkit-scrollbar{ width: 4px; }
.sidebar::-webkit-scrollbar-thumb{ background: rgba(255,105,180,0.3); border-radius: 2px; }
.sidebar::-webkit-scrollbar-track{ background: transparent; }
.content-area{
    display: flex;
    flex-direction: column;
    background-color:rgba(255, 255, 255, 0.75);
    backdrop-filter: blur(8px);
    border: 2px solid rgba(255, 182, 213, 0.3);border-radius: 10px;
    width: 80%;
    overflow-y: auto;
}
.content-area::-webkit-scrollbar{ width: 4px; }
.content-area::-webkit-scrollbar-thumb{ background: rgba(255,105,180,0.3); border-radius: 2px; }
.content-area::-webkit-scrollbar-track{ background: transparent; }
.currentArticle{
    background-color: rgba(255, 105, 180, 0.3);
    padding: 4px 8px;
}
.article-tag-inline{
    display: inline-block;
    background: rgba(255, 105, 180, 0.15);
    color: #d63384;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 10px;
    margin-left: 15px;
}
.comment-submit-btn{
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.75), rgba(255, 150, 200, 0.75));
    border: none;
    color: #fff;
    cursor: pointer;
    padding: 8px 24px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.25);
}
.comment-submit-btn:hover{
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.9), rgba(255, 130, 190, 0.9));
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.4);
    transform: translateY(-1px);
}
.comment-submit-btn:active{
    transform: translateY(0);
    box-shadow: 0 1px 4px rgba(255, 105, 180, 0.25);
}
.laterLikeClass{
    background-color: rgba(255, 105, 180, 0.7);
    border-radius: 4px;
}
.newLikeClass{
    background-color: rgba(255, 255, 255, 0.6);
    border-radius: 4px;
}

.laterFavoriteClass{
    background-color: rgba(255, 193, 7, 0.7);
    border-radius: 4px;
}
.newFavoriteClass{
    background-color: rgba(255, 255, 255, 0.6);
    border-radius: 4px;
}
.download-btn-class{
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.65), rgba(255, 150, 200, 0.75));
    border-radius: 4px;
    color: #fff;
    font-weight: 500;
    transition: all 0.3s ease;
    box-shadow: 0 2px 6px rgba(255, 105, 180, 0.2);
    letter-spacing: 0.5px;
}
.download-btn-class:hover{
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.85), rgba(255, 130, 190, 0.95));
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(255, 105, 180, 0.35);
}
.download-btn-class:active{
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.25);
}

.bottom-actions{
    display: flex;
    height: 100%;
    align-items: center;
    gap: 16px;
    margin-right: 20px;
}
.action-item{
    list-style: none;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    padding: 8px 16px;
    cursor: pointer;
    border-radius: 4px;
    min-width: fit-content;
    transition: all 0.3s ease;
}
.action-icon{
    font-size: 16px;
    line-height: 1;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}
.action-text{
    font-size: 14px;
    font-weight: 500;
    white-space: nowrap;
}
.action-count{
    font-size: 13px;
    font-weight: 600;
    color: inherit;
    min-width: 20px;
    text-align: center;
}
.img-icon{
    width: 20px;
    height: 20px;
    object-fit: contain;
    display: inline-block;
    vertical-align: middle;
    transition: transform 0.2s ease;
}
.bottom{
    width: 100%;
    height: 45px;
    display: flex;
    align-items: center;
    flex-direction: row;
    justify-content: space-between;
    background-color:rgba(255, 182, 213, 0.45);
    backdrop-filter: blur(8px);
    border-top: 1px solid rgba(255, 182, 213, 0.3);
    position: sticky;
    bottom: 0;
    flex-shrink: 0;
    z-index: 10;
}
.bottom-author{
    background-color:rgba(255, 182, 213, 0.3);height: 100%;display: flex;align-items: center;justify-content: space-around;
    border-radius: 8px;padding: 0 16px;
}

/* ============ 评论模块样式 ============ */
.comment-section{
    width: 90%;
    margin: 0 auto 10%;
    display: flex;
    flex-direction: column;
}

/* -- 评论输入区域 -- */
.comment-input-area{
    margin-bottom: 16px;
}
.comment-input-row{
    display: flex;
    align-items: flex-start;
    gap: 12px;
}
.comment-avatar-small{
    width: 36px;
    height: 36px;
    min-width: 36px;
    border-radius: 50%;
    overflow: hidden;
    border: 2px solid rgba(255, 182, 213, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, rgba(255,105,180,0.5), rgba(255,182,213,0.6));
    color: #fff;
    font-size: 14px;
    font-weight: bold;
    flex-shrink: 0;
}
.comment-textarea{
    flex: 1;
    border-radius: 12px;
    padding: 12px 16px;
    resize: none;
    border: 1px solid rgba(255,182,213,0.4);
    background: rgba(255,255,255,0.7);
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    outline: none;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.06);
    font-family: inherit;
}
.comment-textarea::placeholder{
    color: #aaa;
    font-style: italic;
}
.comment-textarea:focus{
    border-color: rgba(255, 105, 180, 0.5);
    box-shadow: 0 0 0 3px rgba(255, 105, 180, 0.1), 0 4px 16px rgba(255, 105, 180, 0.12);
    background: rgba(255,255,255,0.9);
}
.comment-submit-wrap{
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
}

/* -- 评论列表 -- */
.comment-list-area{
    margin-top: 8px;
}
.comment-divider{
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba(255,182,213,0.4), transparent);
    margin-bottom: 16px;
}
.comment-empty{
    text-align: center;
    padding: 32px 0;
    color: #aaa;
    font-size: 14px;
}
.comment-list{
    list-style: none;
    padding: 0;
    margin: 0;
}
.comment-item{
    padding: 4px 0;
}
.comment-item-header{
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 8px;
}
.comment-item-header .comment-avatar-small{
    width: 32px;
    height: 32px;
    min-width: 32px;
    font-size: 13px;
}
.comment-meta{
    display: flex;
    flex-direction: column;
    gap: 2px;
    flex: 1;
    min-width: 0;
}
.comment-username{
    font-weight: 600;
    color: #d63384;
    font-size: 14px;
    line-height: 1.3;
}
.comment-time{
    font-size: 12px;
    color: #999;
    line-height: 1.3;
}
.comment-delete{
    font-size: 12px;
    color: #ff69b4;
    cursor: pointer;
    opacity: 0;
    transition: all 0.2s ease;
    padding: 4px 8px;
    border-radius: 4px;
    flex-shrink: 0;
}
.comment-item:hover .comment-delete{
    opacity: 1;
}
.comment-delete:hover{
    background: rgba(255, 105, 180, 0.12);
    color: #e64980;
}
.comment-body{
    font-size: 15px;
    line-height: 1.7;
    color: #444;
    margin: 0 0 6px 42px;
    word-break: break-word;
    white-space: pre-wrap;
}
.comment-divider-inner{
    height: 1px;
    background: rgba(255,182,213,0.15);
    margin: 10px 0 10px 42px;
}
.comment-item:last-child .comment-divider-inner{
    display: none;
}
.comment-item:hover{
    background: rgba(255, 182, 213, 0.04);
    border-radius: 8px;
}

/* ============ 下载模块样式 ============ */
.download-format-options{
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 8px 0;
}
.format-option{
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px 20px;
    border: 2px solid rgba(255, 182, 213, 0.3);
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.3s ease;
    background: rgba(255, 255, 255, 0.6);
}
.format-option:hover{
    border-color: rgba(52, 152, 219, 0.6);
    background: rgba(52, 152, 219, 0.08);
    transform: translateX(4px);
    box-shadow: 0 4px 12px rgba(52, 152, 219, 0.15);
}
.format-icon{
    font-size: 28px;
    width: 48px;
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, rgba(255, 182, 213, 0.2), rgba(255, 105, 180, 0.2));
    border-radius: 10px;
}
.format-info{
    display: flex;
    flex-direction: column;
    gap: 4px;
}
.format-name{
    font-size: 16px;
    font-weight: 600;
    color: #333;
}
.format-desc{
    font-size: 13px;
    color: #888;
}
.download-progress-content{
    display: flex;
    flex-direction: column;
    gap: 20px;
    padding: 10px 0;
}
.progress-text{
    text-align: center;
    color: #666;
    font-size: 14px;
    line-height: 1.5;
    min-height: 42px;
}
.progress-actions{
    display: flex;
    justify-content: center;
    gap: 12px;
}

/* ============ 响应式 ============ */
@media (max-width: 768px) {
    .comment-section{
        width: 96%;
    }
    .comment-input-row{
        gap: 8px;
    }
    .comment-body{
        margin-left: 32px;
        font-size: 14px;
    }
    .comment-divider-inner{
        margin-left: 32px;
    }
    .comment-avatar-small{
        width: 30px;
        height: 30px;
        min-width: 30px;
        font-size: 12px;
    }
    .comment-item-header .comment-avatar-small{
        width: 28px;
        height: 28px;
        min-width: 28px;
        font-size: 11px;
    }
    .bottom-actions{
        gap: 10px;
        margin-right: 10px;
    }
    .action-item{
        padding: 6px 10px;
        gap: 4px;
    }
    .action-icon{
        font-size: 14px;
    }
    .action-text{
        font-size: 12px;
    }
    .action-count{
        font-size: 11px;
        min-width: 16px;
    }
    .like-icon{
        width: 18px;
        height: 18px;
    }
}
.like-item:hover .like-icon{
    transform: scale(1.15);
}</style>









