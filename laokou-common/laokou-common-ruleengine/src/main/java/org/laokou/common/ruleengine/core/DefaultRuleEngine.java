package org.laokou.common.ruleengine.core;

import org.laokou.common.ruleengine.api.Facts;
import org.laokou.common.ruleengine.api.Rule;
import org.laokou.common.ruleengine.api.RuleEngine;
/**
 * @author cola
 */
public class DefaultRuleEngine implements RuleEngine {

    @Override
    public void fire(Rule rule, Facts facts) {
        if (rule == null) {
            return;
        }
        rule.apply(facts);
    }
}
