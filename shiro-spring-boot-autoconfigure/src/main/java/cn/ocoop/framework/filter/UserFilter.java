package cn.ocoop.framework.filter;

import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
    private StatusCode statusCode;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (!RequestUtils.isAjaxRequest(request)) return super.onAccessDenied(request, response);

        ResponseUtils.responseInvalidLogin(response, statusCode.getInvalidLoginCode());
        return false;
    }
}
