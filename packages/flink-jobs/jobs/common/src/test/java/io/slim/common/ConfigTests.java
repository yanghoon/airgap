package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import io.slim.common.context.ConfigFactory;
import io.smallrye.config.Config;
import io.smallrye.config.ConfigMapping;

public class ConfigTests {
    
    @Test
    public void testConfigLoading() {
        var config = Config.getOrCreate();
        var val = config.getValue("job.name", String.class);

        assertEquals("kafka-to-multi-sink", val);
    }

    @Test
    public void testConfigMapping() {
        var config = ConfigFactory.create(JobConfig.class);
        var jobConfig = config.getConfigMapping(JobConfig.class);

        assertEquals("kafka-to-multi-sink", jobConfig.name());
    }

    @Test
    public void testFailedDynamicMapping() {
        var config = ConfigFactory.create();
        var ex = assertThrows(
            NoSuchElementException.class,
            () -> config.getConfigMapping(JobConfig.class)
        );
        assertTrue(ex.getMessage().contains("SRCFG00027"));
    }

    @ConfigMapping(prefix = "job")
    public static interface JobConfig {
        String name();
    }

}
