/**
 * 
 */
package nc.uap.lfw.editor.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWNodeTreeItem;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.window.ChooseWindowTemplateDialog;
import nc.uap.lfw.window.CreateWindowDialog;
import nc.uap.lfw.window.WindowCreateHelper;
import nc.uap.lfw.window.template.ICreatorWithChildren;
import nc.uap.lfw.window.template.ICreatorWithOrgs;

import org.eclipse.core.resources.IProject;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 * Application业务组件工具箱——创建Window命令处理类
 * @author chouhl
 *
 */
public class ApplicationCreateCommand extends Command {

	private ApplicationObj obj;
	
	private ApplicationGraph graph;
	
	private Rectangle rect;
	
	private String sourcePackage;

	public ApplicationCreateCommand(ApplicationObj obj, ApplicationGraph graph, Rectangle rect) {
		super();
		this.obj = obj;
		this.graph = graph;
		this.rect = rect;
		setLabel(WEBPersConstants.NEW_WINDOW);
	}

	public boolean canExecute() {
		return obj != null && graph != null && rect != null;
	}

	public void execute() {
		ApplicationObj applicationObj = (ApplicationObj) obj;
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		
		ChooseWindowTemplateDialog typeDialog = new ChooseWindowTemplateDialog();
		if(typeDialog.open() != IDialogConstants.OK_ID)
			return;
		String windowType = typeDialog.getViewType();
		
		CreateWindowDialog dialog = new CreateWindowDialog(windowType);
		String compid = null;
		if (dialog.open() == IDialogConstants.OK_ID) {
			Map<String, String> extInfo = new HashMap<String, String>();
			if(typeDialog.getChildCount() > 0)
				extInfo.put(ICreatorWithChildren.CHILD_COUNT_KEY, typeDialog.getChildCount() + "");
			if(typeDialog.isWithOrg()){
				extInfo.put(ICreatorWithOrgs.ORGS_KEY, "true");
			}
			dialog.setExtInfo(extInfo);
			applicationObj.setWindowId(dialog.getId());
			applicationObj.setWindowName(dialog.getName());
			compid = dialog.getCompid();
			applicationObj.setControllerClazz(dialog.getControllerClazz());
			sourcePackage = dialog.getSourcePackage();
//			boolean isNotExist = true;
//			List<LfwWindow> windows = LFWAMCConnector.getCacheWindows();
//			for(LfwWindow w:windows){
//				if(obj.getWindowId()!=null&&obj.getWindowId().equals(w.getId())){
//					isNotExist = false;
//					break;
//				}
//			}
			String fullWinId = null;
			if(compid.equals(LfwUIComponent.ANNOYUICOMPONENT)||compid.equals("")) //$NON-NLS-1$
				fullWinId = dialog.getId();
			else
				fullWinId = compid +"."+dialog.getId(); 
			LfwWindow win = LFWAMCConnector.getWindowById(fullWinId);
//			List<TreeItem> treeItems = LFWAMCPersTool.getAllWindowTreeItems(graph.getProject(), graph.getCurrentTreeItem());
//			for(TreeItem treeItem : treeItems){
//				if(treeItem instanceof LFWBasicTreeItem){
//					if(obj.getWindowId() != null && obj.getWindowId().equals(((LFWBasicTreeItem)treeItem).getId())){
//						isNotExist = false;
//						break;
//					}
//				}
//			}
			if(win==null){
				LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
				if (view == null){
					return;
				}
				LfwWindow window = LFWSaveElementTool.createNewWindowConf(obj.getWindowId(), obj.getWindowName(), compid, obj.getControllerClazz(), sourcePackage);
				try {
					WindowCreateHelper.doWindowCreate(dialog, window);
					//刷新树
//					LFWPageMetaTreeItem treeItem = (LFWPageMetaTreeItem) view.addWindowTreeNode(obj.getWindowId(), obj.getWindowName(), graph.getProject(), graph.getCurrentTreeItem());
//					treeItem.setType(LFWNodeTreeItem.WINDOW);
					
					ApplicationEditor editor = ApplicationEditor.getActiveEditor();
					WindowConfigObj windowObj = new WindowConfigObj();
					windowObj.setWindowConfig(window.createWindowConfig());
					editor.repaintWindowObj(windowObj);
					redo(windowObj);
					editor.setDirtyTrue();
				} catch (Exception e) {
					MainPlugin.getDefault().logError("Application业务组件工具箱——创建Window失败", e);
					MessageDialog.openError(null, WEBPersConstants.NEW_WINDOW, "创建Window失败");
				}
			}else{
				MessageDialog.openInformation(null, WEBPersConstants.NEW_WINDOW, "已存在ID为：" + obj.getWindowId() + " 的Window节点");
			}
		}
	}
	
	public void redo(WindowConfigObj obj) {
		graph.addWindowCell(obj);
		graph.getApplication().addWindow(obj.getWindowConfig());
	}

}
