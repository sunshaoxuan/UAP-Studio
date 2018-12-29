package nc.uap.lfw.editor.extNode;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.NewAppNodeManualDialog;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWFuncTreeItem;


public class CreateTemplateAction extends NodeAction{

	private String type;
	
	public CreateTemplateAction(String type){
		super(M_editor.CreateTemplateAction_0);
		this.type = type;
	}
//	public void run(){
//		if (type.equals(LFWDirtoryTreeItem.QUERY_FOLDER)) {
//			IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			try {
//				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(type);
//				wbp.openEditor(editorInput, "Templates"); //$NON-NLS-1$
//			} catch (PartInitException e) {
//				MainPlugin.getDefault().logError(e.getMessage(),e);
//			}
//		}
//		else if (type.equals(LFWDirtoryTreeItem.PRINT_FOLDER)) {
//			IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			try {
//				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(type);
//				wbp.openEditor(editorInput, "Templates"); //$NON-NLS-1$
//			} catch (PartInitException e) {
//				MainPlugin.getDefault().logError(e.getMessage(),e);
//			}
//		}
//	}
	public void run(){
		String title= type.equals("query")?M_editor.CreateTemplateAction_1:M_editor.CreateTemplateAction_2; //$NON-NLS-1$
		LFWFuncTreeItem item = (LFWFuncTreeItem)LFWPersTool.getCurrentTreeItem();
		NewTemplateDialog manualDialog = new NewTemplateDialog(null,title);
		manualDialog.setType(type);
		manualDialog.setNodecode(item.getNodecode());
		manualDialog.open();
	}
}
