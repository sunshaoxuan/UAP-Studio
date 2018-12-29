package nc.uap.lfw.build.pub.util.pdm.itf;

import java.io.File;

import nc.uap.lfw.build.exception.SDPBuildException;

/**
 * 数据库脚本服务接口
 * 
 * 需实现方法: validatePDM, genDBCreateScript
 * 
 * @author fanp
 */
public interface IDBScriptService extends ISql {
	/**
	 * 该方法实现对指定PDM文件的校验 校验规则: A. PDM文件格式是否为"XML"格式 B. 产生PDM文件所使用的Sybase
	 * PowerDesigner的版本是否为12.0.0.1700 C. PDM文件是否以SQL Server 2005数据库版本生成
	 * 
	 * @param pdm_path
	 * @return 如校验失败则抛出SDPBuildException异常; 如校验成功则正常返回
	 */
	public void validatePDM(File pdm_path) throws SDPBuildException;

	/**
	 * 该方法为指定PDM文件导出指定数据库类型的建库脚本 目前支持的数据库脚本类型: A. SQL Server 2005 B. Oracle 10g
	 * C. DB2 8.x
	 * 
	 * @param db_type
	 * @return
	 */
	public void genDBCreateScript(int db_type);
}
