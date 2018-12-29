package nc.uap.lfw.build.exception;

import java.util.Hashtable;

import nc.uap.lfw.build.pub.util.pdm.itf.IParse;

public class SDPSystemMessage {
	private static Hashtable<String, String> htMsg = null;

	/* ϵͳ��Ϣ_IO�ļ�ϵͳ���� */
	public static final String SDP_BUILD_0000 = "SDP_BUILD_0000";

	/* ϵͳ��Ϣ_ClearCase���� */
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

	/* ϵͳ��Ϣ_PDM���� */
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

	/* ϵͳ��Ϣ_����ű����� */
	public static final String SDP_BUILD_3000 = "SDP_BUILD_3000";
	public static final String SDP_BUILD_3001 = "SDP_BUILD_3001";

	/* ϵͳ��Ϣ_Ԥ�ýű����� */
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

	/* ϵͳ��Ϣ_���߱Ƚ� */
	public static final String SDP_BUILD_5000 = "SDP_BUILD_5000";
	public static final String SDP_BUILD_5001 = "SDP_BUILD_5001";
	public static final String SDP_BUILD_5002 = "SDP_BUILD_5002";
	public static final String SDP_BUILD_5003 = "SDP_BUILD_5003";

	/* ϵͳ��Ϣ_ִ�й������� */
	public static final String SDP_BUILD_6000 = "SDP_BUILD_6000";
	public static final String SDP_BUILD_6001 = "SDP_BUILD_6001";

	/* ϵͳ��Ϣ_ClearQuest���� */
	public static final String SDP_BUILD_7000 = "SDP_BUILD_7000";

	static {
		/**
		 * SDP����ϵͳ��Ϣ�� Key: Msg Code, ��ʽΪ: "SDP_%domain%_%msg code%" Value: Msg
		 * Content, ��ʽΪ:
		 * "MSG_SDP_%domain%_%sub_domain%_%action type%::%msg content%"
		 */
		htMsg = new Hashtable<String, String>();
		htMsg.put(SDP_BUILD_0000, "MSG_SDP_BUILD_IO_BATCHDELETE_ERROR::����ɾ��Ŀ¼IO����");

		htMsg.put(SDP_BUILD_1000, "MSG_SDP_BUILD_CC_SNAPSHOOT_VIEW_DIR_NOT_EXIST::��̬��ͼ��Ŀ¼������");
		htMsg.put(SDP_BUILD_1001, "MSG_SDP_BUILD_CC_UNKNOWN_VIEW_TYPE::����δ֪��ͼ����");
		htMsg.put(SDP_BUILD_1002, "MSG_SDP_BUILD_CC_GET_PREDEFINED_CONSTANT::��ȡԤ����CC��������");
		htMsg.put(SDP_BUILD_1003, "MSG_SDP_BUILD_CC_DYNAMICAL_VIEW_DIR_NOT_FOUND_WITHIN_M::��̬��ͼ��Ŀ¼(M��)������");
		htMsg.put(SDP_BUILD_1004, "MSG_SDP_BUILD_CC_LACK_OF_VOB_DIR::��̬��ͼ��Ŀ¼(M��)��ȱ��VOBĿ¼");
		htMsg.put(SDP_BUILD_1005, "MSG_SDP_BUILD_CC_LACK_OF_COMPONENT_DIR::��̬��ͼVOBĿ¼��ȱ�ٹ���Ŀ¼");
		htMsg.put(SDP_BUILD_1006, "MSG_SDP_BUILD_CC_LACK_OF_MODULE_DIR::��̬��ͼ��Դ����Ŀ¼��ȱ��ģ��Ŀ¼");
		htMsg.put(SDP_BUILD_1007, "MSG_SDP_BUILD_CC_LACK_OF_PDM_DIR::��̬��ͼ��Դ����Ŀ¼��ȱ��PDMĿ¼");
		htMsg.put(SDP_BUILD_1008, "MSG_SDP_BUILD_CC_LACK_OF_SCRIPT_DIR::��̬��ͼ��Դ����Ŀ¼��ȱ��SCRIPTĿ¼");
		htMsg.put(SDP_BUILD_1009, "MSG_SDP_BUILD_CC_LACK_OF_INIT_DIR::��̬��ͼ��Դ����Ŀ¼��ȱ��INITĿ¼");

		htMsg.put(SDP_BUILD_2000, "MSG_SDP_BUILD_PDM_VALIDATION_FILE_NOT_EXIST::PDM�ļ�����ʧ��");
		htMsg.put(SDP_BUILD_2001, "MSG_SDP_BUILD_PDM_PARSE_STACK::PDM����ջ��ѹ������ݿ�������ʹ���");
		htMsg.put(SDP_BUILD_2002, "MSG_SDP_BUILD_PDM_VALIDATION_WRONG_VERSION::PowerDesigner�汾����, Ŀǰ֧�ֵİ汾Ϊ" + IParse.PDM_VERSION);
		htMsg.put(SDP_BUILD_2003, "MSG_SDP_BUILD_PDM_VALIDATION_UNSUPPORTED_DBTYPE::PDM�ļ��е�ǰ���ݿ����Ͳ���, Ŀǰ֧�ֵ����ݿ�����Ϊ" + IParse.PDM_DB_TYPE);
		htMsg.put(SDP_BUILD_2004, "MSG_SDP_BUILD_PDM_VALIDATION_MISMATCH_FILETYPE::PDM�ļ���ʽ����, Ŀǰ֧�ֵ��ļ�����Ϊ" + IParse.PDM_FILE_TYPE);
		htMsg.put(SDP_BUILD_2005, "MSG_SDP_BUILD_PDM_IO_READ::�����ⲿPDM�ļ�����");
		htMsg.put(SDP_BUILD_2006, "MSG_SDP_BUILD_PDM_INITIALIZE_DOMPARSER::��ʼ��DOM Parser����");
		htMsg.put(SDP_BUILD_2007, "MSG_SDP_BUILD_PDM_VALIDATION_WRONG_NUMBER::PDM�ļ��а����Ƿ�������PDM");
		htMsg.put(SDP_BUILD_2008, "MSG_SDP_BUILD_PDM_PARSE_FAIL::PDM��������");
		htMsg.put(SDP_BUILD_2009, "MSG_SDP_BUILD_PDM_VALIDATION_PKCONSTRAINT_SIZE::PDM������Լ�����Ƴ���(SQLSERVER:30λ, ORACLE:30λ, DB2:18λ)");
		htMsg.put(SDP_BUILD_2010, "MSG_SDP_BUILD_PDM_VALIDATION_UNKNOWN_DATATYPE::PDM�б�����ʹ����δ֪��������");
		htMsg.put(SDP_BUILD_2011, "MSG_SDP_BUILD_PDM_VALIDATION_FKCONSTRAINT_SIZE::PDM�����Լ�����Ƴ���(SQLSERVER:30λ, ORACLE:30λ, DB2:18λ)");
		htMsg.put(SDP_BUILD_2012, "MSG_SDP_BUILD_PDM_VALIDATION_INDEX_SIZE::PDM���������Ƴ���(SQLSERVER:30λ, ORACLE:30λ, DB2:18λ)");

		htMsg.put(SDP_BUILD_3000, "MSG_SDP_BUILD_DBCREATESCRIPT_SQLBUILDER::�ű������߹�������");
		htMsg.put(SDP_BUILD_3001, "MSG_SDP_BUILD_DBCREATESCRIPT_TEMPLATEPARSE_ERROR::ģ�巴�����ɽ���ű�����");

		htMsg.put(SDP_BUILD_4000, "MSG_SDP_BUILD_DBRECORDSCRIPT_DS_PARSE::Ԥ�ýű������ⲿ����Դ(DATASOURCE.XML)��������");
		htMsg.put(SDP_BUILD_4001, "MSG_SDP_BUILD_DBRECORDSCRIPT_ITEMS_PARSE::Ԥ�ýű������ļ�(ITEMS.XML)��������");
		htMsg.put(SDP_BUILD_4002, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_AVAILABLE_DATABASE_CONNECTION::��ȡԤ�ýű���������ⲿ����Դ֮���ݿ����Ӵ���");
		htMsg.put(SDP_BUILD_4003, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_TIMEOUT::Ԥ�ýű���������ⲿ����Դ֮���ݿ����ӳ�ʱ");
		htMsg.put(SDP_BUILD_4004, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_INITIAL_STATE_ERROR::Ԥ�ýű���������ⲿ����Դ֮���ݿ����ӳ�ʼ״̬����");
		htMsg.put(SDP_BUILD_4005, "MSG_SDP_BUILD_DBRECORDSCRIPT_DATABASE_CONNECTION_CLOSE_ERROR::Ԥ�ýű���������ⲿ����Դ֮���ݿ����ӹرմ���");
		htMsg.put(SDP_BUILD_4006, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_COMMON_MAP_ERROR::���ع���Ԥ�ýű�ӳ���ļ�����");
		htMsg.put(SDP_BUILD_4007, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_MODULE_MAP_ERROR::����ģ��Ԥ�ýű�ӳ���ļ�����");
		htMsg.put(SDP_BUILD_4008, "MSG_SDP_BUILD_DBRECORDSCRIPT_GLOBAL_DATASOURCE_CONFIG_FILE_NOT_EXIST::ȫ���ⲿ����Դ�����ļ�������");
		htMsg.put(SDP_BUILD_4009, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_SCRIPT_ITEMS_CHOOSED::δѡ��Ҫ������Ԥ�ýű�");
		htMsg.put(SDP_BUILD_4010, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_PRIMARYKEY_DEFINED::Ҫ����Ԥ�ýű��ı�δ��������");
		htMsg.put(SDP_BUILD_4011, "MSG_SDP_BUILD_DBRECORDSCRIPT_NUMBER_OF_PRIMARYKEY_GREAT_THAN_ONE::Ҫ����Ԥ�ýű��ı������������ֶθ�������1");
		htMsg.put(SDP_BUILD_4012, "MSG_SDP_BUILD_DBRECORDSCRIPT_LOAD_MODULE_QUERYENGINE_PROP_ERROR::����ģ���Զ����ѯ�����ļ�����");
		htMsg.put(SDP_BUILD_4013, "MSG_SDP_BUILD_DBRECORDSCRIPT_GENERATE_INIT_SCRIPT_ERROR::����Ԥ�ýű�����");
		htMsg.put(SDP_BUILD_4014, "MSG_SDP_BUILD_DBRECORDSCRIPT_GET_TABLE_METADATA_ERROR::��ȡԤ�ýű����ڱ�Ԫ���ݴ���");
		htMsg.put(SDP_BUILD_4015, "MSG_SDP_BUILD_DBRECORDSCRIPT_NO_SCRIPT_ITEMS_DIR_MAPPING::Ҫ������Ԥ�ýű���û������Ŀ¼�����࣬������Ӧ��.map�ļ�");

		htMsg.put(SDP_BUILD_5000, "MSG_SDP_BUILD_BASELINECOMPARE_ERROR::���߱Ƚϴ���");
		htMsg.put(SDP_BUILD_5001, "MSG_SDP_BUILD_BASELINECOMPARE_GET_LASTBASELINE_FROM_BUILDREPORT_ERROR::���ϴγɹ�����Ĺ��챨���ȡ������ߴ���");
		htMsg.put(SDP_BUILD_5002, "MSG_SDP_BUILD_MAKEBASELINE_ERROR::��ʶ���ι�����ߴ���");
		htMsg.put(SDP_BUILD_5003, "MSG_SDP_BUILD_PREPARE_ECO_FILE_ERROR::׼��ECO������ļ�����");

		htMsg.put(SDP_BUILD_6000, "MSG_SDP_BUILD_EXEC_BUILD_COMMAND_ERROR::ִ�й����������");
		htMsg.put(SDP_BUILD_6001, "MSG_SDP_BUILD_NO_AVAILABLE_BUILD_COMMAND_ERROR::δ�ҵ������������");
		htMsg.put(SDP_BUILD_7000, "MSG_SDP_BUILD_LOOKUP_UCM_UTILITY_ACTIVITY_ERROR::��ѯ���õ�UCM Utility Activity����");
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
