package nc.uap.portal.portlets;

import nc.uap.portal.core.PortalBaseRectangleFigure;
import nc.uap.portal.core.PortalElementObjWithGraph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * portlet Õº–Œ¿‡
 * 
 * @author dingrf
 *
 */
public class PortletEleObjFigure extends PortalBaseRectangleFigure{
	
	/**—’…´*/
	private static Color bgColor = new Color(null, 77, 162, 85);
	
	public PortletEleObjFigure(PortalElementObjWithGraph ele){
		super(ele);
		setTypeLabText("<<Portlet>>");
		setBackgroundColor(bgColor);
		PortletElementObj portlet = (PortletElementObj) ele;
		setTitleText(portlet.getPortlet().getPortletName(), portlet.getPortlet().getPortletName());
		Point point = portlet.getLocation();
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
		return "<<Portlet>>";
	}
}
	
