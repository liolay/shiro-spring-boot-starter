package cn.ocoop.framework.filter;

import cn.ocoop.framework.config.RequestProperties;
import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
    private RequestProperties requestProperties;

    public RequestProperties getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented()))
            return super.onAccessDenied(request, response);

        ResponseUtils.responseInvalidLogin(response, requestProperties.getInvalidLoginCode());
        return false;
    }
}
