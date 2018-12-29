package nc.uap.lfw.editor.extNode;

import java.util.Map;

import nc.uap.studio.pub.db.model.TableStructure;

/**
 * 构造安装盘的预置数据描述。
 * 
 * @author huangbind
 * 
 */
public class InitDataInfo {

	private String bc;
	
	/**
	 * 是否业务脚本。
	 */
	private boolean isBusiness = true;

	/**
	 * 表名
	 */
	private String table;

	/**
	 * 条件
	 */
	private String where;

	/**
	 * 主子表结构。
	 */
	private TableStructure struct;

	/**
	 * 序号映射。
	 */
	private Map<String, String> tableNoMap;

	/**
	 * 映射名。
	 */
	private String mapName;

	public boolean isBusiness() {
		return isBusiness;
	}

	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	public TableStructure getStruct() {
		return struct;
	}

	public void setStruct(TableStructure struct) {
		this.struct = struct;
	}

	public Map<String, String> getTableNoMap() {
		return tableNoMap;
	}

	public void setTableNoMap(Map<String, String> tableNoMap) {
		this.tableNoMap = tableNoMap;
	}

	public String getTable() {
		return table==null?null:table.toLowerCase();
	}

	public void setTable(String table) {
			this.table = table;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

}
