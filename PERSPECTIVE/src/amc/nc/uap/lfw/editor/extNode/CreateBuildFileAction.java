package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBuildTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * 建立构造安装盘配置文件
 * @author qinjianc
 *
 */

public class CreateBuildFileAction extends NodeAction {

	LFWDirtoryTreeItem item = null;

	public CreateBuildFileAction(LFWBasicTreeItem item) {
		super(WEBPersConstants.NEWBUILDFILE);
		this.item = (LFWDirtoryTreeItem) item;
	}

	public void run() {
		createFile();
	}

	/**
	 * 新建xml文件并加入树节点
	 */
	private void createFile() {

		File folder = item.getFile();
		IProject project = LFWPersTool.getCurrentProject();
		try {
			if (!folder.exists())
				folder.mkdirs();
			if (folder.isDirectory()) {				
//				FileOutputStream install = new FileOutputStream(project.getLocation().toString()+"/setup.ini");
//				
//				int data = 0;
//				byte[] b = new byte[1638400];
//				while (true) {
//					data = ins.read(b, 0, 1638400);
//					if (data == -1) {
//						break;
//					}
//					install.write(b, 0, data);
//				}
//				install.flush();
//				install.close();
//				FileUtilities
				File buildFile = new File(folder, "setup.ini");
				if (!buildFile.exists()){
					buildFile.createNewFile();
					String path = "installdisk/setup.ini";
					InputStream ins = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(path);
					String content = FileUtilities.fetchFileContent(ins, "GBK");
					String modulecode = LFWPersTool.getProjectModuleName(project);
					String moduleTitle = "";
					String moduleId = "";
					CpModuleVO[] modules = LFWWfmConnector.getModules();
					if(modules!=null){
						for(CpModuleVO module:modules){
							if(modulecode.equals(module.getDevmodulecode())){
								moduleId = module.getId();
								moduleTitle = module.getTitle();
							}
						}
					}
					content = content.replace("${modulecode}", moduleId);
					content = content.replace("${modulename}", moduleTitle);
					FileUtilities.saveFile(buildFile, content, "GBK");	
				}
												
				ResourcesPlugin.getWorkspace().getRoot()
						.getProject(project.getName())
						.refreshLocal(IResource.DEPTH_INFINITE, null);
				// file.refreshLocal(IResource.DEPTH_INFINITE, null);
				if (item.getItemCount() == 0) {
					LFWBuildTreeItem buildItem = new LFWBuildTreeItem(item,
							buildFile, buildFile.getName());
					buildItem.setType(LFWDirtoryTreeItem.BUILD_CONFIG);
				}
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
	}	
}
