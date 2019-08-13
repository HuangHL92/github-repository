package com.ruoyi.common.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 日志写入控制
 * @Author yufei
 * @Date 2019-03-06 07:59
 **/
@Target(ElementType.METHOD)//这个注解是应用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteLog2DB {

    /**
     * 需要写入日志
     * @return
     */
    boolean required() default true;



}