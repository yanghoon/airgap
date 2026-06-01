rootProject.name = "flink-jobs"

// Jobs
include(":jobs:counter-job")
include(":jobs:slim-kafka-print-job")

// Starters - Flink Core
include(":starters:flink-dependencies")
include(":starters:flink-core")
include(":starters:flink-table")
include(":starters:flink-logging")
include(":starters:flink-test")

// Starters - Libraries
include(":starters:flink-kafka")

// Libraries
include(":jobs:common")
