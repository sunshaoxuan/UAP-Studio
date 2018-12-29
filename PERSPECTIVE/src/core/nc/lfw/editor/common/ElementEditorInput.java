package nc.lfw.editor.common;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;

public class ElementEditorInput extends WidgetEditorInput {
	private WebElement element;
	private WebElement cloneElement;
	public ElementEditorInput(WebElement ele, LfwView widget, LfwWindow pagemeta) {
		super(widget, pagemeta);
		this.element = ele;
	}
	public WebElement getElement() {
		return element;
	}
	
	public WebElement getCloneElement() {
		if(cloneElement == null)
			cloneElement = (WebElement) element.clone();
		return cloneElement;
	}
	
}
