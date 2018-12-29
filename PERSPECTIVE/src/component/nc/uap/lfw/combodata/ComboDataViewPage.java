package nc.uap.lfw.combodata;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.combodata.core.ComboDataPropertiesView;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.lang.M_combodata;
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
 * 下拉框属性页面
 * @author zhangxya
 *
 */
public class ComboDataViewPage extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private ComboDataPropertiesView comboPropertiesView = null;
	
	public void createControl(Composite parent) {

		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_combodata.ComboDataViewPage_0);
		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		
		setCellPropertiesView(new CellPropertiesView(voTabFolder, SWT.NONE));
//		cellPropertiesView = new CellPropertiesView(voTabFolder, SWT.NONE);
//		ti1.setControl(cellPropertiesView);
		
	}

	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet)
			return;
		if (part == null || selection == null) {
			return;
		} else if(part instanceof ComboDataEditor)
			dealComboEditor(selection);
	}
	
	
	public ComboDataPropertiesView getComboPropertiesView() {
		return comboPropertiesView;
	}

	public void setComboPropertiesView(ComboDataPropertiesView comboPropertiesView) {
		this.comboPropertiesView = comboPropertiesView;
	}

	private void addComboPropItemControl() {
		Control control = null;
		try { 
			control = cellPropTabItem.getControl();
		} catch (Exception e) {
			return;
		} finally {
			if(control instanceof CellPropertiesView){
				cellPropTabItem.setControl(null);
				comboPropertiesView = new ComboDataPropertiesView(voTabFolder, SWT.NONE);
				cellPropTabItem.setControl(comboPropertiesView);
			}else{
				comboPropertiesView = new ComboDataPropertiesView(voTabFolder, SWT.NONE);
				cellPropTabItem.setControl(comboPropertiesView);
			}
			voTabFolder.setSelection(cellPropTabItem);
		}
	}
	
	
	private void dealComboEditor(ISelection selection){
		StructuredSelection ss = (StructuredSelection) selection;
		Object sel = ss.getFirstElement();
		if (sel instanceof LFWElementPart) {
			LFWElementPart lfwEle = (LFWElementPart) sel;
			Object model = lfwEle.getModel();
			if (model instanceof ComboDataElementObj) {
				ComboDataElementObj comboDataObj = (ComboDataElementObj)model;
				if(comboDataObj.getCombodata() instanceof StaticComboData){
					addComboPropItemControl();
					ComboDataElementObj combobj = (ComboDataElementObj) model;
					ComboData combodata = combobj.getCombodata();
					combobj.setId(combodata.getId());
					List<CombItem> datas = new ArrayList<CombItem>();
					CombItem[] items = combodata.getAllCombItems();
					if(items != null){
						for (int i = 0; i < items.length; i++) {
							datas.add(items[i]);
						}
					}
					comboPropertiesView.getTv().setInput(datas);
					getComboPropertiesView().setLfwElementPart(lfwEle);
					getComboPropertiesView().getTv().expandAll();
					sl.topControl = voTabFolder;
					//partName = "(属性)";
					//TODO guoweic 增加控件属性页内容
				
					// 清空listener属性页内容
					clearItemControl(getJsListenerItem());
					comp.layout();
					if (!comp.isVisible())
						comp.setVisible(true);
					} 
			}
		} else {
			comp.setVisible(false);
		}
	}
}
