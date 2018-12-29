package nc.uap.portal.portlets.action;

import java.io.InputStream;

import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalTools;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * page导出成portlet
 * @author dingrf
 *
 */

public class ExportPageToPortletAction  extends Action {

	
	public ExportPageToPortletAction() {
		this.setText(PortalProjConstants.EXPORT_PATE_TO_PORTLET);
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];
		Shell shell = new Shell(Display.getCurrent());
		if (!(selTI instanceof LFWPageMetaTreeItem)){
			MessageDialog.openError(shell, "错误提示", "必须在Page节点上导出portlet!");
			return;
		}
		
		try {
			String id = ((LFWPageMetaTreeItem)selTI).getPageId();
			String name = ((LFWPageMetaTreeItem)selTI).getText();
	
			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			PortletApplicationDefinition portletApp = PortalConnector.getPortletApp(projectPath, projectModuleName);

			/*判断ID重复*/
			PortletDefinition p = PortalTools.getPortletById(portletApp, id);
			if (p != null){
				MessageDialog.openError(shell, "错误提示", "已经存在id为"+ id +"的Portlet!");
				return;
			}
		
			/*从iframePortlet模板创建*/
			String portletClassPath = "nc/uap/portal/portlets/template/portlet.xml";
			InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(portletClassPath);
			String portaletAppText= FileUtilities.fetchFileContent(ins,"UTF-8"); 
			PortletApplicationDefinition templetPortletApp = PortalConnector.getPortletApp(projectPath, projectModuleName, portaletAppText);
			p = PortalTools.getPortletById(templetPortletApp, "iframePortlet");
			if (p == null){
				MessageDialog.openError(shell, "错误提示", "没有找到模板：iframePortlet");
				return;
			}
			
			
			p.setPortletName(id);
			DisplayName displayName = new DisplayName();
			displayName.setDisplayName(name);
			p.getDisplayNames().clear();
			p.getDisplayName().add(displayName);
			
			/* 填入url*/
			String url = "core/uimeta.um?pageId="+id;
			for(Preference prf : p.getPortletPreferences().getPortletPreferences()){
				if(prf.getName().equals("if_src")){
					prf.getValues().clear();
					prf.getValues().add(url);
					break;
				}
			}
			
			/*保存portlet*/
			PortalConnector.savePortletToXml(projectPath, projectModuleName, p,null);
			
			MessageDialog.openInformation(shell, "信息", "导出成功");
			} catch (Exception e) {
			String title =PortalProjConstants.EXPORT_PATE_TO_PORTLET;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}

