package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.slim.common.context.ConfigProvider;
import io.smallrye.config.Config;
import io.smallrye.config.ConfigMapping;

public class ConfigTests {
    
    @Test
    public void testConfigLoading() {
        var config = Config.getOrCreate();
        var val = config.getValue("job.name", String.class);

        assertEquals("kafka-to-multi-sink", val);
    }

    @Test
    public void testConfigMapping() {
        // var config = Config.getOrCreate();
        var config = new io.smallrye.config.SmallRyeConfigBuilder()
        //     .addDefaultInterceptors()
        //     .addDefaultSources()
            .addDiscoveredSources()
        //     .addDiscoveredInterceptors()
            .withMapping(JobConfig.class)
            .build();
        var jobConfig = config.getConfigMapping(JobConfig.class);

        assertEquals("kafka-to-multi-sink", jobConfig.name());
    }

    @Test
    public void testDynamicMapping() {
        var jobConfig = ConfigProvider.getConfigMapping(JobConfig.class);
        assertEquals("kafka-to-multi-sink", jobConfig.name());
    }

    @ConfigMapping(prefix = "job")
    public static interface JobConfig {
        String name();
    }

// import org.apache.flink.api.java.utils.ParameterTool;
// import org.apache.flink.table.api.EnvironmentSettings;
// import org.apache.flink.table.api.StatementSet;
// import org.apache.flink.table.api.TableEnvironment;

// public class MultiSinkJob {
//     public static void main(String[] args) throws Exception {
//         // 1. 컨텍스트 초기화 및 CLI 파라미터 주입
//         JobContext context = new JobContext(ParameterTool.fromArgs(args).toMap());

//         // 2. 값 명시적 전처리 (파일 치환 및 복호화)
//         context.getTemplateContext()
//                .computeIfPresent("kafka.options.properties.ssl.truststore.certificates", ResourceLoader::load)
//                .computeIfPresent("kafka.options.properties.secret", MultiSinkJob::decrypt);

//         // 3. 파이프라인 구성 완료
//         context.prepare();

//         // 4. Flink 실행 흐름 분기
//         SqlManager sqlManager = context.getSqlManager();
//         TableEnvironment tableEnv = TableEnvironment.create(
//             EnvironmentSettings.newInstance().inStreamingMode().build()
//         );

//         // 메타데이터 등록 (Schemas -> DDL)
//         for (String schema : sqlManager.getRenderedSchemas()) {
//             tableEnv.executeSql(schema);
//         }

//         // 스트리밍 파이프라인 묶음 제출 (Pipelines -> DML)
//         StatementSet statementSet = tableEnv.createStatementSet();
//         for (String pipeline : sqlManager.getRenderedPipelines()) {
//             statementSet.addInsertSql(pipeline);
//         }

//         statementSet.execute();
//     }

//     private static String decrypt(String encrypted) {
//         // 실제 KMS 복호화 로직을 이 곳에 구현합니다.
//         return "decrypted_" + encrypted;
//     }
// }

}
