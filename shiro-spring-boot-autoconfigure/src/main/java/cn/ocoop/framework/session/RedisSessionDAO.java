package cn.ocoop.framework.session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisSessionDAO extends AbstractSessionDAO {
    private RedisTemplate redisTemplate;
    private String sessionCacheName = "shiro:session:";

    public RedisSessionDAO() {
    }

    public RedisSessionDAO(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getSessionCacheName() {
        return sessionCacheName;
    }

    public void setSessionCacheName(String sessionCacheName) {
        this.sessionCacheName = sessionCacheName;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    private void saveSession(Session session) {
        if (session == null || session.getId() == null) return;

        final Long expireValue = Long.valueOf(session.getTimeout() / 1000L);
        final String sessionKey = getSessionKey(session.getId());
        this.redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set(sessionKey, session);
                operations.expire(sessionKey, expireValue.longValue(), TimeUnit.SECONDS);
                return operations.exec();
            }
        });
    }

    private String getSessionKey(Serializable sessionId) {
        return sessionCacheName + sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) return null;
        return (Session) redisTemplate.opsForValue().get(getSessionKey(sessionId));
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session.getId() == null) return;
        redisTemplate.delete(getSessionKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set keys = redisTemplate.keys(sessionCacheName + "*");
        if (CollectionUtils.isEmpty(keys)) return null;
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
