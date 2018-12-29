package nc.uap.lfw.component;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.lang.M_application;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EditComponentDialog extends DialogWithTitle{

	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	
	private Text packageText;
	
	private String id;
	
	private String name;
	
	private String packName;
	
	private LfwUIComponent component;
	
	public EditComponentDialog(LfwUIComponent component, String title) {
		super(null, title);
		this.component = component;		
	}
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label packLabel = new Label(container, SWT.NONE);
		packLabel.setText(M_application.EditComponentDialog_0);
		packageText = new Text(container, SWT.NONE);
		packageText.setLayoutData(new GridData(220,20));
		packageText.setText(component.getPack()); 
		setPackName(component.getPack());
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText("ID"); //$NON-NLS-1$
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220,20));
		idText.setText(component.getId()); 
		setId(component.getId());
		//名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_application.EditComponentDialog_2);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220,20));
		nameText.setText(component.getName()); 
		setName(component.getName());
		
		return container;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	
	protected void okPressed() {
		if(idText.getText().trim().length()==0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_12, M_application.CreateApplicationDialog_13);
			return;
		}
		if(!id.equals(idText.getText().trim())){
			MessageDialog.openWarning(null, M_application.EditComponentDialog_3, M_application.EditComponentDialog_4);
			return;
		}
		if(nameText.getText().trim().length()==0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_15, M_application.CreateApplicationDialog_16);
			return;
		}
		if(!name.equals(nameText.getText().trim())&&id.equals(LfwUIComponent.ANNOYUICOMPONENT)){
			MessageDialog.openWarning(null, M_application.EditComponentDialog_3, M_application.EditComponentDialog_1);
			return;
		}
		setName(nameText.getText().trim());
		
		if(!packName.equals(packageText.getText().trim())){
			MessageDialog.openWarning(null, M_application.EditComponentDialog_3, M_application.EditComponentDialog_5);
			return;
		}
		super.okPressed();
	}
}
