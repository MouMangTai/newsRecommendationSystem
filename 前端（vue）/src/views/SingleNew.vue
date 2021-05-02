<template>
	<div>
		<el-row>
			<el-col :span="5">
				<div class="grid-content bg-purple"></div>
			</el-col>
			<el-col :span="14">
				<el-divider></el-divider>
				<div>
					<h1>{{singlenew.newTitle}}</h1>
				</div>
				<el-divider></el-divider>
				<div>
					{{singlenew.newCreattime}}
					<el-divider direction="vertical"></el-divider>
					{{singlenew.newRecourse}}
				</div>
				<div class="content">
					<pre class="text-wrapper" v-html="this.singlenew.newContent"></pre>
				</div>
			</el-col>
			<el-col :span="5">
				<div class="grid-content bg-purple">
					<div class="box">
						<el-card class="box-card" shadow="hover">
							<div>
								<span>评分</span>
								<span class="rightspan" @click="rate()">
									<el-rate  v-model="rateform.score" :colors="colors">
									</el-rate>
								</span>
							</div>
							<div>
								<span>收藏</span>
								<span class="rightspan" @click="collect()">
									<el-switch v-model="isCollect" active-color="#13ce66" inactive-color="#ff4949">
									</el-switch>
								</span>
							</div>
						</el-card>
					</div>
				</div>
			</el-col>
		</el-row>

	</div>
</template>


<script>
	export default {
		data() {
			return {
				singlenew: {},
				newContent: "",
				isCollect: null,
				colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
				rateform: {
					id:0,
					uid: 0,
					nid: 0,
					score: 0
				},
				collectionform:{
					id:0,
					uid:0,
					nid:0,
					collection:0
				}
			}
		},
		methods: {
			collect() {
				if(localStorage.getItem("id")==null){
					alert("请登录");
					this.$router.replace('/login');
					return ;
				}
				const _this = this
				if(_this.isCollect==true){
					_this.collectionform.collection=1
				}else{
					_this.collectionform.collection=0
				}
				
				if(_this.collectionform.id==0){
					console.log(_this.collectionform)
					axios.post('http://localhost:8888/Collection/add', _this.collectionform)
						.then(function(response) {
						})
						.catch(function(error) {
							console.log(error);
						});
				}else{
					console.log(_this.collectionform)
					axios.put('http://localhost:8888/Collection/update', _this.collectionform)
						.then(function(response) {
					
						})
						.catch(function(error) {
							console.log(error);
						});
				}
			},
			rate() {
				if(localStorage.getItem("id")==null){
					alert("请登录");
					this.$router.replace('/login');
					return ;
				}
				const _this = this
				if(_this.rateform.id==0){
					axios.post('http://localhost:8888/Rate/add', _this.rateform)
						.then(function(response) {
						})
						.catch(function(error) {
							console.log(error);
						});
				}else{
					axios.put('http://localhost:8888/Rate/update', _this.rateform)
						.then(function(response) {
					
							alert("评分成功")
						})
						.catch(function(error) {
							console.log(error);
						});
				}
			},
			loadRate(){
				const _this = this
				this.rateform.uid = parseInt(localStorage.getItem("id"))
				this.rateform.nid = this.$route.query.id
				axios.get('http://localhost:8888/Rate/findByUidAndNid/' + this.rateform.uid + '/' + this.rateform.nid)
					.then(function(response) {
						_this.rateform.score = response.data.data.score
						_this.rateform.id = response.data.data.id
					})
					.catch(function(error) {
					});
			},
			loadCollection(){
				const _this = this
				this.collectionform.uid = parseInt(localStorage.getItem("id"))
				this.collectionform.nid = this.$route.query.id
				axios.get('http://localhost:8888/Collection/findByUidAndNid/' + this.collectionform.uid + '/' + this.collectionform.nid)
					.then(function(response) {
						_this.collectionform.collection = response.data.data.collection
						_this.collectionform.id = response.data.data.id
						if(_this.collectionform.collection==1){
							_this.isCollect = true
						}else{
							_this.isCollect = false
						}
					})
					.catch(function(error) {
					});
			},
			loadNewById(id) {
				const _this = this

				axios.get('http://localhost:8888/News/findById/' + id)
					.then(function(response) {

						_this.singlenew = response.data.data;
					})
					.catch(function(error) {
						console.log(error);
					});

				
			}
		},
		created() {
			this.loadNewById(this.$route.query.id);
			this.loadRate();
			this.loadCollection();
		}

	}
</script>

<style>
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

	.rightspan {
		float: right;
	}
</style>
