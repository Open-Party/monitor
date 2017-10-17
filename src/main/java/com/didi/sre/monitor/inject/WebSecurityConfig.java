package com.didi.sre.monitor.inject;

import com.didi.sre.monitor.model.common.MD5PasswordEncoder;
import com.didi.sre.monitor.service.user.SysUserServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.RedirectUrlBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author soarpenguin on 9/5/17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static Logger logger = Logger.getLogger(WebSecurityConfig.class);

    private static final String[] ignorePathMatchers = new String[]{
        "/login", "/doLogin", "/register", "/doRegister", "/swagger/**", "/openapi/health",
        "/plugins/**/*.js", "/plugins/**/*.css", "/plugins/**/*.png",
        "/bootstrap/**/*.js", "/bootstrap/**/*.css", "/bootstrap/**/*.woff2",
        "/dist/**/*.js", "/dist/**/*.css"
    };

    @Bean
    UserDetailsService customUserService() { // 注册UserDetailsService
        return new SysUserServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("Initializing user details auth service with md5 password encoder.");
        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder()); //user Details Service auth.
        auth.eraseCredentials(false);

        //auth
        //    .inMemoryAuthentication()
        //    .withUser("admin").password("admin").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.jee().disable();
        http.x509().disable();

        http
                .authorizeRequests()
                .antMatchers(ignorePathMatchers)
                .permitAll()
                .anyRequest().authenticated()
                .antMatchers("/admin").hasAuthority("ADMIN")
            .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index")
                .failureUrl("/login")
                .permitAll()
                .successHandler(new AuthenticationSuccessHandler())
            .and()
                .logout()
                .logoutUrl("/signOut")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(new LogoutSuccessHandler())
                .permitAll()
           .and()
              .rememberMe()//登录后记住用户,下次自动登录,数据库中必须存在名为persistent_logins的表
              .tokenValiditySeconds(1209600);
    }

    @Bean
    public MD5PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    protected boolean isAjax(HttpServletRequest request) {
        return StringUtils.isNotBlank(request.getHeader("X-Requested-With"));
    }

    private class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
                throws ServletException, IOException {

            clearAuthenticationAttributes(request);

            String ru = (String) request.getSession().getAttribute("ru");
            request.getSession().removeAttribute("ru");

            if (StringUtils.isNotEmpty(ru)) {
                response.sendRedirect(ru);
            }

            if (!isAjax(request)) {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }

    private class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request,
                                    HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

            if (!isAjax(request)) {
                super.onLogoutSuccess(request, response, authentication);
            }
        }
    }

    private class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {
            String returnUrl = buildHttpReturnUrlForRequest(request);
            request.getSession().setAttribute("ru", returnUrl);

            response.setCharacterEncoding("utf-8");
            if (isAjax(request)) {
                response.getWriter().println("请登录");
            } else {
                response.sendRedirect("/login");
            }

        }
    }

    protected String buildHttpReturnUrlForRequest(HttpServletRequest request)
            throws IOException, ServletException {

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme("http");
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(request.getServerPort());
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setServletPath(request.getServletPath());
        urlBuilder.setPathInfo(request.getPathInfo());
        urlBuilder.setQuery(request.getQueryString());

        return urlBuilder.getUrl();
    }

    private class MyAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setCharacterEncoding("utf-8");
            if (isAjax(request)) {
                response.getWriter().println("您无权访问");
            } else {
                response.sendRedirect("/403");
            }

        }
    }
}
