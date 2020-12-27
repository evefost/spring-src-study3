## 1.依赖
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>
## 2.tomcat 基本配置
    web.xml
    ContextLoaderListener RootContext
    DispatcherServlet
    
## 3.tomcat启动
    ContextLoaderListener:contextInitialized 初始RootContext
    DispatcherServlet:init()初始ServletContext
## 4 spring-mvc 初始化
    ServletContext 据配置,解释spring-mvc标签，初始化,mvc相关实例
    DispatcherServlet:处理请求   