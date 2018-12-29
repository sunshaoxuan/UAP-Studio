package nc.uap.lfw.linkcomp;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Á´½Ómodel
 * @author zhangxya
 *
 */
public class LinkCompElementObj extends LFWWebComponentObj{

	private static final long serialVersionUID = 6253081418703115641L;

	public static final String PROP_LINKCOMP_ELEMENT ="linkcomp_element"; //$NON-NLS-1$
	public static final String PROP_TEXT = "element_text"; //$NON-NLS-1$
	public static final String PROP_I18NNAME = "element_i18nname"; //$NON-NLS-1$
	public static final String PROP_HREF = "element_href"; //$NON-NLS-1$
	public static final String PROP_HASIMAG = "element_hasimg"; //$NON-NLS-1$
	public static final String PROP_IMAGE = "element_image"; //$NON-NLS-1$
	public static final String PROP_TARGET = "element_target"; //$NON-NLS-1$
	
	private LinkComp linkComp;
	
	public LinkComp getLinkComp() {
		return linkComp;
	}
	
	public void setLinkComp(LinkComp linkComp) {
		this.linkComp = linkComp;
		fireStructureChange(PROP_LINKCOMP_ELEMENT, linkComp);
	}


	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[6];
		pds[0] = new TextPropertyDescriptor(PROP_TEXT, M_listview.LinkCompElementObj_0);
		pds[0].setCategory(M_listview.LinkCompElementObj_1);
		pds[1] = new TextPropertyDescriptor(PROP_I18NNAME, M_listview.LinkCompElementObj_2);
		pds[1].setCategory(M_listview.LinkCompElementObj_1);
		pds[2] = new TextPropertyDescriptor(PROP_HREF, M_listview.LinkCompElementObj_3);
		pds[2].setCategory(M_listview.LinkCompElementObj_1);
		pds[3] = new ComboBoxPropertyDescriptor(PROP_HASIMAG, M_listview.LinkCompElementObj_4, Constant.ISLAZY);
		pds[3].setCategory(M_listview.LinkCompElementObj_1);
		pds[4] = new TextPropertyDescriptor(PROP_IMAGE, M_listview.LinkCompElementObj_5);
		pds[4].setCategory(M_listview.LinkCompElementObj_1);
		pds[5] = new TextPropertyDescriptor(PROP_TARGET, M_listview.LinkCompElementObj_6);
		pds[5].setCategory(M_listview.LinkCompElementObj_1);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_TEXT.equals(id))
			linkComp.setText((String)value);
		else if(PROP_I18NNAME.equals(id))
			linkComp.setI18nName((String)value);
		else if(PROP_HREF.equals(id))
			linkComp.setHref((String)value);
		else if(PROP_HASIMAG.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			linkComp.setHasImg(truevalue);
		}
		else if(PROP_IMAGE.equals(id))
			linkComp.setImage((String)value);
		else if(PROP_TARGET.equals(id))
			linkComp.setTarget((String)value);
		else 
			super.setPropertyValue(id, value);
	}
	
	public Object getPropertyValue(Object id) {
		if(PROP_TEXT.equals(id))
			return linkComp.getText() == null?"":linkComp.getText(); //$NON-NLS-1$
		else if(PROP_I18NNAME.equals(id))
			return linkComp.getI18nName() == null?"":linkComp.getI18nName(); //$NON-NLS-1$
		else if(PROP_HREF.equals(id))
			return linkComp.getHref() == null?"":linkComp.getHref(); //$NON-NLS-1$
		else if(PROP_HASIMAG.equals(id))
			return linkComp.isHasImg() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_IMAGE.equals(id))
			return linkComp.getImage() == null?"":linkComp.getImage(); //$NON-NLS-1$
		else if(PROP_TARGET.equals(id))
			return linkComp.getTarget() == null?"":linkComp.getTarget(); //$NON-NLS-1$
		return super.getPropertyValue(id);
	}

	public WebElement getWebElement() {
		return linkComp;
	}

}
