package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.base.PredicateConstants;
import uap.lfw.core.ml.LfwResBundle;

public class AndOperator implements ILogicalOperator {

	public static String  AND_i18n (){
		return LfwResBundle.getInstance().getStrByID("_template", "UPP_NewQryTemplate-0030");//"²¢ÇÒ"	
	}
	
	
	private static final long serialVersionUID = 5543224285706477857L;
	private static AndOperator instance = new AndOperator();
	private AndOperator()
	{
		
	}
	
	public static AndOperator getInstance()
	{
		return instance;
	}
	@Override
	public String toString() {
		return AND_i18n();
		
//		return PredicateConstants.STRING_AND;
	}
	private Object readResolve()
	{
		return instance;
	}

	public String getLogicOperatorCode() {
		return PredicateConstants.STRING_AND;
	}
}
