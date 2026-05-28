plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api("org.apache.flink:flink-table-api-java")
    api("org.apache.flink:flink-table-runtime")
    api("org.apache.flink:flink-table-planner-loader")
}
