package nc.uap.lfw.build.pub.util.pdm;

import java.io.File;

import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.pub.util.pdm.itf.IDB2;
import nc.uap.lfw.build.pub.util.pdm.itf.IDBScriptService;
import nc.uap.lfw.build.pub.util.pdm.itf.IOracle;
import nc.uap.lfw.build.pub.util.pdm.itf.ISQLServer;

/**
 * 建库脚本服务类
 * 
 * 工作方式: 多实例, 线程安全
 * 可访问方法: validatePDM
 * 
 * @author fanp
 */
public class DBScriptServiceImpl implements IDBScriptService, ISQLServer, IOracle, IDB2
{
//	private String taskId;
	private File pdmFile;

	/**
	 * 构造函数(完成初始PDM合法性校验)
	 * 
	 * @param pdm_path
	 * @return
	 */
	public DBScriptServiceImpl(String task_id, String pdm_path) throws SDPBuildException
	{
//		this.taskId = task_id;
		this.pdmFile = new File(pdm_path);
		try
		{
			/* PDM合法性校验 */
		    validatePDM(this.pdmFile);
		}
		catch(SDPBuildException sdp_build_exception)
		{
			throw sdp_build_exception;
		}
	}

	/**
	 * 该方法实现对指定PDM文件的校验
	 * 校验规则:
	 * A. PDM文件格式是否为"XML"格式
	 * B. 产生PDM文件所使用的Sybase PowerDesigner的版本是否为12.0.0.1700
	 * C. PDM文件是否以SQL Server 2005数据库版本生成
	 * 
	 * @param pdm_file
	 * @return 如校验失败则抛出SDPBuildException异常; 如校验成功则正常返回
	 */
	public void validatePDM(File pdm_file) throws SDPBuildException
	{
		try
		{
			/* PDM合法性校验 */
			PDMUtil.validate(pdm_file);
		}
		catch(SDPBuildException sdp_build_exception)
		{
			/* 检验失败 */
			throw sdp_build_exception;
		}
		return;
	}
	
	/**
	 * 该方法为指定PDM文件导出指定数据库类型的建库脚本
	 * 目前支持的数据库脚本类型:
	 * A. SQL Server 2005
	 * B. Oracle 10g
	 * C. DB2 8.x
	 * 
	 * @param db_type
	 * @return
	 */
    public void genDBCreateScript(int db_type)
    {
    	
    }
    
//    private String getTaskId()
//    {
//    	return this.taskId;
//    }
//    
//    private File getPdmFile()
//    {
//    	return this.pdmFile;
//    }
}
