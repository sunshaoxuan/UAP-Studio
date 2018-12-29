package nc.lfw.editor.menubar;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.figure.ui.IDirectEditable;
import nc.uap.lfw.figure.ui.NullBorder;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class MenuItemLabel extends Label implements IDirectEditable {
	
	private MenuItem item;

	public MenuItemLabel(MenuItem item) {
		super(item.getText());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.item = item;
		markError();
	}

	private void markError() {

	}

	
	public Object getEditableObj() {
		return item;
	}

}
