package nc.uap.lfw.form.commands;

import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * 选中form的所有行
 * @author zhangxya
 *
 */
public class FormSelectedAllAction extends Action {

	private FormPropertiesView view;
	
	public FormSelectedAllAction(FormPropertiesView view) {
		super(M_form.FormSelectedAllAction_0);
		setHoverImageDescriptor(PaletteImage.getSelectedAllImgDescriptor());
		this.view = view;
	}
	
	public void run(){
		CheckboxTreeViewer ctx = view.getCtv();
		ctx.setAllChecked(true);
	}
}
