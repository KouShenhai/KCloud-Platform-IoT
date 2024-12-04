package org.laokou.common.ruleengine.api;
/**
 * @author cola
 */
public interface RuleEngine {
    /**
     * Fire rule on given facts.
     */
    void fire(Rule rule, Facts facts);

}
