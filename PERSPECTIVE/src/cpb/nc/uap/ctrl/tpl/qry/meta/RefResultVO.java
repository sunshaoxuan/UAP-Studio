package nc.uap.ctrl.tpl.qry.meta;

public class RefResultVO extends nc.vo.pub.ValueObject {
	private String refPK;
	private String refCode;
	private String refName;
	private Object refObj;

	private boolean isIncludeSub = false;
/**
 * RefResultVO 构造子注解.
 */
public RefResultVO() {
	super();
}
/**
 * 返回数值对象的显示名称.
 * 
 * 创建日期:(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称.
 */
public String getEntityName() {
	return null;
}
/**
 * 返回对象标识,用来唯一定位对象.
 *
 * 创建日期:(2001-6-14)
 * @return String
 */
public String getPrimaryKey() {

	return refPK;
}
/**
 * 属性refCode的Getter方法.
 *
 * 创建日期:(2001-6-14)
 * @return String
 */
public String getRefCode() {
	return refCode;
}
/**
 * 属性refName的Getter方法.
 *
 * 创建日期:(2001-6-14)
 * @return String
 */
public String getRefName() {
	return refName;
}
/**
 * 属性refObj的Getter方法.
 *
 * 创建日期:(2001-6-14)
 * @return Object
 */
public Object getRefObj() {
	return refObj;
}
/**
 * 属性refPK的Getter方法.
 *
 * 创建日期:(2001-6-14)
 * @return String
 */
public String getRefPK() {
	return refPK;
}
/**
 * 此处插入方法说明.
 * 创建日期:(2004-4-15 9:51:14)
 * @return boolean
 */
public boolean isIncludeSub() {
	return isIncludeSub;
}
/**
 * 此处插入方法说明.
 * 创建日期:(2004-4-15 9:51:14)
 * @param newIsIncludeSub boolean
 */
public void setIsIncludeSub(boolean newIsIncludeSub) {
	isIncludeSub = newIsIncludeSub;
}
/**
 * 设置对象标识,用来唯一定位对象.
 *
 * 创建日期:(2001-6-14)
 * @param newPK String 
 */
public void setPrimaryKey(String newPK) {

	refPK = newPK;
}
/**
 * 属性refCode的setter方法.
 *
 * 创建日期:(2001-6-14)
 * @param newRefCode String
 */
public void setRefCode(String newRefCode) {

	refCode = newRefCode;
}
/**
 * 属性refName的setter方法.
 *
 * 创建日期:(2001-6-14)
 * @param newRefName String
 */
public void setRefName(String newRefName) {

	refName = newRefName;
}
/**
 * 属性refObj的setter方法.
 *
 * 创建日期:(2001-6-14)
 * @param newRefObj Object
 */
public void setRefObj(Object newRefObj) {

	refObj = newRefObj;
}
/**
 * 属性refPK的setter方法.
 *
 * 创建日期:(2001-6-14)
 * @param newRefPK String
 */
public void setRefPK(String newRefPK) {

	refPK = newRefPK;
}
/**
 * 验证对象各属性之间的数据逻辑正确性.
 * 
 * 创建日期:(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
 *     ValidationException,对错误进行解释.
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
