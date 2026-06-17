plugins {
    id("java-library")
}

dependencies {
    api("org.apache.iceberg:iceberg-flink-runtime-1.20")
    api("org.apache.iceberg:iceberg-aws-bundle")
    api("org.apache.flink:flink-connector-files")
    api("org.apache.hadoop:hadoop-client") {
        exclude(group = "net.minidev")
    }
}