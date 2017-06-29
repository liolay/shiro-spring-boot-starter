package cn.ocoop.framework.filter;

import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public abstract class AuthorizationWithStatusCodeFilter extends AuthorizationFilter {
    private StatusCode statusCode;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        if (!RequestUtils.isAjaxRequest(request)) return super.onAccessDenied(request, response);

        Subject subject = getSubject(request, response);

        if (subject.getPrincipal() == null) {
            ResponseUtils.responseInvalidLogin(response, statusCode.getInvalidLoginCode());
        } else {
            ResponseUtils.responseInvalidPermission(response, statusCode.getInvalidPermissionCode());
        }
        return false;
    }

}
