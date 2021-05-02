<template>
	<div>
		<el-button @click="loadNewsByTime()">最新消息</el-button>
		<el-button @click="loadNewsByHeat()">热门新闻</el-button>
		<div class="rightspan">
			<el-select v-model="value" placeholder="请选择">
			    <el-option
			      v-for="(item,index) in items"
			      :key="item.id"
			      :label="item.type"
			      :value="index+1">
			    </el-option>
			  </el-select>
		</div>
		
		
		<el-divider></el-divider>
		<div v-for="(item,index) in news">

			<span class="leftspan">
				[
				{{items[item.typeId-1].type}}
				]
			</span>

			<span class="midspan">
				<el-link @click="singleNew(item.newId)">
					<div class="Bigfont">{{item.newTitle}}</div>
				</el-link>
			</span>

			<span class="rightspan">

				{{item.newCreattime}}

			</span>
			<el-divider v-if="(index+1)%5==0"></el-divider>
		</div>
		<el-divider></el-divider>
		<el-pagination :page-size="size" :pager-count="11" layout="prev, pager, next" :total="total"
			@current-change="loadNewsByTime">
		</el-pagination>
		</el-tabs>

	</div>
</template>

<script>
	export default {
		data() {
			return {
				items: null,
				activeName: 'first',
				news: null,
				size: null,
				total: null,
				value:null
			}
		},
		methods: {
			singleNew(id) {
				this.$router.push({
					path: '/singleNew',
					query: {
						id: id
					}
				});
				const _this = this
				axios.get('http://localhost:8888/News/heat/' + id)
					.then(function(response) {})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadNewsByTime(currentPage) {
				const _this = this
				axios.get('http://localhost:8888/News/Page/' + currentPage + '/30')
					.then(function(response) {
						_this.news = response.data.data.content;
						_this.total = response.data.data.totalElements;
						_this.size = response.data.data.size;
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadNewsByHeat() {
				this.$router.push('/newShowByHeat');
			},
			loadType() {
				const _this = this
				axios.get('http://localhost:8888/Type/all')
					.then(function(response) {
						
						_this.items = response.data.data;
					})
					.catch(function(error) {
						console.log(error);
					});
			}
		},
		created() {
			this.loadType();
			
			this.loadNewsByTime(1);
		}
	}
</script>

<style>
	.rightspan {
		float: right;
	}

	.Bigfont {
		font-weight: 1000
	}
	.text-wrapper {
		word-break: break-all;
		word-wrap: break-word;
	}
	
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
	
	pre {
		white-space: pre-wrap;
		/* css3.0 */
		white-space: -moz-pre-wrap;
		/* Firefox */
		white-space: -pre-wrap;
		/* Opera 4-6 */
		white-space: -o-pre-wrap;
		/* Opera 7 */
		word-wrap: break-word;
		/* Internet Explorer 5.5+ */
	}
	
	.box-card {
		margin-top: 20px;
		width: auto;
		height: auto;
	}
</style>
