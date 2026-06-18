
<template>
    <div style="
             background: url('/static/ARTI_01_卡哇伊_右.png');
            position:fixed;
            background-repeat: no-repeat;
            background-size: cover;
            
            top: 0;left: 0;"    >

        <div style="display: flex; height: 100vh;flex-direction: column;">
            <div class="top"> 
                <div style="margin-left: 5%; height: 40px;display: flex;width: 20%;color: #fff;
                            border: 2px solid rgba(255, 182, 213, 0.5);padding: 4px 12px; border-radius: 4px;border-right: none;border-bottom: none;border-bottom: none;
                            
                            ">
                    
                    <div style="width: 80%;display: flex;height: 100%;align-items: center">
                        <img style="height: 100%;margin-right: 2%; box-shadow: 0 0 10px rgba(255,182,213,0.6);" src="/static\诶嘿.png" alt="">
                        <h4 style="text-shadow: 0 1px 3px rgba(0,0,0,0.2);"> 智能笔记</h4>
                    </div>
                    
                    <div style="width: 30%;text-align: center;height: 100%;
                                display: flex;align-items: center">

                        <h4  style="cursor:pointer;
                                border: 2px solid rgba(255,255,255,0.5);
                                box-shadow: 0 2px 8px rgba(255, 105, 180, 0.3);
                                padding: 4px 12px; 
                                border-radius: 4px; 
                                border-bottom: none;border-top: none;" @click="comeBackCompany">
                                
                                回退

                        </h4>
                        
                    </div>
                </div>
                
                <div class="nav-links"
                            style="padding-right: 4%;  height: 100%;display: flex;align-items: center;padding-left: 1%;
                            border: 2px solid rgba(255, 182, 213, 0.5);
                            border-radius: 4px;
                            border-right: none;border-bottom: none;border-top: none;
                            ">                    
                    <RouterLink to="/"                  active-class="active">  首页</RouterLink>
                    <RouterLink to="/main/articleList"  active-class="active">  浏览</RouterLink>
                    <RouterLink v-if="isLogin" to="/main/knowledgeGraph" active-class="active"> 图谱 </RouterLink>
                    <RouterLink v-if="isLogin" to="/main/release"        active-class="active">  创作 </RouterLink>
                    <RouterLink v-if="isLogin" to="/main/infor/dashboard"active-class="active">  我的 </RouterLink>
                    <RouterLink v-else="isLogin" to="/main/login"        active-class="active">登陆 </RouterLink>                   
                    <a  href="/" @click="eixt()" v-if="isLogin"> 退出 </a>
                    

                </div>
                
            </div>
            <!-- 主体展示内容 -->
            
            <div style="display: flex;justify-content: center; backdrop-filter: blur(10px); ">
                <router-view/> 
            </div>

        </div>
    </div>

</template>


<script setup name="Main">
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useGlobalStore } from '@/stores/global'
import { storeToRefs } from 'pinia'

const globalStore = useGlobalStore()
// 解构 getters 中的 isLogin，保持响应式
const { isLogin } = storeToRefs(globalStore)

const router = useRouter();

function eixt(){
    console.info("退出："+globalStore.isLogin)
    localStorage.clear ()
    location.reload();

}

// 返回上一个页面
let comeBackCompany = () => {
  router.back();
  //也可以这样写：
  //router.go(-1);
};

</script>

<style scoped>

.top{
    display: flex;
    justify-content: space-between;
    background-color:rgba(255, 182, 213, 0.35);
    backdrop-filter: blur(8px);
    border-bottom: 1px solid rgba(248, 194, 227, 0.3);

    align-items: center;
    width: 100vw;
}

.nav-links a {
    color: rgba(255, 255, 255, 0.85);
    text-decoration: none;
    margin-left: 10px;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 15px;
    font-weight: 500;
    letter-spacing: 1px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
}

.nav-links a:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.12);
    text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.active {
    color: #fff;
    background-color: rgba(255, 105, 180, 0.6);
    border-radius: 4px;
}


.main{ 
    width: 100vw;
    height: 600px;
    display: flex;
    flex-direction: column;
  

}

.center{
    display: flex;
    width: 100%;
    height: 100%;
    background-color: rgb(168, 229, 219);
    justify-content: flex-start;


}

.content {
  position: relative;
  padding: 50px;
  max-width: 800px;
  margin: 40px auto 0;
  /* 文字区域加渐变蒙版，和背景融合 */
  background: linear-gradient(90deg, rgba(26, 37, 51, 0.7) 0%, rgba(26, 37, 51, 0.3) 100%);
  backdrop-filter: blur(6px);
  border-radius: 12px;
  border: 1px solid rgba(141, 219, 239, 0.382);
  box-shadow: 0 0 20px rgba(100, 181, 246, 0.2);
}



</style>



