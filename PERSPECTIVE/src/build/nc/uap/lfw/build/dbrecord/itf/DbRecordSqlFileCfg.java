package nc.uap.lfw.build.dbrecord.itf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 预置脚本生成sql文件的相关配置。
 * 
 * @author PH
 */
public class DbRecordSqlFileCfg {
	
	public static final String MODULE_QUERYENGINE_FILE_NAME = "QE.properties";
	//自由报表配置文件名称
	public static final String MODULE_FREEREPORT_FILE_NAME = "FR.properties";
	
	public static final String CUSTOM_QUERY = "自定义查询";
	
	public static final String FREE_REPORT = "自由报表"; 
	
	private static final String BUSINESS_DIR_NAME = "business";
	
	private static final String FORMAT_MD_ENABLE_ATTR = "bFmd";
	
	private static final String FORMAT_MD_ID_ATTR = "fid";
	
	private static final String QUERY_MD_ENABLE_ATTR = "bQmd";
	
	private static final String QUERY_MD_ID_ATTR = "qid";
	
	private static final String ENABLE = "Y";
	
	//private static final String DISABLE = "N";
	/** sql生成文件根路径 */
	private String rootDir;
	/** 公共映射文件 */
	private String commonMapPath;
	/** 模块映射文件 */
	private String moduleMapPath;
	/** 模块查询引擎文件路径 */
	private String moduleQueryEngineFilePath;
	/** 自由报表配置文件路径 */
	private String moduleFreeReportFilePath;
	/** oidMark规则文件路径 */
	private String oidMarkRuleFilePath;
	/** 部门 */
	private String department;
	
	private Properties commonProp;
	
	private Properties moduleProp;
	
	private Properties qeProp;
	
	private Properties frProp;
	
	private Object obj = new Object();
	
	public DbRecordSqlFileCfg(String rootDir, String commonMapPath, String moduleMapPath) {
		this.rootDir = rootDir;
		this.commonMapPath = commonMapPath;
		this.moduleMapPath = moduleMapPath;
	}

	public String getAbsolutePath(String tableName){
		if(commonProp == null){
			synchronized (obj) {
				if(commonProp == null){
					commonProp = getProperties(commonMapPath);
					moduleProp = getProperties(moduleMapPath); 
					//map = new HashMap<String, String>();
				}
			}
		}
		String commonMapValue = commonProp.getProperty(tableName);
		if(StringUtils.isNotBlank(commonMapValue)){
			return new StringBuilder(rootDir).append(File.separator)
				.append(commonMapValue.trim()).append(File.separator).append(tableName).toString();
		}else{
			String moduleMapValue = moduleProp.getProperty(tableName);
			if(StringUtils.isNotBlank(moduleMapValue)){
				return new StringBuilder(rootDir).append(File.separator).append(BUSINESS_DIR_NAME)
					.append(File.separator).append(moduleMapValue.trim()).append(File.separator).append(tableName).toString();
			}
		}
		return null;
	}
	
	public boolean isFmdEnabledOfFR(){
		return ENABLE.equalsIgnoreCase(getFRProperty(FORMAT_MD_ENABLE_ATTR));
	}
	
	public boolean isQmdEnabledOfFR(){
		return ENABLE.equalsIgnoreCase(getFRProperty(QUERY_MD_ENABLE_ATTR));
	}
	
	public String getFidOfFR(){
		return getFRProperty(FORMAT_MD_ID_ATTR);
	}
	
	public String getQidOfFR(){
		return getFRProperty(QUERY_MD_ID_ATTR);
	}

	public boolean isFmdEnabledOfQE(){
		return ENABLE.equalsIgnoreCase(getQEProperty(FORMAT_MD_ENABLE_ATTR));
	}
	
	public boolean isQmdEnabledOfQE(){
		return ENABLE.equalsIgnoreCase(getQEProperty(QUERY_MD_ENABLE_ATTR));
	}
	
	public String getFidOfQE(){
		return getQEProperty(FORMAT_MD_ID_ATTR);
	}
	
	public String getQidOfQE(){
		return getQEProperty(QUERY_MD_ID_ATTR);
	}

	private String getQEProperty(String key){
		if(moduleQueryEngineFilePath == null){
			return null;
		}
		if(qeProp == null){
			synchronized (obj) {
				if(qeProp == null){
					qeProp = getProperties(moduleQueryEngineFilePath);
				}
			}
		}
		return qeProp.getProperty(key);
	}
	
	private String getFRProperty(String key){
		if(moduleFreeReportFilePath == null){
			return null;
		}
		if(frProp == null){
			synchronized (obj) {
				if(frProp == null){
					frProp = getProperties(moduleFreeReportFilePath);
				}
				
			}
		}
		return frProp.getProperty(key);
	}
	
	private Properties getProperties(String fileName){
		Properties prop = null;
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(is);
		} catch (FileNotFoundException e) {
			MainPlugin.getDefault().logError("文件" + fileName + "不存在。", e);
			throw new SdpBuildRuntimeException("文件" + fileName + "不存在。");
		} catch (IOException e) {
			MainPlugin.getDefault().logError("读取文件" + fileName + "失败。", e);
			throw new SdpBuildRuntimeException("读取文件" + fileName + "失败。");
		} finally{
			IOUtils.closeQuietly(is);
		}
		return prop;
	}

	public String getModuleQueryEngineFilePath() {
		return moduleQueryEngineFilePath;
	}

	public void setModuleQueryEngineFilePath(String moduleQueryEngineFilePath) {
		this.moduleQueryEngineFilePath = moduleQueryEngineFilePath;
	}

	public String getOidMarkRuleFilePath() {
		return oidMarkRuleFilePath;
	}

	public void setOidMarkRuleFilePath(String oidMarkRuleFilePath) {
		this.oidMarkRuleFilePath = oidMarkRuleFilePath;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getModuleFreeReportFilePath() {
		return moduleFreeReportFilePath;
	}

	public void setModuleFreeReportFilePath(String moduleFreeReportFilePath) {
		this.moduleFreeReportFilePath = moduleFreeReportFilePath;
	}


}
