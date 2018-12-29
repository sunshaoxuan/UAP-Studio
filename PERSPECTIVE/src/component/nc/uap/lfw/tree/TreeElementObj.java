package nc.uap.lfw.tree;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.perspective.model.Constant;
import nc.uap.lfw.tree.core.TreeEditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Ê÷×é¼þµÄmodel
 * @author zhangxya
 *
 */
public class TreeElementObj extends LFWWebComponentObj{

	private static final long serialVersionUID = 6253081418703115641L;
	
	public static final String PROP_DRAGENABLE= "element_DRAGENABLE"; //$NON-NLS-1$
	public static final String PROP_WITHCHECKBOX= "element_WITHCHECKBOX"; //$NON-NLS-1$
	public static final String PROP_WITHROOT= "element_WITHROOT"; //$NON-NLS-1$
	public static final String PROP_ROOTOPEN= "element_ROOTOPEN"; //$NON-NLS-1$
	public static final String PROP_TEXT= "element_TEXT"; //$NON-NLS-1$
	public static final String PROP_LANGDIR= "element_LANGDIR"; //$NON-NLS-1$
	public static final String PROP_I18NNAME= "element_I18NNAME";	 //$NON-NLS-1$
	public static final String PROP_TREE_ELEMENT ="tree_element"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	public static final String PROP_CHECKBOXMODEL = "element_CHECKBOXMODEL"; //$NON-NLS-1$
	public static final String PROP_CANEDIT = "element_CANEDIT"; //$NON-NLS-1$
	public static final String PROP_IMGRENDER = "element_IMGRENDER"; //$NON-NLS-1$
	public static final String PROP_OPENLEVEL = "element_OPENLEVEL"; //$NON-NLS-1$
	
	private TreeViewComp treeComp;
	private Dataset ds;
	private boolean truevalue = false;
	
	public TreeViewComp getTreeComp() {
		return treeComp;
	}
	
	public void setTreeComp(TreeViewComp treeComp) {
		this.treeComp = treeComp;
		fireStructureChange(PROP_TREE_ELEMENT, treeComp);
	}

	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
//		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		
		PropertyDescriptor[] pds = new PropertyDescriptor[16];
		pds[0] =  new ComboBoxPropertyDescriptor(PROP_WITHCHECKBOX,M_tree.TreeElementObj_0, Constant.ISLAZY);
		pds[0].setCategory(M_tree.TreeElementObj_1);
		pds[1] =  new ComboBoxPropertyDescriptor(PROP_ROOTOPEN,M_tree.TreeElementObj_2, Constant.ISLAZY);
		pds[1].setCategory(M_tree.TreeElementObj_1);
		pds[2] = new ComboBoxPropertyDescriptor(PROP_DRAGENABLE,M_tree.TreeElementObj_3, Constant.ISLAZY);
		pds[2].setCategory(M_tree.TreeElementObj_1);
		pds[3] = new ComboBoxPropertyDescriptor(PROP_WITHROOT,M_tree.TreeElementObj_4, Constant.ISLAZY);
		pds[3].setCategory(M_tree.TreeElementObj_1);
		pds[4] = new TextPropertyDescriptor(PROP_TEXT, M_tree.TreeElementObj_5);
		pds[4].setCategory(M_tree.TreeElementObj_1);
		pds[5] = new TextPropertyDescriptor(PROP_I18NNAME, M_tree.TreeElementObj_6);
		pds[5].setCategory(M_tree.TreeElementObj_1);
		pds[6] = new TextPropertyDescriptor(PROP_LANGDIR, M_tree.TreeElementObj_7);
		pds[6].setCategory(M_tree.TreeElementObj_1);
		//caption
		pds[8] = new TextPropertyDescriptor(PROP_CAPTION,M_tree.TreeElementObj_8);
		pds[8].setCategory(M_tree.TreeElementObj_9);
		pds[7] = new ComboBoxPropertyDescriptor(PROP_CHECKBOXMODEL,M_tree.TreeElementObj_10, Constant.CHECKBOXMODEL);
		pds[7].setCategory(M_tree.TreeElementObj_1);
		pds[9] = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		pds[9].setCategory(M_tree.TreeElementObj_9);
		pds[10] = new ComboBoxPropertyDescriptor(PROP_VISIBLE,M_tree.TreeElementObj_11, Constant.ISLAZY);
		pds[10].setCategory(M_tree.TreeElementObj_9);
		pds[11] = new ComboBoxPropertyDescriptor(PROP_ENABLED,M_tree.TreeElementObj_12, Constant.ISLAZY);
		pds[11].setCategory(M_tree.TreeElementObj_9);
		pds[12] = new TextPropertyDescriptor(PROP_CONTEXTMENU,M_tree.TreeElementObj_13);
		pds[12].setCategory(M_tree.TreeElementObj_9);
		pds[13] = new ComboBoxPropertyDescriptor(PROP_CANEDIT,M_tree.TreeElementObj_14, Constant.ISLAZY);
		pds[13].setCategory(M_tree.TreeElementObj_1);
		pds[14] = new TextPropertyDescriptor(PROP_IMGRENDER,M_tree.TreeElementObj_15);
		pds[14].setCategory(M_tree.TreeElementObj_1);
		pds[15] = new TextPropertyDescriptor(PROP_OPENLEVEL,M_tree.TreeElementObj_16);
		pds[15].setCategory(M_tree.TreeElementObj_1);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if(PROP_ROOTOPEN.equals(id)){
			if((Integer)value == 0)
				truevalue = true;
			else
				truevalue = false;
			treeComp.setRootOpen(truevalue);
		}
		else if(PROP_DRAGENABLE.equals(id)){
			if((Integer)value == 0)
				truevalue = true;
			else
				truevalue = false;
			treeComp.setDragEnable(truevalue);
		}
		else if(PROP_WITHCHECKBOX.equals(id)){
			if((Integer)value == 0)
				truevalue = true;
			else
				truevalue = false;
			treeComp.setWithCheckBox(truevalue);
		}
		else if(PROP_WITHROOT.equals(id)){
			if((Integer)value == 0)
				truevalue = true;
			else
				truevalue = false;
			treeComp.setWithRoot(truevalue);
		}
		else if(PROP_CHECKBOXMODEL.equals(id)){			
			treeComp.setCheckBoxModel((Integer)value);
		}
		else if(PROP_CAPTION.equals(id)){
			String oldValue = treeComp.getCaption();
			if((oldValue == null && value != null)  || (oldValue != null && value != null && !oldValue.equals(value))){
				treeComp.setCaption((String)value);
				TreeEditor.getActiveEditor().refreshTreeItemText(treeComp);
			}
		}
		if(PROP_CANEDIT.equals(id)){
			if((Integer)value == 0)
				truevalue = true;
			else
				truevalue = false;
			treeComp.setCanEdit(truevalue);
		}
		else if(PROP_TEXT.equals(id))
			treeComp.setText((String)value);
		else if(PROP_I18NNAME.equals(id))
			treeComp.setI18nName((String)value);
		else if(PROP_LANGDIR.equals(id))
			treeComp.setLangDir((String)value);
		else if(PROP_IMGRENDER.equals(id))
			treeComp.setImgRender((String)value);
		else if(PROP_OPENLEVEL.equals(id))
			try{
				treeComp.setOpenLevel(Integer.parseInt((String)value));
			}
			catch(NumberFormatException e){
				MessageDialog.openError(null, M_tree.TreeElementObj_17, M_tree.TreeElementObj_18);
			}
	}
	
	public Object getPropertyValue(Object id) {
		if(PROP_ROOTOPEN.equals(id))
			return (treeComp.isRootOpen() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_DRAGENABLE.equals(id))
			return (treeComp.isDragEnable() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_WITHCHECKBOX.equals(id))
			return (treeComp.isWithCheckBox() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_WITHROOT.equals(id))
			return (treeComp.isWithRoot() == true)? Integer.valueOf(0):Integer.valueOf(1);
	    else if(PROP_CHECKBOXMODEL.equals(id))
				return treeComp.getCheckBoxModel();
		else if(PROP_TEXT.equals(id))
			return treeComp.getText() == null?"":treeComp.getText(); //$NON-NLS-1$
		else if(PROP_I18NNAME.equals(id))
			return treeComp.getI18nName() == null?"":treeComp.getI18nName(); //$NON-NLS-1$
		else if(PROP_LANGDIR.equals(id))
			return treeComp.getLangDir()== null?"":treeComp.getLangDir(); //$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return treeComp.getCaption() == null?"":treeComp.getCaption(); //$NON-NLS-1$
		else if(PROP_CANEDIT.equals(id))
			return (treeComp.isCanEdit() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_IMGRENDER.equals(id))
			return treeComp.getImgRender()== null?"":treeComp.getImgRender(); //$NON-NLS-1$
		else if(PROP_OPENLEVEL.equals(id))
			return String.valueOf(treeComp.getOpenLevel());
		return super.getPropertyValue(id);
	}
	
	public WebElement getWebElement() {
		return treeComp;
	}
}
