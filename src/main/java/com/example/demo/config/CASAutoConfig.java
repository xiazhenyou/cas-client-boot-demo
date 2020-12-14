package com.example.demo.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.MySingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CASAutoConfig
 * @Description
 * @menu
 * @Author xiazhenyou
 * @Date 2020/12/14 14:41
 * @Version 1.0
 **/
//@Configuration
public class CASAutoConfig {

        @Value("${cas.server-url-prefix}")
        private String serverUrlPrefix;
        @Value("${cas.server-login-url}")
        private String serverLoginUrl;
        @Value("${cas.client-host-url}")
        private String clientHostUrl;
        @Value("${udf.ignorePattern}")
        private String ignorePattern;
        @Value("${udf.ignoreUrlPatternType}")
        private String ignoreUrlPatternType;


        /**
         * 配置登出过滤器(过滤器顺序有要求，先登出-》授权过滤器-》配置过滤验证器-》wraper过滤器)
         * @return
         */
        @Bean
        public FilterRegistrationBean filterSingleRegistration() {
            final FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new MySingleSignOutFilter());
            // 设定匹配的路径
            registration.addUrlPatterns("/*");
            Map<String,String>  initParameters = new HashMap<String, String>();
            initParameters.put("casServerUrlPrefix", serverUrlPrefix);
            registration.setInitParameters(initParameters);
            // 设定加载的顺序
            registration.setOrder(1);
            return registration;
        }

        /**
         * 配置授权过滤器
         * @return
         */
        @Bean
        public FilterRegistrationBean filterAuthenticationRegistration() {
            final FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new AuthenticationFilter());
            // 设定匹配的路径
            registration.addUrlPatterns("/*");
            Map<String,String> initParameters = new HashMap<String, String>();
            initParameters.put("casServerLoginUrl", serverLoginUrl);
            initParameters.put("serverName", clientHostUrl);

            if(ignorePattern != null && !"".equals(ignorePattern)){
                initParameters.put("ignorePattern", ignorePattern);
            }

            //自定义UrlPatternMatcherStrategy 验证规则
            if(ignoreUrlPatternType != null && !"".equals(ignoreUrlPatternType)){
                initParameters.put("ignoreUrlPatternType", ignoreUrlPatternType);
            }

            registration.setInitParameters(initParameters);
            // 设定加载的顺序
            registration.setOrder(2);
            return registration;
        }

        /**
         * 配置过滤验证器 这里用的是Cas30ProxyReceivingTicketValidationFilter
         * @return
         */
        @Bean
        public FilterRegistrationBean filterValidationRegistration() {
            final FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
            // 设定匹配的路径
            registration.addUrlPatterns("/*");
            Map<String,String> initParameters = new HashMap<String, String>();
            initParameters.put("casServerUrlPrefix", serverUrlPrefix);
            initParameters.put("serverName", clientHostUrl);
            initParameters.put("useSession", "true");
            registration.setInitParameters(initParameters);
            // 设定加载的顺序
            registration.setOrder(3);
            return registration;
        }

        /**
         * request wraper过滤器
         * @return
         */
        @Bean
        public FilterRegistrationBean filterWrapperRegistration() {
            final FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new HttpServletRequestWrapperFilter());
            // 设定匹配的路径
            registration.addUrlPatterns("/*");
            // 设定加载的顺序
            registration.setOrder(4);
            return registration;
        }

        /**
         * 添加监听器
         * @return
         */
        @Bean
        public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
            ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
            registrationBean.setListener(new SingleSignOutHttpSessionListener());
            registrationBean.setOrder(1);
            return registrationBean;
        }

}
