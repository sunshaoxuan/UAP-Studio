/**
 * 
 */
package nc.uap.lfw.editor.application;

import java.util.EventObject;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.window.DeleteWindowObjAction;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.palette.AMCPaletteFactory;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

/**
 * 
 * Application编辑器类
 * @author chouhl
 *
 */
public class ApplicationEditor extends LFWBaseEditor {

	private ApplicationGraph graph = new ApplicationGraph();
	
	public ApplicationEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	
	/**
	 * 获取当前的ApplicationEditor
	 * @return
	 */
	public static ApplicationEditor getActiveEditor() {
		LFWBaseEditor editor = LFWBaseEditor.getActiveEditor();
		if(editor instanceof ApplicationEditor) {
			return (ApplicationEditor)editor;
		}else {
			return null;
		}
	}
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ApplicationEditorInput editorInput = (ApplicationEditorInput)input;
		Application webElement = (Application)editorInput.getCloneElement();
//		webElement = LFWAMCConnector.getApplication(LFWAMCPersTool.getCurrentFolderPath(), WEBPersConstants.AMC_APPLICATION_FILENAME);
		graph.setApplication(webElement);
		//Application图形
		ApplicationObj obj = new ApplicationObj();
		obj.setApp(webElement);
		graph.addApplicationCell(obj);
		//Window图形
		List<WindowConfig> windowList = webElement.getWindowList();
		if(windowList != null && windowList.size() > 0){
			for(WindowConfig window: windowList){
				WindowConfigObj windowObj = new WindowConfigObj();
				windowObj.setWindowConfig(window);
				graph.addWindowCell(windowObj);
			}
		}
		Connector[] connectors = webElement.getConnectors();
//		webElement.get
		for(Connector conn:connectors){
			String source = conn.getSource();
			String target = conn.getTarget();
			LfwWindow sourceWin = LFWAMCConnector.getWindowById(source);
			LfwWindow targetWin = LFWAMCConnector.getWindowById(target);
			if(sourceWin!=null&&targetWin!=null){
				if(sourceWin.getPlugoutDesc(conn.getPlugoutId())==null)
					obj.setErrorMsg(M_application.ApplicationEditor_0+source+M_application.ApplicationEditor_1);
				if(targetWin.getPluginDesc(conn.getPluginId())==null)
					obj.setErrorMsg(M_application.ApplicationEditor_2+target+M_application.ApplicationEditor_1);
			}
			else{
				obj.setErrorMsg(M_application.ApplicationEditor_4);
			}
		}
		//重绘编辑器界面图形
		repaintGraph();
	}
	
	
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(this.graph);
		getGraphicalViewer().addDropTargetListener(
				new nc.uap.lfw.perspective.editor.DiagramTemplateTransferDropTargetListener(
						getGraphicalViewer()));
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
			getActionRegistry().registerAction(new DeleteWindowObjAction());
			shareKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		}
		return shareKeyHandler;
	}
	
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new ApplicationEditPartFactory(this));
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		getGraphicalViewer().setContextMenu(getMenuManager());
	}
	
	private PaletteRoot paleteRoot = null;

	protected PaletteRoot getPaletteRoot() {
		if (paleteRoot == null) {
			paleteRoot = AMCPaletteFactory.createApplicationPalette(graph.getProject(), graph.getCurrentTreeItem());
		}
		return paleteRoot;
	}
	
	public LFWAbstractViewPage createViewPage() {
		return new ApplicationViewPage();
	}
	/**
	 * 右键菜单
	 */
	protected void editMenuManager(IMenuManager manager) {
		if (null != getCurrentSelection()) {
			StructuredSelection ss = (StructuredSelection) getCurrentSelection();
			Object sel = ss.getFirstElement();
			if (sel instanceof ApplicationPart) {
				ApplicationPart lfwEle = (ApplicationPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof WindowConfigObj) {
					manager.add(new DeleteWindowObjAction());
				}
			}
		}
	}


	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	protected LfwElementObjWithGraph getTopElement() {
		List<WindowConfigObj> cells = graph.getWindowCells();
		if(cells != null && cells.size() > 0){
			if(cells.size() % 2 == 0){//偶数个,返回倒数第二个Window
				return cells.get(cells.size() - 2);
			}else{//奇数个,返回最后一个Window
				return cells.get(cells.size() - 1);
			}
		}else if(graph.getApplicationCells() != null && graph.getApplicationCells().size() > 0){
			return graph.getApplicationCells().get(0);
		}else{
			return null;
		}
	}


	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}
	public void setFocus() {
		super.setFocus();
		IEditorInput input = getEditorInput();
		if (input instanceof ApplicationEditorInput) {
			ApplicationEditorInput editorInput = (ApplicationEditorInput)input;
			try{
				TreeItem selectedTI = editorInput.getCurrentTreeItem();
//				if(selectedTI == null || selectedTI.isDisposed()){
//					selectedTI = getSelectedTreeItem(WEBPersConstants.APPLICATION);
//				}
				if(selectedTI != null && !selectedTI.isDisposed()){
					LFWAMCPersTool.getTree().setSelection(selectedTI);
					editorInput.setCurrentTreeItem(selectedTI);
				}else{
					this.setDirtyFalse();
					MessageDialog.openError(null, M_application.ApplicationEditor_5, M_application.ApplicationEditor_6);
				}
			}catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
		}
	}
	
	@Override
	protected TreeItem getSelectedTreeItem(LFWDirtoryTreeItem dirTreeItem, LfwBaseEditorInput editorInput) {
		if(dirTreeItem != null && editorInput instanceof ApplicationEditorInput){
			ApplicationEditorInput appEI = (ApplicationEditorInput)editorInput;
			String appId = appEI.getWebElement().getId();
			if(appId == null){
				return null;
			}
			return getSelectedTreeItem(appId, dirTreeItem);
		}
		return null;
	}
	
	private TreeItem getSelectedTreeItem(String id, TreeItem parent){
		if(id != null && parent != null){
			TreeItem[] children = parent.getItems();
			if(children != null && children.length > 0){
				for(TreeItem child : children){
					if(child instanceof LFWApplicationTreeItem){
						if(id.equals(((LFWApplicationTreeItem) child).getId())){
							return child;
						}
					}else if(child instanceof LFWVirtualDirTreeItem){
						TreeItem item = getSelectedTreeItem(id, child);
						if(item != null){
							return item;
						}
					}
				}
			}
		}
		return null;
	}
	
	private static final Dimension size = new Dimension(100, 100);
	private static final int padding = 50;
	
	/**
	 * 重绘编辑器界面图形位置
	 */
	public void repaintGraph() {
		int pointX = 100;
		int pointY = -50;
		List<ApplicationObj> appCells = graph.getApplicationCells();
		if(appCells != null && appCells.size() > 0){
			for(int i=0;i<appCells.size();i++){
				if(i % ApplicationGraph.number == 0){
					pointX = 100;
					pointY += size.height + padding;
				}
				pointX += (i % ApplicationGraph.number) * (size.width + padding);
				appCells.get(i).setSize(size);
				appCells.get(i).setLocation(new Point(pointX, pointY));
			}
		}
		List<WindowConfigObj> windowCells = graph.getWindowCells();
		if(windowCells != null && windowCells.size() > 0){
			pointX = 100;
			for(int i=0;i<windowCells.size();i++){
				if(i % ApplicationGraph.number == 0){
					pointX = 100;
					pointY += size.height + padding;
				}
				pointX += (i % ApplicationGraph.number) * (size.width + padding);
				windowCells.get(i).setSize(size);
				windowCells.get(i).setLocation(new Point(pointX, pointY));
			}
		}
	}
	
	/**
	 * 重绘Window图形
	 * @param obj
	 */
	public void repaintWindowObj(WindowConfigObj obj){
		if(obj == null){
			return;
		}
		int pointX = 100;
		int pointY = -50;
		List<WindowConfigObj> windowCells = graph.getWindowCells();
		if(windowCells != null && windowCells.size() > 0){
			if(windowCells.size() % ApplicationGraph.number == 0){
				pointY = windowCells.get(windowCells.size() - 2).getLocation().y + size.height + padding;
			}else{
				pointX += (windowCells.size() % ApplicationGraph.number) * (size.width + padding);
				pointY = windowCells.get(windowCells.size() - 1).getLocation().y;
			}
			if(pointX == windowCells.get(windowCells.size() - 1).getLocation().x && pointY == windowCells.get(windowCells.size() - 1).getLocation().y){
				pointX = 100;
				pointY += size.height + padding;
			}
		}else{
			List<ApplicationObj> appCells = graph.getApplicationCells();
			if(appCells != null && appCells.size() > 0){
				pointX = 100;
				pointY = appCells.get(appCells.size() - 1).getLocation().y + size.height + padding;
			}else{
				pointX = 100;
				pointY += size.height + padding;
			}
		}
		obj.setSize(size);
		obj.setLocation(new Point(pointX, pointY));
	}
	
//	@Override
//	public void repaintListenerPositon() {
//		
//	}
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		save();
	}
	
	private void save(){
		Application application = graph.getApplication();
		if(application != null){
			if(application.getDefaultWindowId() == null || application.getDefaultWindowId().trim().length() == 0){
				if(application.getWindowList() != null && application.getWindowList().size() > 0){
					MessageDialog.openInformation(null, M_application.ApplicationEditor_5, M_application.ApplicationEditor_8);
					setDirtyTrue();
					return;
				}
			}
			LFWSaveElementTool.saveApplication(application);
			LFWAMCPersTool.getCurrentApplicationTreeItem().setApplication(application);
			LFWAMCPersTool.getCurrentApplicationTreeItem().setText(application.getCaption() + "[" + application.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			getCommandStack().markSaveLocation();
		}
	}
	
	public ApplicationGraph getGraph(){
		return this.graph;
	}
	
	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		return graph.getApplication().getAcceptEventDescs();
	}
	
}
