package io.laokou.generator.utils;

/**
 * 数据库类型
 *
 */
public enum DbType {
	MySQL("com.mysql.cj.jdbc.Driver"),
	Oracle("oracle.jdbc.driver.OracleDriver"),
	PostgreSQL("org.postgresql.Driver");

	private final String driverClass;

	DbType(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getDriverClass() {
		return driverClass;
	}
}