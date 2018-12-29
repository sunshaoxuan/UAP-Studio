/**
 * 
 */
package nc.uap.lfw.editor.application;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.lfwtools.perspective.MainPlugin;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * Application图形显示设置类
 * @author chouhl
 *
 */
public class ApplicationFigure extends LFWBaseRectangleFigure {

	/**
	 * 大小
	 */
	private Dimension dimen;
	/**
	 * 总高度 
	 */
	private int height = 0;
	/**
	 * 图形颜色
	 */
	private static Color bgColor = new Color(null, 190, 195, 240);

	public ApplicationFigure(ApplicationObj obj) {
		super(obj);
		
		setTypeLabText(getTypeLabText());		
		setBackgroundColor(getBackgroundColor());		
		obj.setFigure(this);
		markError(obj.validate());
		if(obj.getErrorMsg()!=null){
			markError(obj.getErrorMsg());
			MainPlugin.getDefault().logError(obj.getErrorMsg());
		}
		//设置大小和位置
		Point point = obj.getLocation();
		dimen = obj.getSize();
		this.height += 3 * LINE_HEIGHT;
		setBounds(new Rectangle(point.x, point.y, dimen.width, dimen.height < this.height ? this.height : dimen.height));
	}

	protected String getTypeText() {
		return "<<Application>>";
	}

	protected String getTypeLabText() {
		return "<<Application>>";
	}
	
	public Color getBackgroundColor() {
		return bgColor;
	}
	
}
