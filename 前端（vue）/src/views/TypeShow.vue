<template>
	<div>
		<el-table :data="tableData" border style="width: 100%">
			<el-table-column fixed prop="id" label="id">
			</el-table-column>
			<el-table-column prop="type" label="标签">
			</el-table-column>
			
		
			<el-table-column fixed="right" label="操作" width="100">
				<template slot-scope="scope">
					
					<el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
					<el-button type="text" size="small">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<!-- 分页 -->
		<el-pagination 
		:page-size="size" 
		:pager-count="11" 
		layout="prev, pager, next" 
		:total="total"
		@current-change="loadTable">
		</el-pagination>
		
		<!-- 弹出窗 -->
		<el-dialog title="新闻信息" :visible.sync="dialogVisible" width="30%">
			<el-form ref="curData" :model="curData" label-width="80px">
				<el-form-item label="标签">
					<el-input v-model="curData.type"></el-input>
				</el-form-item>
		
		
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="save">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		methods: {
			loadTable(currentPage) {
				const _this = this
				axios.get('http://localhost:8888/Type/Page/'+currentPage+'/4')
					.then(function(response) {
						console.log(response);
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
				size:null,
				total:null,
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
