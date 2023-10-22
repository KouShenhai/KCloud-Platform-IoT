package org.laokou.common.idempotent.utils;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.DEFAULT;

/**
 * 类名称：IdempotentUtils
 * <p>
 * 描述：幂等性工具
 *
 * @author why
 */
@Component
@RequiredArgsConstructor
public class IdempotentUtil {

	private final RedisUtil redisUtil;

	private static final ThreadLocal<Boolean> IS_IDEMPOTENT = new ThreadLocal<>();

	private static final ThreadLocal<Map<String, String>> REQUEST_ID = ThreadLocal.withInitial(HashMap::new);

	/**
	 * 得到幂等键
	 * @return {@link String }
	 */
	public String getIdempotentKey() {
		String idempotentKey = String.valueOf(IdGenerator.defaultSnowflakeId());
		String apiIdempotentKey = RedisKeyUtil.getApiIdempotentKey(idempotentKey);
		redisUtil.set(apiIdempotentKey, DEFAULT, RedisUtil.HOUR_ONE_EXPIRE);
		return idempotentKey;
	}

	public static Map<String, String> getRequestId() {
		return REQUEST_ID.get();
	}

	/**
	 * 是否是幂等接口
	 */
	public static boolean isIdempotent() {
		Boolean status = IS_IDEMPOTENT.get();
		return (status != null && status.equals(Boolean.TRUE));
	}

	/**
	 * 设置接口幂等 扩展方法: 用于开启子线程后设置子线程的幂等性状态, 以及定时任务等
	 */
	public static void setIdempotent() {
		IS_IDEMPOTENT.set(Boolean.TRUE);
	}

	/**
	 * 清理 不被servlet管理的和开启子线程的需要手动清理
	 */
	public static void cleanIdempotent() {
		IS_IDEMPOTENT.remove();
		REQUEST_ID.remove();
	}

}
