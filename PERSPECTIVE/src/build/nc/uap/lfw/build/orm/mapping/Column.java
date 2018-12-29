package nc.uap.lfw.build.orm.mapping;

public class Column implements IColumn {
	
	private String name;
	
	private ITableBase table;
	
	private int dataType;
	
	private String typeName;
	
	private int length;
	
	private int precise;
	
	private boolean nullable = true;
	
	private String defaultValue;
	
	private String desc;
	
	private String stereoType;
	
	public int getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public ITableBase getTableBase() {
		return table;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDesc() {
		return desc;
	}

	public int getLength() {
		return length;
	}

	public int getPrecise() {
		return precise;
	}

	public boolean isNullable() {
		return nullable;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	public void setTable(ITable table) {
		this.table = table;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPrecise(int precise) {
		this.precise = precise;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setStereotype(String stereoType){
		this.stereoType = stereoType;
	}
	
	@Override
	public String toString() {
		return name + ":" + typeName;
	}

	public String getStereotype() {
		return stereoType;
	}
	
}
