package org.laokou.common.statemachine;

/**
 * Visitable
 *
 * @author Frank Zhang 2020-02-08 8:41 PM
 */
public interface Visitable {
    String accept(final Visitor visitor);
}
