package io.slim.common;

import java.util.Map;

public class JobContext {
    private final JobEnvironment environment;
    private final TemplateContext templateContext;
    private SqlManager sqlManager;

    public JobContext(Map<String, String> cliParams) {
        String configPath = cliParams.getOrDefault("config", "classpath:application.yaml");
        this.environment = new JobEnvironment(configPath);
        this.templateContext = new TemplateContext(this.environment.getConfig());
    }

    public void prepare() {
        this.sqlManager = new SqlManager(this);
    }

    public TemplateContext getTemplateContext() { return templateContext; }
    public JobEnvironment getEnvironment() { return environment; }
    public SqlManager getSqlManager() {
        if (sqlManager == null) throw new IllegalStateException("prepare() must be called");
        return sqlManager;
    }
}
