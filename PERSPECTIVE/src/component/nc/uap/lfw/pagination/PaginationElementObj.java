package nc.uap.lfw.pagination;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_listview;

public class PaginationElementObj extends LFWWebComponentObj{
	
	
	public static final String PROP_PAGINATION_ELEMENT ="pagination_element";  //$NON-NLS-1$
	public static final String PROP_DATASET = "element_DATASET"; //$NON-NLS-1$
	
	private PaginationComp paginationComp;;
	private Dataset ds;
	
	public PaginationComp getPaginationComp() {
		return paginationComp;
	}
	public void setPaginationComp(PaginationComp paginationComp) {
		this.paginationComp = paginationComp;
		fireStructureChange(PROP_PAGINATION_ELEMENT,  paginationComp);
	}
	public Dataset getDs() {
		return ds;
	}
	public void setDs(Dataset ds) {
		this.ds = ds;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[1];
		pds[0] = new NoEditableTextPropertyDescriptor(PROP_DATASET,M_listview.PaginationElementObj_0);
		pds[0].setCategory(M_listview.PaginationElementObj_1);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_DATASET.equals(id)){
			paginationComp.setDataset((String)value);
			fireStructureChange(PROP_PAGINATION_ELEMENT,  paginationComp);
		}
	}
	public Object getPropertyValue(Object id) {
		if(PROP_DATASET.equals(id))
			return paginationComp.getDataset() == null?"":paginationComp.getDataset(); //$NON-NLS-1$
		else return super.getPropertyValue(id);
	}
	public WebElement getWebElement() {
		return paginationComp;
	}

}
