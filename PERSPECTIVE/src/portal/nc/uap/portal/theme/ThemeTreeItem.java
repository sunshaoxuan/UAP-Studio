package nc.uap.portal.theme;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Theme;
import nc.uap.portal.theme.action.DelThemeAction;
import nc.uap.portal.theme.action.EditThemeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Theme TreeItem
 * 
 * @author dingrf
 *
 */
public class ThemeTreeItem extends PortalBasicTreeItem{
	private ImageDescriptor imageDescriptor = null;
	
	private TreeItem parentItem;
	
	private boolean isCurrentBCP = false;
	
	public ThemeTreeItem(TreeItem parentItem, Theme theme) {
		this(parentItem, theme, true);
	}
	
	public ThemeTreeItem(TreeItem parentItem, Theme theme, boolean isCurrentBCP) {
		super(parentItem, SWT.NONE);
		this.parentItem = parentItem;
		setData(theme);
		this.isCurrentBCP = isCurrentBCP;
		if(!isCurrentBCP){
			setText(theme.getTitle()+M_portal.ThemeTreeItem_0);
		}else{
			setText(theme.getTitle());
		}
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
//		imageDescriptor = MainPlugin.loadImage(MainPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}
	
	public boolean getIsCurrentBCP(){
		return isCurrentBCP;
	}

	protected void checkSubclass () {
	}


	public void deleteNode() {
		
	}

	public File getFile() {
		if (parentItem != null && parentItem instanceof PortalDirtoryTreeItem) {
			PortalDirtoryTreeItem lfwItem = (PortalDirtoryTreeItem) parentItem;
			return lfwItem.getFile();
		}
		return null;
	}


	public String getIPathStr() {
		return null;
	}
	public void deleteNode(ThemeTreeItem themeTreeItem) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
//		LookAndFeel lookAndFeel = PortalConnector.getLookAndFeel(projectPath, projectModuleName);
//		
		Theme theme = (Theme) themeTreeItem.getData();
//		
//		for (Theme thm : lookAndFeel.getTheme()){
//			if (thm.getId().equals(theme.getId())){
//				lookAndFeel.getTheme().remove(thm);
//				break;
//			}
//		}
//		PortalConnector.saveLookAndFeelToXml(projectPath, projectModuleName, lookAndFeel);
		PortalConnector.updateLookAndFeelToXml(projectPath, theme, 3);
		
		if (MessageDialog.openConfirm(new Shell(), M_portal.ThemeTreeItem_1, M_portal.ThemeTreeItem_2)){
			PortalConnector.deleteThemeFolder(projectPath, projectModuleName,theme.getId());
		}
		
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
		EditThemeAction editThemeAction = new EditThemeAction();
		DelThemeAction delThemeAction = new DelThemeAction();
		manager.add(editThemeAction);
		manager.add(delThemeAction);
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
	} 
}
