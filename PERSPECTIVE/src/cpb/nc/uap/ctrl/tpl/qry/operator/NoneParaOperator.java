package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;


public abstract class NoneParaOperator implements IOperator {

	private static final long serialVersionUID = -5191289628896157070L;
	
	public int getParameterNumber() {
		return 0;
	}

	public abstract String getOperatorCode();

	public String getSQLString(FilterMeta meta, IFieldValue value) {
		String opCode = getOperatorCode();
		return meta.getSQLFieldCode() + " " + opCode;
	}

	public String getDescription(IFilterMeta meta, IFieldValue value) {
		// String code = getOperatorCode();

		return meta.toString() + " " + toString();
	}

	public boolean isFuzzy() {
		return false;
	}
}
