package nc.uap.ctrl.tpl.qry.operator;

public class NeqICOperator extends EqICOperator{
	private static final long serialVersionUID = -1976570665432108547L;
	
	private static final NeqICOperator INSTANCE = new NeqICOperator();
	
	
	public static NeqICOperator getInstance() {
		return INSTANCE;
	}
	

	@Override
	public int getParameterNumber() {
		return -1;
	}
	
	public String getMultiValueOperator() {
		return IOperatorConstants.NIN;
	}
	
	protected String getSingleValueOperatorCode() {
		return IOperatorConstants.NEQ;
	}
	
	@Override
	public String toString() {	
		return IOperatori18n.getNEQ_Desc();
	}

	@Override
	public String getOperatorCode() {
		return getSingleValueOperatorCode();
	}
}
