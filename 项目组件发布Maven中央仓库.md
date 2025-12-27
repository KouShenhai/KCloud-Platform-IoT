# Maven Central 正式版本发布指南

本文档说明如何使用 `maven-release` profile 将项目发布到 Maven Central Repository。

## 前置条件

### 1. 注册 Maven Central 账号

1. 访问 [Maven Central Portal](https://central.sonatype.com/)
2. 注册账号并验证域名或 GitHub 仓库所有权
3. 生成 User Token (用于认证)

### 2. 安装和配置 GPG

#### Windows 系统

1. 下载并安装 [Gpg4win](https://www.gpg4win.org/)
2. 验证安装:
   ```shell
   gpg --version
   ```

3. 生成 GPG 密钥对:
   ```shell
   gpg --gen-key
   ```
   按提示输入:
   - 真实姓名
   - 电子邮件地址 (建议使用 pom.xml 中配置的邮箱: 2413176044@qq.com)
   - 密码 (请妥善保管)

4. 查看密钥:
   ```shell
   gpg --list-keys
   ```

5. 发布公钥到密钥服务器:
   ```shell
   gpg --keyserver keyserver.ubuntu.com --send-keys <YOUR_KEY_ID>
   ```
6. 验证密钥
```shell
gpg --keyserver keyserver.ubuntu.com --recv-keys <YOUR_KEY_ID>
```

#### Linux/Mac 系统

```bash
# 安装 GPG (如果未安装)
# Ubuntu/Debian
sudo apt-get install gnupg

# macOS
brew install gnupg

# 生成密钥
gpg --gen-key

# 发布公钥
gpg --keyserver keyserver.ubuntu.com --send-keys <YOUR_KEY_ID>
```

### 3. 配置 Maven settings.xml

在 `~/.m2/settings.xml` (Windows: `C:\Users\<用户名>\.m2\settings.xml`) 中添加:

```xml
<settings>
  <servers>
    <!-- Maven Central 认证 -->
    <server>
      <id>kcloud-platform-iot-maven-central</id>
      <username>YOUR_CENTRAL_USERNAME</username>
      <password>YOUR_CENTRAL_TOKEN</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>gpg</id>
      <properties>
        <!-- GPG 密钥 ID (可选,如果有多个密钥) -->
        <gpg.keyname>YOUR_KEY_ID</gpg.keyname>
        <!-- GPG 密码 (不推荐明文存储,建议使用密码管理器或环境变量) -->
        <gpg.passphrase>YOUR_GPG_PASSPHRASE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

**安全建议**: 使用环境变量存储敏感信息:
```shell
# Windows PowerShell
$env:GPG_PASSPHRASE="your_passphrase"

# Linux/Mac
export GPG_PASSPHRASE="your_passphrase"
```

然后在 settings.xml 中使用:
```xml
<gpg.passphrase>${env.GPG_PASSPHRASE}</gpg.passphrase>
```

## 发布流程

### 1. 准备发布版本

确保 `pom.xml` 中的版本号是正式版本 (不含 `-SNAPSHOT` 或 `-M1` 等后缀):

```xml
<version>4.0.0</version>
```

### 2. 执行发布命令

#### 完整发布 (包含 GPG 签名)

```shell
# 发布GitHub【快照版本】
mvn clean deploy -P github-snapshot -DskipTests
# 发布Maven Central【快照版本】
mvn clean deploy -P maven-snapshot -DskipTests
# 发布Maven Central【正式版本】
mvn clean deploy -P maven-release -DskipTests
```

#### 跳过 GPG 签名 (测试用)

如果您还没有配置 GPG 或只是想测试构建流程:

```shell
mvn clean deploy -Pmaven-release -Dgpg.skip=true
```

**注意**: Maven Central 要求所有正式版本必须经过 GPG 签名,因此跳过签名的构建无法发布到 Maven Central。

### 3. 发布过程说明

执行 `mvn clean deploy -Pmaven-release` 时,Maven 会:

1. ✅ 清理之前的构建 (`clean`)
2. ✅ 编译源代码
3. ✅ 运行测试
4. ✅ 生成主 JAR 包
5. ✅ 生成源码 JAR (`-sources.jar`)
6. ✅ 生成 Javadoc JAR (`-javadoc.jar`)
7. ✅ 使用 GPG 签名所有 JAR 包
8. ✅ 上传到 Maven Central Staging Repository
9. ✅ 自动发布 (如果 `autoPublish=true`)

### 4. 验证发布

1. 登录 [Maven Central Portal](https://central.sonatype.com/)
2. 查看 "Deployments" 页面
3. 等待同步完成 (通常需要 10-30 分钟)
4. 在 [Maven Central Search](https://search.maven.org/) 搜索您的 artifact

## 配置说明

### maven-release Profile 包含的插件

#### 1. Central Publishing Plugin
```xml
<publishingServerId>kcloud-platform-iot-maven-central</publishingServerId>
<autoPublish>true</autoPublish>
<waitUntil>published</waitUntil>
```
- `autoPublish`: 自动发布到 Maven Central (设为 `false` 可手动审核后发布)
- `waitUntil`: 等待发布完成

#### 2. Maven Source Plugin
生成源码 JAR,Maven Central 要求必须包含。

#### 3. Maven Javadoc Plugin
生成 Javadoc JAR,Maven Central 要求必须包含。
- `doclint=none`: 忽略 Javadoc 警告
- `quiet=true`: 减少输出信息

#### 4. Maven GPG Plugin
使用 GPG 签名所有构建产物。
- `gpg.skip`: 可通过 `-Dgpg.skip=true` 跳过签名

## 常见问题

### Q1: GPG 签名失败 "Could not determine gpg version"

**原因**: 系统未安装 GPG 或 GPG 不在 PATH 中。

**解决方案**:
1. 安装 GPG (见前置条件)
2. 确保 `gpg` 命令在 PATH 中
3. 或者临时跳过签名: `-Dgpg.skip=true`

### Q2: GPG 签名时要求输入密码

**解决方案**:
1. 在 `settings.xml` 中配置 `gpg.passphrase`
2. 或使用环境变量 `GPG_PASSPHRASE`
3. 或在命令行添加: `-Dgpg.passphrase=YOUR_PASSWORD`

### Q3: 上传失败 "401 Unauthorized"

**原因**: Maven Central 认证失败。

**解决方案**:
1. 检查 `settings.xml` 中的 `<server>` 配置
2. 确认 `<id>` 与 pom.xml 中的 `publishingServerId` 一致
3. 验证 username 和 token 是否正确

### Q4: 发布后在 Maven Central 搜索不到

**原因**: 同步需要时间。

**解决方案**:
1. 等待 10-30 分钟
2. 检查 [Maven Central Portal](https://central.sonatype.com/) 的发布状态
3. 确认版本号是否正确

### Q5: Javadoc 生成失败

**解决方案**:
1. 配置已设置 `doclint=none` 忽略警告
2. 如果仍然失败,检查代码中的 Javadoc 注释格式
3. 临时禁用 Javadoc: 修改 profile 移除 `maven-javadoc-plugin`

## 版本管理建议

### 语义化版本

建议使用 [语义化版本](https://semver.org/lang/zh-CN/):
- `MAJOR.MINOR.PATCH` (例如: `4.0.0`)
- MAJOR: 不兼容的 API 变更
- MINOR: 向下兼容的功能新增
- PATCH: 向下兼容的问题修正

### 版本更新流程

1. 开发阶段使用 SNAPSHOT 版本:
   ```xml
   <version>4.1.0-SNAPSHOT</version>
   ```

2. 发布前更新为正式版本:
   ```xml
   <version>4.1.0</version>
   ```

3. 使用 versions-maven-plugin 批量更新版本:
   ```shell
   mvn versions:set -DnewVersion=4.1.0
   mvn versions:commit
   ```

## 完整发布检查清单

- [ ] 代码已提交到 Git
- [ ] 版本号已更新为正式版本
- [ ] 所有测试通过
- [ ] GPG 密钥已生成并发布到密钥服务器
- [ ] `settings.xml` 已正确配置
- [ ] 执行 `mvn clean deploy -Pmaven-release`
- [ ] 验证 Maven Central Portal 中的发布状态
- [ ] 在 Maven Central Search 中确认可搜索到
- [ ] 创建 Git tag: `git tag -a v4.0.0 -m "Release version 4.0.0"`
- [ ] 推送 tag: `git push origin v4.0.0`

## 相关链接

- [Maven Central Portal](https://central.sonatype.com/)
- [Maven Central 发布指南](https://central.sonatype.org/publish/publish-guide/)
- [GPG 签名指南](https://central.sonatype.org/publish/requirements/gpg/)
- [Gpg4win 下载](https://www.gpg4win.org/)
- [语义化版本规范](https://semver.org/lang/zh-CN/)
