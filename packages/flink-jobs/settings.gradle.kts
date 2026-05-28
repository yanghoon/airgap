rootProject.name = "flink-jobs"

// Jobs
include(":jobs:counter-job")
include(":jobs:slim-kafka-print-job")

// Starters - Flink Core
include(":starters:flink-dependencies")
include(":starters:flink-core")
include(":starters:flink-table")
include(":starters:flink-logging")

// Starters - Libraries
include(":starters:flink-kafka")
