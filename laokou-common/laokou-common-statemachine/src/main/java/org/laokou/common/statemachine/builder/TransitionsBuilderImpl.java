package org.laokou.common.statemachine.builder;

import org.laokou.common.statemachine.Action;
import org.laokou.common.statemachine.Condition;
import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.Transition;
import org.laokou.common.statemachine.impl.StateHelper;
import org.laokou.common.statemachine.impl.TransitionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TransitionsBuilderImpl
 *
 * @author Frank Zhang 2020-02-08 7:43 PM
 */
public class TransitionsBuilderImpl<S,E,C> extends AbstractTransitionBuilder<S,E,C> implements ExternalTransitionsBuilder<S,E,C> {
    /**
     * This is for fromAmong where multiple sources can be configured to point to one target
     */
    private final List<State<S, E, C>> sources = new ArrayList<>();

    private final List<Transition<S, E, C>> transitions = new ArrayList<>();

    public TransitionsBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        super(stateMap, transitionType);
    }

    @SafeVarargs
	@Override
    public final From<S, E, C> fromAmong(S... stateIds) {
        for(S stateId : stateIds) {
            sources.add(StateHelper.getState(super.stateMap, stateId));
        }
        return this;
    }

    @Override
    public On<S, E, C> on(E event) {
        for(State<S, E, C> source : sources) {
            Transition<S, E, C> transition = source.addTransition(event, super.target, super.transitionType);
            transitions.add(transition);
        }
        return this;
    }

    @Override
    public When<S, E, C> when(Condition<C> condition) {
        for(Transition<S, E, C> transition : transitions){
            transition.setCondition(condition);
        }
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        for(Transition<S, E, C> transition : transitions){
            transition.setAction(action);
        }
    }
}
