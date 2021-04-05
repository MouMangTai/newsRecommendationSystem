## 毕业设计进展

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

#### 4.明日计划

1. 思考如何设计表来存储用户点击以及标签的设计。
2. 实现后端

### 4.04 搭建框架

#### 思路

​	后台框架采用bootstrap + springboot +mysql 

​	前端采用 thymeleaf

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

1.  `@RestController` **将返回的对象数据直接以 JSON 或 XML 形式写入 HTTP 响应(Response)中。**绝大部分情况下都是直接以 JSON 形式返回给客户端，很少的情况下才会以 XML 形式返回。转换成 XML 形式还需要额为的工作，上面代码中演示的直接就是将对象数据直接以 JSON 形式写入 HTTP 响应(Response)中。关于`@Controller`和`@RestController` 的对比，我会在下一篇文章中单独介绍到（`@Controller` +`@ResponseBody`= `@RestController`）。
2. `@RequestMapping` :上面的示例中没有指定 GET 与 PUT、POST 等，因为**`@RequestMapping`默认映射所有HTTP Action**，你可以使用`@RequestMapping(method=ActionType)`来缩小这个映射。
3. `@PostMapping`实际上就等价于 `@RequestMapping(method = RequestMethod.POST)`，同样的 ` @DeleteMapping` ,`@GetMapping`也都一样，常用的 HTTP Action 都有一个这种形式的注解所对应。
4. `@PathVariable` :取url地址中的参数。`@RequestParam ` url的查询参数值。
5. `@RequestBody`:可以**将 HttpRequest body 中的 JSON 类型数据反序列化为合适的 Java 类型。**
6. `ResponseEntity`: **表示整个HTTP Response：状态码，标头和正文内容**。我们可以使用它来自定义HTTP Response 的内容。



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
