package cn.ocoop.framework.filter;


import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DefaultFilter {

    anon(AnonymousFilter.class),
    authc(FormAuthenticationFilter.class),
    authcBasic(BasicHttpAuthenticationFilter.class),
    logout(LogoutFilter.class),
    noSessionCreation(NoSessionCreationFilter.class),
    perms(PermissionsAuthorizationFilter.class),
    rest(HttpMethodPermissionFilter.class),
    roles(RolesAuthorizationFilter.class),
    anyRoles(AnyRolesAuthorizationFilter.class),
    user(UserFilter.class);

    private final Class<? extends Filter> filterClass;

    DefaultFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    public static Map<String, Filter> createInstanceMap() {
        Map<String, Filter> filters = new LinkedHashMap<>(values().length);
        for (DefaultFilter defaultFilter : values()) {
            filters.put(defaultFilter.name(), defaultFilter.newInstance());
        }
        return filters;
    }

    public Filter newInstance() {
        return (Filter) ClassUtils.newInstance(this.filterClass);
    }

}
