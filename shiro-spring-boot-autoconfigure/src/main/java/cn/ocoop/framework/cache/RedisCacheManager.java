package cn.ocoop.framework.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager extends AbstractCacheManager {
    private final RedisTemplate redisTemplate;

    public RedisCacheManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new RedisCache(name, redisTemplate);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        try {
            ((JedisConnectionFactory) redisTemplate.getConnectionFactory()).destroy();
        } catch (Throwable e) {
        }
    }
}
