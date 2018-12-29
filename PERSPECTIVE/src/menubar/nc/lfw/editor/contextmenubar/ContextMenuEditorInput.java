package nc.lfw.editor.contextmenubar;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * ÓÒ¼ü²Ëµ¥Edior input
 * @author zhangxya
 *
 */
public class ContextMenuEditorInput extends ElementEditorInput{


	public ContextMenuEditorInput(ContextMenuComp context, LfwView widget, LfwWindow pagemeta){
		super(context, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_menubar.ContextMenuEditorInput_0; //$NON-NLS-1$
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_menubar.ContextMenuEditorInput_0; //$NON-NLS-1$
	}
}
