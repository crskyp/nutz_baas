package com.justep.baas.data;

public class ColumnValue {
	private Object value = null;
	private Object oldValue = null;
	private boolean changed = false;
	
	public ColumnValue(Object value) {
		this.value = value;
	}
	
	public ColumnValue(Object value, Object oldValue, boolean changed) {
		this.value = value;
		this.oldValue = oldValue;
		this.changed = changed;
	}
	
	public Object getValue() {
		return value;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public boolean isChanged() {
		return changed;
	}

}
