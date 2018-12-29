package nc.uap.lfw.selfdefcomp.core;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class SelfDefEleObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 010,171,131);
	public SelfDefEleObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText("<<自定义控件>>");
		setBackgroundColor(bgColor);
		SelfDefEleObj selfdefObj = (SelfDefEleObj) ele;
		setTitleText(selfdefObj.getSelfDefComp().getId(), selfdefObj.getSelfDefComp().getId());
		markError(selfdefObj.validate());
		Point point = selfdefObj.getLocation();
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
		return "<<自定义控件>>";
	}
}
	