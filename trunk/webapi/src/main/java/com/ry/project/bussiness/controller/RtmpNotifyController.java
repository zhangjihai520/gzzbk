package com.ry.project.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.ry.project.bussiness.controller.action.IRtmpNotifyActionService;
import com.ry.project.bussiness.domain.response.RtmpResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rtmp")
public class RtmpNotifyController {

    @Autowired
    IRtmpNotifyActionService rtmpNotifyActionService;

    /**
     * 推流通知
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/publish")
    public String publish(HttpServletRequest request, HttpServletResponse response) {
        var params = getAllRequestParam(request);
        try {
            if (rtmpNotifyActionService.pushAuth(params)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return RtmpResponse.GetSuccessResponse();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(String.format("推流异常，参数%s", JSON.toJSONString(params)), e);
            return RtmpResponse.GetErrorResponse();
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error(String.format("推流异常，参数%s", JSON.toJSONString(params)));
        return RtmpResponse.GetValidateErrorResponse();
    }


    private String debug(HttpServletRequest request, String action) {
        String str = action + ": " + request.getRequestURI() + " " + request.getQueryString();
        System.out.println(str);
        return str;
    }

    /**
     *      * 获取客户端请求参数中所有的信息
     *      * @param request
     *      * @return
     *      
     */
    protected Map<String, String> getAllRequestParam(HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);//如果字段的值为空，判断若值为空，则删除这个字段>
            }
        }
        return res;
    }
}