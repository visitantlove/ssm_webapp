[TOC]

# SSM框架结构

### SSM主要配置

参考http://elf8848.iteye.com/blog/875830

Mybatis中文文档http://www.mybatis.org/mybatis-3/zh/configuration.html

### 核心类与接口

1. DispatcherServlet: 前置控制器

2. HandlerMapping接口: 处理请求的映射

   HandlerMapping的实现类:

   SimpleUrlHandlerMapping 通过配置文件，把一个URL映射到Controller

   DefaultAnnotationHandlerMapping  通过注解，把一个URL映射到Controller类

3. HandlerAdapter接口: 处理请求的映射

   HandlerAdapter的实现类:

   AnnotationMethodHandlerAdapter 通过注解，把一个URL映射到Controller类的一个方法上

4. Controller接口: 控制器

   可通过使用注解@Controller代替

5. HandlerInterceptor接口: 拦截器

   自定义拦截器继承此接口或继承类HandlerInterceptorAdapter， 主要有以下方法:

   1. Action之前执行  public boolean preHandle(HttpServletRequest request,
         HttpServletResponse response, Object handler);
   2. 生成视图之前执行
       public void postHandle(HttpServletRequest request,
         HttpServletResponse response, Object handler,
         ModelAndView modelAndView);
   3. 最后执行，可用于释放资源
       public void afterCompletion(HttpServletRequest request,
         HttpServletResponse response, Object handler, Exception ex)

   在preHandle中，可进行编码、安全控制等处理

   在postHandle中，有机会修改ModelAndView

   在afterCompletion中，可根据ex是否为null判断是否发生异常，进行日志记录

6. ViewResolver接口实现类:

   UrlBasedViewResolver 通过配置文件，把一个视图名交给一个view来处理

   InternalResourceViewResolver 比UrlBasedViewResolver多加入了JSTL支持

7. HandlerExceptionResolver接口: 异常处理

   实现类:

   SimpleMapingExceptionResolver

### 1、web.xml

1. 配置前端控制器

```xml
<!DOCTYPE web-app PUBLIC
        "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
        "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
  
  <display-name>ssm-web-app</display-name>
  <!-- 全局范围内环境参数初始化 -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:conf/spring-mybatis.xml</param-value>
  </context-param>

  <context-param>
    <param-name>log4jConfigLocation</param-name>
    <param-value>classpath:log4j.properties</param-value>
  </context-param>
  
  <!-- 编码过滤器 -->
    <filter>
      <filter-name>encodingFilter</filter-name>
      <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
      <!-- 用来定义参数，可在Servlet中使用getServletContext().getInitParamter(param-name)取值 -->  
      <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
  
  <!-- spring监听器 -->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  
  <!-- 防止spring内存溢出监听器，比如quartz -->
  <listener>
    <listener-class>org.springframework.web.util.IntrospectorCleanupListener</listener-class>
  </listener>
  
  <servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 指明配置文件 -->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath*:conf/spring-mvc.xml</param-value>
    </init-param>
    <!-- 启动顺序，1表示此Servlet随容器启动 -->
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
  </servlet>
  <servlet-mapping>
    <servlet-name>SpringMVC</servlet-name>
    <!-- 此处表示拦截的请求形式，也可以配置成 / 形式 -->
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>
  
  <!-- 定义首页 -->
  <welcome-file-list>
    <welcome-file>/index.jsp</welcome-file>
  </welcome-file-list>
  
  <!-- 配置错误页面 -->  
  <!--将错误代码(Error Code)或异常(Exception)的种类对应到web应用资源路径.-->
  <error-page>  
    <!--HTTP Error code,例如: 404、403-->
    <error-code>404</error-code>        
    <!--用来设置发生错误或异常时要显示的页面-->
    <location>error.html</location>           
  </error-page>  
  <error-page>  
    <!--设置可能会发生的java异常类型,例如:java.lang.Exception-->
    <exception-type>java.lang.Exception</exception-type>  
    <!--用来设置发生错误或异常时要显示的页面-->
    <location>ExceptionError.html</location>              
  </error-page>  
  
  <!-- session配置 -->
  <session-config>
    <session-timeout>15</session-timeout>
  </session-config>
  
</web-app>
```

其中:

+ 配置的加载顺序: ServletContext >> context-param >> listener >> filter >> servlet >>  spring
+ `<param-value>**.xml</param-value>`此处有多种写法, 多个值用逗号分隔:
  1. 不写，默认`/WEB-INF/<servlet-name>-servlet.xml`
  2. `<param-value>/WEB-INF/classes/springMVC.xml</param-value>`
  3. `<param-value>classpath*:springMVC-mvc.xml</param-value>`

### 2、spring-mvc.xml

1. 配置 `<mvc:annotation-driven />`
2. 配置controller的注入 `<context:component-scan base-package="com.springmvc.controller"` 
3. 配置视图解析器

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans  
    xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:tx="http://www.springframework.org/schema/tx"  
    xmlns:context="http://www.springframework.org/schema/context"    
    xmlns:mvc="http://www.springframework.org/schema/mvc"    
    xsi:schemaLocation="http://www.springframework.org/schema/beans   
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd   
    http://www.springframework.org/schema/tx   
    http://www.springframework.org/schema/tx/spring-tx-3.0.xsd  
    http://www.springframework.org/schema/context  
    http://www.springframework.org/schema/context/spring-context-3.0.xsd  
    http://www.springframework.org/schema/mvc  
    http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">  
  
    <!-- 自动扫描的包名 -->  
    <context:component-scan base-package="com.app,com.core,JUnit4" ></context:component-scan>  
      
    <!-- 配置映射器与适配器，默认注解支持 -->  
    <mvc:annotation-driven />  
      
    <!-- 视图解释类 -->  
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">  
        <property name="prefix" value="/WEB-INF/jsp/"/>  
        <property name="suffix" value=".jsp"/><!--可为空,方便实现自已的依据扩展名来选择视图解释类的逻辑  -->  
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />  
    </bean>  
      
    <!-- 拦截器 -->  
    <mvc:interceptors>  
        <bean class="com.core.mvc.MyInteceptor" />  
    </mvc:interceptors>       
      
    <!-- 对静态资源文件的访问  方案一 （二选一） -->  
    <mvc:default-servlet-handler/>  
      
    <!-- 对静态资源文件的访问  方案二 （二选一）-->  
    <mvc:resources mapping="/images/**" location="/images/" cache-period="31556926"/>  
    <mvc:resources mapping="/js/**" location="/js/" cache-period="31556926"/>  
    <mvc:resources mapping="/css/**" location="/css/" cache-period="31556926"/>  
  
    <!--避免IE执行AJAX时，返回JSON出现下载文件 -->
    <bean id="mappingJacksonHttpMessageConverter"
          class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
        <property name="supportedMediaTypes">
            <list>
                <value>text/html;charset=UTF-8</value>
            </list>
        </property>
    </bean>
  
    <!-- 文件上传配置 -->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 默认编码 -->
        <property name="defaultEncoding" value="UTF-8"/>
        <!-- 上传文件大小限制为31M，31*1024*1024 -->
        <property name="maxUploadSize" value="32505856"/>
        <!-- 内存中的最大值 -->
        <property name="maxInMemorySize" value="4096"/>
    </bean>
</beans>   
```

其中:

+ `<context:component-scan/>` 扫描指定的包中的注解，常用注解:
  1. @Controller 声明Action组件
  2. @Service 声明Service组件
  3. @Repository 声明Dao组件
  4. @Component 泛指组件，不好分类时使用
  5. RequestMapping("/xxx") 请求映射
  6. @Resource 用于注入，默认按名称装配，@Resource(name="beanName")
  7. @Autowired 用于注入，默认按类型装配
  8. @Transactional(rollbackFor={Exception.class}) 事务管理
  9. @ResponseBody
  10. @Scope("prototype") 设定bean的作用域
+ `<mvc:annotation-driven /> `会自动注册DefaultAnnotationHandlerMapping 与 AnnotationMethodHandlerAdapter 两个bean，是spring MVC 为@Controllers分发请求所必须的。并提供
  + 数据绑定支持
  + @NumberFormatannotation支持
  + @DateTimeFormat支持
  + @Valid支持
  + 读写XML的支持(JAXB)
  + 读写JSON的支持(Jackson)
+ `<mvc:interceptors/>`是一种简写形式，会为每个HandlerMapping注入一个拦截器。
+ `<mvc:default-servlet-handler/> `使用默认的Servlet来响应静态文件。
+ `<mvc:resources mapping="/images/**" location="/images/" cache-period="31556926"/> `匹配URL  /images/**  的URL被当做静态资源，由Spring读出到内存中再响应http。

### 3、spring-mybatis.xml

1. 自动扫描配置 `context:component-scan base-package="com.springmvc"`
2. 加载数据资源属性文件
3. 配置数据源
4. 配置Sessionfactory
5. 装配Dao接口
6. 声明式事务管理
7. 注解事务切面

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:aop="http://www.springframework.org/schema/aop"  
    xmlns:context="http://www.springframework.org/schema/context"  
    xmlns:tx="http://www.springframework.org/schema/tx"  
    xsi:schemaLocation="http://www.springframework.org/schema/beans  
        http://www.springframework.org/schema/beans/spring-beans-4.1.xsd  
        http://www.springframework.org/schema/tx  
        http://www.springframework.org/schema/tx/spring-tx-4.1.xsd  
        http://www.springframework.org/schema/aop  
        http://www.springframework.org/schema/aop/spring-aop-4.1.xsd  
        http://www.springframework.org/schema/context  
        http://www.springframework.org/schema/context/spring-context-4.1.xsd">  
  <!--1 自动扫描 将标注Spring注解的类自动转化Bean-->  
  <context:component-scan base-package="com.xxx" />  
  <!--2 加载数据资源属性文件 -->  
  <bean id="propertyConfigurer"  
    class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">  
    <property name="location" value="classpath:jdbc.properties" />  
  </bean>  
  <!-- 加载多个properties文件
    <bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath:jdbc.properties</value>
                <value>classpath:common.properties</value>
            </list>
        </property>
        <property name="fileEncoding" value="UTF-8"/>
    </bean>
    <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer">
        <property name="properties" ref="configProperties"/>
    </bean>
    -->
  
  <!-- 3 配置数据源 -->
  <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"  
    destroy-method="close">  
    <property name="driverClassName" value="${driver}" />  
    <property name="url" value="${url}" />  
    <property name="username" value="${username}" />  
    <property name="password" value="${password}" />  
    <!-- 初始化连接大小 -->  
    <property name="initialSize" value="${initialSize}"></property>  
    <!-- 连接池最大数量 -->  
    <property name="maxActive" value="${maxActive}"></property>  
    <!-- 连接池最大空闲 -->  
    <property name="maxIdle" value="${maxIdle}"></property>  
    <!-- 连接池最小空闲 -->  
    <property name="minIdle" value="${minIdle}"></property>  
    <!-- 获取连接最大等待时间 -->  
    <property name="maxWait" value="${maxWait}"></property>  
  </bean>  
  
  <!-- 4   配置sessionfactory -->  
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">  
    <property name="dataSource" ref="dataSource" />  
    <!-- 自动扫描mapping.xml文件 -->  
    <property name="mapperLocations" value="classpath:mapping/*.xml"></property>  
  </bean>  
  
  <!-- 5  装配dao接口,DAO接口所在包名，Spring会自动查找其下的类  -->  
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
    <property name="basePackage" value="com.xxx.dao" />  
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>  
  </bean>  
  
  <!-- 6、声明式事务管理 -->  
  <bean id="transactionManager"  
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
    <property name="dataSource" ref="dataSource" />  
  </bean>  
  <!-- 7、注解事务切面 -->
  <tx:annotation-driven transaction-manager="transactionManager"/>  
  
</beans>
```

