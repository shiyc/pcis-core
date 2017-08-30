package com.sycsky.hibatis.metadata;

import javax.persistence.GenerationType;

public class IdentityDef extends BaseColumnDef {
	private GenerationType strategy;

	public IdentityDef(String propertyName, Class<?> propertyType, String columnName) {
		super(propertyName, propertyType, columnName);
	}

	public GenerationType getStrategy() {
		return strategy;
	}

	public void setStrategy(GenerationType strategy) {
		this.strategy = strategy;
	}
}
