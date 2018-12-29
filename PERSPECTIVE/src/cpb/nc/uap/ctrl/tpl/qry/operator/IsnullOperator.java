package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;

public class IsnullOperator extends NoneParaOperator {

	private static final long serialVersionUID = 3253547401202428704L;


	private static final IsnullOperator INSTANCE = new IsnullOperator();

	public static IsnullOperator getInstance() {
		return INSTANCE;
	}

	public IsnullOperator() {}
	
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		String columnName = meta.getSQLFieldCode();
		switch (meta.getDataType()) {
		case IQueryConstants.UFREF:// ����
			return columnName + " = '~'";
		case IQueryConstants.INTEGER:// ��ֵ
		case IQueryConstants.DECIMAL:
			return "cast(" + columnName + " as char) = '~'";
		default:
			return "" + columnName + " = '~'";
		}
	}

	@Override
	public String toString() {//Ϊ��
		return IOperatori18n.getISNull_Desc();
	}

	@Override
	public String getOperatorCode() {
		return IOperatorConstants.ISNULL;
	}

}
