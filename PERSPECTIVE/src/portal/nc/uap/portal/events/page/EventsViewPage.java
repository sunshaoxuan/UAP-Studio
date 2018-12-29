package nc.uap.portal.events.page;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.events.EventsEditor;
import nc.uap.portal.events.EventsElementObj;
import nc.uap.portal.events.EventsElementPart;
import nc.uap.portal.events.page.EventsPropertiesView;
import nc.uap.portal.lang.M_portal;

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
 * 事件 模型视图
 * 
 * @author dingrf
 * 
 */
public class EventsViewPage extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_portal.EventsViewPage_0);
		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		
		setCellPropertiesView(new EventsPropertiesView(voTabFolder, SWT.NONE));
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
		} 
		else if (part instanceof EventsEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof EventsElementPart) {
				EventsElementPart eEle = (EventsElementPart) sel;
				Object model = eEle.getModel();
				if (model instanceof EventsElementObj) {
					EventsElementObj eeobj = (EventsElementObj) model;
					List<EventDefinition> eds = eeobj.getEventDefinitions();
					((EventsPropertiesView) getCellPropertiesView()).getTv().setInput(eds);
					((EventsPropertiesView) getCellPropertiesView()).setEventsElementPart(eEle);
					((EventsPropertiesView) getCellPropertiesView()).getTv().expandAll();
					
					sl.topControl = voTabFolder;
					// 增加控件属性页内容
					setCellPropTabItem(cellPropTabItem);
					addCellPropItemControl();
					comp.layout();
					if (!comp.isVisible())
						comp.setVisible(true);
					
				}
			} else {
				comp.setVisible(false);
			}
		}
	}
}
