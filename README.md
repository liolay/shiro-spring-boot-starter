shiro-spring-boot-starter
===============================

一、项目说明
-------
1. 为简化Apache Shiro权限框架和SpringBoot整合，本项目为SpringBoot和Shiro整合starter,引用shiro-spring-boot-starter即可自动配置Shiro。

2. 项目默认实现并配置了基于Redis分布式会话管理，所以使用该项目请确保已经添加了spring-boot-starter-data-redis并正确配置了该starter所需的配置项。

3. 项目默认使用`ModularRealmAuthorizer`的另一个实现`ModularSourceRealmAuthorizer`，该实现在多Realm登录时，验证权限时仅从已通过身份验证的Realm获取权限并验证。

   此外， `ModularSourceRealmAuthorizer`提供了获取单个用户所有权限，所有角色的接口。
   如你不需要这些扩展可通过配置自己的`ModularRealmAuthorizer`实现覆盖。
   
4. 项目提供了一个缓存管理`ShiroRealmCacheManager`，该类提供了清除用户认证缓存，权限缓存等静态方法，你可直接调用，这在用户权限和认证信息发生变更时很有用。

二、配置说明
----------
**1.配置文件**

强烈建议使用yml作为配置文件，以下也使用yml作为示例。目前该项目提供可配置的部分配置项如下：

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
  #未登录响应状态码
  invalid-login-code: 418
  #未登录响应状态码
  invalid-permission-code: 401
  #定义系统是否只提供服务
  service-oriented: false
  #登录、登录失败、登出时返回的 json:{key：true/false}的key
  log-in-out-response-key: success
  #自定义filter配置
  filters:
    anon: org.apache.shiro.web.filter.authc.AnonymousFilter
  
  #自定义URL拦截器    
  filter-chain-definitions:
    /** = anon
   ```    
  > _**这里是给出部分配置项，其他配置项参考ShiroAutoConfiguration中配置的类的具体属性。 
  如果你还不熟悉@ConfigurationProperties注解的意思，建议先阅读SpringBoot官方文档。**_   
  
**2.session配置**

项目默认实现并配置了基于Redis的分布式会话管理扩展，以下为会话相关配置项：
```
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
```    
>_**其他配置项参考ShiroAutoConfiguration中配置的类的具体属性。
 请先请确保已经添加了spring-boot-starter-data-redis并正确配置了该starter所需的配置项。**_    

**3.realm配置**  
 
默认情况下没有提供任何realm的实现配置，请根据你自己的需求来配置多个realm。
```
shiro:
 
  #realm配置  
  realms:
    - realm:
        #realm类全路径
        target: org.apache.shiro.realm.jdbc.JdbcRealm
        #设置realm的属性
        property:
          cachingEnabled: true
          authenticationCachingEnabled: true
          authorizationCachingEnabled: true
          authenticationCacheName: usernamePasswordRealm2:authenticationCache
          authorizationCacheName: usernamePasswordRealm2:authorizationCache
        
        #该realm使用的凭证匹配器配置
        credentials-matcher:
          #凭证匹配器全路径
          target: org.apache.shiro.authc.credential.HashedCredentialsMatcher
          property:
            hashAlgorithmName: MD5
            hashIterations: 2
            storedCredentialsHexEncoded: true
```
 
**4.记住我功能配置** 

```
shiro:

  #记住我功能配置  
  remember-me:
    cipher-key: this is ur cipher-key
    cookie:
      name: remember-me
      path: /
      max-age: 31536000
```


**5.自定义Filter**
 
默认覆盖了Shiro提供的`BasicHttpAuthenticationFilter`，`FormAuthenticationFilter`，`HttpMethodPermissionFilter`，
`LogoutFilter`， `PermissionsAuthorizationFilter`，`RolesAuthorizationFilter`，为这些过滤器添加了对AJAX请求的处理。
   
此外，还提供了`AnyRolesAuthorizationFilter`，用于校验用户是否具有角色列表中的任意一个，默认注册的filter名称为：`anyRoles`。
这些预定义的拦截器都定义在了`cn.ocoop.framework.filter.DefaultFilter`中。

如果预定义的filter不能满足你的需求，你也可以注册自定义Filter：
```
   shiro:
       #自定义filter配置
     filters:
       filter名称：filter对应Class全路径
```
>如果你的Filter名称和默认的相同，将会覆盖默认的。
   
   
**6.自定义状态码**   

   你可以为每个Filter自定义未登录(默认：HTTP StatusCode:418)及无权限(默认：HTTP StatusCode:401)情况下的http响应状态码：
```
   shiro:
      #未登录响应状态码
      invalid-login-code: 418
      #未登录响应状态码
      invalid-permission-code: 401
```   
   > _**此处定义的statusCode仅在ajax或`shiro.service-oriented:true`请求时有效，因为普通http请求通常会跳转到配置的login-url和success-url**_
    
**7.登录成功/失败、登出操作**


```
    shiro:
        log-in-out-response-key: success
```
   当你登录/登出系统时，系统会自动响应客户端请求内容，通过设定的`log-in-out-response-key`来响应操作结果,响应结果为json格式，如：
   * 登录：响应`{"success":true}`表示登录成功
   * 登录：响应`{"success":false}`表示登录失败
   * 登出：响应`{"success":true}`表示登出成功


    
**8.其他配置**
```
    shiro:
        service-oriented:true/false(default)
```         
   这个配置项对于只提供服务/前后端分离的系统很有帮助，因为这些系统并不需要返回页面。
   当你开启这个配置项时`login-url`，`success-url`，`unauthorized-url`将变得没有任何意义，因为Shiro的Filter不会发生重定向，
   取而代之的是通过状态码及具体的内容来响应调用方。
   
