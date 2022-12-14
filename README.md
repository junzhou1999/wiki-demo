# 一个wiki的SpringBoot+Vue的知识库系统

### 1-1
环境：Spring2.7.1 + PostgreSQL 12.11 + JDK 1.8
* 初始化项目

### 1-2
* 添加日志输出和写入文件配置

### 1-3
* 修改启动文案

### 1-4
* 修改启动图案

### 1-5
* 开发一个hello接口  
##### 要点：
* @SpringBootApplication只会扫描所在包以及其子包下的包，  
  如果要扫描其他包下，加@ComponentScan注解
* http请求404代表服务不可达，405代表方法Method禁用

### 1-6
* 增加hello接口测试test.http  
##### 要点：  

---

* @PathVariable，汉语意思是：路径变量。顾名思义，就是要获取一个url 地址中的一部分值  
  如：http://localhost:5921/hello/post/scott，把值scott传给函数。
    * @PostMapping(value = "XXX/XXX/{id}")
    * 方法名和需要绑定的url中变量名称一致，简写 @PathVariable Long id
    * 若不一致，写成 @PathVariable("id") Long id2
* @RequestParam，汉语意思就是： 请求参数。顾名思义，就是获取参数key对应的value。
    * 前端：http://XXX/XXX?id=123
    * 后端：@PostMapping(value = "XXX/XXX")
* 以上为json传输方式

---

### 1-7
* 增加自定义配置项
##### 要点：  
* SpringBoot会识别以下几组文件
    * classpath:application.properties[.yml]
    * classpath:config/application.properties[.yml]
* SpringCloud还会识别以下几组文件    
    * classpath:bootstrap.properties[.yml]
    * classpath:config/bootstrap.properties[.yml]    
    
### 1-8
* 集成热部署  
##### 要点：
* pom添加依赖
* idea勾选Settings --》Compiler --》Build project automatically（应用启动前的编译）
* 双击shift找到Registry的Action，勾选compiler.automake.allow.when.app.running（启动后的编译）
* 想要让运行中的应用立马生效，直接Build Project

### 2-1
* 集成mybatis依赖
* 配置PostgreSQL数据库作为数据源
* 开发了/test/list接口

### 2-2
* 集成Mybatis generator官方生成器生成持久层代码
##### 要点：
* 配置好generator-config配置文件后，新建maven的启动项，  
  命令是mybatis-generator:generate -e
  
### 2-3
* 用Http Client测试使用generator生成的代码
##### 要点：
* POST请求的请求体以JSON的格式传输请求体可以写在路径，也可以写到外边。  
  需要注意后端的接收处理。（用不同注解来匹配格式）
* 数据库连接stringtype=unspecified用于把DEMO表中的enum类型当string类型存储  

### 3-1
* 新增Ebook表，增加持久层代码。

### 3-2
* 完成电子书列表查询接口

### 3-3
* 增加通用返回类CommonResp，以后后端接口返回值都是CommonResp类型

### 3-4
* 封装请求参数(request)和返回参数(response)，实现Ebook表的模糊查询
##### 要点：
* domain下的类是和数据库数据映射的
* req包下的类是用于处理前端发来的请求信息
* resp包下的类是用于处理前端发来的请求信息后返回给前端的信息，  
  有些信息是不需要全部返回给前端的或者会返回更多的一些信息
* controller层不要看到/domain/Ebook实体类

### 3-5
* 制作CopyUtil封装BeanUtils，实现对象或者对象集合的复制
##### 要点：
* 需要了解：反射基础，泛型。
* static不属于类对象的一部分，需要以下面的形式声明泛型方法使用泛型  
  public static **\<T>**  List\<T> func() { }
* 获取运行时类的实例（无参构造）：class.getInstance()  

### 4-1
* 使用Vue CLI生成web项目
##### 要点：
* Vue CLI = Vue + 一堆插件
* 使用nodejs改镜像地址和安装Vue CLI
    * npm get registry
    * npm config set registry http://registry.npm.taobao.org
    * npm install -g @vue/cli
* public/index.html：Vue框架的主页：
* **src**/main.ts：初始启动配置文件，Vue CLI通过加载这个文件，  
  把App.vue渲染到index.html，完成页面显示
* package.json：模块配置信息和项目配置

### 4-2
* 集成Ant Design View，加入按钮示例
##### 要点：
* 安装最新版的Ant Design View：在web目录下：npm install ant-design-vue --save  
  我安装的版本是3.2.10

### 4-3
* 网站首页布局开发

### 4-4
* 首页路由开发，关闭vue/no-unused-components校验
##### 要点：
* 把通用的，静态不变的代码块如：header和footer放到App.vue，  
  其他不同页面的放到各自页面去

### 4-5
* 把通用的header和footer整合到component，并剔除HelloWorld组件
##### 要点：
* import组件，components加入该组件，template使用组件
* template使用的组件名是组件源码所起的名字

### 5-1
* 集成axios
* npm install axios -- save

### 5-2
* 使用axios调用后端接口/ebook/list
##### 要点：
* setup()：vue3新增的初始化方法，组件加载完后会执行这个方法，
  有的时候页面还没渲染出来就已经执行setup()了，vue2的所有生命周期函数在vue3中都被setup包括了 
* 跨域连接配置

### 5-4
* 新增onMounted()方法
##### 要点：
* mounted(vue2)/onMounted(vue3)：页面渲染后执行的方法
    * 初始化的逻辑操作就放进onMounted里

### 5-5
* 使用reactive实现数据绑定

### 5-6
* 使用ref实现数据绑定

### 5-7
* 添加图标包，并从资源组件网站上把list组件作用于原来的content域

### 5-8
* home页面修改列表显示
* 前端添加和删除各种组件，封面样式调整
* 后端添加动态SQL查询ebook列表

### 5-9
* Vue多环境配置，增加dev和prod环境
* 增加编译和启动项，修改启动端口

### 5-10
* axios请求域改为读配置文件

### 5-11
* 修改axios的baseURL读配置文件

### 5-12
* 配置axios拦截器打印请求参数和返回参数

### 6-1
* 容器增加过滤器，打印接口耗时

### 6-2
* Spring增加拦截器，打印接口耗时
##### 要点：
* 接口一进来会先进到容器（Tomcat），进入过滤器，  
  然后容器发到应用（SpringBoot，Web应用），进到Web应用，  
  执行拦截器，实现业务逻辑
  
### 6-3
* 增加AOP，打印接口耗时，请求参数和返回参数
* 去掉过滤器和拦截器
##### 要点：
* AOP的切点，前置后置环绕通知，切面（切点和通知的组合）

### 7-1
* 添加电子书管理路由和页面

### 7-2
* 修改header导航文字和跳转方式

### 7-3
* 修改电子书管理页面布局

### 7-4
* 列表使用表格显示出来并增加分页逻辑

### 7-5
* 后端使用pagehelper插件实现分页查询
##### 要点：
* startPage(m,n)，m是要查询的页数，n是每页的行数
* 传给前端的一般是数据项，还有数据总行数getTotal()

### 7-6
* 封装分页请求参数PageReq和返回参数PageResp

### 7-7
* 电子书管理前后端分页整合完成

### 7-8
* 首页适配后端，匹配分页请求和返回参数

### 7-9
* 点击编辑按钮时，弹出模态框

### 7-10
* 模态框显示电子书表单，编辑某一行时可以将该行数据显示到表单

### 7-11
* 完成电子书编辑功能
* 将查询接口改为EbookQueryReq和EbookQueryResp
##### 要点：
* axios的post请求是"application/json"方式
* 用json传输的post请求后端处理参数时要加入@RequestBody注解

### 7-12
* 增加雪花算法工具类，完成电子书新增功能

### 7-13
* 增加删除电子书功能，删除之前显示提示框

### 7-14
* 集成Validation，为列表查询接口增加最大页数查询限制

### 7-15
* 为save接口增加非空校验

### 7-16
* 增加按名字查询的功能，不限大小写（postgre的ilike关键字）

### 7-17
* vue添加对象复制工具，解决编辑电子书时会影响响应式变量的问题

### 7-18
* vue3使用新的表格渲染方式

### 8-1
* 分类表和持久层的设计
* 无限级树形表结构id+parent
##### 要点：
* Postgres数据库创建表时如果给表名添加双引号，那么这个表名字段是区分大小写的

### 8-2
* 完成分类管理的基本增删改操作（从电子书复制）

### 8-3
* 前后端代码不进行分页，一次性取出分类管理
##### 要点：
* Ant Design Vue标签前如果不加冒号就是一个普通属性，如果后面加字符串他就是一个字符串
* 如果加上冒号就是一个属性，字符串里面的值会变成一个变量

### 8-4
* 分类表格按树形结构展示
* 使用了递归的算法实现

### 8-5
* 用下拉选择框代替id编号来新增和编辑
* 不能选择自己作为父分类且0为顶级分类

### 8-6
* 分类选择改为级联选择组件

### 8-7 
* 电子书列表显示分类名称

### 8-8
* 首页显示分类菜单
##### 要点：
* a-sub-menu的标签的文字渲染方式是:title
* a-menu-item的标签的文字渲染方式是标签外直接文字

### 8-9
* 加载完分类后再加载电子书，因为axios的请求是异步的
* 否则电子书分类栏的渲染会报错

### 8-10
* 点击导航栏欢迎时，content显示欢迎页面

### 8-11
* 点击导航栏二级分类的目录，content显示对应目录下的电子书

### 9-1
* 增加文档表SQL，并生成持久层代码

### 9-2
* 从分类管理中复制一份代码到文档管理
##### 要点：
* 树形结构的实现是a-table的children属性实现的
* 文档表的路由在电子书表的表格文档编辑按钮进入
* 未解决：选择父文档时，只能选择一级文档作为父文档

### 9-3
* 使用递归算法对文档的新增和编辑进行一些逻辑限制
* 1.不能把本文档作为自身的父文档
* 2.自身的子文档也不可以作为自身的父文档
* 3.新增一个顶级文档选项，unshift在数组头部插入一个元素

### 9-4
* 电子书到文档CRUD页面增加参数传递
* 用于新增文档时获取ebookId属性
##### 要点：
* 属性加个:号代表的是变量，变量用双引号括住，里边的单引号代表字符串

### 9-5
* 删除某棵树下的文档，其子树文档也会删除（前端）

### 9-6
* 文档表删除前再添加一个Modal对话框

### 9-7
* 编辑和新增表单添加WangEditor富文本框
* npm install @wangeditor/editor --save
* npm install @wangeditor/editor-for-vue@next --save

### 9-8
* 生成文档内容表的持久层

### 9-9
* 文档管理的布局改为左右布局
* 删除了Modal对话框，直接在页面对文档进行编辑新增
* 布局两侧还有待完善

### 9-10
* 文档管理布局美化
* 1.布局之间增加间距
* 2.表格显示优化
* 3.用v-if的判断来使得表格初始化时能展开所有的文档

### 9-11
* 增加文档编辑和新增的功能
* 输入的文档以HTML的形式填进数据库

### 9-12
* 修正文档编辑的一些变量的逻辑实现
* 1.点击编辑文档时获取数据库的内容并传到前端
* 2.保存文档后弹出提示
* 3.使用getXXX获取数据库查询结果前要判断非空，确保没有空指针异常

### 9-13
* 首页点击文档名称，会跳转到文档的链接
* 文档页面显示还没有实现

### 9-14
* 主页点击某一门电子书跳转到对应文档页面
* 修复读取文档表一次性读取所有文档的BUG
* 页面功能完善：
* 1.支持文档高亮搜索（Ant Design View API）
* 2.把对应的HTML文本显示到页面上

### 9-15
* 进入电子书编辑页面时，默认显示新增电子书状态
* 包括处理完其他操作之后也是新增电子书状态
##### 要点：
* 解决了刚进入页面父文档下拉选择器没有数据的小问题
* 对象的拷贝使用了lodash插件的深拷贝方法
* 安装方式：npm i --save lodash.clonedeep
* 删除了一次性取出所有文档的接口，使用criteria的条件限制而不是动态查询
* 否则别人写一个空值传进来会一次性取出所有数据

### 9-16
* 修复打开页面新建电子书文档时父文档下拉框没有数据的问题
* 初始时如果当前电子书还没有文档，那么取到的数据为空
* 无论是对象还是数组，（数组也是一种对象），经过copy()返回的是undefined

### 9-17
* 查看文档时样式自定义成wangeditor的样式

### 9-18
* 文档管理增加内容预览功能
* 当选中编辑文档时，文档名称高亮显示

### 10-1
* 用户表设计与持久层代码生成

### 10-2
* 完成用户表的增删改查功能

### 10-3
* 新增校验用户名不能重复，添加自定义异常处理

### 10-4
* 修改用户信息时用户名不能被修改
* 值用!!双引号可以转换成其布尔值，本例中用于绕过类型校验检查

### 10-5
* 前端加密密码用于加密传输
* 后端加密密码用于加密存储

### 10-6
* 编辑用户信息时不显示密码
* 同时后端修改用户信息时不允许修改密码

### 10-7
* 增加单独重置密码功能

### 10-8
* 完成后端登录接口

### 10-9
* 前端加入导航栏登录按键
* 菜单组件使用了flex布局，如果要在菜单末尾加上一个元素  
  那么不能再使用float了，要使用position绝对位置
  
### 10-10
* 更新登录为空的校验规则
* 前端没有校验为空的规则，当输入了空文本，提交的是空字符串【""】
* NotNull代表检验【null】，对象为空
* 如果是NotEmpty，代表检验【null】和空字符串【""】

### 10-11
* 前端对密码输入相关进行优化

### 10-12
* 测试远程redis数据库的连接

### 10-13
* 把登录的token和用户信息存到redis中
##### 要点：
* 前端的互斥显示可以用v-show判断一个变量的布尔值

### 10-14
* 使用vuex全局保存用户登录信息，方便其他组件获取登录信息
* 遗留的问题：刷新后值没有了

### 10-15
* vuex整合了SessionStorage，解决网页刷新数据丢失问题
##### 要点：
* 我们不应该把SessionStorage整合到各个组件上来，应该整合到vuex上

### 10-16
* 增加退出登录功能
* 退出登录时，前端清除store登录信息，后端清除redis登录信息
* 加上登录前后的欢迎信息

### 10-17
* 修补登录处理，避免登录错误时密码栏会显示转换后的密文
* 退出登陆后原输入的密码也要清空

### 10-18
* 前后端增加token验证（用于登录拦截）
* 前端登录后获取后端对这个用户的token信息，
* 访问接口用获取到的token在axios拦截器中给到header的token部分，
* 传给后端，后端才能对这些接口访问进行验证

### 10-19
* 前端未登陆时，隐藏管理菜单

### 10-20
* 前端添加路由拦截，未登录时跳到首页

### 10-21
* 增加自定义登录表单验证
* 需要了解js的正则表达式

### 101-1
* 后端增加静态资源的访问
* 暂时不拦截上传接口，做测试用
* 参考：https://cloud.tencent.com/developer/article/1892774
* addResourceHandler：添加URL响应地址目录。
* addResourceLocations：添加实际资源目录。（这里把资源目录都放到家目录）
* 后续应该会把nginx代理静态资源页面

### 101-2
* 后端增加图片上传接口

### 101-3
* 电子书管理页面匹配后端上传接口
* 参考：https://www.wangeditor.com/v5/menu-config.html#上传图片

### 11-1
* 后端：查看一次文档，文档阅读数加1
* 使用了自定义的SQL语句

### 11-2
* 文档页面显示文档数和阅读数

### 11-3
* 前后端完成点赞功能

### 11-4
* 同一个IP，同一篇文章，一天之内只能点赞一次
* 利用线程本地变量，从程序一开始处理请求就获取远程请求主机的IP
* 并把这些key放进redis中
* 当需要进行点赞时，验证是否符合校验规则

### 11-5
* 使用SpringBoot内置定时器定时更新电子书文档信息
* Postgre的update跟Mysql的有区别
* 使用了cron表达式更新数据
* 定时器使用了单线程的，如果程序执行时间大于间隔时间，
* 那么就会错过下一次执行的任务

### 11-6
* 首页完善电子书信息的显示：文档数，点赞数，阅读数
* v-bind:is=>去创建的vue实例对象的data里找值

### 11-7
* 增加生成日志流水号，方便追踪过滤某一事件

### 11-8
* 后端部署Websocket环境
##### 要点：
* 部署一个websocket可以使得网页(session)和后端保持一个长连接
* 本例中当收到一个点赞时，可以即使获得通知
* 注：Controller是开放HTTP接口
* websocket是开放websocket连接

### 11-9
* 前端部署Websocket环境
##### 要点：
* 因为每个页面都要使用footer组件，所以就在footer写入
* 前端负责生成token

### 11-10
* 点赞时，通知后端使用ws推送点赞信息
* 前端再使用通知提醒框提示用户

### 11-11
* 使用异步化解耦点赞&通知功能
* 注：异步化的实现必须是不同类之间相互调用，
* 不能是同一个类之间不同方法进行调用
* 本例中是DocService调用WsService

### 11-12
* 将业务线程流水号异步传递给异步线程，方便生产运维查问题
* 流水号的获取和新增用MDC类get和put

### 11-13
* 加上事务的注解，实现两个sql语句事务的原子性
* 同样的，事务的注解也要是一个类去调用另一个类，
* 不能同一个类里边调用不同方法

### 11-14
* 使用RocketMQ解耦点赞和通知功能，发送和接收调试成功
* mq流程：点赞->发送->消费->弹出通知
* 在spring这里，既是生产者【消息生产者，负责消息的生产发送】
* 也是消费者【消息消费者，负责消息接收和使用】
* RocketMQ本地启动：
* mqnamesrv.cmd
* mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true (自动创建topic)

### 11-15
* 不使用RocketMQ

### 12-1
* 生成电子快照表和持久层代码

### 12-2
* 增加定时任务，定时收集任务
* Postgre的复杂sql语句够难的
* 经测试可以在mybatis的xml一次查询多个sql语句，不用增加allowMultiQueries=true的连接参数

### 12-3
* 后端获取统计数值的接口

### 12-4
* 欢迎文字提取成组件

### 12-5
* 前端增加数值统计组件，显示统计数值

### 12-6
* 集成echarts-5.3.3，欢迎组件测试示例

### 12-7
* 修改预计阅读的的取值，把今天的数据改为下标0，因为今天的数据肯定是有的

### 12-8
* 后端增加近30天的访问阅读量和点赞量的接口

### 12-9
* 欢迎组件添加30天的折线趋势图和一个时钟仪表盘

### 13-1
* 修改页头跟页尾logo和文案展示

### 13-2
* 增加页面加载文案

### 13-3 
* 后端部署运行到服务器并使用nginx作为代理服务器
* 1.设置生产环境的一些特定设置如数据库密码，可以按照教程
    *  设置可替换初始配置的新配置，
    * 增加启动参数：-Dspring.profiles.active=prod
* 2.自定义打包文件名并修正打包时的一些错误
* 3.nginx的配置文件为：wiki-server.conf

### 13-4
* 前端vue部署运行到服务器并使用nginx作为代理服务器
* 1.设置统一的请求地址前缀为后端的服务器地址
* 2.nginx的配置文件为：wiki.conf
* 注：我的前后端都放到了一台服务器上

### 13-5
* 使用Nginx作为后台静态资源服务器
* 这个目录也是后续资源上传的目录

### 13-6
* 使用统一的文件上传接口

### 13-7
* 文档图片，文档封面使用上传工具类完成前端图片上传工作

### 13-8
* 修复连续点赞导致更新数据库返回空指针的异常

### 13-9
* 文档中的图片资源改为读取Nginx的资源地址

### 13-10
* 完善电子书封面图片的编辑和上传

### 13-11
* 主页默认展开所有的子菜单

### 13-12
* 修复来回切换Echart图表丢失的问题

### 13-13
* 修复前后端Long类型精度丢失的问题
* 后端雪花算法：>16位，前端Number：16位，会导致精度丢失
* 所以全局把Long转成String

### 13-14
* 修正电子书上传图片逻辑以及更新一波插件