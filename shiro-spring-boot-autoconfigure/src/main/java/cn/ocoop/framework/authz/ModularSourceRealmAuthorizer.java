package cn.ocoop.framework.authz;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModularSourceRealmAuthorizer extends ModularRealmAuthorizer {

    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).hasRole(principals, roleIdentifier)) return true;
        }
        return false;
    }

    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).isPermitted(principals, permission)) return true;
        }
        return false;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;
            if (((Authorizer) realm).isPermitted(principals, permission)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getRoles() throws InvocationTargetException, IllegalAccessException {
        assertRealmsConfigured();
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        List<String> roles = new ArrayList<>();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;

            Method getAuthorizationInfoMethod = ClassUtils.getMethod(realm.getClass(), "getAuthorizationInfo", PrincipalCollection.class);
            getAuthorizationInfoMethod.setAccessible(true);
            roles.addAll(((AuthorizationInfo) getAuthorizationInfoMethod.invoke(realm, principals)).getRoles());
        }
        return roles;
    }

    public List<String> getPermissions() throws InvocationTargetException, IllegalAccessException {
        assertRealmsConfigured();
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        List<String> permissions = new ArrayList<>();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || principals.fromRealm(realm.getName()).isEmpty()) continue;

            Method getAuthorizationInfoMethod = ClassUtils.getMethod(realm.getClass(), "getAuthorizationInfo", PrincipalCollection.class);
            getAuthorizationInfoMethod.setAccessible(true);
            permissions.addAll(((AuthorizationInfo) getAuthorizationInfoMethod.invoke(realm, principals)).getStringPermissions());
        }
        return permissions;
    }
}