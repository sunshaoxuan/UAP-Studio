package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.build.action.BuildDomainDiskDialog;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.domain.CreateDomainDialog;
import nc.uap.lfw.editor.common.editor.LFWDomainTreeItem;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBuildTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

public class CreateDomainFileAction extends NodeAction {
	
	LFWDomainTreeItem item = null;
	public CreateDomainFileAction(LFWBasicTreeItem item){
		super(M_editor.CreateDomainFileAction_0);
		this.item = (LFWDomainTreeItem)item;
	}
	public void run(){
		createFile();
	}
	/**
	 * 新建xml文件并加入树节点
	 */
	private void createFile() {
		try{
//			Shell shell = Display.getCurrent().getActiveShell();
//			BuildDomainDiskDialog dialog = new BuildDomainDiskDialog(shell, "领域安装盘配置");
//			if(dialog.open()==IDialogConstants.OK_ID){
//				File buildFile = new File("C:/setup.ini");
//				if (!buildFile.exists()){
//					buildFile.createNewFile();
//					String path = "installdisk/setup.ini";
//					InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
//					String content = FileUtilities.fetchFileContent(ins, "GBK");
//		
//					content = content.replace("${modulecode}", "11");
//					content = content.replace("${modulename}", "领域名称");
//					FileUtilities.saveFile(buildFile, content, "GBK");
//				}
//			}
			CreateDomainDialog dialog = new CreateDomainDialog(M_editor.CreateDomainFileAction_0);
			if(dialog.open() == IDialogConstants.OK_ID){
				String code = dialog.getCodeText();
				String name = dialog.getNameText();
				String id = dialog.getIdText();
				
				IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(id); //$NON-NLS-1$
				File domainfolder = workpath.toFile();
				if(!domainfolder.exists())
					domainfolder.mkdirs();
				File buildFile = new File(domainfolder, "setup.ini"); //$NON-NLS-1$
				if (!buildFile.exists()){
					buildFile.createNewFile();
					String path = "installdisk/setup.ini"; //$NON-NLS-1$
					InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
					String content = FileUtilities.fetchFileContent(ins, "GBK"); //$NON-NLS-1$
		
					content = content.replace("${modulecode}", code); //$NON-NLS-1$
					content = content.replace("${modulename}", name); //$NON-NLS-1$
					FileUtilities.saveFile(buildFile, content, "GBK"); //$NON-NLS-1$
					LFWBuildTreeItem buildItem = new LFWBuildTreeItem(item,
							buildFile, id);
					buildItem.setType(LFWDirtoryTreeItem.DOMAIN_BUILD_CONFIG);
				}
			}			
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
	}

}
