package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.pagemeta.EditNodeAction;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.md.component.DeleteAction;
import nc.uap.lfw.md.component.NewBpfFileAction;
import nc.uap.lfw.md.component.NewMdDirAction;
import nc.uap.lfw.md.component.NewMDPFileAction;
import nc.uap.lfw.window.RefreshWindowNodeAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class LFWMdFileTreeItem extends LFWDirtoryTreeItem {


	private IFile mdFile = null;
	String moduleName = null;
	String mdFileRelativePath = null;
	String componentName = null;
	private String type = null;

	public LFWMdFileTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, object, text);
		if (parentItem instanceof LFWMdDirTreeItem) {
			this.componentName = ((LFWMdDirTreeItem) parentItem).getItemName();
		}
		// TODO Auto-generated constructor stub
	}

	protected Image getDirImage() {
		Image currentImage = ImageProvider.pages;
//		imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH,
//				"pages.gif");
		if (type != null) {
			if (type.equals(WEBPersConstants.AMC_MD_FILENAME)) {
				currentImage = ImageProvider.editorb;
			} else if (type.equals(WEBPersConstants.AMC_BPF_FILENAME)) {
				currentImage = ImageProvider.bpf;
			}
		}
		return currentImage;
	}

	@SuppressWarnings("restriction")
	public void mouseDoubleClick() {

		try {
			EditorUtility.openInEditor(mdFile);

		} catch (PartInitException e) {
			WEBPersPlugin.getDefault().logError(e);
		}
	}
	public void addMenuListener(IMenuManager manager) {
		
		DeleteAction deleteAction = new DeleteAction();
		manager.add(deleteAction);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		setImage(getDirImage());
	}

	public IFile getMdFile() {
		return mdFile;
	}

	public void setMdFile(IFile mdFile) {
		this.mdFile = mdFile;
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		LFWAMCPersTool.refreshCurrentPorject();
		dispose();
	}

}
