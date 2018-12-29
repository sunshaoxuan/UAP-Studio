package nc.uap.portal.theme;

import java.io.File;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.om.Theme;
import nc.uap.portal.skin.action.NewSkinAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ThemeType TreeItem
 * 
 * @author dingrf
 *
 */
public class ThemeTypeTreeItem extends PortalBasicTreeItem{
	private ImageDescriptor imageDescriptor = null;
	
	private TreeItem parentItem;
	
	private String type;
	
	private boolean isCurrentBCP = false;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ThemeTypeTreeItem(TreeItem parentItem, Theme theme,String type) {
		super(parentItem, SWT.NONE);
		this.parentItem = parentItem;
		ThemeTreeItem themeTreeItem = (ThemeTreeItem)parentItem;
		this.isCurrentBCP = themeTreeItem.getIsCurrentBCP();
		this.type = type;
		setData(theme);
		setText(type);
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
//		imageDescriptor = MainPlugin.loadImage(MainPlugin.ICONS_PATH, "virtualdir.gif");
		return ImageProvider.virtualdir;
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
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	public void addMenuListener(IMenuManager manager){
		if(!isCurrentBCP){
			return;
		}
		NewSkinAction newSkinAction = new NewSkinAction();
		manager.add(newSkinAction);
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
	} 
}
