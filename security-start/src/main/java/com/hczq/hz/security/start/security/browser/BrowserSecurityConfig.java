package com.hczq.hz.security.start.security.browser;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author zhust
 */
@Configuration
@EnableWebSecurity
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter   {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
         //http.httpBasic() // HTTP Basic
                .and()
                .authorizeRequests() // 授权配置
                .anyRequest()  // 所有请求
                .authenticated(); // 都需要认证
    }
}
