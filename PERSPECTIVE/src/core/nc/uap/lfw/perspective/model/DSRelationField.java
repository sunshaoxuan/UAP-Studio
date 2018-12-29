package nc.uap.lfw.perspective.model;

import nc.uap.lfw.core.data.Field;

/**
 * 将field字段进行扩展
 * @author zhangxya
 *
 */
public class DSRelationField extends Field{
	
	private static final long serialVersionUID = 1L;
	
	private String ismatch;
	private String iscontains;
	private String pk_field;
	private String matchfield;
	public String getMatchfield() {
		return matchfield;
	}
	public void setMatchfield(String matchfield) {
		this.matchfield = matchfield;
	}
	public String getPk_field() {
		return pk_field;
	}
	public void setPk_field(String pk_field) {
		this.pk_field = pk_field;
	}
	public String getIscontains() {
		return iscontains;
	}
	public void setIscontains(String iscontains) {
		this.iscontains = iscontains;
	}
	public String getIsmatch() {
		return ismatch;
	}
	public void setIsmatch(String ismatch) {
		this.ismatch = ismatch;
	}
	
}
