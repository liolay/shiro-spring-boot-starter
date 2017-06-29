package cn.ocoop.framework.filter;

import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class PermissionsAuthorizationFilter extends AuthorizationWithStatusCodeFilter {


    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = this.getSubject(request, response);
        String[] perms = (String[]) mappedValue;
        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else if (!subject.isPermittedAll(perms)) {
                isPermitted = false;
            }
        }

        return isPermitted;
    }

}
