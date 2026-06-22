plugins {
    java
    // application
    id("com.gradleup.shadow") // version "8.3.0"
}

dependencies {
    implementation(project(":starters:flink-core"))
    implementation(project(":starters:flink-table"))
    implementation(project(":starters:flink-logging"))

    // shadowImplementation(project(":starters:flink-kafka"))
    // shadowImplementation(project(":starters:flink-iceberg"))
    implementation(project(":starters:flink-kafka"))
    implementation(project(":starters:flink-iceberg"))
    shadowImplementation(project(":jobs:common"))

    // runtimeOnly(project(":starters:flink-core"))
    // runtimeOnly(project(":starters:flink-table"))
}

tasks.shadowJar {

    mergeServiceFiles()

    manifest {
        attributes("Main-Class" to "io.slim.flink.KafkaPrintJob")
    }

    isZip64 = true
}

// application {
//     mainClass = "io.slim.flink.KafkaPrintJob"
// }

// tasks.shadowJar {
//     dependencies {
//         exclude(dependency("org.apache.flink:flink-core:.*"))
//         exclude(dependency("org.apache.flink:flink-core-api:.*"))
//         exclude(dependency("org.apache.flink:flink-streaming-java:.*"))
//         exclude(dependency("org.apache.flink:flink-clients:.*"))
//         exclude(dependency("org.apache.flink:flink-table.*"))
//         exclude(dependency("org.apache.flink:flink-java:.*"))
//         exclude(dependency("org.apache.flink:flink-runtime:.*"))
//         exclude(dependency("org.apache.flink:flink-datastream:.*"))
//         exclude(dependency("org.apache.flink:flink-datastream-api:.*"))
//         exclude(dependency("org.apache.flink:flink-optimizer:.*"))
//         exclude(dependency("org.apache.flink:flink-cep:.*"))
//         exclude(dependency("org.apache.flink:flink-annotations:.*"))
//         exclude(dependency("org.apache.flink:flink-metrics-core:.*"))
//         exclude(dependency("org.apache.flink:flink-file-sink-common:.*"))
//         exclude(dependency("org.apache.flink:flink-rpc-core:.*"))
//         exclude(dependency("org.apache.flink:flink-rpc-akka-loader:.*"))
//         exclude(dependency("org.apache.flink:flink-queryable-state-client-java:.*"))
//         exclude(dependency("org.apache.flink:flink-hadoop-fs:.*"))
//         exclude(dependency("org.apache.flink:flink-connector-datagen:.*"))
//         exclude(dependency("org.apache.flink:flink-shaded-asm-9:.*"))
//         exclude(dependency("org.apache.flink:flink-shaded-jackson:.*"))
//         exclude(dependency("org.apache.flink:flink-shaded-guava:.*"))
//         exclude(dependency("org.apache.flink:flink-shaded-zookeeper-3:.*"))
//         exclude(dependency("org.apache.flink:flink-shaded-netty:.*"))
//         exclude(dependency("com.google.code.findbugs:jsr305"))
//         exclude(dependency("org.slf4j:.*"))
//         exclude(dependency("org.apache.logging.log4j:.*"))
//     }

//     manifest {
//         attributes("Main-Class" to "io.slim.flink.KafkaPrintJob")
//     }
// }

// tasks.shadowJar {
//     manifest {
//         attributes("Main-Class" to "io.slim.flink.KafkaPrintJob")
//     }

//     dependencies {
//         // [1] Flink 내부 빌드용 더미 파일 (공식 문서 권장)
//         exclude(dependency("org.apache.flink:force-shading:.*"))

//         // [2] Flink Core & Runtime (엔진 핵심부)
//         exclude(dependency("org.apache.flink:flink-core:.*"))
//         exclude(dependency("org.apache.flink:flink-core-api:.*"))
//         exclude(dependency("org.apache.flink:flink-java:.*"))
//         exclude(dependency("org.apache.flink:flink-streaming-java:.*"))
//         exclude(dependency("org.apache.flink:flink-clients:.*"))
//         exclude(dependency("org.apache.flink:flink-runtime:.*"))
//         exclude(dependency("org.apache.flink:flink-optimizer:.*"))
//         exclude(dependency("org.apache.flink:flink-annotations:.*"))
//         exclude(dependency("org.apache.flink:flink-metrics-core:.*"))

//         // [3] Flink Table API & SQL
//         exclude(dependency("org.apache.flink:flink-table-common:.*"))
//         exclude(dependency("org.apache.flink:flink-table-api-java:.*"))
//         exclude(dependency("org.apache.flink:flink-table-api-java-bridge:.*"))
//         exclude(dependency("org.apache.flink:flink-table-runtime:.*"))
//         exclude(dependency("org.apache.flink:flink-table-planner-loader:.*"))

//         // [4] 클러스터에 기본 탑재된 포맷 및 확장 라이브러리
//         exclude(dependency("org.apache.flink:flink-json:.*"))
//         exclude(dependency("org.apache.flink:flink-csv:.*"))
//         exclude(dependency("org.apache.flink:flink-cep:.*"))

//         // [5] 로깅 시스템 (클러스터의 Log4j2 설정과 충돌 방지)
//         exclude(dependency("org.slf4j:slf4j-api:.*"))
//         exclude(dependency("org.apache.logging.log4j:log4j-api:.*"))
//         exclude(dependency("org.apache.logging.log4j:log4j-core:.*"))
//         exclude(dependency("org.apache.logging.log4j:log4j-slf4j-impl:.*"))
        
//         // [6] 기타 공통 유틸
//         exclude(dependency("com.google.code.findbugs:jsr305:.*"))
//     }
// }
