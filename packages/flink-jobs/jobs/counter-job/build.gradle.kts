plugins {
    java
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":starters:flink-core"))
    implementation(project(":starters:flink-logging"))
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "com.airgap.flink.ClunterJob")
    }
}
