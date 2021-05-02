<template>
	<div>
		<el-row>

			<el-col :span="4">
				<div class="grid-content bg-purple">


				</div>
			</el-col>
			<el-col :span="11">
				<h2>个人信息</h2>
				<div class="grid-content bg-purple">

					<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

						<el-form-item label="用户名" prop="userName">
							<el-input v-model="curUser.userName"></el-input>
						</el-form-item>
						<el-form-item label="旧密码" prop="oldPassword">
							<el-input type="password" v-model="ruleForm.oldPassword"></el-input>
						</el-form-item>

						<el-form-item label="新密码" prop="newPassword">
							<el-input type="password" v-model="ruleForm.newPassword"></el-input>
						</el-form-item>

						<el-form-item>
							<el-button type="primary" @click="submitForm('ruleForm')">修改密码</el-button>
							<el-button @click="resetForm('ruleForm')">重置</el-button>
						</el-form-item>
					</el-form>


					<h2>我的收藏</h2>


					<el-table :data="collections" height="250" style="width: 100%">
						<el-table-column prop="title" label="新闻标题" fixed width="380">
						</el-table-column>
						<el-table-column prop="time" label="收藏时间">
						</el-table-column>
					</el-table>


					<h2>我的评分</h2>

					<el-table :data="rates" height="250" style="width: 100%">
						<el-table-column prop="title" label="新闻标题" fixed width="380">
						</el-table-column>
						<el-table-column prop="time" label="评分时间">
						</el-table-column>
						<el-table-column prop="score" label="分数">
						</el-table-column>
					</el-table>


				</div>
			</el-col>
			<el-col :span="1">
				<div class="grid-content bg-purple"></div>
			</el-col>

			<el-col :span="5">





<el-card class="box-card" shadow="hover">

					<h3>喜欢的标签</h3>
					<div class="tag-group">
						<el-tag v-for="(item,index) of types" :key="index" style="cursor:pointer">{{item}}
						</el-tag>
					</div>
				</el-card>
				<el-card class="box-card" shadow="hover">

					<h3>猜你喜欢</h3>
					<div v-for="(item,index) in news">
						<span class="midspan2">
							<el-link @click="singleNew(item.newId)">{{item.newTitle.substring(0,19)}}</el-link>
						</span>
						<el-divider></el-divider>
					</div>
				</el-card>



				
			</el-col>
			<el-col :span="4">
				<div class="grid-content bg-purple"></div>
			</el-col>

		</el-row>
	</div>
</template>

<script>
	export default {
		methods: {
			submitForm(formName) {
				const _this = this;
				this.$refs[formName].validate((valid) => {
					if (valid) {

					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			singleNew(id) {
				this.$router.push({
					path: '/singleNew',
					query: {
						id: id
					}
				});
			},
			loadNewsByFilter() {
				const _this = this
				axios.get('http://localhost:8888/Rec/recByUser/' + localStorage.getItem("id"))
					.then(function(response) {
						console.log(response.data.data);
						_this.news = response.data.data;
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadCollectionByUserId(currentPage) {
				const _this = this
				axios.get('http://localhost:8888/Collection/findByUid/' + localStorage.getItem("id"))
					.then(function(response) {
						_this.collections = response.data.data;
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadRateByUserId() {

				const _this = this
				axios.get('http://localhost:8888/Rate/findByUid/' + localStorage
						.getItem("id"))
					.then(function(response) {
						_this.rates = response.data.data;
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadTypes() {
				const _this = this
				axios.get("http://localhost:8888/Users/findByName/" + localStorage.getItem("userName")).then(function(
					res) {

						if (res == null) {} else {

							if (res.data.data.prefer == "null") {
								_this.types = [];
							} else _this.types = res.data.data.prefer.split(",");
						}
						console.log(_this.types);
					})
					.catch(function(error) {});
			}
		},
		data() {
			return {
				types: [],
				Ratesize: null,
				Ratetotal: null,
				Collectionsize: null,
				Collectiontotal: null,
				curUser: null,
				activeName: 2,
				news: null,
				rates: null,
				collections: null,
				ruleForm: {
					oldPassword: '',
					newPassword: ''
				},
				rules: {
					oldPassword: [{
							required: true,
							message: '请输入旧密码',
							trigger: 'blur'
						},
						{
							min: 3,
							max: 6,
							message: '长度在 3 到 6 个字符',
							trigger: 'blur'
						}
					],
					newPassword: [{
							required: true,
							message: '请输入新密码',
							trigger: 'blur'
						},
						{
							min: 3,
							max: 6,
							message: '长度在 3 到 6 个字符',
							trigger: 'blur'
						}
					]

				}
			}
		},
		created() {
			this.curUser = localStorage;
			this.loadNewsByFilter();
			this.loadRateByUserId(1);
			this.loadCollectionByUserId(1);
			this.loadTypes();
		}
	}
</script>

<style>
</style>
