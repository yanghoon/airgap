plugins {
    java
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":starters:flink-core"))
    implementation(project(":starters:flink-table"))
    implementation(project(":starters:flink-logging"))

    shadowImplementation(project(":starters:flink-kafka"))
    shadowImplementation(project(":starters:flink-iceberg"))
    shadowImplementation(project(":jobs:common"))
}

tasks.shadowJar {

    mergeServiceFiles()

    manifest {
        attributes("Main-Class" to "io.slim.flink.KafkaPrintJob")
    }

    isZip64 = true
}
