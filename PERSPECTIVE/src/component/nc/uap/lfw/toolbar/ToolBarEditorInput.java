package nc.uap.lfw.toolbar;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_toolbar;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * toolbar ±à¼­Æ÷input
 * @author zhangxya
 *
 */
public class ToolBarEditorInput extends ElementEditorInput{


	public ToolBarEditorInput(ToolBarComp toolbar, LfwView widget, LfwWindow pagemeta){
		super(toolbar, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_toolbar.ToolBarEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_toolbar.ToolBarEditorInput_0;
	}

}
