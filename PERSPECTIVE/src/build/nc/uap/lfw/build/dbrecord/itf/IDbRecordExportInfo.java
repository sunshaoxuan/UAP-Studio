package nc.uap.lfw.build.dbrecord.itf;

/**
 * 预置脚本导出信息。
 * 
 * @author PH
 */
public interface IDbRecordExportInfo {
	
	/**
	 * 获取生成Sql文件的序列号。
	 * 
	 * @return
	 */
	public String getSqlNo();
	
	/**
	 * 获取分组字段，只支持单个字段。
	 * 
	 * @return 分组字段
	 */
	public String getGrpField();
	
	/**
	 * 表名。
	 * 
	 * @return
	 */
	String getTableName();
	
	/**
	 * 表描述。
	 * 
	 * @return
	 */
	public String getTableDesc();

}
