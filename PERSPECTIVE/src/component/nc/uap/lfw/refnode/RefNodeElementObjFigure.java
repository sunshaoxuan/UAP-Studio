package nc.uap.lfw.refnode;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_refnode;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * 参照节点图形
 * @author zhangxya
 *
 */
public class RefNodeElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 39, 190, 0);
	public RefNodeElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_refnode.RefNodeElementObjFigure_0);
		setBackgroundColor(bgColor);
		RefNodeElementObj refnodeobj = (RefNodeElementObj) ele;
		setTitleText(refnodeobj.getRefnode().getId(), refnodeobj.getRefnode().getId());
		
		markError(refnodeobj.validate());
		Point point = refnodeobj.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		Dimension dimen = refnodeobj.getSize();
		int width, height;
		if(dimen != null){
			width = dimen.width;
			height = dimen.height;
		}
		else{
			width = 150;
			height = 150;
		}
		setBounds(new Rectangle(x, y, width, height));
		
	}
	
	protected String getTypeText() {
		return M_refnode.RefNodeElementObjFigure_0;
	}
	
}