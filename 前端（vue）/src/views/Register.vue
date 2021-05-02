<template>
	<div>
		
		<el-row>
		  <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
		  <el-col :span="8">
				<h2 >用户注册</h2>
				<div class="grid-content bg-purple-light">
					<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
					  
						<el-form-item label="用户名" prop="userName">
					    <el-input v-model="ruleForm.userName"></el-input>
					  </el-form-item>
						
						<el-form-item label="用户密码" prop="userPassword">
						  <el-input type="password" v-model="ruleForm.userPassword"></el-input>
						</el-form-item>
					  
					  <el-form-item>
					    <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
					    <el-button @click="resetForm('ruleForm')">重置</el-button>
					  </el-form-item>
					</el-form>
				</div>
				</el-col>
		  <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
		</el-row>
		
		
	</div>
</template>

<script>
  export default {
    data() {
      return {
        ruleForm: {
          userName: '',
					userPassword:'',
					prefer:'null'
        },
        rules: {
          userName: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 6, message: '长度在 3 到 6 个字符', trigger: 'blur' }
          ],
					userPassword: [
					  { required: true, message: '请输入用户密码', trigger: 'blur' },
					  { min: 3, max: 6, message: '长度在 3 到 6 个字符', trigger: 'blur' }
					]
					
        }
      };
    },
    methods: {
      submitForm(formName) {
				const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
						
						axios.get("http://localhost:8888/Users/findByName/"+_this.ruleForm.userName).then((response) => {
							
							console.log(response.data.data);
							if(response.data.data==null){
								
								axios.post("http://localhost:8888/Users/add", _this.ruleForm).then((response) => {
									alert("注册成功");
									this.$router.replace('/login');
								});
							}else{
								alert("用户名已存在")
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