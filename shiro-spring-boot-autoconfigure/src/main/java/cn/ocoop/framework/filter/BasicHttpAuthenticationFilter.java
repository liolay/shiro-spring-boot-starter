package cn.ocoop.framework.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class BasicHttpAuthenticationFilter extends org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //force to valid token every request,when u developer a webapp,this is very useful
        return false;
    }
}

