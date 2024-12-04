package org.laokou.common.ruleengine.core;

import org.laokou.common.ruleengine.api.Facts;
import org.laokou.common.ruleengine.api.Rule;
import org.laokou.common.ruleengine.api.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author cola
 */
public class DefaultRuleEngine implements RuleEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngine.class);

    @Override
    public void fire(Rule rule, Facts facts) {
        if (rule == null) {
            LOGGER.error("Rules is null! Nothing to apply");
            return;
        }
        rule.apply(facts);
    }
}
