package nc.uap.ctrl.tpl.qry.operator;

public class Eq2ICOperator extends EqICOperator{
	private static final long serialVersionUID = 1L;

	private static final Eq2ICOperator INSTANCE = new Eq2ICOperator();

	public static Eq2ICOperator getInstance() {
		return INSTANCE;
	}

	@Override
	public int getParameterNumber() {
		return 1;
	}

	protected String getSingleValueOperatorCode() {
		return IOperatorConstants.EQ;
	}
}
