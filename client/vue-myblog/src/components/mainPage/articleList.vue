<template>
            <div class="main">
                <div class="center">
                    <div style="text-align: center;
                                padding: 10px;width: 20%;margin-left: 10% ;margin-right:0.2% ;
                                border: 2px solid rgba(255, 182, 213, 0.4);border-radius: 10px;
                                border-top: none
                                ">
                        
                        <div style="background-color:rgba(255, 182, 213, 0.35);
                                    height:12%;
                                    border-radius: 2%;
                                    backdrop-filter: blur(8px);
                                    ">
                            <img src=" "/>
                            <h2 style="color:#fff;text-shadow: 0 1px 3px rgba(0,0,0,0.2);">分类标签</h2>
                                    
                        </div>

                        <div class="li-01">
                            <nav style="padding: 0; margin-top:0.2%;display: flex;flex-direction: column;" >
                                <input type="radio" name="nav" id="all" checked />
                                <label for="all" @click="changeSign(4)">全部</label>
                            
                                <input type="radio" name="nav" id="day"  />
                                <label for="day" @click="changeSign(0)">日常</label>
                                
                                <input type="radio" name="nav" id="skills" />
                                <label for="skills" @click="changeSign(1)">技术</label>
                                
                                <input type="radio" name="nav" id="talks" />
                                <label for="talks" @click="changeSign(2)">杂谈</label>
                                
                                <input type="radio" name="nav" id="else"  />
                                <label for="else" @click="changeSign(3)" style=" cursor: pointer;">其他</label>

                                </nav>
                        </div>

                    </div>

                    
                    <div style="width: 100%;height: 100vh;  "   >

                        <div style="width: 80%;height: 100vh; 
                                    overflow-y: auto;
                                    "  >
                            <div class="search-area">
                                <div class="search-box">
                                    <el-input placeholder="搜索文章" v-model="searchKeyword" @keyup.enter="doSearch" class="search-input" />
                                    <el-button @click="doSearch" class="search-btn">搜索</el-button>
                                </div>
                                <el-button v-if="isSearchMode" @click="clearSearch" class="clear-btn">清除搜索</el-button>
                            </div>

                            <div style="flex-direction: column ;margin-top: 3%;margin-left: 5%;width: 80%; 
                                        ">
                                <!-- 根据点赞/收藏进行排序  实现 -->
<!--                             
                                <nav style="padding: 0; margin: 10px;display: flex;" >
                                    
                                    <input type="radio" name="order" id="favorite" checked />
                                    <label for="favorite" @click="changeOrder(0)" style="width: 60px;">收藏</label>
                                    
                                    <input type="radio" name="order" id="likes" />
                                    <label for="likes" @click="changeOrder(1)"  style="width: 60px;">点赞</label>
                                
                                </nav> -->

                                <ul style="padding:0;">
                                    <li class="li-02" v-for="article in articleList" :key="article.id">
                                        <div class="article-title" @click="articleSkip(article.id)">{{article.articleTitle}}</div>
                                        <div style="display:flex;align-items:center;margin-top:6px;flex-wrap:wrap;">
                                            <span class="article-tag">{{ article.articleSign }}</span>
                                            <span class="article-meta">{{ article.authorName }}</span>
                                            <span class="article-meta">{{ article.createTime }}</span>
                                        </div>
                                    </li>                                    
                                </ul>
                                
                                <div>
                                    <el-button @click="articleListShow()" style="margin-left: 80%;">刷新</el-button>
                                </div>


                                <div style="display: flex; width: 100%;justify-content: center;margin-bottom: 30%;">
                                    <!-- 分页展示 -->
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


                    </div>
                    
                </div>
                
           </div>

</template>


<script setup name="ArticleList">
import router from '@/router/indext';
import { onMounted,ref } from 'vue'
import { getArticleListBySign ,searchArticle} from '@/api/article.ts'

let signId=ref(4)
let signOrder=ref(0)
let searchKeyword=ref('')
let isSearchMode=ref(false)

var articleList = ref([])
const currentPage=ref(1)
const totals=ref(10)

const handleCurrentChange = async (val) => {
    currentPage.value=val
    if(isSearchMode.value){
        const list = await searchArticle(searchKeyword.value.trim(), currentPage.value)
        articleList.value=list.data.articlePageDTOList
        totals.value=list.data.total
        currentPage.value=list.data.currentPage
    }else{
        articleListShow()
    }
}


//利用钩子函数，在组件创建完成后，加载列表界面
//数据的刷新   
async function articleListShow(){

        console.log(signId.value,signOrder.value)
        const curentP=currentPage.value
        // //标签，排序依赖，当前页数      获得: 所有列表 , 总条数 , 当前页码 
        const list = await getArticleListBySign(signId.value,signOrder.value,curentP) //逻辑有问题signOrder是排序排序方式，不是总页数

        //赋值  totle   currentPage   articleList 
        articleList.value=list.data.articlePageDTOList
        totals.value=list.data.total
        currentPage.value=list.data.currentPage



}

// 类别，文章标签
function changeSign(si){
    signId.value=si
    signOrder.value=0
    isSearchMode.value=false
    searchKeyword.value=''
    console.log(signId.value,signOrder.value)
    articleListShow()
}

async function doSearch(){
    const keyword = searchKeyword.value.trim()
    if(!keyword){
        return
    }
    isSearchMode.value=true
    currentPage.value=1
    const list = await searchArticle(keyword, currentPage.value)
    articleList.value=list.data.articlePageDTOList
    totals.value=list.data.total
    currentPage.value=list.data.currentPage
}

function clearSearch(){
    isSearchMode.value=false
    searchKeyword.value=''
    currentPage.value=1
    articleListShow()
}

// 排序方式
// function changeOrder(so){

//     signOrder.value=so
//     console.log(signId.value,signOrder.value)
//     articleListShow()

// } 

// 文章跳转
async function articleSkip(id){
    router.push({
        name: 'ReadArticle',
        query: { id: id }
    })
}

// 页面初始加载数据
onMounted(()=>{
    
    articleListShow()

})

</script>

<style scoped>
.main{ 
    width: 100%;
    height: 600px;
    display: flex;
    flex-direction: column;
    height: 100vh;overflow-y: auto;
    background-color: rgba(0, 0, 0, 0);
    /* backdrop-filter: blur(20px); */
}

.center{
    display: flex;
    width: 100%;
    height: 100%;
    /* background-color:rgba(255, 182, 213, 0.15); */
}
.li-01{
    display: flex;
    padding: 10px;
    flex-direction: column;
    cursor: pointer;
    background-color:rgba(255, 182, 213, 0.25);
}

.li-02{
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
.li-02:hover{
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

/* 选中时的 label 样式 */
nav input[type="radio"]:checked + label {
  background: rgba(255, 105, 180, 0.6);
  color: #fff;
  border: 2px solid rgba(255, 182, 213, 0.5);
  border-radius: 4px;
}

label{
    display: flex;
    justify-content: center;
    flex-direction: column;
    height: 40px;
    align-items: center;
    cursor: pointer;
}

/* 移除 a 标签的默认样式 */
nav input {
  opacity: 0;
  height: 0px;
  text-decoration: none;
  color: inherit;
}

span{
    cursor: pointer;
}

.search-area{
    width: 80%;
    margin-top: 5%;
    margin-left: 5%;
    display: flex;
    align-items: center;
    gap: 12px;
}
.search-box{
    display: flex;
    align-items: center;
    gap: 0;
    width:100%;
    max-width: 700px;
}
.search-input{
    flex: 1;
}
.search-input :deep(.el-input__wrapper){
    background: rgba(255, 255, 255, 0.55);
    backdrop-filter: blur(6px);
    border: 1px solid rgba(255, 182, 213, 0.4);
    border-radius: 10px 0 0 10px;
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.1);
    transition: all 0.3s ease;
}
.search-input :deep(.el-input__wrapper:hover){
    border-color: rgba(255, 105, 180, 0.5);
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.15);
}
.search-input :deep(.el-input__wrapper.is-focus){
    border-color: rgba(255, 105, 180, 0.6);
    box-shadow: 0 0 0 1px rgba(255, 105, 180, 0.3), 0 4px 16px rgba(255, 105, 180, 0.15);
}
.search-input :deep(.el-input__inner){
    color: #333;
    font-size: 14px;
}
.search-input :deep(.el-input__inner::placeholder){
    color: #aaa;
    font-style: italic;
}
.search-btn{
    flex-shrink: 0;
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.7), rgba(255, 182, 213, 0.7)) !important;
    border: none !important;
    color: #fff !important;
    border-radius: 0 10px 10px 0 !important;
    font-size: 14px !important;
    padding: 0 20px !important;
    height: 32px !important;
    transition: all 0.3s ease !important;
}
.search-btn:hover{
    background: linear-gradient(135deg, rgba(255, 105, 180, 0.85), rgba(255, 182, 213, 0.85)) !important;
}
.clear-btn{
    background: rgba(255, 255, 255, 0.55) !important;
    backdrop-filter: blur(6px) !important;
    border: 1px solid rgba(255, 182, 213, 0.4) !important;
    color: #d63384 !important;
    border-radius: 10px !important;
    font-size: 13px !important;
    padding: 8px 16px !important;
    transition: all 0.3s ease !important;
}
.clear-btn:hover{
    background: rgba(255, 255, 255, 0.75) !important;
    border-color: rgba(255, 105, 180, 0.6) !important;
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.15) !important;
}

</style>

