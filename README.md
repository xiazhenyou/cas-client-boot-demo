# cas-client-boot-demo
基于spring boot配置cas客户端
demo分别写了三个请求:拦截请求 test1/index,test1/index1 以及不拦截请求test1/index2,
## 第一步，引入cas 客户端所需包
      <dependency>
            <groupId>net.unicon.cas</groupId>
            <artifactId>cas-client-autoconfig-support</artifactId>
            <version>2.3.0-GA</version>
      </dependency>
## 第二部，配置spring boot 配置文件
```
server:
  port: 8989
cas:
  # cas服务端地址
  server-url-prefix: http://sso.maxkey.top/maxkey/authz/cas/
  # cas服务端登陆地址
  server-login-url: http://sso.maxkey.top/maxkey/authz/cas/login
  # 客户端访问地址
  client-host-url: http://localhost:8989/
  # 认证方式，默认cas
  validation-type: cas
  #  客户端需要拦截的URL地址
  authentication-url-patterns:
    - /test1/index
    - /test1/index1
```
## 第三部 在application启动类上加上 @EnableCasClient 注解
## 第四步 在代码中获取登录用户信息
        String token =request.getParameter("token");
        System.out.println("token : "+token);
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        String username=     assertion.getPrincipal().getName();
        
