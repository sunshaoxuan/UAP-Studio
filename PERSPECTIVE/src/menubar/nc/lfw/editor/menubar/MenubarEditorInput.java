package nc.lfw.editor.menubar;

import nc.lfw.editor.common.PagemetaElementEditorInput;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 
 * @author guoweic
 *
 */
public class MenubarEditorInput extends PagemetaElementEditorInput {

	private LfwView widget;
	
	private String gridId;
	
	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}
	

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public MenubarEditorInput(WebElement element, LfwWindow pagemeta) {
		super(element, pagemeta);
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_menubar.MenubarEditorInput_0;
	}

	public String getToolTipText() {
		return  M_menubar.MenubarEditorInput_0;
	}

}
