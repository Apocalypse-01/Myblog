// stores/global.js
import { defineStore } from 'pinia'

export const useGlobalStore = defineStore('global', {
  // 仅声明布尔类型的响应式变量（初始值按需设为 true/false）
  state: () => ({
    // 布尔变量：示例为“全局弹窗是否显示”，初始值 false
    isLogin: localStorage.getItem('isLogin') === 'true' 
  }),
  
  // 仅保留修改该布尔变量的方法（按需添加）
  actions: {
    // 方法1：直接设置布尔值
    setIslogin(value:boolean) {
      
      this.isLogin = value // 接收布尔值参数，直接赋值
       // 同步到本地存储（localStorage 仅支持字符串，需转换）
      localStorage.setItem('isLogin', this.isLogin.toString())
      
    },
    // 方法2：切换布尔值（常用，比如“显示/隐藏”切换）
    toggleIslogin() {
      this.isLogin = !this.isLogin
      localStorage.setItem('isLogin', this.isLogin.toString())
    }
  }
})