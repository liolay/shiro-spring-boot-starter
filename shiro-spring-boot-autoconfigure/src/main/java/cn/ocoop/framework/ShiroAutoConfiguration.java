package cn.ocoop.framework;

import cn.ocoop.framework.authz.ModularSourceRealmAuthorizer;
import cn.ocoop.framework.cache.RedisCacheManager;
import cn.ocoop.framework.config.RequestProperties;
import cn.ocoop.framework.config.ShiroProperties;
import cn.ocoop.framework.filter.DefaultFilter;
import cn.ocoop.framework.session.RedisSessionDAO;
import cn.ocoop.framework.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.lang.reflect.InvocationTargetException;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(value = {RedisTemplate.class})
@EnableConfigurationProperties({
        ShiroProperties.class,
        RequestProperties.class
})
@Import(ShiroConfiguration.class)
public class ShiroAutoConfiguration {
    private final ShiroProperties shiroProperties;
    private final RequestProperties requestProperties;

    public ShiroAutoConfiguration(ShiroProperties shiroProperties, RequestProperties requestProperties) {
        this.shiroProperties = shiroProperties.afterPropertiesSet();
        this.requestProperties = requestProperties;
    }

    @Bean
    @ConditionalOnMissingBean(FilterRegistrationBean.class)
    public FilterRegistrationBean filterRegistrationBean(ShiroFilterFactoryBean shiroFilterFactoryBean) throws Exception {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setFilter((Filter) shiroFilterFactoryBean.getObject());
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    @ConfigurationProperties(prefix = "shiro")
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) throws InvocationTargetException, IllegalAccessException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(DefaultFilter.createInstanceMap(requestProperties));
        return shiroFilterFactoryBean;
    }


    @Bean
    @ConditionalOnMissingBean(SecurityManager.class)
    public SecurityManager securityManager(
            WebSessionManager sessionManager,
            AbstractAuthenticator authenticator,
            CacheManager cacheManager,
            RememberMeManager rememberMeManager,
            ModularRealmAuthorizer modularRealmAuthorizer
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setAuthorizer(modularRealmAuthorizer);
        securityManager.setRealms(shiroProperties.realms());
        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean(ModularRealmAuthorizer.class)
    public ModularRealmAuthorizer modularRealmAuthorizer() {
        return new ModularSourceRealmAuthorizer();
    }

    @Bean
    @ConfigurationProperties(prefix = "shiro.remember-me")
    @ConditionalOnMissingBean(RememberMeManager.class)
    public RememberMeManager rememberMeManager() {
        return new CookieRememberMeManager();
    }

    @Bean("shiroCacheManager")
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    @ConfigurationProperties(prefix = "shiro.session")
    @ConditionalOnMissingBean(WebSessionManager.class)
    public WebSessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(sessionDAO);
        return defaultWebSessionManager;
    }

    @Bean
    @ConfigurationProperties(prefix = "shiro.session")
    @ConditionalOnMissingBean(SessionDAO.class)
    public SessionDAO sessionDAO(RedisTemplate redisTemplate) {
        return new RedisSessionDAO(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractAuthenticator.class)
    public AbstractAuthenticator authenticator() {
        return new ModularRealmAuthenticator();
    }

}
