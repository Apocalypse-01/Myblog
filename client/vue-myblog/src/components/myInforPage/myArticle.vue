
<template>

<div style="width: 80%;height: 100%;
            background-color:rgba(255, 182, 213, 0.25);
            backdrop-filter: blur(8px);
            display: flex;align-items: baseline;position: relative;flex-direction: column;
            box-shadow:2px 2px 3px rgba(0,0,0,0.1);
            border: 2px solid rgba(255, 182, 213, 0.4);border-radius: 12px;
            overflow-y: auto;"   >
        
        <div style="flex-direction: column ;margin-top: 5%;margin-left: 5%;width: 90%;height: 100%;">

                <ul style="padding:0;">

                    <li class="article-card" v-for="article in articleList" :key="article.id">
                        <div style="display:flex;align-items:center;justify-content:space-between;">
                            <div style="flex:1;min-width:0;">
                                <div class="article-title" @click="articleSkip(article.id)">{{article.articleTitle}}</div>
                                <div style="display:flex;align-items:center;margin-top:6px;flex-wrap:wrap;">
                                    <span class="article-tag">{{ article.articleSign }}</span>
                                    <span class="article-meta">{{ article.authorName }}</span>
                                    <span class="article-meta">{{ article.createTime }}</span>
                                </div>
                            </div>
                            <div style="margin-left:16px;flex-shrink:0;">
                                <el-button size="small" type="primary" @click="editArticle(article)">编辑</el-button>
                                <el-button size="small" type="danger" @click="handleDeleteArticle(article.id)">删除</el-button>
                            </div>
                        </div>
                    </li>                                     
                    
                </ul>
                
                <div>
                    <el-button @click="articleListShow()" style="margin-left: 80%;">刷新</el-button>
                </div>


                <div style="display: flex; width: 100%;justify-content: center;margin-bottom: 30%;">

                    <el-pagination background 
                        layout="prev, pager, next" 
                        :current-page="currentPage"
                         @current-change="handleCurrentChange"
                        :page-size="10"
                        :total="totals"
                        style="margin-bottom: 20px;display: flex;"
                        >
                    </el-pagination>
                    
                </div>

        </div>

    </div>

</template>


<script setup name="MyArticle">
import router from '@/router/indext';
import { onMounted, ref } from 'vue';
import { getArticleListById, getArticle, deleteArticle } from '@/api/article.ts'

var articleList = ref([])
const currentPage=ref(1)
const totals=ref(10)

const handleCurrentChange = (val) => {
    currentPage.value=val
    articleListShow()
}

async function articleListShow(){
    const authorId=localStorage.getItem("authorId")
    const curentP=currentPage.value
    
    const list = await getArticleListById(authorId,curentP)

    articleList.value=list.data.articlePageDTOList
    totals.value=list.data.total
    currentPage.value=list.data.currentPage
}

async function articleSkip(id){
    router.push({
        name: 'ReadArticle',
        query: { id: id }
    })
}

function editArticle(article){
    localStorage.setItem('editArticleData', JSON.stringify({
        id: article.id,
        articleTitle: article.articleTitle,
        articleSign: article.articleSign,
        articleContent: article.articleContent
    }))
    router.push({
        name: 'Release',
        query: {
            id: article.id,
            isEdit: true
        }
    })
}

async function handleDeleteArticle(id){
    if(!confirm('确定要删除这篇文章吗？删除后不可恢复！')) return
    const authorId = localStorage.getItem('authorId')
    const res = await deleteArticle(id, Number(authorId))
    if(res.code == 1){
        alert('删除成功')
        articleListShow()
    } else {
        alert('删除失败')
    }
}

onMounted(()=>{
    articleListShow()
})




</script>

<style scoped>
.article-card{
    list-style-type:none;
    background: rgba(255, 255, 255, 0.55);
    backdrop-filter: blur(6px);
    border-radius: 10px;
    border: 1px solid rgba(255, 182, 213, 0.3);
    margin-top: 12px; 
    margin-bottom: 12px;
    padding: 14px 18px;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.1);
}
.article-card:hover{
    background: rgba(255, 255, 255, 0.75);
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.2);
    transform: translateY(-2px);
}
.article-title{
    font-size: 17px;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.article-title:hover{
    color: #ff69b4;
}
.article-tag{
    display: inline-block;
    background: rgba(255, 105, 180, 0.15);
    color: #d63384;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 10px;
    margin-right: 10px;
}
.article-meta{
    font-size: 12px;
    color: #888;
    margin-right: 10px;
}
</style>



















