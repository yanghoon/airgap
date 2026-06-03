package io.slim.common;

import java.util.stream.Stream;

import io.slim.common.context.ConfigFactory;
import io.slim.common.sql.SqlManager;
import io.slim.common.template.DefaultTemplateEngine;
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
        context.sqlManager = new SqlManager(
            context.environment,
            context.templateContext,
            new DefaultTemplateEngine()
        );

        return context;
    }

    public JobEnvironment getEnvironment() { return environment; }
    public TemplateContext getTemplateContext() { return templateContext; }
    public SqlManager getSqlManager() { return sqlManager; }
    
}
