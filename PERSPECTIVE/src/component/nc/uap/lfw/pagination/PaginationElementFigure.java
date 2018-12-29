package nc.uap.lfw.pagination;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_listview;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class PaginationElementFigure  extends LFWBaseRectangleFigure{

	private static Color bgColor = new Color(null, 57, 242, 235);
	public PaginationElementFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_listview.PaginationElementFigure_0);
		setBackgroundColor(bgColor);
		PaginationElementObj lvobj = (PaginationElementObj) ele;
		setTitleText(lvobj.getPaginationComp().getId(), lvobj.getPaginationComp().getId());
//		markError(lvobj.validate());
		Point point = lvobj.getLocation();
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

	@Override
	protected String getTypeText() {
		return M_listview.PaginationElementFigure_0;
	}
	
}
