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
 * �ű����ɹ�����
 * 
 * ������ʽ: ����, �̰߳�ȫ �ɷ��ʷ���: getInstance, getBuildOrder, isScriptSerialized,
 * isScriptSensitive, isDebug, getSQLFileByPDM, getSQLFileByItem
 * 
 * @author fanp
 */
public class SqlBuildRule implements ISql {
	/**
	 * commonmap�ļ���һ��·��
	 */
	private String commonMapPath;
	/**
	 * modulemap�ļ���һ��·��
	 */
	private String moduleMapPath;
	/**
	 * Ԥ�ýű�����ϵ�ṹ�����ļ��洢��ַ
	 */
	private String multitableRuleMapPath;

	/**
	 * NC������ű�ӳ��
	 */
	private Map<String, String> commonMap;
	/**
	 * ģ��˽�б�ű�ӳ��
	 */
	private Map<String, String> moduleMap;
	/**
	 * ģ��˽���Զ����ѯ����
	 */
	private Map<String, String> moduleQueryEngineMap;

	/**
	 * 
	 * @param common_script_map_rule_path
	 *            commonmap��·��
	 * @param module_script_map_rule_path
	 *            modulemap��·��
	 * @param hierarchy_rule_path
	 *            ���ӱ������ļ�·��
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
	 * �������ļ��ж�ȡ����Ӧ����Դ������֯��map
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
	 * �÷�������ָ����sqlҳ�±귵����Ӧ�Ľű�Ŀ¼
	 * 
	 * ��Ӧ����Ϊ: '0' - '00001' '1' - '00002' '2' - '00003'
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
	 * �÷�������ָ����sqlҳ�±귵����Ӧ�Ľű��ļ�����ǰ׺
	 * 
	 * ��Ӧ����Ϊ: '0' - 'tb_' '1' - 'fi_' '2' - 'vtp_'
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
	 * �÷�������ָ��SQL�ű���Ŀ¼��PDM���Ʒ��ؽ���ű���Դ洢·��(��:
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

		/* ������ݿ�����Ŀ¼����(��: SQLSERVER, ORACLE, DB2) */
		if (db_type.equalsIgnoreCase(ISql._SQLSERVER_))
			dbType = "SQLSERVER";
		else if (db_type.equalsIgnoreCase(ISql._ORACLE_))
			dbType = "ORACLE";
		else if (db_type.equalsIgnoreCase(ISql._DB2_))
			dbType = "DB2";
		else {
			MainPlugin.getDefault().logError("ָ�������ݿ�����Ŀǰ��֧�֣�" + db_type);
			return null;
		}

		/* ���sqlĿ¼(��: 00001, 00002, 00003) */
		String sqlDir = getSqlPageDir(sql_page_index);

		/* ���sql�ű��ļ�����(��: tb_nc_fi_gl.sql, fi_nc_fi_gl.sql, vtp_nc_fi_gl.sql) */
		sqlFileName = getSqlFilePrefix(sql_page_index) + pdm_name.substring(0, pdm_name.lastIndexOf(".")).toLowerCase()
				+ ".sql";

		/* ���sql�ű��ļ������·�� */
		sqlFile = new File(sql_root + File.separator + "script" + File.separator + "dbcreate" + File.separator + dbType
				+ File.separator + sqlDir + File.separator + sqlFileName);
		return sqlFile;
	}

	/**
	 * �÷�������SQL�ű���Ŀ¼��Ԥ�ýű���𷵻�Ԥ�ýű�����Դ洢·��
	 * ����ǹ���ӳ�䣬����·��Ϊsql_root/script/init/����ӳ��������/itemVo�е�itemRule
	 * ����Ƿ�ҵ��ģ��ӳ�䣬����·��Ϊsql_root/script/init/business/ҵ��ӳ��������/item�е�itemRule
	 * 
	 * @param sql_root
	 * @param itemVo
	 * @param db_type
	 *            ��ʱ����
	 * @return
	 */
	public File getSQLFileByItem(String sql_root, Item itemVo, String db_type) {
		File sqlFile = null;
		/* ���sql�ű��ļ������·�� */
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
	 * �÷����ж�ָ��Ԥ�ýű��Ƿ�ΪNC����Ԥ�ýű�
	 * 
	 * @param item_vo
	 * @return
	 */
	public boolean isCommonScriptType(Item item_vo) {
		return getCommonMap().containsKey(item_vo.getItemRule());
	}

	/**
	 * �÷�������ָ��Ԥ�ýű���ӳ��Ŀ¼, ���Ϊcommon,��common.map�л�ȡ��
	 * �����ģ������ģ���modulemap�л�ȡ
	 * 
	 * @param isCommonScript
	 *            �Ƿ�Ϊcommonmap
	 * @param itemVo
	 * @return
	 * @throws SDPException ��������ļ��в����ڴ�ӳ�䣬�׳��쳣
	 */
	public String getMappingDirName(boolean isCommonScript, Item itemVo) throws SDPException {
		String result = null;
		String itemRule = itemVo.getItemRule();
		if (isCommonScript) {
			result = getCommonMap().get(itemRule);
			if (result == null) {
				throw new SDPException("�ű�����[" + itemVo.getItemName() + "]�������ļ�common.map�в�����!");
			}
		} else {
			result = getModuleMap().get(itemRule);
			if (result == null) {
				throw new SDPException("�ű�����[" + itemVo.getItemName() + "]�������ļ�module.map�в�����");
			}
		}
		return result;
	}

	/**
	 * �÷����ж��Ƿ���Ҫ������ʽ�Զ������Ķ���������
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
	 * �÷����ж��Ƿ���Ҫ������ѯ�Զ������Ķ���������
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
