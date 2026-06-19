import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.0" apply false
}

subprojects {

    group = "io.slim.flink"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    pluginManager.withPlugin("java") {
        // 
        configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }

        // 
        pluginManager.withPlugin("com.gradleup.shadow") {
            val shadowImplementation by configurations.creating {
                description = "Dependencies that will be bundled into the Shadow/Fat JAR"

                description = "Dependencies that will be bundled into the Shadow/Fat JAR"
                
                // 1. 파라미터 파싱 충돌 방지 (NoSuchMethodError 원인)
                exclude(group = "commons-cli", module = "commons-cli")
                exclude(group = "org.apache.commons", module = "commons-math3")
                
                // 2. 구형 로깅 라이브러리 하이재킹 방지 (SLF4J Multiple bindings 원인)
                exclude(group = "org.slf4j", module = "slf4j-log4j12")
                exclude(group = "log4j", module = "log4j")
                exclude(group = "ch.qos.logback", module = "logback-classic")
                exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
            }

            configurations.named("implementation") {
                extendsFrom(shadowImplementation)
            }

            tasks.withType<ShadowJar>().configureEach {
                configurations = listOf(shadowImplementation)
            }
        }
    }

}
