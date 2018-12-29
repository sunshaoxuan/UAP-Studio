package nc.lfw.editor.common;

import java.util.ArrayList;
import java.util.Arrays;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_lfw_core;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


public abstract  class LFWWebComponentObj extends LfwElementObjWithGraph {
	public WebComponent getWebComp() {
		return webComp;
	}

	public void setWebComp(WebComponent webComp) {
		this.webComp = webComp;
	}
	private static final long serialVersionUID = 6253081418703115641L;

	private WebComponent webComp;
	
	public static final String PROP_ID = "element_ID"; //$NON-NLS-1$
//	public static final String PROP_WIDTH="element_width";
//	public static final String PROP_HEIGHT="element_height";
//	public static final String PROP_TOP="element_top";
//	public static final String PROP_LEFT="element_left";
	public static final String PROP_VISIBLE = "element_visible"; //$NON-NLS-1$
	public static final String PROP_ENABLED = "element_enabled"; //$NON-NLS-1$
//	public static final String PROP_POSITION = "element_relative";
	public static final String PROP_CONTEXTMENU = "element_contextMenu"; //$NON-NLS-1$
//	public static final String PROP_CALSSNAME = "element_className";
		
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[4];
		pds[0] = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		pds[0].setCategory(M_lfw_core.LFWWebComponentObj_0);
//		pds[1] = new TextPropertyDescriptor(PROP_WIDTH,"宽度");
//		pds[1].setCategory("基本");
//		pds[2] = new TextPropertyDescriptor(PROP_HEIGHT,"高度");
//		pds[2].setCategory("基本");
//		pds[3] = new TextPropertyDescriptor(PROP_TOP,"顶层距");
//		pds[3].setCategory("基本");
//		pds[4] = new TextPropertyDescriptor(PROP_LEFT,"左边距");
//		pds[4].setCategory("基本");
		pds[1] = new ComboBoxPropertyDescriptor(PROP_VISIBLE,M_lfw_core.LFWWebComponentObj_1, Constant.ISLAZY);
		pds[1].setCategory(M_lfw_core.LFWWebComponentObj_0);
		pds[2] = new ComboBoxPropertyDescriptor(PROP_ENABLED,M_lfw_core.LFWWebComponentObj_2, Constant.ISLAZY);
		pds[2].setCategory(M_lfw_core.LFWWebComponentObj_0);
		
//		pds[3] = new TextPropertyDescriptor(PROP_POSITION,"位置");
//		pds[3].setCategory("基本");
		pds[3] = new TextPropertyDescriptor(PROP_CONTEXTMENU,M_lfw_core.LFWWebComponentObj_3);
		pds[3].setCategory(M_lfw_core.LFWWebComponentObj_0);
//		pds[5] = new TextPropertyDescriptor(PROP_CALSSNAME,"自定义主题");
//		pds[5].setCategory("基本");
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		WebElement webele = getWebElement();
		if(!canChange(webele))
			return;
		WebComponent webComp =(WebComponent) webele;
		if(PROP_ID.equals(id)){
			webComp.setId((String)value);
			LFWBaseEditor.getActiveEditor().refreshTreeItem(webComp);
		}
//		else if(PROP_WIDTH.equals(id)){
//			webComp.setWidth((String)value);
//		}
//		else if(PROP_HEIGHT.equals(id)){
//			webComp.setHeight((String)value);		
//		}
//		else if(PROP_TOP.equals(id)){
//			webComp.setTop((String)value);
//		}
//		else if(PROP_LEFT.equals(id)){
//			webComp.setLeft((String)value);
//		}
		else if(PROP_VISIBLE.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			webComp.setVisible(truevalue);
		}
		else if(PROP_ENABLED.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			webComp.setEnabled(truevalue);
		}
//		else if(PROP_POSITION.equals(id)){
//			webComp.setPosition((String)value);
//		}
		else if(PROP_CONTEXTMENU.equals(id)){
			webComp.setContextMenu((String)value);		
		}
//		else if(PROP_CALSSNAME.equals(id)){
//			webComp.setClassName((String)value);
//		}
	}
	public Object getPropertyValue(Object id) {
		WebElement webele = getWebElement();
		WebComponent webComp =(WebComponent) webele;
		if(PROP_ID.equals(id))
			return webComp.getId() == null?"":webComp.getId(); //$NON-NLS-1$
//		else if(PROP_WIDTH.equals(id))
//			return webComp.getWidth() == null?"":webComp.getWidth();
//		else if(PROP_HEIGHT.equals(id))
//			return webComp.getHeight() == null?"":webComp.getHeight();
//		else if(PROP_TOP.equals(id))
//			return webComp.getTop() == null?"":webComp.getTop();
//		else if(PROP_LEFT.equals(id))
//			return webComp.getLeft() == null?"":webComp.getLeft();
		else if(PROP_VISIBLE.equals(id))
			return webComp.isVisible() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_ENABLED.equals(id))
			return webComp.isEnabled() == true? Integer.valueOf(0):Integer.valueOf(1);
//		else if(PROP_POSITION.equals(id))
//			return webComp.getPosition() == null?"":webComp.getPosition();
		else if(PROP_CONTEXTMENU.equals(id))
			return webComp.getContextMenu() == null?"":webComp.getContextMenu(); //$NON-NLS-1$
//		else if(PROP_CALSSNAME.equals(id))
//			return webComp.getClassName() == null?"":webComp.getClassName();
		return null;
	}

	
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return webComp;
	}
	
}
