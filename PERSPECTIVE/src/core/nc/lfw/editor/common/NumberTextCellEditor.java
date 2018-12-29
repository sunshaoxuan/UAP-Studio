package nc.lfw.editor.common;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class NumberTextCellEditor extends TextCellEditor {

	public NumberTextCellEditor() {
		// TODO Auto-generated constructor stub
	}

	public NumberTextCellEditor(Composite parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public NumberTextCellEditor(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	

	
	protected Control createControl(Composite parent) {
		Text text =(Text) super.createControl(parent);
		text.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {
				String str = e.text;
				if(str == null)
					return;
				char[] chars = str.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					char c = chars[i];
					if(!Character.isDigit(c)){
						e.doit =false;
						break;
					}
				}
			}
			
		});
		return text;
	}
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\r') { // Return key
            if (text != null && !text.isDisposed()
                    && (text.getStyle() & SWT.MULTI) != 0) {
                if ((keyEvent.stateMask & SWT.CTRL) != 0) {
                    super.keyReleaseOccured(keyEvent);
                }
            }else if(text != null && !text.isDisposed()){
                if ((keyEvent.stateMask & SWT.SHIFT) != 0) {
                    fireApplyEditorValue();
                    deactivate();
                }
            }
            return;
        }
        super.keyReleaseOccured(keyEvent);
    }
}
