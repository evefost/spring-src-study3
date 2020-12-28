## 1.依赖
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>
## 2.tomcat 基本配置
    web.xml
    ContextLoaderListener RootApplicationContext
    DispatcherServlet
    
## 3.tomcat启动
    ContextLoaderListener:contextInitialized 初始RootContext
    DispatcherServlet:init()初始ServletContext
## 4 spring-mvc 初始化
    ServletApplicationContext 加载配置，生成bean 实例
    MvcNamespaceHandler 解释spring-mvc标签，初始化,mvc相关实例
    DispatcherServlet:处理请求
    入参处理器 HandlerMethodArgumentResolver
    出参处理器 HandlerMethodReturnValueHandler   