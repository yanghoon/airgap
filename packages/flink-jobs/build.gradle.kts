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
