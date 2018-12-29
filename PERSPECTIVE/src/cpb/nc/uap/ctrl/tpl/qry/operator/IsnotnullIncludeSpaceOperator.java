package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;


public class IsnotnullIncludeSpaceOperator extends IsnotnullOperator {

	private static final long serialVersionUID = 3253547246292428704L;
	
	private static final IsnotnullIncludeSpaceOperator INSTANCE = new IsnotnullIncludeSpaceOperator();

	public static IsnotnullIncludeSpaceOperator getInstance() {
		return INSTANCE;
	}

	@Override
	public String getOperatorCode() {
		return IOperatorConstants.ISNOTNULL_INCLUDE_SPACE;
	}

	@Override
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		String sql = super.getSQLString(meta, value);
		return "(" + sql + " and " + "len(" + meta.getFieldCode() + ")>0)";
	}

}
