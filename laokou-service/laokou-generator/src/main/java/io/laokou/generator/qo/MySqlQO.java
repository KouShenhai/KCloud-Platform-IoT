package io.laokou.generator.qo;

import org.apache.commons.lang.StringUtils;

/**
 * MySQL查询
 *

 */
public class MySqlQO implements AbstractQO {

    @Override
    public DbType dbType() {
        return DbType.MySQL;
    }

    @Override
    public String tablesSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select table_name, table_comment from information_schema.tables ");
        sql.append("where table_schema = (select database()) ");
        //表名查询
        if(StringUtils.isNotBlank(tableName)){
            sql.append("and table_name = '").append(tableName).append("' ");
        }
        sql.append("order by table_name asc");

        return sql.toString();
    }

    @Override
    public String tableName() {
        return "table_name";
    }

    @Override
    public String tableComment() {
        return "table_comment";
    }

    @Override
    public String tableFieldsSql() {
        return "select column_name, data_type, column_comment, column_key from information_schema.columns "
                + "where table_name = '%s' and table_schema = (select database()) order by ordinal_position";
    }

    @Override
    public String fieldName() {
        return "column_name";
    }

    @Override
    public String fieldType() {
        return "data_type";
    }

    @Override
    public String fieldComment() {
        return "column_comment";
    }

    @Override
    public String fieldKey() {
        return "column_key";
    }
}
