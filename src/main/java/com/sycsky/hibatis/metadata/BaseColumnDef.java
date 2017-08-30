package com.sycsky.hibatis.metadata;

public class BaseColumnDef {
	private String propertyName;
	private Class<?> propertyType;
	private String columnName;

	public BaseColumnDef(String propertyName, Class<?> propertyType, String columnName) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.columnName = columnName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
