plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.airgap"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.flink:flink-streaming-java:1.20.0")
    implementation("org.apache.flink:flink-clients:1.20.0")
    implementation("org.apache.flink:flink-connector-base:1.20.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.22.1")
    runtimeOnly("org.apache.logging.log4j:log4j-api:2.22.1")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.22.1")
}

application {
    mainClass.set("com.airgap.flink.CounterJob")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.airgap.flink.CounterJob"
    }
}