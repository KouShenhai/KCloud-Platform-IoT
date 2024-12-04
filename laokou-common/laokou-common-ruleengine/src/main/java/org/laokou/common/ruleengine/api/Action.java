package org.laokou.common.ruleengine.api;

/**
 * @author cola
 */
@FunctionalInterface
public interface Action {
    void execute(Facts facts);
}

