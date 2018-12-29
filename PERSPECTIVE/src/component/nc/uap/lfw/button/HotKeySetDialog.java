package nc.uap.lfw.button;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_button;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HotKeySetDialog extends DialogWithTitle{

	private Combo modifierCombo;
	private String result;
	
	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public HotKeySetDialog(Shell parentShell, String title) {
		super(parentShell, title);
		initHotKeyModifierMap();
	}
	
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
	
	private Text hotKeyText;
	
	private void initHotKeyModifierMap() {
		if (hotKeyModifierMap == null) {
			hotKeyModifierMap = new HashMap<String, String>();
			hotKeyModifierMap.put("1", "SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("2", "CTRL"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("8", "ALT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("3", "CTRL+SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("10", "CTRL+ALT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("9", "ALT+SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("11", "CTRL+SHIFT+ALT"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	
	protected void okPressed() {
		String preHotKey = (String) modifierCombo.getText();
		String hotKey = hotKeyText.getText();
		if(hotKey == null || hotKey.equals("")){ //$NON-NLS-1$
			MessageDialog.openError(null, M_button.HotKeySetDialog_0, M_button.HotKeySetDialog_1);
			return;
		}
		result = preHotKey + "+" +  hotKey; //$NON-NLS-1$
		super.okPressed();
	}
	
	

	// ÈÈ¼üÇ°×ºÓ³Éä
	private Map<String, String> hotKeyModifierMap = null;
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label prehotkeyLabel = new Label(container, SWT.NONE);
		prehotkeyLabel.setText(M_button.HotKeySetDialog_2);
		prehotkeyLabel.setLayoutData(new GridData(60,20));
		//prehotkeyLabel.setLayoutData(createGridData(120, 1));
		modifierCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		modifierCombo.setLayoutData(new GridData(120,20));
		
		Label hotkeyLabel = new Label(container, SWT.NONE);
		hotkeyLabel.setText(M_button.HotKeySetDialog_3);
		hotkeyLabel.setLayoutData(new GridData(60,20));
		//hotkeyLabel.setLayoutData(createGridData(120, 1));
		hotKeyText = new Text(container, SWT.BORDER);
		hotKeyText.setLayoutData(new GridData(140,20));
		//hotKeyText.setLayoutData(createGridData(120, 1));
		
		Set<String> values = hotKeyModifierMap.keySet();
		Iterator<String> it = values.iterator();
		int index = 0;
		while (it.hasNext()) {
			String value = it.next();
			String text = hotKeyModifierMap.get(value);
			modifierCombo.add(text);
			modifierCombo.setData(text, value);
			if (index == 0)
				modifierCombo.select(0);
		}
		return container;
	}

}
