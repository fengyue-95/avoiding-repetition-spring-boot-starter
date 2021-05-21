package com.fengyue95.avoidingrepetitionspringbootstarter.web;

import javax.servlet.http.HttpServletRequest;

import com.fengyue95.avoidingrepetitionspringbootstarter.annotation.AvoidingRepetition;
import com.fengyue95.avoidingrepetitionspringbootstarter.util.IpAddressUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengyue
 * @date 2021/4/22
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    @AvoidingRepetition(lockPrefix = "fengyue", lockTime = 10, autoRelease = false)
    public String index(@RequestParam(value = "str") String str, HttpServletRequest request)
        throws InterruptedException {
        String ipAddress = IpAddressUtil.getIpAddress(request);
        return ipAddress + "--" + str;
    }
}
