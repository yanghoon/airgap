package io.slim.common.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import io.slim.common.JobEnvironment;

public class TemplateContext {

    private final Map<String, String> vars;

    public TemplateContext(JobEnvironment environment) {
        this.vars = new HashMap<>();

        // 1. YAML 파싱 과정에서 중간에 삽입된 모든 큰따옴표("") 제거
        Stream.ofNullable(environment.vars())
            .flatMap(vars -> vars.entrySet().stream())
            .forEach(entry -> {
                var rawKey = entry.getKey();
                var cleanKey = rawKey.replace("\"", "");
                this.vars.put(cleanKey, entry.getValue());
            });
        
        this.vars.put("job.name", environment.job().name());
        this.vars.put("env", environment.env());
    }

    @Deprecated
    public TemplateContext computeIfPresent(String flatKey, Function<String, String> processor) {
        vars.computeIfPresent(flatKey, (k, v) -> processor.apply(v));
        return this;
    }

    public Map<String, Object> getVars() {
        return Collections.unmodifiableMap(vars);
    }

}
