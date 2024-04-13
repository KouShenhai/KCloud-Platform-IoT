package org.laokou.common.lock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TypeEnum", description = "类型枚举")
public enum TypeEnum {

		@Schema(name = "LOCK", description = "普通锁(默认)")
		LOCK,

		@Schema(name = "FAIR_LOCK", description = "公平锁")
		FAIR_LOCK,

		@Schema(name = "READ_LOCK", description = "读锁")
		READ_LOCK,

		@Schema(name = "WRITE_LOCK", description = "写锁")
		WRITE_LOCK,

		@Schema(name = "FENCED_LOCK", description = "强一致性锁(可以解决主从延迟)")
		FENCED_LOCK

}
