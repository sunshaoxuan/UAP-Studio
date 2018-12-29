package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.lfw.application.ManualAppNodeAction;
import nc.uap.lfw.application.NewAppNodeManualDialog;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.editor.extNode.CreateTemplateAction;
import nc.uap.lfw.editor.extNode.PublishBtnResAction;
import nc.uap.lfw.editor.extNode.SimpleBrowserEditorInput;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class LFWFuncTreeItem extends LFWDirtoryTreeItem {
	private BusinessComponent bcp = null;
	
	private String type = null;
	
	private String funcPk = null;
	
	private String nodecode = null;
	
	private CpAppsNodeVO exsitFunc = null;

	public LFWFuncTreeItem(TreeItem parentItem, File file, BusinessComponent bcp, String text,String type) {
		super(parentItem, file, text);
		this.bcp = bcp;
		this.type = type;
//		setImage(getDirImage());
	}
	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}
	public void addMenuListener(IMenuManager manager) {
		if(type.equals("node")){
			PublishBtnResAction btnPublishAction = new PublishBtnResAction(this);
			manager.add(btnPublishAction);
		}		
		else if(type.equals("query")||type.equals("print")){
			CreateTemplateAction newTemplateAction = new CreateTemplateAction(type);
			manager.add(newTemplateAction);
		}
	}	
	public void mouseDoubleClick(){
		if(type.equals("node")){
//			IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			try {
//				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(type);
//				wbp.openEditor(editorInput, "FuncEditor");
//			} catch (PartInitException e) {
//				MainPlugin.getDefault().logError(e.getMessage(),e);
//			}
			NewAppNodeManualDialog manualDialog = new NewAppNodeManualDialog(null,WEBPersConstants.MANUAL_APPLICATION);
			manualDialog.setExsitFunc(exsitFunc);
			manualDialog.open();
		}
		
	}
	public String getFuncPk() {
		return funcPk;
	}
	public void setFuncPk(String funcPk) {
		this.funcPk = funcPk;
	}
	public String getNodecode() {
		return nodecode;
	}
	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}
	public CpAppsNodeVO getExsitFunc() {
		return exsitFunc;
	}
	public void setExsitFunc(CpAppsNodeVO exsitFunc) {
		this.exsitFunc = exsitFunc;
	}
	
	


}
