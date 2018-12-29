package nc.uap.lfw.toolbar;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebElement;

public class ToolBarItemObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = 7478128548498487393L;

	private ToolBarItem toolbarItem;
	
		
	public ToolBarItem getToolbarItem() {
		return toolbarItem;
	}


	public void setToolbarItem(ToolBarItem toolbarItem) {
		this.toolbarItem = toolbarItem;
	}


	public void setPropertyValue(Object id, Object value) {
		
	}

	
	public Object getPropertyValue(Object id) {
		return null;
	}


	@Override
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return toolbarItem;
	}
	
	
}
