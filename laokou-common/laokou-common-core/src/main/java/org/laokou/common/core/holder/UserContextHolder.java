package org.laokou.common.core.holder;

public class UserContextHolder {
    private static final ThreadLocal<Long> USER_CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static void set(Long userId) {
        USER_CONTEXT_HOLDER.remove();
        USER_CONTEXT_HOLDER.set(userId);
    }

    public static Long get() {
        return USER_CONTEXT_HOLDER.get();
    }

}
