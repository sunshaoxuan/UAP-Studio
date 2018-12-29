package nc.uap.portal.category;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.category.action.DeleteCategoryAction;
import nc.uap.portal.category.action.EditCategoryAction;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.portlets.PortletTreeItem;
import nc.uap.portal.portlets.action.NewFunctionPortletAction;
import nc.uap.portal.portlets.action.NewIframePortletAction;
import nc.uap.portal.portlets.action.NewJspPortletAction;
import nc.uap.portal.portlets.action.NewPortletAction;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * portlet分类树节点
 * 
 * @author dingrf
 *
 */
public class CategoryTreeItem extends PortalBasicTreeItem{
	
	/**树节点图片描述符*/
	private ImageDescriptor imageDescriptor = null;
	
	/**父节点*/
	private TreeItem parentItem;
	
	public CategoryTreeItem(TreeItem parentItem, PortletDisplayCategory category) {
		super(parentItem, SWT.NONE);
		this.parentItem = parentItem;
		setData(category);
		setText(M_portal.CategoryTreeItem_0 + category.getText());
		setImage(getDirImage());
	}
	
	public CategoryTreeItem(TreeItem parentItem, PortletDisplayCategory category,int index) {
		super(parentItem, SWT.NONE,index);
		this.parentItem = parentItem;
		setData(category);
		setText(M_portal.CategoryTreeItem_0 + category.getText());
		setImage(getDirImage());
	}
	
	/**
	 * 取树节点图片
	 * @return
	 */
	protected Image getDirImage() {
//		imageDescriptor = MainPlugin.loadImage(MainPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}

	public TreeItem getParentItem() {
		return parentItem;
	}

	public void setParentItem(TreeItem parentItem) {
		this.parentItem = parentItem;
	}

	/**
	 * 刷新node
	 * 
	 */
	public void refreshNode(CategoryTreeItem categoryTreeItem) {
		PortletDisplayCategory portletDisplayCategory = (PortletDisplayCategory)categoryTreeItem.getData();
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
		for (PortletDisplayCategory pdc : display.getCategory()){
			if (pdc.getId().equals(portletDisplayCategory.getId())){
				categoryTreeItem.setData(pdc);
				break;
			}
		}
	}
	
	/**
	 * 删除node
	 * 
	 */
	@Override
	public void deleteNode() {
			TreeItem[] treeItems = this.getItems();
			if (treeItems.length >0){
				if (MessageDialog.openQuestion(new Shell(), M_portal.CategoryTreeItem_1, M_portal.CategoryTreeItem_2)){
					for (int i =treeItems.length-1 ; i>-1;i--){
						PortletTreeItem portletTreeItem = (PortletTreeItem)treeItems[i];
						portletTreeItem.deleteNode();
					}
				}
				else{
					TreeItem parentItem = this.getParentItem();
					for (int i =0 ; i< treeItems.length;i++){
						PortletDefinition portlet = (PortletDefinition) treeItems[i].getData();
						new PortletTreeItem(parentItem, portlet);
						treeItems[i].dispose();
					}
				}
			}
		PortletDisplayCategory portletDisplayCategory = (PortletDisplayCategory)this.getData();
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
		for (PortletDisplayCategory pdc : display.getCategory()){
			if (pdc.getId().equals(portletDisplayCategory.getId())){
				display.getCategory().remove(pdc);
				break;
			}
		}
		PortalConnector.saveDisplayToXml(projectPath, projectModuleName, display);

		dispose();
	}
	
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	@Override
	public void addMenuListener(IMenuManager manager){
		MenuManager portletMenuManager = new MenuManager(M_portal.CategoryTreeItem_3);
		portletMenuManager.setRemoveAllWhenShown(true);
		portletMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				NewPortletAction newPortletAction = new NewPortletAction();
				NewFunctionPortletAction newFuncPortletAction = new NewFunctionPortletAction();
				NewJspPortletAction newJspPortletAction = new NewJspPortletAction();
				NewIframePortletAction newIframePortletAction = new NewIframePortletAction();
				manager.add(newPortletAction);
				manager.add(newFuncPortletAction);
				manager.add(newJspPortletAction);
				manager.add(newIframePortletAction);
			}
		});
		EditCategoryAction editCategoryAction = new EditCategoryAction();
		DeleteCategoryAction deleteCategoryAction = new DeleteCategoryAction();
		manager.add(portletMenuManager);
		manager.add(editCategoryAction);
		manager.add(deleteCategoryAction);
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	@Override
	public void mouseDoubleClick(){
	} 
	
}
