package nc.uap.lfw.editor.extNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_editor;

public class RefInfoDialog extends DialogWithTitle{
	
	private Text refCodeText = null;
	
	private Text refNameText = null;
	
	private Combo refTypeText = null;
	
	private Text refClassText = null;
	
	private String refCode = null;
	
	private String refName = null;
	
	private int refType = 0;
	
	private String refClass = null;

	public RefInfoDialog(String title) {
		super(null, title);
		// TODO Auto-generated constructor stub
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label codeLabel = new Label(container, SWT.NONE);
		codeLabel.setText(M_editor.RefInfoDialog_0);
		refCodeText = new Text(container, SWT.NONE);
		refCodeText.setLayoutData(new GridData(220,20));
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_editor.RefInfoDialog_1);
		refNameText = new Text(container, SWT.NONE);
		refNameText.setLayoutData(new GridData(220,20));
		refNameText.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				if(refNameText.getText() == null || refNameText.getText().trim().length() == 0){
					refNameText.setText(refCodeText.getText());
				}
				refNameText.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		Label typeLabel = new Label(container, SWT.NONE);
		typeLabel.setText(M_editor.RefInfoDialog_2);
		refTypeText = new Combo(container, SWT.READ_ONLY);
		refTypeText.setLayoutData(new GridData(100,20));
		String[] items = {M_editor.RefInfoDialog_3,M_editor.RefInfoDialog_4,M_editor.RefInfoDialog_5};
		refTypeText.setItems(items);
		refTypeText.select(0);
		
		Label classLabel = new Label(container, SWT.NONE);
		classLabel.setText(M_editor.RefInfoDialog_6);
		refClassText = new Text(container, SWT.NONE);
		refClassText.setLayoutData(new GridData(220,20));
		
		refClassText.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				if(refClassText.getText() == null || refClassText.getText().trim().length() == 0){
					refClassText.setText("nc.ref.model."+refCodeText.getText()+"RefModel"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				refClassText.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		return container;
		
	}
	
	protected void okPressed() {
		
		if(refCodeText.getText()==null||refNameText.getText()==null||refTypeText.getText()==null||refClassText.getText()==null){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.RefInfoDialog_7);
			return;
		}
		if(refCodeText.getText().trim().length()==0){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.RefInfoDialog_8);
			return;
		}
		if(refNameText.getText().trim().length()==0){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.RefInfoDialog_9);
			return;
		}
		if(refClassText.getText().trim().length()==0){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.RefInfoDialog_10);
			return;
		}		
		setRefCode(refCodeText.getText());
		setRefName(refNameText.getText());
		setRefType(refTypeText.getSelectionIndex());
		setRefClass(refClassText.getText());
		if(refClassText.getText().indexOf(".")<0){ //$NON-NLS-1$
			setRefClass("nc.ref.model."+refClassText.getText()); //$NON-NLS-1$
		}
		super.okPressed();
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public int getRefType() {
		return refType;
	}

	public void setRefType(int refType) {
		this.refType = refType;
	}

	public String getRefClass() {
		return refClass;
	}

	public void setRefClass(String refClass) {
		this.refClass = refClass;
	}
	
	
}
