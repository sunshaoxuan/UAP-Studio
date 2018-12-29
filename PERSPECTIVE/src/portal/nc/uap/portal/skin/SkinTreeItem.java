package nc.uap.portal.skin;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.window.WindowBrowserEditor;
import nc.uap.lfw.editor.window.WindowEditorInput;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.perspective.PortalPlugin;
import nc.uap.portal.skin.action.DelSkinAction;
import nc.uap.portal.skin.action.EditSkinAction;
import nc.uap.portal.theme.ThemeTreeItem;
import nc.uap.portal.theme.ThemeTypeTreeItem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Skin TreeItem
 * 
 * @author dingrf
 *
 */
public class SkinTreeItem extends PortalBasicTreeItem{
	
	private ImageDescriptor imageDescriptor = null;
	
	private boolean isCurrentBCP = false;
	
	public SkinTreeItem(TreeItem parentItem, Skin skin) {
		super(parentItem, SWT.NONE);
		ThemeTreeItem themeTreeItem = (ThemeTreeItem)parentItem.getParentItem();
		this.isCurrentBCP = themeTreeItem.getIsCurrentBCP();
		setData(skin);
		setText(skin.getName()+"["+skin.getId()+"]"); //$NON-NLS-1$ //$NON-NLS-2$
		setImage(getDirImage());
	}
	
	private PortalBaseEditorInput editorInput;

	public PortalBaseEditorInput getEditorInput() {
		return editorInput;
	}

	public void setEditorInput(PortalBaseEditorInput editorInput) {
		this.editorInput = editorInput;
	}
	
	protected Image getDirImage() {
//		imageDescriptor = PortalPlugin.loadImage(PortalPlugin.ICONS_PATH, "skin.gif");
		return ImageProvider.skin;
	}

	@Override
	public void deleteNode() {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();

		ThemeTypeTreeItem themeTypeTreeItem = (ThemeTypeTreeItem)this.getParentItem(); 
		String type = themeTypeTreeItem.getType();
		String themeId = ((Theme)themeTypeTreeItem.getData()).getId();
		
		
//		SkinDescription skinDescription  = PortalConnector.getSkinDescription(projectPath, projectModuleName, type, themeId);
		
		Skin skin = (Skin) this.getData();
		PortalConnector.updateSkinDescription(projectPath, skin, 3);
		PortalConnector.deleteSkinFile(projectPath, skin);
//		if (skinDescription != null){
//			for (Skin s: skinDescription.getSkin()){
//				if(s.getId().equals(skin.getId())){
//					skinDescription.getSkin().remove(s);
//					break;
//				}
//			}
//		}
//		PortalConnector.saveSkinDescription(projectPath, projectModuleName, type, themeId, skinDescription);
		
//		PortalConnector.deleteSkinFile(projectPath, projectModuleName, type, themeId, skin.getId()+".ftl");
//		if (type.equals(PortalProjConstants.TYPE_PORTLET)){
//			PortalConnector.deleteSkinFile(projectPath, projectModuleName, type, themeId, skin.getId()+".css");
//			PortalConnector.deleteSkinFile(projectPath, projectModuleName, type, themeId, skin.getId()+".js");
//		}
		dispose();
		
	}
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	public void addMenuListener(IMenuManager manager){
		if(!isCurrentBCP){
			return;
		}
		EditSkinAction editSkinAction = new EditSkinAction();
		DelSkinAction delSkinAction = new DelSkinAction();
		manager.add(editSkinAction);
		manager.add(delSkinAction);
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
		IProject project = LFWPersTool.getCurrentProject();
		String projectName = LFWPersTool.getCurrentProjectModuleName();
		Skin skin = (Skin) getData();
		IFile currentfile = project.getFile(projectName + "/web/tpl/" + skin.getThemeid()+"/"+skin.getStype()+"/"+skin.getId()+".html"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if(!currentfile.exists()){
			MessageDialog.openWarning(null, M_portal.SkinTreeItem_0, M_portal.SkinTreeItem_1);
			return;
		}
		
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
//		view.openSkinEditor(this);
		IWorkbenchPage workbenchPage = view.getViewSite().getPage();
		IEditorReference[] ers = workbenchPage.getEditorReferences();
		IEditorPart editorPart = null;
		for (int i = 0; i < ers.length; i++) {
			editorPart = ers[i].getEditor(true);
			if(editorPart instanceof TextEditor){
				TextEditor textEditor = (TextEditor)editorPart;
				IFile file = ((FileEditorInput)textEditor.getEditorInput()).getFile();
				if (file.getLocation() != null && (file.getLocation().toOSString().equals(currentfile.getLocation().toOSString()))) {
					break;
				}
			}
			editorPart = null;
		}
		if (editorPart != null)
			workbenchPage.bringToTop(editorPart);
		else {
			try {
				String editorid = "org.eclipse.ui.DefaultTextEditor"; //$NON-NLS-1$
				workbenchPage.openEditor(new FileEditorInput(currentfile), editorid);
			} catch (PartInitException e) {
				PortalPlugin.getDefault().logError(e);
			}
		}
	} 
}
