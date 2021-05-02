<template>
	<div>
		<el-container>
			<!-- 侧页面 -->
			<el-aside width="200px" style="background-color: rgb(238, 241, 246)">
				<el-menu :default-openeds="['1','2','3']"   router   >
					<el-submenu index="1">
						<template slot="title"><i class="el-icon-setting"></i>新闻管理</template>
						<el-menu-item index="/newShow">查看新闻</el-menu-item>
						<el-menu-item index="/newAdd">添加新闻</el-menu-item>
					</el-submenu>
					<el-submenu index="2">
						<template slot="title"><i class="el-icon-setting"></i>用户管理</template>
						<el-menu-item index="/userShow">查看用户</el-menu-item>
						<el-menu-item index="/userAdd">添加用户</el-menu-item>
					</el-submenu>
					<el-submenu index="3">
						<template slot="title"><i class="el-icon-setting"></i>标签管理</template>
						<el-menu-item index="/typeShow">查看标签</el-menu-item>
						<el-menu-item index="/typeAdd">添加标签</el-menu-item>
					</el-submenu>
				</el-menu>
			</el-aside>
			<!-- 主页面 -->
			<el-main>
				<router-view></router-view>
			</el-main>
		</el-container>
	</div>
</template>

<script>
	export default {
		methods: {
			loadTable(currentPage) {
				const _this = this
				axios.get('http://localhost:8888/Users/Page/' + currentPage + '/2')
					.then(function(response) {
						_this.tableData = response.data.data.content;
						_this.total = response.data.data.totalElements;
						_this.size = response.data.data.size;
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			edit(row) {
				console.log(row);
				this.curData = row;
				this.dialogVisible = true;
			},
			save() {
	
			}
		},
		data() {
			return {
				size: null,
				total: null,
				curData: {},
				tableData: null,
				dialogVisible: false
			}
		},
		created() {
			this.loadTable(1);
		}
	}
</script>

<style>
</style>
