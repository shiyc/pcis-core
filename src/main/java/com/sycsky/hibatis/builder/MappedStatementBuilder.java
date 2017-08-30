package com.sycsky.hibatis.builder;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

public class MappedStatementBuilder {
	private Configuration configuration;

	public MappedStatementBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	private String buildStatementId(Class<?> entityClass, String name) {
		final String className = entityClass.getName();
		StringBuilder sb = new StringBuilder(className.length() + name.length() + 1);
		return sb.append(className).append('.').append(name).toString();
	}

	public String delete(Class<?> entityClass) {
		String statementId = buildStatementId(entityClass, "delete-Inline");
		if (configuration.hasStatement(statementId)) {
			return statementId;
		}
		SqlBuilder sb = new SqlBuilder(entityClass);
		return sb.deleteString();
	}

	public String insert(Class<?> entityClass) {
		String statementId = buildStatementId(entityClass, "insert-Inline");
		if (configuration.hasStatement(statementId)) {
			return statementId;
		}
		SqlBuilder sb = new SqlBuilder(entityClass);
		return sb.insertString();
	}

	public String select(Class<?> entityClass) {
		String statementId = buildStatementId(entityClass, "select-Inline");
		if (configuration.hasStatement(statementId)) {
			return statementId;
		}
		SqlBuilder sb = new SqlBuilder(entityClass);
		return sb.selectString();
	}

	public String update(Class<?> entityClass) {
		String statementId = buildStatementId(entityClass, "update-Inline");
		if (configuration.hasStatement(statementId)) {
			return statementId;
		}
		SqlBuilder sqlBuilder = new SqlBuilder(entityClass);
		String sql = sqlBuilder.updateString();
		String mapId = statementId + "-Inline";

		SqlSource sqlSource = new StaticSqlSource(configuration, sql);

		MappedStatement.Builder builder = new MappedStatement.Builder(configuration, statementId, sqlSource,
				SqlCommandType.UPDATE);
		MappedStatement ms = builder.parameterMap(sqlBuilder.buildParameterMap(configuration, mapId)).build();
		if (!configuration.hasStatement(statementId)) {
			configuration.addMappedStatement(ms);
		}
		return statementId;
	}
}
