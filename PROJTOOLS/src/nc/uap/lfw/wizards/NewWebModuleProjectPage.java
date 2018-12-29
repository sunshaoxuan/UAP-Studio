package nc.uap.lfw.wizards;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;

/**
 *向导主页面 
 * 
 * @since 1.6
 */
public class NewWebModuleProjectPage extends WizardPage {
	private final class NameGroup extends Observable implements
			IDialogFieldListener {
		protected final StringDialogField fNameField;

		public NameGroup(Composite composite, String initialName) {
			final Composite nameComposite = new Composite(composite, SWT.NONE);
			nameComposite.setFont(composite.getFont());
			nameComposite.setLayout(initGridLayout(new GridLayout(2, false),
					false));
			nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fNameField = new StringDialogField();
			fNameField.setLabelText(M_wizards.NewWebModuleProjectPage_0);
			setName(initialName);
			fNameField.setDialogFieldListener(this);
			fNameField.doFillIntoGrid(nameComposite, 2);
			LayoutUtil.setHorizontalGrabbing(fNameField.getTextControl(null));
		}

		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		public String getName() {
			return fNameField.getText().trim();
		}

		public void postSetFocus() {
			fNameField.postSetFocusOnDialogField(getShell().getDisplay());
		}

		public void setName(String name) {
			fNameField.setText(name);
		}

		public void dialogFieldChanged(DialogField field) {
			validatePage();
			fireEvent();
		}
	}

	private final class LocationGroup extends Observable implements Observer,
			IStringButtonAdapter, IDialogFieldListener {
		protected final SelectionButtonDialogField fWorkspaceRadio;
		protected final SelectionButtonDialogField fExternalRadio;
		protected final StringButtonDialogField fLocation;
		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC = JavaUI.ID_PLUGIN
				+ ".last.external.project"; //$NON-NLS-1$

		public LocationGroup(Composite composite) {
			final int numColumns = 3;
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(initGridLayout(new GridLayout(numColumns, false),
					true));
			group.setText("Location"); //$NON-NLS-1$
			fWorkspaceRadio = new SelectionButtonDialogField(SWT.RADIO);
			fWorkspaceRadio.setLabelText("Create project in &workspace"); //$NON-NLS-1$
			fExternalRadio = new SelectionButtonDialogField(SWT.RADIO);
			fExternalRadio.setLabelText("Create project at e&xternal location"); //$NON-NLS-1$
			fLocation = new StringButtonDialogField(this);
			fLocation.setLabelText(M_wizards.NewWebModuleProjectPage_1);
			fLocation.setButtonLabel("B&rowse..."); //$NON-NLS-1$
			fExternalRadio.attachDialogField(fLocation);
			fWorkspaceRadio.setSelection(true);
			fExternalRadio.setSelection(false);
			fWorkspaceRadio.doFillIntoGrid(group, numColumns);
			fExternalRadio.doFillIntoGrid(group, numColumns);
			fLocation.doFillIntoGrid(group, numColumns);
			fWorkspaceRadio.setDialogFieldListener(this);
			fLocation.setDialogFieldListener(this);
			LayoutUtil.setHorizontalGrabbing(fLocation.getTextControl(null));
		}

		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		protected String getDefaultPath(String name) {
			final IPath path = Platform.getLocation().append(name);
			return path.toOSString();
		}

		public void update(Observable o, Object arg) {
			if (isInWorkspace()) {
				fLocation.setText(getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		public IPath getLocation() {
			if (isInWorkspace()) {
				return Platform.getLocation();
			}
			return new Path(fLocation.getText().trim());
		}

		public boolean isInWorkspace() {
			return fWorkspaceRadio.isSelected();
		}

		public void changeControlPressed(DialogField field) {
			final DirectoryDialog dialog = new DirectoryDialog(getShell());
			dialog.setMessage(M_wizards.NewWebModuleProjectPage_2);
			String directoryName = fLocation.getText().trim();
			if (directoryName.length() == 0) {
				String prevLocation = JavaPlugin.getDefault()
						.getDialogSettings().get(DIALOGSTORE_LAST_EXTERNAL_LOC);
				if (prevLocation != null) {
					directoryName = prevLocation;
				}
			}
			if (directoryName.length() > 0) {
				final File path = new File(directoryName);
				if (path.exists())
					dialog.setFilterPath(new Path(directoryName).toOSString());
			}
			final String selectedDirectory = dialog.open();
			if (selectedDirectory != null) {
				fLocation.setText(selectedDirectory);
				JavaPlugin.getDefault().getDialogSettings().put(
						DIALOGSTORE_LAST_EXTERNAL_LOC, selectedDirectory);
			}
		}

		public void dialogFieldChanged(DialogField field) {
			validatePage();
			fireEvent();
		}
	}

	private final class ModuleInfoGroup extends Observable implements
			IDialogFieldListener {
		protected final StringDialogField fModuleName;
		protected final StringDialogField fModuleConfig;

		public ModuleInfoGroup(Composite composite) {
			final int numColumns = 4;
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(initGridLayout(new GridLayout(numColumns, false),
					true));
			group.setText(M_wizards.NewWebModuleProjectPage_3);
			fModuleName = createDialogField(M_wizards.NewWebModuleProjectPage_4,
					fInitialName, numColumns, group);
			fModuleConfig = createDialogField(M_wizards.NewWebModuleProjectPage_5,
					"module.xml", numColumns, group); //$NON-NLS-1$
		}

		private StringDialogField createDialogField(String label,
				String defValue, final int numColumns, final Group group) {
			StringDialogField field = new StringDialogField();
			field.setLabelText(label);
			field.setText(defValue);
			field.setDialogFieldListener(this);
			field.doFillIntoGrid(group, numColumns);
			LayoutUtil.setHorizontalGrabbing(field.getTextControl(null));
			return field;
		}

		public String getModuleName() {
			return fModuleName.getText().trim();
		}

		public String getModuleConfig() {
			return fModuleConfig.getText().trim();
		}

		public void dialogFieldChanged(DialogField field) {
			validatePage();
		}
	}

	private NameGroup fNameGroup;
	private LocationGroup fLocationGroup;
	private ModuleInfoGroup fModuleInfoGroup;
	private String fInitialName;
	private boolean fInited = false;

	/* 隐藏下一页 */
	private boolean hideNext = true;

	public boolean isHideNext() {
		return hideNext;
	}

	public void setHideNext(boolean hideNext) {
		this.hideNext = hideNext;
	}

	public NewWebModuleProjectPage(String pageName) {
		super(pageName);
		setTitle(M_wizards.NewWebModuleProjectPage_6);
		setDescription(M_wizards.NewWebModuleProjectPage_7); //
		fInitialName = ""; //$NON-NLS-1$
	}

	public IWizardPage getNextPage() {
		if (hideNext)
			return null;
		NewModuleWebContextPage nextpage = (NewModuleWebContextPage) super
				.getNextPage();
		nextpage.getMainPath().setText(getModuleName());
		return nextpage;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		fNameGroup = new NameGroup(composite, fInitialName);
		fLocationGroup = new LocationGroup(composite);
		fModuleInfoGroup = new ModuleInfoGroup(composite);
		fNameGroup.addObserver(fLocationGroup);
		fNameGroup.notifyObservers();
		setControl(composite);
		fInited = true;
		Dialog.applyDialogFont(composite);
		setPageComplete(false);
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

	public IPath getLocationPath() {
		return fLocationGroup.getLocation();
	}

	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(
				fNameGroup.getName());
	}

	public boolean isInWorkspace() {
		return fLocationGroup.isInWorkspace();
	}

	public String getProjectName() {
		return fNameGroup.getName();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			fNameGroup.postSetFocus();
		}
	}

	public String getModuleName() {
		return fModuleInfoGroup.getModuleName();
	}

	public String getModuleConfig() {
		return fModuleInfoGroup.getModuleConfig();
	}

	protected boolean validatePage() {
		if (fInited) {
			boolean vlidProjectName = validateName(getProjectName(),
					M_wizards.NewWebModuleProjectPage_8, null);
			if (!vlidProjectName) {
				setPageComplete(false);
				return false;
			}
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for(IProject proj:projects){
				if(proj.getName().equalsIgnoreCase(fNameGroup.getName())){
					setErrorMessage(M_wizards.NewWebModuleProjectPage_14);
					setPageComplete(false);
					return false;
				}					
			}
			IPath path = new Path(""); //$NON-NLS-1$
			if (!path.isValidPath(getLocationPath().toString())) {
				setErrorMessage("Invalid project contents directory"); //$NON-NLS-1$
				setPageComplete(false);
				return false;
			}
			boolean validModuleName = validateName(getModuleName(),
					M_wizards.NewWebModuleProjectPage_9,
					M_wizards.NewWebModuleProjectPage_10);
			if (!validModuleName) {
				setPageComplete(false);
				return false;
			}
			String moduleConfig = getModuleConfig();
			if (moduleConfig != null) {
				if (!"module.xml".equals(moduleConfig)) { //$NON-NLS-1$
					String extension = ProjCoreUtility
							.getExtension(moduleConfig);
					if (!"module".equals(extension)) { //$NON-NLS-1$
						setPageComplete(false);
						setErrorMessage(M_wizards.NewWebModuleProjectPage_11);
						return false;
					} else {
						boolean validConfigName = validateName(getModuleName(),
								M_wizards.NewWebModuleProjectPage_12,
								M_wizards.NewWebModuleProjectPage_13);
						if (!validConfigName) {
							setPageComplete(false);
							return false;
						}
					}
				}
			}
			setErrorMessage(null);
			setMessage(null);
			setPageComplete(true);
			return true;
		}
		setPageComplete(false);
		return false;
	}

	// public int getPriority() {
	// return fModuleInfoGroup.getPriority();
	// }
	private boolean validateName(String name, String emptyMsg, String errorMsg) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (name.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			setMessage(emptyMsg);
			return false;
		}
		IStatus nameStatus = workspace.validateName(name, 0);
		if (!nameStatus.isOK()) {
			if (errorMsg == null)
				setErrorMessage(nameStatus.getMessage());
			else
				setErrorMessage(errorMsg);
			return false;
		}
		setErrorMessage(null);
		setMessage(null);
		return true;
	}
}
