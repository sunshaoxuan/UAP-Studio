package nc.uap.lfw.form;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.views.CellPropertiesView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * grid 属性页
 * @author zhangxya
 *
 */
public class FormViewPage extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private FormPropertiesView formPropertiesView = null;
	
	public FormPropertiesView getFormPropertiesView() {
		return formPropertiesView;
	}

	public void setFormPropertiesView(FormPropertiesView formPropertiesView) {
		this.formPropertiesView = formPropertiesView;
	}

	public void createControl(Composite parent) {

		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_form.FormViewPage_0);
		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		setCellPropertiesView(new CellPropertiesView(voTabFolder, SWT.NONE));
	}

	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}

	private void addFormPropItemControl() {
		Control control = null;
		try { 
			control = cellPropTabItem.getControl();
		} catch (Exception e) {
			return;
		} finally {
			if(control instanceof CellPropertiesView){
				cellPropTabItem.setControl(null);
			}
			formPropertiesView = new FormPropertiesView(voTabFolder, SWT.NONE);
			cellPropTabItem.setControl(formPropertiesView);
			voTabFolder.setSelection(cellPropTabItem);
		}
	}
	
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet)
			return;
		if(FormEditor.getActiveEditor() != null)
			FormEditor.getActiveEditor().setCurrentSelection(selection);
		if (part == null || selection == null) {
			return;
		} else if(part instanceof FormEditor)
			dealFormEditor(selection);
	}
	

	private void dealFormEditor(ISelection selection){
		StructuredSelection ss = (StructuredSelection) selection;
		Object sel = ss.getFirstElement();
		if (sel instanceof LFWElementPart) {
			// (DatasetElementObj) dsobj = (DatasetElementObj)sel.;
			LFWElementPart lfwEle = (LFWElementPart) sel;
			Object model = lfwEle.getModel();
			if (model instanceof FormElementObj) {
				addFormPropItemControl();
				FormElementObj formobj = (FormElementObj) model;
				FormComp formcomp = formobj.getFormComp();
				formobj.setId(formcomp.getId());
				List<FormElement> datas = formcomp.getElementList();
				getFormPropertiesView().getTv().setInput(datas);
				getFormPropertiesView().setLfwElementPart(lfwEle);
				getFormPropertiesView().getTv().expandAll();
				sl.topControl = voTabFolder;
				//partName = "(属性)";
				//TODO guoweic 增加控件属性页内容
			
				// 清空listener属性页内容
				clearItemControl(getJsListenerItem());
				comp.layout();
				if (!comp.isVisible())
					comp.setVisible(true);
				
			} 
		}else if(sel instanceof LfwCanvasGraphPart){
			addEventPropertiesView(FormEditor.getActiveEditor().getFormElement().getFormComp().getEventConfs(), FormEditor.getActiveEditor().getWidget().getControllerClazz());
		}else {
			comp.setVisible(false);
		}
	}

}
