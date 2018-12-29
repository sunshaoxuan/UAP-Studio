package nc.lfw.editor.widget;

import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.editor.widget.plug.DelPluginDescAction;
import nc.lfw.editor.widget.plug.DelPlugoutDescAction;
import nc.lfw.editor.widget.plug.NewPluginDescAction;
import nc.lfw.editor.widget.plug.NewPlugoutDescAction;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;

import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.view.ViewViewPage;
import nc.uap.lfw.editor.window.DeleteInnerWinObjAction;
import nc.uap.lfw.editor.window.DeleteWindowObjAction;
import nc.uap.lfw.editor.window.WindowConfigObj;
//import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.internal.ui.actions.breakpoints.DisableBreakpointsAction;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Widget編輯器
 * @author guoweic
 *
 */
public class WidgetEditor extends LFWBaseEditor {

	private WidgetGraph graph = new WidgetGraph();

	public WidgetGraph getGraph() {
		return graph;
	}

	public void setGraph(WidgetGraph graph) {
		this.graph = graph;
	}
	
//	@Override
//	public void commandStackChanged(EventObject arg0) {
//		firePropertyChange(IEditorPart.PROP_DIRTY);
//		super.commandStackChanged(arg0);
//	}
//
//	public boolean isDirty() {
//		if(super.isDirty())
//			return true;
//		return getCommandStack().isDirty();
//	}
	
	public WidgetEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	TableViewer tv = null;

	public TableViewer getTv() {
		return tv;
	}

	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());

	}
	
	/**
	 * 获取当前的WidgetEditor（基类中已有该方法）
	 * @return
	 */
	public static WidgetEditor getActiveWidgetEditor() {
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if (page != null) {
//			editor = page.getActiveEditor();
//		}
		editor = LFWBaseEditor.getActiveEditor();
		if (editor != null && editor instanceof WidgetEditor) {
			return (WidgetEditor) editor;
		} else {
			return null;
		}
	}

	
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	private WidgetElementObj wdElementObj = null;
	
	private static final Dimension size = new Dimension(100, 100);
	private static final int padding = 50;

	protected void setInput(IEditorInput input) {
		super.setInput(input);
		WidgetEditorInput wdEditorInput = (WidgetEditorInput) input;
//		LFWWidgetTreeItem widgetTreeItem = wdEditorInput.getTreeItem();
//		LfwWidget widget = widgetTreeItem.getWidget();
		LfwView widget = (LfwView) wdEditorInput.getCloneElement();
		
		//先屏蔽 因为这里重取widget后，丢掉了pluginDesc plugoutDesc对象
//		TreeItem ti = LFWAMCPersTool.getCurrentTreeItem();
//		if(ti instanceof LFWBasicTreeItem){
//			LFWBasicTreeItem bTreeItem = (LFWBasicTreeItem)ti;
//			if(LFWTool.NEW_VERSION.equals(bTreeItem.getLfwVersion())){
//				widget = LFWAMCConnector.getView(LFWAMCPersTool.getCurrentFolderPath(), WEBProjConstants.AMC_VIEW_FILENAME);
//			}
//		}
		
		WidgetElementObj wdObj = new WidgetElementObj();

		wdObj.setWidget(widget);
		
		// 显示Widget
		int pointX = 50;
		int pointY = 100;
		// 设置显示位置
		wdObj.setSize(new Dimension(100, 100));
		Point point = new Point(pointX, pointY);
		wdObj.setLocation(point);
		graph.addWidgetCell(wdObj);
		
		graph.setWidget(widget);
		
		this.wdElementObj = wdObj;
		
//		wdElementObj.getFigure().markError("fdafadf");
		// 显示Listener
//		addListenerCellToEditor(widget.getListenerMap(), graph);
		
		//显示plugoutDesc与pluginDesc
		int count = -2;
		if (widget.getPlugoutDescs() != null){
			for (PlugoutDesc p: widget.getPlugoutDescs()){
				PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
				plugoutObj.setId(p.getId());
				plugoutObj.setPlugout(p);
				Point outpoint = new Point();
//				int count =wdObj.getPlugoutCells().size()+wdObj.getPluginCells().size();;
				outpoint.x = wdObj.getLocation().x + wdObj.getSize().width + 50;
				outpoint.y = wdObj.getLocation().y + count * 45;
				count++;
				ChildConnection conn = new ChildConnection(wdObj,plugoutObj);
				conn.connect();
				plugoutObj.setConn(conn);
				plugoutObj.setLocation(outpoint);
				plugoutObj.setSize(new Dimension(100, 40));
				plugoutObj.setWidgetObj(wdObj);
				wdObj.addPlugoutCell(plugoutObj);
			}
		}
		count = -2;
		if (widget.getPluginDescs() != null){
			for (PluginDesc p: widget.getPluginDescs()){
				PluginDescElementObj pluginObj = new PluginDescElementObj();
				pluginObj.setId(p.getId());
				pluginObj.setPlugin(p);
				Point outpoint = new Point();
//				int count =wdObj.getPluginCells().size()+wdObj.getPlugoutCells().size();
				outpoint.x = wdObj.getLocation().x -  wdObj.getSize().width - 50;
				outpoint.y = wdObj.getLocation().y + count * 45;
				count++;
				ChildConnection conn = new ChildConnection(wdObj,pluginObj);
				conn.connect();
				pluginObj.setConn(conn);
				pluginObj.setLocation(outpoint);
				pluginObj.setSize(new Dimension(100, 40));
				pluginObj.setWidgetObj(wdObj);
				wdObj.addPluginCell(pluginObj);
			}
		}
		//绘制内联window
		List<Connector> connectList = widget.getConnectorList();
//		int count = 0;
		count = 0;
		WindowConfig[] winList = widget.getInlineWindows();
		if(winList.length > 0){
			pointY = 50;
			for(WindowConfig winConf : winList){
//				LfwWindow win = LFWAMCPersTool.getPageMetaDetailById(winConf.getId());
				LfwWindow win = LFWAMCConnector.getWindowById(winConf.getId());
				if(win == null) continue;
				WindowConfigObj winObj = new WindowConfigObj();
				winObj.setWindowConfig(winConf);
				winObj.setSize(size);
				pointX = 550;			
				point = new Point(pointX, pointY);
				winObj.setLocation(point);
				graph.addWindowCell(winObj);
				pointY += size.height + padding;
				
				//绘制内联window和本view的plugin和plugout连接线
				for(Connector conn:connectList){
					String connType = conn.getConnType();
					if(connType.equals(Connector.VIEW_INLINEWINDOW)){
						if(win.getPluginDesc(conn.getPluginId())!=null){
							IPluginDesc p = (IPluginDesc) win.getPluginDesc(conn.getPluginId());
							PluginDescElementObj pluginObj = new PluginDescElementObj();
							pluginObj.setPlugin(p);
							pluginObj.setId(p.getId());
							Point outpoint = new Point();
							count =winObj.getPluginCells().size()+winObj.getPlugoutCells().size();
							outpoint.x = winObj.getLocation().x - winObj.getSize().width - 50;
							outpoint.y = winObj.getLocation().y + count * 40;
							ChildConnection childConn = new ChildConnection(winObj,pluginObj);
							childConn.connect();
							pluginObj.setLocation(outpoint);
							pluginObj.setSize(new Dimension(100, 30));
	//						pluginObj.setWindowObj(winObj);						
							pluginObj.setConn(childConn);
							winObj.addPluginCell(pluginObj);
							int connFlag = 0;
							for(PlugoutDescElementObj plugout:wdObj.getPlugoutCells()){
								if(plugout.getId().equals(conn.getPlugoutId())){
									Connection plugConn = new Connection(plugout,pluginObj);								
									plugConn.connect();
									connFlag = 1;
								}
							}
							if(connFlag==0){
								wdObj.setErrorMsg(M_pagemeta.WidgetEditor_0);
							}
						}
						else{
							wdObj.setErrorMsg(M_pagemeta.WidgetEditor_1);
						}
					}
					if(connType.equals(Connector.INLINEWINDOW_VIEW)){
						if(win.getPlugoutDesc(conn.getPlugoutId())!=null){
							IPlugoutDesc p = (IPlugoutDesc) win.getPlugoutDesc(conn.getPlugoutId());
							PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
							plugoutObj.setPlugout(p);
							plugoutObj.setId(p.getId());
							Point outpoint = new Point();
							count =winObj.getPluginCells().size()+winObj.getPlugoutCells().size();
							outpoint.x = winObj.getLocation().x - winObj.getSize().width - 50;
							outpoint.y = winObj.getLocation().y + count * 40;
							ChildConnection childConn = new ChildConnection(winObj,plugoutObj);
							childConn.connect();
							plugoutObj.setLocation(outpoint);
							plugoutObj.setSize(new Dimension(100, 30));
	//						plugoutObj.setWindowObj(winObj);						
							plugoutObj.setConn(childConn);
							winObj.addPlugoutCell(plugoutObj);
							int connFlag = 0;
							for(PluginDescElementObj plugin:wdObj.getPluginCells()){
								if(plugin.getId().equals(conn.getPluginId())){
									Connection plugConn = new Connection(plugoutObj,plugin);								
									plugConn.connect();
									connFlag = 1;
								}
							}
							if(connFlag==0){
								wdObj.setErrorMsg(M_pagemeta.WidgetEditor_2);
							}
						}
						else{
							wdObj.setErrorMsg(M_pagemeta.WidgetEditor_3);
						}
					}
				}
			}
		}
	}
	/**
	 * 重新绘制页面
	 */
	public void repaintGraph() {
		List<WindowConfigObj> windowCells = graph.getWindowCells();
		while(windowCells.size()>0){
			graph.removeWindowCell(windowCells.get(0));
		}
		WindowConfig[] winList = graph.getWidget().getInlineWindows();
		if(winList.length > 0){
			int pointY = 50;
			for(WindowConfig pm : winList){
				WindowConfigObj winObj = new WindowConfigObj();
//				LfwWindow window = LFWAMCConnector.getWindowById(pm.getId());
				winObj.setWindowConfig(pm);
				winObj.setSize(size);
				int pointX = 550;				
				Point point = new Point(pointX, pointY);
				winObj.setLocation(point);
				graph.addWindowCell(winObj);
				pointY += size.height + padding;
			}
		}
	}
	
	/**
	 * 保存Widget到文件中
	 * @param widget
	 */
	private void saveWidget(LfwView widget) {
//		WidgetEditorInput input = (WidgetEditorInput) getEditorInput();
		// 获取项目路径
		String projectPath = LFWPersTool.getProjectPath();
//		LFWWidgetTreeItem widgetTreeItem = input.getTreeItem();
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if (null == widgetTreeItem)
			widgetTreeItem = LFWPersTool.getWidgetTreeItemById(widget.getId());
//		String pagemetaNodePath = ((LFWNodeTreeItem)widgetTreeItem.getParentItem()).getFile().getPath();
		String pagemetaNodePath = null;
		if(widgetTreeItem != null){
			pagemetaNodePath = ((LFWBasicTreeItem)widgetTreeItem.getParentItem()).getFile().getPath();
			
		}else{
			pagemetaNodePath = ((LFWBasicTreeItem)LFWPersTool.getCurrentTreeItem()).getFile().getPath();
		}
		// 保存Widget到widget.wd中
		LFWSaveElementTool.updateView(widget);
		if(widgetTreeItem.getParentItem() instanceof LFWPageMetaTreeItem){
			LFWPageMetaTreeItem window = ((LFWPageMetaTreeItem)widgetTreeItem.getParentItem());
			window.getPm().removeView(widget.getId());
			window.getPm().addView(widget);
		}	
		widgetTreeItem.setWidget(widget);
	}
	
	public WidgetElementObj getModel() {
		return this.wdElementObj;
	}
	
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(this.graph);
		getGraphicalViewer().addDropTargetListener(
				new nc.uap.lfw.perspective.editor.DiagramTemplateTransferDropTargetListener(
						getGraphicalViewer()));
		LFWPersTool.showView(IPageLayout.ID_PROP_SHEET);
	}
	
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}

		};
	}
	
	private KeyHandler shareKeyHandler = null;

	private KeyHandler getShareKeyHandler() {
		if (shareKeyHandler == null) {
			shareKeyHandler = new KeyHandler();
			shareKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		}
		return shareKeyHandler;
	}
	
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new WidgetEditPartFactory(this));
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		getGraphicalViewer().setContextMenu(getMenuManager());
		
	}
	
	/**
	 * 增加编辑器自定义菜单
	 * @param manager
	 */
	protected void editMenuManager(IMenuManager manager) {
		if (null != getCurrentSelection()) {
			StructuredSelection ss = (StructuredSelection) getCurrentSelection();
			Object sel = ss.getFirstElement();
			if (sel instanceof WidgetElementPart) {
				WidgetElementPart lfwEle = (WidgetElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof WidgetElementObj) {
					//增加plugoutDesc
					WidgetElementObj widgetObj = (WidgetElementObj) model;
					manager.add(new NewPluginDescAction(widgetObj));
					manager.add(new NewPlugoutDescAction(widgetObj));
					
				}
				else if (model instanceof WindowConfigObj) {
					WindowConfigObj windowObj = (WindowConfigObj)model;
					manager.add(new DeleteInnerWinObjAction(windowObj));
				}
			}
			else if (sel instanceof PluginDescElementPart) {
				PluginDescElementPart lfwEle = (PluginDescElementPart) sel;
				Object model = lfwEle.getModel();
				PluginDescElementObj pluginObj = (PluginDescElementObj) model;
				if(pluginObj.getWidgetObj()!=null)
					manager.add(new DelPluginDescAction(pluginObj));
			}
			else if (sel instanceof PlugoutDescElementPart) {
				PlugoutDescElementPart lfwEle = (PlugoutDescElementPart) sel;
				Object model = lfwEle.getModel();
				PlugoutDescElementObj plugoutObj = (PlugoutDescElementObj) model;
				if(plugoutObj.getWidgetObj()!=null)
					manager.add(new DelPlugoutDescAction(plugoutObj));
			}
			else {
				return;
			}
		} else {
			return;
		}
	}

	private PaletteRoot paleteRoot = null;

	protected PaletteRoot getPaletteRoot() {
		if (paleteRoot == null) {
			paleteRoot = PaletteFactory.createWidgetPalette();
		}
		return paleteRoot;
	}
	
	
	public void doSave(IProgressMonitor monitor) {
		// 保存Event代码
		super.doSave(monitor);
//		saveEventScript();
//		String msg = graph.getCells().get(0).validate();
//		if(msg != null){
//			String message = "片段输入信息有错误，是否还要保存：\r\n\r\n\r\n"+msg;
//			if(!MessageDialog.openConfirm(getSite().getShell(), "提示", message))
//				return;
//		}
		LfwView widget = graph.getWidget();
		saveWidget(widget);
	}

	
	public LFWAbstractViewPage createViewPage() {
		TreeItem treeItem = LFWAMCPersTool.getCurrentTreeItem();
		return new ViewViewPage();
	}

	public String getPath() {
		return null;
	}

	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	protected LfwElementObjWithGraph getTopElement() {
		return graph.getWidgetCells().get(0);
	}
	
	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		return graph.getWidget().getAcceptEventDescs();
	}

	@Override
	public void setFocus() {
		super.setFocus();
		IEditorInput input = getEditorInput();
		if (input instanceof WidgetEditorInput) {
			WidgetEditorInput editorInput = (WidgetEditorInput)input;
			try{
				TreeItem selectedTI = editorInput.getCurrentTreeItem();
				if(selectedTI != null && !selectedTI.isDisposed()){
					LFWAMCPersTool.getTree().setSelection(selectedTI);
					editorInput.setCurrentTreeItem(selectedTI);
				}else{
					this.setDirtyFalse();
					MessageDialog.openError(null, M_pagemeta.WidgetEditor_4, M_pagemeta.WidgetEditor_5);
				}
			}catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
		}
	}
	
	@Override
	protected TreeItem getSelectedTreeItem(LFWDirtoryTreeItem dirTreeItem,
			LfwBaseEditorInput editorInput) {
		TreeItem ti = null;
		if(editorInput instanceof WidgetEditorInput){
			LfwView widget = ((WidgetEditorInput)editorInput).getWidget();
			if(widget != null){
				if(widget.getRefId().startsWith("..") /*|| widget.getExtendAttribute(LfwView.POOLWIDGET) != null*/){ //$NON-NLS-1$
					//public view节点集合
					TreeItem[] treeItems = dirTreeItem.getItems();
					if(treeItems != null && treeItems.length > 0){
						for(TreeItem item : treeItems){
							if(item instanceof LFWWidgetTreeItem){
								if(widget.getId().equals(((LFWWidgetTreeItem)item).getWidget().getId())){
									ti = item;
									break;
								}
							}
						}
					}
				}else{
					LfwWindow pm = widget.getPagemeta();
					//Window节点集合
					ti = getSelectedTreeItem(pm.getId(), widget.getId(), dirTreeItem);
				}
			}
		}
		return ti;
	}
	
	private TreeItem getSelectedTreeItem(String pageMetaId, String id, TreeItem parent){
		if(pageMetaId != null && id != null && parent != null){
			TreeItem[] children = parent.getItems();
			if(children != null && children.length > 0){
				for(TreeItem child : children){
					if(child instanceof LFWPageMetaTreeItem){
						if(pageMetaId.equals(((LFWPageMetaTreeItem) child).getId())){
							TreeItem[] views = child.getItems();
							if(views != null && views.length > 0){
								for(TreeItem view : views){
									if(view instanceof LFWWidgetTreeItem){
										if(id.equals(((LFWWidgetTreeItem) view).getId())){
											return view;
										}
									}
								}
							}
						}
					}else if(child instanceof LFWVirtualDirTreeItem){
						TreeItem item = getSelectedTreeItem(pageMetaId, id, child);
						if(item != null){
							return item;
						}
					}
				}
			}
		}
		return null;
	}
	
}
