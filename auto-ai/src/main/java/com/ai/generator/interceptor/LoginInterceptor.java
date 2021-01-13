package com.ai.generator.interceptor;

import com.ai.generator.aop.ApiController;
import com.ai.generator.entity.TokenEntity;
import com.ai.generator.enums.ResultEnum;
import com.ai.generator.http.HttpResult;
import com.ai.generator.util.TokenUtil;
import com.alibaba.fastjson.JSON;
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
 * @author Administrator
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    protected static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private TokenUtil tokenUtil ;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            String ip = (String)httpServletRequest.getAttribute("ip");
            String token = httpServletRequest.getHeader("token");
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            if (o instanceof HandlerMethod) {
                HandlerMethod h = (HandlerMethod) o;
                ApiController apiController = h.getMethodAnnotation(ApiController.class);
                if (apiController != null) {
                    Class clazz = apiController.paramClass();
                    /**
                     *
                     *  登录拦截器修改：2019-09-08 maosr
                     *
                     *  更新内容：
                     *
                     *  isLogin=true,必须是登录状态，未登录抛出异常，前端提示登录
                     *  isLogin=false,不是登录状态即可访问接口，不用强制校验是否登录；但是如果是登录状态token存在有效信息，request中需要设置用户信息
                     *
                     */
                    //验证是否需要进行登陆校验
                    TokenEntity tokenEntity = null ;
                    if (!StringUtils.isEmpty(token)) {
                        tokenEntity = tokenUtil.getTokenEntityFromCache(token);
                    }
                    if(null==tokenEntity){
                        if(apiController.isLogin()){
                            //未登录提示
                            HttpResult.returnJson(httpServletResponse, ResultEnum.NO_LOGIN);
                            return false;
                        }
                    }

                    if(!(clazz.getName().equalsIgnoreCase(Class.class.getName()))){
                        JSONObject data = (JSONObject)httpServletRequest.getAttribute("requestObject");
                        if(null==data){
                            data=new JSONObject();
                        }
                        if(tokenEntity!=null){
                            data.put("token",tokenEntity.getToken());
                            data.put("loginUserId",tokenEntity.getId());
                            data.put("logintime",tokenEntity.getLogintime().getTime());
                        }
                        data.put("ip",ip);

                        Object object = data.toJavaObject(clazz);
                        httpServletRequest.setAttribute("request", object);
                        log.info("请求uri：{} paramClass：{} request：{}",requestURL.toString(),clazz.getName(),JSON.toJSONString(object));
                        data = null ;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            HttpResult.returnJson(httpServletResponse, ResultEnum.ERROR);
            log.info("登录拦截系统异常：{}",e);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.removeAttribute("request");
    }
}
