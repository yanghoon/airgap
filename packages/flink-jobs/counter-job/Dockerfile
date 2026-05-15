FROM gradle:8-jdk17-alpine AS builder

WORKDIR /app

COPY *.gradle.kts .
COPY counter-job/src counter-job/src
COPY counter-job/*.gradle.kts counter-job/
RUN gradle :counter-job:build --no-daemon

FROM flink:1.20

RUN mkdir -p /opt/flink/usrlib
COPY --from=builder /app/counter-job/build/libs/counter-job-*-all.jar /opt/flink/usrlib/counter-job.jar
