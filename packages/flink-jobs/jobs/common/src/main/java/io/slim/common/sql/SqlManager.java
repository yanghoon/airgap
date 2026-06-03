package io.slim.common.sql;

import java.util.List;
import java.util.stream.Collectors;

import io.slim.common.JobEnvironment;
import io.slim.common.ResourceLoader;
import io.slim.common.template.TemplateContext;
import io.slim.common.template.TemplateEngine;

public class SqlManager {
    private final JobEnvironment environment;
    private final TemplateContext templateContext;
    private final TemplateEngine templateEngine;

    public SqlManager(JobEnvironment environment, TemplateContext templateContext, TemplateEngine templateEngine) { 
        this.environment = environment;
        this.templateContext = templateContext;
        this.templateEngine = templateEngine;
    }

    public List<String> getRenderedSchemas() {
        return renderList(environment.job().schemas());
    }

    public List<String> getRenderedPipelines() {
        return renderList(environment.job().pipelines());
    }

    private List<String> renderList(List<String> paths) {
        if (paths == null) return List.of();
        return paths.stream().map(path -> 
            templateEngine.render(ResourceLoader.load(path), templateContext.getVars())
        ).collect(Collectors.toList());
    }

}
