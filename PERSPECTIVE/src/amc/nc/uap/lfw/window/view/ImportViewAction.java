package nc.uap.lfw.window.view;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.window.RefreshWindowNodeAction;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ImportViewAction extends NodeAction{
	
	public ImportViewAction(){
		super(M_window.ImportViewAction_0);
	}
	public void run(){
		try {
		Shell shell = new Shell(Display.getCurrent());
		DirectoryDialog folderDialog = new DirectoryDialog(shell);
		folderDialog.setMessage(M_window.ImportViewAction_1);
		String selectDir = folderDialog.open();
		if(selectDir==null){
			return;
		}
		File srcFolder = new File(selectDir);
		String name = srcFolder.getName();
		LFWPageMetaTreeItem pmItem = (LFWPageMetaTreeItem)LFWPersTool.getCurrentTreeItem();
		File destFolder = new File(pmItem.getFile(),name);
		if(!destFolder.exists()) destFolder.mkdir();
		String bcpPath = LFWAMCPersTool.getBCPProjectPath();
		LfwWindow pm = pmItem.getPm();
		
		for(File subFile:srcFolder.listFiles()){
			if(subFile.isFile()){
				if(subFile.getName().endsWith("um")||subFile.getName().endsWith("wd")) //$NON-NLS-1$ //$NON-NLS-2$
					if(subFile.getName().endsWith("wd")){ //$NON-NLS-1$
						LfwView widget = LFWAMCConnector.getView(srcFolder.getPath(), subFile.getName());
						ViewConfig wconf = new ViewConfig();
						wconf.setId(widget.getId());
						wconf.setRefId(widget.getRefId());
						wconf.setCanFreeDesign(widget.isCanFreeDesign());
						pm.addViewConfig(wconf);
					}
					FileUtilities.copyFileToDir(destFolder.getPath(), subFile, subFile.getName());				
			}
			else{
				if(subFile.getName().equals("src")){ //$NON-NLS-1$
					FileUtilities.copyFileFromDir(bcpPath+"/src", subFile.getPath()); //$NON-NLS-1$
				}				
			}
		}
		LFWSaveElementTool.savePagemeta(pm);
		IProject project = LFWPersTool.getCurrentProject();
		project.refreshLocal(2, null);
		RefreshWindowNodeAction refresh = new RefreshWindowNodeAction();
		refresh.run();
		MessageDialog.openInformation(shell, M_window.ImportViewAction_2, M_window.ImportViewAction_3);
		
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
			MessageDialog.openError(null, M_window.ImportViewAction_4, M_window.ImportViewAction_5);
		}
		
	}
	
}
