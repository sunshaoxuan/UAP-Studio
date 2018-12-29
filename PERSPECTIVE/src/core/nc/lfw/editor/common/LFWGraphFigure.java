package nc.lfw.editor.common;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;

/**
 * »­²¼Figure
 * @author zhangxya
 *
 */
public class LFWGraphFigure extends FreeformLayer {
	
	public LFWGraphFigure() {
		super();
		setLayoutManager(new FreeformLayout());
	}

}

