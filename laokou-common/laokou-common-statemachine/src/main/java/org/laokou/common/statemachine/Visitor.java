package org.laokou.common.statemachine;

/**
 * Visitor
 *
 * @author Frank Zhang 2020-02-08 8:41 PM
 */
public interface Visitor {

    char LF = '\n';

    /**
     * @param visitable the element to be visited.
     * @return 字符串
     */
    String visitOnEntry(StateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return 字符串
     */
    String visitOnExit(StateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return 字符串
     */
    String visitOnEntry(State<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return 字符串
     */
    String visitOnExit(State<?, ?, ?> visitable);
}
