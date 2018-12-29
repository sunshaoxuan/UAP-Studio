/**
 * 
 */
package nc.uap.lfw.editor.common.editor;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.chart.core.ChartGraphPart;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.lang.M_editor;
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
 * @author chouhl
 * 2011-11-17
 */
public class LFWBrowserViewPage extends LFWAbstractViewPage {
	
	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;

	@Override
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_editor.LFWBrowserViewPage_0);
		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		
		setCellPropertiesView(new CellPropertiesView(voTabFolder, SWT.NONE));
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		EventConf[] eventConfs = null;
		String controllerClazz = null;
		if (part instanceof PropertySheet)
			return;
		//partName = "模型视图";
		if (part == null || selection == null) {
			return;
		}else if(part instanceof LFWBrowserEditor){
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if(sel instanceof LfwCanvasGraphPart){
				addEventPropertiesView(eventConfs, controllerClazz);
			}else if(sel instanceof ChartGraphPart){
				addEventPropertiesView(eventConfs, controllerClazz);
			}
		}
	}

	@Override
	public TabItem getJsListenerItem() {
		return this.cellPropTabItem;
	}
	
	@Override
	public Control getControl() {
		return this.comp;
	}
	
	@Override
	public void setFocus() {
		getControl().setFocus();
	}
	
}
