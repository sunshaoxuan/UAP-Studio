package nc.uap.portal.theme;

import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.om.LookAndFeel;

/**
 * ThemeEditorInput
 * 
 * @author dingrf
 * 
 */
public class ThemeEditorInput extends PortalBaseEditorInput {

	private LookAndFeel lookAndFeel;

	private String moduleName;
	
	public LookAndFeel getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(LookAndFeel lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public ThemeEditorInput(LookAndFeel lookAndFeel,String ModuleName){
		this.lookAndFeel = lookAndFeel;
		this.moduleName = ModuleName;
	}
	
	public String getName() {
		return "Ö÷Ìâ±à¼­Æ÷";
	}

	public String getToolTipText() {
		return "²å¼þ±à¼­Æ÷";
	}

}
