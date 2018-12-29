package nc.uap.lfw.build.dbrecord.itf;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class MainTableRecordItem {
	
	private String itemKey;
	
	@XStreamAlias("itemRule")
	private String tableName;
	
	@XStreamAlias("itemName")
	private String tableDesc;
	
	private String sysField;
	
	private String corpField;
	
	private String grpField;
	
	@XStreamAlias("fixedWhere")
	private String whereCondition;

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public String getSysField() {
		return sysField;
	}

	public void setSysField(String sysField) {
		this.sysField = sysField;
	}

	public String getCorpField() {
		return corpField;
	}

	public void setCorpField(String corpField) {
		this.corpField = corpField;
	}

	public String getGrpField() {
		return grpField;
	}

	public void setGrpField(String grpField) {
		this.grpField = grpField;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}
}
