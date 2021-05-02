import Vue from "vue"
import VueRouter from "vue-router"
import UserShow from "../views/UserShow.vue"
import UserAdd from "../views/UserAdd.vue"
import NewShow from "../views/NewShow.vue"
import NewAdd from "../views/NewAdd.vue"
import TypeShow from "../views/TypeShow.vue"
import TypeAdd from "../views/TypeAdd.vue"
import App from "../App.vue"
import Back from "../views/Back.vue"
import Fore from "../views/Fore.vue"
import SingleNew from "../views/SingleNew.vue"
import NewShowByTime from "../views/NewShowByTime.vue"
import NewShowByHeat from "../views/NewShowByHeat.vue"
import NewShowByFilter from "../views/NewShowByFilter.vue"
import Login from "../views/Login.vue"
import Register from "../views/Register.vue"
import SingleUser from "../views/SingleUser.vue"
import Header from "../views/Header.vue"

const originalReplace = VueRouter.prototype.replace;
VueRouter.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(err => err);
};


Vue.use(VueRouter)

const routes = [
	{
		path: '/app',
		component: App
	},
	{
		path:'/',
		name:'导航栏',
		component:Header,
		redirect:'/fore',
		children:[
			{
				path: '/fore',
				name: '前台',
				component: Fore,
				redirect: '/newShowByTime',
				children: [{
						path: '/newShowByTime',
						name: '最新消息',
						component: NewShowByTime
					},
					{
						path: '/newShowByHeat',
						name: '热门新闻',
						component: NewShowByHeat
					},
					{
						path: '/newShowByFilter',
						name: '基于协同过滤的新闻推荐',
						component: NewShowByFilter
					}
				]
			},
			{
				path: '/back',
				name: '后台',
				component: Back,
				children: [{
						path: '/userShow',
						name: '展示用户',
						component: UserShow
					},
					{
						path: '/userAdd',
						name: '添加用户',
						component: UserAdd
					},
					{
						path: '/newShow',
						name: '展示新闻',
						component: NewShow
					},
					{
						path: '/newAdd',
						name: '添加新闻',
						component: NewAdd
					},
					{
						path: '/typeShow',
						name: '展示标签',
						component: TypeShow
					},
					{
						path: '/typeAdd',
						name: '添加标签',
						component: TypeAdd
					}
				]
			},
			{
				path: '/register',
				name: '用户注册',
				component: Register
			},
			{
				path: '/login',
				name: '用户登录',
				component: Login
			},
			{
				path: '/singleUser',
				name: '个人用户信息',
				component: SingleUser
			},
			{
				path: '/singleNew',
				name: '单独页面',
				component: SingleNew
			}
		]
	}
	


]
const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

export default router
