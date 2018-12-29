package nc.uap.lfw.perspective.model;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.editor.DataSetEditor;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;



/**
 * 设置FieldRelation时的引用ds
 * @author zhangxya
 *
 */
public class RefDatasetElementObj extends LfwElementObjWithGraph{
	
	private static final long serialVersionUID = 1L;
	private DatasetElementObj dsobj = null;
	private GridElementObj gridobj = null;
	public GridElementObj getGridobj() {
		return gridobj;
	}
	public void setGridobj(GridElementObj gridobj) {
		this.gridobj = gridobj;
	}
	
	private FieldRelation refFieldRelation ;
	public FieldRelation getRefFieldRelation() {
		return refFieldRelation;
	}
	public void setRefFieldRelation(FieldRelation refFieldRelation) {
		this.refFieldRelation = refFieldRelation;
	}
	
	public static final String PROP_FIELDRELATION_PROPS = "add_reffieldrelation_props"; //$NON-NLS-1$
	public void addFieldRelation(FieldRelation fr){
		fireStructureChange(PROP_FIELDRELATION_PROPS, fr);
	}
	
	public static final String PROP_DELETE_FIELDRELATION_PROPS = "delete_reffieldrelation_props"; //$NON-NLS-1$
	public void deleteFieldRelation(FieldRelation fr){
		fireStructureChange(PROP_DELETE_FIELDRELATION_PROPS, fr);
	}
	
	private RefDatasetElementObj parent = null;
	
	public RefDatasetElementObj getParent() {
		return parent;
	}
	public void setParent(RefDatasetElementObj parent) {
		this.parent = parent;
	}
	public DatasetElementObj getDsobj() {
		return dsobj;
	}
	public void setDsobj(DatasetElementObj dsobj) {
		this.dsobj = dsobj;
	}

	public static final String REFDS_CHILD_ADD = "refds_child_add"; //$NON-NLS-1$
	private ArrayList<RefDatasetElementObj> children = new ArrayList<RefDatasetElementObj>();
	
	public ArrayList<RefDatasetElementObj> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<RefDatasetElementObj> children) {
		this.children = children;
	}
	public boolean addChild(RefDatasetElementObj cell) {
		cell.setParent(this);
		boolean b = children.add(cell);
		if (b) {
			fireStructureChange(REFDS_CHILD_ADD, cell);
		}
		return b;
	}
	
	public static final String REFDS_CHILD_REMOVE = "refds_child_remove"; //$NON-NLS-1$
	
	public void removeChild(RefDatasetElementObj cell){
		children.remove(cell);
		fireStructureChange(REFDS_CHILD_REMOVE, cell);
	}
	

	private DatasetElementObj dsEle = null;
	public static final String PROP_CELL_LOCATION = "cell_location"; //$NON-NLS-1$

	public DatasetElementObj getDsEle() {
		return dsEle;
	}

	public void setDsEle(DatasetElementObj dsEle) {
		this.dsEle = dsEle;
	}

	private Dataset ds;

	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}
	
	private String islazy;
	private String refdatasetid;
	
	public String getIslazy() {
		return islazy;
	}

	public void setIslazy(String islazy) {
		this.islazy = islazy;
	}

	public String getRefdatasetid() {
		return refdatasetid;
	}

	public void setRefdatasetid(String refdatasetid) {
		this.refdatasetid = refdatasetid;
	}

	public static final String PROP_ISLAZY = "element_ISLAZY"; //$NON-NLS-1$
	public static final String PROP_NEEDPROCESS = "element_NEEDPROCESS"; //$NON-NLS-1$
	//public static final String PROP_REFDATASETID = "element_REFDATASETID";
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[2];
		PropertyDescriptor pd1 = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		pd1.setCategory(M_perspective.RefDatasetElementObj_0);
		al.add(pd1);
		if(DataSetEditor.getActiveEditor()!=null){
			PropertyDescriptor pd2 = new ComboBoxPropertyDescriptor(PROP_NEEDPROCESS,"needProcess", Constant.ISLAZY); //$NON-NLS-1$
			pd2.setCategory(M_perspective.RefDatasetElementObj_0);
			al.add(pd2);
		}
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		if(PROP_ID.equals(id))
			setIslazy((String)value);
		else if(PROP_NEEDPROCESS.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			refFieldRelation.setNeedProcess(truevalue);
		}
	}
	public Object getPropertyValue(Object id) {
		if(PROP_ID.equals(id)){
			return ds.getId() == null?"":ds.getId(); //$NON-NLS-1$
		}else if(PROP_NEEDPROCESS.equals(id)&&refFieldRelation!=null)
			return refFieldRelation.isNeedProcess() == true? Integer.valueOf(0):Integer.valueOf(1);
		return null;
	}
		
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return ds;
	}
	
}
