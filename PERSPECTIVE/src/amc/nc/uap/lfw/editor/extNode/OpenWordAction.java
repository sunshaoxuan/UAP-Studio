package nc.uap.lfw.editor.extNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWAppsNodeTreeItem;

public class OpenWordAction extends NodeAction{
	private String pk;
	public OpenWordAction(String pk){
		super(M_editor.OpenWordAction_0);
		this.pk = pk;
	}
	public void run(){
		 CpPrintTemplateVO[] printTemp =LFWWfmConnector.getPrintTplByCondition("pk_print_template = '"+pk+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		 if(printTemp!= null&&printTemp.length>0){
			 String filePk = printTemp[0].getPk_file();
			 String nodeCode = printTemp[0].getNodecode();
			 LFWAppsNodeTreeItem item = (LFWAppsNodeTreeItem)LFWPersTool.getCurrentTreeItem();
			 item.setFilePk(filePk);
//			 if(filePk==null){
//				 if(MessageDialog.openConfirm(null, "提示", "模板设计文件不存在，是否创建?")){
//					 LfwFileVO vo = new LfwFileVO();
//					 vo.setFilemgr("nc.uap.portal.comm.file.PortalFileManager");
//					 vo.setFilename("printTemp.doc");
//					 vo.setFiletypo("doc");
//					 vo.setDisplayname("printTemp.doc");
//					 filePk = LFWWfmConnector.insertLfwfile(vo);
//					 LFWAppsNodeTreeItem item = (LFWAppsNodeTreeItem)LFWPersTool.getCurrentTreeItem();
//					 item.setFilePk(filePk);
//					 LFWWfmConnector.executeSql("update cp_print_template set pk_file='"+filePk+"' where nodecode='"+nodeCode+"'");
//				 }
//				 else return;
//			 }
			 
			 IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput("printdesign"); //$NON-NLS-1$
					wbp.openEditor(editorInput, "Templates"); //$NON-NLS-1$
				} catch (PartInitException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
		 }
		 else{
			 
		 }
	}

}
