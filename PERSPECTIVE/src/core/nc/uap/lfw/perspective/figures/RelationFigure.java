package nc.uap.lfw.perspective.figures;

import nc.lfw.editor.common.Connection;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class RelationFigure extends ConnectionFigure {
	private Label sourceConstraintFigure = new Label();
	private Label targetConstraintFigure = new Label();
	private Border border = new LineBorder(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	
	public RelationFigure(Connection con){
		super(con);
		PolygonDecoration targetPD = new PolygonDecoration();
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(0, 0);
		pl.addPoint(-2, -2);
		targetPD.setTemplate(pl);
		setTargetDecoration(targetPD);
		setConnectionRouter(new BendpointConnectionRouter());
		setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		ConnectionEndpointLocator sourceLocator = new ConnectionEndpointLocator(this, false);
		ConnectionEndpointLocator targetLocator = new ConnectionEndpointLocator(this, true);
		add(sourceConstraintFigure, sourceLocator);
		add(targetConstraintFigure, targetLocator);
	}
	public boolean isTargetConstraintFigure(IFigure figure) {
		if (figure.equals(targetConstraintFigure)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSourceConstraintFigure(IFigure figure) {
		if (figure.equals(sourceConstraintFigure)) {
			return true;
		} else {
			return false;
		}
	}

	public void setSourceConstraintFigureText(String text) {
		sourceConstraintFigure.setText(modifyString(text));
	}

	public void setTargetConstraintFigureText(String text) {
		targetConstraintFigure.setText(modifyString(text));
	}
	public void setSelection(boolean sel){
		super.setSelection(sel);
		if(sel){
			sourceConstraintFigure.setBorder(border);
			targetConstraintFigure.setBorder(border);
		}else{
			sourceConstraintFigure.setBorder(null);
			targetConstraintFigure.setBorder(null);

		}
	}



	
}
