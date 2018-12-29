package nc.uap.lfw.window;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.window.template.IViewCreator;
import nc.uap.lfw.window.template.ViewCreatorFactory;
import nc.uap.lfw.window.template.ViewPair;

public class WindowCreateHelper {
	public static void doWindowCreate(CreateWindowDialog dialog, LfwWindow winConf) {
		
		String fullWinId =null;
		if(winConf.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT)||winConf.getComponentId().equals("")) //$NON-NLS-1$
			fullWinId = winConf.getId();
		else
			fullWinId = winConf.getComponentId()+"."+winConf.getId(); //$NON-NLS-1$
		
		if(LFWAMCConnector.getWindowById(fullWinId)!=null){
			MessageDialog.openInformation(null, WEBPersConstants.NEW_WINDOW, "已存在ID为：" + winConf.getId() + " 的Window节点");
			return;
		}
		
		String filePath = createFilePath(winConf);
		//保存Window节点到文件
		UIMeta winUm = createWinUimeta(dialog.isFlowlayout(), dialog.getPreferredWidth(), dialog.getPreferredHeight(), filePath);
		ViewPair pair = createViewByTemplateType(winConf, winUm, dialog.getTemplateType(), dialog.getExtInfo());
		
		if(pair != null){
			String viewFilePath = filePath + "/" + pair.view.getId(); //$NON-NLS-1$
			LFWSaveElementTool.createView(pair.view, pair.viewUm, viewFilePath);
		}
		LFWSaveElementTool.createPagemeta(winConf, winUm, filePath);
//		winConf.addView(pair.view);
		Map<String, String> extInfo = dialog.getExtInfo();
		if(extInfo == null){
			extInfo = new HashMap<String, String>(2);
			dialog.setExtInfo(extInfo);
		}
		extInfo.put("FULLID", winConf.getFullId()); //$NON-NLS-1$
		String projPath = LFWAMCPersTool.getLFWProjectPath();
		if(pair != null){
			updateCode(projPath, pair.view, dialog.getTemplateType(), dialog.getExtInfo());		
			boolean created = false;
			
			int timecount = 0;
			while(!created){
				try {			
					if(timecount == 10){
						MessageDialog.openError(null, M_window.WindowCreateHelper_0, M_window.WindowCreateHelper_1);
						MainPlugin.getDefault().logError(M_window.WindowCreateHelper_2);
						break;
					}
						
					Thread.sleep(1000);
					if(LFWAMCConnector.getWindowById(fullWinId)!=null){
						created = true;
					}
					timecount++;
				} catch (InterruptedException e) {
					MainPlugin.getDefault().logError(e);
				}
			}
		}
		else{
			LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
			try{
				TreeItem selTI = LFWPersTool.getCurrentTreeItem();
				File viewfile = new File(filePath);
				LFWPageMetaTreeItem treeItem = new LFWPageMetaTreeItem(selTI, viewfile, winConf.getCaption() + "[" + winConf.getId() + "]");
//				LFWPageMetaTreeItem treeItem = (LFWPageMetaTreeItem)view.addAMCTreeNode(winConf.getId(), winConf.getCaption(), ILFWTreeNode.WINDOW);
				treeItem.setPm(winConf);
				treeItem.setType(ILFWTreeNode.WINDOW);
			}
			catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
//		if(LFWAMCConnector.getWindowById(winConf.getId());
	}
	
	private static String createFilePath(LfwWindow winConf) {
		String folderPath = LFWAMCPersTool.getCurrentFolderPath();
		if(LFWAMCPersTool.getCurrentTreeItem() instanceof LFWApplicationTreeItem){
			int index = folderPath.indexOf("/html"); //$NON-NLS-1$
			folderPath = folderPath.substring(0, index+5)+"/nodes/"; //$NON-NLS-1$
		}
		String filePath = folderPath + "/" + winConf.getId().replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return filePath;
	}

	private static UIMeta createWinUimeta(boolean flowmode, String preferredWidth, String preferredHeight, String filePath) {
		UIMeta meta = new UIMeta();
		String fp = filePath.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um"; //$NON-NLS-1$ //$NON-NLS-2$
		meta.setAttribute(UIMeta.ID, id);
		meta.setFlowmode(flowmode);
		meta.setPreferredWidth(preferredWidth);
		meta.setPreferredHeight(preferredHeight);
		return meta;
	}

	private static ViewPair createViewByTemplateType(LfwWindow window, UIMeta winUm, String windowType, Map<String, String> extInfo) {
		IViewCreator creator = ViewCreatorFactory.getViewCreator(windowType, extInfo);
		return creator.createViewPair(window, winUm);
	}
	
	private static void updateCode(String projPath, LfwView view, String windowType, Map<String, String> extInfo) {
		IViewCreator creator = ViewCreatorFactory.getViewCreator(windowType, extInfo);
		creator.replaceCode(projPath, view);
	}
}
