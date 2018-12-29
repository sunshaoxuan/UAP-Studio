package nc.uap.lfw.ref.model;

import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.datamodel.MdClassVO;

public class MdFieldInfo {
	private String tableName;
	private String pkField;
	private Field field;
	private MdClassVO parentClassVO;
	
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
	public MdClassVO getParentClassVO() {
		return parentClassVO;
	}
	public void setParentClassVO(MdClassVO parentClassVO) {
		this.parentClassVO = parentClassVO;
	}
	
	
	
	
}
