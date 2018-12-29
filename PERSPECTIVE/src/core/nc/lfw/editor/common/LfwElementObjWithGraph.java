package nc.lfw.editor.common;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.comp.WebElement;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.dialogs.MessageDialog;


public abstract class LfwElementObjWithGraph extends LFWBasicElementObj{
	private static final long serialVersionUID = -343535692109586815L;
	private LfwBaseGraph graph;	// 图形显示位置
	private Point location = new Point(0, 0);
	public static final String PROP_CELL_SIZE = "cell_size";
	public static final String PROP_CELL_LOCATION = "cell_location";
	public static final String warningMessage = "非自定义的组件,属性不可修改!";
	// 图形大小
	private Dimension size = new Dimension(80, 100);
	public Point getLocation() {
		return location;
	}

	public boolean canChange(WebElement webcomp) {
		if(webcomp != null && webcomp.getFrom() != null){
			MessageDialog.openWarning(null, "警告信息", warningMessage);
			return false;
		}
		return true;
	}
	
	public void setLocation(Point location) {
		Point oldP = this.location;
		this.location = location;
		firePropertyChange(PROP_CELL_LOCATION, oldP, location);
	}
	
	public LfwBaseGraph getGraph() {
		return graph;
	}
	public void setGraph(LfwBaseGraph graph) {
		this.graph = graph;
	}
	

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		Dimension oldD = this.size;
		this.size = size;
		firePropertyChange(PROP_CELL_SIZE, oldD, size);
	}
	
	public String validate(){
		try{
			WebElement ele = getWebElement();
			if(ele != null)
				ele.validate();
		}catch(Exception e){
			MainPlugin.getDefault().logError(e);
			return e.getMessage();
		}
		return null;
	}
	
	public abstract WebElement getWebElement();
	
}
