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
 * 预置脚本导出实现类
 * 
 * 工作方式: 多例 可访问方法: exportProductScript, exportModuleScript, exportScript
 * 
 * @author fanp
 */
public class SqlExporter implements IExport {
	/** 预置脚本类别 */
	private Item item = null;

	/** 预置脚本存储地址 */
	private String sqlRoot = null;

	/** 公共预置脚本映射文件存储地址 */
	private String commonMapRoot = null;

	/** 模块预置脚本映射文件存储地址 */
	private String moduleMapRoot = null;

	/** 主子表拓补关系定义文件存储地址 */
	private String multiTableHierarchyRoot = null;
	
	/** 模块主子表拓补关系定义文件存储地址 */
	private String moduleMultiTableHierarchyRoot;
	
//	private InstallDiskDescriptionVO installDiskVO = null;
	
	private Map<String, PkInfo> illegalPkMap = new LinkedHashMap<String, PkInfo>();

	// /**
	// * 生成脚本时产生的错误信息都存放于此
	// */
	// private List<RecordValidateMessage> errorMsgList = new
	// ArrayList<RecordValidateMessage>();

	/**
	 * 生成预置脚本时产生的消息VO对象
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
	 * 该方法导出指定产品的全部初始化脚本
	 * 
	 * @param
	 * @return
	 */
	public void exportProductScript() {
	}

	/**
	 * 该方法导出指定模块的全部初始化脚本
	 * 
	 * @param
	 * @return
	 */
	public void exportModuleScript() {
	}

	/**
	 * 该方法导出指定预置脚本, 同时返回该脚本的根目录 使用说明: 当以编组方式导出"打印模板"时, 编组字段固定为'vnodecode'
	 * 如果失败，返回null, 此时应访问messageVo得到错误的消息
	 * 
	 * @param con
	 *            SDPConnection当前连接
	 * @return
	 */
	public File exportScript(SDPConnection con) {
		/* 校验预置脚本导出目录 */
		SqlBuildRule rule = new SqlBuildRule(getCommonMapRoot(), getModuleMapRoot(), getMultiTableHierarchyRoot());
		// 得到生成脚本的位置，先删除再创建
		File scriptRoot = rule.getSQLFileByItem(getSqlRoot(), getItem(), ISql._SQLSERVER_);
		if (scriptRoot == null) {
			messageVo.getMessageList().add(
					new RecordValidateMessage("错误", "脚本类型[" + getItem().getItemName() + "]对应的脚本目录配置文件有误，请先执行校验操作！"));
			return null;
		}
		if (scriptRoot.exists()) {
			try {
				FileUtil.forceDelete(scriptRoot);
			} catch (IOException e) {
				messageVo.getMessageList().add(
						new RecordValidateMessage("错误", "脚本类型[" + getItem().getItemName() + "]生成脚本的位置：" + scriptRoot
								+ " 文件创建失败！"));
				MainPlugin.getDefault().logError(e.getMessage(), e);
				return null;
			}
		}
		FileUtil.forceCreate(scriptRoot.getPath(), true);
		/* 确定"SQL分隔符" */
		String sqlDelimiter = "\r\ngo";
		/* 解析表拓补结构及元数据信息 */
		/*---------------------------------------------------------------------------
		 * 效率优化, 减少获取表元数据操作的调用次数(此操作涉及数据库访问)
		 * MainTable mainTable = rule.getMultiTableHierarchyStructure(getItem());
		 --------------------------------------------------------------------------*/
		MainTable mainTable;
		try {
			mainTable = SqlBuildRuleUtil.getMultiTableHierarchyStructure(getItem(), con,  getMultiTableHierarchyRoot(), moduleMultiTableHierarchyRoot);
			//表不存在
			if(!isValidTable(mainTable.getTableMetaData())){
				messageVo.setSuccess(false);
				messageVo.getMessageList().add(new RecordValidateMessage("错误", mainTable.getTableName() + " 表在数据库中不存在或未定义列！"));
				return null;
			}
			if(mainTable.getSubTableSet() == null || mainTable.getSubTableSet().size() == 0){
				messageVo.getMessageList().add(new RecordValidateMessage("提示", "表" + getItem().getItemRule() + "解析为单表结构"));
			} else {
				messageVo.getMessageList().add(new RecordValidateMessage("提示", "表" + getItem().getItemRule() + "解析为主子表结构, 配置文件名称为:"
						+ getItem().getItemRule()+".xml"));
			}
		} catch (SDPBuildException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			messageVo.getMessageList().add(
					new RecordValidateMessage("表" + getItem().getItemRule() + "发生错误", e.getMessage()));
			return null;
		}
		MainPlugin.getDefault().logInfo("表拓补结构解析完成");

		/* 对于"自定义查询"导出项执行特殊处理_二进制数据 */
		if (getItem().getItemName().equalsIgnoreCase("自定义查询")) {
			customerCode(con, scriptRoot, rule);
			return scriptRoot;
		}

		try {
			String itemRule = getItem().getItemRule();
			/* 对于聚簇主键的导出项执行特殊处理 */
			if (itemRule.equalsIgnoreCase("md_module") || itemRule.equalsIgnoreCase("pd_dd")
					|| itemRule.equalsIgnoreCase("md_class") || itemRule.equalsIgnoreCase("md_enumvalue")) {
				customerKeyExport(mainTable, scriptRoot, con, itemRule, sqlDelimiter);
				return scriptRoot;
			}

			/* 判断是否需要以"编组方式"导出 */
			boolean needGrp = getItem().getGrpField() != null && !getItem().getGrpField().equals("");
			if (needGrp) { // 以编组方式导出
				exportByGroup(mainTable, scriptRoot, con, itemRule, sqlDelimiter, rule);
			} else { // 非编组方式导出
				exportByNoGroup(mainTable, scriptRoot, con, itemRule, sqlDelimiter, rule);
			}
			messageVo.setSuccess(true);
		} catch (SDPBuildException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			messageVo.getMessageList().add(
					new RecordValidateMessage("导出" + getItem().getItemRule() + "时产生错误", e.getMessage()));
			return null;
		}
		return scriptRoot;
	}

	/**
	 * 该方法根据打印数据项唯一值列表(print_data_item)返回对应的查询条件
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
	 * 该方法根据一组序号返回指定结果集中的投影，即返回第几条数据
	 * 
	 * @param pk_value_list
	 * @param index_list
	 *            序号值，应为"1","2"的字符串数组
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
	 * 该方法根据一组序号返回指定PK值列表中的投影
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
	 * 该方法为单表结构的唯一主表生成sql脚本文件，并写入到sql_file_path路径中
	 * 
	 * @param tableVo
	 *            表VO
	 * @param sqlFilePath
	 *            输出的文件路径
	 * @param sql_delimiter
	 *            sql分隔符
	 * @param result_set
	 *            数据结果集
	 */
	private void generateSingleTableSqlFile(Table tableVo, String sqlFilePath, String sql_delimiter,
			List<List<String>> result_set) {
		BufferedWriter fileWriter = null;
		try {
			/* 创建sql文件 */
			File sqlFile = new File(sqlFilePath);
			if (sqlFile.exists())
				FileUtils.forceDelete(sqlFile);
			sqlFile.createNewFile();
			fileWriter = FileUtil.getFileWriter(sqlFile);

			SqlExporterUtils.outputResultToWriter(tableVo, fileWriter, sql_delimiter, result_set);
		} catch (IOException e) {
			//TODO: 异常没有进行处理
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}  finally {
			if (fileWriter != null) {
				IOUtils.closeQuietly(fileWriter);
			}
		}
	}

	/**
	 * 该方法为指定表按"主-子"表拓补结构递归生成各级sql脚本文件
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
			/* 获得当前子表的名称、外键列名称、附加导出条件、sql脚本序号 */
			String subTableName = sub_table.getTableName();
			String fkFieldName = sub_table.getForeignKeyColumn();
			String additionalWhereCondition = sub_table.getWhereCondition();
			String sqlNo = sub_table.getSqlNo();

			/* 获得当前子表的元数据 */
			// Table subTableVO = getTable(uds.getDatabaseType(), uds.getUser(),
			// subTableName, con);
			Table subTableVO = mainTable.lookup(subTableName);
			if(!isValidTable(subTableVO)){
				messageVo.getMessageList().add(new RecordValidateMessage("警告", "表" + subTableName + "不存在或未定义列。"));
				return;
			}

			/* 根据构造当前子表的查询条件, 执行数据库查询, 获得结果集 */
			String treatedWhereCondition = getTreatedWhereCondition(fkFieldName, pk_value_list);
			List<List<String>> resultSet = SqlExporterUtils.getTableDataByWhereCondition(subTableVO, treatedWhereCondition,
					additionalWhereCondition, con);
			
			List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(subTableVO, null, resultSet, false);
			String[] pkValueList = v.get(0).toArray(new String[0]);
//			//加入OIDMark校验
//			if(subTableVO.getTablePrimaryKey() != null && subTableVO.getTablePrimaryKey().length > 0){
//				checkOidMark(pkValueList, subTableName, subTableVO.getTablePrimaryKey()[0]);
//			}
			//addOidVldMsg(subTableName);

			/* 生成当前子表的sql脚本文件 */
			String sqlFilePath = sql_root + File.separator + sqlNo + ".sql";
			generateSingleTableSqlFile(subTableVO, sqlFilePath, delimiter, resultSet);

			/* 递归生成当前子表的全部下级孙表的sql脚本文件 */
			List<SubTable> childrenTableSet = sub_table.getSubTableSet();
			if (childrenTableSet == null || childrenTableSet.size() == 0) {
				/* 当前子表已经是末级子表, 递归返回 */
				return;
			} else {
				/* 当前子表非末级子表, 继续递归生成其下级孙表的sql脚本文件 */
				for (SubTable childTable : childrenTableSet) {
					generateMultiTableSqlFile(childTable, pkValueList, sql_root, delimiter, uds, con, mainTable);
				}// for
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}
	}


	/**
	 * 该方法返回特殊处理(以100为单位编组)后的子表查询条件 说明:
	 * 因Oracle对"select %foreign key field% in (%pk list%)"类型sql语句有"列表总长<1000"限制,
	 * 采取以下策略 生成查询条件:
	 * ------------------------------------------------------------
	 * ---------------------------------- 每组长度为'100',
	 * (1)不满一组(即数量<100)的PK列表直接生成"%foreign key field% in (%pk list%)"类型查询条 件;
	 * (2)满一组(即数量>=100)的PK列表分组生成"%foreign key field% in (%pk list%)"类型单元查询条件后再按
	 * 'or'合并成最终查询条件。
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

		MainPlugin.getDefault().logInfo("分组处理前的PK列值列表数: " + field_value_list.length);
		String whereCondition = null;

		if (field_value_list.length >= 100) {
			/* 满一组 */
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

				/* 准备下一组 */
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

			/* 删除结尾多余的' or' */
			sb.delete(sb.length() - 3, sb.length());
			whereCondition = sb.toString();
		} else {
			/* 不满一组 */
			whereCondition = "";
			for (int j = 0; j < field_value_list.length; j++) {
				// whereCondition += ("'" + field_value_list[j] + "',");
				whereCondition += (field_value_list[j] + ",");
			}
			whereCondition = whereCondition.substring(0, whereCondition.length() - 1);
			whereCondition = " " + field_name + " in (" + whereCondition + ")";
		}
		MainPlugin.getDefault().logInfo("分组处理后的查询条件: " + whereCondition);

		return whereCondition;
	}

	/**
	 * 返回选择的主键列、编组目录列的值列表
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
		
		/* 获得PK字段、PK字段列号 */
		if(pkExist){
			tablePrimaryKeyFieldName = table_vo.getTablePrimaryKey()[0];
			MainPlugin.getDefault().logInfo("PK列: " + tablePrimaryKeyFieldName);
			pkFieldIndex = getPKFieldIndex(table_vo);
			MainPlugin.getDefault().logInfo("PK列序号: " + pkFieldIndex);
		}
		
		/* 获得编组字段、编组字段列号 */
		if (need_group) {
			tableGroupFieldName = item_vo.getGrpField();
			MainPlugin.getDefault().logInfo("编组列: " + tableGroupFieldName);
			grpFieldIndex = getGroupFieldIndex(table_vo, item_vo);
			MainPlugin.getDefault().logInfo("编组列序号: " + grpFieldIndex);
		}
		
		/* 获得PK列值列表、编组列值列表 */
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
		MainPlugin.getDefault().logInfo("PK列值列表数量: " + vecPKFieldList.size());
		if (need_group)
			MainPlugin.getDefault().logInfo("编组列值列表数量:" + vecGroupFieldList.size());
		

		result.add(vecPKFieldList);
		result.add(vecGroupFieldList);

		return result;
	}

	/**
	 * 该方法返回指定表的唯一主键字段的序号
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
	 * 该方法返回指定表的编组字段的序号
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
	 * 该方法检查指定表中主键定义是否支持脚本导出
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
	 * 该方法返回主表导出条件
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
	 * 自定义查询的脚本生成
	 * 
	 * @param con
	 * @param scriptRoot
	 * @param rule
	 */
	private void customerCode(SDPConnection con, File scriptRoot, SqlBuildRule rule) {
		boolean bFmdSuccess = true;
		boolean bQmdSuccess = true;
		if (getItem().getItemName().equalsIgnoreCase("自定义查询")) {
			if (rule.isFmdNeeded() != null) {
				/* 导出"格式对象目录" */
				MainPlugin.getDefault().logInfo("导出类型: FMD");
				String fid = rule.isFmdNeeded();
//				if (ExportUtil.onExportBinary(fid, "", true, scriptRoot.getAbsolutePath(), con.getPhysicalConnection())) {
//					MainPlugin.getDefault().logInfo("FMD 导出成功");
//				} else {
//					MainPlugin.getDefault().logInfo("FMD 导出失败");
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("导出类型:FMD产生错误", "自定义查询:" + getItem().getItemRule() + " 发生错误"));
//					bFmdSuccess = false;
//				}
			}

			if (rule.isQmdNeeded() != null) {
				/* 导出"查询对象目录" */
				MainPlugin.getDefault().logInfo("导出类型: QMD");
				String qid = rule.isQmdNeeded();
//				if (ExportUtil
//						.onExportBinary(qid, "", false, scriptRoot.getAbsolutePath(), con.getPhysicalConnection()))
//					MainPlugin.getDefault().logInfo("QMD 导出成功");
//				else {
//					MainPlugin.getDefault().logInfo("QMD 导出失败");
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("导出类型:QMD产生错误", "自定义查询:" + getItem().getItemRule() + " 发生错误"));
//					bQmdSuccess = false;
//				}
			}
			
			messageVo.setSuccess(bFmdSuccess && bQmdSuccess);
			// return scriptRoot;
		}
	}

	/**
	 * 对于聚簇主键的导出项执行特殊处理
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
			/* 需要编组 */
			MainPlugin.getDefault().logInfo("以编组方式导出预置脚本...");
			throw new SDPBuildException("表:" + getItem().getItemRule() + " 不支持以分组方式导出");
			// ...
		} else {
			/* 不需要编组 */
			MainPlugin.getDefault().logInfo("以非编组方式导出预置脚本...");
			/* 取得主表的元数据, 校验主键字段 */
			Table tableVO = mainTable.lookup(itemRule);
			/* 根据主表查询条件执行数据库查询, 获得结果集 */
			String whereCondition = getWhereCond(getItem());
			List<List<String>> resultSet;
			try {
				resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
				
				Map<String, List<String>> pksMapValues = getPkValues(resultSet, tableVO);
//				for(Map.Entry<String, List<String>> entry : pksMapValues.entrySet()){
//					checkOidMark(entry.getValue().toArray(new String[0]), tableVO.getTableName(), entry.getKey());
//				}
				
				/* 生成sql脚本文件 */
				if (!mainTable.isAssociatedTables()) {
					/* 单表结构 */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
					messageVo.setSuccess(true);
				} else {
					/* 主子表结构 */
					throw new SDPBuildException("表:" + getItem().getItemRule() + " 不支持主子表的数据导出");
				}
			} catch (SQLException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
				throw new SDPBuildException("表:" + getItem().getItemRule() + " 根据指定where条件执行数据库查询并结果集发生错误！");
			}

		}
	}

	/**
	 * 以分组方式导出脚本
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
		/* 需要编组 */
		MainPlugin.getDefault().logInfo("以编组方式导出预置脚本...");

		Table tableVO = mainTable.lookup(itemRule);
		try {
			/* 对于主表主键为聚簇主键(Clustered Primary Key)的不在此处处理, 需要其他方法特殊处理 */
			if (mainTable.getSubTableSet() != null && mainTable.getSubTableSet().size() > 0) {
				checkPrimaryKey(tableVO);
			}
		} catch (SDPBuildException sdp_build_exception) {
			throw sdp_build_exception;
		}

		/* 根据主表查询条件执行数据库查询, 获得结果集 */
		String whereCondition = getWhereCond(getItem());
		List<List<String>> resultSet;
		try {
			resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
		} catch (SQLException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			throw new SDPBuildException(itemRule + " 根据指定where条件执行数据库查询并结果集发生错误！");
		}

		/* 按照主表导出条件取得PK列值列表、唯一编组列值列表 */
		List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(tableVO, getItem(), resultSet, true);
		String[] pkValueList = v.get(0).toArray(new String[0]);
		String[] grpValueList = v.get(1).toArray(new String[0]);
		String[] uniqueGrpValueList = CommonUtil.getUniqueValueList(grpValueList);
		
		//加入OIDMark校验
//		if(tableVO.getTablePrimaryKey() != null && tableVO.getTablePrimaryKey().length > 0){
//			checkOidMark(pkValueList, tableVO.getTableName(), tableVO.getTablePrimaryKey()[0]);
//		}
		//addOidVldMsg(tableVO.getTableName());

		if (uniqueGrpValueList != null && uniqueGrpValueList.length > 0) {
			for (int m = 0; m < uniqueGrpValueList.length; m++) {
				/* 取得当前唯一编组值 */
				String grpValue = uniqueGrpValueList[m];

				/* 对当前唯一编组值分割原始PK值列表及结果集, 得到对应的"PK值列表投影"和"结果集投影" */
				String[] indexList = CommonUtil.getValueIndexListBySpecifiedValue(grpValueList, grpValue);
				String[] pkValueListAfterGroup = getPKValueListAfterGroup(pkValueList, indexList);
				List<List<String>> resultSetAfterGroup = getResultSetAfterGroup(resultSet, indexList);

				/* 创建当前组字段值的编组目录 */
				String groupDirName = tableVO.getTableName() + "_" + CommonUtil.replace(grpValue, "'", "");
				FileUtil.forceCreate(scriptRoot.getAbsolutePath() + File.separator + groupDirName, true);

				/* 生成当前编组字段值的sql脚本文件 */
				if (!mainTable.isAssociatedTables()) {
					/* 单表结构 */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
							+ sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSetAfterGroup);
					continue;
				} else {
					/* 主子表结构 */
					String sqlFileName = mainTable.getSqlNo() + ".sql";
					String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
							+ sqlFileName;
					generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSetAfterGroup);

					/*
					 * 如当前"主表"是"pub_print_template",
					 * 则需以"特殊关联表"方式增加导出"pub_print_dataitem"子表,
					 * 该子表不遵守"主-子表外键关联关系"导出规则, 其导出条件仅由编组值决定
					 */
					if (tableVO.getTableName().equalsIgnoreCase("pub_print_template")) {
						/* 构造SubTable */
						SubTable print_data_item_subtable = new SubTable();
						print_data_item_subtable.setForeignKeyColumn(null);
						print_data_item_subtable.setSqlNo("006");
						print_data_item_subtable.setSubTableSet(null);
						print_data_item_subtable.setTableName("pub_print_dataitem");
						print_data_item_subtable.setWhereCondition(null);

						/* 获得元数据 */
						Table print_data_item_tablevo;
						try {
							print_data_item_tablevo = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(),
									print_data_item_subtable.getTableName(), con);
						} catch (SQLException e) {
							MainPlugin.getDefault().logError(e.getMessage(), e);
							throw new SDPBuildException(itemRule + " 获得元数据信息时发生错误！", e);
						}
						if(isValidTable(print_data_item_tablevo)){
							/* 根据打印数据项唯一值列表(print_data_item)求出对应的查询条件 */
							String print_data_item_wherecondition = getPrintDataItemWhereCondition(new String[] { grpValue });

							/* 执行查询得到结果集 */
							String print_data_item_additionalwherecondition = print_data_item_subtable.getWhereCondition();
							List<List<String>> print_data_item_resultset;
							try {
								print_data_item_resultset = SqlExporterUtils.getTableDataByWhereCondition(print_data_item_tablevo,
										print_data_item_wherecondition, print_data_item_additionalwherecondition, con);
							} catch (SQLException e) {
								MainPlugin.getDefault().logError(e.getMessage(), e);
								throw new SDPBuildException(itemRule + "打印数据项唯一值列表条件执行数据库查询并结果集发生错误！", e);
							}
							
//							//校验pub_print_dataitem表oidMark。
//							List<List<String>> printItemPkGrps = getPkFiledValueListAndGroupFieldValueList(print_data_item_tablevo, null, print_data_item_resultset, false);
//							String[] pkValues = printItemPkGrps.get(0).toArray(new String[0]);
//							if(print_data_item_tablevo.getTablePrimaryKey() != null && print_data_item_tablevo.getTablePrimaryKey().length > 0){
//								checkOidMark(pkValues, print_data_item_tablevo.getTableName(), print_data_item_tablevo.getTablePrimaryKey()[0]);
//							}

							/* 得到脚本文件全路径 */
							sqlFileName = print_data_item_subtable.getSqlNo() + ".sql";
							sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + groupDirName + File.separator
									+ sqlFileName;

							/* 增加生成"print_data_item"子表sql脚本文件 */
							generateSingleTableSqlFile(print_data_item_tablevo, sqlFilePath, sqlDelimiter,
									print_data_item_resultset);
						}
					}

					/* 递归生成"主表"之"全部下级子表"的sql脚本文件 */
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
	 * 非分组方式导出脚本
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
		/* 不需要编组 */
		MainPlugin.getDefault().logInfo("以非编组方式导出预置脚本...");

		/* 取得主表的元数据, 主键聚簇校验 */
		// Table tableVO = getTable(uds.getDatabaseType(), uds.getUser(),
		// getItem().getItemRule(), con);
		Table tableVO = mainTable.lookup(itemRule);
		try {
			if (mainTable.getSubTableSet() != null && mainTable.getSubTableSet().size() > 0) {
				checkPrimaryKey(tableVO);
			}
		} catch (SDPBuildException sdp_build_exception) {
			/* 对于主表主键为聚簇主键(Clustered Primary Key)的不在此处处理, 需要其他方法特殊处理 */
			throw sdp_build_exception;
		}

		/* 根据主表查询条件执行数据库查询, 获得结果集 */
		String whereCondition = getWhereCond(getItem());
		List<List<String>> resultSet;
		try {
			resultSet = SqlExporterUtils.getTableDataByWhereCondition(tableVO, whereCondition, null, con);
		} catch (SQLException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			throw new SDPBuildException(itemRule + " 根据指定where条件执行数据库查询并结果集发生错误！");
		}
		
		List<List<String>> v = getPkFiledValueListAndGroupFieldValueList(tableVO, null, resultSet, false);
		String[] pkValueList = v.get(0).toArray(new String[0]);
		
//		//加入OIDMark校验
//		if(tableVO.getTablePrimaryKey() != null && tableVO.getTablePrimaryKey().length > 0){
//			checkOidMark(pkValueList, tableVO.getTableName(), tableVO.getTablePrimaryKey()[0]);
//		}

		/* 生成sql脚本文件 */
		if (!mainTable.isAssociatedTables()) {
			/* 单表结构 */
			String sqlFileName = mainTable.getSqlNo() + ".sql";
			String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
			generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);
		} else {
			/* 主子表结构 */
			String sqlFileName = mainTable.getSqlNo() + ".sql";
			String sqlFilePath = scriptRoot.getAbsolutePath() + File.separator + sqlFileName;
			generateSingleTableSqlFile(tableVO, sqlFilePath, sqlDelimiter, resultSet);

			/* 递归生成"主表"之"全部下级子表"的sql脚本文件 */
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
//	 * 校验主键的OidMark。
//	 * 
//	 * @param pkValues 主键值集
//	 * @param oidMark OidMark
//	 * @throws SDPBuildException 校验不通过时抛出
//	 */
//	private void checkOidMark(String[] pkValues, String tblName, String pkColName) throws SDPBuildException{
//		if(pkValues != null && pkValues.length != 0){
//			String version = installDiskVO.getProductVersion().getVersion();
//			OIDMarkRule oidMarkRule = OIDMarkRule.getInstance(installDiskVO.getProductVersion().getVersion());
//			if(oidMarkRule == null){
//				MainPlugin.getDefault().logInfo("Can't get oidMarkRule under version: " + version +" .");
//				messageVo.getMessageList().add(
//						new RecordValidateMessage("警告", "版本" + version + "未配置oidMarkRule.xml或配置有误。"));
//			}else if(oidMarkRule.getTables().contains(tblName)){
//				String oidMark = oidMarkRule.getDeptOidMarkMap().get(installDiskVO.getDepartment().getName());
//				if(oidMark != null && !(oidMark = oidMark.trim()).equals("")){
//					List<String> oidMarks = Arrays.asList(oidMark.split(",\\s"));
//					for(String pkValue : pkValues){
//						if(pkValue == null || (pkValue = pkValue.trim()).length() < 7 
//								|| !oidMarks.contains(pkValue.substring(5, 7))){
//							pkValue = pkValue.substring(1, pkValue.length()-1);
//							addIllegalPk(tblName, pkColName, oidMarks, pkValue);
//							String msg = new StringBuilder("表").append(tblName).append("的主键(")
//								.append(pkValue).append(")不符合OIDMark规则").append(oidMarks).append("。").toString();
//							MainPlugin.getDefault().logInfo(msg);
//						}
//					}
//				}else{
//					String msg = "部门：" + installDiskVO.getDepartment().getName() + "未配置OIDMark。";
//					MainPlugin.getDefault().logInfo(msg);
//					messageVo.getMessageList().add(
//							new RecordValidateMessage("警告", msg));
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
				StringBuilder oidWarnMsg = new StringBuilder("表").append(entry.getKey()).append("的主键(")
					.append(pkNameValuesEntry.getKey())
					.append(")不符合OIDMark规则").append(pkInfo.oidMarks).append("。以下值不符合：");
				for(String illegalPk : pkNameValuesEntry.getValue()){
					oidWarnMsg.append(IOUtils.LINE_SEPARATOR).append("       ").append(illegalPk);
				}
				messageVo.getMessageList().add(new RecordValidateMessage("警告", oidWarnMsg.toString()));
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
