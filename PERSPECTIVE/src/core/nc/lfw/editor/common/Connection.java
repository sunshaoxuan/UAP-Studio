package nc.lfw.editor.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

/**
 * 连接基类
 * @author zhangxya
 *
 */
public class Connection extends LFWBasicElementObj{
	
	public static final String SOURCE_CONSTRAINT = "sourceConstraint";
	public static final String TARGET_CONSTRAINT = "targetConstrain";
	public static final String PROP_SOURCE_ATTR = "relation_sourc_attr";
	private LfwBaseGraph grahp = null;
	public LfwBaseGraph getGrahp() {
		return grahp;
	}

	public void setGrahp(LfwBaseGraph grahp) {
		this.grahp = grahp;
	}

	private static final long serialVersionUID = 1794771154424833502L;
	public static final String PROP_BEND_POINT = "relation_bend_point";
	private LFWBasicElementObj source = null;
	private LFWBasicElementObj target = null;
	private List<Point> bendPoints = new ArrayList<Point>();
	private boolean isConnected = false;
	public Connection(LFWBasicElementObj source, LFWBasicElementObj target){
		this.source = source;
		this.target = target;
	}
	
	public void connect(){
		if(!isConnected){
			source.addSourceConn(this);
			target.addTargetConn(this);
			isConnected = true;
		}
	}
	public void disConnect(){
		if(isConnected){
			source.removeSourceConn(this);
			target.removeTargetConn(this);
			isConnected = false;
		}
	}
	public void addBendPoint(int index, Point point){
		bendPoints.add(index, point);
		fireStructureChange(PROP_BEND_POINT, point);
	}
	public Point removeBendPoint(int index){
		Point point = bendPoints.remove(index);
		fireStructureChange(PROP_BEND_POINT, point);
		return point;
	}
	public List<Point> getBendPoints(){
		return bendPoints;
	}
//	public void removeAllBendPoints(){
//		getBendPoints().clear();
//		fireStructureChange(PROP_BEND_POINT, null);
//	}
//	public void appendBendPointEle(Document doc, Element parent){
//		List<Point> points = getBendPoints();
//		Element pointsEle = doc.createElement("points");
//		parent.appendChild(pointsEle);
////		pw.println(tabStr+"<points>");
//		for (int i = 0; i < points.size(); i++) {
//			Point p = points.get(i);
//			Element tempEle = doc.createElement("point");
//			tempEle.setAttribute("x", p.x+"");
//			tempEle.setAttribute("y", p.y+"");
//			pointsEle.appendChild(tempEle);
//		}
//		
//	}
//	public void printBendPointXML(PrintWriter pw,String tabStr){
//		List<Point> points = getBendPoints();
//		pw.println(tabStr+"<points>");
//		for (int i = 0; i < points.size(); i++) {
//			Point p = points.get(i);
//			pw.print(tabStr+"\t<point x='");
//			pw.print(p.x);
//			pw.print("' y='");
//			pw.print(p.y);
//			pw.println("'/>");
//		}
//		pw.println(tabStr+"</points>");
//	}
//	protected static void parseConnectionBendPoint(Node node, Connection con){
//		NodeList nl = node.getChildNodes();
//		for (int i = 0; i < nl.getLength(); i++) {
//			Node child = nl.item(i);
//			String str =child.getNodeName();
//			if("points".equalsIgnoreCase(str)){
//				NodeList cnl = child.getChildNodes();
//				int index = 0;
//				for (int j = 0; j < cnl.getLength(); j++) {
//					Node pointNode = cnl.item(j);
//					if("point".equalsIgnoreCase(pointNode.getNodeName())){
//						NamedNodeMap m = pointNode.getAttributes();
//						int x = Integer.parseInt(m.getNamedItem("x").getNodeValue());
//						int y = Integer.parseInt(m.getNamedItem("y").getNodeValue());
//						con.addBendPoint(index++, new Point(x,y));
//					}
//				}
//			}
//		}
//
//	}
	
	public LFWBasicElementObj getSource() {
		return source;
	}
	public LFWBasicElementObj getTarget() {
		return target;
	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
	}

	public void setSource(LFWBasicElementObj source) {
		this.source = source;
	}

	public void setTarget(LFWBasicElementObj target) {
		this.target = target;
	}
}
