package nc.uap.portal.plugins.model;

import nc.vo.pub.SuperVO;

import org.apache.commons.lang.StringUtils;

/**
 * ��չ��
 * 
 * @author licza
 * @since 2010��9��9��14:33:10
 */
public class PtExtensionPoint extends SuperVO {

	
	private static final long serialVersionUID = 8350649195253142839L;
	/** ��չ��Ψһ��ʶ **/
	protected String point;
	/** ��չ���� **/
	protected String title;
	
	protected String title2;
	protected String title3;
	protected String title4;
	protected String title5;
	protected String title6;
	/** ʵ����չ���� **/
	protected String classname;;

	/** ɾ����־ **/
	private java.lang.Integer dr = 0;
	/** �������¼� **/
	private nc.vo.pub.lang.UFDateTime ts;
	/** ���� **/
	public String pk_extpoint;
	
	/**����ģ��**/
	public String module;
	
	public String pk_module;
	

	public String getPoint() {
		return point;
	}

	public void setPoint(String id) {
		this.point = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public String getTitle4() {
		return title4;
	}

	public void setTitle4(String title4) {
		this.title4 = title4;
	}

	public String getTitle5() {
		return title5;
	}

	public void setTitle5(String title5) {
		this.title5 = title5;
	}

	public String getTitle6() {
		return title6;
	}

	public void setTitle6(String title6) {
		this.title6 = title6;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getPk_module() {
		return pk_module;
	}

	public void setPk_module(String pk_module) {
		this.pk_module = pk_module;
	}

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	public String getPk_extpoint() {
		return pk_extpoint;
	}

	public void setPk_extpoint(String pk_extpoint) {
		this.pk_extpoint = pk_extpoint;
	}

	@Override
	public String getPKFieldName() {
		return "pk_extpoint";
	}

	@Override
	public String getTableName() {
		return "pt_extpoint";
	}

	@Override
	public int hashCode() {
		return point.hashCode()*title.hashCode()*classname.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PtExtensionPoint) {
			PtExtensionPoint _ex = (PtExtensionPoint) obj;
			return StringUtils.equals(point, _ex.getPoint()) && StringUtils.equals(title, _ex.getTitle()) && StringUtils.equals(classname, _ex.getClassname());
		} else {
			return false;
		}
	}
	 
	
	public void copy(PtExtensionPoint exp) {
		if(exp!=null){
			this.title=exp.getTitle();
			this.classname=exp.getClassname();
		}
	}
}
