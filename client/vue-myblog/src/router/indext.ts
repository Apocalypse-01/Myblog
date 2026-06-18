import Myinfor from'@/components/myInforPage/myInfor.vue'
import Home from '@/components/mainPage/home.vue'
import Login from '@/components/Page/login.vue'
import Register from '@/components/Page/register.vue'
import Main from '@/views/main.vue'
import Welcome from '@/views/welcome.vue'
import { createRouter, createWebHistory, RouterLink } from 'vue-router'
import Article from '@/components/mainPage/article.vue'
import ArticleList from '@/components/mainPage/articleList.vue'
import Infor from '@/components/myInforPage/infor.vue'
import MyArticle from '@/components/myInforPage/myArticle.vue'
import Mylike from '@/components/myInforPage/mylike.vue'
import Myfavority from '@/components/myInforPage/myfavority.vue'
import Release from '@/components/mainPage/release.vue'
import ReadArticle from '@/components/elsePage/readArticle.vue'
import KnowledgeGraph from '@/components/mainPage/knowledgeGraph.vue'
import Dashboard from '@/components/mainPage/dashboard.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/main/home',
    //   // name: 'Welcome',
    //   component: Welcome,
    //   children:[{
    //     path:'',
    //     // name:'Login',
    //     component:Login,
        
    //   },{
    //     path:'/register',
    //     component:Register
    //   }
    // ],
    // },
    {
      path: '/main',
      name: 'Main',
      component:Main,
      children:[
        {

          path: '/',
          name: 'Home',
          component:Home    

      }
      ,
      {
        path: '/main/articleList',
        name: 'ArticleList',
        component:ArticleList   
      },
      {
        path: '/main/release',
        name: 'Release',
        component:Release   
      },
      {
        path: '/main/login',
        // name: 'Welcome',
        component: Welcome,
        children:[{
          path:'',
          // name:'Login',
          component:Login,
          
        },{
          path:'register',
          component:Register
        }
      ],
      },
      {
        path: '/main/read/article',
        name: 'Article',
        component:Article   
      },
      {
        path: '/main/infor',
        name: 'Infor',
        component:Infor,
        children:[{

            path: '/main/infor/myinfor',
            name: 'myInfor',
            component:Myinfor

          },
          {
            path: '/main/infor/myarticle',
            name: 'Myarticle',
            component:MyArticle
          },
          {
            path: '/main/infor/mylike',
            name: 'Mylike',
            component:Mylike
          },
          {
            path: '/main/infor/myfavority',
            name: 'Myfavority',
            component:Myfavority
          },
          {
            path: '/main/infor/dashboard',
            name: 'Dashboard',
            component:Dashboard
          },
         ]
      },
      {
        path:'/main/readArticle',
        name:'ReadArticle',
        component:ReadArticle
      },
      {
        path:'/main/knowledgeGraph',
        name:'KnowledgeGraph',
        component:KnowledgeGraph
      }

    ]
  },


    
  ],


  
}
)

export default router
