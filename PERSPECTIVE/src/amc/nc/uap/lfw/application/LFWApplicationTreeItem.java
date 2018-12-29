/**
 * 
 */
package nc.uap.lfw.application;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.base.LFWAMCTreeItem;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * Application TreeItem¿‡
 * 
 * @author chouhl
 * 
 */
public class LFWApplicationTreeItem extends LFWAMCTreeItem {

	private Application application = null;

	public LFWApplicationTreeItem(TreeItem parentItem, Object object,
			String text) {
		super(parentItem, object, text);
	}

	public LFWApplicationTreeItem(TreeItem parentItem, File file) {
		super(parentItem, file);
	}

	public LFWApplicationTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
	}

	public void mouseDoubleClick() {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool
				.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
			EditApplicationNodeAction.openApplicationEditor();
			if (getItemCount() == 0) {
				LFWExplorerTreeView.getLFWExploerTreeView(null)
						.refreshAppItem();
			}
		}
		else {
			removeAll();
			IProject project = LFWPersTool.getCurrentProject();
			String path = LFWPersTool.getBcpPath(this);			
			String id = this.getId();
			String bcp = null;
			String nameStr = null;
			if (path == null) {
				path = LFWPersTool.getProjectPath().replace("/", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
				nameStr = project.getName().toLowerCase() + "_nodes\\a_" + id + "-res.properties"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			else{
				bcp = path.substring(path.lastIndexOf("\\") + 1); //$NON-NLS-1$
				nameStr = bcp.toLowerCase() + "_nodes\\a_" + id + "-res.properties"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			

			StringBuilder simpStr = new StringBuilder(path);
			simpStr.append("\\resources\\lang\\simpchn\\").append(nameStr); //$NON-NLS-1$
//			StringBuilder tradStr = new StringBuilder(path);
//			tradStr.append("\\resources\\lang\\tradchn\\").append(nameStr); //$NON-NLS-1$
//			StringBuilder engStr = new StringBuilder(path);
//			engStr.append("\\resources\\lang\\english\\").append(nameStr); //$NON-NLS-1$

			LFWDirtoryTreeItem simpchnNode = new LFWDirtoryTreeItem(this,
					simpStr, M_application.LFWApplicationTreeItem_0);
			simpchnNode.setType("langType"); //$NON-NLS-1$
//			LFWDirtoryTreeItem tradchnNode = new LFWDirtoryTreeItem(this,
//					tradStr, M_application.LFWApplicationTreeItem_1);
//			tradchnNode.setType("langType"); //$NON-NLS-1$
//			LFWDirtoryTreeItem englishNode = new LFWDirtoryTreeItem(this,
//					engStr, M_application.LFWApplicationTreeItem_2);
//			englishNode.setType("langType"); //$NON-NLS-1$

			String simpPath = simpchnNode.getObject().toString();
			IFile resFile = null;
			if(bcp!=null){
				resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\resources\\")));		 //$NON-NLS-1$
			if (resFile.isAccessible()) {
				LFWMetadataTreeItem simpresNode = new LFWMetadataTreeItem(
						simpchnNode, simpchnNode.getObject(), id
								+ "-res.properties"); //$NON-NLS-1$
				simpresNode.setMdFile(resFile);
			}
//			String tradPath = tradchnNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//			else resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\resources\\")));	 //$NON-NLS-1$
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem tradresNode = new LFWMetadataTreeItem(
//						tradchnNode, tradchnNode.getObject(), id
//								+ "-res.properties"); //$NON-NLS-1$
//				tradresNode.setMdFile(resFile);
//			}
//			String englishPath = englishNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//			else resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\resources\\")));	 //$NON-NLS-1$
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem englishresNode = new LFWMetadataTreeItem(
//						englishNode, englishNode.getObject(), id
//						+ "-res.properties"); //$NON-NLS-1$
//				englishresNode.setMdFile(resFile);
//			}
			this.setExpanded(true);
		}
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
