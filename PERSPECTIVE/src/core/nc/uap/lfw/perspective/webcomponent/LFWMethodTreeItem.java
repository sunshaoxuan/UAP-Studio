package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;

public class LFWMethodTreeItem extends LFWDirtoryTreeItem{
	
	private int positon = 0;
	
	private File resFile = null;
	
	private String mothodName = null;
	
	private int length = 0;

	public LFWMethodTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
		this.setResFile(file);
		this.setMothodName(text);
	}
	
	public int getPositon() {
		return positon;
	}

	public void setPositon(int positon) {
		this.positon = positon;
	}
	
	public File getResFile() {
		return resFile;
	}

	public void setResFile(File resFile) {
		this.resFile = resFile;
	}
	

	public String getMothodName() {
		return mothodName;
	}

	public void setMothodName(String mothodName) {
		this.mothodName = mothodName;
	}
	
	

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	protected Image getDirImage() {
		return ImageProvider.start;
	}
	@SuppressWarnings("restriction")
	public void mouseDoubleClick() {
		try {
			IProject project = LFWPersTool.getCurrentProject();
			IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
			EditorUtility.openInEditor(file);
			ITextEditor editor = (ITextEditor)MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			ISelection newSelection = new TextSelection(positon, length);
			editor.getSelectionProvider().setSelection(newSelection);
//			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			

		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e);
		}
	}


}
