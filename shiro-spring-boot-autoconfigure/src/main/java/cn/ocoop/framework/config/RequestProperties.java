package cn.ocoop.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RequestProperties.PREFIX)
public class RequestProperties {
    public static final String PREFIX = "shiro";

    private int invalidLoginCode = 418;
    private int invalidPermissionCode = 401;
    private boolean serviceOriented = false;
    private String logInOutResponseKey = "success";

    public String getLogInOutResponseKey() {
        return logInOutResponseKey;
    }

    public void setLogInOutResponseKey(String logInOutResponseKey) {
        this.logInOutResponseKey = logInOutResponseKey;
    }

    public boolean isServiceOriented() {
        return serviceOriented;
    }

    public void setServiceOriented(boolean serviceOriented) {
        this.serviceOriented = serviceOriented;
    }

    public int getInvalidLoginCode() {
        return invalidLoginCode;
    }

    public void setInvalidLoginCode(int invalidLoginCode) {
        this.invalidLoginCode = invalidLoginCode;
    }

    public int getInvalidPermissionCode() {
        return invalidPermissionCode;
    }

    public void setInvalidPermissionCode(int invalidPermissionCode) {
        this.invalidPermissionCode = invalidPermissionCode;
    }
}
