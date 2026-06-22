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

        val platformLibs by configurations.creating {
            exclude(group = "org.slf4j", module = "slf4j-log4j12")
            exclude(group = "log4j", module = "log4j")
            exclude(group = "ch.qos.logback", module = "logback-classic")
            exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
            exclude(group = "commons-cli", module = "commons-cli")
        }

        afterEvaluate {
            val excludedStarters = setOf(
                ":starters:flink-core",
                ":starters:flink-table",
                ":starters:flink-logging"
            )

            configurations.named("implementation").get().dependencies
                .filterIsInstance<ProjectDependency>()
                .filter { it.dependencyProject.path.startsWith(":starters:") }
                .filterNot { it.dependencyProject.path in excludedStarters }
                .forEach { dependencies.add(platformLibs.name, it) }
        }

        val copyPlatformLibs by tasks.registering(Copy::class) {
            val targetDir = layout.buildDirectory.dir("platform-libs")
            doFirst { mkdir(targetDir) }
            from(platformLibs)
            into(targetDir)
        }

        // 
        pluginManager.withPlugin("com.gradleup.shadow") {
            val shadowImplementation by configurations.creating {
                description = "Dependencies that will be bundled into the Shadow/Fat JAR"
            }

            configurations.named("implementation") {
                extendsFrom(shadowImplementation)
            }

            tasks.withType<ShadowJar>().configureEach {
                dependsOn(copyPlatformLibs)

                configurations = listOf(shadowImplementation)
            }
        }
    }

}
