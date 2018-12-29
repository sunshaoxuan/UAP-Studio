package nc.uap.portal.plugins.model;

import java.io.Serializable;

import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

/**
 * 扩展
 * 
 * @author licza
 * @since 2010年9月9日15:12:17
 */
public class PtExtension extends SuperVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6245810623877791443L;
	/** 扩展名 **/
	protected String id;
	/** 类名 **/
	protected String classname;
	/** 名称 **/
	protected String title;
	protected String title2;
	protected String title3;
	protected String title4;
	protected String title5;
	protected String title6;
	
	/** 扩展点类名（即接口名） **/
	protected String point;
	/** 国际化名称 **/
	protected String i18nname;
	/** 删除标志 **/
	private java.lang.Integer dr = 0;
	/** 最后操作时间 **/
	private nc.vo.pub.lang.UFDateTime ts;
	/** 主键 **/
	public String pk_extension;
	/**	扩展点主键 **/
	public String pk_extpoint;
	/** 模块 **/
	public String module;
	
	public String pk_module;
	
	private UFBoolean isactive;
	
	public static final String ID="id";
	public static final String CLASSNAME="classname";
	public static final String TITLE="title";
	public static final String TITLE2 = "title2";
	public static final String TITLE3 = "title3";
	public static final String TITLE4 = "title4";
	public static final String TITLE5 = "title5";
	public static final String TITLE6 = "title6";
	
	public static final String I18NNAME= "i18nname";
	public static final String POINT="point";
	public static final String PK_EXTENTION="pk_extension";
	public static final String PK_EXTPOINT = "pk_extpoint";
	public static final String MODULE="module";
	public static final String PK_MODULE = "pk_module";
	public static final String ISACTIVE="isactive";
	
	
	
	/**
	 * 获得一个实例
	 * 
	 * @return
	 */
	public Object newInstance() {
			if(isactive != null && !isactive.booleanValue())
				throw new SecurityException("this plugin is unreachable!");
			Object ins = LfwClassUtil.newInstance(classname);
			if (ins instanceof IDynamicalPlugin) {
				IDynamicalPlugin dp = (IDynamicalPlugin) ins;
				dp.init(getId(), null, getTitle());
			}
			return ins;
	}
	/**
	 * 获得一个实例
	 * @param <T>
	 * @param itf
	 * @return
	 * @throws CpbBusinessException
	 */
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> itf){
			return (T) newInstance();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getPk_extension() {
		return pk_extension;
	}

	public void setPk_extension(String pk_extension) {
		this.pk_extension = pk_extension;
	}

	@Override
	public String getPKFieldName() {
		return "pk_extension";
	}

	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public String getPk_extpoint() {
		return pk_extpoint;
	}
	public void setPk_extpoint(String pk_extpoint) {
		this.pk_extpoint = pk_extpoint;
	}
	@Override
	public String getTableName() {
		return "pt_extension";
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

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return id.hashCode()*point.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PtExtension) {
			PtExtension ex = (PtExtension) obj;
			return StringUtils.equals(ex.getId(), id)  && StringUtils.equals(ex.getPoint(), point);
		} else {
			return false;
		}
	}

	public boolean same(PtExtension ex){
		if(ex != null){
			return StringUtils.equals(ex.getId(), id) && StringUtils.equals(ex.getClassname(), classname) && StringUtils.equals(ex.getTitle(), title) && StringUtils.equals(ex.getPoint(), point);
		} else {
			return false;
		}
	}
	
 	/**
	 * 复制
	 * 
	 * @param ex
	 */
	public void copy(PtExtension ex) {
		this.classname = ex.getClassname();
		this.id = ex.getId();
		this.point = ex.getPoint();
		this.title = ex.getTitle();
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public UFBoolean getIsactive() {
		return isactive;
	}

	public void setIsactive(UFBoolean isactive) {
		this.isactive = isactive;
	}
	public String getPk_module() {
		return pk_module;
	}
	public void setPk_module(String pk_module) {
		this.pk_module = pk_module;
	}

}
