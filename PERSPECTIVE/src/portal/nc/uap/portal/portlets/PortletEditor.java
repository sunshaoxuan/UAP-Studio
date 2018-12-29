package nc.uap.portal.portlets;


import java.util.EventObject;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.grid.core.GridViewPage;
import nc.uap.lfw.perspective.views.ILFWViewPage;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.perspective.PortalPlugin;
import nc.uap.portal.portlets.page.PortletViewPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * PortletEditor
 * 
 * @author dingrf
 *
 */
public class PortletEditor extends LFWBaseEditor {

	private PortletElementObj contents = new PortletElementObj();
	
	TableViewer tv = null;

	private LFWAbstractViewPage viewPage;

	private ISelection currentSelection;

	public LFWAbstractViewPage getViewPage() {
		return viewPage;
	}
	
	public void setCurrentSelection(ISelection currentSelection) {
		this.currentSelection = currentSelection;
	}

	public ISelection getCurrentSelection() {
		return currentSelection;
	}
	
	public PortletEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	public TableViewer getTv() {
		return tv;
	}
	
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}

	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	
	/**
	 * 获取当前的PageFlowEditor（基类中已有该方法）
	 * @return
	 */
	public static PortletEditor getActiveEditor() {
//		IWorkbenchPage page = PortalPlugin.getDefault().getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if (page != null) {
//			editor = page.getActiveEditor();
//		}
		editor = LFWBaseEditor.getActiveEditor();
		if (editor != null && editor instanceof PortletEditor) {
			return (PortletEditor) editor;
		} else {
			return null;
		}
	}
	
	
	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	public void setFocus() {
		try{
			super.setFocus();
			IEditorInput input = getEditorInput();
			if (input instanceof PortletEditorInput) {
				PortletEditorInput portletEditorInput = (PortletEditorInput)input;				
				try{
					TreeItem selectedTI = portletEditorInput.getCurrentTreeItem();
					if(selectedTI != null && !selectedTI.isDisposed()){
						LFWAMCPersTool.getTree().setSelection(selectedTI);
						portletEditorInput.setCurrentTreeItem(selectedTI);
					}else{
						this.setDirtyFalse();
						MessageDialog.openError(null, "提示", "当前编辑器已失效,请关闭并重新打开!");
					}
				}catch(Exception e){
					MainPlugin.getDefault().logError(e);
				}
//				PortletDefinition portlet = portletEditorInput.getPortlet();
//				PortalDirtoryTreeItem nodeTreeItem = null;
//				PortletTreeItem portletTreeItem = null;
//				Tree tree = LFWPersTool.getTree();
//				TreeItem[] treeItems = tree.getItems();
//				IProject project = LFWPersTool.getCurrentProject();
//				LFWProjectTreeItem projectTreeItem = null;
//				for (int i = 0; i < treeItems.length; i++) {
//					LFWProjectTreeItem projectItem = (LFWProjectTreeItem) treeItems[i];
//					if(projectItem.getProjectModel().getJavaProject().equals(project)){
//						projectTreeItem = projectItem;
//						break;
//					}
//				}
//				TreeItem[] nodesTreeItems = projectTreeItem.getItems();
//				for (int i = 0; i < nodesTreeItems.length; i++) {
//					TreeItem treeItem = nodesTreeItems[i];
//					if(treeItem.getText().equals(PortalProjConstants.PORTLETS)){
//						nodeTreeItem = (PortalDirtoryTreeItem) treeItem;
//						break;
//					}
//				}
//				if(nodeTreeItem != null){
//					TreeItem[] portletTreeItems = nodeTreeItem.getItems();
//					for (int i = 0; i < portletTreeItems.length; i++) {
//						TreeItem treeItem = portletTreeItems[i];
//						if(treeItem.getText().equals(portlet.getPortletName())){
//							portletTreeItem = (PortletTreeItem) treeItem;
//							tree.setSelection(portletTreeItem);
//							break;
//						}
//					}
//				}
			}
		}catch(Throwable e){
			PortalPlugin.getDefault().logError(e.getMessage());
		}
	}
	
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		PortletEditorInput pEditorInput = (PortletEditorInput) input;
		PortletDefinition portlet = pEditorInput.getPortlet();
		
		PortletElementObj portletEle = new PortletElementObj();
		portletEle.setPortlet(portlet);
		portletEle.setCategoryId(pEditorInput.getCategoryId());
		portletEle.setInitParams((List<InitParam>)portlet.getInitParams());
		portletEle.setSupports((List<Supports>)portlet.getSupports());
		List<Preference> pf = portlet.getPortletPreferences().getPortletPreferences();
		Preference preference = null;
		for(int i = pf.size() - 1 ; i > -1 ; i--){
			preference =pf.get(i);
			if (PortalProjConstants.PREFERENCE_VERSION.equals(preference.getName())){
				portletEle.setVersion(preference.getValues().get(0));
				portlet.getPortletPreferences().getPortletPreferences().remove(preference);
			}
		}
		portletEle.setPortletPreferences(portlet.getPortletPreferences());
		portletEle.setPreferences(portlet.getPortletPreferences().getPortletPreferences());
		portletEle.setSupportedProcessingEvents((List<EventDefinitionReference>)portlet.getSupportedProcessingEvents());
		portletEle.setSupportedPublishingEvents((List<EventDefinitionReference>)portlet.getSupportedPublishingEvents());
		
		portletEle.setLocation(new Point(100, 100));
		contents = portletEle;
	}
	
	public void executComand(Command cmd){
		super.getCommandStack().execute(cmd);
	}
	
	public void savePortlet(PortletDefinition portlet,String categoryId) {
		// 获取项目路径
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortalConnector.savePortletToXml(projectPath, projectModuleName, portlet, categoryId);
		getCommandStack().markSaveLocation();
	}


	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(this.contents);
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
		getGraphicalViewer().setEditPartFactory(new PortletEditPartFactory());
	}


	
	public void doSave(IProgressMonitor monitor) {
		save();
	}
	
	public void save() {
		PortletElementObj portletEle = (PortletElementObj) contents;
		/*
		 * 更新版本号
		 */
		String oldVersion = portletEle.getVersion();
		String newVersion =  String.valueOf((Integer.valueOf(oldVersion)+1));
		Preference versionPreference = new Preference();
		versionPreference.setName(PortalProjConstants.PREFERENCE_VERSION);
		versionPreference.addValue(newVersion);
		versionPreference.setReadOnly(true);
		List<Preference> pf = portletEle.getPortlet().getPortletPreferences().getPortletPreferences();
		Preference preference = null;
		for(int i = pf.size() - 1 ; i > -1 ; i--){
			preference =pf.get(i);
			if (versionPreference.getName().equals(preference.getName())){
				portletEle.getPortlet().getPortletPreferences().getPortletPreferences().remove(preference);
			}
		}
		portletEle.getPortlet().getPortletPreferences().getPortletPreferences().add(versionPreference);
		savePortlet(portletEle.getPortlet(),portletEle.getCategoryId());
		portletEle.setVersion(newVersion);
	}
	
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class cls) {
		if (cls == ILFWViewPage.class) {
			this.viewPage = new PortletViewPage();
			return this.viewPage;
		}
		return super.getAdapter(cls);
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		return new PortletViewPage();
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
		// TODO 自动生成的方法存根
		
	}

}
