
import _axios from '@/utils/request.js'
import {User} from '@/class/class.js'


/**
 * 用户登陆
 * @param user 
 * @returns 
 */
export  async function userLogin(user:User){
  
  // 接受的结果
  const re = await _axios.post('/user/login',
    {
      // 传递的参数
      email:user.getEmail(),
      password:user.getPassword()
    
    })
    
  return re
}


/**
 * 用户注册
 * @param user 
 * @returns 
 */
export async function userRegister(user:User){

  // 传递的参数
  const re = await _axios.post('/user/register',{
        userName:user.getUserName(),
        email:user.getEmail(),
        password:user.getPassword(),
        phone:user.getPhone(),

    }
  )
  
  return re

}

// // 获取用户信息
export async function getUserInfo(userId:number) {

  const re = await _axios.get('/user/getUserInfo',{
          params: {
                userId: userId
            }
      }
    )
  
  return re

}

export async function updateUserInfo(data: any) {
  const re = await _axios.put('/user/updateUserInfo', data)
  return re
}

export async function uploadAvatar(formData: FormData) {
  const re = await _axios.post('/user/uploadAvatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
  return re
}




