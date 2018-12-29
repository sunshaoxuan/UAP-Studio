package nc.uap.lfw.build.action;

import java.io.File;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_build;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.webcomponent.LFWBuildTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

public class DeleteDomainAction extends NodeAction{
	private LFWBuildTreeItem item;
	public DeleteDomainAction(LFWBuildTreeItem item){
		super(M_build.DeleteDomainAction_0);
		this.item = item;
	}
	public void run(){
		if(MessageDialog.openConfirm(null, M_build.DeleteDomainAction_1, M_build.DeleteDomainAction_2)){
			IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain"); //$NON-NLS-1$
			File deleteFolder = workpath.append(item.getText()).toFile();
			int index = item.getParentItem().indexOf(item);
			FileUtilities.deleteFile(deleteFolder);
			LFWExplorerTreeBuilder builder = LFWExplorerTreeBuilder.getInstance();
			TreeItem domainItem = item.getParentItem();
			domainItem.removeAll();			
			File domainFolder = workpath.toFile();
			if(domainFolder.exists()&&domainFolder.listFiles().length>0){
				for(File domain:domainFolder.listFiles()){
					File buildFile = new File(domain, "setup.ini"); //$NON-NLS-1$
					if(buildFile.exists()){
						LFWBuildTreeItem buildItem = new LFWBuildTreeItem(domainItem,buildFile, domain.getName());
						buildItem.setType(LFWDirtoryTreeItem.DOMAIN_BUILD_CONFIG);
					}
				}
			}
		}
	}

}
