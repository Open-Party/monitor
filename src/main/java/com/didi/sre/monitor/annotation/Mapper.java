package com.didi.sre.monitor.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * @author soarpenguin on 17-8-26.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Mapper {
    String value() default "";
}

