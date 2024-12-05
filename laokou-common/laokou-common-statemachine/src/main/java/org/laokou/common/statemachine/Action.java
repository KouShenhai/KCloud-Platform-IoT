package org.laokou.common.statemachine;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action} with a {@link StateContext}.
 *
 * @author Frank Zhang 2020-02-07 2:51 PM
 */
public interface Action<S, E, C> {

    void execute(S from, S to, E event, C context);

}
