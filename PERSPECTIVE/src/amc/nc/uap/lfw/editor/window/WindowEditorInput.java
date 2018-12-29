package nc.uap.lfw.editor.window;

import org.eclipse.core.resources.IProject;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;

public class WindowEditorInput extends PagemetaEditorInput {
	
	/**
	 * 当前工程
	 */
	private IProject project = null;

	public WindowEditorInput(LfwWindow pagemeta) {
		super(pagemeta);
		this.project = LFWAMCPersTool.getCurrentProject();
	}

	public String getName() {
		return M_editor.WindowEditorInput_0;
	}
	
	public String getToolTipText() {
		return M_editor.WindowEditorInput_0;
	}
	
	public IProject getProject() {
		return project;
	}
	
}
