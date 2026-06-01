package io.slim.common.template;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.slim.common.AppConfig;

public class TemplateContext {
    private final AppConfig config;
    private final Map<String, String> mutableVars;

    public TemplateContext(AppConfig config) {
        this.config = config;
        this.mutableVars = new HashMap<>(config.vars());
    }

    public TemplateContext computeIfPresent(String flatKey, Function<String, String> processor) {
        mutableVars.computeIfPresent(flatKey, (k, v) -> processor.apply(v));
        return this;
    }

    public Map<String, Object> toFlatMap() {
        Map<String, Object> finalMap = new HashMap<>();
        mutableVars.forEach((k, v) -> finalMap.put("app.vars." + k, v));
        
        // 최상위 메타데이터 명시적 주입
        finalMap.put("app.env", config.env());
        finalMap.put("app.job.name", config.job().name());
        
        return finalMap;
    }
}
