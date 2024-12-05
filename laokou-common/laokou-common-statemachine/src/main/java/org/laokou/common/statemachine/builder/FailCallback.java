package org.laokou.common.statemachine.builder;

/**
 * FailCallback
 *
 * @author 龙也 2022/9/15 12:02 PM
 */
@FunctionalInterface
public interface FailCallback<S, E, C> {

    /**
     * Callback function to execute if failed to trigger an Event
     */
    void onFail(S sourceState, E event, C context);
}
