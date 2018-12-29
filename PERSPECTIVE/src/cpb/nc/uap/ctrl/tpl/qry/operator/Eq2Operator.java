package nc.uap.ctrl.tpl.qry.operator;


/**
 * 不允许多选的等号
 * @author huangzg
 *
 */
public class Eq2Operator extends EqOperator {

	private static final long serialVersionUID = 79122814953707632L;

	private static final Eq2Operator INSTANCE = new Eq2Operator();

	public static Eq2Operator getInstance() {
		return INSTANCE;
	}

	@Override
	public int getParameterNumber() {
		return 1;
	}
	
	public String getOperatorCode() {
		return IOperatorConstants.EQ2;
	}

	protected String getSingleValueOperatorCode() {
		return IOperatorConstants.EQ;
	}
}
