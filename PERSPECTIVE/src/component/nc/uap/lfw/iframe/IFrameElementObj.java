package nc.uap.lfw.iframe;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * IFrame控件
 * @author zhangxya
 *
 */
public class IFrameElementObj extends LFWWebComponentObj{

	private static final long serialVersionUID = 6253081418703115641L;
	private IFrameComp iframecomp;
	public static final String PROP_IFRAME_ELEMENT ="iframe_element"; //$NON-NLS-1$
	public static final String PROP_SRC = "element_src"; //$NON-NLS-1$
	public static final String PROP_NAME = "element_name"; //$NON-NLS-1$
	public static final String PROP_BORDER = "element_border"; //$NON-NLS-1$
	public static final String PROP_FARMEBORDER = "element_farmeborder"; //$NON-NLS-1$
	public static final String PROP_SCROLLING = "element_scrolling"; //$NON-NLS-1$
	
	public IFrameComp getIframecomp() {
		return iframecomp;
	}
	
	public void setIframecomp(IFrameComp iframecomp) {
		this.iframecomp = iframecomp;
		fireStructureChange(PROP_IFRAME_ELEMENT, iframecomp);
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[4];
		pds[0] = new TextPropertyDescriptor(PROP_SRC, M_form.IFrameElementObj_0);
		pds[0].setCategory(M_form.IFrameElementObj_1);
		pds[1] = new TextPropertyDescriptor(PROP_NAME, M_form.IFrameElementObj_2);
		pds[1].setCategory(M_form.IFrameElementObj_1);
		//pds[2] = new ObjectComboPropertyDescriptor(PROP_FARMEBORDER,"边框是否三维显示", Constant.ISFARMEBORDER);
		pds[2] = new TextPropertyDescriptor(PROP_FARMEBORDER, M_form.IFrameElementObj_3);
		pds[2].setCategory(M_form.IFrameElementObj_1);
		pds[3] = new ObjectComboPropertyDescriptor(PROP_SCROLLING,M_form.IFrameElementObj_4,Constant.SCROLLING);
		pds[3].setCategory(M_form.IFrameElementObj_1);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_SRC.equals(id))
			iframecomp.setSrc((String)value);
		if(PROP_NAME.equals(id))
			iframecomp.setName((String)value);
		else if(PROP_BORDER.equals(id)){
			iframecomp.setBorder((String)value);
		}
		else if(PROP_FARMEBORDER.equals(id)){
			iframecomp.setFrameBorder((String)value);
		}
		else if(PROP_SCROLLING.equals(id)){
			if(value == null)
				return;
			if(value.equals(M_form.IFrameElementObj_5))
				iframecomp.setScrolling("yes"); //$NON-NLS-1$
			else if(value.equals(M_form.IFrameElementObj_6))
				iframecomp.setScrolling("no"); //$NON-NLS-1$
			else iframecomp.setScrolling("auto"); //$NON-NLS-1$
		}
	}
	public Object getPropertyValue(Object id) {
		if(PROP_SRC.equals(id))
			return iframecomp.getSrc() == null?"":iframecomp.getSrc(); //$NON-NLS-1$
		else if(PROP_NAME.equals(id))
			return iframecomp.getName() == null?"":iframecomp.getName(); //$NON-NLS-1$
		else if(PROP_BORDER.equals(id))
			return iframecomp.getBorder() == null?"":iframecomp.getBorder(); //$NON-NLS-1$
		else if(PROP_FARMEBORDER.equals(id))
			return iframecomp.getFrameBorder() == null? Constant.ISFARMEBORDER[0]:iframecomp.getFrameBorder();
		else if(PROP_SCROLLING.equals(id))
			return iframecomp.getScrolling() == null? Constant.SCROLLING[0]:iframecomp.getScrolling();
		else return super.getPropertyValue(id);
	}

	public WebElement getWebElement() {
		return iframecomp;
	}

}
