package com.airgap.flink;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

public class CounterSource extends RichSourceFunction<Long> {

    private volatile boolean isRunning = true;
    private long counter = 0;

    @Override
    public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning) {
            ctx.collect(counter);
            counter++;
            Thread.sleep(5000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}