package nc.uap.portal.skin;

import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.lang.M_portal;

/**
 * SkinEditorInput
 * 
 * @author dingrf
 *
 */
public class SkinEditorInput extends PortalBaseEditorInput {
	
	/**主题ID*/
	private String themeId;
	
	/**样式分类*/
	private String type;
	
	/**样式ID*/
	private String id;
	
	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SkinEditorInput(String themeId,String type,String id){
		this.themeId = themeId;
		this.type = type;
		this.id = id;
	}
	
	public String getName() {
		return M_portal.SkinEditorInput_0;
	}

	public String getToolTipText() {
		return M_portal.SkinEditorInput_0;
	}

}
