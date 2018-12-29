package nc.uap.lfw.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * grid model
 * @author zhangxya
 *
 */
public class GridElementObj extends LFWWebComponentObj{

	private static final long serialVersionUID = 6253081418703115641L;

	public static final String PROP_GRID_ELEMENT ="grid_element"; //$NON-NLS-1$
	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; //$NON-NLS-1$
	public static final String Grid_ADD_CELL_PROPS = "add_grid_props"; //$NON-NLS-1$
	public static final String PROP_EDITABLE = "element_EDITABLE"; //$NON-NLS-1$
	public static final String PROP_DATASET = "element_DATASET"; //$NON-NLS-1$
	public static final String PROP_MULTISELECT = "element_MULTISELECT"; //$NON-NLS-1$
	public static final String PROP_ROWHEIGHT = "element_ROWHEIGHT"; //$NON-NLS-1$
	public static final String PROP_HEADROWHEIGHT = "element_headerRowHeight"; //$NON-NLS-1$
	public static final String PROP_SHOWNMUCOL = "element_showNumCol"; //$NON-NLS-1$
	public static final String PROP_SHOWSUMROW = "element_showSumRow"; //$NON-NLS-1$
	public static final String PROP_PAGESIZE = "element_pageSize"; //$NON-NLS-1$
	public static final String PROP_SIMPLEPAGEBAR = "element_simplePageBar"; //$NON-NLS-1$
	public static final String PROP_SORTALBE= "element_sortable"; //$NON-NLS-1$
	public static final String PROP_PAGENATIONTOP = "element_pagenationTop"; //$NON-NLS-1$
	public static final String PROP_SHOWCOLINFO = "element_showColInfo"; //$NON-NLS-1$
	public static final String PROP_GROUPCOLUMNS = "element_GROUPCOLUMNS"; //$NON-NLS-1$
	public static final String PROP_SHOWHEADER = "element_showHeader"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	public static final String PROP_ROWRENDER = "element_ROWRENDER"; //$NON-NLS-1$
	public static final String PROP_EXTENDCELLEDITOR = "element_EXTENDCELLEDITOR"; //$NON-NLS-1$
	public static final String PROP_EXPANDTREE = "element_EXPANDTREE"; //$NON-NLS-1$
	public static final String PROP_SHOWFORM = "element_SHOWFORM"; //$NON-NLS-1$
	private GridComp gridComp;
	private Dataset ds;
	private List<IGridColumn> props = new ArrayList<IGridColumn>();
	
	public GridComp getGridComp() {
		return gridComp;
	}
	
	public void setGridComp(GridComp gridComp) {
		this.gridComp = gridComp;
		fireStructureChange(PROP_GRID_ELEMENT,  gridComp);
	}

	
	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}
	
	public void addProp(IGridColumn prop){
		props.add(prop);
		fireStructureChange(Grid_ADD_CELL_PROPS, prop);
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[18];
		pds[0] = new ComboBoxPropertyDescriptor(PROP_EDITABLE,M_grid.GridElementObj_0, Constant.ISLAZY);
		pds[0].setCategory(M_grid.GridElementObj_1);
		pds[1] = new NoEditableTextPropertyDescriptor(PROP_DATASET,M_grid.GridElementObj_2);
		pds[1].setCategory(M_grid.GridElementObj_1);
		pds[2] = new ComboBoxPropertyDescriptor(PROP_MULTISELECT,M_grid.GridElementObj_3, Constant.ISLAZY);
		pds[2].setCategory(M_grid.GridElementObj_1);
		pds[3] = new TextPropertyDescriptor(PROP_ROWHEIGHT, M_grid.GridElementObj_4);
		pds[3].setCategory(M_grid.GridElementObj_1);
		pds[4] = new TextPropertyDescriptor(PROP_HEADROWHEIGHT, M_grid.GridElementObj_5);
		pds[4].setCategory(M_grid.GridElementObj_1);
		pds[5] = new ComboBoxPropertyDescriptor(PROP_SHOWNMUCOL,M_grid.GridElementObj_6, Constant.ISLAZY);
		pds[5].setCategory(M_grid.GridElementObj_1);
		pds[6] = new ComboBoxPropertyDescriptor(PROP_SHOWSUMROW,M_grid.GridElementObj_7, Constant.ISLAZY);
		pds[6].setCategory(M_grid.GridElementObj_1);
		pds[7] = new TextPropertyDescriptor(PROP_PAGESIZE, M_grid.GridElementObj_8);
		pds[7].setCategory(M_grid.GridElementObj_1);
//		pds[8] = new ComboBoxPropertyDescriptor(PROP_SIMPLEPAGEBAR,M_grid.GridElementObj_9, Constant.ISLAZY);
//		pds[8].setCategory(M_grid.GridElementObj_1);
		pds[8] = new ComboBoxPropertyDescriptor(PROP_SORTALBE,M_grid.GridElementObj_10, Constant.ISLAZY);
		pds[8].setCategory(M_grid.GridElementObj_1);
		pds[9] = new ComboBoxPropertyDescriptor(PROP_PAGENATIONTOP,M_grid.GridElementObj_11, Constant.ISLAZY);
		pds[9].setCategory(M_grid.GridElementObj_1);
		pds[10] = new ComboBoxPropertyDescriptor(PROP_SHOWCOLINFO,M_grid.GridElementObj_12, Constant.ISLAZY);
		pds[10].setCategory(M_grid.GridElementObj_1);
		pds[11] = new TextPropertyDescriptor(PROP_GROUPCOLUMNS, M_grid.GridElementObj_13);
		pds[11].setCategory(M_grid.GridElementObj_1);
		pds[12] = new ComboBoxPropertyDescriptor(PROP_SHOWHEADER,M_grid.GridElementObj_14, Constant.ISLAZY);
		pds[12].setCategory(M_grid.GridElementObj_1);
		pds[13] = new TextPropertyDescriptor(PROP_ROWRENDER,M_grid.GridElementObj_15);
		pds[13].setCategory(M_grid.GridElementObj_1);
		pds[14] = new TextPropertyDescriptor(PROP_EXTENDCELLEDITOR,M_grid.GridElementObj_16);
		pds[14].setCategory(M_grid.GridElementObj_1);
		//caption
		pds[15] = new TextPropertyDescriptor(PROP_CAPTION,M_grid.GridElementObj_17);
		pds[15].setCategory(M_grid.GridElementObj_18);
		
		pds[16] = new ComboBoxPropertyDescriptor(PROP_EXPANDTREE,M_grid.GridElementObj_19, Constant.ISLAZY);
		pds[16].setCategory(M_grid.GridElementObj_1);
		pds[17] = new ComboBoxPropertyDescriptor(PROP_SHOWFORM,M_grid.GridElementObj_20, Constant.ISLAZY);
		pds[17].setCategory(M_grid.GridElementObj_1);

		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_DATASET.equals(id)){
			gridComp.setDataset((String)value);
			fireStructureChange(PROP_GRID_ELEMENT,  gridComp);
		}
		else if(PROP_EDITABLE.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setEditable(truevalue);
			//setEditable((String)value);
		}
		else if(PROP_MULTISELECT.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setMultiSelect(truevalue);
		}
		else if(PROP_ROWHEIGHT.equals(id))
			gridComp.setRowHeight((String)value);
		else if(PROP_HEADROWHEIGHT.equals(id))
			gridComp.setHeaderRowHeight((String)value);
		else if(PROP_SHOWNMUCOL.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setShowNumCol(truevalue);
		}
		else if(PROP_SHOWSUMROW.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setShowSumRow(truevalue);
		}
		else if(PROP_PAGESIZE.equals(id))
			gridComp.setPageSize((String)value);
//		else if(PROP_SIMPLEPAGEBAR.equals(id)){
//			boolean truevalue = false;
//			if((Integer)value == 0)
//				truevalue = true;
//			gridComp.setSimplePageBar(truevalue);
//		}
		else if(PROP_SORTALBE.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setSortable(truevalue);
		}
		else if(PROP_PAGENATIONTOP.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setPagenationTop(truevalue);
		} else if (PROP_SHOWCOLINFO.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setShowColInfo(truevalue);
		}else if(PROP_GROUPCOLUMNS.equals(id))
			gridComp.setGroupColumns((String)value);
		else if(PROP_SHOWHEADER.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setShowHeader(truevalue);
		}
		else if(PROP_EXPANDTREE.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setExpandTree(truevalue);
		}
		else if(PROP_SHOWFORM.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			gridComp.setShowForm(truevalue);
		}
		else if(PROP_ROWRENDER.equals(id))
			gridComp.setRowRender((String)value);
		else if(PROP_EXTENDCELLEDITOR.equals(id))
			gridComp.setExtendCellEditor((String)value);
		else if(PROP_CAPTION.equals(id)){
			String oldValue = gridComp.getCaption();
			if((oldValue == null && value != null)  || (oldValue != null && value != null && !oldValue.equals(value))){
				gridComp.setCaption((String)value);
				GridEditor.getActiveEditor().refreshTreeItemText(gridComp);
			}
		}
		else
			super.setPropertyValue(id, value);
	}
	public Object getPropertyValue(Object id) {
		if(PROP_DATASET.equals(id))
			return gridComp.getDataset() == null?"":gridComp.getDataset(); //$NON-NLS-1$
		else if(PROP_EDITABLE.equals(id))
			return (gridComp.isEditable() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_MULTISELECT.equals(id))
			return (gridComp.isMultiSelect() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_ROWHEIGHT.equals(id))
			return gridComp.getRowHeight() == null?"":gridComp.getRowHeight(); //$NON-NLS-1$
		else if(PROP_HEADROWHEIGHT.equals(id))
			return gridComp.getHeaderRowHeight() == null?"":gridComp.getHeaderRowHeight(); //$NON-NLS-1$
		else if(PROP_SHOWNMUCOL.equals(id))
			return (gridComp.isShowNumCol() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_SHOWSUMROW.equals(id))
			return (gridComp.isShowSumRow() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_PAGESIZE.equals(id))
			return gridComp.getPageSize() == null?"":gridComp.getPageSize(); //$NON-NLS-1$
//		else if(PROP_SIMPLEPAGEBAR.equals(id))
//			return (gridComp.isSimplePageBar() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_SORTALBE.equals(id))
			return (gridComp.isSortable() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_PAGENATIONTOP.equals(id))
			return gridComp.isPagenationTop() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_SHOWCOLINFO.equals(id))
			return gridComp.isShowColInfo() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_GROUPCOLUMNS.equals(id))
			return gridComp.getGroupColumns() == null?"":gridComp.getGroupColumns(); //$NON-NLS-1$
		else if(PROP_SHOWHEADER.equals(id))
			return gridComp.isShowHeader() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_EXPANDTREE.equals(id))return gridComp.isExpandTree() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_SHOWFORM.equals(id))return gridComp.isShowForm() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_ROWRENDER.equals(id))
				return gridComp.getRowRender() == null?"":gridComp.getRowRender(); //$NON-NLS-1$
		else if(PROP_EXTENDCELLEDITOR.equals(id))
				return gridComp.getExtendCellEditor() == null?"":gridComp.getExtendCellEditor(); //$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return gridComp.getCaption() == null?"":gridComp.getCaption(); //$NON-NLS-1$
		else return super.getPropertyValue(id);
	}

	public WebElement getWebElement() {
		return gridComp;
	}

	
;
}
