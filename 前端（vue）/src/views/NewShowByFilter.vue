<template>
	<div>
		
				<!-- <el-button v-for="(item,index) in items" type="warning" v-bind:key="index" round>
					{{ item.type }}
				</el-button> -->
						<div v-for="item in news">
							<span class="leftspan">
								[
								{{items[item.typeId-1].type}}
								]
							</span>
							<span class="midspan">
								<el-link @click="singleNew(item.newId)">{{item.newTitle}}</el-link>
							</span>
							<span class="rightspan">
								热度:{{item.heatNum}}
								<el-divider direction="vertical"></el-divider>
								{{item.newCreattime}}
							</span>
						</div>
				<!-- </el-tabs> -->

			
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
			loadNewsByFilter() {
				const _this = this
				axios.get('http://localhost:8888/Rec/recByUser/'+localStorage.getItem("id"))
					.then(function(response) {
						console.log(response.data.data);
						_this.news = response.data.data;
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
			this.loadNewsByFilter();
		}
	}
</script>

<style>
	.rightspan {
		float: right;
		color: #8f9295;
	}

	.leftspan {
		color: #8f9295;
	}



	
</style>
