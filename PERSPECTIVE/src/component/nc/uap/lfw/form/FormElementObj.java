package nc.uap.lfw.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * form 的model
 * 
 * @author zhangxya
 * 
 */
public class FormElementObj extends LFWWebComponentObj {
	private static final long serialVersionUID = 6253081418703115641L;

	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; //$NON-NLS-1$
	public static final String PROP_FORM_ELEMENT = "form_element"; //$NON-NLS-1$
	public static final String PROP_DATASET = "element_DATASET"; //$NON-NLS-1$
	public static final String PROP_WITHFORM = "element_withForm"; //$NON-NLS-1$
	public static final String PROP_ROWHEIGHT = "element_rowHeight"; //$NON-NLS-1$
	public static final String PROP_BGCOLOR = "element_backgroundColor"; //$NON-NLS-1$
	public static final String PROP_RENDERHIDDENELE = "element_renderHiddenEle"; //$NON-NLS-1$
	// public static final String PROP_POSITION = "element_POSTTION";
	public static final String PROP_COLUMNCOUNT = "element_COLUMNCOUNT"; //$NON-NLS-1$
	public static final String PROP_RENDERTYPE = "element_RENDERTYPE"; //$NON-NLS-1$
	public static final String Form_ADD_CELL_PROPS = "add_form_props"; //$NON-NLS-1$
	public static final String PROP_ELEWIDTH = "element_elewidth"; //$NON-NLS-1$
	public static final String PROP_LABELMINWIDTH = "element_labelMinWidth"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	public static final String PROP_FORMRENDER = "element_FORMRENDER"; //$NON-NLS-1$
	
	private List<FormElement> props = new ArrayList<FormElement>();
	private FormComp formComp;
	private Dataset ds;
	private String[] renderType = { M_form.FormElementObj_0, M_form.FormElementObj_1, M_form.FormElementObj_2, M_form.FormElementObj_3 };

	public FormComp getFormComp() {
		return formComp;
	}

	public void setFormComp(FormComp formComp) {
		this.formComp = formComp;
		fireStructureChange(PROP_FORM_ELEMENT, formComp);
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
		PropertyDescriptor a1 = new NoEditableTextPropertyDescriptor(
				PROP_DATASET, M_form.FormElementObj_4);
		a1.setCategory(M_form.FormElementObj_5);
		al.add(a1);
		// PropertyDescriptor a2 = new TextPropertyDescriptor(PROP_POSITION,
		// "位置");
		// a2.setCategory("高级");
		// al.add(a2);
		if (formComp.getRenderType() == 1 || formComp.getRenderType() == 3) {
			PropertyDescriptor a3 = new TextPropertyDescriptor(
					PROP_COLUMNCOUNT, M_form.FormElementObj_6);
			a3.setCategory(M_form.FormElementObj_5);
			al.add(a3);
		}
		PropertyDescriptor a4 = new ObjectComboPropertyDescriptor(
				PROP_RENDERTYPE, M_form.FormElementObj_7, renderType);
		a4.setCategory(M_form.FormElementObj_5);
		al.add(a4);
		PropertyDescriptor a5 = new TextPropertyDescriptor(PROP_ROWHEIGHT,
				M_form.FormElementObj_8);
		a5.setCategory(M_form.FormElementObj_5);
		al.add(a5);
		PropertyDescriptor a6 = new ComboBoxPropertyDescriptor(PROP_WITHFORM,
				M_form.FormElementObj_9, Constant.ISLAZY);
		a6.setCategory(M_form.FormElementObj_5);
		al.add(a6);
		PropertyDescriptor a7 = new TextPropertyDescriptor(PROP_BGCOLOR, M_form.FormElementObj_10);
		a7.setCategory(M_form.FormElementObj_5);
		al.add(a7);
//		PropertyDescriptor a8 = new ComboBoxPropertyDescriptor(
//				PROP_RENDERHIDDENELE, "是否渲染隐藏的表单元素", Constant.ISLAZY);
//		a8.setCategory("高级");
//		al.add(a8);
		PropertyDescriptor a9 = new TextPropertyDescriptor(PROP_ELEWIDTH, M_form.FormElementObj_11);
		a9.setCategory(M_form.FormElementObj_5);
		al.add(a9);

		PropertyDescriptor a10 = new TextPropertyDescriptor(PROP_LABELMINWIDTH,
				M_form.FormElementObj_12);
		a10.setCategory(M_form.FormElementObj_5);
		al.add(a10);
		
		PropertyDescriptor a11 = new TextPropertyDescriptor(PROP_FORMRENDER,
		M_form.FormElementObj_13);
		a11.setCategory(M_form.FormElementObj_5);
		al.add(a11);		

		TextPropertyDescriptor captionDes = new TextPropertyDescriptor(
				PROP_CAPTION, M_form.FormElementObj_14);
		captionDes.setCategory(M_form.FormElementObj_15);
		al.add(captionDes);

		return al.toArray(new IPropertyDescriptor[0]);
	}

	public void addProp(FormElement prop) {
		props.add(prop);
		fireStructureChange(Form_ADD_CELL_PROPS, prop);
	}

	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if (PROP_DATASET.equals(id)) {
			formComp.setDataset((String) value);
			fireStructureChange(PROP_FORM_ELEMENT, formComp);
		}
		// else if(PROP_POSITION.equals(id))
		// formComp.setPosition((String)value);
		else if (PROP_COLUMNCOUNT.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			formComp.setColumnCount(intVal);
		}
		else if (PROP_RENDERTYPE.equals(id)) {
			if (value.equals(renderType[0]))
				formComp.setRenderType(1);
			else if (value.equals(renderType[1]))
				formComp.setRenderType(2);
			else if (value.equals(renderType[2]))
				formComp.setRenderType(3);
			else if (value.equals(renderType[3]))
				formComp.setRenderType(5);
		} else if (PROP_CAPTION.equals(id)) {
			String oldValue = formComp.getCaption();
			if ((oldValue == null && value != null)
					|| (oldValue != null && value != null && !oldValue
							.equals(value))) {
				formComp.setCaption((String) value);
				FormEditor.getActiveEditor().refreshTreeItemText(formComp);
			}
		} else if (PROP_ROWHEIGHT.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			formComp.setRowHeight(intVal);
		}
		else if (PROP_WITHFORM.equals(id)) {
			boolean truevalue = false;
			if ((Integer) value == 0)
				truevalue = true;
			formComp.setWithForm(truevalue);
		} else if (PROP_BGCOLOR.equals(id))
			formComp.setBackgroundColor((String) value);
		else if(PROP_FORMRENDER.equals(id))formComp.setFormRender((String)value);
//		else if (PROP_RENDERHIDDENELE.equals(id)) {
//			boolean truevalue = false;
//			if ((Integer) value == 0)
//				truevalue = true;
//			formComp.setRenderHiddenEle(truevalue);
//		} 
		else if (PROP_ELEWIDTH.equals(id)) {
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			formComp.setEleWidth(intVal);
		} else if (PROP_LABELMINWIDTH.equals(id)) {
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			formComp.setLabelMinWidth(intVal);
		}
	}

	public Object getPropertyValue(Object id) {
		if (PROP_ID.equals(id))
			return formComp.getId() == null ? "" : formComp.getId(); //$NON-NLS-1$
		else if (PROP_DATASET.equals(id))
			return formComp.getDataset() == null ? "" : formComp.getDataset(); //$NON-NLS-1$
		// else if(PROP_POSITION.equals(id))
		// return formComp.getPosition() == null?"":formComp.getPosition();
		else if (PROP_COLUMNCOUNT.equals(id))
			return formComp.getColumnCount() == null ? "" : formComp //$NON-NLS-1$
					.getColumnCount().toString();
		else if (PROP_RENDERTYPE.equals(id)) {
			Integer newvalue = Integer.valueOf(formComp.getRenderType());
			if (newvalue == 1)
				return renderType[0];
			else if (newvalue == 2)
				return renderType[1];
			else if (newvalue == 3)
				return renderType[2];
			else if (newvalue == 5)
				return renderType[3];
			return newvalue.toString();
		} else if (PROP_CAPTION.equals(id))
			return formComp.getCaption() == null ? "" : formComp.getCaption(); //$NON-NLS-1$
		// else if(PROP_HEIGHT.equals(id))
		// return formComp.getHeight()==null?"":formComp.getHeight();
		else if (PROP_ROWHEIGHT.equals(id))
			return String.valueOf(formComp.getRowHeight());
		else if (PROP_WITHFORM.equals(id))
			return formComp.isWithForm() == true ? Integer.valueOf(0):Integer.valueOf(1);
		else if (PROP_BGCOLOR.equals(id))
			return formComp.getBackgroundColor() == null ? "" : formComp //$NON-NLS-1$
					.getBackgroundColor();
		else if(PROP_FORMRENDER.equals(id))return formComp.getFormRender()==null?"":formComp.getFormRender(); //$NON-NLS-1$
//		else if (PROP_RENDERHIDDENELE.equals(id))
//			return formComp.isRenderHiddenEle() == true ? new Integer(0)
//					: new Integer(1);
		else if (PROP_ELEWIDTH.equals(id))
			return String.valueOf(formComp.getEleWidth());
		else if (PROP_LABELMINWIDTH.equals(id)) {
			return String.valueOf(formComp.getLabelMinWidth());
		} else
			return super.getPropertyValue(id);
	}

	public void firePropUpdate(FormElement prop) {
		fireStructureChange(PROP_UPDATE_CELL_PROPS, prop);
	}

	public WebElement getWebElement() {
		return formComp;
	}

	;
}
