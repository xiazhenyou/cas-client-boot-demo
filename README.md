# cas-client-boot-demo
基于spring boot配置cas客户端
demo分别写了三个请求:拦截请求 test1/index,test1/index1 以及不拦截请求test1/index2,
CASAutoConfig中，如果把Configuration注解的注释去掉，即可直接通过过滤器方式完成统一登录和注销。
若基于前后端分离，前端建议在登录时把用户和token自己维护好，退出接口解析cas报文获取退出的具体用户。参考test1/logout
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
扩展配置项
````
cas.authentication-url-patterns
cas.validation-url-patterns
cas.request-wrapper-url-patterns
cas.assertion-thread-local-url-patterns
cas.gateway
cas.use-session
cas.redirect-after-validation
cas.allowed-proxy-chains
cas.proxy-callback-url
cas.proxy-receptor-url
cas.accept-any-proxy
server.context-parameters.renew
````
## 第三部 在application启动类上加上 @EnableCasClient 注解
```java
@SpringBootApplication
@EnableCasClient
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```
## 第四步 在代码中获取登录用户信息
``` java
    @GetMapping("test1/index1")
    public String index1(HttpServletRequest request){
        String token =request.getParameter("token");
        System.out.println("token : "+token);
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

        String username=     assertion.getPrincipal().getName();
        System.out.println(username);

        AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
        String username=     principal.getName();
        System.out.println(username);
        //获取其他用户属性，需要在makey配置cas适配器org.maxkey.authz.cas.endpoint.adapter.CasDefaultAdapter
        System.out.println(principal.getAttributes().toString());

        return "test index cas拦截正常,登录账号:"+username;
    }
```
