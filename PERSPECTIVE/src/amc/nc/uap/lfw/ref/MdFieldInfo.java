package nc.uap.lfw.ref;

import nc.uap.lfw.core.data.Field;

public class MdFieldInfo {
	private String tableName;
	private String pkField;
	private Field field;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	
	
}
