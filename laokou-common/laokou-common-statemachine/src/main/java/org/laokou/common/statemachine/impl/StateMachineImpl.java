package org.laokou.common.statemachine.impl;

import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.StateMachine;
import org.laokou.common.statemachine.Transition;
import org.laokou.common.statemachine.Visitor;
import org.laokou.common.statemachine.builder.FailCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

    private final Map<S, State<S, E, C>> stateMap;
    private String machineId;
    private boolean ready;

    private FailCallback<S, E, C> failCallback;

    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public boolean verify(S sourceStateId, E event) {
        isReady();

        State<S,E,C> sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        return transitions != null && !transitions.isEmpty();
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        isReady();
        Transition<S, E, C> transition = routeTransition(sourceStateId, event, ctx);

        if (transition == null) {
            Debugger.debug("There is no Transition for " + event);
            failCallback.onFail(sourceStateId, event, ctx);
            return sourceStateId;
        }

        return transition.transit(ctx, false).getId();
    }
    @Override
    public List<S> fireParallelEvent(S sourceState, E event, C context) {
        isReady();
        List<Transition<S, E, C>> transitions = routeTransitions(sourceState, event, context);
        List<S> result = new ArrayList<>();
        if (transitions == null||transitions.isEmpty()) {
            Debugger.debug("There is no Transition for " + event);
            failCallback.onFail(sourceState, event, context);
            result.add(sourceState);
            return result;
        }
        for (Transition<S, E, C> transition : transitions) {
            S id = transition.transit(context, false).getId();
            result.add(id);
        }
        return result;
    }

    private Transition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        State<S,E,C> sourceState = getState(sourceStateId);
        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);
        if (transitions == null || transitions.isEmpty()) {
            return null;
        }

        Transition<S, E, C> transit = null;
        for (Transition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }
    private List<Transition<S,E,C>> routeTransitions(S sourceStateId, E event, C context) {
        State<S,E,C> sourceState = getState(sourceStateId);
        List<Transition<S, E, C>> result = new ArrayList<>();
        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);
        if (transitions == null || transitions.isEmpty()) {
            return null;
        }

        for (Transition<S, E, C> transition : transitions) {
            Transition<S, E, C> transit = null;
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(context)) {
                transit = transition;
            }
            result.add(transit);
        }
        return result;
    }

    private State<S,E,C> getState(S currentStateId) {
		return StateHelper.getState(stateMap, currentStateId);
    }

    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (State<S,E,C> state : stateMap.values()) {
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUML() {
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        return accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setFailCallback(FailCallback<S, E, C> failCallback) {
        this.failCallback = failCallback;
    }
}
