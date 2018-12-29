package nc.lfw.editor.pagemeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.plug.NewPluginProxyAction;
import nc.lfw.editor.pagemeta.plug.NewPlugoutProxyAction;
import nc.lfw.editor.widget.EditWidgetAction;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.plug.DelPluginDescAction;
import nc.lfw.editor.widget.plug.DelPlugoutDescAction;
import nc.lfw.editor.widget.plug.NewPluginDescAction;
import nc.lfw.editor.widget.plug.NewPlugoutDescAction;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.view.DeleteViewObjAction;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.editor.window.WindowViewPage;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.action.DeleteWidgetAction;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Label;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorPart;

public class RelationEditor extends LFWBaseEditor {
	
	private PagemetaGraph graph = new PagemetaGraph();

	public RelationEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
//		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	public PagemetaGraph getGraph() {
		return graph;
	}

	public void setGraph(PagemetaGraph graph) {
		this.graph = graph;
	}
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	
	public static RelationEditor getActiveRelationEditor() {
		RelationEditor relationEditor = null;
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		editor = LFWBaseEditor.getActiveEditor();
//		if (page != null) {
//			editor = page.getActiveEditor();
//		}
		if(editor!=null && editor instanceof MultiPageEditorPart){
			editor = (IEditorPart) ((MultiPageEditorPart)editor).getSelectedPage();
		}
		if (editor != null && editor instanceof RelationEditor) {
			relationEditor = (RelationEditor)editor;
		}
		else{

		}
		return relationEditor;		
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
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new RelationEditPartFactory(this));
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		getGraphicalViewer().setContextMenu(getMenuManager());
		
	}
	private KeyHandler shareKeyHandler = null;
	
	private KeyHandler getShareKeyHandler() {
		if (shareKeyHandler == null) {
			shareKeyHandler = new KeyHandler();
			getActionRegistry().registerAction(new DeleteViewObjAction());
			shareKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		}
		return shareKeyHandler;
	}
	
	protected void setInput(IEditorInput input) {
		
		
		super.setInput(input);
		
		
		LfwWindow pm = (LfwWindow) ((PagemetaEditorInput) input).getCloneElement();
		
		graph.setPagemeta(pm);
		graph.setEditor(this);
		String pageModel = LFWPersTool.getPageModel();
		if(pageModel != null && !pageModel.equals("")) //$NON-NLS-1$
			graph.setPagemodel(pageModel);
		
		WindowObj selfObj = new WindowObj();
		selfObj.setWindow(pm);
		selfObj.setSize(new Dimension(100, 100));
		int pointX =  200;
		int pointY =  200;
		Point selfPoint = new Point(pointX, pointY);
		selfObj.setLocation(selfPoint);
		graph.addWindowCell(selfObj);
		Integer floatx = 150;
		Integer count = 0;
		Integer countin = 0;
		Integer countout = -1;
		Integer countpin = 0;
		Integer countpout = -1;
		
//		显示plugoutDesc与pluginDesc
		if (pm.getPluginDescs() != null){
			for (IPluginDesc p: pm.getPluginDescs()){
				PluginDescElementObj pluginObj = new PluginDescElementObj();
				pluginObj.setPlugin(p);
				pluginObj.setId(p.getId());
				Point outpoint = new Point();
				if(p instanceof PluginProxy){
					floatx = -150;
					count = ++countpin;
				}else{
					floatx = 150;
					count = ++countin;
				}
				outpoint.x = selfObj.getLocation().x + floatx;
				outpoint.y = selfObj.getLocation().y + count * 45;
				ChildConnection conn = new ChildConnection(selfObj,pluginObj);
				conn.connect();
				pluginObj.setConn(conn);
				pluginObj.setLocation(outpoint);
				pluginObj.setSize(new Dimension(100, 40));
				pluginObj.setWindowObj(selfObj);
				selfObj.addPluginCell(pluginObj);
			}
		}		
//		int currentCount = count;
		count = 0;
		if (pm.getPlugoutDescs() != null){
			for (IPlugoutDesc p: pm.getPlugoutDescs()){
				PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
				plugoutObj.setPlugout(p);
				plugoutObj.setId(p.getId());
				Point outpoint = new Point();
				if(p instanceof PlugoutProxy){
					floatx = -150;
					count = ++countpout;
				}else{
					floatx = 150;
					count = ++countout;
				}
//				count = selfObj.getPluginCells().size() + selfObj.getPlugoutCells().size();
//				outpoint.x = selfObj.getLocation().x + selfObj.getSize().width + 50;
				outpoint.x = selfObj.getLocation().x + floatx;
				outpoint.y = selfObj.getLocation().y - count * 45;
				ChildConnection conn = new ChildConnection(selfObj,plugoutObj);
				conn.connect();
				plugoutObj.setConn(conn);
				plugoutObj.setLocation(outpoint);
				plugoutObj.setSize(new Dimension(100, 40));
				plugoutObj.setWindowObj(selfObj);
				selfObj.addPlugoutCell(plugoutObj);
			}
		}
		pointX = 750;
		pointY = 50;
		int point_flag = 0;
		Map<String, List<Connector>> connViewMap = new HashMap<String, List<Connector>>();
		Map<String, Connector> connectorMap = pm.getConnectorMap();
		Iterator<String> it = connectorMap.keySet().iterator();
		while (it.hasNext()){
			Connector connector = connectorMap.get(it.next());
			String source = connector.getSource();
			String target = connector.getTarget();
			String viewName = null;
			if(source==null||target==null)
				break;
			if(source.equals("")&&target.equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}else if((!source.equals("")&&target.equals(""))||(connector.getConnType()!=null&&connector.getConnType().equals(Connector.VIEW_WINDOW))){ //$NON-NLS-1$ //$NON-NLS-2$
				viewName = source;
			}else if((source.equals("")&&!target.equals(""))||(connector.getConnType()!=null&&connector.getConnType().equals(Connector.WINDOW_VIEW))){ //$NON-NLS-1$ //$NON-NLS-2$
				viewName = target;
			}
			if(viewName!=null){
				if(!connViewMap.containsKey(viewName)){
					connViewMap.put(viewName, new ArrayList<Connector>());
				}
				connViewMap.get(viewName).add(connector);
			}
		}
		for (String key:connViewMap.keySet()) {
			int count_flag = 0;
			List<Connector> connList = connViewMap.get(key);
			WidgetElementObj wdObj = new WidgetElementObj();
			LfwView widget = pm.getWidget(key);
			wdObj.setWidget(widget);
			wdObj.setSize(new Dimension(100, 100));					
			Point point = new Point(pointX, pointY + point_flag);
			wdObj.setLocation(point);
			graph.addWidgetCell(wdObj);
			point_flag = point_flag + connList.size() * 40 + 50;
			
			for(Connector connector:connList){
				String connType = connector.getConnType();
				if(connType==null) 
					continue;
				else if(connType.equals(connector.VIEW_WINDOW)){
					int connFlag = 0;
					for (PlugoutDesc p: wdObj.getWidget().getPlugoutDescs()){
						if(p.getId().equals(connector.getPlugoutId())){
							PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
							plugoutObj.setPlugout(p);
							plugoutObj.setId(p.getId());
							Point outpoint = new Point();
							outpoint.x = wdObj.getLocation().x - wdObj.getSize().width - 50;
							outpoint.y = wdObj.getLocation().y + count_flag * 40;
							count_flag++;
							ChildConnection conn = new ChildConnection(wdObj,plugoutObj);
							conn.connect();
							plugoutObj.setLocation(outpoint);
							plugoutObj.setSize(new Dimension(100, 30));
							plugoutObj.setWidgetObj(wdObj);						
							plugoutObj.setConn(conn);
							wdObj.addPlugoutCell(plugoutObj);
							for(PluginDescElementObj plugin:selfObj.getPluginCells()){
								if(plugin.getId().equals(connector.getPluginId())){
									Connection plugConn = new Connection(plugoutObj,plugin);								
									plugConn.connect();
									graph.addConns(plugConn);
									connFlag = 1;
								}
							}
						}
					}
					if(connFlag==0){
						selfObj.setErrorMsg(M_pagemeta.RelationEditor_0+connector.getSource()+M_pagemeta.RelationEditor_1);
					}
				}else if(connType.equals(connector.WINDOW_VIEW)){
					int connFlag = 0;
					for (PluginDesc p: wdObj.getWidget().getPluginDescs()){
						if(p.getId().equals(connector.getPluginId())){
							PluginDescElementObj pluginObj = new PluginDescElementObj();
							pluginObj.setPlugin(p);
							pluginObj.setId(p.getId());
							Point outpoint = new Point();
							outpoint.x = wdObj.getLocation().x - wdObj.getSize().width - 50;
							outpoint.y = wdObj.getLocation().y + count_flag * 40;
							count_flag++;
							ChildConnection conn = new ChildConnection(wdObj,pluginObj);
							conn.connect();
							pluginObj.setLocation(outpoint);
							pluginObj.setSize(new Dimension(100, 30));
							pluginObj.setWidgetObj(wdObj);						
							pluginObj.setConn(conn);
							wdObj.addPluginCell(pluginObj);
							for(PlugoutDescElementObj plugout:selfObj.getPlugoutCells()){
								if(plugout.getId().equals(connector.getPlugoutId())){
									Connection plugConn = new Connection(plugout,pluginObj);								
									plugConn.connect();
									graph.addConns(plugConn);
									connFlag = 1;
								}
							}
						}
					}
					if(connFlag==0){
						selfObj.setErrorMsg(M_pagemeta.RelationEditor_2+connector.getTarget()+M_pagemeta.RelationEditor_3);
					}
				}
				
			}
			
		}
	}
	
	/**
	 * 重新绘制页面
	 */
	public void repaintGraph() {
		// 删除所有Widget图标和Connector连线
		List<Connection> conns = graph.getConnections();
//		for (Connection connection : conns) {
		while (conns.size() > 0) 
			graph.removeConn(conns.get(0));
		List<WidgetElementObj> widgetCells = graph.getWidgetCells();
//		for (WidgetElementObj widgetObj : widgetCells) {
		while (widgetCells.size() > 0)
			graph.removeWidgetCell(widgetCells.get(0));
		
		LfwWindow pm = graph.getPagemeta();
		
		// 重新设置页面状态图标显示位置
//		adjustPageStateCell(graph);
		
		// 调整Listener对象位置
//		repaintListenerPositon();
		
		WindowObj selfObj = graph.getWindowCells().get(0);
		int pointX = 750;
		int pointY = 50;
		int point_flag = 0;
		Map<String, List<Connector>> connViewMap = new HashMap<String, List<Connector>>();
		Map<String, Connector> connectorMap = pm.getConnectorMap();
		Iterator<String> it = connectorMap.keySet().iterator();
		while (it.hasNext()){
			Connector connector = connectorMap.get(it.next());
			String source = connector.getSource();
			String target = connector.getTarget();
			String viewName = null;
			if(source==null||target==null)
				break;
			if(source.equals("")&&target.equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}else if((!source.equals("")&&target.equals(""))||(connector.getConnType()!=null&&connector.getConnType().equals(Connector.VIEW_WINDOW))){ //$NON-NLS-1$ //$NON-NLS-2$
				viewName = source;
			}else if((source.equals("")&&!target.equals(""))||(connector.getConnType()!=null&&connector.getConnType().equals(Connector.WINDOW_VIEW))){ //$NON-NLS-1$ //$NON-NLS-2$
				viewName = target;
			}
			if(viewName!=null){
				if(!connViewMap.containsKey(viewName)){
					connViewMap.put(viewName, new ArrayList<Connector>());
				}
				connViewMap.get(viewName).add(connector);
			}
		}
		for (String key:connViewMap.keySet()) {
			int count_flag = 0;
			List<Connector> connList = connViewMap.get(key);
			WidgetElementObj wdObj = new WidgetElementObj();
			LfwView widget = pm.getWidget(key);
			wdObj.setWidget(widget);
			wdObj.setSize(new Dimension(100, 100));					
			Point point = new Point(pointX, pointY + point_flag);
			wdObj.setLocation(point);
			graph.addWidgetCell(wdObj);
			point_flag = point_flag + connList.size() * 40 + 50;
			
			for(Connector connector:connList){
				String connType = connector.getConnType();
				if(connType==null) 
					continue;
				else if(connType.equals(connector.VIEW_WINDOW)){
					int connFlag = 0;
					for (PlugoutDesc p: wdObj.getWidget().getPlugoutDescs()){
						if(p.getId().equals(connector.getPlugoutId())){
							PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
							plugoutObj.setPlugout(p);
							plugoutObj.setId(p.getId());
							Point outpoint = new Point();
							outpoint.x = wdObj.getLocation().x - wdObj.getSize().width - 50;
							outpoint.y = wdObj.getLocation().y + count_flag * 40;
							count_flag++;
							
							plugoutObj.setLocation(outpoint);
							plugoutObj.setSize(new Dimension(100, 30));
							plugoutObj.setWidgetObj(wdObj);					
							ChildConnection conn = new ChildConnection(wdObj,plugoutObj);
							conn.connect();
							plugoutObj.setConn(conn);
							wdObj.addPlugoutCell(plugoutObj);
							for(PluginDescElementObj plugin:selfObj.getPluginCells()){
								if(plugin.getId().equals(connector.getPluginId())){
									Connection plugConn = new Connection(plugoutObj,plugin);								
									plugConn.connect();
									graph.addConns(plugConn);
									connFlag = 1;
								}
							}
						}
					}
					if(connFlag==0){
						selfObj.setErrorMsg(M_pagemeta.RelationEditor_0+connector.getSource()+M_pagemeta.RelationEditor_1);
					}
				}else if(connType.equals(connector.WINDOW_VIEW)){
					int connFlag = 0;
					for (PluginDesc p: wdObj.getWidget().getPluginDescs()){
						if(p.getId().equals(connector.getPluginId())){
							PluginDescElementObj pluginObj = new PluginDescElementObj();
							pluginObj.setPlugin(p);
							pluginObj.setId(p.getId());
							Point outpoint = new Point();
							outpoint.x = wdObj.getLocation().x - wdObj.getSize().width - 50;
							outpoint.y = wdObj.getLocation().y + count_flag * 40;
							count_flag++;
							ChildConnection conn = new ChildConnection(wdObj,pluginObj);
							conn.connect();
							pluginObj.setLocation(outpoint);
							pluginObj.setSize(new Dimension(100, 30));
							pluginObj.setWidgetObj(wdObj);						
							pluginObj.setConn(conn);
							wdObj.addPluginCell(pluginObj);
							for(PlugoutDescElementObj plugout:selfObj.getPlugoutCells()){
								if(plugout.getId().equals(connector.getPlugoutId())){
									Connection plugConn = new Connection(plugout,pluginObj);								
									plugConn.connect();
									graph.addConns(plugConn);
									connFlag = 1;
								}
							}
						}
					}
					if(connFlag==0){
						selfObj.setErrorMsg(M_pagemeta.RelationEditor_2+connector.getTarget()+M_pagemeta.RelationEditor_3);
					}
				}
				
			}
			
		}
		
	}
	@Override
	public void setFocus() {
		super.setFocus();
		IEditorInput input = getEditorInput();
		if (input instanceof PagemetaEditorInput) {
			PagemetaEditorInput editorInput = (PagemetaEditorInput)input;
			try{
				TreeItem selectedTI = editorInput.getCurrentTreeItem();
//				if(selectedTI == null || selectedTI.isDisposed()){
//					selectedTI = getSelectedTreeItem(WEBPersConstants.WINDOW);
//				}
				if(selectedTI != null && !selectedTI.isDisposed()){
					LFWAMCPersTool.getTree().setSelection(selectedTI);
					editorInput.setCurrentTreeItem(selectedTI);
				}else{
					this.setDirtyFalse();
					MessageDialog.openError(null, M_pagemeta.RelationEditor_4, M_pagemeta.RelationEditor_5);
				}
			}catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
		}
	}
	
	/**
	 * 增加编辑器自定义菜单
	 * @param manager
	 */
	protected void editMenuManager(IMenuManager manager) {
		if (null != getCurrentSelection()) {
			StructuredSelection ss = (StructuredSelection) getCurrentSelection();
			Object sel = ss.getFirstElement();
			if (sel instanceof RelationElementPart) {
				RelationElementPart lfwEle = (RelationElementPart) sel;
				Object model = lfwEle.getModel();
				if(model instanceof WindowObj){
					WindowObj windowObj = (WindowObj) model;
					if(windowObj.getWindow().getId().equals(LFWPersTool.getCurrentPageMeta().getId())){
						WindowObj winObj = (WindowObj)model;
						manager.add(new NewPluginDescAction(winObj));
						manager.add(new NewPluginProxyAction(winObj));
						manager.add(new NewPlugoutDescAction(winObj));
						manager.add(new NewPlugoutProxyAction(winObj));
					}
					else{
						
					}
				}
				
			} 
			else if (sel instanceof PluginDescElementPart) {
				PluginDescElementPart lfwEle = (PluginDescElementPart) sel;
				Object model = lfwEle.getModel();
				PluginDescElementObj pluginObj = (PluginDescElementObj) model;
				if(pluginObj.getWindowObj()!=null){
					manager.add(new DelPluginDescAction(pluginObj));
				}
			}
			else if (sel instanceof PlugoutDescElementPart) {
				PlugoutDescElementPart lfwEle = (PlugoutDescElementPart) sel;
				Object model = lfwEle.getModel();
				PlugoutDescElementObj plugoutObj = (PlugoutDescElementObj) model;
				if(plugoutObj.getWindowObj()!=null){
					manager.add(new DelPlugoutDescAction(plugoutObj));
				}
			}
			else {
				return;
			}
		} else {
			return;
		}
	}
	
	public void doSave(IProgressMonitor monitor) {
		// 保存Event代码
		super.doSave(monitor);
		save();
	}
	public void save() {
		LfwWindow pagemeta = graph.getPagemeta();
		savePagemeta(pagemeta);
		refreshEditor();
	}
	
	/**
	 * 保存Pagemeta到文件中
	 * @param widget
	 */
	public void savePagemeta(LfwWindow pagemeta) {
		
		for(IPluginDesc plugin:pagemeta.getPluginDescs()){
			if(plugin instanceof PluginProxy){
				if(((PluginProxy) plugin).getDelegatedViewId()==null&&((PluginProxy) plugin).getDelegatedPlugin()==null)
					{
					MessageDialog.openError(null, M_pagemeta.RelationEditor_6, M_pagemeta.RelationEditor_7);
					return;
					}
			}
		}
		for(IPlugoutDesc plugout:pagemeta.getPlugoutDescs()){
			if(plugout instanceof PlugoutProxy){
				if(((PlugoutProxy) plugout).getDelegatedViewId()==null&&((PlugoutProxy) plugout).getDelegatedPlugout()==null)
					{
					MessageDialog.openError(null, M_pagemeta.RelationEditor_6, M_pagemeta.RelationEditor_8);
					return;
					}
			}
		}
		// 获取项目路径
		String projectPath = LFWPersTool.getProjectPath();
		LFWPageMetaTreeItem pageMetaTreeItem = LFWPersTool.getCurrentPageMetaTreeItem();
		String pagemetaNodePath = pageMetaTreeItem.getFile().getPath();
		// 保存Widget到pagemeta.pm中
		LFWSaveElementTool.updateWindow(pagemeta);
		pageMetaTreeItem.setPm(pagemeta);
	}
	
	public static void refreshEditor() {
		IWorkbenchPage workbenchPage = LFWExplorerTreeView.getLFWExploerTreeView(null).getViewSite().getPage();
		IEditorPart editor = null;
		LfwWindow currentPagemeta = LFWPersTool.getCurrentPageMeta();
		
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] instanceof WindowEditor && ((WindowEditor)parts[i]).getSelectedPage() instanceof RelationEditor) {
				RelationEditor pmEditor = (RelationEditor)((WindowEditor)parts[i]).getSelectedPage();
				LfwWindow pmnew = ((PagemetaEditorInput)pmEditor.getEditorInput()).getPagemeta();
				if (currentPagemeta.getId().equals(pmnew.getId())) {
					editor =  pmEditor;
					break;
				}
			}
		}
		if (editor != null){
			((RelationEditor) editor).getGraph().setPagemeta(currentPagemeta);
			((RelationEditor) editor).repaintGraph();
		}
	}
	@Override
	protected LfwElementObjWithGraph getTopElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		TreeItem treeItem = LFWAMCPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWPageMetaTreeItem){
			return new WindowViewPage();
		}
		else{
			return new PagemetaViewPage();
		}
	}

	
	private PaletteRoot paleteRoot = null;

	protected PaletteRoot getPaletteRoot() {
		if (paleteRoot == null) {
			paleteRoot = PaletteFactory.createBasePalette();
		}
		return paleteRoot;
	}

}
