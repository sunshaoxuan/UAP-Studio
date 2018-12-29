package nc.uap.lfw.form;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_form;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * form×é¼þÍ¼
 * @author zhangxya
 *
 */
public class FormElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 233, 176, 66);
	public FormElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_form.FormElementObjFigure_0);
		setBackgroundColor(bgColor);
		FormElementObj formobj = (FormElementObj) ele;
		setTitleText(formobj.getFormComp().getId(), formobj.getFormComp().getId());
		markError(formobj.validate());
		Point point = formobj.getLocation();
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
		return M_form.FormElementObjFigure_0;
	}
	
}
