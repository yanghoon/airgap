package io.slim.common;

import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

public class JobEnvironment {
    private final AppConfig appConfig;

    public JobEnvironment(String configPath) {
        SmallRyeConfig config = new SmallRyeConfigBuilder()
            .addDefaultSources() // 시스템 속성 및 OS 환경변수 자동 매핑
            .withLocations(configPath) // 전달받은 경로의 YAML 적용
            .withMapping(AppConfig.class)
            .build();

        this.appConfig = config.getConfigMapping(AppConfig.class);
    }

    public AppConfig getConfig() { return appConfig; }
}
