/**
 * 
 */
package nc.uap.lfw.editor.view;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.WidgetElementPart;
import nc.lfw.editor.widget.WidgetGraphPart;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PluginDescItemPropertiesView;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescItemPropertiesView;
import nc.lfw.editor.widget.plug.PlugoutEmitItemPropertiesView;
import nc.lfw.editor.widget.plug.WidgetConnPropertiesView;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;
import nc.uap.lfw.lang.M_editor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * @author chouhl
 *
 */
public class ViewViewPage extends LFWAbstractViewPage {
	
	private TabFolder plugoutFolder = null;
	private TabFolder pluginFolder = null;
	private TabItem plugoutDescTabItem = null;
	private TabItem plugoutEmitTabItem = null;
	private TabItem pluginDescTabItem = null;
	
//	private EventPropertiesView eventPropertieView = null;
	
	private PlugoutDescItemPropertiesView  plugoutDescItemPropertiesView = null;
	private PlugoutEmitItemPropertiesView  plugoutEmitItemPropertiesView = null;
	private PluginDescItemPropertiesView  pluginDescItemPropertiesView = null;
//	private PublishingEventPropertiesView  publishingEventView = null;
	

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setComp(comp);
		comp.setLayout(getSl());
		plugoutFolder = new TabFolder(comp, SWT.NONE);
		pluginFolder = new TabFolder(comp, SWT.NONE);
		
		plugoutDescTabItem = new TabItem(plugoutFolder, SWT.NONE);
		plugoutDescTabItem.setText(WEBPersConstants.PLUGOUTDESC);
		
		plugoutEmitTabItem = new TabItem(plugoutFolder, SWT.NONE);
		plugoutEmitTabItem.setText(WEBPersConstants.PLUGOUTEMIT);
		
		pluginDescTabItem = new TabItem(pluginFolder, SWT.NONE);
		pluginDescTabItem.setText(WEBPersConstants.PLUGINDESC);
		
		TabFolder voTabFolder = new TabFolder(comp, SWT.NONE);
		setVoTabFolder(voTabFolder);
		
		
//		setVoTabFolder(voTabFolder);
//		
//		// InitParam属性页
//		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
//		cellPropTabItem.setText("InitParam");
//		
//		//Supports属性页
//		supportsTabItem = new TabItem(voTabFolder, SWT.NONE);
//		supportsTabItem.setText("Supports");
//
//		setCellPropTabItem(cellPropTabItem);
//		addCellPropItemControl();
//		sl.topControl = voTabFolder;
//		comp.layout();
		
//		setCellPropertiesView(new InitParamPropertiesView(voTabFolder, SWT.NONE));
		
	}
	
	private TabItem connectorTabItem = null;

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet)
			return;
		// 为当前编辑器设置选中对象
		WidgetEditor.getActiveWidgetEditor().setCurrentSelection(selection);
		if (part == null || selection == null) {
			return;
		} else if (part instanceof WidgetEditor) {
			Composite comp = getComp();
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof WidgetElementPart) {
				WidgetElementPart lfwEle = (WidgetElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof WidgetElementObj) {
					getSl().topControl = getVoTabFolder();
					TabItem[] items = getVoTabFolder().getItems();
					if(items != null && items.length > 0){
						for(TabItem item : items){
							item.setControl(null);
						}
					}	
					// 清空listener属性页内容
					clearItemControl(getJsListenerItem());
					comp.layout();
					if (!comp.isVisible()){
						comp.setVisible(true);
					}
									
					LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
					
					addEventPropertiesView(widget.getEventConfs(), widget.getControllerClazz());
					
					List<Connector> connectorList = widget.getConnectorList();
					
					if (connectorTabItem == null){
						connectorTabItem = new TabItem(getVoTabFolder(), SWT.NONE);
						connectorTabItem.setText(M_editor.ViewViewPage_0);
					}
					WidgetConnPropertiesView widgetConnPropertiesView = null;
					WidgetElementObj widgetObj = (WidgetElementObj)model;
					widgetConnPropertiesView = new WidgetConnPropertiesView(getVoTabFolder(), SWT.NONE, true, widgetObj);
					widgetConnPropertiesView.getTv().setInput(connectorList);
//			
					widgetConnPropertiesView.getTv().expandAll();
					connectorTabItem.setControl(widgetConnPropertiesView);
					getVoTabFolder().setSelection(connectorTabItem);
				}
				else{
					getSl().topControl = getVoTabFolder();
					TabItem[] items = getVoTabFolder().getItems();
					if(items != null && items.length > 0){
						for(TabItem item : items){
							item.setControl(null);
						}
					}	
				}
				
			} else if (sel instanceof WidgetGraphPart) {
				LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
				setWebElement(widget);
				addEventPropertiesView(widget.getEventConfs(), widget.getControllerClazz());
			} 
			else if (sel instanceof PlugoutDescElementPart) {
				PlugoutDescElementPart eEle = (PlugoutDescElementPart)sel;
				Object model = eEle.getModel();
				PlugoutDescElementObj plugoutObj = (PlugoutDescElementObj)model;
				List<PlugoutDescItem> descItems = ((PlugoutDesc)plugoutObj.getPlugout()).getDescItemList();
			
				getSl().topControl = plugoutFolder;
				plugoutDescItemPropertiesView = new PlugoutDescItemPropertiesView(plugoutFolder, SWT.NONE, true);
				plugoutDescItemPropertiesView.getTv().setInput(descItems);
				plugoutDescItemPropertiesView.setPlugoutDescElementPart(eEle);
				plugoutDescItemPropertiesView.getTv().expandAll();
				plugoutDescTabItem.setControl(plugoutDescItemPropertiesView);
				
				List<PlugoutEmitItem> emitItems = ((PlugoutDesc)plugoutObj.getPlugout()).getEmitItemList();
				plugoutEmitItemPropertiesView = new PlugoutEmitItemPropertiesView(plugoutFolder, SWT.NONE, true);
				plugoutEmitItemPropertiesView.getTv().setInput(emitItems);
				plugoutEmitItemPropertiesView.setPlugoutDescElementPart(eEle);
				plugoutEmitItemPropertiesView.getTv().expandAll();
				plugoutEmitTabItem.setControl(plugoutEmitItemPropertiesView);
				
				plugoutFolder.setSelection(0);
				if (comp != null) {
					comp.layout();
					if (!comp.isVisible()){
						comp.setVisible(true);
					}
				}
			} 
			else if (sel instanceof PluginDescElementPart) {
				PluginDescElementPart eEle = (PluginDescElementPart)sel;
				Object model = eEle.getModel();
				PluginDescElementObj pluginObj = (PluginDescElementObj)model;
				List<PluginDescItem> descItems = ((PluginDesc)pluginObj.getPlugin()).getDescItemList();
				getSl().topControl = pluginFolder;
				pluginDescItemPropertiesView = new PluginDescItemPropertiesView(pluginFolder, SWT.NONE, true);
				pluginDescItemPropertiesView.getTv().setInput(descItems);
				pluginDescItemPropertiesView.setPluginDescElementPart(eEle);
				pluginDescItemPropertiesView.getTv().expandAll();
				pluginDescTabItem.setControl(pluginDescItemPropertiesView);
				
				pluginFolder.setSelection(0);
				if (comp != null) {
					comp.layout();
					if (!comp.isVisible()){
						comp.setVisible(true);
					}
				}
			} 
			else {
				comp.setVisible(false);
			}
		}
	}
	
//	@Override
//	protected void addJsListenerItem(JsListenerConf jsListener) {
//		TabItem jsListenerItem = getJsListenerItem();
//		if (jsListenerItem == null) {
//			jsListenerItem = new TabItem(getVoTabFolder(), SWT.NONE);
//			jsListenerItem.setText("事件");
//			setJsListenerItem(jsListenerItem);
//		}
////		listenerPropertieView = new ListenerPropertiesView(getVoTabFolder(), SWT.NONE, jsListener);
//		jsListenerItem.setControl(eventPropertieView);
//		getVoTabFolder().setSelection(jsListenerItem);
//	}
//	
//	@Override
//	public void addListenerPropertiesView(JsListenerConf jsListener) {
//		Composite comp = getComp();
//		getSl().topControl = getVoTabFolder();
//		// 增加事件内容页
//		addJsListenerItem(jsListener);
//		// 清空控件属性页内容
////		clearItemControl(cellPropTabItem);
//		// 在listener属性表格页面中，记录当前编辑器页面
//		getListenerPropertieView().setEditor((LFWBaseEditor) LFWBaseEditor.getActiveEditor());
//		if (comp != null) {
//			comp.layout();
//			if (!comp.isVisible()){
//				comp.setVisible(true);
//			}
//		}
//	}
	
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
