package nc.uap.lfw.progressbar;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class ProgressBarEditorInput extends ElementEditorInput{


	public ProgressBarEditorInput(ProgressBarComp progress, LfwView widget, LfwWindow pagemeta){
		super(progress, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return "进度条编辑器";
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  "进度条编辑器";
	}

}
