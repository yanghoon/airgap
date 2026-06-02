package io.slim.common;

import java.util.List;
import java.util.Map;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping
public interface JobEnvironment {
    
    @WithDefault("local")
    String env();
    
    // 계층형 구조를 1차원 Flat Map으로 자동 변환합니다.
    Map<String, String> vars();
    
    JobConfig config();

    interface JobConfig {
        String name();
        List<String> schemas();
        List<String> pipelines();
    }

}
