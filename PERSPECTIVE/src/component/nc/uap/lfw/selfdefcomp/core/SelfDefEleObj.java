package nc.uap.lfw.selfdefcomp.core;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class SelfDefEleObj extends LFWWebComponentObj{

	private static final long serialVersionUID = 6253081418703115641L;
	private SelfDefComp selfDefComp;
	public static final String PROP_SELFDEFCOMP_ELEMENT ="selfdefcomp_element";
	
	public static final String PROP_VISIBLE = "element_VISIBLE";
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[1];
		pds[0] = new ComboBoxPropertyDescriptor(PROP_VISIBLE,"是否可见", Constant.ISLAZY);
		pds[0].setCategory("高级");
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_VISIBLE.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			selfDefComp.setVisible(truevalue);
		}
		else
			super.setPropertyValue(id, value);
		
	}
	public Object getPropertyValue(Object id) {
		if(PROP_VISIBLE.equals(id))
			return selfDefComp.isVisible() == true? Integer.valueOf(0):Integer.valueOf(1);
		else return super.getPropertyValue(id);
	}
	
	public WebElement getWebElement() {
		return selfDefComp;
	}

	
	public SelfDefComp getSelfDefComp() {
		return selfDefComp;
	}

	public void setSelfDefComp(SelfDefComp selfDefComp) {
		this.selfDefComp = selfDefComp;
		fireStructureChange(PROP_SELFDEFCOMP_ELEMENT, selfDefComp);
	}
}
