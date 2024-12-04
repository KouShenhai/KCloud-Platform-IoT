package org.laokou.common.ruleengine.api;

@FunctionalInterface
public interface Action {
    void execute(Facts facts);
}

