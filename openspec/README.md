```shell
npm install -g @fission-ai/openspec@latest
openspec --version
```

```shell
npm install -g @anthropic-ai/claude-code
```

```shell
# https://github.com/ForceInjection/OpenSpec-practise/blob/main/docs/openspec-user-manual.md
cd KCloud-Platform-IoT
openspec init
openspec init --tools codex
openspec init --tools claude
openspec init --tools kiro
openspec init --tools qoder
# 提案
/opsx:propose <description>
# 验证
openspec validate <name>
# 实现（根据tasks.md）
/opsx:apply
# 归档
/opsx:archive
```
