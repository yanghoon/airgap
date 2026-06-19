plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api("org.apache.flink:flink-streaming-java")
    api("org.apache.flink:flink-clients")
    api("org.apache.flink:flink-runtime-web")
}
