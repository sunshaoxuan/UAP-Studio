package uap.lfw.smartmw.mw;

/**
 * 包含代码生成过程当中的规则常量和符号常量。
 * 创建日期：(2001-2-13 9:32:15)
 * 作者：游志强</br>
 */
public interface CodeCreationRuleConstants {
	public static final int ENTITY_BEAN = 0;
	public static final int STATELESS_SESSION_BEAN = 1;
	public static final int STATEFUL_SESSION_BEAN = 2;
	public static int FIRST_HOME_METHOD_ID = 100;
	public static int FIRST_REMOTE_METHOD_ID =200;
	public static String InsertCase = "<InsertCase>";
	public static String InsertPoint = "<InsertPoint>";
	public static String HomeMethodIDPrefix = "HOME_";
	public static String RemoteMethodIDPrefix = "METHOD_";
	public static String ParamPrefix = "$";
	public static String CodeSeperator = "_";
	public static String MethodConstClassSuffix = "_Method_Const_Local_";
	public static String LocalClassSuffix = "_Local_";
	public static String RemoteClassSuffix = "_stub_";
	public static String BasePackage = "nc.bs.mw.naming.";
	public static String TestClientPrefix = "_TestClient_";
	public static String BeanKeyWord = "";
	public static String[] BeanSuffixes = new String[]{"bean","bo","beanbo"};
	public static String ArraySymbol = "S";
	public static String BOWrapperSuffix = "_wrapper";
}
