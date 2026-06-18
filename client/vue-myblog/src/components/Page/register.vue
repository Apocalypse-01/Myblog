<template>
    <div style="height: 30vh;display: flex;justify-content: center;">
        <h2 style="display: flex;align-items: center;">注册界面</h2>         
    </div>
    
    <div class="regis" >
        
        <el-form label-width="80px" label-position="left" >
            <el-form-item label="姓名" prop="username" > 
                <el-input v-model="name" placeholder="请输入姓名" />   
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email" >
                <div style="display:flex;flex-direction:column;width:100%;">
                    <el-input v-model="email" placeholder="example@mail.com" />
                    <div class="error-hint" :style="email && !emailValid ? 'visibility:visible' : 'visibility:hidden'">请输入正确的邮箱格式，如 example@mail.com</div>
                </div>
            </el-form-item>

            <el-form-item label="密码"      prop="password" >
                <el-input v-model="password" placeholder="请输入密码" show-password/>
            </el-form-item>

            <el-form-item label="手机号"    prop="phone" >
                <div style="display:flex;flex-direction:column;width:100%;">
                    <el-input v-model="phone" placeholder="请输入11位手机号"/>
                    <div class="error-hint" :style="phone && !phoneValid ? 'visibility:visible' : 'visibility:hidden'">请输入正确的11位手机号</div>
                </div>
            </el-form-item>
        
        </el-form> 
        <br style="height: 20px;">
        <div>
            <el-button  @click="register" style="width: 120px;">提交</el-button>
            <el-button  @click="login" style="width: 120px;">返回登陆</el-button>
        </div>
        
        
    </div>

</template>

<!--  -----------------------------------------------------  -->

<script setup lang="ts" name="Register">
import router from '@/router/indext';
import axios from 'axios';
import { ElInput } from 'element-plus';
import { ref, computed } from 'vue';
import {userRegister} from '@/api/user.js'
import { da } from 'element-plus/es/locales.mjs';
import {User} from '@/class/class';
import { ElMessage } from 'element-plus'

const email=ref("")
const name=ref("")
const password=ref("")
const phone=ref("")

const emailValid = computed(() => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value))
const phoneValid = computed(() => /^1[3-9]\d{9}$/.test(phone.value))

async function register(){
    if(!name.value.trim()){
        ElMessage.warning('请输入姓名')
        return
    }
    if(!emailValid.value){
        ElMessage.warning('请输入正确的邮箱格式')
        return
    }
    if(!password.value.trim()){
        ElMessage.warning('请输入密码')
        return
    }
    if(phone.value && !phoneValid.value){
        ElMessage.warning('请输入正确的手机号')
        return
    }

    const user = new User(name.value,phone.value,email.value,password.value)

    const resu = await userRegister(user)

    if(resu.code==1){
        ElMessage.success('注册成功，请登录')
        router.push({path:'/'})
    } else {
        ElMessage.error(resu.msg || '注册失败')
    }
}

function login(){
    
    router.push({path:'/'})
}

</script>

// ------------------------------------------------------------

<style scoped>
.regis{
  display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center

}
.error-hint{
    font-size: 12px;
    color: #f56c6c;
    margin-top: 4px;
    height: 18px;
    line-height: 18px;
}
</style>
