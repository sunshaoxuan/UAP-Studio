package nc.uap.lfw.pagination;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.jface.resource.ImageDescriptor;

public class PaginationEditorInput  extends ElementEditorInput{

	public PaginationEditorInput(PaginationComp paginationComp, LfwView widget, LfwWindow pagemeta) {
		super(paginationComp, widget, pagemeta);
	}
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	public String getName() {
		return M_iframe.PaginationEditorInput_0;
	}
	public String getToolTipText() {
		return M_iframe.PaginationEditorInput_0;
	}

}
