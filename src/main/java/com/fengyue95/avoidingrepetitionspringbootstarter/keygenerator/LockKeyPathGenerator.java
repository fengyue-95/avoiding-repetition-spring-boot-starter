package com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator;

import javax.servlet.http.HttpServletRequest;

import com.fengyue95.avoidingrepetitionspringbootstarter.annotation.AvoidingRepetition;
import com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.service.LockKeyGenerator;
import org.springframework.stereotype.Component;

/**
 * @author fengyue
 * @date 2021/5/21
 */
@Component
public class LockKeyPathGenerator implements LockKeyGenerator {

    @Override
    public String generate(AvoidingRepetition avoidingRepetition, HttpServletRequest request) {
        String prefix = avoidingRepetition.lockPrefix();
        String servletPath = request.getServletPath();
        return String.format("%s_%s", prefix, servletPath);
    }
}
