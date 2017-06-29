package cn.ocoop.framework.filter;

import cn.ocoop.framework.config.RequestProperties;
import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public abstract class AuthorizationWithStatusCodeFilter extends AuthorizationFilter {
    private RequestProperties requestProperties;

    public RequestProperties getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented()))
            return super.onAccessDenied(request, response);

        Subject subject = getSubject(request, response);

        if (subject.getPrincipal() == null) {
            ResponseUtils.responseInvalidLogin(response, requestProperties.getInvalidLoginCode());
        } else {
            ResponseUtils.responseInvalidPermission(response, requestProperties.getInvalidPermissionCode());
        }
        return false;
    }

}
