package nc.uap.lfw.perspective.commands;

import org.eclipse.draw2d.geometry.Point;

public class MoveBendPointCommand extends AbstractBendPointCommand {
	private Point oldPoint = null;
	public boolean canExecute() {
		return conn != null && point != null;
	}
	public void redo() {
		oldPoint = conn.removeBendPoint(index);
		conn.addBendPoint(index, point);
		
	}

	public void undo() {
		conn.removeBendPoint(index);
		conn.addBendPoint(index, oldPoint);
	}
    public String getLabel() {      
        return "move bendpoint";
     }  
}
