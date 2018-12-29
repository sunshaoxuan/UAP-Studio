package nc.uap.ctrl.tpl.qry.meta;

import nc.vo.pub.ValidationException;

public class OperaVO extends nc.vo.pub.ValueObject {
	private static final long serialVersionUID = -5191289628896157070L;
	private java.lang.String operaCode;
	private java.lang.String operaName;

	/**
	 * QueryOperaVO 构造子注解.
	 */
	public OperaVO() {
		super();
	}

	/**
	 * 此处插入方法说明.
	 * 
	 * 创建日期:(2001-3-7 11:34:51)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public Object clone() {
		OperaVO newvo = new OperaVO();
		newvo.setOperaCode(this.getOperaCode());
		newvo.setDirty(isDirty());
		newvo.setOperaName(getOperaName());
		newvo.setPrimaryKey(getPrimaryKey());
		return newvo;
	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:(2001-2-15 14:18:08)
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return null;
	}

	/**
	 * 创建日期:(2001-3-20 17:40:04)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOperaCode() {
		return operaCode;
	}

	/**
	 * 创建日期:(2001-3-20 17:40:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOperaName() {
		return operaName;
	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public String getPrimaryKey() {
		return null;
	}

	/**
	 * 创建日期:(2001-3-20 17:40:04)
	 * 
	 * @param newOperaCode
	 *            java.lang.String
	 */
	public void setOperaCode(java.lang.String newOperaCode) {
		operaCode = newOperaCode;
	}

	/**
	 * 创建日期:(2001-3-20 17:40:25)
	 * 
	 * @param newOperaName
	 *            java.lang.String
	 */
	public void setOperaName(java.lang.String newOperaName) {
		operaName = newOperaName;
	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:(2001-3-26)
	 * 
	 * @param id
	 *            String
	 */
	public void setPrimaryKey(String id) {

	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:(2001-2-15 11:47:35)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
