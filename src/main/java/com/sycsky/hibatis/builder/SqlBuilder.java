package com.sycsky.hibatis.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GenerationType;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import com.sycsky.hibatis.metadata.ColumnDef;
import com.sycsky.hibatis.metadata.EntityMetadata;
import com.sycsky.hibatis.metadata.IdentityDef;

public class SqlBuilder {
	private EntityMetadata metadata;

	public SqlBuilder(Class<?> entityClass) {
		this.metadata = new EntityMetadata(entityClass);
	}

	public ParameterMap buildParameterMap(Configuration configuration, String mapId) {
		List<IdentityDef> identityDefs = metadata.getIdentityDefs();
		List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>(identityDefs.size());
		for (IdentityDef def : identityDefs) {
			ParameterMapping.Builder pmmb = new ParameterMapping.Builder(configuration, def.getPropertyName(),
					def.getPropertyType());
			parameterMappings.add(pmmb.build());
		}
		ParameterMap.Builder pmb = new ParameterMap.Builder(configuration, mapId, Long.class, parameterMappings);
		return pmb.build();
	}

	public String deleteString() {
		SQL sql = new SQL().DELETE_FROM(metadata.getTableName());
		StringBuilder conditions = new StringBuilder();
		List<IdentityDef> identityDefs = metadata.getIdentityDefs();
		for (IdentityDef def : identityDefs) {
			if (conditions.length() > 0) {
				conditions.append(" and ");
			}
			conditions.append(def.getColumnName()).append("=#{").append(def.getPropertyName()).append('}');
		}
		return sql.WHERE(getWhereClause()).toString();
	}

	private String getWhereClause() {
		StringBuilder conditions = new StringBuilder();
		List<IdentityDef> identityDefs = metadata.getIdentityDefs();
		for (IdentityDef def : identityDefs) {
			if (conditions.length() > 0) {
				conditions.append(" and ");
			}
			conditions.append(def.getColumnName()).append("=#{").append(def.getPropertyName()).append('}');
		}
		return conditions.toString();
	}

	public String insertString() {
		SQL sql = new SQL().INSERT_INTO(metadata.getTableName());

		List<IdentityDef> identityDefs = metadata.getIdentityDefs();
		for (IdentityDef def : identityDefs) {
			if (def.getStrategy() != GenerationType.IDENTITY) {
				final String propertyName = def.getPropertyName();
				StringBuilder sb = new StringBuilder(propertyName.length() + 3);
				sql.VALUES(def.getColumnName(), sb.append("#{").append(propertyName).append('}').toString());
			}
		}
		List<ColumnDef> columnDefs = metadata.getColumnDefs();
		for (ColumnDef def : columnDefs) {
			if (def.isInsertable()) {
				final String propertyName = def.getPropertyName();
				StringBuilder sb = new StringBuilder(propertyName.length() + 3);
				sql.VALUES(def.getColumnName(), sb.append("#{").append(propertyName).append('}').toString());
			}
		}
		return sql.toString();
	}

	public String selectString() {
		SQL sql = new SQL().SELECT("*").FROM(metadata.getTableName()).WHERE(getWhereClause());
		return sql.toString();
	}

	public String selectString(String[] fields) {
		StringBuilder sb = new StringBuilder();
		for (String field : fields) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append(field);
		}
		SQL sql = new SQL().SELECT(sb.toString()).FROM(metadata.getTableName()).WHERE(getWhereClause());
		return sql.toString();
	}

	public String updateString() {
		SQL sql = new SQL().UPDATE(metadata.getTableName());
		List<ColumnDef> columnDefs = metadata.getColumnDefs();
		for (ColumnDef def : columnDefs) {
			if (def.isUpdatable()) {
				final String columnName = def.getColumnName();
				final String propertyName = def.getPropertyName();
				StringBuilder sb = new StringBuilder(columnName.length() + propertyName.length() + 4);
				sql.SET(sb.append(columnName).append("=#{").append(propertyName).append('}').toString());
			}
		}
		return sql.WHERE(getWhereClause()).toString();
	}
}
