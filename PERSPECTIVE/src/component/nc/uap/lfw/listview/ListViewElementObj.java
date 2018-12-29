package nc.uap.lfw.listview;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.listview.core.ListViewEditor;
import nc.uap.lfw.perspective.model.Constant;

/**
 * listview model
 * @author zhangxx4
 */

public class ListViewElementObj extends LFWWebComponentObj{
	private static final long serialVersionUID = 9186533063040178127L;
	
	public static final String PROP_LISTVIEW_ELEMENT ="listview_element"; //$NON-NLS-1$
	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; //$NON-NLS-1$
	public static final String PROP_DATASET = "element_DATASET"; //$NON-NLS-1$
	public static final String PROP_WIDTH = "element_WIDTH"; //$NON-NLS-1$
	public static final String PROP_ROWWIDTH = "element_ROWWIDTH"; //$NON-NLS-1$
	public static final String PROP_ROWHEIGHT = "element_ROWHEIGHT"; //$NON-NLS-1$
	public static final String PROP_FLOATTYPE = "element_FLOATTYPE"; //$NON-NLS-1$
	public static final String PROP_SHOWNMUCOL = "element_showNumCol"; //$NON-NLS-1$
	public static final String PROP_PAGESIZE = "element_pageSize"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	public static final String PROP_RENDERTYPE = "element_RENDERTYPE"; //$NON-NLS-1$
	private ListViewComp listviewComp;
	private Dataset ds;
	
	public ListViewComp getlistviewComp() {
		return listviewComp;
	}
	
	public void setlistviewComp(ListViewComp listviewComp) {
		this.listviewComp = listviewComp;
		fireStructureChange(PROP_LISTVIEW_ELEMENT,  listviewComp);
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
		PropertyDescriptor[] pds = new PropertyDescriptor[8];
		pds[0] = new TextPropertyDescriptor(PROP_WIDTH,M_listview.ListViewElementObj_0);
		pds[0].setCategory(M_listview.ListViewElementObj_1);
		pds[1] = new NoEditableTextPropertyDescriptor(PROP_DATASET,M_listview.ListViewElementObj_2);
		pds[1].setCategory(M_listview.ListViewElementObj_1);
		pds[2] = new TextPropertyDescriptor(PROP_ROWWIDTH,M_listview.ListViewElementObj_3);
		pds[2].setCategory(M_listview.ListViewElementObj_1);
		pds[3] = new TextPropertyDescriptor(PROP_ROWHEIGHT, M_listview.ListViewElementObj_4);
		pds[3].setCategory(M_listview.ListViewElementObj_1);
		pds[4] = new ComboBoxPropertyDescriptor(PROP_FLOATTYPE,M_listview.ListViewElementObj_6, Constant.TEXTALIGN);
		pds[4].setCategory(M_listview.ListViewElementObj_1);
		pds[5] = new TextPropertyDescriptor(PROP_PAGESIZE, M_listview.ListViewElementObj_8);
		pds[5].setCategory(M_listview.ListViewElementObj_1);
		pds[6] = new TextPropertyDescriptor(PROP_RENDERTYPE,M_listview.ListViewElementObj_15);
		pds[6].setCategory(M_listview.ListViewElementObj_1);
		//caption
		pds[7] = new TextPropertyDescriptor(PROP_CAPTION,M_listview.ListViewElementObj_17);
		pds[7].setCategory(M_listview.ListViewElementObj_18);
		
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_DATASET.equals(id)){
			listviewComp.setDataset((String)value);
			fireStructureChange(PROP_LISTVIEW_ELEMENT,  listviewComp);
		}
		else if(PROP_WIDTH.equals(id))
			listviewComp.setWidth((String)value);
		else if(PROP_ROWWIDTH.equals(id))
			listviewComp.setRowWidth((String)value);
		else if(PROP_ROWHEIGHT.equals(id))
			listviewComp.setRowHeight((String)value);
		else if(PROP_FLOATTYPE.equals(id)){
			String truevalue = "center";
			if((Integer)value == 0)
				truevalue = "center";
			if((Integer)value == 1)
				truevalue = "left";
			if((Integer)value == 2)
				truevalue = "right";
			listviewComp.setFloatType(truevalue);
		}
		else if(PROP_PAGESIZE.equals(id))
			listviewComp.setPageSize((String)value);
		else if(PROP_RENDERTYPE.equals(id))
			listviewComp.setRenderType((String)value);
		else if(PROP_CAPTION.equals(id)){
			String oldValue = listviewComp.getCaption();
			if((oldValue == null && value != null)  || (oldValue != null && value != null && !oldValue.equals(value))){
				listviewComp.setCaption((String)value);
				ListViewEditor.getActiveEditor().refreshTreeItemText(listviewComp);
			}
		}
		else
			super.setPropertyValue(id, value);
	}
	public Object getPropertyValue(Object id) {
		if(PROP_DATASET.equals(id))
			return listviewComp.getDataset() == null?"":listviewComp.getDataset(); //$NON-NLS-1$
		else if(PROP_WIDTH.equals(id))
			return listviewComp.getWidth() == null?"":listviewComp.getWidth(); //$NON-NLS-1$
		else if(PROP_ROWWIDTH.equals(id))
			return listviewComp.getRowWidth() == null?"":listviewComp.getRowWidth(); //$NON-NLS-1$
		else if(PROP_ROWHEIGHT.equals(id))
			return listviewComp.getRowHeight() == null?"":listviewComp.getRowHeight(); //$NON-NLS-1$
		else if(PROP_FLOATTYPE.equals(id)){
			if(listviewComp.getFloatType() != null){
				for(int i=0;i<Constant.TEXTALIGN.length;i++){
					if(Constant.TEXTALIGN[i].equals(listviewComp.getFloatType())){
						return Integer.valueOf(i);
					}
				}
			}
			return Integer.valueOf(0);
		}
		else if(PROP_PAGESIZE.equals(id))
			return listviewComp.getPageSize() == null?"":listviewComp.getPageSize(); //$NON-NLS-1$
		else if(PROP_RENDERTYPE.equals(id))
				return listviewComp.getRenderType() == null?"":listviewComp.getRenderType();//$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return listviewComp.getCaption() == null?"":listviewComp.getCaption(); //$NON-NLS-1$
		else return super.getPropertyValue(id);
	}

	public WebElement getWebElement() {
		return listviewComp;
	}
}
