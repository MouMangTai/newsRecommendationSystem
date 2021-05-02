<template>
	<div>
				<el-button @click="loadNewsByTime()">最新消息</el-button>
				<el-button @click="loadNewsByHeat()">热门新闻</el-button>
				<el-divider ></el-divider>
						<div v-for="(item,index) in news">
							<span class="leftspan">
								[
								{{items[item.typeId-1].type}}
								]
							</span>
							<span class="midspan">
								<el-link @click="singleNew(item.newId)"><div class="Bigfont">{{item.newTitle}}</div></el-link>
							</span>
							<span class="rightspan">
								{{item.newCreattime}}
							</span>
							
							<el-divider v-if="(index+1)%5==0"></el-divider>
						</div>
						<el-divider ></el-divider>
						<el-pagination :page-size="size" :pager-count="11" layout="prev, pager, next" :total="total"
							@current-change="loadNewsByHeat">
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
				total: null
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
					.then(function(response) {
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			loadNewsByTime(){
				this.$router.push('/newShowByTime');
			},
			loadNewsByHeat(currentPage) {
				const _this = this
				axios.get('http://localhost:8888/News/PageByHeatNum/' + currentPage + '/30')
					.then(function(response) {
						console.log(response);
						_this.news = response.data.data.content;
						_this.total = response.data.data.totalElements;
						_this.size = response.data.data.size;
					})
					.catch(function(error) {
						console.log(error);
					});
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
			this.loadNewsByHeat(1);
		}
	}
</script>

<style>
	.rightspan {
		float: right;
	}


</style>
