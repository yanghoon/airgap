plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":starters:flink-dependencies")))

    api("org.junit.jupiter:junit-jupiter")
    api("org.junit.jupiter:junit-jupiter-api")
    api("org.junit.platform:junit-platform-launcher")
}
