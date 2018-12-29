package nc.uap.lfw.perspective.listener;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.editors.text.TextEditor;

public class LfwJsEditor extends TextEditor {
	
	public LfwJsEditor(IWorkbenchPartSite site, IEditorInput editorInput) {
    	super();
    	super.setSite(site);
    	super.setInput(editorInput);
    }
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}
	
	public IDocument getDocument() {
		return getSourceViewer().getDocument();
	}
	
}
