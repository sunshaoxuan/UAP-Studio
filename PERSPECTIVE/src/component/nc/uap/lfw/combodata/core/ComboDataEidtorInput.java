package nc.uap.lfw.combodata.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_combodata;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * 下拉数据集input
 * @author zhangxya
 *
 */
public class ComboDataEidtorInput  extends ElementEditorInput{


	public ComboDataEidtorInput(ComboData combo, LfwView widget, LfwWindow pagemeta){
		super(combo, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_combodata.ComboDataEidtorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_combodata.ComboDataEidtorInput_1;
	}
}
