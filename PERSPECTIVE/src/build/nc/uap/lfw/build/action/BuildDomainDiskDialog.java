package nc.uap.lfw.build.action;

import java.io.File;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.lang.M_build;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class BuildDomainDiskDialog extends DialogWithTitle{

	public BuildDomainDiskDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	private LocationGroup locationGroup;
	protected Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(500, 200));
		composite.setLocation(150, 150);
		locationGroup = new LocationGroup(composite);
		return composite;
	}
	private StringButtonDialogField fLocation = null;
	
	private String type = null;

	/**
	 * 出盘位置选择栏
	 * @author qinjianc
	 *
	 */
	private final class LocationGroup implements IStringButtonAdapter {

		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC = JavaUI.ID_PLUGIN
				+ ".last.external.project";  //$NON-NLS-1$

		public LocationGroup(Composite composite) {
			Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout layout = new GridLayout();
			layout.verticalSpacing = 50;
			// // layout.marginRight = 20;
			layout.marginTop = 50;
			// // layout.marginLeft = 20;
			layout.numColumns = 4;
			layout.makeColumnsEqualWidth = false;
			group.setLayout(layout);
			// group.setLayout(new GridLayout(6, true));
//			GridData gd = createGridData(100, 5);
			group.setText(M_build.BuildDomainDiskDialog_2); 
			// Label label = new Label(group, SWT.NONE);
			// label.setText("安装盘发布位置：");
			// Text diskLocation = new Text(group,SWT.NONE);
			// diskLocation.setLayoutData(gd);
			fLocation = new StringButtonDialogField(this);
			fLocation.setLabelText(M_build.BuildConfigEditor_6);
			fLocation.setButtonLabel("B&rowse");  //$NON-NLS-1$
			fLocation.doFillIntoGrid(group, 4);	
			Button compileBtn = new Button(group, SWT.RADIO);
			compileBtn.setText(M_build.BuildDomainDiskDialog_0);
			compileBtn.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					setType("compile");  //$NON-NLS-1$
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			Button diskBtn = new Button(group, SWT.RADIO);
			diskBtn.setText(M_build.BuildDomainDiskDialog_1);
			diskBtn.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					setType("disk");  //$NON-NLS-1$
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});

		}
		public String getLocation(){
			return fLocation.getText();
		}

		@Override
		public void changeControlPressed(DialogField field) {

			final DirectoryDialog dialog = new DirectoryDialog(MainPlugin
					.getDefault().getWorkbench().getActiveWorkbenchWindow()
					.getShell());
			dialog.setMessage(M_build.BuildConfigEditor_11);
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
				JavaPlugin.getDefault().getDialogSettings()
						.put(DIALOGSTORE_LAST_EXTERNAL_LOC, selectedDirectory);
			}
		}
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 20);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
	public String getDiskLocation(){
		return locationGroup.getLocation();
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	protected void okPressed() {
		if(getDiskLocation()==""){  //$NON-NLS-1$
			MessageDialog.openError(null, M_build.BuildConfigEditor_8, M_build.BuildConfigEditor_10);
			return;
		}
		super.okPressed();
	}

}
