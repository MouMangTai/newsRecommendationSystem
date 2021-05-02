<template>
	<div>
		<el-row>

			<el-col :span="4">
				<div class="grid-content bg-purple"></div>
			</el-col>
			<el-col :span="11">
				<router-view></router-view>
			</el-col>
			<el-col :span="1">
				<div class="grid-content bg-purple"></div>
			</el-col>

			<el-col :span="6">
				<div class="grid-content bg-purple">
					<el-card class="box-card" shadow="hover" v-if="hasPrefer==false">

						<h3>新闻推荐</h3>
						<div>
							<h4>选择喜欢的标签</h4>
							<div class="tag-group">
								<el-tag v-for="(item,index) of types" :key="index" @click="selectTag(index,item.type)"
									style="cursor:pointer">{{item.type}}
								</el-tag>
							</div>
							<h4>已选择{{tagTypes.length}}个标签</h4>
							<el-tag v-for="(item,index) of tagTypes" :key="index" type="warning">
								{{item}}
							</el-tag>
							<div>
								<el-button @click="submitType()">提交</el-button>
							</div>

						</div>
					</el-card>
					<el-card class="box-card" shadow="hover" v-if="hasPrefer==true">
						<h3>基于K-means的协同过滤推荐</h3>
						<div>
							<div v-for="(item,index) in newsByKmeans">
								<!-- <el-divider></el-divider> -->
								<span class="leftspan">
									[
									{{types[item.typeId-1].type}}
									]
								</span>
								<span class="midspan2">
									<el-link @click="singleNew(item.newId)">{{item.newTitle.substring(0,19)}}</el-link>
								</span>
							</div>
						</div>
					
					</el-card>
					<el-card class="box-card" shadow="hover" v-if="hasPrefer==true">
						<h3>基于用户的协同过滤推荐</h3>
						<div>
							<div v-for="(item,index) in newsByUser">
								<!-- <el-divider></el-divider> -->
								<span class="leftspan">
									[
									{{types[item.typeId-1].type}}
									]
								</span>
								<span class="midspan2">
									<el-link @click="singleNew(item.newId)">{{item.newTitle.substring(0,19)}}</el-link>
								</span>
							</div>
						</div>

					</el-card>
					<el-card class="box-card" shadow="hover" v-if="hasPrefer==true">
						<h3>基于物品的协同过滤推荐</h3>
						<div>

							<div v-for="(item,index) in newsByItem">
								<!-- <el-divider></el-divider> -->
								<span class="leftspan">
									[
									{{types[item.typeId-1].type}}
									]
								</span>
								<span class="midspan2">
									<el-link @click="singleNew(item.newId)">{{item.newTitle.substring(0,19)}}</el-link>
								</span>
							</div>
						</div>

					</el-card>


				</div>
			</el-col>
			<el-col :span="4">
				<div class="grid-content bg-purple"></div>
			</el-col>

		</el-row>


	</div>
</template>

<script>
	export default {
		data() {
			return {
				hasPrefer: null,
				types: null,
				arr: [],
				tagTypes: [],
				lock: true,
				newsByUser: null,
				newsByItem: null,
				newsByKmeans: null,
				size: null,
				total: null,
				typeForm: {
					userName: '',
					prefer: ""
				},
			}
		},
		methods: {
			submitType() {
				if (this.tagTypes.length == 0) alert("请至少选择一个标签");
				else if (localStorage.getItem("id") == null) {
					alert("请先登录");
					this.$router.push("/login");
				} else {
					const _this = this

					this.typeForm.userName = localStorage.getItem("userName");
					this.typeForm.prefer = _this.tagTypes.toString();

					axios.put('http://localhost:8888/Users/updateByUserName', _this.typeForm)
						.then(function(response) {
							alert("选择成功")
							_this.hasPrefer = true;
							localStorage.setItem('prefer',_this.tagTypes.toString());
							console.log(localStorage);
							_this.loadNewsByFilterItem();
							_this.loadNewsByFilterUser();
							_this.loadNewsByFilterKmeans();
						})
						.catch(function(error) {
							console.log(error);
						});
				}
			},
			selectTag(index, tagType) {
				this.lock = true;
				for (let i = 0; i < this.tagTypes.length; i++) {
					if (this.tagTypes[i] == tagType) {
						this.tagTypes.splice(i, 1);
						this.lock = false;
					}
				}
				for (let i = 0; i < this.arr.length; i++) {
					if (i == index) {
						this.arr[index] ? this.arr.splice(index, 1, false) : this.arr.splice(index, 1, true);
					}
				}
				if (this.lock) {
					this.tagTypes.push(tagType);
				}
			},
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
			loadNewsByFilterUser() {
				const _this = this
				if (localStorage != null &&localStorage.getItem("prefer")!="null") {
					axios.get('http://localhost:8888/Rec/recByUser/' + localStorage.getItem("id"))
						.then(function(response) {
							_this.newsByUser = response.data.data;
						})
						.catch(function(error) {
							console.log(error);
						});
				}

			},
			loadNewsByFilterKmeans() {
				const _this = this
				if (localStorage != null &&localStorage.getItem("prefer")!="null") {
					axios.get('http://localhost:8888/Rec/recByKmeans/' + localStorage.getItem("id"))
						.then(function(response) {
							_this.newsByKmeans = response.data.data;
						})
						.catch(function(error) {
							console.log(error);
						});
				}
			
			},
			loadNewsByFilterItem() {
				const _this = this
				console.log(localStorage);
				if (localStorage != null&&localStorage.getItem("prefer")!="null") {
					axios.get('http://localhost:8888/Rec/recByItem/' + localStorage.getItem("id"))
						.then(function(response) {
							_this.newsByItem = response.data.data;
						})
						.catch(function(error) {
							console.log(error);
						});
				}

			},
			loadType() {
				const _this = this
				axios.get('http://localhost:8888/Type/all')
					.then(function(response) {

						_this.types = response.data.data;
						for (let i = 0; i < _this.types.length; i++) {
							_this.arr.push(false);
						}
					})
					.catch(function(error) {
						console.log(error);
					});
			},
			chargeType() {
				const _this = this

				axios.get("http://localhost:8888/Users/findByName/" + localStorage.getItem("userName")).then(function(
						res) {

						if (res == null) _this.hasPrefer = false;
						else {
							if (res.data.data.prefer == "null") {
								_this.hasPrefer = false;
							} else _this.hasPrefer = true;
						}
					})
					.catch(function(error) {
						_this.hasPrefer = false;
					});

			}
		},

		created() {
			this.loadType();
			this.loadNewsByFilterUser();
			this.loadNewsByFilterItem();
			this.loadNewsByFilterKmeans();
			this.chargeType();
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


	.el-carousel__item h3 {
		color: #475669;
		font-size: 14px;
		opacity: 0.75;
		line-height: 150px;
		margin: 0;
	}

	.el-carousel__item:nth-child(2n) {
		background-color: #99a9bf;
	}

	.el-carousel__item:nth-child(2n+1) {
		background-color: #d3dce6;
	}
</style>
