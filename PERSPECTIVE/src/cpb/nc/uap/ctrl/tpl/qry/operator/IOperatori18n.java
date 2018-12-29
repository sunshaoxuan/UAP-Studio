package nc.uap.ctrl.tpl.qry.operator;

import uap.lfw.core.ml.LfwResBundle;

public class IOperatori18n {
	
	//等于
	public static String getEQ_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-eq");
	}
	
	//	"不等于"
	public static String getNEQ_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-ne");
	}
	
	//	大于 greater than
	public static String getGT_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-gt");	
	}
	
	//大于等于
	public static String getGE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-ge");
	}
	
	//	小于 less than
	public static String getLT_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-lt");
	}
	//	小于等于
	public static String getLE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-le");
	}
	
	//like 包含
	public static String getLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-like")/* @res "包含" */;
	}
	
	//left like 左包含
	public static String getLLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-llike")/* @res "左包含" */;
	}
	
	//right like 右包含
	public static String getRLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-rlike")/* @res "右包含" */;
	}
	//not like 不包含
	public static String getNotLike_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-notlike")/* @res "不包含" */;
	}
	public static String getIn_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-in")/* @res "包含于" */;
	}
	public static String getNotIn_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-notin")/* @res "不包含于" */;
	}
	
	//为空
	public static String getISNull_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-null")/* @res "为空" */;
	}
	//不为空
	public static String getISNotNull_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPP_Template-000512")/* @res "不为空" */;
	}
	
	
	//介于
	public static String getBetween_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-between")/* @res "介于" */;
	}
	
	public static String getOperatorDescByOperCode(String opercode){
		//以后再说了
		return null;
	}

}
