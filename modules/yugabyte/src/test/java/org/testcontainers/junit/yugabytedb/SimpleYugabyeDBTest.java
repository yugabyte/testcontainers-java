package org.testcontainers.junit.yugabytedb;

import org.junit.Test;
import org.testcontainers.containers.YugabyteDBContainer;
import org.testcontainers.db.AbstractContainerDatabaseTest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;
import static org.testcontainers.YugabyteDBTestImage.YUGABYTEDB_IMAGE;

public class SimpleYugabyeDBTest extends AbstractContainerDatabaseTest {
    static {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
    }

    @Test
    public void testSimple() throws SQLException {
        try (YugabyteDBContainer yugabyte = new YugabyteDBContainer(YUGABYTEDB_IMAGE)) {
            yugabyte.start();
            ResultSet resultSet = performQuery(yugabyte, "SELECT 1");
            int resultSetInt = resultSet.getInt(1);
            assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
        }
    }

    @Test
    public void testExplicitInitScript() throws SQLException {
        try (YugabyteDBContainer yugabyte = new YugabyteDBContainer(YUGABYTEDB_IMAGE)
            .withInitScript("somepath/init_postgresql.sql")) {
            yugabyte.start();

            ResultSet resultSet = performQuery(yugabyte, "SELECT foo FROM bar");

            String firstColumnValue = resultSet.getString(1);
            assertEquals("Value from init script should equal real value", "hello world", firstColumnValue);
        }
    }
}

