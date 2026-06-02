package io.slim;

import io.slim.common.JobContext;

public class Main {
    
    public static void main(String[] args) {
        // System.out.println(Config.get());
        // System.out.println(ConfigProvider.getConfig());
        System.out.println(JobContext.create());
    }

}
