/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.mybatisplus.config;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.laokou.common.mybatisplus.entity.BasePage;
import java.util.Map;
/**
 * @author laokou
 */
@Slf4j
public class DataFilterInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (parameter instanceof Map map) {
            try {
                Object qo = map.get("qo");
                if (qo instanceof BasePage basePage) {
                    // 获取aop拼接的sql
                    String sqlFilter = basePage.getSqlFilter();
                    // 获取select查询语句
                    Select select = (Select) CCJSqlParserUtil.parse(boundSql.getSql());
                    PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                    // 获取where
                    Expression expression = plainSelect.getWhere();
                    if (null == expression) {
                        plainSelect.setWhere(new StringValue(sqlFilter));
                    } else {
                        AndExpression andExpression = new AndExpression(expression,new StringValue(sqlFilter));
                        plainSelect.setWhere(andExpression);
                    }
                    String newSql = select.toString().replaceAll("'","").replaceAll("\"","'");
                    // 新sql写入
                    PluginUtils.mpBoundSql(boundSql).sql(newSql);
                }
            } catch (Exception ex) {}
        }
    }



}
