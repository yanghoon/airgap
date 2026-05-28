package io.slim.flink;

import java.util.Optional;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaPrintJob {

    private static Logger log = LoggerFactory.getLogger(KafkaPrintJob.class);

    public static void main(String[] args) {
        var env = StreamExecutionEnvironment.getExecutionEnvironment();
        var tableEnv = StreamTableEnvironment.create(env);

        var sourceSql = """
            CREATE TABLE source (
                `log_line` STRING
            ) WITH (
                'connector' = 'kafka',
                'topic' = 'logs',
                'properties.group.id' = '%s',

                'scan.startup.mode' = 'earliest-offset',

                'properties.bootstrap.servers' = 'localhost:443',
                'properties.security.protocol' = 'SSL',
                'properties.ssl.truststore.type' = 'PEM',
                'properties.ssl.truststore.certificates' = '%s',
                'properties.ssl.endpoint.identification.algorithm' = '',
                'properties.ssl.certificate.verification' = '0',

                'format' = 'raw'
            )
        """;

        var kafkaGroupId = "local-" + System.currentTimeMillis();
        var kafkaPem = loadKafkaPem();
        tableEnv.executeSql(String.format(sourceSql, kafkaGroupId, kafkaPem));

        tableEnv.executeSql("""
            CREATE TABLE sink (
                `log_line` STRING
            ) WITH (
                'connector' = 'print'
            )
        """);

        tableEnv.createStatementSet()
            .addInsertSql("""
                INSERT INTO sink SELECT * FROM source
            """)
            .execute();
    }

    private static String loadKafkaPem() {
        try (var inputStream = KafkaPrintJob.class.getClassLoader().getResourceAsStream("kafka.pem")) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load kafka.pem from resources");
            }
            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            log.error("Error loading kafka.pem", e);
            throw new RuntimeException("Failed to load kafka.pem", e);
        }
    }

    private static String resolveKafkaPemPath() {
        try {
            var candidatePath = System.getenv().getOrDefault("KAFKA_PEM_PATH", "kafka.pem");
            var resourceUrl = KafkaPrintJob.class.getClassLoader().getResource(candidatePath);
            if (resourceUrl == null) {
                throw new RuntimeException("Failed to find kafka.pem in resources");
            }
            return resourceUrl.toURI().getPath();
        } catch (Exception e) {
            log.error("Error resolving kafka.pem path", e);
            throw new RuntimeException("Failed to resolve kafka.pem path", e);
        }
    }

}
