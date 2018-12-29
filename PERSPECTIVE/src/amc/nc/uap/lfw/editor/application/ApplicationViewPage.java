/**
 * 
 */
package nc.uap.lfw.editor.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.editor.application.plug.ConnectorPropertiesView;
import nc.uap.lfw.lang.M_application;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * 
 * Application属性视图界面类
 * @author chouhl
 *
 */
public class ApplicationViewPage extends LFWAbstractViewPage {
	
//	private EventPropertiesView eventPropertieView = null;

	private TabItem connectorTabItem = null;
	
	private ConnectorPropertiesView connectorPropertiesView = null;
	
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setComp(comp);
		comp.setLayout(getSl());
		
		TabFolder voTabFolder = new TabFolder(comp, SWT.NONE);
		setVoTabFolder(voTabFolder);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection){
		if (part instanceof PropertySheet){
			return;
		}
		// 为当前编辑器设置选中对象
		ApplicationEditor.getActiveEditor().setCurrentSelection(selection);
		if (part == null || selection == null) {
			return;
		} else if (part instanceof ApplicationEditor) {
			Composite comp = getComp();
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			TabItem[] items = getVoTabFolder().getItems();
			if(items != null && items.length > 0){
				for(TabItem item : items){
					item.setControl(null);
				}
			}
			if (sel instanceof ApplicationPart) {
				ApplicationPart lfwEle = (ApplicationPart) sel;
				Object model = lfwEle.getModel();
//				if (model instanceof ApplicationObj || model instanceof WindowObj) {
//					getSl().topControl = getVoTabFolder();
//					// 清空listener属性页内容
//					clearItemControl(getJsListenerItem());
//					comp.layout();
//					if (!comp.isVisible()){
//						comp.setVisible(true);
//					}
//				}
				getSl().topControl = getVoTabFolder();
				
				// 清空listener属性页内容
//				clearItemControl(getJsListenerItem());
				comp.layout();
				if (!comp.isVisible()){
					comp.setVisible(true);
				}
				if (model instanceof ApplicationObj){
					//增加plug页
					Application appConf = ApplicationEditor.getActiveEditor().getGraph().getApplication();
					List<Connector> connectorList = new ArrayList(Arrays.asList(appConf.getConnectors()));
	
					if (connectorTabItem == null){
						connectorTabItem = new TabItem(getVoTabFolder(), SWT.NONE);
						connectorTabItem.setText(M_application.ApplicationViewPage_0);
					}
					connectorPropertiesView = new ConnectorPropertiesView(getVoTabFolder(), SWT.NONE, true, lfwEle);
					connectorPropertiesView.getTv().setInput(connectorList);
	//				connectorPropertiesView.setApplicationPart(lfwEle);
					connectorPropertiesView.getTv().expandAll();
					connectorTabItem.setControl(connectorPropertiesView);
					getVoTabFolder().setSelection(connectorTabItem);
				}
			} 
			else if (sel instanceof ApplicationGraphPart) {
				Application appConf = ApplicationEditor.getActiveEditor().getGraph().getApplication();
				setWebElement(appConf);
				addEventPropertiesView(appConf.getEventConfs(), appConf.getControllerClazz());
			} 
			
			else {
				comp.setVisible(false);
			}
		}
	}
	
//	public void addEventPropertiesView(EventConf[] eventConfs, String controllerClazz){
//		Composite comp = getComp();
//		getSl().topControl = getVoTabFolder();
//		// 增加事件内容页
//		TabItem jsListenerItem = getJsListenerItem();
//		if (jsListenerItem == null) {
//			jsListenerItem = new TabItem(getVoTabFolder(), SWT.NONE);
//			jsListenerItem.setText("事件");
//			setJsListenerItem(jsListenerItem);
//		}
//		eventPropertieView = new EventPropertiesView(getVoTabFolder(), SWT.NONE, eventConfs, controllerClazz, getWebElement());
//		jsListenerItem.setControl(eventPropertieView);
//		getVoTabFolder().setSelection(jsListenerItem);
//		// 清空控件属性页内容
////		clearItemControl(cellPropTabItem);
//		// 在listener属性表格页面中，记录当前编辑器页面
//		eventPropertieView.setEditor((LFWBaseEditor) LFWBaseEditor.getActiveEditor());
//		if (comp != null) {
//			comp.layout();
//			if (!comp.isVisible()){
//				comp.setVisible(true);
//			}
//		}
//	}
//
//	public EventPropertiesView getEventPropertieView() {
//		return eventPropertieView;
//	}
//
//	public void setEventPropertieView(EventPropertiesView eventPropertieView) {
//		this.eventPropertieView = eventPropertieView;
//	}
	
}
