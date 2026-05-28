plugins {
    java
    application
}

dependencies {
    compileOnly(project(":starters:flink-core"))
    compileOnly(project(":starters:flink-table"))

    implementation(project(":starters:flink-kafka"))

    runtimeOnly(project(":starters:flink-logging"))
}

application {
    mainClass.set("io.slim.flink.KafkaPrintJob")
}
