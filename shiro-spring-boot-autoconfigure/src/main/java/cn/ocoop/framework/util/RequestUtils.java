package cn.ocoop.framework.util;

import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

public class RequestUtils {

    public static boolean isAjaxRequest(ServletRequest request) {

        String requestHeader = WebUtils.toHttp(request).getHeader("X-Requested-With");
        return requestHeader != null && "XMLHttpRequest".equals(requestHeader);
    }

}
