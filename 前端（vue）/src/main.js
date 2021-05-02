import Vue from 'vue'
import './plugins/axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router  from "./router/index.js"

Vue.use(ElementUI)

// router.beforeEach((to,form,next) =>{
//     //如果进入到的路由是登录页或者注册页面，则正常展示
//     if(to.path == '/login' || to.path == '/register' ){     
//         next();
//     }else if( !(localStorage.getItem('userName')) ){
//         alert("还没有登录，请先登录！");
//         next('/login');     //转入login登录页面，登录成功后会将token存入localStorage
//     }else{
//         next();
//     }
// })
new Vue({
  el: '#app',
  render: h => h(App),
	router 
})
