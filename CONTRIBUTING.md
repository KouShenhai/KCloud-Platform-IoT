## 代码贡献
欢迎各路英雄好汉参与KCloud-Platform-Alibaba代码贡献，期待您的加入！Fork本仓库 新建 feat_xxx_环境_时间（如 feat_laokou_dev_20230116） 分支提交代码，新建Pull Request！我觉得没问题就会合并到主干分支，你也就成为正式贡献者啦！    

## RESTFul代码风格
- url路径名词均为复数
- 响应结果必须使用JSON
- url结尾不应该包含斜杠'/'
- API必须有版本的概念，v1，v2，v3
- url中大小写不敏感，不要出现大写字母
- 使用-而不是使用_做URL路径中字符串连接（脊柱命名法）
- 使用Token令牌来做用户身份的校验与权限分级，而不是Cookie
- 使用动词表示增删改查， GET查询，POST新增，PUT更新，DELETE删除
- HTTP状态码，在REST中都有特定的意义：200，201，202，204，400，401，403，500。比如401表示用户身份认证失败，403表示没有操作资源的权限

## 提交规范
- fix：修补bug
- test：增加测试
- feat：新功能(feature)
- docs：文档(documentation)
- chore：构建过程或辅助工具的变动
- style：格式(不影响代码运行的改动)
- refactor：重构(即不是新增功能,也不是修改bug的代码变动)