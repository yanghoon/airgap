plugins {
    id("java-platform")
}

dependencies {

    var flinkVersion = "1.20.1"
    var log4jVersion = "2.17.1"

    var kafkaVersion = "3.4.0-1.20"
    var icebergVersion = "1.8.1"

    constraints {
        // Flink Core
        api("org.apache.flink:flink-streaming-java:${flinkVersion}")
        api("org.apache.flink:flink-clients:${flinkVersion}")

        // Flink Table
        api("org.apache.flink:flink-table-api-java:${flinkVersion}")
        api("org.apache.flink:flink-table-runtime:${flinkVersion}")
        api("org.apache.flink:flink-table-planner-loader:${flinkVersion}")

        // Flink Connector
        api("org.apache.flink:flink-connector-base:${flinkVersion}")

        // Flink Connector (Kafka)
        api("org.apache.flink:flink-connector-kafka:${kafkaVersion}")

        // Iceberg Flink
        api("org.apache.iceberg:iceberg-flink-runtime-${flinkVersion}:${icebergVersion}")

        // Logging
        api("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
    }

}
