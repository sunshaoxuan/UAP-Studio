package nc.uap.lfw.form.commands;

import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

public class FormUnSelectedAllAction extends Action{
	private FormPropertiesView view;
	
	public FormUnSelectedAllAction(FormPropertiesView view) {
		setText(M_form.FormUnSelectedAllAction_0);
		setHoverImageDescriptor(PaletteImage.getUnSelectedAllImgDescriptor());
		this.view = view;
	}
	
	public void run(){
		CheckboxTreeViewer ctx = view.getCtv();
		ctx.setAllChecked(false);
	}
}
