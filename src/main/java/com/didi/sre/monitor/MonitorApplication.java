package com.didi.sre.monitor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author soarpenguin on 9/5/17.
 */
@ComponentScan(basePackages ="com.didi.sre.monitor")
@SpringBootApplication
public class MonitorApplication {
	private static final Logger logger = LoggerFactory.getLogger(MonitorApplication.class);

	@PostConstruct
	public void initApplication() throws IOException {
		logger.info("Running with Spring profile(s) : {}");
	}

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}
}
