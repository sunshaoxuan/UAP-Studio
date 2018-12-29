package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.perspective.action.DelDocAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class LFWDocTreeItem extends LFWDirtoryTreeItem{

	private ImageDescriptor imageDescriptor = null;
	private File resFile = null;
	
	public LFWDocTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
		this.resFile = file;
	}
	protected Image getDirImage() {
//		imageDescriptor = WEBProjPlugin.loadImage(
//					WEBProjPlugin.ICONS_PATH, "form.gif");
		return ImageProvider.form;
	}
	
	public void addMenuListener(IMenuManager manager) {
		DelDocAction action = new DelDocAction();
		manager.add(action);
	}
	
	public void mouseDoubleClick(){
		try {
			IProject project = LFWPersTool.getCurrentProject();
			IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
//			IDE.openEditor(page, file);
			EditorUtility.openInEditor(file,true);

		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e);
		}
	}
}
