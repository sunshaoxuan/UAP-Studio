package nc.uap.lfw.build.pub.util.pdm.vo;

import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.itf.ITableHierarchy;

/**
 * 表拓补结构体系中的主表VO类
 * 
 * @author fanp
 */
public class MainTable implements ITableHierarchy {
	/* 主表名称 */
	private String tableName = null;

	/* 主键列 */
	// private String primaryKeyColumn = null;
	/* 主表导出条件 */
	private String whereCondition = null;

	/* 子表容器 */
	private List<SubTable> subTableSet = null;

	/* 脚本序号 */
	private String sqlNo = null;

	/* 是否关联表 */
	private boolean isAssociatedTables = false;

	/* 元数据 */
	private Table metaData = null;

	/**
	 * 构造函数,单表结构由此构造函数进入
	 * 
	 * @param itemVO
	 */
	public MainTable(Item itemVO) {
		setTableName(itemVO.getItemRule());
		setWhereCondition(itemVO.getFixedWhere());
	}

	/**
	 * 该方法搜索MainTable以下各级子表(含自己)并返回指定表的元数据
	 * 
	 * @param table_name
	 * @return (1)tableVO(找到对应的元数据); (2)null(未找到对应的元数据)
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
