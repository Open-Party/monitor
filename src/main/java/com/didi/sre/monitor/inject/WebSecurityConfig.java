package com.didi.sre.monitor.inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

	/**
	 * 登录session key
	 */
	public final static String SESSION_KEY = "user";

	@Bean
	public SecurityInterceptor getSecurityInterceptor() {
		return new SecurityInterceptor();
	}

	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

		// 排除配置
		addInterceptor.excludePathPatterns("/login**");
		addInterceptor.excludePathPatterns("/doLogin**");
		addInterceptor.excludePathPatterns("/register**");
		addInterceptor.excludePathPatterns("/swagger/**");
		addInterceptor.excludePathPatterns("/openapi/health");

		// 拦截配置
		addInterceptor.addPathPatterns("/**");
	}

	private class SecurityInterceptor extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			HttpSession session = request.getSession();
			if (session.getAttribute(SESSION_KEY) != null)
				return true;

			// 跳转登录
			String url = "/login";
			response.sendRedirect(url);
			return false;
		}

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
                throws Exception {
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
                throws Exception {
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("yef.zhu@gmail.com").password("12345678").roles("USER");
    }
}
