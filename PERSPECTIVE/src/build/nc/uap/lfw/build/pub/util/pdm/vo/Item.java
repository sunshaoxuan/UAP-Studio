package nc.uap.lfw.build.pub.util.pdm.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Ԥ�ýű����͵�VOת������
 * 
 * ������ʽ: ��DBRecordConfigUtil��items.xmlת���õ�
 * 
 * @author fanp
 */
@XStreamAlias("item")
public class Item {
	/* �ű����ID(��������Ҫ��'BD'��ͷ) */
	public String itemKey;
	/* �ű�������� */
	public String itemName;
	/* �ű���������(������) */
	public String itemRule;
	/* ��ͣ�� */
	public String sysField;
	/* ��ͣ�� */
	public String corpField;
	/* ���������ֶ� */
	public String grpField;
	/* ����������(Ĭ��ȫ������) */
	public String fixedWhere = " 1=1 ";

	/**
	 * ���캯��
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
