package com.sycsky.hibatis.metadata;

public class ColumnDef extends BaseColumnDef {
	private boolean insertable;
	private boolean updatable;

	public ColumnDef(String propertyName, Class<?> propertyType, String columnName) {
		super(propertyName, propertyType, columnName);
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
}
