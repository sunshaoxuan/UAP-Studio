/**
 * 
 */
package nc.uap.lfw.publicview;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.window.view.CreateCodeViewDialog;
import nc.uap.lfw.window.view.CreateNormalViewDialog;
import nc.uap.lfw.window.view.ViewTypeDialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * 新建PublicView节点类
 * @author chouhl
 *
 */
public class PublicViewNodeAction extends NodeAction {

	public PublicViewNodeAction() {
		super(WEBPersConstants.NEW_PUBLIC_VIEW);
	}

	public void run() {
		createPublicView();
	}
	
	private void createPublicView(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null){
			return;
		}
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		ViewTypeDialog typeDialog = new ViewTypeDialog(WEBPersConstants.NEW_VIEW);
		if(typeDialog.open() == IDialogConstants.OK_ID){
			if(typeDialog.isNormalView()){
				CreateNormalViewDialog dialog = new CreateNormalViewDialog(WEBPersConstants.NEW_PUBLIC_VIEW);
				dialog.setCreateView(false);
				if(dialog.open() == IDialogConstants.OK_ID){
					String publicViewId = dialog.getId().trim();
					String publicViewName = dialog.getName().trim();
					LfwView widget = new LfwView();
					widget.setId(publicViewId);
					widget.setCaption(publicViewName);
					widget.setRefId(publicViewId);
					widget.setControllerClazz(dialog.getControllerClazz());
					widget.setSourcePackage(dialog.getSourcePackage());
					if(LFWPersTool.getCurrentTreeItem() instanceof LFWComponentTreeItem){
						LFWComponentTreeItem compItem = (LFWComponentTreeItem)LFWPersTool.getCurrentTreeItem();
						String componentId = compItem.getComponent().getId();
						String pack = compItem.getComponent().getPack();
						if(pack==null||"".equals(pack))
							widget.setComponentId(componentId);
						else 
							widget.setComponentId(pack+"."+componentId);
					}
//					widget.setExtendAttribute(LfwView.POOLWIDGET, LfwView.POOLWIDGET);
					try{
						UIMeta meta = new UIMeta();
						String folderPath = LFWPersTool.getCurrentFolderPath();
						String filePath = folderPath + File.separator + widget.getId();
						String fp = filePath.replaceAll("\\\\", "/");
						String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
						meta.setId(id);
						meta.setFlowmode(dialog.isFlowlayout());
						
						//保存PublicView节点到文件
						LFWSaveElementTool.createView(widget, meta);
						String widgetPath = LFWPersTool.getCurrentFolderPath();
						File widgetfile = new File(widgetPath + "/"+ widget.getId());
						TreeItem parentTreeItem = LFWAMCPersTool.getTree().getSelection()[0];
						LFWWidgetTreeItem widgetTreeItem = new LFWWidgetTreeItem(parentTreeItem, widgetfile, widget, widget.getCaption()+"["+ widget.getId()+ "]");
						LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) widgetTreeItem;
						direc.setId(widget.getId());
						direc.setItemName(widget.getCaption());
						widgetTreeItem.setType(LFWDirtoryTreeItem.POOLWIDGETFOLDER);
						view.detalWidgetTreeItem(widgetTreeItem, widgetfile, widget);
					}catch(Exception e){
						MainPlugin.getDefault().logError(WEBPersConstants.NEW_PUBLIC_VIEW + "节点失败:" + e.getMessage(), e);
						MessageDialog.openError(null, WEBPersConstants.NEW_PUBLIC_VIEW, WEBPersConstants.NEW_PUBLIC_VIEW + "节点失败:" + e.getMessage());
					}
				}
			}
			else{
				CreateCodeViewDialog dialog = new CreateCodeViewDialog(WEBPersConstants.NEW_PUBLIC_VIEW);
				dialog.setCreateView(false);
				if(dialog.open() == IDialogConstants.OK_ID){
					String publicViewId = dialog.getId().trim();
					String publicViewName = dialog.getName().trim();
					LfwView widget = new LfwView();
					widget.setId(publicViewId);
					widget.setCaption(publicViewName);
					widget.setRefId(publicViewId);
					widget.setProvider(dialog.getProviderClazz());
//					widget.setSourcePackage(dialog.getSourcePackage());
//					widget.setExtendAttribute(LfwView.POOLWIDGET, LfwView.POOLWIDGET);
					try{
						UIMeta meta = new UIMeta();
						String folderPath = LFWPersTool.getCurrentFolderPath();
						String filePath = folderPath + File.separator + widget.getId();
						String fp = filePath.replaceAll("\\\\", "/");
						String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
						meta.setId(id);
						meta.setFlowmode(true);
						
						//保存PublicView节点到文件
						LFWSaveElementTool.createView(widget, meta);
						String widgetPath = LFWPersTool.getCurrentFolderPath();
						File widgetfile = new File(widgetPath + "/"+ widget.getId());
						TreeItem parentTreeItem = LFWAMCPersTool.getTree().getSelection()[0];
						LFWWidgetTreeItem widgetTreeItem = new LFWWidgetTreeItem(parentTreeItem, widgetfile, widget, "[" + WEBPersConstants.PUBLIC_VIEW_SUB + "] " + widgetfile.getName());
						view.detalWidgetTreeItem(widgetTreeItem, widgetfile, widget);
					}
					catch(Exception e){
						MainPlugin.getDefault().logError(WEBPersConstants.NEW_PUBLIC_VIEW + "节点失败:" + e.getMessage(), e);
						MessageDialog.openError(null, WEBPersConstants.NEW_PUBLIC_VIEW, WEBPersConstants.NEW_PUBLIC_VIEW + "节点失败:" + e.getMessage());
					}
				}
			}
		}
	}
	
}
