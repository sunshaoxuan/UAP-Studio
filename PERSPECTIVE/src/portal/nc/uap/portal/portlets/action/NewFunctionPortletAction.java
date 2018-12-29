package nc.uap.portal.portlets.action;

import java.io.InputStream;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.palette.PaletteImage;
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
import nc.uap.portal.portlets.dialog.FuncPortletDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NewFunctionPortletAction extends Action {
	private String templateName = "floatFuncPortlet";
	
	public NewFunctionPortletAction() {
		this.setText(PortalProjConstants.NEW_FUNCPORTLET);
		this.setImageDescriptor(PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		FuncPortletDialog jspPortletDialog = new FuncPortletDialog(new Shell(), PortalProjConstants.NEW_JSPPORTLET);
		if(jspPortletDialog.open() == InputDialog.OK){
			String id = jspPortletDialog.getId(); 
			String name = jspPortletDialog.getName();
			String cate = jspPortletDialog.getCate();
			
			
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
				
				p.getSupports().clear();
				
				Preference pref = p.getPortletPreferences().getPortletPreference("linkgroup");
//				pref.setName("linkgroup");
				pref.addValue(cate);
//				if (!viewjsp.equals("")){
//					setJspInitParam(p,"view-jsp",viewjsp,"view");
//				}
//				if (!editjsp.equals("")){
//					setJspInitParam(p,"edit-jsp",editjsp,"edit");
//				}
//				if (!helpjsp.equals("")){
//					setJspInitParam(p,"help-jsp",helpjsp,"help");
//				}
				
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
				String title =PortalProjConstants.NEW_JSPPORTLET;
				String message = e.getMessage();
				MessageDialog.openError(shell, title, message);
			}
		}
		else return;
	}
	
//	@SuppressWarnings("unchecked")
//	private void setJspInitParam(PortletDefinition p ,String name,String value,String portletMode){
//			InitParam initParam = new InitParam();
//			initParam.setParamName(name);
//			initParam.setParamValue(value);
//			p.getInitParams();
//			p.getInitParam().add(initParam);
//			if(p.getSupports().isEmpty()){
//				Supports supports = new Supports();
//				supports.setMimeType("text/html");
//				supports.getPortletModes().add(portletMode);
//				((List<Supports>)p.getSupports()).add(supports);
//			}
//			else{
//				List<String> portletModes =  p.getSupports().get(0).getPortletModes();
//				if (portletModes.indexOf(portletMode) == -1){
//					portletModes.add(portletMode);
//				}
//			}
//		}
}
