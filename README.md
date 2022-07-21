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