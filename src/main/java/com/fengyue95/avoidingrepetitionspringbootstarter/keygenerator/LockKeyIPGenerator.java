package com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator;

import javax.servlet.http.HttpServletRequest;

import com.fengyue95.avoidingrepetitionspringbootstarter.annotation.AvoidingRepetition;
import com.fengyue95.avoidingrepetitionspringbootstarter.util.IpAddressUtil;
import com.fengyue95.avoidingrepetitionspringbootstarter.keygenerator.service.LockKeyGenerator;
import org.springframework.stereotype.Component;

/**
 * @author fengyue
 * @date 2021/5/21
 */
@Component
public class LockKeyIPGenerator implements LockKeyGenerator {

    @Override
    public String generate(AvoidingRepetition avoidingRepetition, HttpServletRequest request){
        String ipAddress = IpAddressUtil.getIpAddress(request);
        String prefix = avoidingRepetition.lockPrefix();

        return String.format("%s_%s",prefix,ipAddress);
    }
}
