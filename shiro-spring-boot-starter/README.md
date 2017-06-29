###shiro-spring-boot-autoconfigure

一、项目说明
-------
1. 为简化Apache Shiro权限框架和SpringBoot整合，本项目为SpringBoot和Shiro整合starter,引用shiro-spring-boot-starter即可自动配置Shiro。

2. 项目默认实现并配置了基于Redis分布式会话管理，所以使用该项目请确保已经添加了spring-boot-starter-data-redis并正确配置了该starter所需的配置项。

3. 项目默认使用ModularRealmAuthorizer的另一个实现ModularSourceRealmAuthorizer，该实现在多Realm登录时，验证权限时仅从已通过身份验证的Realm获取权限并验证。

   此外， ModularSourceRealmAuthorizer提供了获取单个用户所有权限，所有角色的接口。
   如你不需要这些扩展可通过配置自己的ModularRealmAuthorizer实现覆盖。
   
4. 项目提供了一个缓存管理ShiroRealmCacheManager，该类提供了清除用户认证缓存，权限缓存等静态方法，你可直接调用，这在用户权限和认证信息发生变更时很有用。

二、配置说明
-------
1. 强烈建议使用yml作为配置文件，以下也是用yml作为示例。目前该项目提供可配置的配置项如下：
   其他配置项参考ShiroAutoConfiguration中配置的类的具体属性。
   如果你还不熟悉@ConfigurationProperties注解的意思，建议先阅读SpringBoot官方文档。

```
#shiro配置开始
shiro:
  
  #session配置
  session:
    sessionIdCookie:
      name: session-id
      path: /
      max-age: -1
    session-id-cookie-enabled: true
    session-id-url-rewriting-enabled: true
    global-session-timeout: 1800000
    session-cache-name: "shiro:session:"
  
  #realm配置  
  realms:
    #realm类全路径
    - realm:
        target: org.apache.shiro.realm.jdbc.JdbcRealm
        property:
          cachingEnabled: true
          authenticationCachingEnabled: true
          authorizationCachingEnabled: true
          authenticationCacheName: usernamePasswordRealm2:authenticationCache
          authorizationCacheName: usernamePasswordRealm2:authorizationCache
        
        #凭证匹配器配置
        credentials-matcher:
          #凭证匹配器全路径
          target: org.apache.shiro.authc.credential.HashedCredentialsMatcher
          property:
            hashAlgorithmName: MD5
            hashIterations: 2
            storedCredentialsHexEncoded: true


  #记住我功能配置  
  remember-me:
    cipher-key: this is ur cipher-key
    cookie:
      name: remember-me
      path: /
      max-age: 31536000
  
  #登录地址  
  login-url: /login
  #登录成功默认跳转地址
  success-url: /index
  #无权限跳转地址
  unauthorized-url: /unauthorized
  
  #自定义filter配置
  filters:
    anon: org.apache.shiro.web.filter.authc.AnonymousFilter
  
  #自定义URL拦截器    
  filter-chain-definitions:
    /** = anon
   ```    
2. 默认覆盖了Shiro提供的BasicHttpAuthenticationFilter，FormAuthenticationFilter，HttpMethodPermissionFilter，
   LogoutFilter， PermissionsAuthorizationFilter，RolesAuthorizationFilter，为这些过滤器添加了对AJAX请求的处理。
   
   此外，提供了AnyRolesAuthorizationFilter，用于校验用户是否具有角色列表中的任意一个，默认注册的filter名称为：``anyRoles``。
   这些预定义的拦截器都定义在了``cn.ocoop.framework.filter.DefaultFilter``中。
   
   同时，你可以通过为这些类配置statusCode属性来自定义未登录(默认：HTTP StatusCode:418)及无权限(默认：HTTP StatusCode:401)情况下的http响应状态码。
   如果默认的过滤器类无法满足你的需要，你可以通过以下方式定义自己的过滤器：
```
   shiro:
    filters:
        filter名称：filter对应Class全路径
```
         



