package com.didi.sre.monitor;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");

			container.addErrorPages(error401Page, error404Page, error500Page);
		});
	}
}
