package nc.lfw.editor.menubar.ele;

import java.util.Iterator;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.WebElement;

public class MenuItemObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = 7478128548498487393L;

	private MenuItem menuItem;

	private MenuElementObj child = null;

	public void setPropertyValue(Object id, Object value) {

	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setChild(MenuElementObj obj) {
		if (obj != null) {
			child = obj;
			if (obj.getChildrenItems() != null) {
				Iterator<MenuItem> it = obj.getChildrenItems().iterator();
				while (it.hasNext())
					menuItem.addMenuItem(it.next());
			}
		}
	}

	public void removeChild(MenuElementObj obj) {
		child = null;
		menuItem.getChildList().clear();
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public MenuElementObj getChild() {
		return child;
	}

	public WebElement getWebElement() {
		return menuItem;
	}

}
