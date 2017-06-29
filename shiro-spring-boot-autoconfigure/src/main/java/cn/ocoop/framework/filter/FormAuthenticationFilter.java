package cn.ocoop.framework.filter;

import cn.ocoop.framework.config.RequestProperties;
import cn.ocoop.framework.util.RequestUtils;
import cn.ocoop.framework.util.ResponseUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
    private RequestProperties requestProperties;

    public RequestProperties getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented())) {
                this.saveRequestAndRedirectToLogin(request, response);
            } else {
                ResponseUtils.responseInvalidLogin(response, requestProperties.getInvalidLoginCode());
            }
            return false;
        }
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented())) {
            this.issueSuccessRedirect(request, response);
        } else {
            ResponseUtils.responseJson(response, HttpServletResponse.SC_OK, Maps.immutableEntry(requestProperties.getLogInOutResponseKey(), true));
        }
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("Authentication exception", e);
        }

        if (RequestUtils.shouldUseNormalHttpRequestToProcess(request, requestProperties.isServiceOriented())) {
            this.setFailureAttribute(request, e);
            return true;
        }

        try {
            ResponseUtils.responseJson(response, HttpServletResponse.SC_OK, Maps.immutableEntry(requestProperties.getLogInOutResponseKey(), false));
        } catch (IOException e1) {
            log.error("response login fail fails", e1);
        }
        return false;

    }
}
