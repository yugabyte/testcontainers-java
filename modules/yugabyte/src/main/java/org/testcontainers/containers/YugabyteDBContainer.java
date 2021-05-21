package org.testcontainers.containers;

import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class YugabyteDBContainer extends JdbcDatabaseContainer<YugabyteDBContainer> {
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("yugabytedb/yugabyte");
    public static final String IMAGE_TAG = "2.7.0.0-b17";
    public static final String NAME = "yugabytedb";

    public static final String IMAGE = "yugabyte";
    public static final Integer YSQL_PORT = 5433;
    public static final Integer YCQL_PORT = 9042;
    public static final Integer REST_API_PORT = 7000;

    private String databaseName = "yugabyte";
    private String username = "yugabyte";
    private String password = "yugabyte";

    public YugabyteDBContainer(final String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public YugabyteDBContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);

        addFixedExposedPort(REST_API_PORT, REST_API_PORT);
        addFixedExposedPort(YSQL_PORT, YSQL_PORT);
        addFixedExposedPort(YCQL_PORT, YCQL_PORT);
        withExposedPorts(REST_API_PORT, YSQL_PORT, YCQL_PORT);
        withCommand("bin/yugabyted", "start", "--daemon=false");

        this.waitStrategy = new LogMessageWaitStrategy()
            .withRegEx(".*✅ System checks.*")
            .withStartupTimeout(Duration.of(60, SECONDS));
    }

    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getJdbcUrl() {
        String additionalUrlParams = constructUrlParameters("?", "&");
        return "jdbc:postgresql://" + getContainerIpAddress() + ":" + getMappedPort(YSQL_PORT)
            + "/" + databaseName + additionalUrlParams;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    protected String getTestQueryString() {
        return "SELECT 1";
    }

    @Override
    public YugabyteDBContainer withDatabaseName(final String databaseName) {
        throw new UnsupportedOperationException("The YugabyteDB docker image does not currently support creating database on docker run command. Default database is 'yugabyte', you don't need to specify .withDatabaseName() in default case");
    }

    @Override
    public YugabyteDBContainer withUsername(final String username) {
        throw new UnsupportedOperationException("The YugabyteDB docker image does not currently support creating username on docker run command. Default username is 'yugabyte', you don't need to specify .withUsername() in default case.");
    }

    @Override
    public YugabyteDBContainer withPassword(final String password) {
        throw new UnsupportedOperationException("The YugabyteDB docker image does not currently support creating password on docker run command. Default password is 'yugabyte', you don't need to specify .withPassword() in default case. ");
    }

    @Override
    protected void waitUntilContainerStarted() {
        getWaitStrategy().waitUntilReady(this);
    }

}
