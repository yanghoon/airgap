package io.slim.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.slim.common.JobContext;
import io.slim.common.ResourceLoader;

public class KafkaPrintJob {

    private static Logger log = LoggerFactory.getLogger(KafkaPrintJob.class);

    public static void main(String[] args) {
        var env = StreamExecutionEnvironment.getExecutionEnvironment();
        var tableEnv = StreamTableEnvironment.create(env);

        // Create job context and load external configurations
        var context = JobContext.create();

        // Load certificates if needed and generate group id
        context.getTemplateContext().computeIfPresent("kafka.options.properties.ssl.certificates", ResourceLoader::load);
        // context.getTemplateContext().computeIfPresent("kafka.options.properties.group.id", v -> "local-" + System.currentTimeMillis());

        var sqls = context.getSqlManager();
        // Create schemas
        sqls.getRenderedSchemas().stream()
            .forEach(sql -> {
                log.debug("Executing SQL: {}", sql);
                tableEnv.executeSql(sql);
            });
        
        // Create pipelines
        var statements = tableEnv.createStatementSet();
        sqls.getRenderedPipelines().stream()
            .forEach(sql -> {
                log.debug("Executing SQL: {}", sql);
                statements.addInsertSql(sql);
            });
        statements.execute();

    }

}
