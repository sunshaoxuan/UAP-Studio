package nc.uap.lfw.image;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_iframe;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Í¼ÐÎ±à¼­¿Ø¼þ
 * @author zhangxya
 *
 */
public class ImageElementObj extends LFWWebComponentObj{
	
	private static final long serialVersionUID = 6253081418703115641L;
	
	public static final String PROP_IMAGE_ELEMENT ="image_element"; //$NON-NLS-1$
	public static final String PROP_IMAGE1 = "element_IMAGE1"; //$NON-NLS-1$
	public static final String PROP_IMAGE2 = "element_IMAGE2"; //$NON-NLS-1$
	public static final String PROP_ALT = "element_alt"; //$NON-NLS-1$
	public static final String PROP_IMATGEINACT = "element_imageInact"; //$NON-NLS-1$
	public static final String PROP_FLOATRIGHT = "element_floatRight"; //$NON-NLS-1$
	public static final String PROP_FLOATLEFT= "element_floatLeft"; //$NON-NLS-1$
	
	private ImageComp imageComp;
	
	public ImageComp getImageComp() {
		return imageComp;
	}
	
	public void setImageComp(ImageComp imageComp) {
		this.imageComp = imageComp;
		fireStructureChange(PROP_IMAGE_ELEMENT, imageComp);
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[6];
	
		pds[0] = new TextPropertyDescriptor(PROP_IMAGE1, M_iframe.ImageElementObj_0);
		pds[0].setCategory(M_iframe.ImageElementObj_1);
		pds[1] = new TextPropertyDescriptor(PROP_IMAGE2,M_iframe.ImageElementObj_2);
		pds[1].setCategory(M_iframe.ImageElementObj_1);
		pds[2] = new TextPropertyDescriptor(PROP_ALT,M_iframe.ImageElementObj_3);
		pds[2].setCategory(M_iframe.ImageElementObj_1);
		pds[3] = new TextPropertyDescriptor(PROP_IMATGEINACT,M_iframe.ImageElementObj_4);
		pds[3].setCategory(M_iframe.ImageElementObj_1);
		pds[4] = new ComboBoxPropertyDescriptor(PROP_FLOATRIGHT,M_iframe.ImageElementObj_5, Constant.ISLAZY);
		pds[4].setCategory(M_iframe.ImageElementObj_1);
		pds[5] = new ComboBoxPropertyDescriptor(PROP_FLOATLEFT,M_iframe.ImageElementObj_6, Constant.ISLAZY);
		pds[5].setCategory(M_iframe.ImageElementObj_1);
	
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_IMAGE1.equals(id))
			imageComp.setImage1((String)value);
		if(PROP_IMAGE2.equals(id))
			imageComp.setImage2((String)value);
		else if(PROP_ALT.equals(id)){
			imageComp.setAlt((String)value);
		}
		else if(PROP_IMATGEINACT.equals(id)){
			imageComp.setImageInact((String)value);		
		}
		else if(PROP_FLOATRIGHT.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			imageComp.setFloatRight(truevalue);
		}
		else if(PROP_FLOATLEFT.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			imageComp.setFloatLeft(truevalue);
		}
	}
	public Object getPropertyValue(Object id) {
		if(PROP_IMAGE1.equals(id))
			return imageComp.getImage1() == null?"":imageComp.getImage1(); //$NON-NLS-1$
		else if(PROP_IMAGE2.equals(id))
			return imageComp.getImage2() == null?"":imageComp.getImage2(); //$NON-NLS-1$
		else if(PROP_ALT.equals(id))
			return imageComp.getAlt() == null?"":imageComp.getAlt(); //$NON-NLS-1$
		else if(PROP_IMATGEINACT.equals(id))
			return imageComp.getImageInact() == null?"":imageComp.getImageInact(); //$NON-NLS-1$
		else if(PROP_FLOATRIGHT.equals(id))
			return imageComp.isFloatRight() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_FLOATLEFT.equals(id))
			return imageComp.isFloatLeft() == true? Integer.valueOf(0):Integer.valueOf(1);
		else return super.getPropertyValue(id); 
	}
	
	public WebElement getWebElement() {
		return imageComp;
	}
}
