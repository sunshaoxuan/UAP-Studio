
package nc.uap.lfw.toolbar;

import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.figure.ui.IDirectEditable;
import nc.uap.lfw.figure.ui.NullBorder;
import nc.uap.lfw.lang.M_toolbar;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class ToolBarItemLabel extends Label implements IDirectEditable {
	
	private ToolBarItem item;

	public ToolBarItemLabel(ToolBarItem item) {
		String text = ""; //$NON-NLS-1$
		if(item.getText() == null || item.getText().equals("")) //$NON-NLS-1$
			text = M_toolbar.ToolBarItemLabel_2;
		else
			text = item.getText();
		setText(text);
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.item = item;
		markError();
	}

	public void updateFigure(ToolBarItem item){
		this.item = item;
		String text = ""; //$NON-NLS-1$
		if(item.getText() == null || item.getText().equals("")) //$NON-NLS-1$
			text = M_toolbar.ToolBarItemLabel_2;
		else 
			text = item.getText();
		setText(text);
		markError();
	}
	private void markError() {

	}

	
	public Object getEditableObj() {
		return item;
	}

}
