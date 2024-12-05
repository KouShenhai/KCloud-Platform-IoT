package org.laokou.common.ruleengine.core;

import org.laokou.common.ruleengine.api.Facts;
import org.laokou.common.ruleengine.api.Rule;
import java.util.Collections;

/**
 * @author cola
 */
public class AllRules  extends CompositeRule{

    public static CompositeRule allOf(Rule... rules) {
        CompositeRule instance = new AllRules();
        Collections.addAll(instance.rules, rules);
        return instance;
    }

    @Override
    public boolean evaluate(Facts facts) {
		return rules.stream().allMatch(rule -> rule.evaluate(facts));
	}

    @Override
    public void execute(Facts facts) {
        for (Rule rule : rules) {
            rule.execute(facts);
        }
    }

    @Override
    protected boolean doApply(Facts facts) {
        if (evaluate(facts)) {
            for (Rule rule : rules) {
                //所有的rules都执行
                rule.execute(facts);
            }
            return true;
        }
        return false;
    }
}
