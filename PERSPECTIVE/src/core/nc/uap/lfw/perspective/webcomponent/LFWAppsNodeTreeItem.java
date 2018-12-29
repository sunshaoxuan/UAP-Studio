package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.editor.extNode.OpenWordAction;
import nc.uap.lfw.editor.extNode.SimpleBrowserEditorInput;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class LFWAppsNodeTreeItem extends LFWDirtoryTreeItem{

	private String pk = null;
	private String type = null;
	private String nodecode = null;
	private String metaclass = null;
	private String filePk = null;
	public LFWAppsNodeTreeItem(TreeItem parentItem, File file,
			String text , String pk) {
		super(parentItem, file, text);
		this.pk = pk;
	}
	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "widget.gif");
		return ImageProvider.widget;
	}
	public void mouseDoubleClick(){
		if (getType().equals(LFWDirtoryTreeItem.QUERY_FOLDER)) {
			IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(type);
				wbp.openEditor(editorInput, "Templates");
			} catch (PartInitException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		else if (getType().equals(LFWDirtoryTreeItem.PRINT_FOLDER)) {
			IWorkbenchPage wbp= WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(type);
				wbp.openEditor(editorInput, "Templates");
			} catch (PartInitException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}
	public void addMenuListener(IMenuManager manager) {
		if(getType().equals(LFWDirtoryTreeItem.PRINT_FOLDER)) {
			OpenWordAction action = new OpenWordAction(pk);
			manager.add(action);
		}
	}
	
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNodecode() {
		return nodecode;
	}
	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}
	public String getMetaclass() {
		return metaclass;
	}
	public void setMetaclass(String metaclass) {
		this.metaclass = metaclass;
	}
	public String getFilePk() {
		return filePk;
	}
	public void setFilePk(String filePk) {
		this.filePk = filePk;
	}
	

}
