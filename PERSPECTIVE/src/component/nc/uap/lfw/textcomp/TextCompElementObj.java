package nc.uap.lfw.textcomp;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.lang.M_textcomp;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * textcomp 的model
 * @author zhangxya
 *
 */
public class TextCompElementObj extends LFWWebComponentObj{

	public static final String PROP_TEXTCOMP_ELEMENT ="textcomp_element"; //$NON-NLS-1$
	private static final long serialVersionUID = 6253081418703115641L;
	private TextComp textComp;
	private ComboBoxComp comboboxcomp;
	private ReferenceComp referencecomp;
	private TextAreaComp textareaComp;
	
	public static final String PROP_VALUE = "element_VALUE"; //$NON-NLS-1$
	public static final String PROP_TYPE = "element_TYPE"; //$NON-NLS-1$
	public static final String PROP_MAXVALUE = "element_MAXVALUE"; //$NON-NLS-1$
	public static final String PROP_MINVALUE = "element_MINVALUE"; //$NON-NLS-1$
	public static final String PROP_PRECISION= "element_PRECISION"; //$NON-NLS-1$
	public static final String PROP_MAXLENGTH= "element_MAXLENGTH"; //$NON-NLS-1$
	
	//公用属性
	public static final String PROP_TEXT= "element_TEXT"; //$NON-NLS-1$
	public static final String PROP_I18NNAME= "element_I18NNAME"; //$NON-NLS-1$
	public static final String PROP_LANGDIR= "element_LANGDIR"; //$NON-NLS-1$
	public static final String PROP_FOCUE= "element_FOCUS"; //$NON-NLS-1$
	public static final String PROP_TEXTALGIN= "element_TEXTALIGN"; //$NON-NLS-1$
	public static final String PROP_TEXTWIDTH= "element_TEXTWIDTH"; //$NON-NLS-1$
	
	//text
	public static final String PROP_TIP = "element_TIP"; //$NON-NLS-1$
		
	//用户combodata
	public static final String PROP_REFCOMBODATA= "element_REFCOMBODATA"; //$NON-NLS-1$
	public static final String PROP_SELECTONLY= "element_REFCOMSELECTONLY"; //$NON-NLS-1$
	public static final String ALLOWEXTENDVALUE= "element_ALLOWEXTENDVALUE"; //$NON-NLS-1$
	
	//用于参照
	public static final String PROP_REFCODE= "element_REFCODE"; //$NON-NLS-1$
	public static final String REFERENCE_TIP = "element_REF_TIP"; //$NON-NLS-1$
	
	//用于radio
	//public static final String PROP_TEXT= "element_TEXT";
	public static final String PROP_CHECKED= "element_CHECKED"; //$NON-NLS-1$
	public static final String PROP_GROUP= "element_GROUP"; //$NON-NLS-1$

	//checkbox
	public static final String CHECKBOX_I18NNAME = "element_I18NNAME"; //$NON-NLS-1$
	public static final String CHECKBOX_CHECKED = "checkboxelement_CHECKED"; //$NON-NLS-1$
	public static final String CHECKBOX_DATATYPE = "checkbox_datatype"; //$NON-NLS-1$
	
	//textarea
	public static final String TEXTAREA_ROWS = "textarea_rows"; //$NON-NLS-1$
	public static final String TEXTAREA_COLS="textarea_cols"; //$NON-NLS-1$
	public static final String TEXTAREA_TIP = "element_TEXTAREA_TIP"; //$NON-NLS-1$
	
	//checkboxgroupcomp
	public static final String CHECKBOXGROUP_COMBODATAID = "CHECKBOXGROUP_COMBODATAID"; //$NON-NLS-1$
	public static final String CHECKBOXGROUP_VALUE = "CHECKBOXGROUP_VALUE"; //$NON-NLS-1$
	public static final String CHECKBOXGROUP_TABINDEX = "CHECKBOXGROUP_TABINDEX"; //$NON-NLS-1$
	public static final String CHECKBOXGROUP_SEPWIDTH = "CHECKBOXGROUP_SEPWIDTH"; //$NON-NLS-1$
	
	//radiogroupcomp
	public static final String RADIOGROUP_COMBODATAID = "RADIOGROUP_COMBODATAID"; //$NON-NLS-1$
	public static final String RADIOGROUP_VALUE = "RADIOGROUP_VALUE"; //$NON-NLS-1$
	public static final String RADIOGROUP_TABINDEX = "RADIOGROUP_TABINDEX"; //$NON-NLS-1$
	public static final String RADIOGROUP_SEPWIDTH = "RADIOGROUP_SEPWIDTH"; //$NON-NLS-1$
	public static final String RADIOGROUP_INDEX = "RADIOGROUP_INDEX"; //$NON-NLS-1$
	
	//filecomp
	public static final String FILECOMP_FILESIZE = "FILECOMP_FILESIZE"; //$NON-NLS-1$

	public TextAreaComp getTextareaComp() {
		return textareaComp;
	}

	public void setTextareaComp(TextAreaComp textareaComp) {
		this.textareaComp = textareaComp;
	}
	
	public TextComp getTextComp() {
		return textComp;
	}
	
	public void setTextComp(TextComp textComp) {
		this.textComp = textComp;
		fireStructureChange(PROP_TEXTCOMP_ELEMENT, textComp);
	}

	public ReferenceComp getReferencecomp() {
		return referencecomp;
	}

	public void setReferencecomp(ReferenceComp referencecomp) {
		this.referencecomp = referencecomp;
	}

	public CheckBoxComp getCheckboxcomp() {
		return checkboxcomp;
	}

	public void setCheckboxcomp(CheckBoxComp checkboxcomp) {
		this.checkboxcomp = checkboxcomp;
	}

	public RadioComp getRadiocomp() {
		return radiocomp;
	}

	public void setRadiocomp(RadioComp radiocomp) {
		this.radiocomp = radiocomp;
	}
	private CheckBoxComp checkboxcomp;
	private RadioComp radiocomp;
	

	public ComboBoxComp getComboboxcomp() {
		return comboboxcomp;
	}

	public void setComboboxcomp(ComboBoxComp comboboxcomp) {
		this.comboboxcomp = comboboxcomp;
	}
	
	private CheckboxGroupComp checkboxGroupComp;
	public CheckboxGroupComp getCheckboxGroupComp() {
		return checkboxGroupComp;
	}

	public void setCheckboxGroupComp(CheckboxGroupComp checkboxGroupComp) {
		this.checkboxGroupComp = checkboxGroupComp;
	}
	private RadioGroupComp radioGroupComp;



	public RadioGroupComp getRadioGroupComp() {
		return radioGroupComp;
	}

	public void setRadioGroupComp(RadioGroupComp radioGroupComp) {
		this.radioGroupComp = radioGroupComp;
	}

	private String[] getRefCombdata(){
		return LFWPersTool.getRefCombdata();
	}
	
	private String[] getRefnodes(){
		return LFWPersTool.getRefNodes();
}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		int prop = 0;
		//StringText类型
		if(textComp.getEditorType().equals(Constant.TEXTTYPE[0])){
			prop = 2;
		}
		//radiocomp
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[13])){
			prop = 3;
		}
		//IntegerText
		else if (textComp.getEditorType().equals(Constant.TEXTTYPE[1])){
			prop = 4;
		}
		//DECIMALTEXT
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[3])){
			prop = 3;
		}
		//CHECKBOX
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[2])){
			prop = 4;
		}
		//filecomp
		else if(textComp.getEditorType().equals("FileComp")){ //$NON-NLS-1$
			prop = 1;
		}
		//combobox
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[9])){
			prop = 4;
		}
		//reference
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[8])){
			prop = 2;
			
		}
		//textarea
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[10])){
			prop = 3;
		}
		//checkboxgroup
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[12])){
			prop = 4;
		}
		//radiogroup
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[7])){
			prop = 5;
		}
		PropertyDescriptor[] pds = new PropertyDescriptor[7 + prop];
		pds[0] = new ObjectComboPropertyDescriptor(PROP_TYPE,M_textcomp.TextCompElementObj_0, Constant.TEXTTYPE);
		pds[0].setCategory(M_textcomp.TextCompElementObj_1);
		pds[1] = new TextPropertyDescriptor(PROP_TEXT,M_textcomp.TextCompElementObj_2);
		pds[1].setCategory(M_textcomp.TextCompElementObj_1);
		pds[2] = new TextPropertyDescriptor(PROP_I18NNAME,M_textcomp.TextCompElementObj_3);
		pds[2].setCategory(M_textcomp.TextCompElementObj_1);
		pds[3] = new TextPropertyDescriptor(PROP_LANGDIR,M_textcomp.TextCompElementObj_4);
		pds[3].setCategory(M_textcomp.TextCompElementObj_1);
		
		pds[4] = new ObjectComboPropertyDescriptor(PROP_TEXTALGIN, M_textcomp.TextCompElementObj_5, Constant.LABELPOSITION);
		pds[4].setCategory(M_textcomp.TextCompElementObj_1);
		pds[5] = new TextPropertyDescriptor(PROP_TEXTWIDTH,M_textcomp.TextCompElementObj_6);
		pds[5].setCategory(M_textcomp.TextCompElementObj_1);
		pds[6] = new ComboBoxPropertyDescriptor(PROP_FOCUE, M_textcomp.TextCompElementObj_7, Constant.ISLAZY);
		pds[6].setCategory(M_textcomp.TextCompElementObj_1);	
		
		if(textComp.getEditorType().equals(Constant.TEXTTYPE[0])){
			//prop = 2;
			pds[7] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_8);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[8] = new TextPropertyDescriptor(PROP_TIP,M_textcomp.TextCompElementObj_10);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			
			if(textComp instanceof CheckBoxComp || textComp instanceof TextAreaComp || 	textComp instanceof RadioComp || textComp instanceof ReferenceComp || 
					textComp instanceof ComboBoxComp || textComp instanceof RadioGroupComp || textComp instanceof CheckboxGroupComp){
				String id = textComp.getId();
				textComp = new TextComp();
				textComp.setId(id);
				textComp.setEditorType(textComp.getEditorType());
				setTextComp(textComp);
			}
		}
		//IntegerText
		else if (textComp.getEditorType().equals(Constant.TEXTTYPE[1])){
			pds[7] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_11);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new TextPropertyDescriptor(PROP_MAXVALUE,M_textcomp.TextCompElementObj_12);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			pds[9] = new TextPropertyDescriptor(PROP_MINVALUE, M_textcomp.TextCompElementObj_13);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			pds[10] = new TextPropertyDescriptor(PROP_TIP,M_textcomp.TextCompElementObj_14);
			pds[10].setCategory(M_textcomp.TextCompElementObj_9);
			if(textComp instanceof CheckBoxComp ||  textComp instanceof TextAreaComp || textComp instanceof RadioComp || textComp instanceof ReferenceComp || 
					textComp instanceof ComboBoxComp || textComp instanceof RadioGroupComp || textComp instanceof CheckboxGroupComp){
				String id = textComp.getId();
				textComp = new TextComp();
				textComp.setId(id);
				textComp.setEditorType(textComp.getEditorType());
				setTextComp(textComp);
			}
		}
		//radiocomp
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[13])){
			pds[7] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_15);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new ComboBoxPropertyDescriptor(PROP_CHECKED, M_textcomp.TextCompElementObj_16, Constant.ISLAZY);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			pds[9] = new TextPropertyDescriptor(PROP_GROUP, M_textcomp.TextCompElementObj_17);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			if(radiocomp == null){
				if(textComp instanceof RadioComp)
					radiocomp = (RadioComp) textComp;
				else{
					radiocomp = new RadioComp();
					radiocomp.setId(textComp.getId());
					radiocomp.setEditorType(textComp.getEditorType());
				}
			}
			else 
				radiocomp.setEditorType(textComp.getEditorType());
			setTextComp(radiocomp);
				
		}
		//checkboxgroup
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[12])){
			pds[7] = new ObjectComboPropertyDescriptor(CHECKBOXGROUP_COMBODATAID, M_textcomp.TextCompElementObj_18, getRefCombdata());
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[8] = new TextPropertyDescriptor(CHECKBOXGROUP_VALUE,M_textcomp.TextCompElementObj_19);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[9] = new TextPropertyDescriptor(CHECKBOXGROUP_TABINDEX,M_textcomp.TextCompElementObj_20);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[10] = new TextPropertyDescriptor(CHECKBOXGROUP_SEPWIDTH,M_textcomp.TextCompElementObj_21);
			pds[10].setCategory(M_textcomp.TextCompElementObj_9);
			
			if(checkboxGroupComp == null){
				if(textComp instanceof CheckboxGroupComp)
					checkboxGroupComp = (CheckboxGroupComp) textComp;
				else{
					checkboxGroupComp = new CheckboxGroupComp();
					checkboxGroupComp.setId(textComp.getId());
					checkboxGroupComp.setEditorType(textComp.getEditorType());
				}
			}
			else
				checkboxGroupComp.setEditorType(textComp.getEditorType());
			setTextComp(checkboxGroupComp);
		}
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[7])){
			pds[7] = new ObjectComboPropertyDescriptor(RADIOGROUP_COMBODATAID, M_textcomp.TextCompElementObj_22, getRefCombdata());
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[8] = new TextPropertyDescriptor(RADIOGROUP_VALUE,M_textcomp.TextCompElementObj_23);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[9] = new TextPropertyDescriptor(RADIOGROUP_TABINDEX,M_textcomp.TextCompElementObj_24);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[10] = new TextPropertyDescriptor(RADIOGROUP_SEPWIDTH,M_textcomp.TextCompElementObj_25);
			pds[10].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[11] = new TextPropertyDescriptor(RADIOGROUP_INDEX,M_textcomp.TextCompElementObj_26);
			pds[11].setCategory(M_textcomp.TextCompElementObj_9);
			
			if(radioGroupComp == null){
				if(textComp instanceof RadioGroupComp)
					radioGroupComp = (RadioGroupComp) textComp;
				else{
					radioGroupComp = new RadioGroupComp();
					radioGroupComp.setId(textComp.getId());
					radioGroupComp.setEditorType(textComp.getEditorType());
				}
			}
			else
				radioGroupComp.setEditorType(textComp.getEditorType());
			setTextComp(radioGroupComp);
		}
		
		//comboboxcomp
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[9])){
			//prop = 1;
			pds[7] = new ObjectComboPropertyDescriptor(PROP_REFCOMBODATA, M_textcomp.TextCompElementObj_27, getRefCombdata());
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);

			pds[8] = new ComboBoxPropertyDescriptor(PROP_SELECTONLY,M_textcomp.TextCompElementObj_28, Constant.ISLAZY);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			
			
			pds[9] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_29);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[10] = new ComboBoxPropertyDescriptor(ALLOWEXTENDVALUE,M_textcomp.TextCompElementObj_30, Constant.ISLAZY);
			pds[10].setCategory(M_textcomp.TextCompElementObj_9);
			
			
			if(comboboxcomp == null){
				if(textComp instanceof ComboBoxComp)
					comboboxcomp = (ComboBoxComp) textComp;
				if(comboboxcomp == null){
					comboboxcomp = new ComboBoxComp();
					comboboxcomp.setId(textComp.getId());
					comboboxcomp.setEditorType(textComp.getEditorType());
				}
			}
			else
				comboboxcomp.setEditorType(textComp.getEditorType());
			setTextComp(comboboxcomp);
		}
		//reference
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[8])){
			pds[7] = new ObjectComboPropertyDescriptor(PROP_REFCODE, M_textcomp.TextCompElementObj_31, getRefnodes());
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new TextPropertyDescriptor(REFERENCE_TIP,M_textcomp.TextCompElementObj_32);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			if(referencecomp == null){
				if(textComp instanceof ReferenceComp)
					referencecomp = (ReferenceComp) textComp;
				else{
					referencecomp = new ReferenceComp();
					referencecomp.setId(textComp.getId());
					referencecomp.setEditorType(textComp.getEditorType());
				}
			}
			else
				referencecomp.setEditorType(textComp.getEditorType());
			setTextComp(referencecomp);
				
		}
		//DECIMALTEXT
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[3])){
			pds[7] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_33);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new TextPropertyDescriptor(PROP_PRECISION, M_textcomp.TextCompElementObj_34);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			pds[9] = new TextPropertyDescriptor(PROP_TIP,M_textcomp.TextCompElementObj_32);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			if(textComp instanceof CheckBoxComp || textComp instanceof TextAreaComp || 	textComp instanceof RadioComp || textComp instanceof ReferenceComp || 
					textComp instanceof ComboBoxComp || textComp instanceof RadioGroupComp || textComp instanceof CheckboxGroupComp){
				String id = textComp.getId();
				textComp = new TextComp();
				textComp.setId(id);
				textComp.setEditorType(textComp.getEditorType());
				setTextComp(textComp);
			}
		}
		
		//checkbox
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[2])){
			pds[7] = new TextPropertyDescriptor(PROP_VALUE,M_textcomp.TextCompElementObj_33);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new TextPropertyDescriptor(CHECKBOX_I18NNAME,M_textcomp.TextCompElementObj_35);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[9] = new ComboBoxPropertyDescriptor(CHECKBOX_CHECKED, M_textcomp.TextCompElementObj_36, Constant.ISLAZY);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			
			pds[10] = new ObjectComboPropertyDescriptor(CHECKBOX_DATATYPE, M_textcomp.TextCompElementObj_37, Constant.CHECKBOXTYPE);
			pds[10].setCategory(M_textcomp.TextCompElementObj_9);
				
			if(checkboxcomp == null){
				if(textComp instanceof CheckBoxComp)
					checkboxcomp = (CheckBoxComp) textComp;
				else{
					checkboxcomp = new CheckBoxComp();
					checkboxcomp.setId(textComp.getId());
					checkboxcomp.setEditorType(textComp.getEditorType());
				}
			}
			else
				checkboxcomp.setEditorType(textComp.getEditorType());
			setTextComp(checkboxcomp);
		}
		
		else if(textComp.getEditorType().equals(Constant.TEXTTYPE[10])){
			pds[7] = new TextPropertyDescriptor(TEXTAREA_ROWS, M_textcomp.TextCompElementObj_38);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			pds[8] = new TextPropertyDescriptor(TEXTAREA_COLS, M_textcomp.TextCompElementObj_39);
			pds[8].setCategory(M_textcomp.TextCompElementObj_9);
			pds[9] = new TextPropertyDescriptor(TEXTAREA_TIP,M_textcomp.TextCompElementObj_32);
			pds[9].setCategory(M_textcomp.TextCompElementObj_9);
			if(textareaComp == null){
				if(textComp instanceof TextAreaComp)
					textareaComp = (TextAreaComp) textComp;
				else{
					textareaComp = new TextAreaComp();
					textareaComp.setId(textComp.getId());
					textareaComp.setEditorType(textComp.getEditorType());
				}
			}
			else
				textareaComp.setEditorType(textComp.getEditorType());
			setTextComp(textareaComp);
		}
		else if(textComp.getEditorType().equals("FileComp")){ //$NON-NLS-1$
			pds[7] = new TextPropertyDescriptor(FILECOMP_FILESIZE, M_textcomp.TextCompElementObj_40);
			pds[7].setCategory(M_textcomp.TextCompElementObj_9);
			setTextComp(textComp);
		}
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	
	
	public void setPropertyValue(Object id, Object value) {
		if(PROP_VALUE.equals(id)){
			textComp.setValue((String)value);
		}
		else if(PROP_TIP.equals(id))
			textComp.setTip((String)value);
		else if(PROP_TEXT.equals(id)){
			textComp.setText((String)value);
		}
		else if(PROP_I18NNAME.equals(id)){
			textComp.setI18nName((String)value);
		}
		else if(PROP_LANGDIR.equals(id)){
			textComp.setLangDir((String)value);
		}
		else if(PROP_TYPE.equals(id)){
			textComp.setEditorType((String)value);
		}
		
		else if(PROP_FOCUE.equals(id)){
			textComp.setFocus((Integer)value == 0);
		}
		else if(PROP_TEXTALGIN.equals(id)){
			textComp.setTextAlign((String)value);
		}
		else if(PROP_TEXTWIDTH.equals(id)){
			if(value == null || value.equals("")) //$NON-NLS-1$
				value = "0"; //$NON-NLS-1$
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			textComp.setTextWidth(intVal);
		}
		else if(PROP_MAXVALUE.equals(id)){
			textComp.setMaxValue((String)value);
		}
		else if(PROP_MINVALUE.equals(id))
			textComp.setMinValue((String)value);
		else if(PROP_PRECISION.equals(id))
			textComp.setPrecision((String)value);
		else if(PROP_REFCOMBODATA.equals(id)){
			comboboxcomp.setRefComboData((String)value);
		}
		else if(PROP_SELECTONLY.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			comboboxcomp.setSelectOnly(truevalue);
		}
		else if(PROP_CHECKED.equals(id)){
			radiocomp.setChecked((Integer)value == 0);
		}
		else if(PROP_GROUP.equals(id)){
			radiocomp.setGroup((String)value);
		}
		else if(PROP_REFCODE.equals(id)){
			referencecomp.setRefcode((String)value);
		}
		else if(REFERENCE_TIP.equals(id))
			referencecomp.setTip((String)value);
		else if(CHECKBOX_CHECKED.equals(id)){
			checkboxcomp.setChecked((Integer)value == 0);
		}
		else if(CHECKBOX_I18NNAME.equals(id)){
			checkboxcomp.setI18nName((String)value);
		}
		else if(CHECKBOX_DATATYPE.equals(id)){
			checkboxcomp.setDataType((String)value);
		}
		else if(TEXTAREA_ROWS.equals(id)){
			textareaComp.setRows((String)value);
		}
		else if(TEXTAREA_COLS.equals(id)){
			textareaComp.setCols((String)value);
		}
		else if(TEXTAREA_TIP.equals(id))
			textareaComp.setTip((String)value);
		else if(CHECKBOXGROUP_COMBODATAID.equals(id)){
			checkboxGroupComp.setComboDataId((String)value);
		}
		else if(CHECKBOXGROUP_VALUE.equals(id)){
			checkboxGroupComp.setValue((String)value);
		}
		else if(CHECKBOXGROUP_TABINDEX.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			checkboxGroupComp.setTabIndex(intVal);
		}
		else if(CHECKBOXGROUP_SEPWIDTH.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			checkboxGroupComp.setSepWidth(intVal);
		}
		else if(RADIOGROUP_COMBODATAID.equals(id)){
			radioGroupComp.setComboDataId((String)value);
		}
		else if(RADIOGROUP_VALUE.equals(id)){
			radioGroupComp.setValue((String)value);
		}
		else if(RADIOGROUP_TABINDEX.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			radioGroupComp.setTabIndex(intVal);
		}
		else if(RADIOGROUP_SEPWIDTH.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			radioGroupComp.setSepWidth(intVal);
		}
		else if(RADIOGROUP_INDEX.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < 0)
				return;
			radioGroupComp.setIndex(intVal);
		}
		else if(ALLOWEXTENDVALUE.equals(id)){
			comboboxcomp.setAllowExtendValue((Integer)value == 0);
		}
		else if(FILECOMP_FILESIZE.equals(id)){
			textComp.setSizeLimit((String)value);
		}
		
		else
			super.setPropertyValue(id, value);
	}
	public Object getPropertyValue(Object id) {
		if(PROP_VALUE.equals(id))
			return textComp.getValue() == null?"":textComp.getValue(); //$NON-NLS-1$
		else if(PROP_TYPE.equals(id))
			return textComp.getEditorType() == null?"":textComp.getEditorType(); //$NON-NLS-1$
		else if(PROP_FOCUE.equals(id))
			return textComp.isFocus() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_TIP.equals(id))
			return textComp.getTip() == null?"":textComp.getTip();	 //$NON-NLS-1$
		else if(PROP_TEXTALGIN.equals(id))
			return textComp.getTextAlign() == null?"":textComp.getTextAlign(); //$NON-NLS-1$
		else if(PROP_TEXTWIDTH.equals(id))
			return textComp.getTextWidth() == 0?"": String.valueOf(textComp.getTextWidth()); //$NON-NLS-1$
		else if(PROP_MAXVALUE.equals(id))
			return textComp.getMaxValue() == null?"":textComp.getMaxValue(); //$NON-NLS-1$
		else if(PROP_MINVALUE.equals(id))
			return textComp.getMinValue() == null?"":textComp.getMinValue(); //$NON-NLS-1$
		else if(PROP_PRECISION.equals(id))
			return textComp.getPrecision() == null?"":textComp.getPrecision(); //$NON-NLS-1$
		else if(PROP_REFCOMBODATA.equals(id)){
//			if(comboboxcomp == null){
//				comboboxcomp = new ComboBoxComp();
//				comboboxcomp.setId(textComp.getId());
//				setTextComp(comboboxcomp);
//			}
			return comboboxcomp.getRefComboData() == null?"":comboboxcomp.getRefComboData(); //$NON-NLS-1$
		}
		else if(PROP_SELECTONLY.equals(id)){
			return (comboboxcomp.isSelectOnly() == true)? Integer.valueOf(0):Integer.valueOf(1);	
		}
		else if(PROP_TEXT.equals(id)){
			return textComp.getText() == null?"":textComp.getText(); //$NON-NLS-1$
		}
		else if(PROP_I18NNAME.equals(id)){
			return textComp.getI18nName() == null?"":textComp.getI18nName(); //$NON-NLS-1$
		}
		else if(PROP_LANGDIR.equals(id)){
			return textComp.getLangDir() == null?"":textComp.getLangDir(); //$NON-NLS-1$
		}
		else if(PROP_GROUP.equals(id)){
//			if(radiocomp == null){
//				radiocomp = new RadioComp();
//				radiocomp.setId(textComp.getId());
//				setTextComp(radiocomp);
//			}
			return radiocomp.getGroup() == null?"":radiocomp.getGroup(); //$NON-NLS-1$
		}
		else if(PROP_REFCODE.equals(id)){
//			if(referencecomp == null){
//				referencecomp = new ReferenceComp();
//				referencecomp.setId(textComp.getId());
//				setTextComp(referencecomp);
//			}
			return referencecomp.getRefcode() == null?"":referencecomp.getRefcode(); //$NON-NLS-1$
		}
		else if(REFERENCE_TIP.equals(id))
			return referencecomp.getTip() == null?"":referencecomp.getTip(); //$NON-NLS-1$
		else if(PROP_CHECKED.equals(id)){
//			if(radiocomp == null){
//				radiocomp = new RadioComp();
//				radiocomp.setId(textComp.getId());
//				setTextComp(radiocomp);
//			}
			return radiocomp.isChecked() == true? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(CHECKBOX_CHECKED.equals(id)){
//			if(checkboxcomp == null){
//				checkboxcomp = new CheckBoxComp();
//				checkboxcomp.setId(textComp.getId());
//				setTextComp(checkboxcomp);
//			}
			return checkboxcomp.isChecked() == true? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(CHECKBOX_I18NNAME.equals(id)){
//			if(checkboxcomp == null){
//				checkboxcomp = new CheckBoxComp();
//				checkboxcomp.setId(textComp.getId());
//				setTextComp(checkboxcomp);
//			}
			return checkboxcomp.getI18nName() == null? "": checkboxcomp.getI18nName(); //$NON-NLS-1$
		}
		else if(CHECKBOX_DATATYPE.equals(id)){
//			if(checkboxcomp == null){
//				checkboxcomp = new CheckBoxComp();
//				checkboxcomp.setId(textComp.getId());
//				setTextComp(checkboxcomp);
//			}
			return checkboxcomp.getDataType() == null? "": checkboxcomp.getDataType(); //$NON-NLS-1$
		}
		else if(TEXTAREA_ROWS.equals(id)){
			return textareaComp.getRows() == null?"":textareaComp.getRows(); //$NON-NLS-1$
		}
		else if(TEXTAREA_COLS.equals(id)){
			return textareaComp.getCols() == null?"":textareaComp.getCols(); //$NON-NLS-1$
		}
		else if(TEXTAREA_TIP.equals(id))
			return textareaComp.getTip() == null?"":textareaComp.getTip(); //$NON-NLS-1$
		else if(CHECKBOXGROUP_COMBODATAID.equals(id)){
			return checkboxGroupComp.getComboDataId() == null?"":checkboxGroupComp.getComboDataId(); //$NON-NLS-1$
		}
		else if(CHECKBOXGROUP_VALUE.equals(id)){
			return checkboxGroupComp.getValue() == null?"":checkboxGroupComp.getValue(); //$NON-NLS-1$
		}
		else if(CHECKBOXGROUP_TABINDEX.equals(id)){
			return checkboxGroupComp.getTabIndex() == 0? "0":String.valueOf(checkboxGroupComp.getTabIndex()); //$NON-NLS-1$
		}
		else if(CHECKBOXGROUP_SEPWIDTH.equals(id)){
			return checkboxGroupComp.getSepWidth() == 0? "0":String.valueOf(checkboxGroupComp.getSepWidth()); //$NON-NLS-1$
		}
		
		else if(RADIOGROUP_COMBODATAID.equals(id)){
			return radioGroupComp.getComboDataId() == null?"":radioGroupComp.getComboDataId(); //$NON-NLS-1$
		}
		else if(RADIOGROUP_VALUE.equals(id)){
			return radioGroupComp.getValue() == null?"":radioGroupComp.getValue(); //$NON-NLS-1$
		}
		else if(RADIOGROUP_TABINDEX.equals(id)){
			return radioGroupComp.getTabIndex() == 0? "0":String.valueOf(radioGroupComp.getTabIndex()); //$NON-NLS-1$
		}
		else if(RADIOGROUP_SEPWIDTH.equals(id)){
			return radioGroupComp.getSepWidth() == 0? "0":String.valueOf(radioGroupComp.getSepWidth()); //$NON-NLS-1$
		}
		else if(RADIOGROUP_INDEX.equals(id)){
			return radioGroupComp.getIndex() == 0? "0":String.valueOf(radioGroupComp.getIndex()); //$NON-NLS-1$
		}
		else if(ALLOWEXTENDVALUE.equals(id)){
			return comboboxcomp.isAllowExtendValue() == true? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(FILECOMP_FILESIZE.equals(id)){
			return textComp.getSizeLimit() == null ?"":textComp.getSizeLimit(); //$NON-NLS-1$
		}
		else return super.getPropertyValue(id);
	}
	
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return textComp;
	}
}
