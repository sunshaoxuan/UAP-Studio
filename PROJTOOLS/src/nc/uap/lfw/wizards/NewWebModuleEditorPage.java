/**
 * 
 */
package nc.uap.lfw.wizards;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author chouhl
 *
 */
public class NewWebModuleEditorPage extends WizardPage implements IPropertyChangeListener, IDialogFieldListener  {
	
	private SelectionButtonDialogField oldEditorRadio;
	
	private SelectionButtonDialogField newEditorRadio;
	
//	private SelectionButtonDialogField allEditorRadio;

	private String selectedValue = "OLD_VERSION";
	
	protected NewWebModuleEditorPage(String pageName) {
		super(pageName);
		setTitle("Create New LFW Project");
		setDescription("Specify the infomation for create lfw project");
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		partGroup(composite);
		setControl(composite);
	}

	public void partGroup(Composite parent){
		final Group group = new Group(parent, SWT.NONE);
		group.setLayout(initGridLayout(new GridLayout(3, false), true));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		oldEditorRadio = new SelectionButtonDialogField(SWT.RADIO);
		oldEditorRadio.setLabelText("6.0版本"); 
		oldEditorRadio.setDialogFieldListener(this);
		oldEditorRadio.setSelection(false);
		oldEditorRadio.doFillIntoGrid(group, 1);
		
		newEditorRadio = new SelectionButtonDialogField(SWT.RADIO);
		newEditorRadio.setLabelText("6.1版本"); 
		newEditorRadio.setDialogFieldListener(this);
		newEditorRadio.setSelection(true);
		newEditorRadio.doFillIntoGrid(group, 1);
		
//		allEditorRadio = new SelectionButtonDialogField(SWT.RADIO);
//		allEditorRadio.setLabelText("混合版本"); 
//		allEditorRadio.setDialogFieldListener(this);
//		allEditorRadio.setSelection(false);
//		allEditorRadio.doFillIntoGrid(group, 1);
		
	}
	
	protected GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth = 0;
			layout.marginHeight = 0;
		}
		return layout;
	}

	public void propertyChange(PropertyChangeEvent event) {
		
	}

	public void dialogFieldChanged(DialogField field) {
		if(oldEditorRadio.isSelected()){
			this.selectedValue = "OLD_VERSION";
		}else if(newEditorRadio.isSelected()){
			this.selectedValue = "NEW_VERSION";
		}else{
			this.selectedValue = "ALL_VERSION";
		}
	}

	public String getSelectedValue() {
		return selectedValue;
	}

}
