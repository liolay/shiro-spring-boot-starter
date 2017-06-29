package cn.ocoop.framework.filter;

import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpMethodPermissionFilter extends PermissionsAuthorizationFilter {
    private static final Logger log = LoggerFactory.getLogger(HttpMethodPermissionFilter.class);
    private final Map<String, String> httpMethodActions = new HashMap();

    public HttpMethodPermissionFilter() {
        HttpMethodPermissionFilter.HttpMethodAction[] var1 = HttpMethodPermissionFilter.HttpMethodAction.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            HttpMethodPermissionFilter.HttpMethodAction methodAction = var1[var3];
            this.httpMethodActions.put(methodAction.name().toLowerCase(), methodAction.getAction());
        }

    }

    protected Map<String, String> getHttpMethodActions() {
        return this.httpMethodActions;
    }

    protected String getHttpMethodAction(ServletRequest request) {
        String method = ((HttpServletRequest) request).getMethod();
        return this.getHttpMethodAction(method);
    }

    protected String getHttpMethodAction(String method) {
        String lc = method.toLowerCase();
        String resolved = this.getHttpMethodActions().get(lc);
        return resolved != null ? resolved : method;
    }

    protected String[] buildPermissions(String[] configuredPerms, String action) {
        if (configuredPerms != null && configuredPerms.length > 0 && StringUtils.hasText(action)) {
            String[] mappedPerms = new String[configuredPerms.length];

            for (int i = 0; i < configuredPerms.length; ++i) {
                mappedPerms[i] = configuredPerms[i] + ":" + action;
            }

            if (log.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < mappedPerms.length; ++i) {
                    if (i > 0) {
                        sb.append(", ");
                    }

                    sb.append(mappedPerms[i]);
                }

                log.trace("MAPPED '{}' action to permission(s) '{}'", action, sb);
            }

            return mappedPerms;
        } else {
            return configuredPerms;
        }
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        String[] perms = (String[]) (mappedValue);
        String action = this.getHttpMethodAction(request);
        String[] resolvedPerms = this.buildPermissions(perms, action);
        return super.isAccessAllowed(request, response, resolvedPerms);
    }

    private enum HttpMethodAction {
        DELETE("delete"),
        GET("read"),
        HEAD("read"),
        MKCOL("create"),
        OPTIONS("read"),
        POST("create"),
        PUT("update"),
        TRACE("read");

        private final String action;

        HttpMethodAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return this.action;
        }
    }
}
