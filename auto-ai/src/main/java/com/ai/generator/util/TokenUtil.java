package com.ai.generator.util;

import com.ai.generator.consts.RedisCode;
import com.ai.generator.entity.TokenEntity;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class TokenUtil {
    protected static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    @Autowired
    private RedisUtil redisUtil ;
    /**
     * 创建token
     *
     * @param id
     * @return
     */
    public TokenEntity createToken(Long id) {
        TokenEntity tokenModel = new TokenEntity();
        tokenModel.setId(id);
        tokenModel.setToken(RandomUtil.generateString(10) + "_" + id);
        tokenModel.setLogintime(new Date());
        return tokenModel;
    }


    /**
     * 获取token信息
     * @param token
     * @return
     */
    public TokenEntity getTokenEntityFromCache(String token){
        if(StringUtils.isEmpty(token)){
            return null ;
        }
        return redisUtil.get(RedisCode.getUserLoginToken(token));
    }
    /**
     *
     * 缓存登录信息
     * @param tokenModel
     */
    public void cacheToken(TokenEntity tokenModel){
        if(tokenModel == null){
            return ;
        }
        logger.info("登录缓存信息：{}", JSON.toJSONString(tokenModel));
        redisUtil.set(RedisCode.getUserLoginToken(tokenModel.getToken()),tokenModel,RedisCode.LOGIN_TIMES);
    }

    /**
     * 清除缓存信息
     * @param token
     */
    public void clearTokenFromCache(String token){
        if(StringUtils.isEmpty(token)){
            return ;
        }
        redisUtil.remove(RedisCode.getUserLoginToken(token));
    }
}
