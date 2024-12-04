package org.laokou.common.statemachine.builder;

/**
 * StateMachineBuilderFactory
 *
 * @author Frank Zhang 2020-02-08 12:33 PM
 */
public class StateMachineBuilderFactory {
    public static <S, E, C> StateMachineBuilder<S, E, C> create(){
        return new StateMachineBuilderImpl<>();
    }
}
