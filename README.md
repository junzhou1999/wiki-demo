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