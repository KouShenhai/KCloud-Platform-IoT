package org.laokou.common.core.utils;

import java.security.SecureRandom;

/**
 * @author laokou
 */
public final class UniqueIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateId() {
        long timestamp = IdGenerator.SystemClock.now();
        int randomValue = RANDOM.nextInt(10000);
        return String.valueOf(timestamp) + randomValue;
    }

}
