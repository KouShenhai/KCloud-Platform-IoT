package io.laokou.generator.config;

import io.laokou.generator.entity.DataSourceEntity;
import io.laokou.generator.qo.AbstractQO;
import io.laokou.generator.qo.MySqlQO;
import io.laokou.generator.qo.OracleQO;
import io.laokou.generator.qo.PostgreSqlQO;
import io.laokou.generator.utils.DbType;
import io.laokou.generator.utils.DbUtil;
import lombok.Data;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源信息
 *
 */
@Data
public class DataSourceInfo {
    /**
     * 数据源ID
     */
    private Long id;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private AbstractQO dbQO;

    private Connection connection;

    public DataSourceInfo(DataSourceEntity entity) {
        this.id = entity.getId();
        this.dbType = DbType.valueOf(entity.getDbType());
        this.connUrl = entity.getConnUrl();
        this.username = entity.getUsername();
        this.password = entity.getPassword();

        if(dbType == DbType.MySQL) {
            this.dbQO = new MySqlQO();
        }else if(dbType == DbType.Oracle) {
            this.dbQO = new OracleQO();
        }else if(dbType == DbType.PostgreSQL) {
            this.dbQO = new PostgreSqlQO();
        }

        try {
            this.connection = DbUtil.getConnection(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DataSourceInfo(Connection connection) throws SQLException {
        this.id = 0L;
        this.dbType = DbType.valueOf(connection.getMetaData().getDatabaseProductName());

        if(dbType == DbType.MySQL) {
            this.dbQO = new MySqlQO();
        }else if(dbType == DbType.Oracle) {
            this.dbQO = new OracleQO();
        }else if(dbType == DbType.PostgreSQL) {
            this.dbQO = new PostgreSqlQO();
        }

        this.connection = connection;
    }
}
