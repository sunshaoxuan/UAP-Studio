package nc.uap.ctrl.tpl.qry.operator;

public class IOperatorConstants {
	public final static String ISNULL = "is null";

	public final static String ISNOTNULL = "is not null";

	/**
	 * is null that including empty string
	 */
	public final static String ISNULL_INCLUDE_SPACE = "ISNULL";

	public final static String ISNOTNULL_INCLUDE_SPACE = "ISNOTNULL";

	public final static String EQ = "=";
	public final static String EQ2 = "==";//不允许多选等号操作符
	
	public final static String EQIC = "= ic";
	public final static String EQ2IC = "== ic";//忽略大小写
	

	public final static String IN = "in";	
	public final static String INIC = "in ic";//忽略大小写

	public static final String NEQ = "<>";
	public static final String NEQIC = "<> ic";
	
	public static final String NEQ2 = "!=";
	public static final String NEQ2IC = "!= ic";

	public static final String NIN = "not in";
	public static final String NINIC = "not in ic";

	public static final String GREATE = ">";

	public static final String GE = ">=";

	public static final String LE = "<=";

	public static final String LIKE = "like";//等于显示的包含，数据库的like %value%
	
	public static final String LLIKE = "left like";//左包含 like value%
	
	public static final String RLIKE = "right like";//右包含 like %value
	
	public static final String LIKEIC = "like ic";//忽略大小写的like
	
	public static final String LLIKEIC = "llike ic";
	
	public static final String RLIKEIC = "rlike ic";
	
	public static final String NLIKE = "not like";

	public static final String LESS = "<";

	public static final String BETWEEN = "between";
	
	public static final String BETWEENIC = "between ic";//忽略大小写的between

}
