package com.example.forum.core.dal;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DsAno {
    MasterSlaveDsEnum value() default MasterSlaveDsEnum.MASTER;
}
