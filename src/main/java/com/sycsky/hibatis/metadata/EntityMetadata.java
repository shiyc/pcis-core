package com.sycsky.hibatis.metadata;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.javassist.Modifier;

public class EntityMetadata {
	private String tableName;
	private Class<?> entityClass;
	private List<ColumnDef> columnDefs = new ArrayList<>();
	private List<IdentityDef> identityDefs = new ArrayList<>();

	public EntityMetadata(Class<?> clazz) {
		this.entityClass = clazz;
		Table table = entityClass.getAnnotation(Table.class);
		tableName = table != null ? table.name() : entityClass.getSimpleName();
		resolveFields();
	}

	public List<ColumnDef> getColumnDefs() {
		return columnDefs;
	}

	public List<IdentityDef> getIdentityDefs() {
		return identityDefs;
	}

	public String getTableName() {
		return tableName;
	}

	private void resolveFields() {
		Class<?> clazz = entityClass;
		while (clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
					continue;
				}
				if (field.getAnnotation(Transient.class) != null) {
					continue;
				}
				Column column = field.getAnnotation(Column.class);
				String columnName = column != null ? column.name() : field.getName();

				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					IdentityDef identityDef = new IdentityDef(field.getName(), field.getType(), columnName);
					identityDefs.add(identityDef);

					GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
					if (gv != null) {
						identityDef.setStrategy(gv.strategy());
					}
				} else {
					ColumnDef columnDef = new ColumnDef(field.getName(), field.getType(), columnName);
					columnDefs.add(columnDef);

					columnDef.setInsertable(column.insertable());
					columnDef.setUpdatable(column.updatable());
				}
			}
			clazz = clazz.getSuperclass();
		}
		if (identityDefs.isEmpty()) {
			throw new IllegalArgumentException(entityClass.getName() + " has no identity");
		}
	}
}
