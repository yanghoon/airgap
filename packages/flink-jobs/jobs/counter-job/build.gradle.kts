plugins {
    id("java")
    id("application")
    // id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(project(":starters:flink-core"))
    runtimeOnly(project(":starters:flink-logging"))
}

application {
    mainClass.set("com.airgap.flink.CounterJob")
}

// java {
//     sourceCompatibility = JavaVersion.VERSION_17
//     targetCompatibility = JavaVersion.VERSION_17
// }

// tasks.named<Jar>("jar") {
//     manifest {
//         attributes["Main-Class"] = "com.airgap.flink.CounterJob"
//     }
// }
