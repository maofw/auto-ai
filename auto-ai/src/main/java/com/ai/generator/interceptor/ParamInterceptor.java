package com.ai.generator.interceptor;

import com.ai.generator.aop.ApiController;
import com.ai.generator.enums.ResultEnum;
import com.ai.generator.http.HttpResult;
import com.ai.generator.util.IpUtil;
import com.ai.generator.util.RedisLock;
import com.ai.generator.util.Sha256Util;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: QIK
 * @CreateDate: 2019/5/6 10:37
 */
@Component
public class ParamInterceptor implements HandlerInterceptor {
    protected static final Logger log = LoggerFactory.getLogger(ParamInterceptor.class);

    @Autowired
    private RedisLock redisLock;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        log.info("参数前置拦截器 preHandle：{}" , requestURL.toString());
        try {
            String ip = IpUtil.getIpAddr(httpServletRequest);
            String data=httpServletRequest.getParameter("data");
            StringBuffer stringBuffer=new StringBuffer(ip).append(requestURL).append(data);
            String lockKey = stringBuffer.toString();
            stringBuffer = null ;

            if(!redisLock.tryLock(lockKey,"0",RedisLock.REQUESTLOCKTIME)){
                requestURL = null ;
                //频繁请求200毫秒内 同一个IP的同一个请求只能有一个
                log.info("频繁请求：{}",lockKey);
                HttpResult.returnJson(httpServletResponse, ResultEnum.FREQUENTLY);
                return false;
            }

            if (o instanceof HandlerMethod) {
                HandlerMethod h = (HandlerMethod) o;
                ApiController apiController = h.getMethodAnnotation(ApiController.class);
                if (apiController != null) {
                    if(apiController.isParam()){
                        if(StringUtils.isEmpty(data)){
                            HttpResult.returnJson(httpServletResponse, ResultEnum.NO_PARAM);
                            return false;
                        }
                       String dataJson= Sha256Util.base64ToString(data);
                       JSONObject jsonObject=JSONObject.parseObject(dataJson);
                       jsonObject.put("ip",ip);
                       log.info("请求uri：{} param：{}",requestURL.toString(),jsonObject.toJSONString());
                       httpServletRequest.setAttribute("requestObject", jsonObject);
                       requestURL = null ;
                    }
                    httpServletRequest.setAttribute("ip", ip);
                }
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            HttpResult.returnJson(httpServletResponse, ResultEnum.PARAM_CHECK);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.removeAttribute("requestObject");
        request.removeAttribute("ip");
    }
}
