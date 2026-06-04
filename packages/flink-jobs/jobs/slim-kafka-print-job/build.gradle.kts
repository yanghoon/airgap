plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.0"
}

dependencies {
    compileOnly(project(":starters:flink-core"))
    compileOnly(project(":starters:flink-table"))

    implementation(project(":starters:flink-kafka"))

    runtimeOnly(project(":starters:flink-logging"))
    runtimeOnly(project(":starters:flink-core"))
    runtimeOnly(project(":starters:flink-table"))
}

application {
    mainClass = "io.slim.flink.KafkaPrintJob"
}

tasks.shadowJar {
    dependencies {
        exclude(dependency("org.apache.flink:.*"))
        exclude(dependency("com.google.code.findbugs:jsr305"))
        exclude(dependency("org.slf4j:.*"))
        exclude(dependency("org.apache.logging.log4j:.*"))
    }
}
