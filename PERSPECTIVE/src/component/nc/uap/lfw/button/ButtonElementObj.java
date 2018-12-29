package nc.uap.lfw.button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_button;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * button model
 * 
 * @author zhangxya
 * 
 */
public class ButtonElementObj extends LFWWebComponentObj {

	private static final long serialVersionUID = 6253081418703115641L;
	private ButtonComp buttonComp;
	// 热键前缀映射
	private Map<String, String> hotKeyModifierMap = null;

	public static final String PROP_TEXT = "element_Text"; //$NON-NLS-1$
	public static final String PROP_TIP = "element_TIP"; //$NON-NLS-1$
	public static final String PROP_I18NNAME = "element_I118NNAME"; //$NON-NLS-1$
	public static final String PROP_REFIMG = "element_REFIMG"; //$NON-NLS-1$
	// public static final String PROP_ALIGN = "element_ALIGN";
	public static final String PROP_LANGDIR = "element_LANGDIR"; //$NON-NLS-1$
	public static final String PROP_TIPI18NNAME = "element_TIPI18NNAME"; //$NON-NLS-1$
	public static final String PROP_BUTTON_ELEMENT = "button_element"; //$NON-NLS-1$
	public static final String PROP_BUTTON_HOTKEY = "button_HOTKEY"; //$NON-NLS-1$
	public static final String PROP_DISPLAY_HOTKEY = "button_DISPLAYHOTKEY"; //$NON-NLS-1$

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[8];
		pds[0] = new TextPropertyDescriptor(PROP_TEXT, M_button.ButtonElementObj_0);
		pds[0].setCategory(M_button.ButtonElementObj_1);
		pds[1] = new TextPropertyDescriptor(PROP_TIP, M_button.ButtonElementObj_2);
		pds[1].setCategory(M_button.ButtonElementObj_1);
		pds[2] = new TextPropertyDescriptor(PROP_I18NNAME, M_button.ButtonElementObj_3);
		pds[2].setCategory(M_button.ButtonElementObj_1);
		pds[3] = new TextPropertyDescriptor(PROP_REFIMG, M_button.ButtonElementObj_4);
		pds[3].setCategory(M_button.ButtonElementObj_1);
		// pds[4] = new TextPropertyDescriptor(PROP_ALIGN,"位置");
		// pds[4].setCategory("高级");
		pds[4] = new TextPropertyDescriptor(PROP_LANGDIR, M_button.ButtonElementObj_5);
		pds[4].setCategory(M_button.ButtonElementObj_1);
		pds[5] = new TextPropertyDescriptor(PROP_TIPI18NNAME, M_button.ButtonElementObj_6);
		pds[5].setCategory(M_button.ButtonElementObj_1);

		pds[6] = new HotKeySetPropertyDescriptor(PROP_BUTTON_HOTKEY, M_button.ButtonElementObj_7);
		
		pds[6].setCategory(M_button.ButtonElementObj_1);

		pds[7] = new NoEditableTextPropertyDescriptor(PROP_DISPLAY_HOTKEY,
				M_button.ButtonElementObj_8);
		pds[7].setCategory(M_button.ButtonElementObj_1);

		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if (PROP_TEXT.equals(id))
			buttonComp.setText((String) value);
		else if (PROP_TIP.equals(id)) {
			buttonComp.setTip((String) value);
		} else if (PROP_I18NNAME.equals(id)) {
			buttonComp.setI18nName((String) value);
		} else if (PROP_REFIMG.equals(id)) {
			buttonComp.setRefImg((String) value);
		}
		// else if(PROP_ALIGN.equals(id)){
		// buttonComp.setAlign((String)value);
		// }
		else if (PROP_LANGDIR.equals(id))
			buttonComp.setLangDir((String) value);
		else if (PROP_TIPI18NNAME.equals(id))
			buttonComp.setTipI18nName((String) value);
		else if (PROP_BUTTON_HOTKEY.equals(id)) {
			String text = (String) value;
			if(text.lastIndexOf("+")<0){ //$NON-NLS-1$
				MessageDialog.openError(null, M_button.ButtonElementObj_9, M_button.ButtonElementObj_10);
				return;
			}
			buttonComp.setHotKey(text.substring(text.lastIndexOf("+") + 1)); //$NON-NLS-1$
			buttonComp.setDisplayHotKey((String) value);
			if (hotKeyModifierMap == null) {
				hotKeyModifierMap = new HashMap<String, String>();
				hotKeyModifierMap.put("SHIFT", "1"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("CTRL", "2"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("ALT", "8"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("CTRL+SHIFT", "3"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("CTRL+ALT", "10"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("ALT+SHIFT", "9"); //$NON-NLS-1$ //$NON-NLS-2$
				hotKeyModifierMap.put("CTRL+SHIFT+ALT", "11"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			String hotKeyType = text.substring(0, text.lastIndexOf("+")); //$NON-NLS-1$
			if (hotKeyModifierMap.get(hotKeyType) != null) {
				buttonComp.setModifiers(Integer.parseInt(hotKeyModifierMap.get(hotKeyType)));
			}
		} else if (PROP_DISPLAY_HOTKEY.equals(id))
			buttonComp.setDisplayHotKey((String) id);

	}

	public Object getPropertyValue(Object id) {
		if (PROP_TEXT.equals(id))
			return buttonComp.getText() == null ? "" : buttonComp.getText(); //$NON-NLS-1$
		else if (PROP_TIP.equals(id))
			return buttonComp.getTip() == null ? "" : buttonComp.getTip(); //$NON-NLS-1$
		else if (PROP_I18NNAME.equals(id))
			return buttonComp.getI18nName() == null ? "" : buttonComp //$NON-NLS-1$
					.getI18nName();
		else if (PROP_REFIMG.equals(id))
			return buttonComp.getRefImg() == null ? "" : buttonComp.getRefImg(); //$NON-NLS-1$
		// else if(PROP_ALIGN.equals(id))
		// return buttonComp.getAlign() == null?"":buttonComp.getAlign();
		else if (PROP_LANGDIR.equals(id))
			return buttonComp.getLangDir() == null ? "" : buttonComp //$NON-NLS-1$
					.getLangDir();
		else if (PROP_TIPI18NNAME.equals(id))
			return buttonComp.getTipI18nName() == null ? "" : buttonComp //$NON-NLS-1$
					.getTipI18nName();
		else if (PROP_BUTTON_HOTKEY.equals(id))
			return buttonComp.getHotKey() == null ? "" : buttonComp.getHotKey(); //$NON-NLS-1$
		else if (PROP_DISPLAY_HOTKEY.equals(id))
			return buttonComp.getDisplayHotKey() == null ? "" : buttonComp //$NON-NLS-1$
					.getDisplayHotKey();
		else
			return super.getPropertyValue(id);
	}

	public WebElement getWebElement() {
		return buttonComp;
	}

	public ButtonComp getButtonComp() {
		return buttonComp;
	}

	public void setButtonComp(ButtonComp buttonComp) {
		this.buttonComp = buttonComp;
		fireStructureChange(PROP_BUTTON_ELEMENT, buttonComp);
	}
}
