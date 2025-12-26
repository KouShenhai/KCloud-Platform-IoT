```shell
# 发布Github
mvn clean deploy -P snapshot -DskipTests
# 发布Maven Central
mvn clean deploy -P release -DskipTests
```
