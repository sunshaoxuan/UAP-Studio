package nc.uap.portal.perspective.dnd;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.portlets.PortletTreeItem;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 树拖拽事件
 * 
 * @author dingrf
 *
 */

public class PortalDropTargetListener implements DropTargetListener {

	private TreeItem[] dragSourceItem = null;
	
	public TreeItem[] getDragSourceItem() {
		return dragSourceItem;
	}

	public void setDragSourceItem(TreeItem[] dragSourceItem) {
		this.dragSourceItem = dragSourceItem;
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL |DND.FEEDBACK_SELECT;
	}

	@Override
	public void drop(DropTargetEvent event) {
		// portlet的拖拽
		if (dragSourceItem[0] instanceof PortletTreeItem){ 
			if (event.item instanceof CategoryTreeItem || 
					(event.item instanceof PortalDirtoryTreeItem && PortalDirtoryTreeItem.PORTLETS.equals(((PortalDirtoryTreeItem) event.item).getType()))){
				PortletDefinition portlet = (PortletDefinition)((PortletTreeItem) dragSourceItem[0]).getData();
				new PortletTreeItem((TreeItem) event.item, portlet);
				// 保存到xml文件
				PortletDisplayCategory sourceCategory = null;
				PortletDisplayCategory targetCategory = null;
				PortletDisplay  portletDisplay = null;
				if (dragSourceItem[0].getParentItem() instanceof CategoryTreeItem){
					sourceCategory = (PortletDisplayCategory) dragSourceItem[0].getParentItem().getData();
					for (PortletDisplay pd : sourceCategory.getPortletDisplayList()){
						if (pd.getId().equals(portlet.getPortletName())){
							sourceCategory.getPortletDisplayList().remove(pd);
							break;
						}
					}
				}
				if (event.item instanceof CategoryTreeItem){
					targetCategory = (PortletDisplayCategory) event.item.getData();
				}
				String projectPath = LFWPersTool.getProjectWithBcpPath();
				String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
				Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
				// 从源分类中删除portlet
				if (sourceCategory != null){
					for (PortletDisplayCategory pdc : display.getCategory()){
						if (pdc.getId().equals(sourceCategory.getId())){
							removeCategory(pdc,portlet,portletDisplay,sourceCategory);
							break;
						}
					}
				}	
				//增加到目标分类中
				for (PortletDisplayCategory pdc : display.getCategory()){
					if (targetCategory != null && pdc.getId().equals(targetCategory.getId())){
//						if (portletDisplay != null){
//							pdc.getPortletDisplayList().add(portletDisplay);
//						}
//						else{
							portletDisplay = new PortletDisplay();
							portletDisplay.setId(portlet.getPortletName());
							portletDisplay.setTitle(portlet.getDisplayNames().size()>0?portlet.getDisplayNames().get(0).getDisplayName():"");
							portletDisplay.setDynamic(true);
							pdc.getPortletDisplayList().add(portletDisplay);
//						}
						targetCategory.getPortletDisplayList().add(portletDisplay);
						break;
					}
				}
				PortalConnector.saveDisplayToXml(projectPath, projectModuleName, display);
			}
			else{
				event.detail = DND.DROP_NONE;
			}
		}
		else{
			event.detail = DND.DROP_NONE;
		}

	}

	private void removeCategory(PortletDisplayCategory pdc,PortletDefinition portlet,
			PortletDisplay portletDisplay,PortletDisplayCategory sourceCategory){
		for (PortletDisplay pd : pdc.getPortletDisplayList()){
			if (pd.getId().equals(portlet.getPortletName())){
				portletDisplay = pd;
				pdc.getPortletDisplayList().remove(portletDisplay);
				sourceCategory.getPortletDisplayList().remove(portletDisplay);
				break;
			}
		}	
	}
	
	@Override
	public void dropAccept(DropTargetEvent event) {
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
	}

}
