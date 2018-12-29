package nc.lfw.editor.pagemeta;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class WindowEditor extends MultiPageEditorPart{

	@Override
	protected void createPages() {
		// TODO Auto-generated method stub
		PagemetaEditorInput input = (PagemetaEditorInput)getEditorInput();
		try {
			int index = addPage(new PagemetaEditor(),input);
			setPageText(index, M_pagemeta.WindowEditor_0);
			index = addPage(new RelationEditor(), input);
			setPageText(index, M_pagemeta.WindowEditor_1);
		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Object obj = this.getSelectedPage();
		if(obj instanceof PagemetaEditor){
			((PagemetaEditor)obj).doSave(null);
		}
		if(obj instanceof RelationEditor){
			((RelationEditor)obj).doSave(null);
		}
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

}
