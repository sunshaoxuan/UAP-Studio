package nc.uap.lfw.build.exception;

import java.util.Hashtable;

import nc.uap.lfw.build.pub.util.pdm.itf.IParse;

public class SDPSystemMessage {
	private static Hashtable<String, String> htMsg = null;

	/* 系统消息_IO文件系统操作 */
	public static final String SDP_BUILD_0000 = "SDP_BUILD_0000";

	/* 系统消息_ClearCase操作 */
	public static final String SDP_BUILD_1000 = "SDP_BUILD_1000";
	public static final String SDP_BUILD_1001 = "SDP_BUILD_1001";
	public static final String SDP_BUILD_1002 = "SDP_BUILD_1002";
	public static final String SDP_BUILD_1003 = "SDP_BUILD_1003";
	public static final String SDP_BUILD_1004 = "SDP_BUILD_1004";
	public static final String SDP_BUILD_1005 = "SDP_BUILD_1005";
	public static final String SDP_BUILD_1006 = "SDP_BUILD_1006";
	public static final String SDP_BUILD_1007 = "SDP_BUILD_1007";
	public static final String SDP_BUILD_1008 = "SDP_BUILD_1008";
	public static final String SDP_BUILD_1009 = "SDP_BUILD_1009";

	/* 系统消息_PDM解析 */
	public static final String SDP_BUILD_2000 = "SDP_BUILD_2000";
	public static final String SDP_BUILD_2001 = "SDP_BUILD_2001";
	public static final String SDP_BUILD_2002 = "SDP_BUILD_2002";
	public static final String SDP_BUILD_2003 = "SDP_BUILD_2003";
	public static final String SDP_BUILD_2004 = "SDP_BUILD_2004";
	public static final String SDP_BUILD_2005 = "SDP_BUILD_2005";
	public static final String SDP_BUILD_2006 = "SDP_BUILD_2006";
	public static final String SDP_BUILD_2007 = "SDP_BUILD_2007";
	public static final String SDP_BUILD_2008 = "SDP_BUILD_2008";
	public static final String SDP_BUILD_2009 = "SDP_BUILD_2009";
	public static final String SDP_BUILD_2010 = "SDP_BUILD_2010";
	public static final String SDP_BUILD_2011 = "SDP_BUILD_2011";
	public static final String SDP_BUILD_2012 = "SDP_BUILD_2012";

	/* 系统消息_建库脚本生成 */
	public static final String SDP_BUILD_3000 = "SDP_BUILD_3000";
	public static final String SDP_BUILD_3001 = "SDP_BUILD_3001";

	/* 系统消息_预置脚本生成 */
	public static final String SDP_BUILD_4000 = "SDP_BUILD_4000";
	public static final String SDP_BUILD_4001 = "SDP_BUILD_4001";
	public static final String SDP_BUILD_4002 = "SDP_BUILD_4002";
	public static final String SDP_BUILD_4003 = "SDP_BUILD_4003";
	public static final String SDP_BUILD_4004 = "SDP_BUILD_4004";
	public static final String SDP_BUILD_4005 = "SDP_BUILD_4005";
	public static final String SDP_BUILD_4006 = "SDP_BUILD_4006";
	public static final String SDP_BUILD_4007 = "SDP_BUILD_4007";
	public static final String SDP_BUILD_4008 = "SDP_BUILD_4008";
	public static final String SDP_BUILD_4009 = "SDP_BUILD_4009";
	public static final String SDP_BUILD_4010 = "SDP_BUILD_4010";
	public static final String SDP_BUILD_4011 = "SDP_BUILD_4011";
	public static final String SDP_BUILD_4012 = "SDP_BUILD_4012";
	public static final String SDP_BUILD_4013 = "SDP_BUILD_4013";
	public static final String SDP_BUILD_4014 = "SDP_BUILD_4014";
	public static final String SDP_BUILD_4015 = "SDP_BUILD_4015";

	/* 系统消息_基线比较 */
	public static final String SDP_BUILD_5000 = "SDP_BUILD_5000";
	public static final String SDP_BUILD_5001 = "SDP_BUILD_5001";
	public static final String SDP_BUILD_5002 = "SDP_BUILD_5002";
	public static final String SDP_BUILD_5003 = "SDP_BUILD_5003";

	/* 系统消息_执行构造命令 */
	public static final String SDP_BUILD_6000 = "SDP_BUILD_6000";
	public static final String SDP_BUILD_6001 = "SDP_BUILD_6001";

	/* 系统消息_ClearQuest操作 */
	public static final String SDP_BUILD_7000 = "SDP_BUILD_7000";

	static {
		/**
		 * SDP构造系统消息表 Key: Msg Code, 格式为: "SDP_%domain%_%msg code%" Value: Msg
		 * Content, 格式为:
		 * "MSG_SDP_%domain%_%sub_domain%_%action type%::%msg content%"
		 */
		htMsg = new Hashtable<String, String>();
		htMsg.put(SDP_BUILD_0000, "MSG_SDP_BUILD_IO_BATCHDELETE_ERROR::批量删除目录IO错误");

		htMsg.put(SDP_BUILD_1000, "MSG_SDP_BUILD_CC_SNAPSHOOT_VIEW_DIR_NOT_EXIST::静态视图根目录不存在");
		htMsg.put(SDP_BUILD_1001, "MSG_SDP_BUILD_CC_UNKNOWN_VIEW_TYPE::遇到未知视图类型");
		htMsg.put(SDP_BUILD_1002, "MSG_SDP_BUILD_CC_GET_PREDEFINED_CONSTANT::获取预定义CC常量错误");
		htMsg.put(SDP_BUILD_1003, "MSG_SDP_BUILD_CC_DYNAMICAL_VIEW_DIR_NOT_FOUND_WITHIN_M::动态视图根目录(M盘)不存在");
		htMsg.put(SDP_BUILD_1004, "MSG_SDP_BUILD_CC_LACK_OF_VOB_DIR::动态视图根目录(M盘)下缺少VOB目录");
		htMsg.put(SDP_BUILD_1005, "MSG_SDP_BUILD_CC_LACK_OF_COMPONENT_DIR::动态视图VOB目录下缺少构件目录");
		htMsg.put(SDP_BUILD_1006, "MSG_SDP_BUILD_CC_LACK_OF_MODULE_DIR::动态视图资源构件目录下缺少模块目录");
		htMsg.put(SDP_BUILD_1007, "MSG_SDP_BUILD_CC_LACK_OF_PDM_DIR::动态视图资源构件目录下缺少PDM目录");
		htMsg.put(SDP_BUILD_1008, "MSG_SDP_BUILD_CC_LACK_OF_SCRIPT_DIR::动态视图资源构件目录下缺少SCRIPT目录");
		htMsg.put(SDP_BUILD_1009, "MSG_SDP_BUILD_CC_LACK_OF_INIT_DIR::动态视图资源构件目录下缺少INIT目录");

		htMsg.put(SDP_BUILD_2000, "MSG_SDP_BUILD_PDM_VALIDATION_FILE_NOT_EXIST::PDM文件检验失败");
		htMsg.put(SDP_BUILD_2001, "MSG_SDP_BUILD_PDM_PARSE_STACK::PDM解析栈中压入的数据库对象类型错误");
		htMsg.put(SDP_BUILD_2002, "MSG_SDP_BUILD_PDM_VALIDATION_WRONG_VERSION::PowerDesigner版本不对, 目前支持的版本为" + IParse.PDM_VERSION);
		htMsg.put(SDP_BUILD_2003, "MSG_SDP_BUILD_PDM_VALIDATION_UNSUPPORTED_DBTYPE::PDM文件中当前数据库类型不符, 目前支持的数据库类型为" + IParse.PDM_DB_TYPE);
		htMsg.put(SDP_BUILD_2004, "MSG_SDP_BUILD_PDM_VALIDATION_MISMATCH_FILETYPE::PDM文件格式不对, 目前支持的文件类型为" + IParse.PDM_FILE_TYPE);
		htMsg.put(SDP_BUILD_2005, "MSG_SDP_BUILD_PDM_IO_READ::读入外部PDM文件错误");
		htMsg.put(SDP_BUILD_2006, "MSG_SDP_BUILD_PDM_INITIALIZE_DOMPARSER::初始化DOM Parser错误");
		htMsg.put(SDP_BUILD_2007, "MSG_SDP_BUILD_PDM_VALIDATION_WRONG_NUMBER::PDM文件中包含非法数量的PDM");
		htMsg.put(SDP_BUILD_2008, "MSG_SDP_BUILD_PDM_PARSE_FAIL::PDM解析错误");
		htMsg.put(SDP_BUILD_2009, "MSG_SDP_BUILD_PDM_VALIDATION_PKCONSTRAINT_SIZE::PDM中主键约束名称超长(SQLSERVER:30位, ORACLE:30位, DB2:18位)");
		htMsg.put(SDP_BUILD_2010, "MSG_SDP_BUILD_PDM_VALIDATION_UNKNOWN_DATATYPE::PDM中表列中使用了未知数据类型");
		htMsg.put(SDP_BUILD_2011, "MSG_SDP_BUILD_PDM_VALIDATION_FKCONSTRAINT_SIZE::PDM中外键约束名称超长(SQLSERVER:30位, ORACLE:30位, DB2:18位)");
		htMsg.put(SDP_BUILD_2012, "MSG_SDP_BUILD_PDM_VALIDATION_INDEX_SIZE::PDM中索引名称超长(SQLSERVER:30位, ORACLE:30位, DB2:18位)");

		htMsg.put(SDP_BUILD_3000, "MSG_SDP_BUILD_DBCREATESCRIPT_SQLBUILDER::脚本创建者工厂错误");
		htMsg.put(SDP_BUILD_3001, "MSG_SDP_BUILD_DBCREATESCRIPT_TEMPLATEPARSE_ERROR::模板反射生成建库脚本错误");

		htMsg.put(SDP_BUILD_4000, "MSG_SDP_BUILD_DBRECORDSCRIPT_DS_PARSE::预置脚本所在外部数据源(DATASOURCE.XML)解析错误");
		htMsg.put(SDP_BUILD_4001, "MSG_SDP_BUILD_DBRECORDSCRIPT_ITEMS_PARSE::预置脚本配置文件(ITEMS.XML)解析错误");
		htMsg.put(SDP_BUILD_4002, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_AVAILABLE_DATABASE_CONNECTION::获取预置脚本类别所在外部数据源之数据库连接错误");
		htMsg.put(SDP_BUILD_4003, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_TIMEOUT::预置脚本类别所在外部数据源之数据库连接超时");
		htMsg.put(SDP_BUILD_4004, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_INITIAL_STATE_ERROR::预置脚本类别所在外部数据源之数据库连接初始状态错误");
		htMsg.put(SDP_BUILD_4005, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_CLOSE_ERROR::预置脚本类别所在外部数据源之数据库连接关闭错误");
		htMsg.put(SDP_BUILD_4006, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_COMMON_MAP_ERROR::加载公共预置脚本映射文件错误");
		htMsg.put(SDP_BUILD_4007, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_MODULE_MAP_ERROR::加载模块预置脚本映射文件错误");
		htMsg.put(SDP_BUILD_4008, "MSG_SDP_BUILD_DBRECORDSCRIPT_GLOBAL_DATASOURCE_CONFIG_FILE_NOT_EXIST::全局外部数据源定义文件不存在");
		htMsg.put(SDP_BUILD_4009, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_SCRIPT_ITEMS_CHOOSED::未选择要导出的预置脚本");
		htMsg.put(SDP_BUILD_4010, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_PRIMARYKEY_DEFINED::要导出预置脚本的表未定义主键");
		htMsg.put(SDP_BUILD_4011, "MSG_SDP_BUILD_DBRECORDSCRIPT_NUMBER_OF_PRIMARYKEY_GREAT_THAN_ONE::要导出预置脚本的表中主键所在字段个数大于1");
		htMsg.put(SDP_BUILD_4012, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_MODULE_QUERYENGINE_PROP_ERROR::加载模块自定义查询属性文件错误");
		htMsg.put(SDP_BUILD_4013, "MSG_SDP_BUILD_DBRECORDSCRIPT_GENERATE_INIT_SCRIPT_ERROR::生成预置脚本错误");
		htMsg.put(SDP_BUILD_4014, "MSG_SDP_BUILD_DBRECORDSCRIPT_GET_TABLE_METADATA_ERROR::获取预置脚本所在表元数据错误");
		htMsg.put(SDP_BUILD_4015, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_SCRIPT_ITEMS_DIR_MAPPING::要导出的预置脚本项没有设置目录对照相，请检查相应的.map文件");

		htMsg.put(SDP_BUILD_5000, "MSG_SDP_BUILD_BASELINECOMPARE_ERROR::基线比较错误");
		htMsg.put(SDP_BUILD_5001, "MSG_SDP_BUILD_BASELINECOMPARE_GET_LASTBASELINE_FROM_BUILDREPORT_ERROR::由上次成功构造的构造报告获取构造基线错误");
		htMsg.put(SDP_BUILD_5002, "MSG_SDP_BUILD_MAKEBASELINE_ERROR::标识本次构造基线错误");
		htMsg.put(SDP_BUILD_5003, "MSG_SDP_BUILD_PREPARE_ECO_FILE_ERROR::准备ECO变更单文件错误");

		htMsg.put(SDP_BUILD_6000, "MSG_SDP_BUILD_EXEC_BUILD_COMMAND_ERROR::执行构造命令错误");
		htMsg.put(SDP_BUILD_6001, "MSG_SDP_BUILD_NO_AVAILABLE_BUILD_COMMAND_ERROR::未找到构造命令错误");
		htMsg.put(SDP_BUILD_7000, "MSG_SDP_BUILD_LOOKUP_UCM_UTILITY_ACTIVITY_ERROR::查询可用的UCM Utility Activity错误");
	}

	public static String getMsg(String msg_code) {
		if (htMsg.containsKey(msg_code)) {
			String message = (String) htMsg.get(msg_code);
			return message.substring(message.indexOf("::") + 2);
		} else {
			return "";
		}
	}
}
