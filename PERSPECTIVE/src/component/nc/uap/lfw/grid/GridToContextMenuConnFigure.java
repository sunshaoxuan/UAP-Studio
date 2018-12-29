package nc.uap.lfw.grid;

import nc.lfw.editor.common.Connection;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * grid与右键菜单关联图形
 * @author zhangxya
 *
 */
public class GridToContextMenuConnFigure extends PolylineConnection {
	private Label lblCnnName = new Label();
	private Border border = new LineBorder(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
	private String errStr = null;
	
	private Connection con = null;
	
	public Connection getCon() {
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}

	public GridToContextMenuConnFigure(GridToContextMenuConnection con) {
		super();
		this.con = con;
		PolygonDecoration pd = new PolygonDecoration();
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(0, 0);
		pl.addPoint(-2, -2);
		pd.setTemplate(pl);
		setLineStyle(SWT.LINE_DASH);
		setTargetDecoration(pd);
		setConnectionRouter(new BendpointConnectionRouter());
		setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
	}
	
	public void setConnName(String newName){
		lblCnnName.setText(modifyString(newName));
		if(newName == null || newName.trim().length() == 0)
			lblCnnName.setOpaque(false);
		else
			lblCnnName.setOpaque(true);
	}
	public boolean isCnnNameFigure(IFigure figure) {
		if (figure.equals(lblCnnName)) {
			return true;
		} else {
			return false;
		}
	}
	protected String modifyString(String str) {
		if (str == null) {
			str = "";
		}
		int count = 10 - str.length();
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < count; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	public void setSelection(boolean sel){
		if(sel){
			lblCnnName.setBorder(border);
		}else{
			lblCnnName.setBorder(null);
		}
	}
	
	
	public void paint(Graphics g) {
		if(lblCnnName.getBorder() != null){
			g.setLineWidth(2);
		}else{
			g.setLineWidth(1);
		}
		super.paint(g);
	}
	public void markError(String errMsg){
		errStr = errMsg;
		if(errStr != null){
			//lblCnnName.setIcon(ImageFactory.getErrorImg());
		}else{
			lblCnnName.setIcon(null);
		}
	}
	public String getErrorStr(){
		return errStr;
	}
	public boolean isConnNameLabel(Label lbl){
		return lblCnnName.equals(lbl);
	}

}
