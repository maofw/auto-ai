package com.ai.generator.util;

import com.ai.generator.consts.RedisCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
public class RedisUtil {

    private final static Long requestLockTime = 200L;
    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> boolean addZSet(String key, T t) {
        return redisTemplate.opsForZSet().add(key, t, System.currentTimeMillis());
    }

    /**
     * 批量删除登陆信息
     *
     * @param key
     */
    public void batchDel(String key) {
        Set<String> keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }

    /**
     * 批量删除
     * @param key
     */
    public void batchDel(Set<String> key) {
        redisTemplate.delete(key);
    }

    /**
     * 模糊查询key
     * @param key
     * @return
     */
    public Set<String> getKey(String key){
        return redisTemplate.keys(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 延长过期时间
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void extendExpire(String key, long time, TimeUnit timeUnit) {
        Long l = getExpire(key, timeUnit);
        expire(key, l + time, timeUnit);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 添加过期时间
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expire(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    public <T> Set<T> getZSetOrder(String key) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRange(key, 0, -1);
    }

    /**
     * 将list放入缓存(左)
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> Long lAddList(String key, T value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除下表对应的值
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean ldel(String key, T t) {
        redisTemplate.opsForList().remove(key, 0, t);
        return true;
    }

    /**
     * 获取List所有数据
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> List<T> listAll(String key) {
        RedisTemplate<String, T> tTemplate = (RedisTemplate<String, T>) redisTemplate;
        return tTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 获取列表 指定范围内的所有值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> List<T> listRange(String key, long start, long end) {
        RedisTemplate<String, T> tTemplate = (RedisTemplate<String, T>) redisTemplate;
        return tTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表指定长度
     *
     * @param key
     * @return
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 将list放入缓存(右)
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> Long rAddList(String key, T value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取最后进入的
     *
     * @param key
     * @param <T>
     */
    public <T> T rGetList(String key) {
        return (T) redisTemplate.opsForList().index(key, -1);
    }

    /**
     * 指定存入下标
     *
     * @param key
     * @param t
     * @param index
     * @param <T>
     * @return
     */
    public <T> boolean rSet(String key, Long index,T t) {
        try {
            redisTemplate.opsForList().set(key, index, t);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 请求锁
     *
     * @return
     */
    public boolean requestLock(String key) {
        try {
            key = RedisCode.requestLockCode(key);
            Long time = get(key);
            Long currentTime = System.currentTimeMillis();
            if (null != time) {
                if (currentTime - time > requestLockTime) {
                    remove(key);
                }
            }

            if (hasKey(key)) {
                return true;
            }
            set(key, currentTime, requestLockTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("异常锁", e);
        }
        return false;
    }

    public <T> T get(String key) {
        RedisTemplate<String, T> tRedisTemplate = (RedisTemplate<String, T>) redisTemplate;
        return tRedisTemplate.opsForValue().get(key);

    }

    /**
     * 验证key是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 存入信息带过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 存入信息 泛型
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T t) {
        try {
            redisTemplate.opsForValue().set(key, t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 存入信息带过期时间
     *
     * @param key
     * @param value
     * @param time  秒
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
