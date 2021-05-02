## 我的毕业设计记录

### 4.03  找数据集的一晚上（失败）

1. 选定爬取页面（新闻中心滚动新闻）https://news.sina.com.cn/roll/#pageid=153&lid=2512&k=&num=50&page=2

python 2.7.18+pycharm

mysql5.5.15+mysql-front

爬取新闻的结构（标题 时间 内容 来源 栏目）

爬取页面失败，动态页面爬取困难，决定改用数据集。（时间有限，项目重点在于推荐算法而不在于爬取，所以偷点懒）

2. 选用数据集（搜狐新闻文本分类数据集）

数据集：<https://pan.baidu.com/s/1V6o20temK2v3j-bo16x94g> 提取码：fech

数据集格式id，分类，内容，字数

3. 发现数据集（Datawhale推荐系统入门-用户新闻点击数据集）https://www.heywhale.com/mw/project/5fbcbc85d3251d00303cec18/dataset

该数据来自某新闻APP平台的用户交互数据，包括30万用户，近300万次点击，共36万多篇不同的新闻文章，同时每篇新闻文章有对应的embedding向量表示。为了保证比赛的公平性，将会从中抽取20万用户的点击日志数据作为训练集，5万用户的点击日志数据作为测试集A，5万用户的点击日志数据作为测试集B。

### 4.04 搭建框架

#### 思路

​	后台框架采用vue+ springboot +mysql 

#### 完成

1. 进行了一些文件的配置（配置了pom文件和实现了必要的工具类）
2. 定义用户表 字段为id+登录名称+密码+用户类型 0为用户 1为管理员

（使用MyBatisCodeHelperPro进行逆向生成，使用idea自带的database navigation进行数据库操作，报了许多错误，决定明天老老实实跟着how2j的逆向生成）

3. 解决了github pull超时的问题，找到git的安装目录然后找到etc\ssh\sshd_config，将

```
Host github.com

User git

Hostname ssh.github.com

PreferredAuthentications publickey

IdentityFile ~/.ssh/id_rsa

Port 443
```

复制到末尾即可

4. 开始学习spring-boot，创建了简单的helloworld demo

参考：https://snailclimb.gitee.io/springboot-guide/

#### 知识点

1. RESTful Web服务：

区别于MVC开发的关键区别是返回的数据一般以JSON的形式，实现前后端分离开发

2. Lombok

优化java代码提高开发效率。由于目前idea版本过低为2017版不支持lombok，因此重新安装了最新版本的idea。

3. 一些注解
4. `@RestController` **将返回的对象数据直接以 JSON 或 XML 形式写入 HTTP 响应(Response)中。**绝大部分情况下都是直接以 JSON 形式返回给客户端，很少的情况下才会以 XML 形式返回。转换成 XML 形式还需要额为的工作，上面代码中演示的直接就是将对象数据直接以 JSON 形式写入 HTTP 响应(Response)中。关于`@Controller`和`@RestController` 的对比，我会在下一篇文章中单独介绍到（`@Controller` +`@ResponseBody`= `@RestController`）。
5. `@RequestMapping` :上面的示例中没有指定 GET 与 PUT、POST 等，因为**`@RequestMapping`默认映射所有HTTP Action**，你可以使用`@RequestMapping(method=ActionType)`来缩小这个映射。
6. `@PostMapping`实际上就等价于 `@RequestMapping(method = RequestMethod.POST)`，同样的 ` @DeleteMapping` ,`@GetMapping`也都一样，常用的 HTTP Action 都有一个这种形式的注解所对应。
7. `@PathVariable` :取url地址中的参数。`@RequestParam ` url的查询参数值。
8. `@RequestBody`:可以**将 HttpRequest body 中的 JSON 类型数据反序列化为合适的 Java 类型。**
9. `ResponseEntity`: **表示整个HTTP Response：状态码，标头和正文内容**。我们可以使用它来自定义HTTP Response 的内容。

### 4.05 爬取数据并进行数据清理

爬取对象https://www.chinanews.com/scroll-news/news1.html中国新闻网

#### 1.首先建立数据库

```mysql
CREATE TABLE `news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '新闻主键',
  `news_title` varchar(255) DEFAULT '' COMMENT '新闻标题',
  `news_content` text COMMENT '新闻内容',
  `type_id` int(11) DEFAULT NULL COMMENT '新闻类型',
  `news_creatTime` varchar(20) DEFAULT NULL COMMENT '新闻发布时间',
  `news_recourse` varchar(20) DEFAULT NULL COMMENT '新闻来源',
  `news_link` varchar(255) DEFAULT NULL COMMENT '新闻链接',
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COMMENT='新闻信息表';
```

#### 2.主要模块

1. 对于主页面（也就是所有新闻的展示页面）爬取每一条新闻的类别和链接

```python
links = []
def getLink(baseurl):    #baseurl要爬取的页面
    html = requests.get(baseurl, headers=hea)
    html.encoding = 'utf8'
    soup = BeautifulSoup(html.text, 'html.parser')
    for item in soup.select('div.content_list > ul > li'):
        # 对不符合的数据进行清洗
        if (item.a == None):
            continue
        data = []
        type = item.div.text[1:3]  # 类型
        link = item.div.next_sibling.next_sibling.a['href'] #链接
        data.append(type)
        data.append(link)
        links.append(data)
```

2. 根据每一条新闻的链接，获取单条新闻的详细信息。

```python
links = []     # links[0]为类别 links[1]为链接
def getInformationAndSave():
    for link in links:
        url = "https://www.chinanews.com" + link[1]   #每一条新闻的点击链接
        # 获取网页信息
        cur_html = requests.get(url, headers=hea)
        cur_html.encoding = "utf8"
        soup = BeautifulSoup(cur_html.text, 'html.parser')
        # 获取时间
        title = soup.find('h1')
        title = title.text.strip()
        # 获取时间和来源
        tr = soup.find('div', class_='left-t').text.split()
        time = tr[0] + tr[1]
        recourse = tr[2]
        # 获取内容
        cont = soup.find('div', class_="left_zw")
        content = cont.text.strip()
        print(link[0] + "---" + title + "---" + time + "---" + recourse + "---" + url)
        # 存入数据库
        saveDate(title,content,time,recourse,url)
```

3. 将数据存入数据库

```python
def saveDate(title,content,time,recourse,url):
    try:
        cursor.execute("INSERT INTO news(news_title, news_content, type_id, news_creatTime, news_recourse,news_link) VALUES ('%s', '%s', '%s', '%s', '%s' ,'%s')" % \
          (title, content, 1, time, recourse,url))      
        #此处的1为新闻所属类型 
        #如爬取https://www.chinanews.com/china.shtml国内类别的，可设置为1；
        #爬取https://www.chinanews.com/society.shtml社会类别的，可设置为2；
        db.commit()
        print("执行成功")
    except:
        db.rollback()
        print("执行失败")
```

#### 3.不足点

​	爬取不同类别的网页还需要手动修改baseurl以及对应的插入数据库时的type_id

### 4.06

#### 1.根据数据库进行了逆向创建，完成了新闻，新闻类别，用户的增删改查的后端接口

#### 2.学习Vue+SpringBoot前后端分离开发方式。 

1. 前端只需要独立编写客户端代码，后端也只需要独立编写服务端代码提供数据接口即可。
2. 前端通过Ajax请求来访问后端的数据接口，将Model展示到View中即可。

### 4.07-4.08

1.前端：学习vue，利用elmentui搭建前端的后台页面，设置动态路由导航栏，实现前后端分离开发的数据对接

2.后端：完成各个模块的增删改查以及分页的接口，利用idea自带的插件restservices测试各个接口的有效性。

### 4.09-4.11

#### 完成

设计并实现了前端的页面，包括路由的跳转，所有新闻的分页展示以及单个新闻的具体内容显示。

#### 未完成

1.登录注册	2.评分收藏功能的后端接口	3.用户行为数据集的处理	4.后台管理的部分功能	

完成以上就可以进行推荐算法的学习了。

#### 不足

1. 前端是真的花时间，虽然这几天的确没有花心思在毕业设计上。主要原因还是在于自己是边想边做，没有一个现成的原型来参考，并且给我参考我也不太会，只能根据自身现有水平一点点去查去修改。
2. 对于前端的样式太过执着，花费太多的时间在调整css样式上面，后面应尽快搭建系统，抛去细节。

### 4.12 计划完成

前端

- [ ] 1. 登录注册功能
- [ ] 2. 前端管理部分功能（用户的收藏点赞）

后端

- [ ] 1. 学习rpc实现java调用python服务
- [ ] 2. 实现协同过滤算法
- [ ] 3. 评分收藏功能以及推荐功能的后端接口实现
- [ ] 4. 在协同过滤算法的基础上改进K-means算法

### 4.14 

基于内容的推荐算法

​		基于内容的推荐算法的主要优势在于无冷启动问题，只要用户产生了初始的历史数据，就可以开始进行推荐的计算。而且随着用户的浏览记录数据的增加，这种推荐一般也会越来越准确。

### 4.20

k-means聚类的工作流程

1. 先设置初始值k，代表将数据分成几个簇，假定k=2
2. 然后会随机初始化两个点（作为质心），遍历每一个点到这两个质心的距离
3. 每个点比较到两个质心的距离，归入到距离较近的质心的那一个类别
4. 将所有点都分完类别之后，对两个类别横纵坐标分别求平均数，作为最新质心的位置
5. 重新遍历计算重新归类
6. 重新更新质心
7. 循环直到无法更新



针对文本数据如何处理如何聚类

1. 删除停用词（文本清洗）：语料中大量出现，对新闻主要内容无影响

2. TF-IDF（词频-逆文档频率）（提取关键词）：词频相同下，越不常见重要性越高，更能反应文章的特性

   词频TF = 某个词在该文章中出现的次数/该文章的总词数

   逆文档频率IDF = log（ 语料库的文档总数 / （包含该词的文档数+1））

   TF-IDF = TF * IDF

3. 文本分析：分词，文本数据清洗，提取特征（语料库），提取TF-IDF，构造TF-IDF向量

4. 相似度计算（余弦相似度）：

   ![1618879835561](C:\Users\14213\AppData\Roaming\Typora\typora-user-images\1618879835561.png)

### 5.02 项目到此为止了

完成了新闻推荐系统的基本功能包括kmeans推荐，基于用户和物品的协同过滤推荐。