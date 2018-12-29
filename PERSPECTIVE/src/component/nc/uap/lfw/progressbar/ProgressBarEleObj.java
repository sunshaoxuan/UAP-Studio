package nc.uap.lfw.progressbar;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ProgressBarEleObj extends LFWWebComponentObj{

	public static final String PROP_PROGRESSBAR_ELEMENT = "progressbar_element"; //$NON-NLS-1$
	public static final String PROP_VALUE = "element_VALUE"; //$NON-NLS-1$
	public static final String PROP_VALUEALIGN = "element_VALUEALIGN"; //$NON-NLS-1$

	private static final long serialVersionUID = 6253081418703115641L;
	private ProgressBarComp progressBar;
	
	
	public ProgressBarComp getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBarComp progressBar) {
		this.progressBar = progressBar;
		fireStructureChange(PROP_PROGRESSBAR_ELEMENT, progressBar);
	}

		
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[2];
		pds[0] = new TextPropertyDescriptor(PROP_VALUE, M_listview.ProgressBarEleObj_0);
		pds[0].setCategory(M_listview.ProgressBarEleObj_1);
		pds[1] = new ObjectComboPropertyDescriptor(PROP_VALUEALIGN,M_listview.ProgressBarEleObj_2, Constant.TEXTALIGN);
		pds[1].setCategory(M_listview.ProgressBarEleObj_1);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_VALUE.equals(id))
			progressBar.setValue((String)value);
		else if(PROP_VALUEALIGN.equals(id)){
			progressBar.setValueAlign((String)value);
		}
	}
	
	public Object getPropertyValue(Object id) {
		if(PROP_VALUE.equals(id))
			return progressBar.getValue() == null?"":progressBar.getValue(); //$NON-NLS-1$
		else if(PROP_VALUEALIGN.equals(id))
			return progressBar.getValueAlign() == null?"":progressBar.getValueAlign(); //$NON-NLS-1$
		else return super.getPropertyValue(id);
	}
	
	public WebElement getWebElement() {
		return progressBar;
	}
}
