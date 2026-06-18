// 封装 axios 方便使用axios
import axios from 'axios'


// 创建axios实例
const _axios = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/',
    timeout: 10000,
})

//请求拦截器，添加 token
_axios.interceptors.request.use(
    (config)=>{ 
            //请求成功， 
            //  在发送请求之前做些什么
            //  添加token
        const token = localStorage.getItem('token')
        config.headers['token'] = `${token}`;
        return config
    },
    (error)=>{
        // 对请求错误做些什么
        return Promise.reject(error)
    }
)

//响应拦截器
_axios.interceptors.response.use(
    (response)=>{           //响应状态码，为2xx是表示成功过
        // 对响应数据做些什么
        // 返回响应数据
        return response.data

    },
    (error)=>{           //响应状态码，不是2xx是表示没有成功 

        if (error.response) { 
            switch(error.response.status){
                case 401:
                            // 处理未授权的错误
                    console.error('未授权，请重新登录');
                break;

                case 404:
                            // 请求的资源不存在
                    console.error('请求的资源不存在');
                break;
                
                default:
                    console.error('请求失败，请稍后再试');
            }
        }
        else if (error.request) {
            console.error('请求失败，请检查网络连接');
        }
        else {
            console.error('请求配置错误');

        }
        return Promise.reject(error)
    }
)

export default _axios