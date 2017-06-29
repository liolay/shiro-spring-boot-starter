package cn.ocoop.framework.filter;

import cn.ocoop.framework.util.RequestUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        String redirectUrl = this.getRedirectUrl(request, response, subject);

        try {
            subject.logout();
        } catch (SessionException var6) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", var6);
        }

        if (!RequestUtils.isAjaxRequest(request)) {
            this.issueRedirect(request, response, redirectUrl);
        }

        return false;
    }
}
