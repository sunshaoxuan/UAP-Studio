package nc.uap.lfw.perspective.commands;

public class CreateBendPointCommand extends AbstractBendPointCommand {

	public boolean canExecute() {
		return conn != null && point != null;
	}
	public void redo() {
		conn.addBendPoint(index, point);
	}

	public void undo() {
		conn.removeBendPoint(index);
	}
    public String getLabel() {      
        return "create bendpoint";
     }  
}
