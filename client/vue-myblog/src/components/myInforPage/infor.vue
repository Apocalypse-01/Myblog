<template>
    <div class="main">

        <div class="left">
           
            <div class="sidebar-panel">
                
                <div class="avatar-area" @click="goToProfile">
                    <div class="avatar-wrapper">
                        <img v-if="avatarUrl" :src="avatarUrl" class="avatar-img" />
                        <div v-else class="avatar-placeholder">
                            <span>{{ userNameInitial }}</span>
                        </div>
                    </div>
                    <h3 class="avatar-name">{{ displayName }}</h3>
                </div>
                
                <nav>
                    <RouterLink to="/main/infor/dashboard" active-class="active">数据中心</RouterLink>
                    <RouterLink to="/main/infor/myarticle" active-class="active">我的文章</RouterLink>
                    <RouterLink to="/main/infor/myfavority" active-class="active">我的收藏</RouterLink>
                    <RouterLink to="/main/infor/mylike" active-class="active">我的点赞</RouterLink>
                    <RouterLink to="/main/infor/myinfor" active-class="active">我的信息</RouterLink>
                    
                </nav>

            </div>
            
            <div style="width: 100%;height: 100vh;">
                <router-view/>
            </div>
        </div>

      
        
   </div>

</template>



<script setup name="Myinfor">
import { ref, computed, onMounted } from 'vue'
import { getUserInfo } from '@/api/user'
import router from '@/router/indext'

const avatarUrl = ref('')
const displayName = ref('用户')

const userNameInitial = computed(() => {
    return displayName.value ? displayName.value.charAt(0) : '?'
})

function goToProfile() {
    router.push('/main/infor/myinfor')
}

async function loadAvatar() {
    const userId = localStorage.getItem('authorId')
    if (!userId) return
    try {
        const res = await getUserInfo(Number(userId))
        if (res.code == 1) {
            displayName.value = res.data.userName || '用户'
            avatarUrl.value = res.data.avatar || ''
        }
    } catch(e) {}
}

onMounted(() => {
    loadAvatar()
})

</script>

<style scoped>
.main{ 
width: 100vw;
height: 100%;
display: flex;
flex-direction: column;
backdrop-filter: blur(10px);
}

.left{
display: flex;
width: 100%;
height: 100%;
justify-content: center;
}

.sidebar-panel {
    text-align: center;
    background-color: rgba(255, 182, 213, 0.28);
    backdrop-filter: blur(10px);
    padding: 10px;
    width: 20%;
    margin-left: 10%;
    margin-right: 0.2%;
    border: 1px solid rgba(255, 182, 213, 0.3);
    border-radius: 12px;
    flex-shrink: 0;
}

nav {
    padding: 4px 8px;
    margin: 10px 0;
    display: flex;
    flex-direction: column;
    gap: 2px;
}

nav a {
    display: block;
    text-align: center;
    padding: 10px 16px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 500;
    letter-spacing: 1px;
    text-decoration: none;
    color: rgba(255, 255, 255, 0.8);
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.12);
    transition: all 0.25s ease;
}

nav a:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.15);
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.18);
}

.active {
    color: #fff !important;
    background-color: rgba(255, 105, 180, 0.55) !important;
    border-radius: 8px;
    font-weight: 600;
}

.avatar-area{
    background-color:rgba(255, 182, 213, 0.25);
    padding: 15px 10px 10px;
    cursor: pointer;
    transition: all 0.3s ease;
    border-radius: 8px;
}
.avatar-area:hover{
    background-color: rgba(255, 182, 213, 0.4);
}
.avatar-wrapper{
    width: 80px;
    height: 80px;
    margin: 0 auto;
    border-radius: 50%;
    overflow: hidden;
    border: 3px solid rgba(255, 255, 255, 0.6);
    box-shadow: 0 2px 12px rgba(255, 105, 180, 0.3);
    transition: all 0.3s ease;
}
.avatar-area:hover .avatar-wrapper{
    transform: scale(1.08);
    border-color: rgba(255, 105, 180, 0.8);
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.5);
}
.avatar-img{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.avatar-placeholder{
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, rgba(255,105,180,0.5), rgba(255,182,213,0.6));
    color: #fff;
    font-size: 32px;
    font-weight: bold;
}
.avatar-name{
    margin-top: 8px;
    color: #fff;
    text-shadow: 0 1px 3px rgba(0,0,0,0.2);
    font-size: 14px;
}

</style>
