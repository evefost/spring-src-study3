#spring标签

##一、自定义spring标签

1. 创建定义标签文件:spring-test.xsd,设定命名空间：targetNamespace="http://www.lexueba.com/schema/user" 
2. 创建schemas文件,指定命名空间uri指向xsd文件：spring.schemas 
    http\://www.lexueba.com/schema/user.xsd=META-INF/spring-test.xsd
3. spring xml 通过命名空间引用xsd 定义的签标
4. 指定命名空间处理器：spring.handlers  放到META-INF/spring.handlers
    http\://www.lexueba.com/schema/user=MyNamespaceHandler
    DefaultNamespaceHandlerResolver 类读取spring.handlers
    
    
##二、spring标签解释过程

1.spring启动

AbstractApplicationContext  
  
    public void refresh() throws BeansException, IllegalStateException {
            Object var1 = this.startupShutdownMonitor;
            synchronized(this.startupShutdownMonitor) {
                this.prepareRefresh();
                //加载beanfactory,读取资源文件
                ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
                this.prepareBeanFactory(beanFactory);
                try {
                    this.postProcessBeanFactory(beanFactory);
          
 
2.读取xml资源文件    
  
XmlBeanDefinitionReader

    public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
        BeanDefinitionDocumentReader documentReader = this.createBeanDefinitionDocumentReader();
        int countBefore = this.getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(doc, this.createReaderContext(resource));
        return this.getRegistry().getBeanDefinitionCount() - countBefore;
    }
3.选择解释spring 自带或 自定义标签 

DefaultBeanDefinitionDocumentReader

    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
        if(delegate.isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();

            for(int i = 0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                if(node instanceof Element) {
                    Element ele = (Element)node;
                    if(delegate.isDefaultNamespace(ele)) {
                        //解释spring 自带标签
                        this.parseDefaultElement(ele, delegate);
                    } else {
                        //解释自定义标签
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        } else {
            delegate.parseCustomElement(root);
        }

    }
    
4. 获取对应的NamespaceHandler并传入xml element,让其解释生成BeanDefinition

BeanDefinitionParserDelegate
  
      public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
          String namespaceUri = this.getNamespaceURI(ele);
          //根据命名空间，获取对应handler
          NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
          if(handler == null) {
              this.error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
              return null;
          } else {
               //执自parse方法(自定义标签)，最终由相应的NamespaceHanller处理并返回BeanDefinition
              return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
          }
      }
      
5.读取命名空间对应NamespaceHandler
(命名空间处理器处kev,value 放在：META-INF/spring.handlers 文件，
      
DefaultNamespaceHandlerResolver

    private Map<String, Object> getHandlerMappings() {
        if(this.handlerMappings == null) {
            synchronized(this) {
                if(this.handlerMappings == null) {
                    try {
                        //this.handlerMappingsLocation 为:META-INF/spring.handlers,所以自定标签文件必须放在这个里
                        Properties ex = PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
                        if(this.logger.isDebugEnabled()) {
                            this.logger.debug("Loaded NamespaceHandler mappings: " + ex);
                        }

                        ConcurrentHashMap handlerMappings = new ConcurrentHashMap(ex.size());
                        CollectionUtils.mergePropertiesIntoMap(ex, handlerMappings);
                        this.handlerMappings = handlerMappings;
                    } catch (IOException var5) {
                        throw new IllegalStateException("Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", var5);
                    }
                }
            }
        }

        return this.handlerMappings;
    }
    
 6.通過namespaceUrl 找到 NamespaceHandler 并生成实例反回
    
    public NamespaceHandler resolve(String namespaceUri) {
        Map handlerMappings = this.getHandlerMappings();
        Object handlerOrClassName = handlerMappings.get(namespaceUri);
        if(handlerOrClassName == null) {
            return null;
        } else if(handlerOrClassName instanceof NamespaceHandler) {
            return (NamespaceHandler)handlerOrClassName;
        } else {
            String className = (String)handlerOrClassName;

            try {
                Class err = ClassUtils.forName(className, this.classLoader);
                if(!NamespaceHandler.class.isAssignableFrom(err)) {
                    throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri + "] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
                } else {
                //生成实例
                    NamespaceHandler namespaceHandler = (NamespaceHandler)BeanUtils.instantiateClass(err);
                    namespaceHandler.init();
                    handlerMappings.put(namespaceUri, namespaceHandler);
                    return namespaceHandler;
                }
            } catch (ClassNotFoundException var7) {
                throw new FatalBeanException("NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "] not found", var7);
            } catch (LinkageError var8) {
                throw new FatalBeanException("Invalid NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "]: problem with handler class file or dependent class", var8);
            }
        }
    } 
       
 7.自定义NamespaceHandler
       
     public class MyNamespaceHandler extends NamespaceHandlerSupport {
     
         public void init() {
             //如果有多种不种标签，都可以在这里注册（自定义标签 user,other）
             registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
             registerBeanDefinitionParser("other", new OtherBeanDefinitionParser(Other.class));
     
         }
     
     }
     
  8.自定义BeanDefinitionParse 处理定义标签
  
  
      public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
      
          protected Class getBeanClass(Element element) {
              return User.class;
          }
      
          protected void doParse(Element element, BeanDefinitionBuilder builder) {
              //解释定义userName元素
              String userName = element.getAttribute("userName");
              String email = element.getAttribute("email");
              if (StringUtils.hasText(userName)) {
                  builder.addPropertyValue("userName", userName);
              }
              if (StringUtils.hasText(email)) {
                  builder.addPropertyValue("email", email);
              }
          }
      }
      
  9.标签xsd 文件
  
      <?xml version="1.0" encoding="UTF-8"?>
      <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                 targetNamespace="http://www.lexueba.com/schema/user"
                 elementFormDefault="qualified">
          <!--
              schema xmlns:targetNamespace
              targetNamespace :1表示文档中要定义的元素来自什么命名空间
              xmlns:表示此文档的默认命名空间是什么
              elementFormDefault="qualified": 表示要求xml文档的每一个元素都要有命名空间指定
              -->
          <xs:element name="user">
              <xs:complexType>
                  <xs:attribute name="id" type="xs:string"/>
                  <xs:attribute name="userName" type="xs:string"/>
                  <xs:attribute name="email" type="xs:string"/>
              </xs:complexType>
          </xs:element>
      
          <xs:element name="school">
              <xs:complexType>
                  <xs:attribute name="id" type="xs:string"/>
                  <xs:attribute name="name" type="xs:string"/>
                  <xs:attribute name="address" type="xs:string"/>
                  <xs:attribute name="students" type="xs:int"/>
              </xs:complexType>
          </xs:element>
      
          <xs:element name="other">
              <xs:complexType>
                  <xs:attribute name="id" type="xs:string"/>
                  <xs:attribute name="className" type="xs:string"/>
              </xs:complexType>
          </xs:element>
      
      </xs:schema>
     
   10.在spring引用
   
       <?xml version="1.0" encoding="UTF-8"?>
       <beans xmlns="http://www.springframework.org/schema/beans"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:xie="http://www.lexueba.com/schema/user"
              xmlns:sc="http://www.lexueba.com/schema/school"
              xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
              http://www.lexueba.com/schema/user
              http://www.lexueba.com/schema/user.xsd
              http://www.lexueba.com/schema/school
              http://www.lexueba.com/schema/school.xsd">
           <!--注意，命名空间定义的引用
             在使用XML Schema 文档对XML实例文档进行检验，除了要声明名称空间外
             (xmlns="http://www.springframework.org/schema/beans")，
             还必须指定该名称空间所对应的XML Schema文档的存储位置.
             通过schemaLocation属性来指定名称空间所对应的XML Schema文档的存储位置，
             它包含两个部分,一部分是名称空间的URI,第二部分就是该名称空间所标识的XML（key,value）
             Schema文件位置或URL地址xsi:schemaLocation="http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans-2.5.xsd）
           -->
           <!--schemaLocation 指定了用于解析和校验xml的定义文件（xsd）的位置。-->
           <xie:user id="testBean" userName="cong" email="mail.163.com"/>
       
           <!--<xie:school id="school" name="shenzheng zhong xue" address="深南大道102" students="123"/>-->
           <xie:other className="com.xie.java.asm.demo1.xsd.defined.demo1.Other"/>
       
           <sc:school id="school" students="45"/>
       </beans>