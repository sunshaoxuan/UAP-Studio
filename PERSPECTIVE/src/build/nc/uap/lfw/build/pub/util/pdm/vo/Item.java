package nc.uap.lfw.build.pub.util.pdm.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 预置脚本类型的VO转换子类
 * 
 * 工作方式: 由DBRecordConfigUtil对items.xml转换得到
 * 
 * @author fanp
 */
@XStreamAlias("item")
public class Item {
	/* 脚本类别ID(基础数据要以'BD'开头) */
	public String itemKey;
	/* 脚本类别名称 */
	public String itemName;
	/* 脚本导出规则(主表名) */
	public String itemRule;
	/* 已停用 */
	public String sysField;
	/* 已停用 */
	public String corpField;
	/* 导出编组字段 */
	public String grpField;
	/* 主表导出条件(默认全部导出) */
	public String fixedWhere = " 1=1 ";

	/**
	 * 构造函数
	 * 
	 * @return
	 */
	public Item() {
	}

	public String getCorpField() {
		return corpField;
	}

	public String getFixedWhere() {
		return fixedWhere;
	}

	public String getGrpField() {
		return grpField;
	}

	public String getItemKey() {
		return itemKey;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemRule() {
		return itemRule;
	}

	public String getSysField() {
		return sysField;
	}

	public void setCorpField(String newCorpField) {
		corpField = newCorpField;
	}

	public void setFixedWhere(String newFixedWhere) {
		fixedWhere = newFixedWhere;
	}

	public void setGrpField(String newGrpField) {
		grpField = newGrpField;
	}

	public void setItemKey(String newItemKey) {
		itemKey = newItemKey;
	}

	public void setItemName(String newItemName) {
		itemName = newItemName;
	}

	public void setItemRule(String newItemRule) {
		itemRule = newItemRule;
	}

	public void setSysField(String newSysField) {
		sysField = newSysField;
	}

	public String toString() {
		return getItemName();
	}
}
