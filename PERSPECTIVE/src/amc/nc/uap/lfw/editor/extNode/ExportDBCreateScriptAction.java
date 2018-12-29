package nc.uap.lfw.editor.extNode;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.Workbench;

import com.yonyou.common.database.powerdesigner.impl.DbCreateServiceImpl;
import com.yonyou.common.database.powerdesigner.itf.IDbCreateService;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;

public class ExportDBCreateScriptAction extends NodeAction{

	private static final IDbCreateService dbCreateService = new DbCreateServiceImpl();
	
	public ExportDBCreateScriptAction(){
		super("生成建库脚本");		
	}
	public void run() {
		LFWMetadataTreeItem fileItem = (LFWMetadataTreeItem)LFWPersTool.getCurrentTreeItem();
		String bcpId = LFWPersTool.getBcpId(fileItem);
		IFolder folder = LFWPersTool.getCurrentProject().getFolder(bcpId);
		IFile file = fileItem.getMdFile();
		Shell shell = Workbench.getInstance().getActiveWorkbenchWindow().getShell();
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setFilterPath(file.getProject()
				.getLocation().toOSString());
		String open = dialog.open();
		if(open==null){
			return;
		}
		File pdmFile = file.getLocation().toFile();
		try{
			dbCreateService
					.geneSqlFile(pdmFile, false, new Path(open).toFile());
			String uaphome = LfwCommonTool.getUapHome();
			String fromPath = uaphome + "/ncscript/uap/pubapp/dbcreate/Dtype";
			try{
				FileUtilities.copyFileFromDir(open+"/Dtype", fromPath);
			}
			catch(Exception e2){
				MainPlugin.getDefault().logError(e2.getMessage(),e2);
			}
			if (file.getProject().getLocation().isPrefixOf(new Path(open))) {
				try {
					file.getProject().refreshLocal(IResource.DEPTH_INFINITE,
							null);
				} catch (CoreException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
			}
			if(MessageDialog.openConfirm(shell, "", "生成SQL建库脚本成功，确定要打开生成目录吗?")){
				LfwCommonTool.openFolder(new Path(open).toFile());
			}
		}
		catch(Exception e1){
			MainPlugin.getDefault().logError(e1.getMessage(),e1);
		}
		
	}
}
