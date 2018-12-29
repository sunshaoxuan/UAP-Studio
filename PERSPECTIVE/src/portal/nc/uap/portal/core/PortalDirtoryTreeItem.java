package nc.uap.portal.core;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.category.action.NewCategoryAction;
//import nc.uap.portal.managerapps.action.NewMangerAppsAction;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.page.action.CreatePageAction;
import nc.uap.portal.page.action.NewPageAction;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalTreeBuilder;
import nc.uap.portal.portlets.action.NewFunctionPortletAction;
import nc.uap.portal.portlets.action.NewIframePortletAction;
import nc.uap.portal.portlets.action.NewJspPortletAction;
import nc.uap.portal.portlets.action.NewPortletAction;
import nc.uap.portal.theme.action.NewThemeAction;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 文件夹树节点
 * 
 * @author dingrf
 *
 */
public class PortalDirtoryTreeItem extends PortalBasicTreeItem{

	/**文件夹类型*/
	private String type = ""; //$NON-NLS-1$
	
	/**图片描述符*/
	private ImageDescriptor imageDescriptor = null;
	
	public PortalDirtoryTreeItem(TreeItem parentItem, Object object, String text){
		super(parentItem, SWT.NONE);
		setData(object);
		setText(text);
		setImage(getDirImage());
	}

	public PortalDirtoryTreeItem(TreeItem parentItem, File file) {
		this(parentItem, file, file.getName());
	}

	
	protected Image getDirImage() {
//	 imageDescriptor = MainPlugin.loadImage(
//			 MainPlugin.ICONS_PATH, "fmodel-function.png");
		return ImageProvider.fmodel_function;
	}

	public File getFile() {
		return (File) getData();
	}

	/**
	 * 删除节点
	 * 
	 */
	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		dispose();
	}

	/***
	 * 得到文件路径
	 */
	public String getIPathStr() {
		String parentIPath = ""; //$NON-NLS-1$
		TreeItem parent = getParentItem();
		if (parent instanceof ILFWTreeNode) {
			parentIPath = ((ILFWTreeNode) parent).getIPathStr();
		}
		return parentIPath + "/" + getFile().getName(); //$NON-NLS-1$

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	@Override
	public void addMenuListener(IMenuManager manager){
		if(PortalDirtoryTreeItem.PORTALPAGE.equals(getType())){
			NewPageAction newPageAction = new NewPageAction();
			manager.add(newPageAction);
			CreatePageAction createPageAction = new CreatePageAction();
			manager.add(createPageAction);
		}
		else if(PortalDirtoryTreeItem.PORTLETS.equals(getType())){
			NewCategoryAction newCategoryAction = new NewCategoryAction();
			MenuManager portletMenuManager = new MenuManager(M_portal.PortalDirtoryTreeItem_0);
			portletMenuManager.setRemoveAllWhenShown(true);
			portletMenuManager.addMenuListener(new IMenuListener() {
				NewPortletAction newPortletAction = new NewPortletAction();
				NewFunctionPortletAction newFuncPortletAction = new NewFunctionPortletAction();
				NewJspPortletAction newJspPortletAction = new NewJspPortletAction();
				NewIframePortletAction newIframePortletAction = new NewIframePortletAction();
				public void menuAboutToShow(IMenuManager manager) {
					manager.add(newPortletAction);
					manager.add(newFuncPortletAction);
					manager.add(newJspPortletAction);
					manager.add(newIframePortletAction);
				}
			});
			manager.add(newCategoryAction);
//			manager.add(portletMenuManager);
		}
//		else if(PortalDirtoryTreeItem.MANAGERAPPS.equals(getType())){
//			NewMangerAppsAction newMangerAppsAction = new NewMangerAppsAction();
//			manager.add(newMangerAppsAction);
//		}
		else if(PortalDirtoryTreeItem.THEMES.equals(getType())){
			NewThemeAction newThemeAction = new NewThemeAction();
			manager.add(newThemeAction);
		}
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	@Override
	public void mouseDoubleClick(){
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if (getType().equals(PortalDirtoryTreeItem.PORTAL_DEFINE)){
			view.openPortalModuleEditor(this);
			setExpanded(true);
		}
		else if (getType().equals(PortalDirtoryTreeItem.PORTALPAGE)){
			if (getItemCount() ==0){
				PortalTreeBuilder.getInstance().initPageNodeTree(this, LFWPersTool.getCurrentProjectModuleName());
				setExpanded(true);
			}
			else{
				setExpanded(!getExpanded());
			}
		}
		else if (getType().equals(PortalDirtoryTreeItem.PORTLETS)){
			if (getItemCount() ==0){
				PortalTreeBuilder.getInstance().initCategory(this, LFWPersTool.getCurrentProjectModuleName());
				setExpanded(true);
			}
			else{
				setExpanded(!getExpanded());
			}
		}
		else if (getType().equals(PortalDirtoryTreeItem.MANAGERAPPS)){
			if (getItemCount() ==0){
				PortalTreeBuilder.getInstance().initManagerApps(this, LFWPersTool.getCurrentProjectModuleName());
				setExpanded(true);
			}
			else{
				setExpanded(!getExpanded());
			}
		}
		else if (getType().equals(PortalDirtoryTreeItem.THEMES)){
			if (getItemCount() ==0){
				PortalTreeBuilder.getInstance().initTheme(this, LFWPersTool.getCurrentProjectModuleName());
				setExpanded(true);
			}
			else{
				setExpanded(!getExpanded());
			}
		}
		else if (getType().equals(PortalDirtoryTreeItem.PORTAL)){
			view.openPortalModuleEditor(this);
			setExpanded(true);
		}
		else if (getType().equals(PortalDirtoryTreeItem.EVENTS)){
			view.openEventsEditor(this);
			setExpanded(true);
		}
		else if (getType().equals(PortalDirtoryTreeItem.WEBCONFIG)){
			view.openWebConfigEditor(this);
			setExpanded(true);
		}
	} 
}
