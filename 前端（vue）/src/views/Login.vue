<template>
	<div>
		<el-row>
			<el-col :span="8">
				<div class="grid-content bg-purple"></div>
			</el-col>
			<el-col :span="8">
				<h2 >用户登录</h2>
				<div class="grid-content bg-purple-light">
					<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

						<el-form-item label="用户名" prop="userName">
							<el-input v-model="ruleForm.userName"></el-input>
						</el-form-item>

						<el-form-item label="用户密码" prop="userPassword" show-password="false">
							<el-input type="password"  v-model="ruleForm.userPassword"></el-input>
						</el-form-item>
						
						<el-form-item>
							<el-button type="primary" @click="submitForm('ruleForm')">立即登录</el-button>
							<el-button @click="resetForm('ruleForm')">重置</el-button>
						</el-form-item>
					</el-form>
				</div>
			</el-col>
			<el-col :span="8">
				<div class="grid-content bg-purple"></div>
			</el-col>
		</el-row>


	</div>
</template>

<script>
	export default {
		data() {
			return {
				checked:false,
				ruleForm: {
					userName: '',
					userPassword: ''
				},
				rules: {
					userName: [{
							required: true,
							message: '请输入用户名',
							trigger: 'blur'
						},
						{
							min: 3,
							max: 7,
							message: '长度在 3 到 7 个字符',
							trigger: 'blur'
						}
					],
					userPassword: [{
							required: true,
							message: '请输入用户密码',
							trigger: 'blur'
						},
						{
							min: 3,
							max: 5,
							message: '长度在 3 到 5 个字符',
							trigger: 'blur'
						}
					]

				}
			};
		},
		methods: {
			submitForm(formName) {
				const _this = this;
				this.$refs[formName].validate((valid) => {
					if (valid) {
						axios.get("http://localhost:8888/Users/findByName/" + _this.ruleForm.userName).then((
							response) => {
							console.log(response);
							if (response.data.data == null) {
								alert("该用户不存在")
							} else {
								if (response.data.data.userName == _this.ruleForm.userName && response.data
									.data.userPassword == _this.ruleForm.userPassword) {
									localStorage.clear();
									localStorage.setItem('id',response.data.data.userId);
									localStorage.setItem('token',response.data.data.token)
									localStorage.setItem('userName',response.data.data.userName);
									localStorage.setItem('prefer',response.data.data.prefer);
									
									_this.$router.push('/newShowByTime');
									_this.$router.go('/');
									alert("登录成功")

								} else {
									alert("密码输入错误")
								}
							}
						});
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			}
		}
	}
</script>

<style>
	.el-row {
		margin-bottom: 20px;

		&:last-child {
			margin-bottom: 0;
		}
	}

	.grid-content {
		border-radius: 4px;
		min-height: 36px;
	}
</style>
