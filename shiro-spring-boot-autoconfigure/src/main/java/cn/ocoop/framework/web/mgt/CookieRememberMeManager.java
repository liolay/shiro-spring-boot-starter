package cn.ocoop.framework.web.mgt;

import org.springframework.expression.spel.standard.SpelExpressionParser;

public class CookieRememberMeManager extends org.apache.shiro.web.mgt.CookieRememberMeManager {
    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();
    private String cookieCipherKey;

    public String getCookieCipherKey() {
        return cookieCipherKey;
    }

    public void setCookieCipherKey(String cookieCipherKey) {
        super.setCipherKey(
                String.valueOf(
                        SPEL_EXPRESSION_PARSER.parseExpression(
                                new String(cookieCipherKey)
                        ).getValue()
                ).getBytes()
        );
    }

}
