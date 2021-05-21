package org.testcontainers;

import org.testcontainers.utility.DockerImageName;

public interface YugabyteDBTestImage {
//    DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2.7.1.0-b131");
    DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2.7.0.0-b17");
}

