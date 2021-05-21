package com.fengyue95.avoidingrepetitionspringbootstarter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.LockKeyIPGenerator;
import com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.service.LockKeyGenerator;

/**
 * @author fengyue
 * @date 2021/4/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AvoidingRepetition {

    /**
     * 缓存前缀，用于区分不同的业务
     *
     * @return
     */
    String lockPrefix() default "";

    /**
     * 锁等待时间
     *
     * @return
     */
    int waitTime() default 3;

    /**
     * 设置请求锁定时间（默认10秒）
     *
     * @return
     */
    int lockTime() default 10;

    /**
     * 缓存锁定时间单位（默认秒）
     *
     * @return
     */
    TimeUnit lockTimeUnit() default TimeUnit.SECONDS;

    /**
     * 拦截模式（概念废弃） 0 仅限 ip 拦截 1 仅限 ip+url 拦截 2 仅限 ip+url+requestParam.toString 拦截 废弃，通过实现LockKeyGenerator,自定义生成key方式
     * 
     * @return
     */
    // int mode() default 0;

    /**
     * key加密方式 (加密没什么意义) 去掉去掉
     *
     * @return
     */
    // Class<? extends LockKeyEncryption> encryption() default DefaultLockKeyEncryption.class;

    /**
     * 生成key方式
     *
     * @return
     */
    Class<? extends LockKeyGenerator> generate() default LockKeyIPGenerator.class;

    /**
     * 请求结束是否自动释放锁
     * 
     * @return
     */
    boolean autoRelease() default true;

}
