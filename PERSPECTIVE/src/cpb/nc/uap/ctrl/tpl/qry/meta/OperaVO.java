package nc.uap.ctrl.tpl.qry.meta;

import nc.vo.pub.ValidationException;

public class OperaVO extends nc.vo.pub.ValueObject {
	private static final long serialVersionUID = -5191289628896157070L;
	private java.lang.String operaCode;
	private java.lang.String operaName;

	/**
	 * QueryOperaVO ������ע��.
	 */
	public OperaVO() {
		super();
	}

	/**
	 * �˴����뷽��˵��.
	 * 
	 * ��������:(2001-3-7 11:34:51)
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
	 * ������ֵ�������ʾ����.
	 * 
	 * ��������:(2001-2-15 14:18:08)
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {
		return null;
	}

	/**
	 * ��������:(2001-3-20 17:40:04)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOperaCode() {
		return operaCode;
	}

	/**
	 * ��������:(2001-3-20 17:40:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOperaName() {
		return operaName;
	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public String getPrimaryKey() {
		return null;
	}

	/**
	 * ��������:(2001-3-20 17:40:04)
	 * 
	 * @param newOperaCode
	 *            java.lang.String
	 */
	public void setOperaCode(java.lang.String newOperaCode) {
		operaCode = newOperaCode;
	}

	/**
	 * ��������:(2001-3-20 17:40:25)
	 * 
	 * @param newOperaName
	 *            java.lang.String
	 */
	public void setOperaName(java.lang.String newOperaName) {
		operaName = newOperaName;
	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:(2001-3-26)
	 * 
	 * @param id
	 *            String
	 */
	public void setPrimaryKey(String id) {

	}

	/**
	 * ��֤���������֮��������߼���ȷ��.
	 * 
	 * ��������:(2001-2-15 11:47:35)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
	 */
	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
