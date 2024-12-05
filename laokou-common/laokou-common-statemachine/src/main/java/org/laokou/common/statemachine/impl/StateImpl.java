package org.laokou.common.statemachine.impl;

import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.Transition;
import org.laokou.common.statemachine.Visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * StateImpl
 *
 * @author Frank Zhang 2020-02-07 11:19 PM
 */
public class StateImpl<S,E,C> implements State<S,E,C> {
    protected final S stateId;
    private final EventTransitions<S,E,C> eventTransitions = new EventTransitions<>();

    StateImpl(S stateId){
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S,E,C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<Transition<S, E, C>> addTransitions(E event, List<State<S, E, C>> targets, TransitionType transitionType) {
        List<Transition<S, E, C>> result = new ArrayList<>();
        for (State<S, E, C> target : targets) {
            Transition<S, E, C> secTransition = addTransition(event, target, transitionType);
            result.add(secTransition);
        }

        return result;
    }

    @Override
    public List<Transition<S, E, C>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public Collection<Transition<S, E, C>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
	public boolean equals(Object anObject){
		if(anObject instanceof State<?,?,?> state){
			return Objects.equals(this.stateId, state.getId());
		}
		return false;
	}

    @Override
    public String toString(){
        return stateId.toString();
    }
}
