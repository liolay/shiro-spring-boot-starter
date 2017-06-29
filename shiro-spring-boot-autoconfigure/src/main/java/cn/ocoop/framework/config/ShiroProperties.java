package cn.ocoop.framework.config;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = ShiroProperties.PREFIX)
public class ShiroProperties {
    public static final String PREFIX = "shiro";
    private List<RealmDefinition> realms = new ArrayList<>();
    private RequestProperties requestProperties = new RequestProperties();

    public RequestProperties getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties;
    }

    public List<Realm> realms() {
        return realms.stream()
                .map(realmDefinition -> realmDefinition.getRealm().getTarget())
                .collect(Collectors.toList());
    }

    public ShiroProperties afterPropertiesSet() {
        for (ShiroProperties.RealmDefinition realmDefinition : this.getRealms()) {
            setRealmProperty(realmDefinition);
            setCredentialsMatcherProperty(realmDefinition);
            setReamCredentialsMatcher(realmDefinition);
        }
        return this;
    }

    private void setReamCredentialsMatcher(RealmDefinition realmDefinition) {
        if (realmDefinition.getRealm().getTarget() instanceof AuthenticatingRealm) {
            ((AuthenticatingRealm) realmDefinition.getRealm().getTarget()).setCredentialsMatcher(realmDefinition.getRealm().getCredentialsMatcher().getTarget());
        }
    }

    private void setCredentialsMatcherProperty(RealmDefinition realmDefinition) {
        RealmProperty.CredentialsMatcherDefinition credentialsMatcherDefinition = realmDefinition.getRealm().getCredentialsMatcher();
        CredentialsMatcher matcher = credentialsMatcherDefinition.getTarget();
        credentialsMatcherDefinition.getProperty().forEach((propertyName, propertyValue) -> {
            try {
                org.apache.commons.beanutils.BeanUtils.setProperty(matcher, propertyName, propertyValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private void setRealmProperty(RealmDefinition realmDefinition) {
        realmDefinition.getRealm().getProperty().forEach((propertyName, propertyValue) -> {
            try {
                org.apache.commons.beanutils.BeanUtils.setProperty(realmDefinition, propertyName, propertyValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public List<RealmDefinition> getRealms() {
        return this.realms;
    }

    public void setRealms(List<RealmDefinition> realms) {
        this.realms = realms;
    }

    public static class RealmProperty {
        private Realm target;
        private Map<String, Object> property;
        private CredentialsMatcherDefinition credentialsMatcher;

        public Realm getTarget() {
            return target;
        }

        public void setTarget(Realm target) {
            this.target = target;
        }

        public Map<String, Object> getProperty() {
            return property;
        }

        public void setProperty(Map<String, Object> property) {
            this.property = property;
        }

        public CredentialsMatcherDefinition getCredentialsMatcher() {
            return credentialsMatcher;
        }

        public void setCredentialsMatcher(CredentialsMatcherDefinition credentialsMatcher) {
            this.credentialsMatcher = credentialsMatcher;
        }

        public static class CredentialsMatcherDefinition {
            private CredentialsMatcher target;
            private Map<String, Object> property;

            public CredentialsMatcher getTarget() {
                return target;
            }

            public void setTarget(CredentialsMatcher target) {
                this.target = target;
            }

            public Map<String, Object> getProperty() {
                return property;
            }

            public void setProperty(Map<String, Object> property) {
                this.property = property;
            }
        }
    }

    public static class RealmDefinition {
        private RealmProperty realm;


        public RealmProperty getRealm() {
            return realm;
        }

        public void setRealm(RealmProperty realm) {
            this.realm = realm;
        }
    }
}
