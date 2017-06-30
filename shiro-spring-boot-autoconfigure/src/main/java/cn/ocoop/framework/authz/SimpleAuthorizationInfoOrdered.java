package cn.ocoop.framework.authz;


import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.LinkedHashSet;

public class SimpleAuthorizationInfoOrdered implements AuthorizationInfo {
    /**
     * The internal roles collection.
     */
    protected Collection<String> roles;

    /**
     * Collection of all string-based permissions associated with the account.
     */
    protected Collection<String> stringPermissions;

    /**
     * Collection of all object-based permissions associaed with the account.
     */
    protected Collection<Permission> objectPermissions;

    /**
     * Default no-argument constructor.
     */
    public SimpleAuthorizationInfoOrdered() {
    }

    /**
     * Creates a new instance with the specified roles and no permissions.
     *
     * @param roles the roles assigned to the realm account.
     */
    public SimpleAuthorizationInfoOrdered(Collection<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    /**
     * Sets the roles assigned to the account.
     *
     * @param roles the roles assigned to the account.
     */
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    /**
     * Adds (assigns) a role to those associated with the account.  If the account doesn't yet have any roles, a
     * new roles collection (a Set) will be created automatically.
     *
     * @param role the role to add to those associated with the account.
     */
    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new LinkedHashSet<>();
        }
        this.roles.add(role);
    }

    /**
     * Adds (assigns) multiple roles to those associated with the account.  If the account doesn't yet have any roles, a
     * new roles collection (a Set) will be created automatically.
     *
     * @param roles the roles to add to those associated with the account.
     */
    public void addRoles(Collection<String> roles) {
        if (this.roles == null) {
            this.roles = new LinkedHashSet<>();
        }
        this.roles.addAll(roles);
    }

    public Collection<String> getStringPermissions() {
        return stringPermissions;
    }

    /**
     * Sets the string-based permissions assigned directly to the account.  The permissions set here, in addition to any
     * {@link #getObjectPermissions() object permissions} constitute the total permissions assigned directly to the
     * account.
     *
     * @param stringPermissions the string-based permissions assigned directly to the account.
     */
    public void setStringPermissions(Collection<String> stringPermissions) {
        this.stringPermissions = stringPermissions;
    }

    /**
     * Adds (assigns) a permission to those directly associated with the account.  If the account doesn't yet have any
     * direct permissions, a new permission collection (a Set&lt;String&gt;) will be created automatically.
     *
     * @param permission the permission to add to those directly assigned to the account.
     */
    public void addStringPermission(String permission) {
        if (this.stringPermissions == null) {
            this.stringPermissions = new LinkedHashSet<>();
        }
        this.stringPermissions.add(permission);
    }

    /**
     * Adds (assigns) multiple permissions to those associated directly with the account.  If the account doesn't yet
     * have any string-based permissions, a  new permissions collection (a Set&lt;String&gt;) will be created automatically.
     *
     * @param permissions the permissions to add to those associated directly with the account.
     */
    public void addStringPermissions(Collection<String> permissions) {
        if (this.stringPermissions == null) {
            this.stringPermissions = new LinkedHashSet<>();
        }
        this.stringPermissions.addAll(permissions);
    }

    public Collection<Permission> getObjectPermissions() {
        return objectPermissions;
    }

    /**
     * Sets the object-based permissions assigned directly to the account.  The permissions set here, in addition to any
     * {@link #getStringPermissions() string permissions} constitute the total permissions assigned directly to the
     * account.
     *
     * @param objectPermissions the object-based permissions assigned directly to the account.
     */
    public void setObjectPermissions(Collection<Permission> objectPermissions) {
        this.objectPermissions = objectPermissions;
    }

    /**
     * Adds (assigns) a permission to those directly associated with the account.  If the account doesn't yet have any
     * direct permissions, a new permission collection (a Set&lt;{@link Permission Permission}&gt;) will be created automatically.
     *
     * @param permission the permission to add to those directly assigned to the account.
     */
    public void addObjectPermission(Permission permission) {
        if (this.objectPermissions == null) {
            this.objectPermissions = new LinkedHashSet<>();
        }
        this.objectPermissions.add(permission);
    }

    /**
     * Adds (assigns) multiple permissions to those associated directly with the account.  If the account doesn't yet
     * have any object-based permissions, a  new permissions collection (a Set&lt;{@link Permission Permission}&gt;)
     * will be created automatically.
     *
     * @param permissions the permissions to add to those associated directly with the account.
     */
    public void addObjectPermissions(Collection<Permission> permissions) {
        if (this.objectPermissions == null) {
            this.objectPermissions = new LinkedHashSet<>();
        }
        this.objectPermissions.addAll(permissions);
    }
}