package com.airgap.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CounterJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new CounterSource())
            .map(counter -> {
                String timestamp = java.time.LocalDateTime.now().toString();
                return timestamp + " - Counter: " + counter;
            })
            .print();

        env.execute("Counter Job");
    }

}