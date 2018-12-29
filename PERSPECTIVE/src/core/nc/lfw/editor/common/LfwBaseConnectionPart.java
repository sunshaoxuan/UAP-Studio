package nc.lfw.editor.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

/**
 * @author zhangxya
 * 
 */
public abstract class LfwBaseConnectionPart extends AbstractConnectionEditPart
		implements PropertyChangeListener {

	protected abstract void createEditPolicies();

	public void propertyChange(PropertyChangeEvent evt) {
	}

	public void activate() {
		super.activate();
		((Connection) getModel()).addPropertyChangeListener(this);
	}

	public void performRequest(Request request) {
		super.performRequest(request);
	}

	
	public void deactivate() {
		super.deactivate();
		((Connection) getModel()).removePropertyChangeListener(this);
	}

	protected abstract IFigure createFigure();

	public void setSelected(int value) {
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
			((PolylineConnection) getFigure()).setLineWidth(2);
		else
			((PolylineConnection) getFigure()).setLineWidth(1);
	}

}
