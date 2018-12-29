package nc.uap.lfw.editor.extNode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWFuncTreeItem;

public class PublishBtnResAction extends NodeAction{
	
	private String funcPk = null;
	public PublishBtnResAction(LFWFuncTreeItem item){
		super(M_editor.PublishBtnResAction_0);
		this.funcPk = item.getFuncPk();
	}

	public void run(){
		try{
			CpAppsNodeVO funcNode = LFWWfmConnector.getAppsNodeByPk(funcPk);
			if(funcNode!=null){
				if(funcNode.getAppid()!=null){
					String appId = funcNode.getAppid();
					Application app = LFWAMCConnector.getApplicationById(appId);
					if(app == null){
						MessageDialog.openError(Display.getDefault().getActiveShell(), M_editor.PublishBtnResAction_1, M_editor.PublishBtnResAction_2+appId+M_editor.PublishBtnResAction_3);
						return;
					}
					 List<WindowConfig> winConfList = app.getWindowList();
					 List<LfwWindow> winList = new ArrayList<LfwWindow>();
					 for(WindowConfig winconf:winConfList){
						 String id = winconf.getId();
						 if(id!=null){
							 LfwWindow win = LFWAMCConnector.getWindowById(id);
							 if(win!=null)
								 winList.add(win);
						 }					 
					 }
					 LfwWindow[] windows = winList.toArray(new LfwWindow[0]);
					 if(MessageDialog.openConfirm(Display.getDefault().getActiveShell(), M_editor.PublishBtnResAction_4, M_editor.PublishBtnResAction_5)){
						 LFWWfmConnector.updateNodeMenuItems(funcPk, windows);
						 MessageDialog.openInformation(Display.getDefault().getActiveShell(), M_editor.PublishBtnResAction_6, M_editor.PublishBtnResAction_7);
					 }					
				}
			}
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage(),e);
			MessageDialog.openError(Display.getDefault().getActiveShell(), M_editor.PublishBtnResAction_1, M_editor.PublishBtnResAction_9);
		}
	}
}
