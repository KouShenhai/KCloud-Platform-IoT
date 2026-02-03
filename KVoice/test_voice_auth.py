# /*
#  * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
#  *
#  * Licensed under the Apache License, Version 2.0 (the "License");
#  * you may not use this file except in compliance with the License.
#  * You may obtain a copy of the License at
#  *
#  *   http://www.apache.org/licenses/LICENSE-2.0
#  *
#  * Unless required by applicable law or agreed to in writing, software
#  * distributed under the License is distributed on an "AS IS" BASIS,
#  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  * See the License for the specific language governing permissions and
#  * limitations under the License.
#  *
#  */

"""
语音认证系统测试程序
提供交互式测试界面，用于测试唤醒词检测和声纹识别准确率
"""

from datetime import datetime

from voice_auth import VoiceAuthSystem, AuthResult, AuthStats


def print_banner():
    """打印欢迎横幅"""
    banner = """
╔══════════════════════════════════════════════════════════════╗
║              🎤 语音认证系统测试工具 🎤                        ║
║                                                              ║
║  唤醒词: "你好小寇"                                           ║
║  功能: 声纹注册 + 唤醒词检测 + 声纹识别                       ║
╚══════════════════════════════════════════════════════════════╝
"""
    print(banner)


def print_menu():
    """打印主菜单"""
    menu = """
┌──────────────────────────────────────┐
│              📋 主菜单                │
├──────────────────────────────────────┤
│  1. 注册新用户声纹                    │
│  2. 删除用户                          │
│  3. 查看已注册用户                    │
│  4. 单次语音认证测试                  │
│  5. 连续监听测试模式                  │
│  6. 查看认证统计                      │
│  7. 重置统计数据                      │
│  0. 退出                              │
└──────────────────────────────────────┘
"""
    print(menu)


def get_user_input(prompt: str, default: str = None) -> str:
    """获取用户输入"""
    if default:
        prompt = f"{prompt} [{default}]: "
    else:
        prompt = f"{prompt}: "

    value = input(prompt).strip()
    return value if value else (default or "")


class VoiceAuthTester:
    """语音认证测试器"""

    def __init__(self):
        print("正在初始化系统，请稍候...")
        self.system = VoiceAuthSystem(command_handler=self.on_command)
        print("✅ 系统初始化完成!\n")

    def on_command(self, user_id: str, text: str):
        """命令执行回调"""
        user_info = self.system.user_manager.get_user(user_id)
        user_name = user_info.get('name', user_id) if user_info else user_id

        print("\n" + "🎉" * 20)
        print(f"  🎯 命令执行成功!")
        print(f"  👤 授权用户: {user_name}")
        print(f"  📝 语音内容: \"{text}\"")
        print("🎉" * 20 + "\n")

    def run(self):
        """运行测试程序"""
        print_banner()

        while True:
            print_menu()
            choice = get_user_input("请选择操作")

            if choice == "1":
                self.register_user()
            elif choice == "2":
                self.remove_user()
            elif choice == "3":
                self.list_users()
            elif choice == "4":
                self.single_test()
            elif choice == "5":
                self.continuous_test()
            elif choice == "6":
                self.show_stats()
            elif choice == "7":
                self.reset_stats()
            elif choice == "0":
                print("\n👋 再见!")
                break
            else:
                print("⚠️ 无效选项，请重新选择")

    def register_user(self):
        """注册新用户"""
        print("\n" + "=" * 50)
        print("📝 注册新用户")
        print("=" * 50)

        user_id = get_user_input("请输入用户ID (英文/数字)")
        if not user_id:
            print("⚠️ 用户ID不能为空")
            return

        name = get_user_input("请输入用户姓名", user_id)

        print(f"\n准备注册用户: {name} (ID: {user_id})")
        confirm = get_user_input("确认注册? (y/n)", "y")

        if confirm.lower() == 'y':
            success = self.system.register_user(user_id, name)
            if success:
                print(f"\n🎉 用户 '{name}' 注册成功!")
            else:
                print(f"\n❌ 注册失败")
        else:
            print("已取消注册")

    def remove_user(self):
        """删除用户"""
        users = self.system.list_users()
        if not users:
            print("\n⚠️ 没有已注册的用户")
            return

        print("\n已注册用户:")
        for i, user in enumerate(users, 1):
            print(f"  {i}. {user['name']} (ID: {user['user_id']})")

        user_id = get_user_input("\n请输入要删除的用户ID")
        if not user_id:
            return

        confirm = get_user_input(f"确认删除用户 '{user_id}'? (y/n)", "n")
        if confirm.lower() == 'y':
            self.system.remove_user(user_id)

    def list_users(self):
        """列出所有用户"""
        users = self.system.list_users()

        print("\n" + "=" * 50)
        print(f"👥 已注册用户 ({len(users)} 人)")
        print("=" * 50)

        if not users:
            print("  (暂无注册用户)")
        else:
            for user in users:
                created = user.get('created_at', 'Unknown')
                if created != 'Unknown':
                    try:
                        dt = datetime.fromisoformat(created)
                        created = dt.strftime("%Y-%m-%d %H:%M")
                    except:
                        pass
                print(f"  • {user['name']} (ID: {user['user_id']})")
                print(f"    注册时间: {created}")

    def single_test(self):
        """单次测试"""
        print("\n" + "=" * 50)
        print("🎤 单次语音认证测试")
        print("=" * 50)

        if self.system.user_manager.user_count == 0:
            print("\n⚠️ 警告: 没有注册用户，将无法验证声纹")
            print("   请先注册用户 (选项 1)")
            confirm = get_user_input("\n继续测试? (y/n)", "y")
            if confirm.lower() != 'y':
                return

        print("\n请说 \"你好小寇\" 加上您想要执行的命令")
        print("例如: \"你好小寇，今天天气怎么样\"")

        duration = float(get_user_input("录音时长(秒)", "4"))

        input("\n按 Enter 开始录音...")

        event = self.system.single_authenticate(record_duration=duration)

        # 显示测试结论
        self._show_test_conclusion(event)

    def continuous_test(self):
        """连续测试模式"""
        print("\n" + "=" * 50)
        print("🎤 连续监听测试模式")
        print("=" * 50)

        if self.system.user_manager.user_count == 0:
            print("\n⚠️ 警告: 没有注册用户，将无法验证声纹")

        print("\n系统将持续监听语音输入")
        print("唤醒词: \"你好小寇\"")
        print("按 Ctrl+C 退出测试\n")

        duration = float(get_user_input("每次录音时长(秒)", "4"))

        input("\n按 Enter 开始监听...")

        self.system.start_listening(continuous=True, record_duration=duration)

        # 退出后显示统计
        print("\n测试结束，显示统计:")
        self.show_stats()

    def show_stats(self):
        """显示统计信息"""
        stats = self.system.get_stats()

        print("\n" + "=" * 50)
        print("📊 认证统计")
        print("=" * 50)

        print(f"\n📈 总体统计:")
        print(f"   总尝试次数: {stats.total_attempts}")

        print(f"\n🔊 唤醒词检测:")
        print(f"   检测成功: {stats.wake_word_detected}")
        print(f"   检测失败: {stats.wake_word_not_detected}")
        if stats.total_attempts > 0:
            print(f"   准确率: {stats.wake_word_accuracy:.1%}")

        print(f"\n🔐 声纹识别:")
        print(f"   认证成功: {stats.auth_success}")
        print(f"   认证失败: {stats.auth_failed}")
        if stats.wake_word_detected > 0:
            print(f"   成功率: {stats.auth_accuracy:.1%}")

        # 显示最近事件
        if stats.events:
            print(f"\n📝 最近 5 次认证事件:")
            for event in stats.events[-5:]:
                time_str = event.timestamp.strftime("%H:%M:%S")
                result_emoji = {
                    AuthResult.SUCCESS: "✅",
                    AuthResult.WAKE_WORD_NOT_DETECTED: "🔇",
                    AuthResult.VOICE_MISMATCH: "🚫",
                    AuthResult.USER_NOT_REGISTERED: "⚠️",
                    AuthResult.ERROR: "❌"
                }.get(event.result, "❓")

                print(f"   [{time_str}] {result_emoji} {event.result.value}")
                if event.transcribed_text:
                    print(f"              \"{event.transcribed_text[:30]}...\""
                          if len(event.transcribed_text) > 30
                          else f"              \"{event.transcribed_text}\"")

    def reset_stats(self):
        """重置统计"""
        confirm = get_user_input("确认重置所有统计数据? (y/n)", "n")
        if confirm.lower() == 'y':
            self.system.stats = AuthStats()
            print("✅ 统计数据已重置")

    def _show_test_conclusion(self, event):
        """显示测试结论"""
        print("\n" + "=" * 50)
        print("📋 测试结论")
        print("=" * 50)

        if event.result == AuthResult.WAKE_WORD_NOT_DETECTED:
            print("\n❌ 唤醒词检测失败")
            print("   原因: 未在语音中检测到 \"你好小寇\"")
            print(f"   识别内容: \"{event.transcribed_text}\"")
            print("\n💡 建议:")
            print("   - 确保清晰地说出 \"你好小寇\"")
            print("   - 减少背景噪音")
            print("   - 靠近麦克风说话")

        elif event.result == AuthResult.USER_NOT_REGISTERED:
            print("\n⚠️ 无法验证声纹")
            print("   原因: 系统中没有注册用户")
            print("\n💡 建议:")
            print("   - 先注册用户声纹 (选项 1)")

        elif event.result == AuthResult.VOICE_MISMATCH:
            print("\n🚫 声纹验证失败")
            print("   原因: 您的声纹与注册用户不匹配")
            print(f"   最高相似度: {event.similarity_score:.1%}")
            print("\n💡 这意味着:")
            print("   - 如果您是注册用户: 可能是环境噪音影响，请重试")
            print("   - 如果您未注册: 系统正确拒绝了未授权访问")

        elif event.result == AuthResult.SUCCESS:
            user_info = self.system.user_manager.get_user(event.matched_user)
            user_name = user_info.get('name', event.matched_user) if user_info else event.matched_user

            print("\n✅ 认证成功!")
            print(f"   授权用户: {user_name}")
            print(f"   声纹相似度: {event.similarity_score:.1%}")
            print(f"   唤醒词置信度: {event.wake_word_confidence:.1%}")


def main():
    """主函数"""
    try:
        tester = VoiceAuthTester()
        tester.run()
    except KeyboardInterrupt:
        print("\n\n👋 程序已退出")
    except Exception as e:
        print(f"\n❌ 程序错误: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()
