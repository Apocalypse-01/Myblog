

<template>

    <div style="width: 80%;height: 100%;
                background-color:rgba(255, 182, 213, 0.25);
                backdrop-filter: blur(8px);
                display: flex;align-items: baseline;position: relative;flex-direction: column;
                box-shadow:2px 2px 3px rgba(0,0,0,0.1);
                border: 2px solid rgba(255, 182, 213, 0.4);border-radius: 12px;
                "   >
                        
            <div style="flex-direction: column ;margin-top: 5%;margin-left: 5%;width: 90%;">

                    <ul style="padding:0;">
                        <li class="article-card" v-for="article in articleList" :key="article.id" >
                            <div class="article-title" @click="articleSkip(article.id)">{{article.articleTitle}}</div>
                            <div style="display:flex;align-items:center;margin-top:6px;flex-wrap:wrap;">
                                <span class="article-tag">{{ article.articleSign }}</span>
                                <span class="article-meta">{{ article.authorName }}</span>
                                <span class="article-meta">{{ article.createTime }}</span>
                            </div>
                        </li>   

                    </ul>


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
    
    
    
<script setup name="Mylike">
import router from '@/router/indext';
import { onMounted,ref } from 'vue'
import { getArticleFavirotes } from '@/api/article.ts'

    
var articleList = ref([])
const currentPage=ref(1)
const totals=ref(10)

const handleCurrentChange = (val) => {
    currentPage.value=val
    console.log("currentPage:",currentPage.value)

    // 查询
    articleListShow()

}

async function articleListShow(){

        //当前用户id
        const authorId=localStorage.getItem("authorId")
        //当前页数
        const curentP=currentPage.value
        //当前页数      获得: 所有列表 , 总条数 , 当前页码 
        const list = await getArticleFavirotes(authorId,curentP)

        // console.log(list.data)

        //赋值  totle   currentPage   articleList 
        articleList.value=list.data.articlePageDTOList
        totals.value=list.data.total
        currentPage.value=list.data.currentPage

        console.log(articleList.value)
        console.log(totals.value)
        console.log(currentPage.value)
}


// 文章跳转
async function articleSkip(id){
    router.push({
        name: 'ReadArticle',
        query: { id: id }
    })
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    