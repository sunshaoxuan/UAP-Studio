package nc.uap.lfw.figure.ui;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

public class NullBorder extends AbstractBorder{
	public Insets getInsets(IFigure f) {
		return new Insets(0,2,0,1);
	}
	public void paint(IFigure f, Graphics g, Insets insets) {
	}
	
}