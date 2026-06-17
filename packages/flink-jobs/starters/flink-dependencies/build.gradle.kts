plugins {
    id("java-platform")
}

dependencies {

    var flinkVersion = "1.20.1"
    var log4jVersion = "2.17.1"
    var junitVersion = "5.10.2"
    var junitPlatformVersion = "1.10.2"

    var kafkaVersion = "3.4.0-1.20"
    var icebergVersion = "1.8.1"
    var hadoopVersion = "3.3.1"

    var smallryeVersion = "3.17.2"      // MicroProfile
    var commonsTextVersion = "1.11.0"  // Template Engine

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
        api("org.apache.iceberg:iceberg-flink-runtime-1.20:${icebergVersion}")
        api("org.apache.iceberg:iceberg-aws-bundle:${icebergVersion}")
        api("org.apache.flink:flink-connector-files:${flinkVersion}")
        api("org.apache.hadoop:hadoop-client:${hadoopVersion}")


        /** Common **/
        // Logging
        api("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")

        // Test
        api("org.junit.jupiter:junit-jupiter:${junitVersion}")
        api("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
        api("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
        api("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")


        /** Config **/
        // MicroProfile
        api("io.smallrye.config:smallrye-config:${smallryeVersion}")
        api("io.smallrye.config:smallrye-config-source-yaml:${smallryeVersion}")

        // Template Engine
        api("org.apache.commons:commons-text:${commonsTextVersion}")
    }

}
