package nc.uap.lfw.grid.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * gridEditor input
 * @author zhangxya
 *
 */
public class GridEditorInput extends ElementEditorInput{

	
	public GridEditorInput(GridComp gridcomp, LfwView widget, LfwWindow pagemeta) {
		super(gridcomp, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_grid.GridEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_grid.GridEditorInput_0;
	}

}
