package org.testcontainers.containers;

import org.testcontainers.utility.DockerImageName;

public class YugabyteDBContainerProvider extends JdbcDatabaseContainerProvider {
    @Override
    public boolean supports(String databaseType) {
        return databaseType.equals(YugabyteDBContainer.NAME);
    }

    @Override
    public JdbcDatabaseContainer newInstance() {
        return newInstance(YugabyteDBContainer.IMAGE_TAG);
    }

    @Override
    public JdbcDatabaseContainer newInstance(String tag) {
        return new YugabyteDBContainer(DockerImageName.parse(YugabyteDBContainer.IMAGE).withTag(tag));
    }
}
