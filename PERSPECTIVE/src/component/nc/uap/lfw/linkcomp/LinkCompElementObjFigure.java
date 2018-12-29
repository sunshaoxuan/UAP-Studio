package nc.uap.lfw.linkcomp;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_listview;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class LinkCompElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 186, 196, 2);
	public LinkCompElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_listview.LinkCompElementObjFigure_0);
		setBackgroundColor(bgColor);
		LinkCompElementObj linkobj = (LinkCompElementObj) ele;
		setTitleText(linkobj.getLinkComp().getId(), linkobj.getLinkComp().getId());
		markError(linkobj.validate());
		Point point = linkobj.getLocation();
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
		return M_listview.LinkCompElementObjFigure_0;
	}
	
}
