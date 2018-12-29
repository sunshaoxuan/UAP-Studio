package nc.uap.lfw.listview.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_listview;

public class ListViewEditorInput extends ElementEditorInput{

	public ListViewEditorInput(ListViewComp listviewComp, LfwView widget,
			LfwWindow pagemeta) {
		super(listviewComp, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_listview.ListViewEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_listview.ListViewEditorInput_0;
	}

}
