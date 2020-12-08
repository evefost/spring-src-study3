## 1.依赖
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
    </dependency>
## 2.配置
    context xml 配置
    bean 注入注解 Component、Service、Controller、Repository
## 3.Context 启动过程
    1.读取xml配置文件路径
    2.准备环境 Environment 
    3.创建beanFactory
    4.解释xml配置
    5.遍历BeanFactory里所有的BeanDefinition 创建Bean 实例
    
## spring 自定义标签解释过程
    1.读取xml文件XmlBeanDefinitionReader
    2.创建 NamespaceHandlerResolver，初始化命名空间与NamespaceHandler 关系
    3.读取某个元素命名空间
    4.判断元素对应的命名空间是自定义还默认的:BeanDefinitionDocumentReader
    5.据标签命名空间，找到对应的命名空间解释器 NamespaceHandler
    6.根据标签名称获取对应  BeanDefinitionParser(如ComponentScanBeanDefinitionParser)
    7.解释标签信息，并生成beandefinition 注入beanFactory   
## 应用实践示例
    接口化发送消息
      