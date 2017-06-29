package cn.ocoop.framework.filter;

import cn.ocoop.framework.config.RequestProperties;
import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);
    private RequestProperties requestProperties;

    public RequestProperties getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties;
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        String redirectUrl = this.getRedirectUrl(request, response, subject);

        try {
            subject.logout();
        } catch (SessionException var6) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", var6);
        }

        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented())) {
            this.issueRedirect(request, response, redirectUrl);
        } else {
            ResponseUtils.responseJson(response, HttpServletResponse.SC_OK, Maps.immutableEntry(requestProperties.getLogInOutResponseKey(), true));
        }

        return false;
    }
}
