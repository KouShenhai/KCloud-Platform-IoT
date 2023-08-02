///*
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//package org.laokou.auth.server.application.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.laokou.auth.client.vo.IdempotentToken;
//import org.laokou.auth.client.vo.SecretInfoVO;
//import org.laokou.common.core.vo.OptionVO;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * auth服务
// *
// * @author laokou
// */
//public interface SysAuthApplicationService {
//
//	/**
//	 * 验证码
//	 * @param request 请求参数
//	 * @return String
//	 */
//	String captcha(HttpServletRequest request);
//
//	/**
//	 * 退出登录
//	 * @param request 请求参数
//	 * @return Boolean
//	 */
//	Boolean logout(HttpServletRequest request);
//
//	/**
//	 * 下拉列表
//	 * @return List<OptionVO>
//	 */
//	List<OptionVO> getOptionList();
//
//	/**
//	 * 获取密钥配置
//	 * @return 密钥配置
//	 * @throws IOException IO异常
//	 */
//	SecretInfoVO getSecretInfo() throws IOException;
//
//	/**
//	 * 接口幂等性令牌
//	 * @return IdempotentToken
//	 */
//	IdempotentToken idempotentToken();
//
//}
