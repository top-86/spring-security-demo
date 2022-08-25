package com.hczq.hz.security.permit.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hczq.hz.security.permit.handler.MyAuthenticationAccessDeniedHandler;
import com.hczq.hz.security.permit.handler.MyAuthenticationFailureHandler;
import com.hczq.hz.security.permit.handler.MyAuthenticationSuccessHandler;
import com.hczq.hz.security.permit.handler.MyLogOutSuccessHandler;
import com.hczq.hz.security.permit.session.MySessionExpiredStrategy;
import com.hczq.hz.security.permit.validate.code.ValidateCodeFilter;
import com.hczq.hz.security.permit.validate.smscode.SmsAuthenticationConfig;
import com.hczq.hz.security.permit.validate.smscode.SmsCodeFilter;

/**
 * @author zhust
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private MyAuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;
    @Autowired
    private MySessionExpiredStrategy sessionExpiredStrategy;

    @Autowired
    private MyLogOutSuccessHandler logOutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler).and()
            // 添加验证码校验过滤器
            // .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
            // 添加短信验证码校验过滤器
            // .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin() // 表单登录
            // http.httpBasic() // HTTP Basic
            // 登录跳转 URL
            .loginPage("/login.html")
            // 处理表单登录 URL
            .loginProcessingUrl("/login")
            // 处理登录成功
            .successHandler(authenticationSuccessHandler)
            // 处理登录失败
            .failureHandler(authenticationFailureHandler).and()
            // 授权配置
            .authorizeRequests()
            .antMatchers("/authentication/require", "/login.html", "/code/image", "/code/sms", "/session/invalid",
                "/signout/success")
            // 无需认证的请求路径
            .permitAll()
            // 所有请求
            .anyRequest()
            // 都需要认证
            .authenticated()
            // 添加 Session管理器
            .and().sessionManagement()
            // Session失效后跳转到这个链接
            // .invalidSessionUrl("/session/invalid")
            .maximumSessions(1).maxSessionsPreventsLogin(true).expiredSessionStrategy(sessionExpiredStrategy).and()
            .and().logout().logoutUrl("/signout").logoutSuccessUrl("/signout/success")
            .logoutSuccessHandler(logOutSuccessHandler).deleteCookies("JSESSIONID").and().csrf().disable();
        // 将短信验证码认证配置加到 Spring Security 中
        // .apply(smsAuthenticationConfig);
    }
}
