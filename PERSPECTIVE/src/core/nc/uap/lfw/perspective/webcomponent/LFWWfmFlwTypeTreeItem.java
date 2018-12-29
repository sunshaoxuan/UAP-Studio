package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.extNode.DelFlwTypeAction;
import nc.uap.lfw.editor.extNode.EditFlwTypeAction;
import nc.uap.lfw.editor.extNode.ExportXMLAction;
import nc.uap.lfw.editor.extNode.SimpleBrowserEditorInput;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * 
 * @author qinjianc
 *
 */
public class LFWWfmFlwTypeTreeItem extends LFWDirtoryTreeItem {

	String def_pk = null;
	
	String type_pk = null;
	
	String cate_Pk = null;

	public LFWWfmFlwTypeTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
		// TODO Auto-generated constructor stub
	}

	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(
//				WEBProjPlugin.ICONS_PATH, "file.png");
		return ImageProvider.file;
	}
	
	public void deleteNode() {
		LFWAMCPersTool.refreshCurrentPorject();
		dispose();
	}

	public void addMenuListener(IMenuManager manager) {
		EditFlwTypeAction editTypeAction = new EditFlwTypeAction();
		manager.add(editTypeAction);
		DelFlwTypeAction delTypeAction = new DelFlwTypeAction();
		manager.add(delTypeAction);
		ExportXMLAction exportAction = new ExportXMLAction();
		manager.add(exportAction);
	}

	public void mouseDoubleClick() {

		String proDefPk = this.def_pk;
		String url = "/portal/app/wfm_flowsetting/wfm_flowdesigner?";

		if (proDefPk != "") {
			url = url + "proDefPk=" + proDefPk;
		}
	
		try {
			IWorkbenchPage wbp = WEBPersPlugin.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(
					"wfmDesign", url);
			wbp.openEditor(editorInput,
					"WfmDesigner");
		} catch (PartInitException e) {
			WEBPersPlugin.getDefault().logError(e);
		}

	}

	public String getDef_pk() {
		return def_pk;
	}

	public void setDef_pk(String def_pk) {
		this.def_pk = def_pk;
	}

	public String getType_pk() {
		return type_pk;
	}

	public void setType_pk(String type_pk) {
		this.type_pk = type_pk;
	}

	public String getCate_Pk() {
		return cate_Pk;
	}

	public void setCate_Pk(String cate_Pk) {
		this.cate_Pk = cate_Pk;
	}
	
	

}
