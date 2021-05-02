<template>
	<div>
		<el-container>
			<el-header>
				<el-row>
					<el-col :span="3">
						<div class="grid-content bg-purple"></div>
					</el-col>
					<el-col :span="18">
						<div class="grid-content bg-purple-light">
							<span class="leftspan1">
								<h1>新闻推荐系统</h1>
							</span>
							<span class="rightspan1" v-if="!isLogin">
								<el-button @click="register()">
									<h3>注册</h3>
								</el-button>

							</span>

							<span class="rightspan1" v-if="!isLogin">
								<el-button @click="login()">
									<h3>登录</h3>
								</el-button>
							</span>

							<span class="rightspan1" v-if="isLogin">
								<el-button @click="logOff()">
									<h3>退出</h3>
								</el-button>
							</span>

							<span class="rightspan1" v-if="isLogin" >
								<el-button @click="singleUser()">
									<h3>{{curUser.userName}}</h3>
								</el-button>
							</span>

							<!-- <span class="rightspan1">
								<el-button @click="backClick()">
									<h3>进入后台</h3>
								</el-button>
							</span> -->

							<span class="rightspan1">
								<el-button @click="foreClick()">
									<h3>首页</h3>
								</el-button>
							</span>
						</div>
					</el-col>
					<el-col :span="3">
						<div class="grid-content bg-purple"></div>
					</el-col>
				</el-row>
			</el-header>
			<el-divider></el-divider>

			<router-view></router-view>

		</el-container>
	</div>
</template>

<script>
	export default {
		methods: {
			foreClick: function() {
				this.$router.replace('/newShowByTime')
			},
			backClick: function() {
				this.$router.replace('/back')
			},
			login: function() {
				this.$router.replace('/login')
			},
			register: function() {
				this.$router.replace('/register')
			},
			logOff: function() {
				localStorage.clear()
				this.isLogin = false
				this.$router.replace('/login')
			},
			singleUser: function() {
				this.$router.replace({
					path: '/singleUser',
					query: {
						userId: localStorage.getItem("id")
					}
				});
			}
	
	
		},
		data() {
			return {
				curUser: "",
				isLogin: true
			}
		},
		created() {
			this.curUser = localStorage;
		}
	}
</script>

<style>
	.rightspan1 {
		float: right;
	}
	
	.leftspan1 {
		float: left;
	}
	
	a {
		text-decoration: none;
	}
	
	.main {
		text-align: center;
	}
	
	.el-aside {
		/* color: #333; */
	}
	
	.el-header {
		/* background-color: #5aaaff; */
		color: #333;
		text-align: center;
	}
</style>
