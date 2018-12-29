
package nc.uap.portal.category.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 用户是否可定制
 * @author dingrf
 */
public class DynamicAction extends Action {

	/**现有状态*/
	private Boolean oldDynamic;
	
	public DynamicAction(Boolean oldDynamic,String imageName) {
		super(PortalProjConstants.DYNAMICNAME, MainPlugin.loadImage(MainPlugin.ICONS_PATH, imageName));
		this.oldDynamic = oldDynamic;
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];
		PortletDisplayCategory portletDisplayCategory = (PortletDisplayCategory)selTI.getParentItem().getData();
		//取Display配置信息
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
		for (PortletDisplayCategory pdc : display.getCategory()){
			if (pdc.getId().equals(portletDisplayCategory.getId())){
				updateDynamic(portletDisplayCategory,selTI);
				break;
			}
		}
		PortalConnector.saveDisplayToXml(projectPath, projectModuleName, display);
	}

	/**
	 * 更新用户可定制状态
	 * 
	 * @param portletDisplayCategory
	 * @param selTI
	 */
	private void  updateDynamic(PortletDisplayCategory portletDisplayCategory,TreeItem selTI){
		PortletDefinition portlet = (PortletDefinition) selTI.getData();
		for (PortletDisplay pd : portletDisplayCategory.getPortletDisplayList()){
			if (pd.getId().equals(portlet.getPortletName())){
				pd.setDynamic(!oldDynamic);
				selTI.getParentItem().setData(portletDisplayCategory);
				break;
			}
		}
	}
}
