package cn.ocoop.framework.web.mgt;

import cn.ocoop.framework.cache.ShiroRealmCacheManager;

public class DefaultWebSecurityManager extends org.apache.shiro.web.mgt.DefaultWebSecurityManager {
    @Override
    protected void afterRealmsSet() {
        super.afterRealmsSet();
        ShiroRealmCacheManager.setRealms(getRealms());
    }
}
