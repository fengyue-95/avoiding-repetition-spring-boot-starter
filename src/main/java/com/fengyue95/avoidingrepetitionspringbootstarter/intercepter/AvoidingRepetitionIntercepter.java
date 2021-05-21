package com.fengyue95.avoidingrepetitionspringbootstarter.intercepter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fengyue95.avoidingrepetitionspringbootstarter.annotation.AvoidingRepetition;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.service.LockKeyGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengyue
 * @date 2021/4/22
 */
@Component(value = "noRepeatSubmitIntercepter")
@Slf4j
public class AvoidingRepetitionIntercepter implements HandlerInterceptor {

    @Autowired
    private RedissonClient     redissonClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Map<String, String> contextMap = new HashMap<>(4);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AvoidingRepetition avoidingRepetitionAnnotation = handlerMethod.getMethod().getAnnotation(
                AvoidingRepetition.class);
            // 如果没有注解 放行
            if (Objects.isNull(avoidingRepetitionAnnotation)) {
                return true;
            }
            String servletPath = request.getServletPath();
            LockKeyGenerator keyGenerator = null;
            try {
                keyGenerator = applicationContext.getBean(avoidingRepetitionAnnotation.generate());
            } catch (Exception e) {
                log.error("get keyGenerator error on servletPath:{}", servletPath);
            }
            if (Objects.isNull(keyGenerator)) {
                log.error("norepeatsubmit annotation need keyGenerator servletPath:{}", servletPath);
                return true;
            }
            String keyStr = keyGenerator.generate(avoidingRepetitionAnnotation, request);
            if (Objects.isNull(keyStr)) {
                log.error("preHandle key is null url:{}", servletPath);
                return true;
            }
            RLock lock = redissonClient.getLock(keyStr);
            if (lock.isLocked()) {
                // 资源已被锁住（即存在重复请求）
                log.error("repetition url:{}", servletPath);
                return false;
            }
            boolean lockResult = lock.tryLock(avoidingRepetitionAnnotation.waitTime(), avoidingRepetitionAnnotation.lockTime(),
                                              avoidingRepetitionAnnotation.lockTimeUnit());
            if (!lockResult) {
                // 未获取到锁
                log.error("lock key error url:{},key:{}", servletPath, keyStr);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AvoidingRepetition avoidingRepetitionAnnotation = handlerMethod.getMethod().getAnnotation(
                AvoidingRepetition.class);
            // 如果没有注解 放行
            if (Objects.isNull(avoidingRepetitionAnnotation)) {
                return;
            }
            String servletPath = request.getServletPath();
            LockKeyGenerator keyGenerator = null;
            try {
                keyGenerator = applicationContext.getBean(avoidingRepetitionAnnotation.generate());
            } catch (Exception e) {
                log.error("get keyGenerator error on servletPath:{}", servletPath);
            }
            if (Objects.isNull(keyGenerator)) {
                log.error("norepeatsubmit annotation need keyGenerator servletPath:{}", servletPath);
                return;
            }
            String keyStr = keyGenerator.generate(avoidingRepetitionAnnotation, request);
            RLock lock = redissonClient.getLock(keyStr);
            if (lock.isHeldByCurrentThread() && avoidingRepetitionAnnotation.autoRelease()) {
                lock.unlock();
                log.info("释放锁：{}", keyStr);
            }
        }

    }
}
