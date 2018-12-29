/**
 * 
 */
package nc.uap.lfw.editor.propertiesview;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.chart.core.ChartEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.EventUtil;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.common.tools.ViewEventTool;
import nc.uap.lfw.editor.publicview.PublicViewBrowserEditor;
import nc.uap.lfw.editor.view.ViewBrowserEditor;
//import nc.uap.lfw.excel.core.ExcelEditor;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.iframe.IFrameEditor;
import nc.uap.lfw.image.ImageEditor;
import nc.uap.lfw.label.LabelEditor;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.linkcomp.LinkCompEditor;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.progressbar.ProgressBarEditor;
import nc.uap.lfw.selfdefcomp.core.SelfDefCompEditor;
import nc.uap.lfw.textcomp.TextCompEditor;
import nc.uap.lfw.toolbar.ToolBarEditor;
import nc.uap.lfw.tree.core.TreeEditor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author chouhl
 *
 */
public class OperateEventAction extends NodeAction {

	private EventPropertiesView view = null;
	
	private String controllerClazz = null;
	
	private int actionType = 0;
	
	private String sourcePackage = null;

	public OperateEventAction(int actionType, EventPropertiesView view, String controllerClazz){
		this.actionType = actionType;
		this.view = view;
		this.controllerClazz = controllerClazz;
		switch(actionType){
			case 1:
				setText(WEBPersConstants.ADD);
				setToolTipText(WEBPersConstants.ADD);
				break;
			case 2:
				setText(WEBPersConstants.DELETE);
				setToolTipText(WEBPersConstants.DELETE);
				break;
			default:
				break;
		}
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	
	@Override
	public void run() {
		switch(actionType){
			case 1:
				addEvent();
				break;
			case 2:
				deleteEvent();
				break;
			default:
				break;
		}
	}
	
	private void addEvent(){
		List<JsEventDesc> eventList = view.getEditor().getAcceptEventDescs();
		if(eventList == null){
			if(view.getWebElement() != null){
				eventList = view.getWebElement().getAcceptEventDescs();
			}else if(view.getUiElement() != null){
				eventList = EventUtil.createAcceptEventDescs(view.getUiElement());
			}
		}
		AddEventPropertiesDialog dialog = new AddEventPropertiesDialog(LFWTool.getAcceptEventList(eventList));
		if(dialog.open() == IDialogConstants.OK_ID){
			EventConf eventConf = dialog.getEventConf();
			AddEventMethodNameDialog d = new AddEventMethodNameDialog(WEBPersConstants.ADD);
			d.setName(eventConf.getName());
			if(d.open() == IDialogConstants.OK_ID){
				TableItem[] tabs = view.getTv().getTable().getItems();
				for(TableItem tb:tabs){
					if(tb.getText().equals(d.getName())){
						MessageDialog.openError(null, M_editor.OperateEventAction_0, d.getName() + M_editor.OperateEventAction_1);
						return;
					}						
				}
				eventConf.setMethodName(d.getName());
				if(!checkEventMethodName(eventConf)){
					operateEventMethod(eventConf);
					view.getTv().add(eventConf);
				}else{
					MessageDialog.openError(null, M_editor.OperateEventAction_0, eventConf.getMethodName() + M_editor.OperateEventAction_2);
				}
			}
		}
	}
	
	private void deleteEvent() {
		IStructuredSelection selection = (IStructuredSelection) view.getTv().getSelection();
		EventConf eventConf = (EventConf) selection.getFirstElement();
		if(eventConf != null){
			EventConf clone = (EventConf)eventConf.clone();
			if(checkEventMethodName(clone)){
				operateEventMethod(clone);
				view.getTv().remove(eventConf);
			}else{
				view.getTv().remove(eventConf);
				view.getWebElement().removeEventConf(clone.getName(),clone.getMethodName());
				MessageDialog.openError(null, M_editor.OperateEventAction_0, eventConf.getName() + M_editor.OperateEventAction_3);
			}	
		}
	}
	
	private boolean checkEventMethodName(EventConf eventConf){
		boolean exist = false;
		//WebElement对象包含的事件
		EventConf[] eventConfs = null;
		//通过服务获取WebElement对象
		if(view.getEditor() instanceof ApplicationEditor){//ApplicationController
			Application app = ((ApplicationEditor)view.getEditor()).getGraph().getApplication();
			if(app == null){
				app = LFWAMCConnector.getApplication(LFWAMCPersTool.getCurrentFolderPath(), WEBPersConstants.AMC_APPLICATION_FILENAME);
				((ApplicationEditor)view.getEditor()).getGraph().setApplication(app);
			}
			eventConfs = app.getEventConfs();
			sourcePackage = app.getSourcePackage();
			controllerClazz = app.getControllerClazz();
		}
		else if(view.getEditor() instanceof PagemetaEditor){//WindowController
			LfwWindow winConf = ((PagemetaEditor)view.getEditor()).getGraph().getPagemeta();
			if(winConf == null){
				winConf = LFWAMCConnector.getWindow(LFWAMCPersTool.getCurrentFolderPath(), WEBPersConstants.AMC_WINDOW_FILENAME);
				((PagemetaEditor)view.getEditor()).getGraph().setPagemeta(winConf);
			}
			eventConfs = winConf.getEventConfs();
			sourcePackage = winConf.getSourcePackage();
			controllerClazz = winConf.getControllerClazz();
		}else if(view.getEditor() instanceof LFWBaseEditor){//ViewController
			LfwView viewConf = null;
			if(view.getEditor() instanceof WidgetEditor){
				viewConf = ((WidgetEditor)view.getEditor()).getGraph().getWidget();
				if(viewConf == null){
					viewConf = LFWAMCConnector.getView(LFWAMCPersTool.getCurrentFolderPath(), WEBPersConstants.AMC_VIEW_FILENAME);
					((WidgetEditor)view.getEditor()).getGraph().setWidget(viewConf);
				}
			}else if(view.getEditor() instanceof ViewBrowserEditor){
				ViewBrowserEditor vbe = (ViewBrowserEditor)view.getEditor();
				viewConf = vbe.getView();
			}else if(view.getEditor() instanceof PublicViewBrowserEditor){
				PublicViewBrowserEditor pvbe = (PublicViewBrowserEditor)view.getEditor();
				viewConf = pvbe.getView();
			}else if(view.getEditor().getEditorInput() instanceof WidgetEditorInput){
				viewConf = ((WidgetEditorInput)view.getEditor().getEditorInput()).getWidget();
			}
			if(viewConf == null){
				viewConf = LFWAMCConnector.getView(LFWAMCPersTool.getCurrentFolderPath(), WEBPersConstants.AMC_VIEW_FILENAME);
			}
			if(viewConf == null){
				MainPlugin.getDefault().logError("LfwWidget对象为空!"); //$NON-NLS-1$
				throw new LfwRuntimeException("添加事件失败:源文件夹或controller类为空!"); //$NON-NLS-1$
			}
			sourcePackage = viewConf.getSourcePackage();
			controllerClazz = viewConf.getControllerClazz();
			eventConfs = ViewEventTool.getAllEvents(viewConf, view.getUimeta());
		}
		if(eventConfs != null && eventConfs.length > 0){
			for(EventConf event : eventConfs){
				switch(event.getEventStatus()){
					case EventConf.NORMAL_STATUS:
					case EventConf.ADD_STATUS:
						if(event.getMethodName().equals(eventConf.getMethodName())){
							exist = true;
							break;
						}
						break;
					case EventConf.DEL_STATUS:
						break;
					default:
						break;
				}
			}
		}
		return exist;
	}
	
	private void operateEventMethod(EventConf eventConf){
		if(sourcePackage != null && sourcePackage.trim().length() > 0 && controllerClazz != null && controllerClazz.trim().length() > 0){
			String projectPath = LFWAMCPersTool.getLFWProjectPath();
			int index = controllerClazz.lastIndexOf("."); //$NON-NLS-1$
			String packageName = null;
			if(index > 0){
				packageName = controllerClazz.substring(0, index);
			}else{
				packageName = ""; //$NON-NLS-1$
			}
			String className = controllerClazz.substring(index + 1);
			String classFilePath = projectPath + File.separator + sourcePackage + packageName.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			String classFileName = className + ".java"; //$NON-NLS-1$
			eventConf.setClassFilePath(classFilePath);
			eventConf.setClassFileName(classFileName);
		}else{
			MainPlugin.getDefault().logError("添加事件失败:源文件夹或controller类为空!"); //$NON-NLS-1$
			throw new LfwRuntimeException("添加事件失败:源文件夹或controller类为空!"); //$NON-NLS-1$
		}
		if(view.getUiElement() == null){
			WebElement we = null;
			if(view.getEditor() instanceof ApplicationEditor){
				ApplicationEditor appEditor = ApplicationEditor.getActiveEditor();
				we = appEditor.getGraph().getApplication();
				operateEventConf(we, eventConf);
			}else if(view.getEditor() instanceof PagemetaEditor){
				PagemetaEditor pagemetaEditor = (PagemetaEditor)view.getEditor();
				we = pagemetaEditor.getGraph().getPagemeta();
				operateEventConf(we, eventConf);
			}else if(view.getEditor() instanceof WidgetEditor){
				WidgetEditor widgetEditor = (WidgetEditor)view.getEditor();
				we = widgetEditor.getGraph().getWidget();
				operateEventConf(we, eventConf);
			}else if(view.getEditor() instanceof ViewBrowserEditor){
				operateEventConf(view.getWebElement(), eventConf);
			}else if(view.getEditor() instanceof PublicViewBrowserEditor){
				operateEventConf(view.getWebElement(), eventConf);
			}else if(view.getEditor() instanceof GridEditor){
				//获取Editor
				GridEditor ge = (GridEditor)view.getEditor();
				//获取WebElement
				GridComp gc = ge.getGridElement().getGridComp();
				//处理事件
				operateEventConf(gc, eventConf);
			}else if(view.getEditor() instanceof FormEditor){
				FormEditor fe = ((FormEditor)view.getEditor());
				FormComp fc = fe.getFormElement().getFormComp();
				operateEventConf(fc, eventConf);
			}else if(view.getEditor() instanceof ButtonEditor){
				ButtonEditor be = ((ButtonEditor)view.getEditor());
				ButtonComp bc = be.getButtonElement().getButtonComp();
				operateEventConf(bc, eventConf);
			}else if(view.getEditor() instanceof TreeEditor){
				TreeEditor te = ((TreeEditor)view.getEditor());
				TreeViewComp tvc = te.getTreeElement().getTreeComp();
				operateEventConf(tvc, eventConf);
			}else if(view.getEditor() instanceof ImageEditor){
				ImageEditor ie = ((ImageEditor)view.getEditor());
				ImageComp ic = ie.getImageElement().getImageComp();
				operateEventConf(ic, eventConf);
			}else if(view.getEditor() instanceof LabelEditor){
				LabelEditor le = ((LabelEditor)view.getEditor());
				LabelComp lc = le.getLabelElement().getLabelComp();
				operateEventConf(lc, eventConf);
			}else if(view.getEditor() instanceof TextCompEditor){
				TextCompEditor tce = ((TextCompEditor)view.getEditor());
				TextComp tc = tce.getTextElement().getTextComp();
				operateEventConf(tc, eventConf);
//			}else if(view.getEditor() instanceof ExcelEditor){
//				ExcelEditor ee = ((ExcelEditor)view.getEditor());
//				ExcelComp ec = ee.getExcelElement().getExcelComp();
//				operateEventConf(ec, eventConf);
			}else if(view.getEditor() instanceof IFrameEditor){
				IFrameEditor ife = ((IFrameEditor)view.getEditor());
				IFrameComp ifc = ife.getIframeElement().getIframecomp();
				operateEventConf(ifc, eventConf);
			}else if(view.getEditor() instanceof ToolBarEditor){
				ToolBarEditor tbe = ((ToolBarEditor)view.getEditor());
				ToolBarComp tbc = tbe.getToolbarElement().getToolbarComp();
				operateEventConf(tbc, eventConf);
			}else if(view.getEditor() instanceof LinkCompEditor){
				LinkCompEditor lce = ((LinkCompEditor)view.getEditor());
				LinkComp lc = lce.getLinkElement().getLinkComp();
				operateEventConf(lc, eventConf);
			}else if(view.getEditor() instanceof ProgressBarEditor){
				ProgressBarEditor pbe = ((ProgressBarEditor)view.getEditor());
				ProgressBarComp pbc = pbe.getProgressElement().getProgressBar();
				operateEventConf(pbc, eventConf);
			}else if(view.getEditor() instanceof ChartEditor){
				ChartEditor ce = ((ChartEditor)view.getEditor());
				ChartComp cc = ce.getChartElement().getChartComp();
				operateEventConf(cc, eventConf);
			}else if(view.getEditor() instanceof SelfDefCompEditor){
				SelfDefCompEditor sce = ((SelfDefCompEditor)view.getEditor());
				SelfDefComp sc = sce.getSelfdefElement().getSelfDefComp();
				operateEventConf(sc, eventConf);
			}else if(view.getEditor() instanceof MenubarEditor){
				MenubarEditor me = (MenubarEditor)view.getEditor();
				MenubarComp mc = me.getMenubarTemp();
				if(mc != null && view.getWebElement() instanceof MenuItem){
					MenuItem item = (MenuItem)view.getWebElement();
					List<MenuItem> miList = mc.getMenuList();
					if(miList != null && miList.size() > 0){
						for(MenuItem mi : miList){
//							if(mi.getId().equals(item.getId())){
//								operateEventConf(mi, eventConf);
//								break;
//							}
							int flag = operateMenuEventConf(mi, item, eventConf);
							if(flag == 1)
								break;
						}
					}else{
						miList = new ArrayList<MenuItem>();
						MenuItem clone = (MenuItem)item.clone();
						operateEventConf(clone, eventConf);
						miList.add(clone);
						mc.setMenuList(miList);
					}
				}else{
					MainPlugin.getDefault().logError("MenuItem对象转换失败!"); //$NON-NLS-1$
					throw new LfwRuntimeException("MenuItem对象转换失败!"); //$NON-NLS-1$
				}
			}else if(view.getEditor() instanceof ContextMenuEditor){
				ContextMenuEditor cme = (ContextMenuEditor)view.getEditor();
				ContextMenuComp cmc = cme.getMenubarTemp();
				if(cmc != null && view.getWebElement() instanceof MenuItem){
					MenuItem item = (MenuItem)view.getWebElement();
					List<MenuItem> miList = cmc.getItemList();
					if(miList != null && miList.size() > 0){
						for(MenuItem mi : miList){
//							if(mi.getId().equals(item.getId())){
//								operateEventConf(mi, eventConf);
//								break;
//							}
							int flag = operateMenuEventConf(mi, item, eventConf);
							if(flag == 1)
								break;
						}
					}else{
						miList = new ArrayList<MenuItem>();
						MenuItem clone = (MenuItem)item.clone();
						operateEventConf(clone, eventConf);
						miList.add(clone);
						cmc.setItemList(miList);
					}
				}else{
					MainPlugin.getDefault().logError("MenuItem对象转换失败!"); //$NON-NLS-1$
					throw new LfwRuntimeException("MenuItem对象转换失败!"); //$NON-NLS-1$
				}
			}
//			else if(view.getEditor() instanceof TabEditor){
//				TabEditor te = ((TabEditor)view.getEditor());
//				UITabComp uiComp = te.getTabElement().getTabComp();
//				operateEventConf(uiComp, eventConf);
//			}else if(view.getEditor() instanceof CardLayoutEditor){
//				CardLayoutEditor cle = ((CardLayoutEditor)view.getEditor());
//				CardLayout cl = cle.getCardElement().getCardComp();
//				operateEventConf(cl, eventConf);
//			}
			else if(view.getEditor() instanceof DataSetEditor){
				DataSetEditor dse = ((DataSetEditor)view.getEditor());
				Dataset ds = dse.getDsElementObj().getDs();
				operateEventConf(ds, eventConf);
			}
		}else{
			//UIElement事件
			EventConf[] events = view.getUiElement().getEventConfs();
			events = addEventConf(events, (EventConf)eventConf.clone());
			view.getUiElement().removeAllEventConf();
			for(EventConf event : events){
				view.getUiElement().addEventConf(event);
			}
			//UIMeta事件
			events = view.getUimeta().getEventConfs();
			events = addEventConf(events, (EventConf)eventConf.clone());
			view.getUimeta().removeAllEventConf();
			for(EventConf event : events){
				view.getUimeta().addEventConf(event);
			}
		}
		LFWBaseEditor.getActiveEditor().setDirtyTrue();
	}
	private int operateMenuEventConf(MenuItem item,MenuItem curItem,EventConf eventConf){
		if(item.getId().equals(curItem.getId())){
			operateEventConf(item, eventConf);
			return 1;
		}
		List<MenuItem> items = item.getChildList();
		if(items!=null&&items.size()>0){
			for(MenuItem subItem:items){
				int i = operateMenuEventConf(subItem,curItem ,eventConf);
				if(i==1)
					return 1;
			}
		}	
		return 0;
	}
	
	private void operateEventConf(WebElement we, EventConf eventConf){
		if(we != null){
			/*for(EventConf ec:we.getEventConfList()){
				if(ec.getEventStatus()!=EventConf.NORMAL_STATUS){
					ec.setEventStatus(EventConf.NORMAL_STATUS);
				}
			}*/
			switch(actionType){
				case 1:
					eventConf.setEventStatus(EventConf.ADD_STATUS);
					we.addEventConf(eventConf);
					break;
				case 2:
					switch(eventConf.getEventStatus()){
						case EventConf.ADD_STATUS:
							we.removeEventConf(eventConf.getName(), eventConf.getMethodName());
							break;
						case EventConf.NORMAL_STATUS:
							eventConf.setEventStatus(EventConf.DEL_STATUS);
							we.removeEventConf(eventConf.getName(), eventConf.getMethodName());
							we.addEventConf(eventConf);
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}
		}else{
			MainPlugin.getDefault().logError("WebElement对象为空!"); //$NON-NLS-1$
			throw new LfwRuntimeException("WebElement对象为空!"); //$NON-NLS-1$
		}
	}
	
	/**
	 * 增加事件
	 * @param events
	 * @param eventConf
	 * @return
	 */
	private EventConf[] addEventConf(EventConf[] events, EventConf eventConf){
		List<EventConf> list = new ArrayList<EventConf>();
		if(events != null){
			for(EventConf event : events){
				list.add(event);
			}
		}
		switch(actionType){
			case 1:
				eventConf.setEventStatus(EventConf.ADD_STATUS);
				list.add(eventConf);
				break;
			case 2:
				switch(eventConf.getEventStatus()){
					case EventConf.ADD_STATUS:
						if(list.size() > 0){
							Iterator<EventConf> it = list.iterator();
							while(it.hasNext()){
								EventConf event = it.next();
								if(event.getName().equals(eventConf.getName()) && event.getMethodName().equals(eventConf.getMethodName())){
									it.remove();
									break;
								}
							}
						}
						break;
					case EventConf.NORMAL_STATUS:
						eventConf.setEventStatus(EventConf.DEL_STATUS);
						if(list.size() > 0){
							Iterator<EventConf> it = list.iterator();
							while(it.hasNext()){
								EventConf event = it.next();
								if(event.getName().equals(eventConf.getName()) && event.getMethodName().equals(eventConf.getMethodName())){
									it.remove();
									break;
								}
							}
						}
						list.add(eventConf);
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}
		return list.toArray(new EventConf[0]);
	}
	
}
