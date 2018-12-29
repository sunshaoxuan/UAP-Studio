package nc.uap.portal.portlets.action;

import java.io.InputStream;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalTools;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.portlets.PortletTreeItem;
import nc.uap.portal.portlets.dialog.IframePortletDialog;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建iframe Portlet命令
 * @author dingrf
 *
 */

public class NewIframePortletAction  extends Action {
	
	private String templateName = "iframePortlet";
	
	public NewIframePortletAction() {
		this.setText(PortalProjConstants.NEW_IFRAMEPORTLET);
		this.setImageDescriptor(PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		IframePortletDialog iframeportletDialog = new IframePortletDialog(new Shell(), PortalProjConstants.NEW_IFRAMEPORTLET);
		if(iframeportletDialog.open() == InputDialog.OK){
			String id = iframeportletDialog.getId(); 
			String name = iframeportletDialog.getName();
			String url = iframeportletDialog.getUrl();
			
			Tree tree = view.getTreeView().getTree();
			TreeItem[] selTIs = tree.getSelection();
			TreeItem selTI = selTIs[0];

			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			PortletApplicationDefinition portletApp = PortalConnector.getPortletApp(projectPath, projectModuleName);

			/*判断ID重复*/
			if (PortalTools.getPortletById(portletApp, id) != null){
				MessageDialog.openError(shell, "错误提示", "已经存在id为"+ id +"的Portlet!");
				return;
			}
			
			try {
				PortletDefinition p = null;
				String portletClassPath = "nc/uap/portal/portlets/template/portlet.xml";
				InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(portletClassPath);
				String portaletAppText= FileUtilities.fetchFileContent(ins,"UTF-8"); 
				PortletApplicationDefinition templatePortletApp = PortalConnector.getPortletApp(projectPath, projectModuleName, portaletAppText);
				p = PortalTools.getPortletById(templatePortletApp, templateName);
				if (p == null){
					MessageDialog.openError(shell, "错误提示", "没有找到模板："+ templateName);
					return;
				}
				p.setPortletName(id);
				DisplayName displayName = new DisplayName();
				displayName.setDisplayName(name);
				p.getDisplayNames().clear();
				p.getDisplayName().add(displayName);
				for(Preference prf : p.getPortletPreferences().getPortletPreferences()){
					if(prf.getName().equals("if_src")){
						prf.getValues().clear();
						prf.getValues().add(url);
						break;
					}
				}
				
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
				String title =PortalProjConstants.NEW_IFRAMEPORTLET;
				String message = e.getMessage();
				MessageDialog.openError(shell, title, message);
			}
		}
		else return;
		
	}
}

