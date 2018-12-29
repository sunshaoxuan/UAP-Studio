package nc.uap.lfw.perspective.webcomponent;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.editor.extNode.ExportDBCreateScriptAction;
import nc.uap.lfw.editor.extNode.ExportInitScriptAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class LFWMetadataTreeItem extends LFWDirtoryTreeItem {

	private IFile mdFile = null;

	public LFWMetadataTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, object, text);
		// TODO Auto-generated constructor stub
	}

	protected Image getDirImage() {
		return ImageProvider.page;
	}

	@SuppressWarnings("restriction")
	public void mouseDoubleClick() {
		// EditNodeAction editNodeAction = new EditNodeAction();
		// editNodeAction.run();
		// if(getItemCount() == 0){
		// RefreshWindowNodeAction refreshNodeAction = new
		// RefreshWindowNodeAction();
		// refreshNodeAction.run();
		// }
		try {
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(mdFile.getName().endsWith(".xml")||mdFile.getName().endsWith(".pdm")){
				IDE.openEditor(workbenchPage, mdFile,"tk.eclipse.plugin.xmleditor.editors.XMLEditor");
			}
			else if(mdFile.getName().endsWith(".map")||mdFile.getName().endsWith(".ini")){
				IDE.openEditor(workbenchPage, mdFile,"org.eclipse.ui.DefaultTextEditor");
			}
			else
				EditorUtility.openInEditor(mdFile);

		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
	}
	
	public void addMenuListener(IMenuManager manager) {
		if(mdFile.getName().endsWith(".pdm")){
			ExportDBCreateScriptAction action = new ExportDBCreateScriptAction();
			manager.add(action);
		}
		if(mdFile.getName().equals("items.xml")){
			ExportInitScriptAction action = new ExportInitScriptAction();
			manager.add(action);
		}
	}


	public IFile getMdFile() {
		return mdFile;
	}

	public void setMdFile(IFile mdFile) {
		this.mdFile = mdFile;
	}

}
