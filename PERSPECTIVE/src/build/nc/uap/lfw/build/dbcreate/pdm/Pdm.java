package nc.uap.lfw.build.dbcreate.pdm;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.build.orm.mapping.IFkConstraint;
import nc.uap.lfw.build.orm.mapping.IIndex;
import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * 表(列、主键、外键)、索引、视图信息。
 * 
 * @author PH
 */
public class Pdm {
	
	private String pdmName;
	
	private String pdmDesc;
	
	private String version;
	
	private List<ITable> tables;
	
	private List<IFkConstraint> fkConstraints;
	
	private List<IIndex> indexs;
	
	private List<ViewInfo> views;
	

	public Pdm() {
		tables = new ArrayList<ITable>();
		fkConstraints = new ArrayList<IFkConstraint>();
		indexs = new ArrayList<IIndex>();
		views = new ArrayList<ViewInfo>();
	}

	public List<ITable> getTables() {
		return tables;
	}

	public List<ViewInfo> getViews() {
		return views;
	}

	public List<IFkConstraint> getFkConstraints() {
		return fkConstraints;
	}

	public List<IIndex> getIndexs() {
		return indexs;
	}

	public String getPdmName() {
		return pdmName;
	}

	public void setPdmName(String pdmName) {
		this.pdmName = pdmName;
	}

	public String getPdmDesc() {
		return pdmDesc;
	}

	public void setPdmDesc(String pdmDesc) {
		this.pdmDesc = pdmDesc;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 视图创建所需信息。
	 * 
	 * @author PH
	 */
	public static class ViewInfo {
		
		private String name;
		
		private String desc;
		
		private String sql;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}
	}
}
