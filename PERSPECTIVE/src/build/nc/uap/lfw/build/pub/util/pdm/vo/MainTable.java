package nc.uap.lfw.build.pub.util.pdm.vo;

import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.itf.ITableHierarchy;

/**
 * ���ز��ṹ��ϵ�е�����VO��
 * 
 * @author fanp
 */
public class MainTable implements ITableHierarchy {
	/* �������� */
	private String tableName = null;

	/* ������ */
	// private String primaryKeyColumn = null;
	/* ���������� */
	private String whereCondition = null;

	/* �ӱ����� */
	private List<SubTable> subTableSet = null;

	/* �ű���� */
	private String sqlNo = null;

	/* �Ƿ������ */
	private boolean isAssociatedTables = false;

	/* Ԫ���� */
	private Table metaData = null;

	/**
	 * ���캯��,����ṹ�ɴ˹��캯������
	 * 
	 * @param itemVO
	 */
	public MainTable(Item itemVO) {
		setTableName(itemVO.getItemRule());
		setWhereCondition(itemVO.getFixedWhere());
	}

	/**
	 * �÷�������MainTable���¸����ӱ�(���Լ�)������ָ�����Ԫ����
	 * 
	 * @param table_name
	 * @return (1)tableVO(�ҵ���Ӧ��Ԫ����); (2)null(δ�ҵ���Ӧ��Ԫ����)
	 */
	public Table lookup(String table_name) {
		if (getTableName().equalsIgnoreCase(table_name))
			return getTableMetaData();

		if (getSubTableSet() == null || getSubTableSet().size() == 0)
			return null;

		for (int i = 0; i < getSubTableSet().size(); i++) {
			SubTable subTable = (SubTable) getSubTableSet().get(i);
			Table tableVO = subTable.lookup(table_name);
			if (tableVO != null)
				return tableVO;
		}// for
		return null;
	}

	public String getSqlNo() {
		return sqlNo;
	}

	public void setSqlNo(String newSqlNo) {
		sqlNo = newSqlNo;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String newTableName) {
		tableName = newTableName;
	}

	/*
	 * public String getPrimaryKeyColumn() { return primaryKeyColumn; }
	 * 
	 * public void setPrimaryKeyColumn(String newPrimaryKeyColumn) {
	 * primaryKeyColumn = newPrimaryKeyColumn; }
	 */

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String newWhereCondition) {
		whereCondition = newWhereCondition;
	}

	public List<SubTable> getSubTableSet() {
		return subTableSet;
	}

	public void setSubTableSet(List<SubTable> vec) {
		subTableSet = vec;
	}

	public boolean isAssociatedTables() {
		return isAssociatedTables;
	}

	public void setAssociated(boolean is_associated_tables) {
		this.isAssociatedTables = is_associated_tables;
	}

	public Table getTableMetaData() {
		return metaData;
	}

	public void setTableMetaData(Table table_vo) {
		this.metaData = table_vo;
	}
}
