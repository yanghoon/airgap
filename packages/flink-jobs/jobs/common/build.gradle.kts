plugins {
    id("java")
}

dependencies {
    api(platform((project(":starters:flink-dependencies"))))
    // Flink Dependencies (Provided)
    // compileOnly("org.apache.flink:flink-streaming-java:1.18.0")
    // compileOnly("org.apache.flink:flink-table-api-java-bridge:1.18.0")

    // Cloud-Native Configuration (MicroProfile)
    implementation("io.smallrye.config:smallrye-config")
    implementation("io.smallrye.config:smallrye-config-source-yaml")

    // Lightweight Template Engine
    implementation("org.apache.commons:commons-text")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
}
