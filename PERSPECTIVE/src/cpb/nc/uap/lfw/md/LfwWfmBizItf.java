package nc.uap.lfw.md;

import nc.vo.pub.lang.UFDateTime;

public interface LfwWfmBizItf {
	static final String ATTRIBUTE_BILLMAKEDATE = "billmakedate";
	
	static final String ATTRIBUTE_BILLMAKER = "billmaker";

	static final String ATTRIBUTE_BILLNO = "billno";

	static final String ATTRIBUTE_ORG = "billorg";
	
	static final String ATTRIBUTE_APPROVER = "approver";
	
	static final String ATTRIBUTE_APPROVEDATE = "approvedate";

	static final String ATTRIBUTE_FORMSTATE = "formstate";
	
	static final String ATTRIBUTE_FORMTITLE = "formtitle";
	
	static final String ATTRIBUTE_FORMINSPK = "forminspk";
	

	/**
	 * 设置form单据的状态
	 * @return
	 */
	String getFormState();
	
	void setFormState(String formstate);
	
	
	/**
	 * 单据标题
	 * @return
	 */
	String getFormTitle();
	
	void setFormTitle(String formtitle);
	
	
	/**
	 * 单据主键
	 * @return
	 */
	String getFormInspk();
	
	void setFormInspk(String forminspk);
		
	
	/**
	 * 返回制单日期
	 * @return
	 */
	UFDateTime getBillMakeDate();
	
	/**
	 * 回写单据日期
	 * @return
	 */
	void setBillMakeDate(UFDateTime billmakedate);
	

	/**
	 * 返回制单人PK
	 * @return
	 */
	String getBillMaker();

	/**
	 * 返回审批人PK
	 * @return
	 */
	String getApprover();

	/**
	 * 返回单据号
	 * @return
	 */
	String getBillNo();

	/**
	 * 返回部门PK
	 * @return
	 */
	String getBillOrg();

	/**
	 * 返回单据的审批时间
	 * @return
	 */
	UFDateTime getApproveDate();
//
	/**
	 * 回写单据的审批时间
	 * @return
	 */
	void setApproveDate(UFDateTime approveDate);

	/**
	 * 回写部门PK
	 * @return
	 */
	void setBillOrg(String billorg);

	/**
	 * 回写单据的审批人
	 * @return
	 */
	void setApprover(String approver);

	/**
	 * 回写单据号
	 * @return
	 */
	void setBillNo(String billNo);

	/**
	 * 回写单据的制单人
	 * @return
	 */
	void setBillMaker(String maker);

}
