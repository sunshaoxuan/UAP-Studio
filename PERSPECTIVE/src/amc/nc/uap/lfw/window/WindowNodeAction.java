/**
 * 
 */
package nc.uap.lfw.window;

import java.util.HashMap;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.base.RefreshAMCNodeGroupAction;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.window.template.ICreatorWithChildren;
import nc.uap.lfw.window.template.ICreatorWithOrgs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * 新建Window节点类
 * @author chouhl
 *
 */
public class WindowNodeAction extends NodeAction {

	public WindowNodeAction() {
		super(WEBPersConstants.NEW_WINDOW);
	}

	public void run() {
		createWindow();
	}
	
	private void createWindow(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if (view == null){
			return;
		}
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		
		ChooseWindowTemplateDialog typeDialog = new ChooseWindowTemplateDialog();
		if(typeDialog.open() != IDialogConstants.OK_ID)
			return;
		String windowType = typeDialog.getViewType();
		
		CreateWindowDialog windowDialog = new CreateWindowDialog(windowType);
		if (windowDialog.open() == IDialogConstants.OK_ID) {
			Map<String, String> extInfo = new HashMap<String, String>(2);
			extInfo.put(ICreatorWithOrgs.ORGS_KEY, String.valueOf(typeDialog.isWithOrg()));
			extInfo.put(ICreatorWithChildren.CHILD_COUNT_KEY, String.valueOf(typeDialog.getChildCount()));
			if(windowDialog.getExtInfo() == null){
				windowDialog.setExtInfo(extInfo);
			}else{
				windowDialog.getExtInfo().putAll(extInfo);
			}			
			String pmId = windowDialog.getId();
			String pmName = windowDialog.getName();
			String compid = windowDialog.getCompid();
			LfwWindow winConf = LFWSaveElementTool.createNewWindowConf(pmId, pmName, compid, windowDialog.getControllerClazz(), windowDialog.getSourcePackage());
			try {
				WindowCreateHelper.doWindowCreate(windowDialog, winConf);
				//刷新树
//				LFWPageMetaTreeItem treeItem = (LFWPageMetaTreeItem)view.addAMCTreeNode(pmId, pmName, ILFWTreeNode.WINDOW);
//				treeItem.setPm(winConf);
//				treeItem.setType(ILFWTreeNode.WINDOW);
				if(windowType!=IWindowTemplateType.TYPE_EMPTY){
					RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
							WEBPersConstants.AMC_WINDOW_PATH, ILFWTreeNode.WINDOW,
							WEBPersConstants.AMC_WINDOW_FILENAME,getBcp());
					refreshAMCNodeGroupAction.run();
				}
				
				//刷新内存
//				NCConnector.refreshNode();
			} catch (Exception e) {
				MainPlugin.getDefault().logError(WEBPersConstants.NEW_WINDOW + "节点失败:" + e.getMessage(), e);
				MessageDialog.openError(null, WEBPersConstants.NEW_WINDOW, WEBPersConstants.NEW_WINDOW + "节点失败:" + e.getMessage());
			}
		}
	}
	private BusinessComponent getBcp(){
		BusinessComponent bcp = new BusinessComponent();
		TreeItem treeitem = LFWPersTool.getCurrentTreeItem();
		if(treeitem instanceof LFWDirtoryTreeItem){
			LFWDirtoryTreeItem item = (LFWDirtoryTreeItem)treeitem;
			if(item.getBcp()!=null){
				bcp = item.getBcp();
				return bcp;
			}				
		}
		String bcpid = LFWPersTool.getBcpId(treeitem);
		bcp.setName(bcpid);
		bcp.setDispname(bcpid);
		return bcp;
	}
	
}
