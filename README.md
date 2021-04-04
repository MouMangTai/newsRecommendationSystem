## 毕业设计进展

### 4.03  找数据集的一晚上

选定爬取页面（新闻中心滚动新闻）https://news.sina.com.cn/roll/#pageid=153&lid=2512&k=&num=50&page=2

python 2.7.18+pycharm

mysql5.5.15+mysql-front

爬取新闻的结构（标题 时间 内容 来源 栏目）

爬取页面失败，动态页面爬取困难，决定改用数据集。（时间有限，项目重点在于推荐算法而不在于爬取，所以偷点懒）



选用数据集（搜狐新闻文本分类数据集）

数据集：<https://pan.baidu.com/s/1V6o20temK2v3j-bo16x94g> 提取码：fech

数据集格式

id，分类，内容，字数



发现数据集（Datawhale推荐系统入门-用户新闻点击数据集）https://www.heywhale.com/mw/project/5fbcbc85d3251d00303cec18/dataset

该数据来自某新闻APP平台的用户交互数据，包括30万用户，近300万次点击，共36万多篇不同的新闻文章，同时每篇新闻文章有对应的embedding向量表示。为了保证比赛的公平性，将会从中抽取20万用户的点击日志数据作为训练集，5万用户的点击日志数据作为测试集A，5万用户的点击日志数据作为测试集B。

### 4.04 搭建框架

后台框架采用bootstrap + springboot +mysql 

前端采用 thymeleaf

进行了一些文件的配置（配置了pom文件和实现了必要的工具类）

定义用户表 字段为id+登录名称+密码+用户类型 0为用户 1为管理员

（使用MyBatisCodeHelperPro进行逆向生成，使用idea自带的database navigation进行数据库操作，报了许多错误，决定明天老老实实跟着how2j的逆向生成）





