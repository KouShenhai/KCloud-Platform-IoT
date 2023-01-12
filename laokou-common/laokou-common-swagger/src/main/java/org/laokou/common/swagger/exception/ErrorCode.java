/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.swagger.exception;

/**
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * 如：10001（10代表系统模块，001代表业务代码）
 * 错误编码
 * @author laokou
 */
public interface ErrorCode {
    /**
     * 内部服务器错误
     */
    int INTERNAL_SERVER_ERROR = 500;
    /**
     * 未授权
     */
    int UNAUTHORIZED = 401;
    /**
     * 没有权限
     */
    int FORBIDDEN = 403;
    /**
     * 不为空
     */
    int NOT_NULL = 10001;
    /**
     * 数据库记录存在
     */
    int DB_RECORD_EXISTS = 10002;
    /**
     * 获取参数失败
     */
    int PARAMS_GET_ERROR = 10003;
    /**
     * 帐户或密码错误
     */
    int ACCOUNT_PASSWORD_ERROR = 10004;
    /**
     * 帐户禁用
     */
    int ACCOUNT_DISABLE = 10005;
    /**
     * 唯一标识不为空
     */
    int IDENTIFIER_NOT_NULL = 10006;
    /**
     * 验证码错误
     */
    int CAPTCHA_ERROR = 10007;
    /**
     * 子菜单存在
     */
    int SUB_MENU_EXIST = 10008;
    /**
     * 密码错误
     */
    int PASSWORD_ERROR = 10009;
    /**
     * 帐户不存在
     */
    int ACCOUNT_NOT_EXIST = 10010;
    /**
     * 上级部门错误
     */
    int SUPERIOR_DEPT_ERROR = 10011;
    /**
     * 高级菜单错误
     */
    int SUPERIOR_MENU_ERROR = 10012;
    /**
     * 数据范围参数错误
     */
    int DATA_SCOPE_PARAMS_ERROR = 10013;
    /**
     * 部门子部门删除错误
     */
    int DEPT_SUB_DELETE_ERROR = 10014;
    /**
     * 部门用户删除错误
     */
    int DEPT_USER_DELETE_ERROR = 10015;
    /**
     * 动作部署错误
     */
    int ACT_DEPLOY_ERROR = 10016;
    /**
     * 动作模式错误
     */
    int ACT_MODEL_IMG_ERROR = 10017;
    /**
     *  ACT_模型导出错误
     */
    int ACT_MODEL_EXPORT_ERROR = 10018;
    /**
     * 上传文件为空
     */
    int UPLOAD_FILE_EMPTY = 10019;
    /**
     * 授权不为空
     */
    int AUTHORIZATION_NOT_NULL = 10020;
    /**
     * 授权无效
     */
    int AUTHORIZATION_INVALID = 10021;
    /**
     * 帐户已锁定
     */
    int ACCOUNT_LOCK = 10022;
    /**
     * ACT_DEPLOY_格式错误
     */
    int ACT_DEPLOY_FORMAT_ERROR = 10023;
    /**
     * OSS上传文件错误
     */
    int OSS_UPLOAD_FILE_ERROR = 10024;
    /**
     * 发送短信出错
     */
    int SEND_SMS_ERROR = 10025;
    /**
     * 邮件模板不存在
     */
    int MAIL_TEMPLATE_NOT_EXISTS = 10026;
    /**
     * REDIS_错误
     */
    int REDIS_ERROR = 10027;
    /**
     * 作业错误
     */
    int JOB_ERROR = 10028;
    /**
     * 无效的符号
     */
    int INVALID_SYMBOL  = 10029;
    /**
     * JSON_格式错误
     */
    int JSON_FORMAT_ERROR = 10030;
    /**
     * 短信配置错误
     */
    int SMS_CONFIG_ERROR = 10031;
    /**
     * 任务已被签收，操作失败
     */
    int TASK_CLIME_FAIL = 10032;
    /**
     * 不存在的流程定义
     */
    int NONE_EXIST_PROCESS = 10033;
    /**
     * 上级节点不存在
     */
    int SUPERIOR_NOT_EXIST = 10034;
    /**
     * 驳回
     */
    int REJECT_MESSAGE = 10035;
    /**
     * 回退
     */
    int ROLLBACK_MESSAGE = 10036;
    /**
     * 任务没有分组，无法取消认领
     */
    int UNCLAIM_ERROR_MESSAGE = 10037;
    /**
     * 上级区域选择错误
     */
    int SUPERIOR_REGION_ERROR = 10038;
    /**
     * 请先删除下级区域
     */
    int REGION_SUB_DELETE_ERROR = 10039;
    /**
     * 流程已挂起，不能启动实例
     */
    int PROCESS_START_ERROR = 10040;
    /**
     * 多实例任务不能驳回
     */
    int REJECT_PROCESS_PARALLEL_ERROR = 10041;
    /**
     * 存在多个处理中的任务，不能驳回
     */
    int REJECT_PROCESS_HANDLEING_ERROR = 10042;
    /**
     * 多实例任务不能终止
     */
    int END_PROCESS_PARALLEL_ERROR = 10043;
    /**
     * 存在多个处理中的任务，不能终止流程
     */
    int END_PROCESS_HANDLEING_ERROR = 10044;
    /**
     * 终止
     */
    int END_PROCESS_MESSAGE = 10045;
    /**
     * 多实例任务不能回退
     */
    int BACK_PROCESS_PARALLEL_ERROR = 10046;
    /**
     * 存在多个并行执行的任务，不能回退
     */
    int BACK_PROCESS_HANDLEING_ERROR = 10047;
    /**
     * 你没有权限访问，请联系管理员
     */
    int NOT_PERMISSIONS = 20001;
    /**
     * 服务正在维护，请联系管理员
     */
    int SERVICE_MAINTENANCE = 20002;
    /**
     * 账号或密码解密失败，请检查密钥
     */
    int DECRYPT_FAIL = 20003;
    /**
     * 账号不为空
     */
    int USERNAME_NOT_NULL = 20004;
    /**
     * 密码不为空
     */
    int PASSWORD_NOT_NULL = 20005;
    /**
     * 验证码不为空
     */
    int CAPTCHA_NOT_NULL = 20006;
    /**
     * 无效客户端凭据
     */
    int INVALID_CLIENT = 20007;
    /**
     * 客户端未授权
     */
    int UNAUTHORIZED_CLIENT = 20008;
    /**
     * 无效授权
     */
    int INVALID_GRANT = 20009;
    /**
     * 无效作用域
     */
    int INVALID_SCOPE = 20010;
    /**
     * 无效令牌
     */
    int INVALID_TOKEN = 20011;
    /**
     * 无效请求
     */
    int INVALID_REQUEST = 20012;
    /**
     * 不支持的认证类型
     */
    int UNSUPPORTED_GRANT_TYPE = 20013;
    /**
     * 不支持的资源类型
     */
    int UNSUPPORTED_RESPONSE_TYPE = 20014;
    /**
     * 访问被拒绝
     */
    int ACCESS_DENIED = 20015;
}
