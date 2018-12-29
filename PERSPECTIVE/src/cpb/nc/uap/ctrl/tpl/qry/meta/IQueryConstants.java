package nc.uap.ctrl.tpl.qry.meta;

public interface IQueryConstants {
//	public final static String TITLE_NORMAL = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template","UPP_Template-000513")/*@res "常用条件"*/;
//	public final static String TITLE_DEFINE = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template","UPP_Template-000514")/*@res "自定义条件"*/;


	//数据类型
	public final static int STRING = 0; //字符
	public final static int INTEGER = 1; //整数
	public final static int DECIMAL = 2; //小数
	public final static int DATE = 3; //日期
	public final static int BOOLEAN = 4; //逻辑
	public final static int UFREF = 5; //参照
	//public final static intCOMBO = 6;//下拉
	public final static int USERCOMBO = 6; //下拉
	public final static int USERDEF = 7; //其它
	public final static int TIME = 8; //时间
	public final static int MULTILANG = 9; //多语
	public final static int LITERALDATE = 10; //UFLiteralDate
	public final static int UFTIME = 11; //UFTime

	public final static int DISPCODE = 0; //显示编码
	public final static int DISPNAME = 1; //显示名称

	public final static int RETURNCODE = 0; //返回编码
	public final static int RETURNNAME = 1; //返回名称
	public final static int RETURNPK = 2; //返回PK

//	public final static String DISP_AND =
//		nc.ui.pub.ClientEnvironment.getInstance().getModuleLang().getString(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template","UPP_Template-000515")/*@res "并且"*/);
//	public final static String DISP_OR =
//		nc.ui.pub.ClientEnvironment.getInstance().getModuleLang().getString(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template","UPP_Template-000516")/*@res "或者"*/);

	public final static String NO_BRACKET = " ";
	public final static String LEFT_BRACKET = "(";
	public final static String RIGHT_BRACKET = ")";

	public final static String COMBO_INDEX = "I"; //返回序号(数值型)
	public final static String COMBO_INDEX_CHAR = "C"; //返回序号(字符型)
	public final static String COMBO_STRING = "S"; //返回内容(字符串)
	public final static String COMBO_PAIR = "P"; //成对的类型,hzg+,since v5.02
	public final static String COMBO_UNCHECK = "U"; //未检查
	public final static String COMBO_NULL = "N"; //不是有效类型
	
	
	//for translation  resid=value
	public final static String COMBO_INDEX_X = "IX"; //返回序号(数值型)
	public final static String COMBO_INDEX_CHAR_X = "CX"; //返回序号(字符型)
	public final static String COMBO_STRING_X = "SX"; //返回内容(字符串)
	public final static String COMBO_INDICATE_X = "X"; //显示需要多语
	
	//支持通过元数据定义的枚举类型
	public final static String COMBO_INTEGER_META = "IM"; //枚举的Value是整形的 
	public final static String COMBO_STRING_META = "SM";  //枚举的Value是字符串
	
	
	//for translation  resid=value
	public final static String COMBO_INDEX_DBFIELD = "IF"; //返回序号(数值型)
	public final static String COMBO_INDEX_CHAR_DBFIELD = "CF"; //返回序号(字符型)
	public final static String COMBO_STRING_DBFIELD = "SF"; //返回内容(字符串)
	public final static String COMBO_STRING_DBFIELD_XH="XH";//返回表中的xh(序号)列值
	public final static String COMBO_STRING_DBFIELD_ZFQZ="ZFQZ";//返回表中的zfqz(字符取值)列值
}