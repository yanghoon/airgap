package io.slim;

import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.SmallRyeConfigProviderResolver;
import jakarta.annotation.Priority;

@Priority(1000)
public class AutoInitConfigProviderResolver extends SmallRyeConfigProviderResolver {

    @Override
    public SmallRyeConfig get(ClassLoader classLoader) {
        try {
            return super.get(classLoader);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("SRCFG00015")) {
                SmallRyeConfig config = new SmallRyeConfigBuilder()
                    .addDefaultInterceptors()
                    .addDefaultSources()
                    .addDiscoveredSources()
                    .addDiscoveredInterceptors()
                    .build();
                registerConfig(config, classLoader);
                return config;
            }
            throw e;
        }
    }

}
