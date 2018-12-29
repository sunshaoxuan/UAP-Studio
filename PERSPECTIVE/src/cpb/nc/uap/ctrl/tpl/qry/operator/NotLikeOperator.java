package nc.uap.ctrl.tpl.qry.operator;


public class NotLikeOperator extends LikeOperator {

	private static final long serialVersionUID = -3312909946740685673L;
	
	private static final NotLikeOperator INSTANCE = new NotLikeOperator();

	public static NotLikeOperator getInstance() {
		return INSTANCE;
	}
	@Override
	public String toString() {
		return IOperatori18n.getNotLike_Desc();//"不相似于"，不包含
	}

	@Override
	public String getOperatorCode() {
		return IOperatorConstants.NLIKE;
	}

}
