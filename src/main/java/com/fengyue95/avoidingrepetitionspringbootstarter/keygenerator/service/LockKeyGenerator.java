package com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.service;

import javax.servlet.http.HttpServletRequest;

import com.fengyue95.avoidingrepetitionspringbootstarter.annotation.AvoidingRepetition;

/**
 * @author fengyue
 * @date 2021/5/21
 */
public interface LockKeyGenerator {

    /**
     * 生成key
     * @param avoidingRepetition
     * @param request
     * @return
     */
    String generate(AvoidingRepetition avoidingRepetition, HttpServletRequest request)
        throws IllegalAccessException, InstantiationException;
}
