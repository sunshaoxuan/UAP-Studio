package nc.uap.lfw.grid.core;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.lang.M_grid;
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
 * grid view page
 * @author zhangxya
 *
 */
public class GridViewPage  extends LFWAbstractViewPage { 

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private GridPropertisView gridPropertiesView = null;
	public GridPropertisView getGridPropertiesView() {
		return gridPropertiesView;
	}

	public void setGridPropertiesView(GridPropertisView gridPropertiesView) {
		this.gridPropertiesView = gridPropertiesView;
	}

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
		cellPropTabItem.setText(M_grid.GridViewPage_0);
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

	/**
	 * 增加gridEditor中属性页签内容
	 */
	private void addGridPropItemControl() {
		Control control = null;
		try { 
			control = cellPropTabItem.getControl();
		} catch (Exception e) {
			return;
		} finally {
				if(control instanceof CellPropertiesView){
				cellPropTabItem.setControl(null);
				
			}
			gridPropertiesView = new GridPropertisView(voTabFolder, SWT.NONE);
			cellPropTabItem.setControl(gridPropertiesView);
			voTabFolder.setSelection(cellPropTabItem);
		}
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet)
			return;
		// 为当前编辑器设置选中对象
		if(GridEditor.getActiveEditor() != null)
			GridEditor.getActiveEditor().setCurrentSelection(selection);
		//partName = "模型视图";
		if (part == null || selection == null) {
			return;
		} 
		else if(part instanceof GridEditor){
			dealGridEditor(selection);
		}
	}
	

	private void dealGridEditor(ISelection selection){
		StructuredSelection ss = (StructuredSelection) selection;
		Object sel = ss.getFirstElement();
		if (sel instanceof LFWElementPart) {
			// (DatasetElementObj) dsobj = (DatasetElementObj)sel.;
			LFWElementPart lfwEle = (LFWElementPart) sel;
			Object model = lfwEle.getModel();
			if (model instanceof GridElementObj) {
				addGridPropItemControl();
				GridElementObj gridobj = (GridElementObj) model;
				GridComp gridcomp = gridobj.getGridComp();
				gridobj.setId(gridcomp.getId());
				List<IGridColumn> datas = gridcomp.getColumnList();
				getGridPropertiesView().getTv().setInput(datas);
				getGridPropertiesView().setLfwElementPart(lfwEle);
				getGridPropertiesView().getTv().expandAll();
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
			addEventPropertiesView(GridEditor.getActiveEditor().getGridElement().getGridComp().getEventConfs(), GridEditor.getActiveEditor().getWidget().getControllerClazz());
		}else {
			comp.setVisible(false);
		}
	}
	
}
