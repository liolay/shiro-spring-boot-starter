package cn.ocoop.framework.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

/**
 * Created by liolay on 2017/6/22.
 */
public class RedisCache<K, V> implements Cache<K, V> {
    private final String name;
    private final RedisTemplate redisTemplate;

    public RedisCache(String name, RedisTemplate redisTemplate) {
        this.name = name;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {
        return (V) redisTemplate.opsForValue().get(getKey(key));
    }

    private String getKey(K key) {
        return name + ":" + key;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V ret = get(key);
        redisTemplate.opsForValue().set(getKey(key), value);
        return ret;
    }

    @Override
    public V remove(K key) throws CacheException {
        V ret = get(key);
        redisTemplate.delete(key);
        return ret;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set keys() {
        return redisTemplate.keys(name + ":*");
    }

    @Override
    public Collection values() {
        return redisTemplate.opsForValue().multiGet(keys());
    }
}
