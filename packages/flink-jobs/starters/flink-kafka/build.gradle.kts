plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api("org.apache.flink:flink-connector-base")
    api("org.apache.flink:flink-connector-kafka")
}
