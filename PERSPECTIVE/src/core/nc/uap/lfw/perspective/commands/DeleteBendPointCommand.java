package nc.uap.lfw.perspective.commands;

public class DeleteBendPointCommand extends AbstractBendPointCommand {
	public boolean canExecute() {
		return conn != null ;
	}
	public void redo() {
		point = conn.removeBendPoint(index);
	}

	public void undo() {
		conn.addBendPoint(index, point);
	}
    public String getLabel() {      
        return "delete bendpoint";
     }  
}
