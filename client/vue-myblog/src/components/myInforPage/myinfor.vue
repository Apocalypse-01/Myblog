<template>
    <div style="width: 80%;
                background-color:rgba(255, 182, 213, 0.25);
                backdrop-filter: blur(8px);
                display: flex;align-items: baseline;position: relative;flex-direction: column;
                box-shadow:2px 2px 3px rgba(0,0,0,0.1);
                border: 2px solid rgba(255, 182, 213, 0.4);border-radius: 12px;
                overflow-y: auto;" >

        <div style="flex-direction: column;margin-top: 5%;margin-left: 5%;width: 90%;">

            <!-- 头像区域 -->
            <div class="avatar-section">
                <div class="avatar-display" @click="isEditing ? triggerUpload() : null" :class="{ 'avatar-editable': isEditing }">
                    <img v-if="avatarUrl" :src="avatarUrl" class="avatar-img" />
                    <div v-else class="avatar-placeholder">
                        <span>{{ userNameInitial }}</span>
                    </div>
                    <div v-if="isEditing" class="avatar-overlay">更换头像</div>
                </div>
                <input type="file" ref="fileInput" accept="image/jpeg,image/png,image/gif" style="display:none" @change="handleFileChange" />
                <div v-if="isEditing && previewUrl" class="preview-hint">已选择新头像，保存后生效</div>
            </div>

            <template v-if="!isEditing">
                <div class="info-card">
                    <div class="info-label">名字</div>
                    <div class="info-value">{{userName || '未设置'}}</div>
                </div>
                
                <div class="info-card">
                    <div class="info-label">邮箱</div>
                    <div class="info-value">{{email || '未设置'}}</div>
                </div>

                <div class="info-card">
                    <div class="info-label">手机号</div>
                    <div class="info-value">{{phone || '未设置'}}</div>
                </div>

                <div class="info-card">
                    <div class="info-label">个性签名</div>
                    <div class="info-value">{{signature || '未设置'}}</div>
                </div>     
            </template>

            <template v-else>
                <div class="info-card">
                    <div class="info-label">名字</div>
                    <div class="info-value">
                        <el-input v-model="editForm.userName" size="small" placeholder="请输入名字" />
                    </div>
                </div>
                
                <div class="info-card">
                    <div class="info-label">邮箱</div>
                    <div class="info-value">
                        <el-input v-model="editForm.email" size="small" placeholder="请输入邮箱" />
                        <div v-if="emailError" class="error-tip">{{ emailError }}</div>
                    </div>
                </div>

                <div class="info-card">
                    <div class="info-label">手机号</div>
                    <div class="info-value">
                        <el-input v-model="editForm.phone" size="small" placeholder="请输入手机号" />
                        <div v-if="phoneError" class="error-tip">{{ phoneError }}</div>
                    </div>
                </div>

                <div class="info-card">
                    <div class="info-label">个性签名</div>
                    <div class="info-value">
                        <el-input v-model="editForm.signature" type="textarea" :rows="2" size="small" placeholder="请输入个性签名" />
                    </div>
                </div>     
            </template>

        </div>

        <div style="display: flex;margin-top: 20px;justify-content: flex-end;margin-right: 10%;margin-bottom: 5%;gap: 10px;">
            <template v-if="!isEditing">
                <button class="info-btn" @click="startEdit">修改信息</button>
            </template>
            <template v-else>
                <button class="info-btn" @click="saveEdit">保存</button>
                <button class="info-btn-secondary" @click="cancelEdit">取消</button>
            </template>
        </div>

    </div>
</template>

<script setup name="MyInfor">
import {getUserInfo, updateUserInfo, uploadAvatar} from "@/api/user"
import { onMounted, ref, reactive, computed } from "vue"
import { ElMessage, ElMessageBox } from 'element-plus'

const userName=ref('')
const phone=ref('')
const email=ref('')
const signature=ref('')
const avatarUrl=ref('')
const userId=ref(0)

const isEditing = ref(false)
const editForm = reactive({
    userName: '',
    email: '',
    phone: '',
    signature: ''
})
const previewUrl = ref('')
const avatarFile = ref(null)
const fileInput = ref(null)

const userNameInitial = computed(() => {
    return userName.value ? userName.value.charAt(0) : '?'
})

const emailError = computed(() => {
    if (!editForm.email) return ''
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(editForm.email) ? '' : '邮箱格式不正确'
})

const phoneError = computed(() => {
    if (!editForm.phone) return ''
    const re = /^1[3-9]\d{9}$/
    return re.test(editForm.phone) ? '' : '手机号格式不正确'
})

async function loadUserInfo() {
    const user = await getUserInfo(userId.value)
    if (user.code == 1) {
        userName.value = user.data.userName || ''
        phone.value = user.data.phone || ''
        email.value = user.data.email || ''
        signature.value = user.data.signature || ''
        avatarUrl.value = user.data.avatar || ''
    }
}

function startEdit() {
    editForm.userName = userName.value
    editForm.email = email.value
    editForm.phone = phone.value
    editForm.signature = signature.value
    previewUrl.value = ''
    avatarFile.value = null
    isEditing.value = true
}

function triggerUpload() {
    fileInput.value.click()
}

function handleFileChange(e) {
    const file = e.target.files[0]
    if (!file) return
    if (file.size > 2 * 1024 * 1024) {
        ElMessage.warning('头像文件大小不能超过2MB')
        return
    }
    if (!['image/jpeg', 'image/png', 'image/gif'].includes(file.type)) {
        ElMessage.warning('仅支持 JPG、PNG、GIF 格式')
        return
    }
    avatarFile.value = file
    const reader = new FileReader()
    reader.onload = (ev) => {
        previewUrl.value = ev.target.result
        avatarUrl.value = ev.target.result
    }
    reader.readAsDataURL(file)
}

function cancelEdit() {
    const hasChanges = editForm.userName !== userName.value || editForm.email !== email.value ||
        editForm.phone !== phone.value || editForm.signature !== signature.value || avatarFile.value
    if (hasChanges) {
        ElMessageBox.confirm('未保存的修改将丢失，确定取消吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            isEditing.value = false
            avatarFile.value = null
            previewUrl.value = ''
            loadUserInfo()
        }).catch(() => {})
    } else {
        isEditing.value = false
    }
}

async function saveEdit() {
    if (emailError.value) {
        ElMessage.warning(emailError.value)
        return
    }
    if (phoneError.value) {
        ElMessage.warning(phoneError.value)
        return
    }
    if (!editForm.userName.trim()) {
        ElMessage.warning('名字不能为空')
        return
    }

    try {
        await ElMessageBox.confirm('确定保存修改吗？', '确认修改', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
        })
    } catch {
        return
    }

    const hasNewAvatar = avatarFile.value !== null && avatarFile.value !== undefined
    let avatarPath = ''

    if (hasNewAvatar) {
        const formData = new FormData()
        formData.append('file', avatarFile.value)
        formData.append('userId', String(userId.value))
        try {
            const uploadRes = await uploadAvatar(formData)
            if (uploadRes && uploadRes.code == 1) {
                avatarPath = uploadRes.data
            } else {
                ElMessage.error('头像上传失败：' + (uploadRes?.msg || '服务器错误'))
                return
            }
        } catch(e) {
            ElMessage.error('头像上传失败，请检查网络后重试')
            return
        }
    }

    try {
        const updateData = {
            id: userId.value,
            userName: editForm.userName,
            email: editForm.email,
            phone: editForm.phone,
            signature: editForm.signature
        }
        if (hasNewAvatar && avatarPath) {
            updateData.avatar = avatarPath
        }

        const res = await updateUserInfo(updateData)

        if (res && res.code == 1) {
            ElMessage.success('信息修改成功')
            isEditing.value = false
            avatarFile.value = null
            previewUrl.value = ''
            if (fileInput.value) {
                fileInput.value.value = ''
            }
            await loadUserInfo()
        } else {
            ElMessage.error('修改失败：' + (res?.msg || '请重试'))
        }
    } catch(e) {
        ElMessage.error('保存失败，请检查网络后重试')
    }
}

onMounted(()=>{
    const id = localStorage.getItem("authorId")
    userId.value = Number(id)
    loadUserInfo()
})
</script>

<style scoped>
.avatar-section{
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
}
.avatar-display{
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
    border: 3px solid rgba(255, 182, 213, 0.5);
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.2);
    position: relative;
    transition: all 0.3s ease;
}
.avatar-editable{
    cursor: pointer;
}
.avatar-editable:hover{
    transform: scale(1.05);
    border-color: rgba(255, 105, 180, 0.8);
    box-shadow: 0 6px 20px rgba(255, 105, 180, 0.4);
}
.avatar-display:hover .avatar-overlay{
    opacity: 1;
}
.avatar-overlay{
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,0.5);
    color: #fff;
    text-align: center;
    font-size: 12px;
    padding: 4px 0;
    opacity: 0;
    transition: opacity 0.3s ease;
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
    font-size: 36px;
    font-weight: bold;
}
.preview-hint{
    font-size: 12px;
    color: #d63384;
    margin-top: 8px;
}
.info-card{
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.55);
    backdrop-filter: blur(6px);
    border-radius: 10px;
    border: 1px solid rgba(255, 182, 213, 0.3);
    margin-top: 12px;
    margin-bottom: 12px;
    padding: 14px 18px;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(255, 105, 180, 0.1);
}
.info-card:hover{
    background: rgba(255, 255, 255, 0.75);
    box-shadow: 0 4px 16px rgba(255, 105, 180, 0.2);
    transform: translateY(-2px);
}
.info-label{
    width: 100px;
    font-size: 14px;
    font-weight: 600;
    color: #d63384;
    flex-shrink: 0;
}
.info-value{
    font-size: 15px;
    color: #333;
    margin-left: 16px;
    flex: 1;
}
.info-btn{
    background-color: rgba(255, 105, 180, 0.7);
    border: none;
    color: #fff;
    padding: 8px 20px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.3s ease;
}
.info-btn:hover{
    background-color: rgba(255, 105, 180, 0.9);
}
.info-btn-secondary{
    background-color: rgba(255, 255, 255, 0.6);
    border: 1px solid rgba(255, 182, 213, 0.5);
    color: #d63384;
    padding: 8px 20px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.3s ease;
}
.info-btn-secondary:hover{
    background-color: rgba(255, 255, 255, 0.8);
    border-color: rgba(255, 182, 213, 0.7);
}
.error-tip{
    font-size: 12px;
    color: #f56c6c;
    margin-top: 4px;
}
</style>
