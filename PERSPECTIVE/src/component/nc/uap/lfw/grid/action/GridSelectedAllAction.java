package nc.uap.lfw.grid.action;

import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * grid选中所有行
 * @author zhangxya
 *
 */
public class GridSelectedAllAction extends Action{
	
	private GridPropertisView view;
	
	public GridSelectedAllAction(GridPropertisView view) {
		super(M_grid.GridSelectedAllAction_0);
		setHoverImageDescriptor(PaletteImage.getSelectedAllImgDescriptor());
		this.view = view;
	}
	
	public void run(){
		CheckboxTreeViewer ctx = view.getCtv();
		ctx.setAllChecked(true);
	}
}


