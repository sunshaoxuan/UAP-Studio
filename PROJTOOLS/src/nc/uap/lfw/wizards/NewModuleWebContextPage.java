package nc.uap.lfw.wizards;

import java.util.regex.Pattern;

import nc.uap.lfw.lang.M_wizards;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class NewModuleWebContextPage extends WizardPage implements
		IPropertyChangeListener, IDialogFieldListener {

	//主项目单选按钮
	private SelectionButtonDialogField fMainRadio;
	
	//从属项目单选按钮
	private SelectionButtonDialogField fPartRadio;
	
	//主项目访问器路径
	private StringDialogField mainPath;
	
	//从属项目访问器路径
	private StringDialogField partPath;

	public StringDialogField getMainPath() {
		return mainPath;
	}

	public void setMainPath(StringDialogField mainPath) {
		this.mainPath = mainPath;
	}

	public StringDialogField getPartPath() {
		return partPath;
	}

	public SelectionButtonDialogField getMainRadio() {
		return fMainRadio;
	}

	public SelectionButtonDialogField getPartRadio() {
		return fPartRadio;
	}

	protected NewModuleWebContextPage(String pageName) {
		super(pageName);
		setTitle(M_wizards.NewModuleWebContextPage_0);
		setDescription(M_wizards.NewModuleWebContextPage_1);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		PartGroup(composite);
		setControl(composite);
	}

	public Group PartGroup(Composite composite) {
		return PartGroup(composite, true, null);
	}
	/**
	 * 主项目与从属项目组
	 * 
	 * @param composite
	 * @return
	 */
	public Group PartGroup(Composite composite, boolean mainProject, String name) {
		final int numColumns = 2;
		final Group group = new Group(composite, SWT.NONE);
		group
				.setLayout(initGridLayout(new GridLayout(numColumns, false),
						true));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMainRadio = new SelectionButtonDialogField(SWT.RADIO);
		fMainRadio.setLabelText(M_wizards.NewModuleWebContextPage_2); 
		fMainRadio.setDialogFieldListener(this);
		mainPath = new StringDialogField();
		mainPath.setLabelText(M_wizards.NewModuleWebContextPage_3);
		mainPath.setDialogFieldListener(this);
		fMainRadio.attachDialogField(mainPath);

		fPartRadio = new SelectionButtonDialogField(SWT.RADIO);
		fPartRadio.setLabelText(M_wizards.NewModuleWebContextPage_4); 
		fPartRadio.setDialogFieldListener(this);
		partPath = new StringDialogField();
		partPath.setLabelText(M_wizards.NewModuleWebContextPage_5);
		if(name != null)
			partPath.setText(name);
		partPath.setDialogFieldListener(this);
		fPartRadio.attachDialogField(partPath);
		fMainRadio.setSelection(mainProject);
		fPartRadio.setSelection(!mainProject);
		fMainRadio.doFillIntoGrid(group, numColumns);
		mainPath.doFillIntoGrid(group, numColumns);
		fPartRadio.doFillIntoGrid(group, numColumns);
		partPath.doFillIntoGrid(group, numColumns);
		LayoutUtil.setHorizontalGrabbing(mainPath.getTextControl(null));
		LayoutUtil.setHorizontalGrabbing(partPath.getTextControl(null));
		return group;
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

	/**
	 * 校验 主项目时mainPath不能为空，不能有特殊符号，
	 * 从属项目时partPaht不能为空，不能有特殊符号。
	 */
	private void validate() {
		if (fMainRadio.isSelected() && mainPath != null) {
			if (mainPath.getText().equals("")) { //$NON-NLS-1$
				setPageComplete(false);
				return;
			}
			Pattern pattern = Pattern.compile("[!@~#$%^&*-+]+"); //$NON-NLS-1$
			if (pattern.matcher(mainPath.getText().trim()).find()) {
				setPageComplete(false);
				return;
			}
		}
		if (fPartRadio.isSelected() && partPath != null) {
			if (partPath.getText().equals("")) { //$NON-NLS-1$
				setPageComplete(false);
				return;
			}
			Pattern pattern = Pattern.compile("[!@~#$%^&*-+]+"); //$NON-NLS-1$
			if (pattern.matcher(partPath.getText().trim()).find()) {
				setPageComplete(false);
				return;
			}
		}
		setPageComplete(true);
	}

	public void propertyChange(PropertyChangeEvent event) {
		validate();
	}

	public void dialogFieldChanged(DialogField field) {
		validate();
	}
}