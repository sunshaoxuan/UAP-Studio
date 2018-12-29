package nc.uap.lfw.perspective.editor;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_perspective;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * DatasetµÄinput
 * @author zhangxya
 *
 */
public class DataSetEitorInput extends ElementEditorInput{

	public DataSetEitorInput(Dataset ds,LfwView widget, LfwWindow pagemeta){
		super(ds, widget, pagemeta);
	}
	
	

	public boolean exists() {
		// TODO Auto-generated method stub
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return M_perspective.DataSetEitorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_perspective.DataSetEitorInput_0;
	}
}
