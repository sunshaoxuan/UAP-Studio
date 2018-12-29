package nc.uap.lfw.textcomp;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_textcomp;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * textcompÍ¼ÐÎ
 * @author zhangxya
 *
 */
public class TextCompElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 57, 242, 235);
	public TextCompElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_textcomp.TextCompElementObjFigure_0);
		setBackgroundColor(bgColor);
		TextCompElementObj textcompobj = (TextCompElementObj) ele;
		setTitleText(textcompobj.getTextComp().getId(),textcompobj.getTextComp().getId());
		markError(textcompobj.validate());
		Point point = textcompobj.getLocation();
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
		return M_textcomp.TextCompElementObjFigure_0;
	}
}
	
