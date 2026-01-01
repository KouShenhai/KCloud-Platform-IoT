/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.security.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * An <code>TransmittableThreadLocal</code>-based implementation of
 * {@link SecurityContextHolderStrategy}.
 *
 * @author Ben Alex
 * @author Rob Winch
 * @author laokou
 * @see ThreadLocal
 */
public final class TransmittableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

	private static final ThreadLocal<Supplier<SecurityContext>> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

	@Override
	public void clearContext() {
		CONTEXT_HOLDER.remove();
	}

	@NonNull
	@Override
	public SecurityContext getContext() {
		return getDeferredContext().get();
	}

	@Override
	public void setContext(@NonNull SecurityContext context) {
		Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
		CONTEXT_HOLDER.set(() -> context);
	}

	@NonNull
	@Override
	public Supplier<SecurityContext> getDeferredContext() {
		Supplier<SecurityContext> result = CONTEXT_HOLDER.get();
		if (result == null) {
			SecurityContext context = createEmptyContext();
			result = () -> context;
			CONTEXT_HOLDER.set(result);
		}
		return result;
	}

	@Override
	public void setDeferredContext(@NonNull Supplier<SecurityContext> deferredContext) {
		Assert.notNull(deferredContext, "Only non-null Supplier instances are permitted");
		Supplier<SecurityContext> notNullDeferredContext = () -> {
			SecurityContext result = deferredContext.get();
			Assert.notNull(result, "A Supplier<SecurityContext> returned null and is not allowed.");
			return result;
		};
		CONTEXT_HOLDER.set(notNullDeferredContext);
	}

	@NonNull
	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

}
