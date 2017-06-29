package cn.ocoop.framework.filter;

public class StatusCode {
    private int invalidLoginCode = 418;
    private int invalidPermissionCode = 401;

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
