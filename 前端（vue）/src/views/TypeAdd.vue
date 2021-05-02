<template>
	<div>
		<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
			<el-form-item label="标签名称" prop="type">
				<el-input v-model="ruleForm.type"></el-input>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
				<el-button @click="resetForm('ruleForm')">重置</el-button>
			</el-form-item>
		</el-form>
	</div>

</template>

<script>
	export default {
		data() {
			return {
				ruleForm: {
					type: ''
				},
				rules: {
					type: [{
							required: true,
							message: '请输入标签名称',
							trigger: 'blur'
						},
						{
							min: 2,
							max: 5,
							message: '长度在 2 到 5 个字符',
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
						axios.post("http://localhost:8888/Type/add", _this.ruleForm).then((response) => {
							alert("添加成功");
							this.$router.replace('/typeShow');
						});
					} else {
						alert('error submit!!');
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
