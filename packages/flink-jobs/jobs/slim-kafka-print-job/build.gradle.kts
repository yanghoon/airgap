plugins {
    java
    application
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
    mainClass.set("io.slim.flink.KafkaPrintJob")
}
