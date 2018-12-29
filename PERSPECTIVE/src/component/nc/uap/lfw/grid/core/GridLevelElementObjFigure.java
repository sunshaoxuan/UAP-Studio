/**
 * 
 */
package nc.uap.lfw.grid.core;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NameLabel;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.tree.DsLabel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelElementObjFigure extends LFWBaseRectangleFigure {
	
	private static Color bgColor = new Color(null, 45, 186, 206);
	
	private LfwElementObjWithGraph element = null;

	public GridLevelElementObjFigure(LfwElementObjWithGraph ele) {
		super(ele);
		this.element = ele;
		setTypeLabText(M_grid.GridLevelElementObjFigure_0);
		setBackgroundColor(bgColor);
		GridLevelElementObj gridlevel = (GridLevelElementObj) ele;
		setTitleText(gridlevel.getId(), gridlevel.getId());
		addDs();
		Point point = gridlevel.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y + 200;
		}else{
			x = 100;
			y = 300;
		}
		setBounds(new Rectangle(x, y, 100, 100));
	}

	private void addDs() {
		GridLevelElementObj gridlevel = (GridLevelElementObj)element;
		Dataset ds = gridlevel.getDs();
		NameLabel label = new NameLabel(M_grid.GridLevelElementObjFigure_1, ds.getId());
		addToContent(label);
		DsLabel labelds = new DsLabel(ds.getId(), ds);
		addToContent(labelds);
	}
	
	@Override
	protected String getTypeText() {
		return "<<GridLevel>>"; //$NON-NLS-1$
	}

	public LfwElementObjWithGraph getElement() {
		return element;
	}

	public void setElement(LfwElementObjWithGraph element) {
		this.element = element;
	}

}
