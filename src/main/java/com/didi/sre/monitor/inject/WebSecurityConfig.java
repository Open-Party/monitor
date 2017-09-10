package com.didi.sre.monitor.inject;

import com.didi.sre.monitor.service.user.SysUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by soarpenguin on 9/5/17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static Logger logger = Logger.getLogger(WebSecurityConfig.class);

    @Bean
    UserDetailsService customUserService() { //注册UserDetailsService 的bean
        return new SysUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .inMemoryAuthentication()
//            .withUser("test@gmail.com").password("12345678").roles("USER");

        auth.userDetailsService(customUserService()); //user Details Service验证
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/login", "/doLogin", "/register", "/doRegister", "/swagger/**", "/openapi/health", "/plugins/**", "/bootstrap/**", "/dist/**")
                .permitAll()
                .anyRequest().authenticated() //其他所有资源都需要认证，登陆后访问
                //.antMatchers("/hello").hasAuthority("ADMIN") //登陆后之后拥有“ADMIN”权限才可以访问, 否则系统会出现“403”权限不足的提示
            .and()
                .formLogin()
                .loginPage("/login")//指定登录页是"/login"
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index")//登陆成功路径
                //.failureUrl("/login")//登陆失败路径
                .permitAll()
                //.successHandler(loginSuccessHandler()) //登录成功后可使用loginSuccessHandler()存储用户信息,可选。
            .and()
                .logout()
                .logoutUrl("/signOut")
                .logoutSuccessUrl("/login") //退出登录后的默认网址是/login
                .invalidateHttpSession(true)
                .permitAll();
//           .and()
//              .rememberMe()//登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
//              .tokenValiditySeconds(1209600);
    }
}
