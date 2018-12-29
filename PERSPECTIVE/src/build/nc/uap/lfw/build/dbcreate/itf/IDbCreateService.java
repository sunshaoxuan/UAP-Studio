package nc.uap.lfw.build.dbcreate.itf;

import java.io.File;

import nc.uap.lfw.build.exception.SdpBuildRuntimeException;

/**
 * 建库脚本业务接口。
 * 
 * @author PH
 */
public interface IDbCreateService {
	
	/**
	 * 校验PDM文件是否合法。
	 * 
	 * @param pdmFile 待校验PDM文件
	 * @param parseReference 是否解析外键
	 * @throws SdpBuildRuntimeException 当校验失败时抛出。
	 */
	public void validatePdm(File pdmFile, boolean parseReference) throws SdpBuildRuntimeException;
	
	/**
	 * 据PDM生成(oracle、sqlserver、db2)的sql脚本文件。
	 * 
	 * @param pdmFile PDM文件
	 * @param geneReference 是否生成外键
	 * @param sqlRoot 生成Sql文件目录
	 * @throws SdpBuildRuntimeException 当PDM文件校验失败或生成文件失败时抛出。
	 */
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot) throws SdpBuildRuntimeException;
	
	/**
	 * 据PDM生成指定数据库的sql脚本文件。
	 * 
	 * @param pdmFile PDM文件
	 * @param geneReference 是否生成外键
	 * @param sqlRoot 生成Sql文件目录
	 * @param dbType 需生成数据库的类型。
	 * @throws SdpBuildRuntimeException 当PDM文件校验失败或生成文件失败时抛出。
	 */
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot, DatabaseType dbType) throws SdpBuildRuntimeException;
	
	/**
	 * 据PDM生成数据字典。
	 * 
	 * @param pdmFile PDM文件
	 * @param geneReference 是否生成外键
	 * @param ddRoot 字典文件根目录
	 * @throws SdpBuildRuntimeException 当PDM文件校验失败或生成文件失败时抛出。
	 */
	public void geneDataDictionary(File pdmFile, boolean geneReference, File ddRoot) throws SdpBuildRuntimeException;
	
	
	/**
	 * 数据库类型。
	 * 
	 * @author PH
	 */
	public enum DatabaseType{
		ORACLE, SQLSERVER, DB2
	}
}
