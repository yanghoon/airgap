package io.slim.common;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.slim.common.context.ConfigFactory;
import io.slim.common.sql.SqlManager;
import io.slim.common.template.TemplateContext;

public class JobContext {
    private JobEnvironment environment;
    private TemplateContext templateContext;
    private SqlManager sqlManager;

    public static JobContext create(Class<?>... mappingClasses) {
        var config = ConfigFactory.create(
            Stream.concat(
                Stream.of(JobEnvironment.class),
                Stream.of(mappingClasses)
            ).toArray(Class[]::new)
        );
        var context = new JobContext();
        context.environment = config.getConfigMapping(JobEnvironment.class);
        context.templateContext = new TemplateContext(context.environment);
        return context;
    }

    // public JobContext(Map<String, String> cliParams) {
    //     String configPath = cliParams.getOrDefault("config", "classpath:application.yaml");
    //     this.environment = new JobEnvironment(configPath);
    //     this.templateContext = new TemplateContext(this.environment.getConfig());
    // }

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
