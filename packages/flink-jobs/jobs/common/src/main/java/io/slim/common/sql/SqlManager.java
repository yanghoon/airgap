package io.slim.common.sql;

import java.util.List;
import java.util.stream.Collectors;

public class SqlManager {
    private final JobContext context;

    public SqlManager(JobContext context) { 
        this.context = context; 
    }

    public List<String> getRenderedSchemas() {
        return renderList(context.getEnvironment().getConfig().job().schemas());
    }

    public List<String> getRenderedPipelines() {
        return renderList(context.getEnvironment().getConfig().job().pipelines());
    }

    private List<String> renderList(List<String> paths) {
        if (paths == null) return List.of();
        return paths.stream().map(path -> 
            TemplateEngine.render(ResourceLoader.load(path), context.getTemplateContext().toFlatMap())
        ).collect(Collectors.toList());
    }
}
