package io.laokou.admin.infrastructure.interceptor;
/**
 * 数据范围
 *
 * @author limingze
 * @create: 2022-07-13 09:45
 * @since 1.0.0
 */
public class DataScope {
    private String sqlFilter;

    public DataScope(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    public String getSqlFilter() {
        return sqlFilter;
    }

    public void setSqlFilter(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    @Override
    public String toString() {
        return this.sqlFilter;
    }
}
