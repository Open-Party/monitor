package com.didi.sre.monitor.inject;

import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by soarpenguin on 17-8-10.
 */

@Configuration
public class VelocityLayoutConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Bean(name = "velocityViewResolver")
    public VelocityLayoutViewResolver velocityViewResolver(VelocityProperties properties) {
        VelocityLayoutViewResolver resolver = new VelocityLayoutViewResolver();
        properties.applyToViewResolver(resolver);
        resolver.setLayoutUrl("/layout/default.vm");
        resolver.getAttributesMap().put("serverAddr", getIp());
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DefaultKey.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            resolver.getAttributesMap().put(entry.getKey(), entry.getValue());
        }
        return resolver;
    }

    private static String getIp() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            return null;
        }
        return addr.getHostAddress();
    }
}

