package nl.spring.brilkwijt.config;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    @NotNull
    private String appUrl;

    public String getAppUrl() {
        return appUrl;
    }
}
