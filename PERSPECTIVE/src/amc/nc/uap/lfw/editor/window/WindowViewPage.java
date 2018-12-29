/**
 * 
 */
package nc.uap.lfw.editor.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.PagemetaElementPart;
import nc.lfw.editor.pagemeta.PagemetaGraphPart;
import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.pagemeta.RelationElementPart;
import nc.lfw.editor.pagemeta.RelationGraphPart;
import nc.lfw.editor.pagemeta.plug.ConnectorPropertiesView;
import nc.lfw.editor.pagemeta.plug.PmConnPropertiesView;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PluginDescItemPropertiesView;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescItemPropertiesView;
import nc.lfw.editor.widget.plug.PlugoutEmitItemPropertiesView;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.lang.M_editor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * @author chouhl
 *
 */
public class WindowViewPage extends LFWAbstractViewPage {
	
	private TabFolder plugoutFolder = null;
	private TabFolder pluginFolder = null;
	private TabItem plugoutDescTabItem = null;
	private TabItem plugoutEmitTabItem = null;
	private TabItem pluginDescTabItem = null;
	private TabItem connectorTabItem = null;
	private ConnectorPropertiesView connectorPropertiesView = null;
	private PmConnPropertiesView pmConnectorPropertiesView = null;
	
	private TabItem attrItem = null;
//	private EventPropertiesView eventPropertieView = null;

	private PlugoutDescItemPropertiesView  plugoutDescItemPropertiesView = null;
	private PlugoutEmitItemPropertiesView  plugoutEmitItemPropertiesView = null;
	private PluginDescItemPropertiesView  pluginDescItemPropertiesView = null;
	
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
		
		attrItem = new TabItem(voTabFolder, SWT.NONE);
		attrItem.setText(WEBPersConstants.ATTRIBUTE);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet){
			return;
		}
		// 为当前编辑器设置选中对象
		PagemetaEditor.getActiveEditor().setCurrentSelection(selection);
		if(part!=null && part instanceof MultiPageEditorPart){
			part = (IWorkbenchPart) ((MultiPageEditorPart)part).getSelectedPage();
		}
		if (part == null || selection == null) {
			return;
		} else if (part instanceof PagemetaEditor) {
			Composite comp = getComp();
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof PagemetaElementPart) {
				PagemetaElementPart lfwEle = (PagemetaElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof WidgetElementObj) {
					LfwWindow pageMeta = PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta();
					ViewConfig widgetConfig = pageMeta.getViewConfig(((WidgetElementObj) model).getWidget().getId());
//					ExtAttribute attr = widgetConfig.getExtendAttribute(LfwWindow.$QUERYTEMPLATE);
//					if(attr == null){
//						attr = new ExtAttribute();
//						attr.setKey(LfwWindow.$QUERYTEMPLATE);
//						widgetConfig.addAttribute(attr);
//					}
					addExtendAttributesView(widgetConfig);
					WidgetElementObj widget = (WidgetElementObj)model;
					List<Connector> connectorList = widget.getConnectorList();
					HashMap<String, Connector> connectMap =(HashMap)pageMeta.getConnectorMap();
					if (connectorList == null){
						connectorList = new ArrayList<Connector>();
						widget.setConnectorList(connectorList);
					}
					List<Connector> connList = new ArrayList<Connector>();
					for(Connector conn:connectorList){
						if(conn.getConnType()==null||conn.getConnType().equals("")||conn.getConnType().equals(Connector.VIEW_VIEW)) //$NON-NLS-1$
							connList.add(conn);
					}
					if(connectorTabItem != null)
						connectorTabItem.dispose();
					connectorTabItem = new TabItem(getVoTabFolder(), SWT.NONE);
					connectorTabItem.setText(M_editor.WindowViewPage_1);
					connectorPropertiesView = new ConnectorPropertiesView(getVoTabFolder(), SWT.NONE, true, widget);
					connectorPropertiesView.getTv().setInput(connList);
//					connectorPropertiesView.setApplicationPart(lfwEle);
					connectorPropertiesView.getTv().expandAll();
					connectorTabItem.setControl(connectorPropertiesView);
					getVoTabFolder().setSelection(connectorTabItem);
					
				}
				else if(model instanceof WindowObj){
					LfwWindow pageMetaConf = PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta();
					setWebElement(pageMetaConf);
					TabItem[] items = getVoTabFolder().getItems();
					if(items != null && items.length > 0){
						for(TabItem item : items){
							item.setControl(null);
						}
					}
//					ExtAttribute attr = pageMetaConf.getExtendAttribute(LfwWindow.$QUERYTEMPLATE);
//					if(attr == null){
//						attr = new ExtAttribute();
//						attr.setKey(LfwWindow.$QUERYTEMPLATE);
//						pageMetaConf.addAttribute(attr);
//					}
					addExtendAttributesView(pageMetaConf);
					addEventPropertiesView(pageMetaConf.getEventConfs(), pageMetaConf.getControllerClazz());
					
					
				}
			}
			else if (sel instanceof PagemetaGraphPart) {
				LfwWindow pageMetaConf = PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta();
				setWebElement(pageMetaConf);
				TabItem[] items = getVoTabFolder().getItems();
				if(items != null && items.length > 0){
					for(TabItem item : items){
						item.setControl(null);
					}
				}
//				ExtAttribute attr = pageMetaConf.getExtendAttribute(LfwWindow.$QUERYTEMPLATE);
//				if(attr == null){
//					attr = new ExtAttribute();
//					attr.setKey(LfwWindow.$QUERYTEMPLATE);
//					pageMetaConf.addAttribute(attr);
//				}
				addExtendAttributesView(pageMetaConf);
				addEventPropertiesView(pageMetaConf.getEventConfs(), pageMetaConf.getControllerClazz());
			}
//			else if (sel instanceof PlugoutDescElementPart) {
//				PlugoutDescElementPart eEle = (PlugoutDescElementPart)sel;
//				Object model = eEle.getModel();
//				PlugoutDescElementObj plugoutObj = (PlugoutDescElementObj)model;
//				List<PlugoutDescItem> descItems = ((PlugoutDesc)plugoutObj.getPlugout()).getDescItemList();
//				getSl().topControl = plugoutFolder;
//				plugoutDescItemPropertiesView = new PlugoutDescItemPropertiesView(plugoutFolder, SWT.NONE, false);
//				plugoutDescItemPropertiesView.getTv().setInput(descItems);
//				plugoutDescItemPropertiesView.setPlugoutDescElementPart(eEle);
//				plugoutDescItemPropertiesView.getTv().expandAll();
//				plugoutDescTabItem.setControl(plugoutDescItemPropertiesView);
//				
//				List<PlugoutEmitItem> emitItems = ((PlugoutDesc)plugoutObj.getPlugout()).getEmitItemList();
//				plugoutEmitItemPropertiesView = new PlugoutEmitItemPropertiesView(plugoutFolder, SWT.NONE, false);
//				plugoutEmitItemPropertiesView.getTv().setInput(emitItems);
//				plugoutEmitItemPropertiesView.setPlugoutDescElementPart(eEle);
//				plugoutEmitItemPropertiesView.getTv().expandAll();
//				plugoutEmitTabItem.setControl(plugoutEmitItemPropertiesView);
//				
//				plugoutFolder.setSelection(0);
//				if (comp != null) {
//					comp.layout();
//					if (!comp.isVisible()){
//						comp.setVisible(true);
//					}
//				}
//			} 
//			else if (sel instanceof PluginDescElementPart) {
//				PluginDescElementPart eEle = (PluginDescElementPart)sel;
//				Object model = eEle.getModel();
//				PluginDescElementObj pluginObj = (PluginDescElementObj)model;
//				List<PluginDescItem> descItems = ((PluginDesc)pluginObj.getPlugin()).getDescItemList();
//				getSl().topControl = pluginFolder;
//				pluginDescItemPropertiesView = new PluginDescItemPropertiesView(pluginFolder, SWT.NONE, false);
//				pluginDescItemPropertiesView.getTv().setInput(descItems);
//				pluginDescItemPropertiesView.setPluginDescElementPart(eEle);
//				pluginDescItemPropertiesView.getTv().expandAll();
//				pluginDescTabItem.setControl(pluginDescItemPropertiesView);
//				
//				pluginFolder.setSelection(0);
//				if (comp != null) {
//					comp.layout();
//					if (!comp.isVisible()){
//						comp.setVisible(true);
//					}
//				}
//			}
			else {
				comp.setVisible(false);
			}
		}
		else if(part instanceof RelationEditor){
			Composite comp = getComp();
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof RelationElementPart) {
				RelationElementPart lfwEle = (RelationElementPart) sel;
				Object model = lfwEle.getModel();
				if(model instanceof WindowObj){
					LfwWindow pageMetaConf = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
					setWebElement(pageMetaConf);
//					TabItem[] items = getVoTabFolder().getItems();
//					if(items != null && items.length > 0){
//						for(TabItem item : items){
//							item.setControl(null);
//						}
//					}
//					ExtAttribute attr = pageMetaConf.getExtendAttribute(LfwWindow.$QUERYTEMPLATE);
//					if(attr == null){
//						attr = new ExtAttribute();
//						attr.setKey(LfwWindow.$QUERYTEMPLATE);
//						pageMetaConf.addAttribute(attr);
//					}
					addExtendAttributesView(pageMetaConf);
					addEventPropertiesView(pageMetaConf.getEventConfs(), pageMetaConf.getControllerClazz());
					
					WindowObj window = (WindowObj)model;
					
					HashMap<String, Connector> connectMap =(HashMap)pageMetaConf.getConnectorMap();
					ArrayList connectorList = new ArrayList<Connector>();
					if(connectMap!=null&&connectMap.size()>0){
						Set<String> keySet = connectMap.keySet();						
						for(String key:keySet){
							Connector connectValue = connectMap.get(key);
							if(connectValue.getConnType()!=null&&(connectValue.getConnType().equals(Connector.VIEW_WINDOW)||connectValue.getConnType().equals(Connector.WINDOW_VIEW))){
								connectorList.add(connectValue);
							}
						}
					}
					if(connectorTabItem != null)
						connectorTabItem.dispose();
					connectorTabItem = new TabItem(getVoTabFolder(), SWT.NONE);
					connectorTabItem.setText(M_editor.WindowViewPage_2);
					pmConnectorPropertiesView = new PmConnPropertiesView(getVoTabFolder(), SWT.NONE, true, window);
					pmConnectorPropertiesView.getTv().setInput(connectorList);
//					connectorPropertiesView.setApplicationPart(lfwEle);
					pmConnectorPropertiesView.getTv().expandAll();
					connectorTabItem.setControl(pmConnectorPropertiesView);
					getVoTabFolder().setSelection(connectorTabItem);
				}
				else{
					TabItem[] items = getVoTabFolder().getItems();
					if(items != null && items.length > 0){
						for(TabItem item : items){
							clearItemControl(item);
							item.setControl(null);
						}
					}
				}
			}
			else if (sel instanceof RelationGraphPart) {
				LfwWindow pageMetaConf = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
				setWebElement(pageMetaConf);
				TabItem[] items = getVoTabFolder().getItems();
				if(items != null && items.length > 0){
					for(TabItem item : items){
						item.setControl(null);
					}
				}
//				ExtAttribute attr = pageMetaConf.getExtendAttribute(LfwWindow.$QUERYTEMPLATE);
//				if(attr == null){
//					attr = new ExtAttribute();
//					attr.setKey(LfwWindow.$QUERYTEMPLATE);
//					pageMetaConf.addAttribute(attr);
//				}
				addExtendAttributesView(pageMetaConf);
				addEventPropertiesView(pageMetaConf.getEventConfs(), pageMetaConf.getControllerClazz());
			}
			else if (sel instanceof PlugoutDescElementPart) {
				PlugoutDescElementPart eEle = (PlugoutDescElementPart)sel;
				Object model = eEle.getModel();
				PlugoutDescElementObj plugoutObj = (PlugoutDescElementObj)model;
				if(plugoutObj.getPlugout() instanceof PlugoutDesc){
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
				}
				else{
					getSl().topControl = null;
				}
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
				if(pluginObj.getPlugin() instanceof PluginDesc){
					List<PluginDescItem> descItems = ((PluginDesc)pluginObj.getPlugin()).getDescItemList();
	//				PluginDescItem[] descItems = pluginObj.getPlugin().getDescItems();
					
					getSl().topControl = pluginFolder;
					pluginDescItemPropertiesView = new PluginDescItemPropertiesView(pluginFolder, SWT.NONE, true);
					pluginDescItemPropertiesView.getTv().setInput(descItems);
					pluginDescItemPropertiesView.setPluginDescElementPart(eEle);
					pluginDescItemPropertiesView.getTv().expandAll();
					pluginDescTabItem.setControl(pluginDescItemPropertiesView);
					
					pluginFolder.setSelection(0);
				}
				else{
					getSl().topControl = null;
				}
				if (comp != null) {
					comp.layout();
					if (!comp.isVisible()){
						comp.setVisible(true);
					}
				}
			}
			else{
				comp.setVisible(false);
			}
		}
		// 显示属性
		IViewPart[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
		PropertySheet sheet = null;
		for (int i = 0, n = views.length; i < n; i++) {
			if(views[i] instanceof PropertySheet) {
				sheet = (PropertySheet) views[i];
				break;
			}
		}
		if (sheet != null) {
			StructuredSelection select = (StructuredSelection) selection;
			sheet.selectionChanged(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(), select);
		}
	}
	
	@Override
	public void addExtendAttributesView(WebElement webElement) {
		Composite comp = getComp();
		getSl().topControl = getVoTabFolder();
		TabItem[] items = getVoTabFolder().getItems();
		if(items != null && items.length > 0){
			for(TabItem item : items){
				item.setControl(null);
			}
		}
		// 增加扩展属性内容页
		addExtendAttributesItem(webElement);
		// 在扩展属性表格页面中，记录当前编辑器页面
		getExtendAttributesView().setEditor(PagemetaEditor.getActiveEditor());
		if (comp != null) {
			comp.layout();
			if (!comp.isVisible())
				comp.setVisible(true);
		}
	}

}
