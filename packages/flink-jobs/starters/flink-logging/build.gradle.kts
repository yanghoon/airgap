plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api("org.apache.logging.log4j:log4j-slf4j-impl")
}
