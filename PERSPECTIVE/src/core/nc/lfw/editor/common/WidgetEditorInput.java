package nc.lfw.editor.common;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_pagemeta;

public class WidgetEditorInput extends PagemetaEditorInput {
	private LfwView widget;
	private LfwView cloneElement;
	public WidgetEditorInput(LfwView widget, LfwWindow pagemeta) {
		super(pagemeta);
		this.widget = widget;
	}
	public LfwView getWidget() {
		return widget;
	}
	
	public WebElement getCloneElement() {
		if(cloneElement == null)
			cloneElement = (LfwView) widget.clone();
		return cloneElement;
	}

	public String getName() {
		return M_pagemeta.WidgetEditorInput_0;
	}
	
}
