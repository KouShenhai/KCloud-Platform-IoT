package org.laokou.common.core.utils;

import java.util.UUID;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.StringConstant.ROD;

/**
 * @author laokou
 */
public final class UUIDGenerator {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace(ROD, EMPTY);
    }

}
