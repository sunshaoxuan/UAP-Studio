package nc.uap.lfw.perspective.commands;

import nc.uap.lfw.perspective.model.DatasetElementFigure;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class SizeChangeCommand extends Command{
	
	private DatasetElementFigure dataset;
	private Rectangle oldRect;
	private Rectangle newRect;
	public SizeChangeCommand(DatasetElementFigure dataset, Rectangle newRect){
		this.dataset = dataset;
		this.newRect = newRect;
	}
	@Override
	public void execute(){
		dataset.setSize(newRect.getSize().width, newRect.getSize().height);
	}
	
}
