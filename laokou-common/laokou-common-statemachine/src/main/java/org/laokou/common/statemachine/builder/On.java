package org.laokou.common.statemachine.builder;

import org.laokou.common.statemachine.Condition;

/**
 * On
 *
 * @author Frank Zhang 2020-02-07 6:14 PM
 */
public interface On<S, E, C> extends When<S, E, C>{
    /**
     * Add condition for the transition
     * @param condition transition condition
     * @return When clause builder
     */
    When<S, E, C> when(Condition<C> condition);
}
