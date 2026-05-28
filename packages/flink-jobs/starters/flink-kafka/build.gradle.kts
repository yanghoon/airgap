plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api(project(":starters:flink-connector-base"))
    api(project(":starters:flink-connector-kafka"))
}
