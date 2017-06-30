package cn.ocoop.demo;

import cn.ocoop.framework.authz.SimpleAuthorizationInfoOrdered;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import java.util.Arrays;

public class DemoRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //有序的角色，权限集合实现
        SimpleAuthorizationInfoOrdered authorizationInfo = new SimpleAuthorizationInfoOrdered();
        authorizationInfo.setRoles(Arrays.asList("角色1", "角色2"));
        authorizationInfo.setStringPermissions(Arrays.asList("权限1", "权限2"));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo(
                "用户名",
                "密码密文",
                new SimpleByteSource("盐值"),
                this.getName()
        );
    }

}
