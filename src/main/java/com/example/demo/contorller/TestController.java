package com.example.demo.contorller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName TestController
 * @Description 测试
 * @menu 测试
 * @Author xiazhenyou
 * @Date 2020/7/22 9:45
 * @Version 1.0
 **/
@RestController
public class TestController {

    @GetMapping("test1/index")
    public String index(HttpServletRequest request){
        AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
        String username=     principal.getName();
        System.out.println(username);
        //获取其他用户属性
        System.out.println(principal.getAttributes().toString());
        return "test1 index cas拦截正常,登录账号:"+username;
    }

    @GetMapping("test1/index1")
    public String index1(HttpServletRequest request){
        String token =request.getParameter("token");
        System.out.println("token : "+token);
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

        String username=     assertion.getPrincipal().getName();
        System.out.println(username);

        return "test index cas拦截正常,登录账号:"+username;
    }

    /**
     * 不走cas认证，无法获取登录信息
     * @param request
     * @return
     */
    @GetMapping("test1/index2")
    public String index2(HttpServletRequest request){
        String token =request.getParameter("token");
        System.out.println("token : "+token);
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
//
        String username=     assertion.getPrincipal().getName();
        System.out.println(username);

        return "cas 未拦截";
    }

    /**
     * 不通过过滤器退出时，解析nameId,清空自己前端对应的用户token
     * @param request
     * @return
     */
    @PostMapping("test1/logout")
    public String logout(HttpServletRequest request){
        String logoutMessage = CommonUtils.safeGetParameter(request, "logoutRequest", Arrays.asList("logoutRequest"));
        //用户主键
        String nameId= XmlUtils.getTextForElement(logoutMessage,"NameID");
        System.out.println(nameId);
        return "用户ID："+nameId;
    }
}
