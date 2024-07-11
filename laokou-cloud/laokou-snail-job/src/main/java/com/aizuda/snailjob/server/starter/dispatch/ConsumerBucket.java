package com.aizuda.snailjob.server.starter.dispatch;

import lombok.Data;

import java.util.Set;

/**
 * @author opensnail
 * @since 2.4.0
 */
@Data
public class ConsumerBucket {

	private Set<Integer> buckets;

}
