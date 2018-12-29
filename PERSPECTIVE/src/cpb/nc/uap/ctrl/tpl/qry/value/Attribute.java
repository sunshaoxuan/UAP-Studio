package nc.uap.ctrl.tpl.qry.value;

import java.io.Serializable;

/**
 * 库表属性
 * 
 * @author 刘晨伟
 *
 * 创建日期：2009-10-19
 */
public class Attribute implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4516219450985128387L;
	
	private String code;
	private String name;
	
	private String attrpath;// 对应的元数据属性路径
	
	public Attribute() {
		super();
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAttrpath() {
		return attrpath;
	}
	
	public void setAttrpath(String attrpath) {
		this.attrpath = attrpath;
	}
}