package nc.uap.lfw.tree.core;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NameLabel;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.tree.DsLabel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * TreeLevel Figrue
 * @author zhangxya
 *
 */
public class TreeLevelElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 45, 186, 206);
	private LfwElementObjWithGraph element = null;
	public LfwElementObjWithGraph getElement() {
		return element;
	}

	public void setElement(LfwElementObjWithGraph element) {
		this.element = element;
	}

	public TreeLevelElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		this.element = ele;
		setTypeLabText(M_tree.TreeLevelElementObjFigure_0);
		setBackgroundColor(bgColor);
		TreeLevelElementObj treelevel = (TreeLevelElementObj) ele;
		setTitleText(treelevel.getId(), treelevel.getId());
		addDs();
		Point point = treelevel.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		setBounds(new Rectangle(x, y, 100, 100));
		
	}

	private void addDs() {
		TreeLevelElementObj treelevel = (TreeLevelElementObj)element;
		Dataset ds = treelevel.getDs();
		NameLabel label = new NameLabel(M_tree.TreeLevelElementObjFigure_1, ds.getId());
		addToContent(label);
		DsLabel labelds = new DsLabel(ds.getId(), ds);
		addToContent(labelds);
	}
	
	protected String getTypeText() {
		return M_tree.TreeLevelElementObjFigure_2;
	}
	
}