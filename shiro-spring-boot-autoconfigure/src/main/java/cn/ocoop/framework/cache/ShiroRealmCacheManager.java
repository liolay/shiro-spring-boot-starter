package cn.ocoop.framework.cache;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public class ShiroRealmCacheManager {
    private static final Logger log = LoggerFactory.getLogger(ShiroRealmCacheManager.class);
    private static Collection<Realm> realms;

    public static void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        if (principals == null) return;

        getRealms().forEach(realm -> {
            if (realm instanceof AuthenticatingRealm) {
                Optional.ofNullable(getAuthenticationCache((AuthenticatingRealm) realm))
                        .ifPresent(cache -> cache.remove(principals));
            }
        });
    }

    public static void clearCachedAuthenticationInfo(PrincipalCollection principals, Class<? extends AuthenticatingRealm>... realms) {
        if (principals == null) return;
        getRealms().forEach(realm -> {
            for (Class realmClass : realms) {
                if (realmClass.isAssignableFrom(realm.getClass())) {
                    Optional.ofNullable(getAuthenticationCache((AuthenticatingRealm) realm))
                            .ifPresent(cache -> cache.remove(principals));
                }
            }
        });
    }

    public static void clearCachedAuthenticationInfo(Class<? extends AuthenticatingRealm>... realms) {
        getRealms().forEach(realm -> {
            for (Class realmClass : realms) {
                if (realmClass.isAssignableFrom(realm.getClass())) {
                    Optional.ofNullable(getAuthenticationCache((AuthenticatingRealm) realm))
                            .ifPresent(cache -> cache.clear());
                }
            }
        });
    }

    public static void clearAllCachedAuthenticationInfo() {
        getRealms().forEach(realm -> {
            if (realm instanceof AuthenticatingRealm) {
                Optional.ofNullable(getAuthenticationCache((AuthenticatingRealm) realm))
                        .ifPresent(cache -> cache.clear());
            }
        });
    }

    private static Cache<Object, AuthenticationInfo> getAuthenticationCache(AuthenticatingRealm realm) {
        AuthenticatingRealm authenticatingRealm = realm;
        Cache<Object, AuthenticationInfo> cache = authenticatingRealm.getAuthenticationCache();
        boolean authcCachingEnabled = authenticatingRealm.isAuthenticationCachingEnabled();
        if (cache == null && authcCachingEnabled) {
            cache = getAuthenticationCacheLazy(authenticatingRealm);
        }
        return cache;
    }

    private static Cache<Object, AuthenticationInfo> getAuthenticationCacheLazy(AuthenticatingRealm realm) {

        if (realm.getAuthenticationCache() == null) {

            log.trace("No authenticationCache instance set.  Checking for a cacheManager...");

            CacheManager cacheManager = realm.getCacheManager();

            if (cacheManager != null) {
                String cacheName = realm.getAuthenticationCacheName();
                log.debug("CacheManager [{}] configured.  Building authentication cache '{}'", cacheManager, cacheName);
                realm.setAuthenticationCache(cacheManager.getCache(cacheName));
            }
        }

        return realm.getAuthenticationCache();
    }

    public static void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) return;
        getRealms().forEach(realm -> {
            if (realm instanceof AuthorizingRealm) {
                Optional.ofNullable(getAuthorizationCache((AuthorizingRealm) realm))
                        .ifPresent(cache -> cache.remove(principals));
            }
        });
    }

    public static void clearCachedAuthorizationInfo(PrincipalCollection principals, Class<? extends AuthorizingRealm>... realms) {
        if (principals == null) return;

        getRealms().forEach(realm -> {
            for (Class realmClass : realms) {
                if (realmClass.isAssignableFrom(realm.getClass())) {
                    Optional.ofNullable(getAuthorizationCache((AuthorizingRealm) realm))
                            .ifPresent(cache -> cache.remove(principals));
                }
            }
        });
    }

    public static void clearCachedAuthorizationInfo(Class<? extends AuthorizingRealm>... realms) {
        getRealms().forEach(realm -> {
            for (Class realmClass : realms) {
                if (realmClass.isAssignableFrom(realm.getClass())) {
                    Optional.ofNullable(getAuthorizationCache((AuthorizingRealm) realm))
                            .ifPresent(cache -> cache.clear());
                }
            }
        });
    }

    public static void clearAllCachedAuthorizationInfo() {
        getRealms().forEach(realm -> {
            if (realm instanceof AuthorizingRealm) {
                Optional.ofNullable(getAuthorizationCache((AuthorizingRealm) realm))
                        .ifPresent(cache -> cache.clear());
            }
        });
    }

    private static Cache<Object, AuthorizationInfo> getAuthorizationCache(AuthorizingRealm realm) {
        Cache<Object, AuthorizationInfo> cache = realm.getAuthorizationCache();
        if (cache == null && realm.isAuthorizationCachingEnabled()) {
            cache = getAuthorizationCacheLazy(realm);
        }
        return cache;
    }

    private static Cache<Object, AuthorizationInfo> getAuthorizationCacheLazy(AuthorizingRealm realm) {

        if (realm.getAuthorizationCache() == null) {

            if (log.isDebugEnabled()) {
                log.debug("No authorizationCache instance set.  Checking for a cacheManager...");
            }

            CacheManager cacheManager = realm.getCacheManager();

            if (cacheManager != null) {
                String cacheName = realm.getAuthorizationCacheName();
                if (log.isDebugEnabled()) {
                    log.debug("CacheManager [" + cacheManager + "] has been configured.  Building " +
                            "authorization cache named [" + cacheName + "]");
                }
                realm.setAuthorizationCache(cacheManager.getCache(cacheName));
            } else {
                if (log.isInfoEnabled()) {
                    log.info("No cache or cacheManager properties have been set.  Authorization cache cannot " +
                            "be obtained.");
                }
            }
        }

        return realm.getAuthorizationCache();
    }

    private static Collection<Realm> getRealms() {
        return realms;
    }

    public static void setRealms(Collection<Realm> realms) {
        ShiroRealmCacheManager.realms = realms;
    }
}
