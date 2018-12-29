package nc.uap.lfw.perspective.commands;

import nc.lfw.editor.common.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class AbstractBendPointCommand extends Command {
	protected Connection conn = null;
	protected int index = 0;
	protected Point point = null;
	public void setIndex(int index) {
		this.index = index;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	public void execute() {
		redo();
	}
}
