/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.DisableInject;
import org.apache.dubbo.common.logger.ErrorTypeAwareLogger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.laokou.common.i18n.common.exception.GlobalException;

import java.lang.reflect.Method;

import static org.apache.dubbo.common.constants.LoggerCodeConstants.CONFIG_FILTER_VALIDATION_EXCEPTION;

/**
 * DubboExceptionInvokerFilter
 * <p>
 * Functions:
 * <ol>
 * <li>unexpected exception will be logged in ERROR level on provider side. Unexpected
 * exception are unchecked exception not declared on the interface</li>
 * <li>Wrap the exception not introduced in API package into RuntimeException. Framework
 * will serialize the outer exception but stringnize its cause in order to avoid of
 * possible serialization problem on client side</li>
 * </ol>
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter implements Filter, Filter.Listener {

	private ErrorTypeAwareLogger logger = LoggerFactory.getErrorTypeAwareLogger(DubboExceptionFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		return invoker.invoke(invocation);
	}

	@Override
	public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
		if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
			try {
				Throwable exception = appResponse.getException();

				// 自定义异常直接返回
				if (exception instanceof GlobalException) {
					return;
				}

				// directly throw if it's checked exception
				if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
					return;
				}
				// directly throw if the exception appears in the signature
				try {
					Method method = invoker.getInterface()
						.getMethod(RpcUtils.getMethodName(invocation), invocation.getParameterTypes());
					Class<?>[] exceptionClasses = method.getExceptionTypes();
					for (Class<?> exceptionClass : exceptionClasses) {
						if (exception.getClass().equals(exceptionClass)) {
							return;
						}
					}
				}
				catch (NoSuchMethodException e) {
					return;
				}

				// for the exception not found in method's signature, print ERROR message
				// in server's log.
				logger.error(CONFIG_FILTER_VALIDATION_EXCEPTION, "", "",
						"Got unchecked and undeclared exception which called by "
								+ RpcContext.getServiceContext().getRemoteHost() + ". service: "
								+ invoker.getInterface().getName() + ", method: " + RpcUtils.getMethodName(invocation)
								+ ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(),
						exception);

				// directly throw if exception class and interface class are in the same
				// jar file.
				String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
				String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
				if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
					return;
				}
				// directly throw if it's JDK exception
				String className = exception.getClass().getName();
				if (className.startsWith("java.") || className.startsWith("javax.")
						|| className.startsWith("jakarta.")) {
					return;
				}
				// directly throw if it's dubbo exception
				if (exception instanceof RpcException) {
					return;
				}

				// otherwise, wrap with RuntimeException and throw back to the client
				appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
			}
			catch (Throwable e) {
				logger.warn(CONFIG_FILTER_VALIDATION_EXCEPTION, "", "",
						"Fail to ExceptionFilter when called by " + RpcContext.getServiceContext().getRemoteHost()
								+ ". service: " + invoker.getInterface().getName() + ", method: "
								+ RpcUtils.getMethodName(invocation) + ", exception: " + e.getClass().getName() + ": "
								+ e.getMessage(),
						e);
			}
		}
	}

	@Override
	public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
		logger.error(CONFIG_FILTER_VALIDATION_EXCEPTION, "", "",
				"Got unchecked and undeclared exception which called by "
						+ RpcContext.getServiceContext().getRemoteHost() + ". service: "
						+ invoker.getInterface().getName() + ", method: " + RpcUtils.getMethodName(invocation)
						+ ", exception: " + e.getClass().getName() + ": " + e.getMessage(),
				e);
	}

	// For test purpose
	@DisableInject
	public void mockLogger(ErrorTypeAwareLogger logger) {
		this.logger = logger;
	}

}
