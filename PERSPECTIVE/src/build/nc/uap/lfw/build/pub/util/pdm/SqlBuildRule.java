package nc.uap.lfw.build.pub.util.pdm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPException;
import nc.uap.lfw.build.pub.util.pdm.itf.IExport;
import nc.uap.lfw.build.pub.util.pdm.itf.ISql;
import nc.uap.lfw.build.pub.util.pdm.vo.Item;

import org.apache.commons.io.IOUtils;

/**
 * 脚本生成规则类
 * 
 * 工作方式: 单例, 线程安全 可访问方法: getInstance, getBuildOrder, isScriptSerialized,
 * isScriptSensitive, isDebug, getSQLFileByPDM, getSQLFileByItem
 * 
 * @author fanp
 */
public class SqlBuildRule implements ISql {
	/**
	 * commonmap文件上一级路径
	 */
	private String commonMapPath;
	/**
	 * modulemap文件上一级路径
	 */
	private String moduleMapPath;
	/**
	 * 预置脚本表体系结构定义文件存储地址
	 */
	private String multitableRuleMapPath;

	/**
	 * NC公共表脚本映射
	 */
	private Map<String, String> commonMap;
	/**
	 * 模块私有表脚本映射
	 */
	private Map<String, String> moduleMap;
	/**
	 * 模块私有自定义查询属性
	 */
	private Map<String, String> moduleQueryEngineMap;

	/**
	 * 
	 * @param common_script_map_rule_path
	 *            commonmap的路径
	 * @param module_script_map_rule_path
	 *            modulemap的路径
	 * @param hierarchy_rule_path
	 *            主子表配置文件路径
	 * @throws SDPBuildException
	 */
	public SqlBuildRule(String common_script_map_rule_path, String module_script_map_rule_path,
			String hierarchy_rule_path) {
		this.commonMapPath = common_script_map_rule_path;
		this.moduleMapPath = module_script_map_rule_path;
		this.multitableRuleMapPath = hierarchy_rule_path;
	}

	public String getMultitableRuleMapPath() {
		return multitableRuleMapPath;
	}

	private Map<String, String> getCommonMap() {
		if (commonMap == null) {
			commonMap = getMapFromFile(commonMapPath, IExport.SQL_EXPORT_COMMON_MAP);
		}
		return commonMap;
	}

	private Map<String, String> getModuleMap() {
		if (moduleMap == null) {
			moduleMap = getMapFromFile(moduleMapPath, IExport.SQL_EXPORT_MODULE_MAP);
		}
		return moduleMap;
	}

	private Map<String, String> getModuleQueryEngineMap() {
		if (moduleQueryEngineMap == null) {
			moduleQueryEngineMap = getMapFromFile(moduleMapPath, IExport.SQL_EXPORT_MODULE_QUERYENGINE);
		}
		return moduleQueryEngineMap;
	}

	/**
	 * 从属性文件中读取出相应的资源，并组织成map
	 * 
	 * @param rootPath
	 * @param fileName
	 * @return
	 */
	private Map<String, String> getMapFromFile(String rootPath, String fileName) {
		Map<String, String> result = new HashMap<String, String>();
		File file = new File(rootPath + File.separator + fileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			PropertyResourceBundle propResBdl = new PropertyResourceBundle(fis);
			Enumeration<String> keys = propResBdl.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				result.put(key, propResBdl.getString(key));
			}
			// for (String key : propResBdl.keySet()) {
			// result.put(key, propResBdl.getString(key));
			// }
		} catch (FileNotFoundException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		} catch (IOException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(fis);
		}
		return result;
	}

	/**
	 * 该方法根据指定的sql页下标返回相应的脚本目录
	 * 
	 * 对应规则为: '0' - '00001' '1' - '00002' '2' - '00003'
	 */
	private String getSqlPageDir(int sql_page_index) {
		switch (sql_page_index) {
		case 0:
			return "00001";
		case 1:
			return "00002";
		case 2:
			return "00003";
		default:
			return null;
		}
	}

	/**
	 * 该方法根据指定的sql页下标返回相应的脚本文件名称前缀
	 * 
	 * 对应规则为: '0' - 'tb_' '1' - 'fi_' '2' - 'vtp_'
	 */
	private String getSqlFilePrefix(int sql_page_index) {
		switch (sql_page_index) {
		case 0:
			return "tb_";
		case 1:
			return "fi_";
		case 2:
			return "vtp_";
		default:
			return null;
		}
	}

	/**
	 * 该方法根据指定SQL脚本根目录及PDM名称返回建库脚本相对存储路径(如:
	 * script\dbcreate\SQLSERVER\00001\tb_nc_fi_gl.sql)
	 * 
	 * @param sql_root
	 * @param pdm_name
	 * @param db_type
	 * @param sql_page_index
	 */
	public File getSQLFileByPDM(String sql_root, String pdm_name, String db_type, int sql_page_index) {
		File sqlFile = null;
		String dbType = null;
		String sqlFileName = null;

		/* 获得数据库类型目录名称(如: SQLSERVER, ORACLE, DB2) */
		if (db_type.equalsIgnoreCase(ISql._SQLSERVER_))
			dbType = "SQLSERVER";
		else if (db_type.equalsIgnoreCase(ISql._ORACLE_))
			dbType = "ORACLE";
		else if (db_type.equalsIgnoreCase(ISql._DB2_))
			dbType = "DB2";
		else {
			MainPlugin.getDefault().logError("指定的数据库类型目前不支持：" + db_type);
			return null;
		}

		/* 获得sql目录(如: 00001, 00002, 00003) */
		String sqlDir = getSqlPageDir(sql_page_index);

		/* 获得sql脚本文件名称(如: tb_nc_fi_gl.sql, fi_nc_fi_gl.sql, vtp_nc_fi_gl.sql) */
		sqlFileName = getSqlFilePrefix(sql_page_index) + pdm_name.substring(0, pdm_name.lastIndexOf(".")).toLowerCase()
				+ ".sql";

		/* 获得sql脚本文件的相对路径 */
		sqlFile = new File(sql_root + File.separator + "script" + File.separator + "dbcreate" + File.separator + dbType
				+ File.separator + sqlDir + File.separator + sqlFileName);
		return sqlFile;
	}

	/**
	 * 该方法根据SQL脚本根目录及预置脚本类别返回预置脚本的相对存储路径
	 * 如果是公共映射，返回路径为sql_root/script/init/公共映射后的名称/itemVo中的itemRule
	 * 如果是非业务模块映射，返回路径为sql_root/script/init/business/业务映射后的名称/item中的itemRule
	 * 
	 * @param sql_root
	 * @param itemVo
	 * @param db_type
	 *            暂时无用
	 * @return
	 */
	public File getSQLFileByItem(String sql_root, Item itemVo, String db_type) {
		File sqlFile = null;
		/* 获得sql脚本文件的相对路径 */
		String root = sql_root + File.separator + "script" + File.separator + "init";
		if (isCommonScriptType(itemVo)) {
			String mappingDirName;
			try {
				mappingDirName = getMappingDirName(true, itemVo);
				if (mappingDirName != null && !mappingDirName.equalsIgnoreCase("")) {
					sqlFile = new File(root + File.separator + mappingDirName + File.separator + itemVo.getItemRule());
				}
			} catch (SDPException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		} else {
			String mappingDirName;
			try {
				mappingDirName = getMappingDirName(false, itemVo);
				if (mappingDirName != null && !mappingDirName.equalsIgnoreCase("")) {
					sqlFile = new File(root + File.separator + "business" + File.separator + mappingDirName
							+ File.separator + itemVo.getItemRule());
				}
			} catch (SDPException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		}
		return sqlFile;
	}

	/**
	 * 该方法判断指定预置脚本是否为NC公共预置脚本
	 * 
	 * @param item_vo
	 * @return
	 */
	public boolean isCommonScriptType(Item item_vo) {
		return getCommonMap().containsKey(item_vo.getItemRule());
	}

	/**
	 * 该方法返回指定预置脚本的映射目录, 如果为common,从common.map中获取；
	 * 如果是模块自身的，从modulemap中获取
	 * 
	 * @param isCommonScript
	 *            是否为commonmap
	 * @param itemVo
	 * @return
	 * @throws SDPException 如果配置文件中不存在此映射，抛出异常
	 */
	public String getMappingDirName(boolean isCommonScript, Item itemVo) throws SDPException {
		String result = null;
		String itemRule = itemVo.getItemRule();
		if (isCommonScript) {
			result = getCommonMap().get(itemRule);
			if (result == null) {
				throw new SDPException("脚本类型[" + itemVo.getItemName() + "]在配置文件common.map中不存在!");
			}
		} else {
			result = getModuleMap().get(itemRule);
			if (result == null) {
				throw new SDPException("脚本类型[" + itemVo.getItemName() + "]在配置文件module.map中不存在");
			}
		}
		return result;
	}

	/**
	 * 该方法判定是否需要导出格式自定义对象的二进制数据
	 * 
	 * @return
	 */
	public String isFmdNeeded() {
		String bFmd = getModuleQueryEngineMap().get("bFmd");
		if (bFmd != null && bFmd.trim().equalsIgnoreCase("Y"))
			return getModuleQueryEngineMap().get("fid");
		else
			return null;
	}

	/**
	 * 该方法判定是否需要导出查询自定义对象的二进制数据
	 * 
	 * @return
	 */
	public String isQmdNeeded() {
		String bQmd = getModuleQueryEngineMap().get("bQmd");
		if (bQmd != null && bQmd.trim().equalsIgnoreCase("Y"))
			return getModuleQueryEngineMap().get("qid");
		else
			return null;
	}

}
