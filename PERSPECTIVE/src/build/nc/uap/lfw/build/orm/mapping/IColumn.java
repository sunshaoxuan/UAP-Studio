package nc.uap.lfw.build.orm.mapping;

/**
 * 数据库列信息接口。
 * 
 * @author PH
 */
public interface IColumn{
	
	/**
	 * 获取列名。
	 * 
	 * @return 列名
	 */
	public String getName();
	
	/**
	 * 获取所属表。
	 * 
	 * @return 所属表
	 */
	public ITableBase getTableBase();
	
	/**
	 * 获取列的数据类型。
	 * @see java.sql.Types
	 * 
	 * @return 数据类型
	 */
	public int getDataType();
	
	/**
	 * 获取列的数据类型名称。
	 * 
	 * @return 数据类型名称
	 */
	public String getTypeName();
	
	/**
	 * 获取字段长度。
	 * 
	 * @return 字段长度
	 */
	public int getLength();
	
	/**
	 * 获取字段精度。
	 * 
	 * @return 字段精度
	 */
	public int getPrecise();
	
	/**
	 * 字段是否可为null。
	 * 
	 * @return true if null enabled.
	 */
	public boolean isNullable();
	
	/**
	 * 获取字段的默认值。
	 * 
	 * @return 字段的默认值
	 */
	public String getDefaultValue(); 
	
	/**
	 * 获取表的默认字符串值。
	 * 
	 * @return 默认字符串值
	 */
//	public String getDefaultStringValue();
	
	/**
	 * 获取字段的描述信息。
	 * 
	 * @return 字段的描述信息
	 */
	public String getDesc();
	
	/**
	 * 获取字段的版型信息（表示多语字段）
	 * @return
	 */
	public String getStereotype();

}
