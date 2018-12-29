package nc.uap.lfw.perspective.model;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.lang.M_perspective;

/**
 * 定义常量
 * @author zhangxya
 *
 */
public interface Constant {
	public static final String DEFAULTNODE = M_perspective.Constant_0;
	public static final String DEFAULTREFNODE =M_perspective.Constant_1;
	public static final String VISIBILITY_PUBLIC="public"; //$NON-NLS-1$
	public static final String VISIBILITY_PROTECTED="protected"; //$NON-NLS-1$
	public static final String VISIBILITY_DEFAULT="default"; //$NON-NLS-1$
	public static final String VISIBILITY_PRIVATE="private"; //$NON-NLS-1$
	public static String[] VISIBILITYS={VISIBILITY_PUBLIC,VISIBILITY_PROTECTED,VISIBILITY_DEFAULT,VISIBILITY_PRIVATE};
	public static String[] HOTKEYMODIFIERS ={"SHIFT","CTRL","ALT","CTRL+SHIFT","CTRL+ALT", "ALT+SHIFT", "CTRL+SHIFT+ALT"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	public static String[] ISLAZY={"是","否"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] LABELPOSITION={"left", "right"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] CHECKBOXTYPE ={StringDataTypeConst.bOOLEAN, StringDataTypeConst.BOOLEAN,StringDataTypeConst.UFBOOLEAN};
	public static String[] ISVISIBLE={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISFIXHEADER={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISEDITABLE={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISIMAGEONLY={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISNULLBALE={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISPRIMARYKEY={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISLOCK={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] SELECTONLY={"Y","N"};	 //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISNEXTLINE={"Y","N"};	 //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISSUMCOL={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISAUTOEXPEND={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] TEXTALIGN={"center","left","right"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public static String[] ISSORTALBE={"Y","N"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] 	ISFARMEBORDER ={"否", "是"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] REFTYPE={M_perspective.Constant_2,M_perspective.Constant_3,M_perspective.Constant_4};
//	public static String[] POOLREFTYPE={"自定义参照","参数参照"};
	public static String[] PARAMREFNODETYPE = {M_perspective.Constant_5,M_perspective.Constant_6,M_perspective.Constant_7};
	public static String[] SCROLLING = {M_perspective.Constant_8, M_perspective.Constant_9, M_perspective.Constant_10};
	public static String[] ISMAIN={"是","否"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISDIALOG={"是","否"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] PLUGOUTTYPE={"normal","widget"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] PLUGINTYPE={"mapping","event"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] ISYES_NO={"是","否"}; //$NON-NLS-1$ //$NON-NLS-2$
	public static String[] CHECKBOXMODEL = {M_perspective.Constant_11,M_perspective.Constant_12,M_perspective.Constant_13};
	public static String[] TOOLBARTYPE = {M_perspective.Constant_14,M_perspective.Constant_15};
	
	public static String[] TEXTTYPE={EditorTypeConst.STRINGTEXT,EditorTypeConst.INTEGERTEXT, EditorTypeConst.CHECKBOX
		,EditorTypeConst.DECIMALTEXT, EditorTypeConst.PWDTEXT, EditorTypeConst.DATETEXT,EditorTypeConst.FILEUPLOAD,EditorTypeConst.RADIOGROUP,
		EditorTypeConst.REFERENCE, EditorTypeConst.COMBODATA,EditorTypeConst.TEXTAREA,EditorTypeConst.RICHEDITOR, EditorTypeConst.CHECKBOXGROUP,
		EditorTypeConst.RADIOCOMP, EditorTypeConst.SELFDEF, EditorTypeConst.DATETIMETEXT, EditorTypeConst.FILECOMP, EditorTypeConst.LIST,
		EditorTypeConst.YEARTEXT, EditorTypeConst.MONTHTEXT, EditorTypeConst.YEARMONTHTEXT, EditorTypeConst.EMAILTEXT, EditorTypeConst.PHONETEXT, 
		EditorTypeConst.LINKTEXT, EditorTypeConst.MONEYTEXT, EditorTypeConst.PRECENTTEXT,EditorTypeConst.TIMETEXT/*, EditorTypeConst.ADDRESSTEXT*/};
	
	public static String[] DATATYPE={StringDataTypeConst.STRING,StringDataTypeConst.INTEGER,StringDataTypeConst.DOUBLE,StringDataTypeConst.dOUBLE
		,StringDataTypeConst.UFDOUBLE,StringDataTypeConst.FLOATE,StringDataTypeConst.fLOATE,StringDataTypeConst.bYTE,StringDataTypeConst.BYTE
		,StringDataTypeConst.bOOLEAN,StringDataTypeConst.BOOLEAN,StringDataTypeConst.UFBOOLEAN,
		StringDataTypeConst.DATE, StringDataTypeConst.UFDATETIME, StringDataTypeConst.BIGDECIMAL,StringDataTypeConst.lONG,StringDataTypeConst.lONG,
		StringDataTypeConst.UFDATE,StringDataTypeConst.UFTIME,StringDataTypeConst.CHAR,StringDataTypeConst.UFNUMBERFORMAT,
		StringDataTypeConst.Decimal, StringDataTypeConst.ENTITY, StringDataTypeConst.OBJECT
		,StringDataTypeConst.CUSTOM,StringDataTypeConst.MEMO, StringDataTypeConst.UFLiteralDate, StringDataTypeConst.UFDate_begin, StringDataTypeConst.UFDate_end, 
		StringDataTypeConst.UFYEAR, StringDataTypeConst.UFMONTH, StringDataTypeConst.UFYEARMONTH, StringDataTypeConst.UFMAIL, StringDataTypeConst.UFPHONE, 
		StringDataTypeConst.UFLINK, StringDataTypeConst.UFMONEY, StringDataTypeConst.UFPERCENT, StringDataTypeConst.UFADDR};
	//校验类型
	public static String[] FORMATERTYPE = {"email","number","chn", "variable", "date", "phone"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}

