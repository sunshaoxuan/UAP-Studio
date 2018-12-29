/**
 * 
 */
package nc.uap.lfw.grid.core;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;

/**
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelElementObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = -1989242340626132502L;
	
	public static final String PROP_ISLAZY = "element_ISLAZY";

	private GridTreeLevel gridTreelevel = null;
	
	private LFWBasicElementObj parentTreeLevel = null;
	
	private Dataset ds = null;
	
	@Override
	public WebElement getWebElement() {
		return gridTreelevel;
	}

	@Override
	public Object getPropertyValue(Object id) {
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

	public GridTreeLevel getGridTreelevel() {
		return gridTreelevel;
	}

	public void setGridTreelevel(GridTreeLevel gridTreelevel) {
		this.gridTreelevel = gridTreelevel;
	}

	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	public LFWBasicElementObj getParentTreeLevel() {
		return parentTreeLevel;
	}

	public void setParentTreeLevel(LFWBasicElementObj parentTreeLevel) {
		this.parentTreeLevel = parentTreeLevel;
	}

}
