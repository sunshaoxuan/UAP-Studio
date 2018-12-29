package nc.uap.portal.portlets.action;

import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalTools;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.portlets.PortletTreeItem;
import nc.uap.portal.portlets.dialog.PortletDialog;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建Portlet命令
 * @author dingrf
 *
 */

public class NewPortletAction  extends Action {

	
	public NewPortletAction() {
		this.setText(PortalProjConstants.NEW_PORTLET);
		this.setImageDescriptor(PaletteImage.getCreateGridImgDescriptor());
	}

	@SuppressWarnings("unchecked")
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		PortletDialog portletDialog = new PortletDialog(new Shell(), PortalProjConstants.NEW_PORTLET);
		if(portletDialog.open() == InputDialog.OK){
			String id = portletDialog.getId(); 
			String name = portletDialog.getName();
			
			Tree tree = view.getTreeView().getTree();
			TreeItem[] selTIs = tree.getSelection();
			TreeItem selTI = selTIs[0];
			TreeItem[] subItems= selTI.getItems();
			
			
			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			PortletApplicationDefinition portletApp = PortalConnector.getPortletApp(projectPath, projectModuleName);

			/*判断ID重复*/
			if (portletApp != null &&PortalTools.getPortletById(portletApp, id) != null){
				MessageDialog.openError(shell, "错误提示", "已经存在id为"+ id +"的Portlet!");
				return;
			}
			
			for (int i=0;i<subItems.length;i++){
				PortletDefinition portlet = (PortletDefinition)(subItems[i].getData());
				if (portlet.getPortletName().equals(id)){
					MessageDialog.openError(shell, "错误提示", "已经存在id为"+ id +"的Portlet!");
					return;
				}
			}
			try {
				PortletDefinition p = new PortletDefinition();
				p.setPortletName(id);
				DisplayName displayName = new DisplayName();
				displayName.setDisplayName(name);
				((List<DisplayName>)p.getDisplayNames()).add(displayName);
				p.getPortletInfo().setTitle(name);
				Supports supports = new Supports();
				supports.setMimeType("text/html");
				((List<Supports>)p.getSupports()).add(supports);
				
				PortletTreeItem pl = (PortletTreeItem)view.addPortletTreeNode(p);

				//保存portlet
				String categoryId = null;
				if (selTI instanceof CategoryTreeItem){
					categoryId = ((PortletDisplayCategory)selTI.getData()).getId();
				}
				PortalConnector.savePortletToXml(projectPath, projectModuleName, p, categoryId);
				
				//打开ds编辑器
				view.openPortletEditor(pl);
				} catch (Exception e) {
				String title =PortalProjConstants.NEW_PORTLET;
				String message = e.getMessage();
				MessageDialog.openError(shell, title, message);
			}
		}
		else return;
	}
}

