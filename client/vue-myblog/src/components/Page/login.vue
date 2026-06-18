
<template>
          <div style="height: 40vh;width: 100%;
               display: flex;justify-content: center;

               ">
               <h2 style="align-self: center;flex-wrap: wrap;flex-direction: column;">登陆界面</h2>

          </div>
            
          <div class="butt" >
               <el-input  style="width: 240px;;" v-model="email"  type="text" placeholder="用户名/电子邮箱"/>
               <br>
               <el-input  style="width: 240px;" v-model="password" placeholder="密码" show-password />
               <br> 
               <el-button @click="login"   >登陆</el-button>
               <br>
               <el-button @click="register">注册</el-button>

          </div>

</template>

<!--  -----------------------------------------------------  -->

<script setup lang="ts" name="Register">
import router from '@/router/indext';
import { ElInput } from 'element-plus';
import { ref } from 'vue';
import {userLogin} from '@/api/user.js'
import  {User}  from '@/class/class.ts'
import { useGlobalStore } from '@/stores/global'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'


let email= ref('') ;
let password=ref('')


//发送请求，将账号密码传递给后端
async function login(){ 
     if(!email.value.trim()){
         ElMessage.warning('请输入邮箱')
         return
     }
     if(!password.value.trim()){
         ElMessage.warning('请输入密码')
         return
     }

     const user=new User('','',email.value,password.value)

     const re = await userLogin(user)

     if(re.code==1){
          const token=re.data.token
          localStorage.setItem("token", token)
          localStorage.setItem("authorId",re.data.id)
          localStorage.setItem("authorName",re.data.userName)
          localStorage.setItem("authorEmail",re.data.email)

          const globalStore=useGlobalStore()
          globalStore.toggleIslogin()
          const {isLogin}=storeToRefs(globalStore)

          router.push({path:'/'})
     }else{
          ElMessage.error(re.msg || '用户名或密码不正确')
     }

}    

function register(){

     router.push({path:'/main/login/register'})
    
}        


</script>

// ------------------------------------------------------------

<style scoped>

.butt{
            display: flex ;
            flex-direction: column

}
    

</style>
