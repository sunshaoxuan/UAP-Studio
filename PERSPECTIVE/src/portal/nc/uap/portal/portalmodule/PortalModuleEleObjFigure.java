package nc.uap.portal.portalmodule;

import nc.uap.portal.core.PortalBaseRectangleFigure;
import nc.uap.portal.core.PortalElementObjWithGraph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * PortalModule Figure 
 * 
 * @author dingrf
 */
public class PortalModuleEleObjFigure extends PortalBaseRectangleFigure{

	/**Í¼ÐÎÑÕÉ«*/
	private static Color bgColor = new Color(null, 68, 68, 247);
	
	public PortalModuleEleObjFigure(PortalElementObjWithGraph ele){
		super(ele);
		setTypeLabText("<<PortalModule>>");
		setBackgroundColor(bgColor);
		PortalModuleElementObj portal = (PortalModuleElementObj) ele;
		Point point = portal.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		setBounds(new Rectangle(x, y, 150, 150));
	}
	
	protected String getTypeText() {
		return "<<PortalModule>>";
	}
}
	
