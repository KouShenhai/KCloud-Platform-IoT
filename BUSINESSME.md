# 示例仓库

示例仓库内包括：

- 快速开始指南

- Gitee推荐的代码协作开发模式

- 如何运用Gitee企业版结合项目管理和代码协作

## 一、快速开始

如果你已经是Git的熟练使用者，直接使用下面的地址来连接远程仓库：

`git@gitee.com:laokou-yun/sample_repository.git`

我们强烈建议所有的Git仓库都有一个 README, LICENSE, .gitignore文件。

详细的教程请查看：

1、[Git入门](https://gitee.com/help/articles/4114)

2、[Visual Studio](https://gitee.com/help/articles/4118) / [TortoiseGit](https://my.oschina.net/longxuu/blog/141699) / [Eclipse](https://gitee.com/help/articles/4119) / [Xcode](https://my.oschina.net/zxs/blog/142544)下如何连接Gitee

3、[个人和组织的Gitee仓库如何导入企业](https://gitee.com/help/articles/4155)

**简易的命令行入门教程**

1、命令行进行本地 Git 全局设置（yourname = Gitee中的用户名，your@email.com = 在Gitee中绑定的邮箱）: 

```
git config --global user.name "yourname"
git config --global user.email "your@email.com"
```

2、命令行本地创建 Git 仓库并推送到 Gitee 远程仓库（repo_name即为Git仓库所在的文件夹名称）:

```
mkdir repo_name
cd repo_name
git init
touch README.md
git add README.md
git commit -m "first commit"
git remote add origin https://gitee.com/your_enterprise_name/test_warehouse.git
git push -u origin master
```
3、命令行推送本地已有 Git 仓库到 Gitee 远程仓库（repo_name即为Git仓库所在的文件夹名称）：

```
cd repo_name
git remote add origin https://gitee.com/your_enterprise_name/test_warehouse.git
git push -u origin master
```

## 二、协作开发：Fork + Pull Request
​Gitee 中的开发协作，最常用和推荐的方式是“Fork + Pull”模式。在“Fork + Pull”模式下，仓库参与者不必向仓库创建者申请提交权限，而是在自己的托管空间下建立仓库的派生（Fork）。至于在派生仓库中创建的提交，可以非常方便地利用 Gitee 的 Pull Request 工具向原始仓库的维护者发送 Pull Request。

### 1、什么是 Pull Request？

Pull Request 是两个仓库 或 同仓库内不同分支 之间提交变更的一种途径，同时也是一种非常好的团队协作方式，常用于团队的代码审查等场景。下面，就来讲解如何在 Gitee 平台提交 Pull Request。

### 2、如何 fork 仓库

fork 仓库时非常简单的，进到仓库页面，然后找到右上角的 fork 按钮，点击后选择 fork 到的命名空间，再点击确认，等待系统在后台完成仓库克隆操作，就完成了 fork 操作，如图：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/173906_0889e8a4_4838521.png "在这里输入图片标题")

### 3、如何提交 Pull Request

首先，您的仓库与目标仓库必须存在差异，这样才能提交,比如这样：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/173954_606bdc43_4838521.png "屏幕截图.png")

如果不存在差异，或者目标分支比你提Pull Request的分支还要新，则会得到这样的提示：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174035_5e81802b_4838521.png "屏幕截图.png")

然后，填入Pull Request的说明，点击提交Pull Request，就可以提交一个Pull Request了，就想下图所示的那样：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174129_c2222538_4838521.png "屏幕截图.png")

### 4、如何对已经存在的 Pull Request 的进行管理

首先，对于一个已经存在的 Pull Request，如果只是观察者，报告者等权限，那么访问将会受到限制，具体权限限制请参考 Gitee 平台关于角色权限的内容，下文涉及的部分，仅针对管理员权限，如果您发现不太一样的地方，请检查您的权限是不是管理员或该 Pull Request 的创建者。

### 5、如何修改一个已经存在的 Pull Request

点击 Pull Request 的详情界面右上角的编辑按钮，就会弹出编辑框，在编辑框中修改你需要修改的信息，然后点击保存即可修改该 Pull Request，如下图所示：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174204_a015d541_4838521.png "屏幕截图.png")

请注意，在该界面，可以对 Pull Request 进行指派负责人，指派测试者等等操作，每一个操作均会通知对应的人员

### 6、对 Pull Request 的 bug 修改如何提交到该 Pull Request 中

对于 Pull Request 中的 bug 修复或者任何更新动作，均不必要提交新的 Pull Request，仅仅只需要推送到您提交 Pull Request 的分支上，稍后我们后台会自动更新这些提交，将其加入到这个 Pull Request 中去

### 7、Pull Request 不能自动合并该如何处理

在提交完 Pull Request 的后，在这个 Pull Request 处理期间，由原本的能自动合并变成不能自动合并，这是一件非常正常的事情，那么，这时，我们有两种选择，一种，继续合并到目标，然后手动处理冲突部分，另一种则是先处理冲突，使得该 Pull Request 处于可以自动合并状态，然后采用自动合并，一般来讲，我们官方推荐第二种，即先处理冲突，然后再合并。具体操作为：

先在本地切换到提交 Pull Request 的分支，然后拉取目标分支到本地，这时，会发生冲突，参考如何处理代码冲突 这一小节将冲突处理完毕，然后提交到 Pull Request 所在的分支，等待系统后台完成Pull Request的更新后，Pull Request 就变成了可自动合并状态

### 8、Pull Request 不小心合并了，可否回退

对于错误合并的 Pull Request，我们提供了回退功能，该功能会产生一个回退 XXX 的 Pull Request，接受该 Pull Request 即可完成回退动作，注意，回退本质上是提交一个完全相反的 Pull Request，所以，你仍然需要进行测试来保证完整性，另，为了不破坏其他 Pull Request，建议只有需回退的 Pull Request 处于最后一次合并操作且往上再无提交时执行回退动作，否则请手动处理。

## 三、项目管理与协作开发相结合
在Gitee企业版中，可以创建【项目】和【任务】。采用 Fork + Pull Request 模式进行协作的团队，可以将代码推送与任务提交进行很好的结合。

通过 Pull Request 关联 Issue（也就是企业版中的【任务】），用户可以在关闭 Pull Request 的时候同时关闭 issue。关联功能具有以下特点：

1. 一个 PR 可以关联多个 issue，例如同时关联 issue1 , issue2 格式为：#issue1ident, #issue2dent
2. PR关联issue后，issue的状态会自动更改为进行中，当PR被合并后，issue会更改为关闭状态。
3. 个人版和企业版的区别： 
> - 个人版，PR只能关联当前仓库的任务 
> - 企业版，PR可以关联所有企业的任务。

## 具体通过Pull Request 关联 Issue操作如下：
#### 1. 在 pr 的内容里面指定需要关闭的 issue 的 ident ,例如：

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174251_f057453f_4838521.png "屏幕截图.png")

#### 2. 在 issue 详情页可以看到关联关系

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174343_3310747b_4838521.png "屏幕截图.png")

#### 3. 当 PR 合并之后其关联的 issue 被关闭

![输入图片说明](https://images.gitee.com/uploads/images/2020/0512/174401_e595fe50_4838521.png "屏幕截图.png")