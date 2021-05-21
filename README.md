# avoidingRepetition-spring-boot-starter

2021-04-23 深夜

问题
为了防止重复提交，通常做法是： 将请求ip+请求路径（或者加上请求参数） / 用户token+请求url 的方式

思路
基本思路
使用spring HandlerInterceptor 拦截+redission 提供的分布式锁来进行控制。

获取当前请求ip+请求路径，作为一个唯一的key，去获取redis分布式锁。 如何获取前用户的标识：sessionId,token,ip 等等多种策略的获取唯一的key，并可以通过不同的加密形式，对原有key进行加密。

看了很多的博客，都是采用AOP去实现,为了防止重复提交一般都是针对web请求，采用拦截器处理足够用了。

SpringBoot利用AOP防止请求重复提交

spring boot 防止重复提交

Spring Boot 如何防止重复提交？

基本配置

cache.host=127.0.0.1
cache.port=6379
maven 依赖
根据当前spring 的版本进行选择合适的依赖 redisson-spring-data 可以具体看官方文档。 redisson-spring-data module if necessary to support required Spring Boot version:

 <dependency>
   <groupId>org.redisson</groupId>
   <artifactId>redisson-spring-boot-starter</artifactId>
   <version>3.15.3</version>
</dependency>
更多配置可以参考链接
https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter

注解设计
尝试获取锁的等待时间、锁的过期时间。
异常错误的提示信息。
指定key生成器。
指定key构建策略。
指定锁前缀，便于标记。
业务执行完成后 是否解锁。
/**
 * @author fengyue
 * @date 2021/4/22
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoRepeatSubmit {

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
     * 拦截模式
     * 0 仅限 ip 拦截
     * 1 仅限 ip+url 拦截
     * 2 仅限 ip+url+requestParam.toString 拦截
     *
     * @return
     */
    int mode() default 0;


    /**
     * 生成key方式
     *
     * @return
     */
    Class<? extends LockKeyGenerator> generator() default DefaultLockKeyGenerator.class;


    /**
     * 请求结束是否自动释放锁
     *
     * @return
     */
    boolean autoRelease() default false;


}
