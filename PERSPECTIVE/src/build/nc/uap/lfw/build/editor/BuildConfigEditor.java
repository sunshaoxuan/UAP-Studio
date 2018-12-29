package nc.uap.lfw.build.editor;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.action.CreateDiskAction;
import nc.uap.lfw.lang.M_build;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * 构造配置编辑器
 * 
 * @author qinjianc
 * 
 */
public class BuildConfigEditor extends MultiPageEditorPart {


	private DbTypeGroup dbTypeGroup = null;

	private LocationGroup locationGroup = null;
	
	private SetupInfoGroup setupInfoGroup = null;

	Combo dbType;
	Text dbUrl;
	Text dbName;
	Text userName;
	Text Passwd;

	/**
	 * 数据库配置栏
	 * @author qinjianc
	 *
	 */
	private final class DbTypeGroup {

		public DbTypeGroup(Composite composite) {
			Group group = new Group(composite, SWT.NONE);
			GridLayout layout = new GridLayout();
			// layout.verticalSpacing = 60;
			layout.marginTop = 50;
			layout.marginLeft = 20;
			layout.numColumns = 2;
			group.setLayout(layout);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			// group.setLayout(new GridLayout(2, false));
			GridData gd = createGridData(200, 1);
			group.setText("关联数据库配置"); //$NON-NLS-1$

			Label label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_0);
			dbType = new Combo(group, SWT.NONE);
			String[] fAvailableIntroIds = { "Sql Server 2008", "ORACLE 11g", //$NON-NLS-1$ //$NON-NLS-2$
					"DB2" }; //$NON-NLS-1$
			dbType.setItems(fAvailableIntroIds);
			dbType.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_1);
			dbUrl = new Text(group, SWT.NONE);
			dbUrl.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_2);
			dbName = new Text(group, SWT.NONE);
			dbName.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_3);
			userName = new Text(group, SWT.NONE);
			userName.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_4);
			Passwd = new Text(group, SWT.PASSWORD);
			Passwd.setLayoutData(gd);
			Button submitBtn = new Button(group, SWT.PUSH);
			submitBtn.setText(M_build.BuildConfigEditor_5);
			submitBtn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					TextEditor editor = (TextEditor) getEditor(1);
					IDocument document = editor.getDocumentProvider()
							.getDocument(editor.getEditorInput());
					String content = document.get();
					// content.split("\n")
					// if(content.trim()==""){
					StringBuffer newContent = new StringBuffer();
					newContent.append("DatabaseType = " + dbType.getText() //$NON-NLS-1$
							+ "\n"); //$NON-NLS-1$
					newContent.append("DatabaseURL = " + dbUrl.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					newContent.append("DatabaseName = " + dbName.getText() //$NON-NLS-1$
							+ "\n"); //$NON-NLS-1$
					newContent.append("Username = " + userName.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					newContent.append("Password = " + Passwd.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					document.set(newContent.toString());
					editor.doSave(null);
					// }
				}
			});

		}
	}
	private final class SetupInfoGroup {
		public SetupInfoGroup(Composite composite) {
			Group group = new Group(composite, SWT.NONE);
			GridLayout layout = new GridLayout();
			// layout.verticalSpacing = 60;
			layout.marginTop = 50;
			layout.marginLeft = 20;
			layout.numColumns = 2;
			group.setLayout(layout);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			// group.setLayout(new GridLayout(2, false));
			GridData gd = createGridData(200, 1);
			group.setText("关联数据库配置"); //$NON-NLS-1$

			Label label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_1);
			dbUrl = new Text(group, SWT.NONE);
			dbUrl.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_2);
			dbName = new Text(group, SWT.NONE);
			dbName.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_3);
			userName = new Text(group, SWT.NONE);
			userName.setLayoutData(gd);
			label = new Label(group, SWT.NONE);
			label.setText(M_build.BuildConfigEditor_4);
			Passwd = new Text(group, SWT.PASSWORD);
			Passwd.setLayoutData(gd);
			Button submitBtn = new Button(group, SWT.PUSH);
			submitBtn.setText(M_build.BuildConfigEditor_5);
			submitBtn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					TextEditor editor = (TextEditor) getEditor(1);
					IDocument document = editor.getDocumentProvider()
							.getDocument(editor.getEditorInput());
					String content = document.get();
					// content.split("\n")
					// if(content.trim()==""){
					StringBuffer newContent = new StringBuffer();
					newContent.append("DatabaseType = " + dbType.getText() //$NON-NLS-1$
							+ "\n"); //$NON-NLS-1$
					newContent.append("DatabaseURL = " + dbUrl.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					newContent.append("DatabaseName = " + dbName.getText() //$NON-NLS-1$
							+ "\n"); //$NON-NLS-1$
					newContent.append("Username = " + userName.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					newContent.append("Password = " + Passwd.getText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					document.set(newContent.toString());
					editor.doSave(null);
					// }
				}
			});

		}
	}

	private StringButtonDialogField fLocation = null;

	/**
	 * 出盘位置选择栏
	 * @author qinjianc
	 *
	 */
	private final class LocationGroup implements IStringButtonAdapter {

		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC = JavaUI.ID_PLUGIN
				+ ".last.external.project"; //$NON-NLS-1$

		public LocationGroup(Composite composite) {
			Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout layout = new GridLayout();
			layout.verticalSpacing = 50;
			// // layout.marginRight = 20;
			layout.marginTop = 50;
			// // layout.marginLeft = 20;
			layout.numColumns = 5;
			layout.makeColumnsEqualWidth = true;
			group.setLayout(layout);
			// group.setLayout(new GridLayout(6, true));
			GridData gd = createGridData(100, 5);
			group.setText("安装盘配置"); //$NON-NLS-1$
			// Label label = new Label(group, SWT.NONE);
			// label.setText("安装盘发布位置：");
			// Text diskLocation = new Text(group,SWT.NONE);
			// diskLocation.setLayoutData(gd);
			fLocation = new StringButtonDialogField(this);
			fLocation.setLabelText(M_build.BuildConfigEditor_6);
			fLocation.setButtonLabel("B&rowse"); //$NON-NLS-1$
			fLocation.doFillIntoGrid(group, 4);
			// fLocation.gridDataForButton
			Button submitBtn = new Button(group, SWT.PUSH);
			submitBtn.setText(M_build.BuildConfigEditor_7);
			gd.horizontalAlignment = GridData.CENTER;
			submitBtn.setLayoutData(gd);
			submitBtn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {					
					IProject project= LFWPersTool.getCurrentProject();
					if(project==null){
						MessageDialog.openError(null, M_build.BuildConfigEditor_8, M_build.BuildConfigEditor_9);
						return;
					}
					CreateDiskAction diskAction = new CreateDiskAction();
					if(fLocation.getText()==""){ //$NON-NLS-1$
						MessageDialog.openError(null, M_build.BuildConfigEditor_8, M_build.BuildConfigEditor_10);
						return;
					}
					diskAction.setLocation(fLocation.getText());
//					diskAction.setDbType(dbType.getText());
//					diskAction.setDbName(dbName.getText());
//					diskAction.setDbUrl(dbUrl.getText());
//					diskAction.setUserName(userName.getText());
//					diskAction.setPasswd(Passwd.getText());
					diskAction.run(null);
//					
				}
			});

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

	@Override
	protected void createPages() {
		FileEditorInput fInput = (FileEditorInput) getEditorInput();
		// h file
		IFile hFile = fInput.getFile();
		try {
			FileEditorInput fed = new FileEditorInput(hFile);
			// IEditorPart htmlEditorPart =
			// makeDefaultEditorFor(hFile.getName());

			Composite container = new Composite(getContainer(), SWT.NONE);
			container.setLayout(new GridLayout(2, true));
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
//			setupInfoGroup = new SetupInfoGroup(container);
			locationGroup = new LocationGroup(container);

			int index = addPage(container);
			// int index = addPage(new ButtonEditor(), fed);
			setPageText(index, M_build.BuildConfigEditor_12);
//			setPageImage(index, fed.getImageDescriptor().createImage());

			index = addPage(new TextEditor(), fed);
			setPageText(index, hFile.getName());
//			setPageImage(index, fed.getImageDescriptor().createImage());

			TextEditor editor = (TextEditor) getEditor(1);
			IDocument document = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
			String content = document.get();

			String line[] = content.split("\n"); //$NON-NLS-1$
			if(content!=null&&!content.equals("")){ //$NON-NLS-1$
				dbType.setText(line[0].substring(line[0].indexOf("=") + 1).trim()); //$NON-NLS-1$
				dbUrl.setText(line[1].substring(line[1].indexOf("=") + 1).trim()); //$NON-NLS-1$
				dbName.setText(line[2].substring(line[2].indexOf("=") + 1).trim()); //$NON-NLS-1$
				userName.setText(line[3].substring(line[3].indexOf("=") + 1).trim()); //$NON-NLS-1$
				Passwd.setText(line[4].substring(line[4].indexOf("=") + 1).trim()); //$NON-NLS-1$
			}			
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}

	}


	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		getActiveEditor().doSave(monitor);

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 20);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	
}
