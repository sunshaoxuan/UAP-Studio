package nc.uap.lfw.build.orm.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Table implements ITable {
	
	/** 表名 */
	private String name;
	/** 表列集 */
	private List<IColumn> allColumns;
	/** 外键约束 */
	private List<IFkConstraint> fkConstraints;
	/** 主键约束 */
	private IPkConstraint pkConstraint;
	/** 描述 */
	private String desc;
	
	public Table() {
		allColumns = new ArrayList<IColumn>();
		fkConstraints = new ArrayList<IFkConstraint>();
	}
	
	public List<IColumn> getAllColumns() {
		return allColumns;
	}

	public List<IFkConstraint> getFkConstraints() {
		return fkConstraints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IPkConstraint getPkConstraint() {
		return pkConstraint;
	}

	public void setPkConstraint(IPkConstraint pkConstraint) {
		this.pkConstraint = pkConstraint;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public IColumn getColumnByName(String colName) {
		for(IColumn col : allColumns){
			if(col.getName().equalsIgnoreCase(colName)){
				return col;
			}
		}
		return null;
	}
	
	public IFkConstraint getFkConstraintByRefTableName(String refTableName) {
		for(IFkConstraint fkConstraint : fkConstraints){
			if(fkConstraint.getRefTable().getName().equalsIgnoreCase(refTableName)){
				return fkConstraint;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("(");
		for(IColumn col : allColumns){
			sb.append(col.getName()).append(" ").append(col.getTypeName()).append(", ");
		}
		sb.append(IOUtils.LINE_SEPARATOR).append("primary key: (");
		for(IColumn pkCol : pkConstraint.getColumns()){
			sb.append(pkCol.getName()).append(",");
		}
		sb.deleteCharAt(sb.length()-1).append(")");
		if(fkConstraints != null && !fkConstraints.isEmpty()){
			for(IFkConstraint fkCol : fkConstraints){
				sb.append(IOUtils.LINE_SEPARATOR).append("foreign key: (");
				for(IColumn col : fkCol.getColumns()){
					sb.append(col.getName()).append(",");
				}
				sb.deleteCharAt(sb.length() - 1).append(")");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
}
