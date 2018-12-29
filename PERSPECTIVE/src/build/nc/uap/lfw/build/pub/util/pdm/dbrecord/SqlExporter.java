package nc.uap.lfw.build.pub.util.pdm.dbrecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.core.vo.RecordGenerateMessageVO;
import nc.uap.lfw.build.core.vo.RecordValidateMessage;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.CommonUtil;
import nc.uap.lfw.build.pub.util.pdm.FileUtil;
import nc.uap.lfw.build.pub.util.pdm.SDPConnection;
import nc.uap.lfw.build.pub.util.pdm.SqlBuildRule;
import nc.uap.lfw.build.pub.util.pdm.SqlBuildRuleUtil;
import nc.uap.lfw.build.pub.util.pdm.itf.IExport;
import nc.uap.lfw.build.pub.util.pdm.itf.ISql;
import nc.uap.lfw.build.pub.util.pdm.vo.Item;
import nc.uap.lfw.build.pub.util.pdm.vo.MainTable;
import nc.uap.lfw.build.pub.util.pdm.vo.SubTable;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;
import nc.uap.lfw.build.pub.util.pdm.vo.TableField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Ԥ�ýű�����ʵ����
 * 
 * ������ʽ: ���� �ɷ��ʷ���: exportProductScript, exportModuleScript, exportScript
 * 
 * @author fanp
 */
public class SqlExporter implements IExport {
	/** Ԥ�ýű���� */
	private Item item = null;

	/** Ԥ�ýű��洢��ַ */
	private String sqlRoot = null;

	/** ����Ԥ�ýű�ӳ���ļ��洢��ַ */
	private String commonMapRoot = null;

	/** ģ��Ԥ�ýű�ӳ���ļ��洢��ַ */
	private String moduleMapRoot = null;

	/** ���ӱ��ز���ϵ�����ļ��洢��ַ */
	private String multiTableHierarchyRoot = null;
	
	/** ģ�����ӱ��ز���ϵ�����ļ��洢��ַ */
	private String moduleMultiTableHierarchyRoot;
	
//	private InstallDiskDescriptionVO installDiskVO = null;
	
	private Map<String, PkInfo> illegalPkMap = new LinkedHashMap<String, PkInfo>();

	// /**
	// * ���ɽű�ʱ�����Ĵ�����Ϣ������ڴ�
	// */
	// private List<RecordValidateMessage> errorMsgList = new
	// ArrayList<RecordValidateMessage>();

	/**
	 * ����Ԥ�ýű�ʱ��������ϢVO����
	 */
	private RecordGenerateMessageVO messageVo = new RecordGenerateMessageVO();

	/**
	 * @param item
	 * @param sql_root
	 * @param common_map_root
	 * @param module_map_root
	 * @param multi_table_hierarchy_root
	 */
	public SqlExporter(Item item, String sql_root, String common_map_root, String module_map_root,
			String multi_table_hierarchy_root, String moduleMultiTableHierarchyRoot 
			) {
		this.item = item;
		this.sqlRoot = sql_root;
		this.commonMapRoot = common_map_root;
		this.moduleMapRoot = module_map_root;
		this.multiTableHierarchyRoot = multi_table_hierarchy_root;
		this.moduleMultiTableHierarchyRoot = moduleMultiTableHierarchyRoot;
//		this.installDiskVO = installDiskVO;
	}

	public RecordGenerateMessageVO getMessageVo() {
		return messageVo;
	}

	/**
	 * �÷�������ָ����Ʒ��ȫ����ʼ���ű�
	 * 
	 * @param
	 * @return
	 */
	public void exportProductScript() {
	}

	/**
	 * �÷�������ָ��ģ���ȫ����ʼ���ű�
	 * 
	 * @param
	 * @return
	 */
	public void exportModuleScript() {
	}

	/**
	 * �÷�������ָ��Ԥ�ýű�, ͬʱ���ظýű��ĸ�Ŀ¼ ʹ��˵��: ���Ա��鷽ʽ����"��ӡģ��"ʱ, �����ֶι̶�Ϊ'vnodecode'
	 * ���ʧ�ܣ�����null, ��ʱӦ����messageVo�õ��������Ϣ
	 * 
	 * @param con
	 *            SDPConnection��ǰ����
	 * @return
	 */
	public File exportScript(SDPConnection con) {
		/* У��Ԥ�ýű�����Ŀ¼ */
		SqlBuildRule rule = new SqlBuildRule(getCommonMapRoot(), getModuleMapRoot(), getMultiTableHierarchyRoot());
		// �õ����ɽű���λ�ã���ɾ���ٴ���
		File scriptRoot = rule.getSQLFileByItem(getSqlRoot(), getItem(), ISql._SQLSERVER_);
		if (scriptRoot == null) {
			messageVo.getMessageList().add(
					new RecordValidateMessage("����", "�ű�����[" + getItem().getItemName() + "]��Ӧ�Ľű�Ŀ¼�����ļ���������ִ��У�������"));
			return null;
		}
		if (scriptRoot.exists()) {
			try {
				FileUtil.forceDelete(scriptRoot);
			} catch (IOException e) {
				messageVo.getMessageList().add(
						new RecordValidateMessage("����", "�ű�����[" + getItem().getItemName() + "]���ɽű���λ�ã�" + scriptRoot
								+ " �ļ�����ʧ�ܣ�"));
				MainPlugin.getDefault().logError(e.getMessage(), e);
				return null;
			}
		}
		FileUtil.forceCreate(scriptRoot.getPath(), true);
		/* ȷ��"SQL�ָ���" */
		String sqlDelimiter = "\r\ngo";
		/* �������ز��ṹ��Ԫ������Ϣ */
		/*---------------------------------------------------------------------------
		 * Ч���Ż�, ���ٻ�ȡ��Ԫ���ݲ����ĵ��ô���(�˲����漰���ݿ����)
		 * MainTable mainTable = rule.getMultiTableHierarchyStructure(getItem());
		 --------------------------------------------------------------------------*/
		MainTable mainTable;
		try {
			mainTable = SqlBuildRuleUtil.getMultiTableHierarchyStructure(getItem(), con,  getMultiTableHierarchyRoot(), moduleMultiTableHierarchyRoot);
			//������
			if(!isValidTable(mainTable.getTableMetaData())){
				messageVo.setSuccess(false);
				messageVo.getMessageList().add(new RecordValidateMessage("����", mainTable.getTableName() + " �������ݿ��в����ڻ�δ�����У�"));
				return null;
			}
			if(mainTable.getSubTableSet() == null || mainTable.getSubTableSet().size() == 0){
				messageVo.getMessageList().add(new RecordValidateMessage("��ʾ", "��" + getItem().getItemRule() + "����Ϊ����ṹ"));
			} else {
				messageVo.getMessageList().add(new RecordValidateMessage("��ʾ", "��" + getItem().getItemRule() + "����Ϊ���ӱ�ṹ, �����ļ�����Ϊ:"
						+ getItem().getItemRule()+".xml"));
			}
		} catch (SDPBuildException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			messageVo.getMessageList().add(
					new RecordValidateMessage("��" + getItem().getItemRule() + "��������", e.getMessage()));
			return null;
		}
		MainPlugin.getDefault().logInfo("���ز��ṹ�������");

		/* ����"�Զ����ѯ"������ִ�����⴦��_���������� */
		if (getItem().getItemName().equalsIgnoreCase("�Զ����ѯ")) {
			customerCode(con, scriptRoot, rule);
			return scriptRoot;
		}

		try {
			String itemRule = getItem().getItemRule();
			/* ���ھ۴������ĵ�����ִ�����⴦�� */
			if (itemRule.equalsIgnoreCase("md_module") || itemRule.equalsIgnoreCase("pd_dd")
					|| itemRule.equalsIgnoreCase("md_class") || itemRule.equalsIgnoreCase("md_enumvalue")) {
				customerKeyExport(mainTable, scriptRoot, con, itemRule, sqlDelimiter);
				return scriptRoot;
			}

			/* �ж��Ƿ���Ҫ��"���鷽ʽ"���� */
			boolean needGrp = getItem().getGrpField() != null && !getItem().getGrpField().equals("");
			if (needGrp) { // �Ա��鷽ʽ����
				exportByGroup(mainTable, scriptRoot, con, itemRule, sqlDelimiter, rule);
			} else { // �Ǳ��鷽ʽ����
				exportByNoGroup(mainTable, scriptRoot, con, itemRule, sqlDelimiter, rule);
			}
			messageVo.setSuccess(true);
		} catch (SDPBuildException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			messageVo.getMessageList().add(
					new RecordValidateMessage("����" + getItem().getItemRule() + "ʱ��������", e.getMessage()));
			return null;
		}
		return scriptRoot;
	}

	/**
	 * �÷������ݴ�ӡ������Ψһֵ�б�(print_data_item)���ض�Ӧ�Ĳ�ѯ����
	 * 
	 * @param unique_vnodecode_list
	 * @return
	 */
	private String getPrintDataItemWhereCondition(String[] unique_vnodecode_list) {
		if (unique_vnodecode_list == null || unique_vnodecode_list.length == 0)
			return "1 != 1";

		StringBuffer sb = new StringBuffer(" vnodecode in ( ");
		for (int i = 0; i < unique_vnodecode_list.length; i++) {
			String vNodeCode = unique_vnodecode_list[i];
			sb.append(vNodeCode);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" )");

		return sb.toString();
	}

	/**
	 * �÷�������һ����ŷ���ָ��������е�ͶӰ�������صڼ�������
	 * 
	 * @param pk_value_list
	 * @param index_list
	 *            ���ֵ��ӦΪ"1","2"���ַ�������
	 * @return
	 */
	private List<List<String>> getResultSetAfterGroup(List<List<String>> result_set, String[] index_list) {
		if (result_set == null || result_set.size() == 0)
			return null;
		if (index_list == null || index_list.length == 0)
			return null;
		List<List<String>> result = new ArrayList<List<String>>();
		for (int i = 0; i < index_list.length; i++) {
			int index = Integer.parseInt(index_list[i]);
			List<String> record = result_set.get(index);
			result.add(record);
		}
		return result;
	}

	/**
	 * �÷�������һ����ŷ���ָ��PKֵ�б��е�ͶӰ
	 * 
	 * @param pk_value_list
	 * @param index_list
	 * @return
	 */
	private String[] getPKValueListAfterGroup(String[] pk_value_list, String[] index_list) {
		if (index_list == null || index_list.length == 0)
			return null;

		if (pk_value_list == null || pk_value_list.length == 0)
			return null;

		List<String> v = new ArrayList<String>();
		for (int i = 0; i < index_list.length; i++) {
			int index = Integer.parseInt(index_list[i]);
			v.add(pk_value_list[index]);
		}// for
		return v.toArray(new String[0]);
	}
	
	/**
	 * �÷���Ϊ����ṹ��Ψһ��������sql�ű��ļ�����д�뵽sql_file_path·����
	 * 
	 * @param tableVo
	 *            ��VO
	 * @param sqlFilePath
	 *            ������ļ�·��
	 * @param sql_delimiter
	 *            sql�ָ���
	 * @param result_set
	 *            ���ݽ����
	 */
	private void generateSingleTableSqlFile(Table tableVo, String sqlFilePath, String sql_delimiter,
			List<List<String>> result_set) {
		BufferedWriter fileWriter = null;
		try {
			/* ����sql�ļ� */
			File sqlFile = new File(sqlFilePath);
			if (sqlFile.exists())
				FileUtils.forceDelete(sqlFile);
			sqlFile.createNewFile();
			fileWriter = FileUtil.getFileWriter(sqlFile);

			SqlExporterUtils.outputResultToWriter(tableVo, fileWriter, sql_delimiter, result_set);
		} catch (IOException e) {
			//TODO: �쳣û�н��д���
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}  finally {
			if (fileWriter != null) {
				IOUtils.closeQuietly(fileWriter);
			}
		}
	}

	/**
	 * �÷���Ϊָ����"��-��"���ز��ṹ�ݹ����ɸ���sql�ű��ļ�
	 * 
	 * @param sub_table
	 * @param pk_value_list
	 * @param sql_root
	 * @param delimiter
	 * @param uds
	 * @param con
	 */
	private void generateMultiTableSqlFile(SubTable sub_table, String[] pk_value_list, String sql_root,
			String delimiter, UnitDataSource uds, SDPConnection con, MainTable mainTable) {
		try {
			/* ��õ�ǰ�ӱ�����ơ���������ơ����ӵ���������sql�ű���� */
			String subTableName = sub_table.getTableName();
			String fkFieldName = sub_table.getForeignKeyColumn();
			String additionalWhereCondition = sub_table.getWhereCondition();
			String sqlNo = sub_table.getSqlNo();

			/* ��õ�ǰ�ӱ��Ԫ���� */
			// Table subTableVO = getTable(uds.getDatabaseType(), uds.getUser(),
			// subTableName, con);
			Table subTableVO = mainTable.lookup(subTableName);
			if(!isValidTable(subTableVO)){
				messageVo.getMessageList().add(new RecordValidateMessage("����", "��" + subTableName + "�����ڻ�δ�����С�"));
				return;
			}

			/* ���ݹ��쵱ǰ�ӱ�Ĳ�ѯ����, ִ�����ݿ��ѯ, ��ý���� */
			String treatedWhereCondition = getTreatedWhereCondition(fkFieldName, pk_value_list);
			List<List<String>> resultSet = SqlExporterUtils.getTableDataByWhereCondition(subTableVO, treatedWhereCondition,
					additionalWhereCondition, con);
			
			List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(subTableVO, null, resultSet, false);
			String[] pkValueList = v.get(0).toArray(new String[0]);
//			//����OIDMarkУ��
//			if(subTableVO.getTablePrimaryKey() != null && subTableVO.getTablePrimaryKey().length > 0){
//				checkOidMark(pkValueList, subTableName, subTableVO.getTablePrimaryKey()[0]);
//			}
			//addOidVldMsg(subTableName);

			/* ���ɵ�ǰ�ӱ��sql�ű��ļ� */
			String sqlFilePath = sql_root + File.separator + sqlNo + ".sql";
			generateSingleTableSqlFile(subTableVO, sqlFilePath, delimiter, resultSet);

			/* �ݹ����ɵ�ǰ�ӱ��ȫ���¼�����sql�ű��ļ� */
			List<SubTable> childrenTableSet = sub_table.getSubTableSet();
			if (childrenTableSet == null || childrenTableSet.size() == 0) {
				/* ��ǰ�ӱ��Ѿ���ĩ���ӱ�, �ݹ鷵�� */
				return;
			} else {
				/* ��ǰ�ӱ��ĩ���ӱ�, �����ݹ��������¼�����sql�ű��ļ� */
				for (SubTable childTable : childrenTableSet) {
					generateMultiTableSqlFile(childTable, pkValueList, sql_root, delimiter, uds, con, mainTable);
				}// for
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}
	}


	/**
	 * �÷����������⴦��(��100Ϊ��λ����)����ӱ��ѯ���� ˵��:
	 * ��Oracle��"select %foreign key field% in (%pk list%)"����sql�����"�б��ܳ�<1000"����,
	 * ��ȡ���²��� ���ɲ�ѯ����:
	 * ------------------------------------------------------------
	 * ---------------------------------- ÿ�鳤��Ϊ'100',
	 * (1)����һ��(������<100)��PK�б�ֱ������"%foreign key field% in (%pk list%)"���Ͳ�ѯ�� ��;
	 * (2)��һ��(������>=100)��PK�б��������"%foreign key field% in (%pk list%)"���͵�Ԫ��ѯ�������ٰ�
	 * 'or'�ϲ������ղ�ѯ������
	 * 
	 * @param field
	 * @param field_value_list
	 * @return
	 */
	private String getTreatedWhereCondition(String field_name, String[] field_value_list) {
		if (field_name == null || "".equals(field_name)) {
			return "1 = 1";
		}

		if (field_value_list == null || field_value_list.length == 0) {
			return "1 != 1";
		}

		MainPlugin.getDefault().logInfo("���鴦��ǰ��PK��ֵ�б���: " + field_value_list.length);
		String whereCondition = null;

		if (field_value_list.length >= 100) {
			/* ��һ�� */
			whereCondition = "";

			StringBuffer sb = new StringBuffer();
			boolean next_group_flag = true;
			int next_group_start_index = 0;
			int next_group_end_index = 99;

			while (true) {
				for (int j = next_group_start_index; j <= next_group_end_index; j++) {
					// whereCondition += ("'" + field_value_list[j] + "',");
					whereCondition += (field_value_list[j] + ",");
				}// for
				whereCondition = whereCondition.substring(0, whereCondition.length() - 1);
				whereCondition = " (" + field_name + " in (" + whereCondition + "))";
				sb.append(whereCondition);
				sb.append(" ");
				sb.append("or");

				if (!next_group_flag)
					break;

				/* ׼����һ�� */
				if (next_group_end_index + 100 < field_value_list.length - 1) {
					next_group_start_index = next_group_end_index + 1;
					next_group_end_index += 100;
					next_group_flag = true;
				} else {
					next_group_start_index = next_group_end_index + 1;
					next_group_end_index = field_value_list.length - 1;
					next_group_flag = false;
				}
				whereCondition = "";
			}// while

			/* ɾ����β�����' or' */
			sb.delete(sb.length() - 3, sb.length());
			whereCondition = sb.toString();
		} else {
			/* ����һ�� */
			whereCondition = "";
			for (int j = 0; j < field_value_list.length; j++) {
				// whereCondition += ("'" + field_value_list[j] + "',");
				whereCondition += (field_value_list[j] + ",");
			}
			whereCondition = whereCondition.substring(0, whereCondition.length() - 1);
			whereCondition = " " + field_name + " in (" + whereCondition + ")";
		}
		MainPlugin.getDefault().logInfo("���鴦���Ĳ�ѯ����: " + whereCondition);

		return whereCondition;
	}

	/**
	 * ����ѡ��������С�����Ŀ¼�е�ֵ�б�
	 * 
	 * @param table_vo
	 * @param item_vo
	 * @param record_set
	 * @param need_group
	 */
	private List<List<String>> getPkFiledValueListAndGroupFieldValueList(Table table_vo, Item item_vo,
			List<List<String>> record_set, boolean need_group) {
		List<List<String>> result = new ArrayList<List<String>>();

		List<String> vecPKFieldList = new ArrayList<String>();
		int pkFieldIndex = -1;
		String tablePrimaryKeyFieldName = null;

		List<String> vecGroupFieldList = new ArrayList<String>();
		int grpFieldIndex = -1;
		String tableGroupFieldName = null;

		boolean pkExist = table_vo.getTablePrimaryKey() != null && table_vo.getTablePrimaryKey().length > 0;
		
		/* ���PK�ֶΡ�PK�ֶ��к� */
		if(pkExist){
			tablePrimaryKeyFieldName = table_vo.getTablePrimaryKey()[0];
			MainPlugin.getDefault().logInfo("PK��: " + tablePrimaryKeyFieldName);
			pkFieldIndex = getPKFieldIndex(table_vo);
			MainPlugin.getDefault().logInfo("PK�����: " + pkFieldIndex);
		}
		
		/* ��ñ����ֶΡ������ֶ��к� */
		if (need_group) {
			tableGroupFieldName = item_vo.getGrpField();
			MainPlugin.getDefault().logInfo("������: " + tableGroupFieldName);
			grpFieldIndex = getGroupFieldIndex(table_vo, item_vo);
			MainPlugin.getDefault().logInfo("���������: " + grpFieldIndex);
		}
		
		/* ���PK��ֵ�б�������ֵ�б� */
		for (int i = 0; i < record_set.size(); i++) {
			List<String> tableRecord = record_set.get(i);
			
			if(pkExist){
				String pkFieldValue = tableRecord.get(pkFieldIndex);
				vecPKFieldList.add(pkFieldValue);
			}
			
			if (need_group) {
				String grpFieldValue = tableRecord.get(grpFieldIndex);
				vecGroupFieldList.add(grpFieldValue);
			}
		}// for
		MainPlugin.getDefault().logInfo("PK��ֵ�б�����: " + vecPKFieldList.size());
		if (need_group)
			MainPlugin.getDefault().logInfo("������ֵ�б�����:" + vecGroupFieldList.size());
		

		result.add(vecPKFieldList);
		result.add(vecGroupFieldList);

		return result;
	}

	/**
	 * �÷�������ָ�����Ψһ�����ֶε����
	 * 
	 * @param table_vo
	 * @return
	 */
	private static int getPKFieldIndex(Table table_vo) {
		String pkField = table_vo.getTablePrimaryKey()[0];
		TableField[] tableFields = table_vo.getTableFields();
		for (int i = 0; i < tableFields.length; i++) {
			TableField field = tableFields[i];
			if (field.getFieldName().equalsIgnoreCase(pkField))
				return i;
		}
		return -1;
	}

	/**
	 * �÷�������ָ����ı����ֶε����
	 * 
	 * @param table_vo
	 * @param item_vo
	 * @return
	 */
	private static int getGroupFieldIndex(Table table_vo, Item item_vo) {
		String grpField = item_vo.getGrpField();
		TableField[] tableFields = table_vo.getTableFields();
		for (int i = 0; i < tableFields.length; i++) {
			TableField field = tableFields[i];
			if (field.getFieldName().equalsIgnoreCase(grpField))
				return i;
		}
		return -1;
	}

	/**
	 * �÷������ָ���������������Ƿ�֧�ֽű�����
	 * 
	 * @param table_vo
	 * @return
	 */
	private void checkPrimaryKey(Table table_vo) throws SDPBuildException {
		if (table_vo == null) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4010));
		}
		String[] pks = table_vo.getTablePrimaryKey();
		if (pks == null || pks.length == 0) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4010));
		} else if (pks.length > 1) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4011));
		} else
			return;
	}

	/**
	 * �÷�����������������
	 * 
	 * @param item_vo
	 * @return
	 */
	private String getWhereCond(Item item_vo) {
		String condition = item_vo.getFixedWhere();
		condition = CommonUtil.spaceToNull(condition);
		return condition;
	}

	private Item getItem() {
		return this.item;
	}

	private String getSqlRoot() {
		return this.sqlRoot;
	}

	private String getCommonMapRoot() {
		return this.commonMapRoot;
	}

	private String getModuleMapRoot() {
		return this.moduleMapRoot;
	}

	private String getMultiTableHierarchyRoot() {
		return this.multiTableHierarchyRoot;
	}

	/**
	 * �Զ����ѯ�Ľű�����
	 * 
	 * @param con
	 * @param scriptRoot
	 * @param rule
	 */
	private void customerCode(SDPConnection con, File scriptRoot, SqlBuildRule rule) {
		boolean bFmdSuccess = true;
		boolean bQmdSuccess = true;
		if (getItem().getItemName().equalsIgnoreCase("�Զ����ѯ")) {
			if (rule.isFmdNeeded() != null) {
				/* ����"��ʽ����Ŀ¼" */
				MainPlugin.getDefault().logInfo("��������: FMD");
				String fid = rule.isFmdNeeded();
//				if (ExportUtil.onExportBinary(fid, "", true, scriptRoot.getAbsolutePath(), con.getPhysicalConnection())) {
//					MainPlugin.getDefault().logInfo("FMD �����ɹ�");
//				} else {
//					MainPlugin.getDefault().logInfo("FMD ����ʧ��");
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("��������:FMD��������", "�Զ����ѯ:" + getItem().getItemRule() + " ��������"));
//					bFmdSuccess = false;
//				}
			}

			if (rule.isQmdNeeded() != null) {
				/* ����"��ѯ����Ŀ¼" */
				MainPlugin.getDefault().logInfo("��������: QMD");
				String qid = rule.isQmdNeeded();
//				if (ExportUtil
//						.onExportBinary(qid, "", false, scriptRoot.getAbsolutePath(), con.getPhysicalConnection()))
//					MainPlugin.getDefault().logInfo("QMD �����ɹ�");
//				else {
//					MainPlugin.getDefault().logInfo("QMD ����ʧ��");
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("��������:QMD��������", "�Զ����ѯ:" + getItem().getItemRule() + " ��������"));
//					bQmdSuccess = false;
//				}
			}
			
			messageVo.setSuccess(bFmdSuccess && bQmdSuccess);
			// return scriptRoot;
		}
	}

	/**
	 * ���ھ۴������ĵ�����ִ�����⴦��
	 * 
	 * @param mainTable
	 * @param scriptRoot
	 * @param con
	 * @param itemRule
	 * @param sqlDelimiter
	 * @throws SDPBuildException
	 */
	private void customerKeyExport(MainTable mainTable, File scriptRoot, SDPConnection con, String itemRule,
			String sqlDelimiter) throws SDPBuildException {
		boolean needGroup = getItem().getGrpField() != null && !getItem().getGrpField().equals("");
		if (needGroup) {
			/* ��Ҫ���� */
			MainPlugin.getDefault().logInfo("�Ա��鷽ʽ����Ԥ�ýű�...");
			throw new SDPBuildException("��:" + getItem().getItemRule() + " ��֧���Է��鷽ʽ����");
			// ...
		} else {
			/* ����Ҫ���� */
			MainPlugin.getDefault().logInfo("�ԷǱ��鷽ʽ����Ԥ�ýű�...");
			/* ȡ�������Ԫ����, У�������ֶ� */
			Table tableVO = mainTable.lookup(itemRule);
			/* ���������ѯ����ִ�����ݿ��ѯ, ��ý���� */
			String whereCondition = getWhereCond(getItem());
			List<List<String>> resultSet;
			try {
				resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
				
				Map<String, List<String>> pksMapValues = getPkValues(resultSet, tableVO);
//				for(Map.Entry<String, List<String>> entry : pksMapValues.entrySet()){
//					checkOidMark(entry.getValue().toArray(new String[0]), tableVO.getTableName(), entry.getKey());
//				}
				
				/* ����sql�ű��ļ� */
				if (!mainTable.isAssociatedTables()) {
					/* ����ṹ */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
					messageVo.setSuccess(true);
				} else {
					/* ���ӱ�ṹ */
					throw new SDPBuildException("��:" + getItem().getItemRule() + " ��֧�����ӱ�����ݵ���");
				}
			} catch (SQLException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
				throw new SDPBuildException("��:" + getItem().getItemRule() + " ����ָ��where����ִ�����ݿ��ѯ���������������");
			}

		}
	}

	/**
	 * �Է��鷽ʽ�����ű�
	 * 
	 * @param mainTable
	 * @param scriptRoot
	 * @param con
	 * @param itemRule
	 * @param sqlDelimiter
	 * @param rule
	 * @throws SDPBuildException
	 */
	private void exportByGroup(MainTable mainTable, File scriptRoot, SDPConnection con, String itemRule,
			String sqlDelimiter, SqlBuildRule rule) throws SDPBuildException {
		UnitDataSource uds = con.getDataSource();
		/* ��Ҫ���� */
		MainPlugin.getDefault().logInfo("�Ա��鷽ʽ����Ԥ�ýű�...");

		Table tableVO = mainTable.lookup(itemRule);
		try {
			/* ������������Ϊ�۴�����(Clustered Primary Key)�Ĳ��ڴ˴�����, ��Ҫ�����������⴦�� */
			if (mainTable.getSubTableSet() != null && mainTable.getSubTableSet().size() > 0) {
				checkPrimaryKey(tableVO);
			}
		} catch (SDPBuildException sdp_build_exception) {
			throw sdp_build_exception;
		}

		/* ���������ѯ����ִ�����ݿ��ѯ, ��ý���� */
		String whereCondition = getWhereCond(getItem());
		List<List<String>> resultSet;
		try {
			resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
		} catch (SQLException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			throw new SDPBuildException(itemRule + " ����ָ��where����ִ�����ݿ��ѯ���������������");
		}

		/* ��������������ȡ��PK��ֵ�б�Ψһ������ֵ�б� */
		List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(tableVO, getItem(), resultSet, true);
		String[] pkValueList = v.get(0).toArray(new String[0]);
		String[] grpValueList = v.get(1).toArray(new String[0]);
		String[] uniqueGrpValueList = CommonUtil.getUniqueValueList(grpValueList);
		
		//����OIDMarkУ��
//		if(tableVO.getTablePrimaryKey() != null && tableVO.getTablePrimaryKey().length > 0){
//			checkOidMark(pkValueList, tableVO.getTableName(), tableVO.getTablePrimaryKey()[0]);
//		}
		//addOidVldMsg(tableVO.getTableName());

		if (uniqueGrpValueList != null && uniqueGrpValueList.length > 0) {
			for (int m = 0; m < uniqueGrpValueList.length; m++) {
				/* ȡ�õ�ǰΨһ����ֵ */
				String grpValue = uniqueGrpValueList[m];

				/* �Ե�ǰΨһ����ֵ�ָ�ԭʼPKֵ�б������, �õ���Ӧ��"PKֵ�б�ͶӰ"��"�����ͶӰ" */
				String[] indexList = CommonUtil.getValueIndexListBySpecifiedValue(grpValueList, grpValue);
				String[] pkValueListAfterGroup = getPKValueListAfterGroup(pkValueList, indexList);
				List<List<String>> resultSetAfterGroup = getResultSetAfterGroup(resultSet, indexList);

				/* ������ǰ���ֶ�ֵ�ı���Ŀ¼ */
				String groupDirName = tableVO.getTableName() + "_" + CommonUtil.replace(grpValue, "'", "");
				FileUtil.forceCreate(scriptRoot.getAbsolutePath() + File.separator + groupDirName, true);

				/* ���ɵ�ǰ�����ֶ�ֵ��sql�ű��ļ� */
				if (!mainTable.isAssociatedTables()) {
					/* ����ṹ */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
							+ sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSetAfterGroup);
					continue;
				} else {
					/* ���ӱ�ṹ */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
							+ sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSetAfterGroup);

					/*
					 * �統ǰ"����"��"pub_print_template",
					 * ������"���������"��ʽ���ӵ���"pub_print_dataitem"�ӱ�,
					 * ���ӱ�����"��-�ӱ����������ϵ"��������, �䵼���������ɱ���ֵ����
					 */
					if (tableVO.getTableName().equalsIgnoreCase("pub_print_template")) {
						/* ����SubTable */
						SubTable print_data_item_subtable = new SubTable();
						print_data_item_subtable.setForeignKeyColumn(null);
						print_data_item_subtable.setSqlNo("006");
						print_data_item_subtable.setSubTableSet(null);
						print_data_item_subtable.setTableName("pub_print_dataitem");
						print_data_item_subtable.setWhereCondition(null);

						/* ���Ԫ���� */
						Table print_data_item_tablevo;
						try {
							print_data_item_tablevo = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(),
									print_data_item_subtable.getTableName(), con);
						} catch (SQLException e) {
							MainPlugin.getDefault().logError(e.getMessage(), e);
							throw new SDPBuildException(itemRule + " ���Ԫ������Ϣʱ��������", e);
						}
						if(isValidTable(print_data_item_tablevo)){
							/* ���ݴ�ӡ������Ψһֵ�б�(print_data_item)�����Ӧ�Ĳ�ѯ���� */
							String print_data_item_wherecondition = getPrintDataItemWhereCondition(new String[] { grpValue });

							/* ִ�в�ѯ�õ������ */
							String print_data_item_additionalwherecondition = print_data_item_subtable.getWhereCondition();
							List<List<String>> print_data_item_resultset;
							try {
								print_data_item_resultset = SqlExporterUtils.getTableDataByWhereCondition(print_data_item_tablevo,
										print_data_item_wherecondition, print_data_item_additionalwherecondition, con);
							} catch (SQLException e) {
								MainPlugin.getDefault().logError(e.getMessage(), e);
								throw new SDPBuildException(itemRule + "��ӡ������Ψһֵ�б�����ִ�����ݿ��ѯ���������������", e);
							}
							
//							//У��pub_print_dataitem��oidMark��
//							List<List<String>> printItemPkGrps = getPkFiledValueListAndGroupFieldValueList(print_data_item_tablevo, null, print_data_item_resultset, false);
//							String[] pkValues = printItemPkGrps.get(0).toArray(new String[0]);
//							if(print_data_item_tablevo.getTablePrimaryKey() != null && print_data_item_tablevo.getTablePrimaryKey().length > 0){
//								checkOidMark(pkValues, print_data_item_tablevo.getTableName(), print_data_item_tablevo.getTablePrimaryKey()[0]);
//							}

							/* �õ��ű��ļ�ȫ·�� */
							sqlFileName = print_data_item_subtable.getSqlNo() + ".sql";
							sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
									+ sqlFileName;

							/* ��������"print_data_item"�ӱ�sql�ű��ļ� */
							generateSingleTableSqlFile(print_data_item_tablevo, sqlFilePath, sqlDelimiter,
									print_data_item_resultset);
						}
					}

					/* �ݹ�����"����"֮"ȫ���¼��ӱ�"��sql�ű��ļ� */
					List<SubTable> subTableSet = mainTable.getSubTableSet();
					for (int i = 0; i < subTableSet.size(); i++) {
						SubTable subTable = (SubTable) subTableSet.get(i);
						generateMultiTableSqlFile(subTable, pkValueListAfterGroup, scriptRoot.getAbsolutePath()
								+ File.separator + groupDirName, sqlDelimiter, uds, con, mainTable);
					}// for
					continue;
				}
			}// for
		}
		
		handleAllOidVldMsg();
	}
	
	private Map<String, List<String>> getPkValues(List<List<String>> resultSet, Table table){
		Map<String, List<String>> pkValues = new HashMap<String, List<String>>();
		String[] pkNames = table.getTablePrimaryKey();
		if(pkNames != null && pkNames.length > 0 && resultSet != null && !resultSet.isEmpty()){
			List<Integer> pkIndexs = new ArrayList<Integer>();
			TableField[] fields = table.getTableFields();
			for(String pkName : pkNames){
				for(int i = 0; i<fields.length; i++){
					if(fields[i].getFieldName().equals(pkName)){
						pkIndexs.add(i);
						break;
					}
				}
			}
			for(int i = 0; i<pkNames.length; i++){
				List<String> values = new ArrayList<String>();
				for(List<String> result : resultSet){
					values.add(result.get(pkIndexs.get(i)));
				}
				pkValues.put(pkNames[i], values);
			}
		}
		return pkValues;
	}

	/**
	 * �Ƿ��鷽ʽ�����ű�
	 * 
	 * @param mainTable
	 * @param scriptRoot
	 * @param con
	 * @param itemRule
	 * @param sqlDelimiter
	 * @param rule
	 * @throws SDPBuildException
	 */
	private void exportByNoGroup(MainTable mainTable, File scriptRoot, SDPConnection con, String itemRule,
			String sqlDelimiter, SqlBuildRule rule) throws SDPBuildException {
		UnitDataSource uds = con.getDataSource();
		/* ����Ҫ���� */
		MainPlugin.getDefault().logInfo("�ԷǱ��鷽ʽ����Ԥ�ýű�...");

		/* ȡ�������Ԫ����, �����۴�У�� */
		// Table tableVO = getTable(uds.getDatabaseType(), uds.getUser(),
		// getItem().getItemRule(), con);
		Table tableVO = mainTable.lookup(itemRule);
		try {
			if (mainTable.getSubTableSet() != null && mainTable.getSubTableSet().size() > 0) {
				checkPrimaryKey(tableVO);
			}
		} catch (SDPBuildException sdp_build_exception) {
			/* ������������Ϊ�۴�����(Clustered Primary Key)�Ĳ��ڴ˴�����, ��Ҫ�����������⴦�� */
			throw sdp_build_exception;
		}

		/* ���������ѯ����ִ�����ݿ��ѯ, ��ý���� */
		String whereCondition = getWhereCond(getItem());
		List<List<String>> resultSet;
		try {
			resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
		} catch (SQLException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			throw new SDPBuildException(itemRule + " ����ָ��where����ִ�����ݿ��ѯ���������������");
		}
		
		List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(tableVO, null, resultSet, false);
		String[] pkValueList = v.get(0).toArray(new String[0]);
		
//		//����OIDMarkУ��
//		if(tableVO.getTablePrimaryKey() != null && tableVO.getTablePrimaryKey().length > 0){
//			checkOidMark(pkValueList, tableVO.getTableName(), tableVO.getTablePrimaryKey()[0]);
//		}

		/* ����sql�ű��ļ� */
		if (!mainTable.isAssociatedTables()) {
			/* ����ṹ */
			String sqlFileName = mainTable.getSqlNo() + ".sql";
			String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
			generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
		} else {
			/* ���ӱ�ṹ */
			String sqlFileName = mainTable.getSqlNo() + ".sql";
			String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
			generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);

			/* �ݹ�����"����"֮"ȫ���¼��ӱ�"��sql�ű��ļ� */
			List<SubTable> subTableSet = mainTable.getSubTableSet();
			
			for (SubTable subTable : subTableSet) {
				generateMultiTableSqlFile(subTable, pkValueList, scriptRoot.getAbsolutePath(), sqlDelimiter, uds, con,
						mainTable);
			}// for
		}
		handleAllOidVldMsg();
	}
	
	private boolean isValidTable(Table tableVO){
		return tableVO != null && tableVO.getTableFields() != null && tableVO.getTableFields().length >0;
	}
	
//	/**
//	 * У��������OidMark��
//	 * 
//	 * @param pkValues ����ֵ��
//	 * @param oidMark OidMark
//	 * @throws SDPBuildException У�鲻ͨ��ʱ�׳�
//	 */
//	private void checkOidMark(String[] pkValues, String tblName, String pkColName) throws SDPBuildException{
//		if(pkValues != null && pkValues.length != 0){
//			String version = installDiskVO.getProductVersion().getVersion();
//			OIDMarkRule oidMarkRule = OIDMarkRule.getInstance(installDiskVO.getProductVersion().getVersion());
//			if(oidMarkRule == null){
//				MainPlugin.getDefault().logInfo("Can't get oidMarkRule under version: " + version +" .");
//				messageVo.getMessageList().add(
//						new RecordValidateMessage("����", "�汾" + version + "δ����oidMarkRule.xml����������"));
//			}else if(oidMarkRule.getTables().contains(tblName)){
//				String oidMark = oidMarkRule.getDeptOidMarkMap().get(installDiskVO.getDepartment().getName());
//				if(oidMark != null && !(oidMark = oidMark.trim()).equals("")){
//					List<String> oidMarks = Arrays.asList(oidMark.split(",\\s"));
//					for(String pkValue : pkValues){
//						if(pkValue == null || (pkValue = pkValue.trim()).length() < 7 
//								|| !oidMarks.contains(pkValue.substring(5, 7))){
//							pkValue = pkValue.substring(1, pkValue.length()-1);
//							addIllegalPk(tblName, pkColName, oidMarks, pkValue);
//							String msg = new StringBuilder("��").append(tblName).append("������(")
//								.append(pkValue).append(")������OIDMark����").append(oidMarks).append("��").toString();
//							MainPlugin.getDefault().logInfo(msg);
//						}
//					}
//				}else{
//					String msg = "���ţ�" + installDiskVO.getDepartment().getName() + "δ����OIDMark��";
//					MainPlugin.getDefault().logInfo(msg);
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("����", msg));
//				}
//			}
//		}
//	}
	
	private void addIllegalPk(String tableName, String pkName, List<String> oidMarks, String pkValue){
		PkInfo pkInfo = illegalPkMap.get(tableName);
		if(pkInfo == null){
			pkInfo = new PkInfo();
			pkInfo.oidMarks =oidMarks;
			illegalPkMap.put(tableName, pkInfo);
		}
		Map<String, List<String>> pkNameValuesMap = pkInfo.pkNameMapIllegalValues;
		List<String> illegalValues = pkNameValuesMap.get(pkName);
		if(illegalValues == null){
			illegalValues = new ArrayList<String>();
			pkNameValuesMap.put(pkName, illegalValues);
		}
		illegalValues.add(pkValue);
	}
	
	private void handleAllOidVldMsg(){
		for(Map.Entry<String, PkInfo> entry : illegalPkMap.entrySet()){
			PkInfo pkInfo = entry.getValue();
			for(Map.Entry<String, List<String>> pkNameValuesEntry : pkInfo.pkNameMapIllegalValues.entrySet()){
				StringBuilder oidWarnMsg = new StringBuilder("��").append(entry.getKey()).append("������(")
					.append(pkNameValuesEntry.getKey())
					.append(")������OIDMark����").append(pkInfo.oidMarks).append("������ֵ�����ϣ�");
				for(String illegalPk : pkNameValuesEntry.getValue()){
					oidWarnMsg.append(IOUtils.LINE_SEPARATOR).append("       ").append(illegalPk);
				}
				messageVo.getMessageList().add(new RecordValidateMessage("����", oidWarnMsg.toString()));
			}
		}
	}
	
	static class PkInfo{
		
		public PkInfo() {
			pkNameMapIllegalValues = new LinkedHashMap<String, List<String>>();
		}

		List<String> oidMarks;
		
		Map<String, List<String>> pkNameMapIllegalValues;
	}

}
