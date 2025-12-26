```shell
# 发布GitHub【快照版本】
mvn clean deploy -P github-snapshot -DskipTests
# 发布Maven Central【快照版本】
mvn clean deploy -P maven-snapshot -DskipTests
```
