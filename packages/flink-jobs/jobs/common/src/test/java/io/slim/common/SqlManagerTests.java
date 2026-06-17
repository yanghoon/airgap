package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SqlManagerTests {
    
    @Test
    public void testSqlManager() {
        var jobContext = JobContext.create();
        var sqlManager = jobContext.getSqlManager();
        assertNotNull(sqlManager);

        var schemas = sqlManager.getRenderedSchemas();
        var datas = sqlManager.getRenderedPipelines();

        assertEquals(3, schemas.size());
        assertEquals(2, datas.size());
    }

}
