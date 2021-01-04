package com.ai.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {
    private Logger log = LoggerFactory.getLogger(RedisLock.class);
    //分布式锁 默认缓存200毫秒
    public final static Long REQUESTLOCKTIME = 200L;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 释放锁脚本
     */
    public static final String UNLOCK_LUA;

    /**
     * 释放锁脚本，原子操作
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
        sb = null ;
    }


    /**
     * 获取分布式锁，原子操作
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @return
     */
    public boolean tryLock(String lockKey, String requestId,Long expireTimes) {
        try{
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.set(lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")), Expiration.milliseconds(TimeUnit.MILLISECONDS.toMillis(expireTimes)), RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean)redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN ,1, lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")));
        };
        return (Boolean)redisTemplate.execute(callback);
    }

    /**
     * 获取Redis锁的value值
     * @param lockKey
     * @return
     */
    public String get(String lockKey) {
        try {
            RedisCallback<String> callback = (connection) -> {
                return new String(connection.get(lockKey.getBytes()), Charset.forName("UTF-8"));
            };
            return (String)redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }
}
