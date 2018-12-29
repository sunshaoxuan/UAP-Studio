package nc.uap.portal.portlets;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.category.action.DynamicAction;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.portlets.action.DeletePortletAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * portlet 树节点
 * 
 * @author dingrf
 *
 */
public class PortletTreeItem extends PortalBasicTreeItem{
	
	private ImageDescriptor imageDescriptor = null;
	
	public PortletTreeItem(TreeItem parentItem, PortletDefinition portlet) {
		super(parentItem, SWT.NONE);
		setData(portlet);
		setText(portlet.getDisplayNames().isEmpty()?"":portlet.getDisplayNames().get(0).getDisplayName());
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
//		imageDescriptor = MainPlugin.loadImage(MainPlugin.ICONS_PATH, "portlet.gif");
		return ImageProvider.portlet;
	}

	public void deleteNode() {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortletApplicationDefinition  portletApp = PortalConnector.getPortletApp(projectPath,projectModuleName);
		PortletDefinition portletDefinition = (PortletDefinition)this.getData();
		if(portletApp.getPortlet(portletDefinition.getPortletName())!=null){
			portletApp.getPortlets().remove(portletApp.getPortlet(portletDefinition.getPortletName()));
			PortalConnector.savePortletAppToXml(projectPath,projectModuleName, portletApp);
		}
		
		/**
		 * 删除 portlet分类中的配置
		 */
		if (this.getParentItem() instanceof CategoryTreeItem){
			Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
			PortletDisplayCategory portletDisplayCategory = (PortletDisplayCategory)this.getParentItem().getData();
			for (PortletDisplayCategory pdc : display.getCategory()){
				if (pdc.getId().equals(portletDisplayCategory.getId())){
					removeDisplay(pdc,portletDefinition);
					break;
				}
			}
			PortalConnector.saveDisplayToXml(projectPath,projectModuleName, display);
		}
		dispose();
		
	}
	
	private void removeDisplay(PortletDisplayCategory pdc,PortletDefinition portletDefinition){
		for (PortletDisplay pd : pdc.getPortletDisplayList()){
			if (pd.getId().equals(portletDefinition.getPortletName())){
				pdc.getPortletDisplayList().remove(pd);
				break;
			}
		}
	}
	
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	public void addMenuListener(IMenuManager manager){
		DeletePortletAction deletePortletAction = new DeletePortletAction();
		manager.add(deletePortletAction);
		if (getParentItem() instanceof CategoryTreeItem){
			PortletDisplayCategory portletDisplayCategory = (PortletDisplayCategory) getParentItem().getData();
			PortletDefinition portlet = (PortletDefinition) getData();
			for (PortletDisplay pd : portletDisplayCategory.getPortletDisplayList()){
				if (pd.getId().equals(portlet.getPortletName())){
					if (pd.getDynamic()){
						manager.add(new DynamicAction(true,"checked.gif"));
					}
					else{
						manager.add(new DynamicAction(false,"un_checked.gif"));
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		view.openPortletEditor(this);
	} 
}
