package org.laokou.common.statemachine.impl;

import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.StateMachine;
import org.laokou.common.statemachine.Transition;
import org.laokou.common.statemachine.Visitor;

/**
 * SysOutVisitor
 *
 * @author Frank Zhang 2020-02-08 8:48 PM
 */
public class SysOutVisitor implements Visitor {

    @Override
    public String visitOnEntry(StateMachine<?, ?, ?> stateMachine) {
		return "-----StateMachine:"+stateMachine.getMachineId()+"-------";
    }

    @Override
    public String visitOnExit(StateMachine<?, ?, ?> stateMachine) {
		return "------------------------";
    }

    @Override
    public String visitOnEntry(State<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        String stateStr = "State:"+state.getId();
        sb.append(stateStr).append(LF);
        for(Transition<?, ?, ?> transition: state.getAllTransitions()){
            String transitionStr = "    Transition:"+transition;
            sb.append(transitionStr).append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?, ?> visitable) {
        return "";
    }
}
