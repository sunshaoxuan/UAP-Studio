package nc.uap.lfw.grid.action;

import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * grid取消选中所有行
 * @author zhangxya
 *
 */
public class GridUnSelectedAllAction extends Action{
	private GridPropertisView view;
	
	public GridUnSelectedAllAction(GridPropertisView view) {
		setText(M_grid.GridUnSelectedAllAction_0);
		setHoverImageDescriptor(PaletteImage.getUnSelectedAllImgDescriptor());
		this.view = view;
	}
	
	public void run(){
		CheckboxTreeViewer ctx = view.getCtv();
		ctx.setAllChecked(false);
	}
}
