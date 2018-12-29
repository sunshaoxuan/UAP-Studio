package nc.lfw.editor.contextmenubar.actions;

import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 创建contextMenu子菜单
 * @author zhangxya
 *
 */
public class CreateContextMenuDialog  extends TitleAreaDialog {

	private Text idText;
	private Combo levelCombo;
	private String subMenuId;
	private int level = 1;
	
	public CreateContextMenuDialog(Shell parentShell) {
		super(parentShell);

	}

	protected void okPressed() {
		subMenuId = idText.getText().trim();
		level = Integer.parseInt((String) levelCombo.getData(levelCombo.getText()));
		if (subMenuId == null || "".equals(subMenuId)) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_menubar.CreateContextMenuDialog_0, M_menubar.CreateContextMenuDialog_1);
			idText.setFocus();
			return;
		}
		//TODO 唯一性校验
//		List<LfwWidgetConf> list = graph.getPagemeta().getWidgetConfList();
//		for (int i = 0, n = list.size(); i < n; i++) {
//			if (list.get(i).getId().equals(subMenuId)) {
//				MessageDialog.openWarning(this.getShell(), "提示", "该子菜单已存在！");
//				idText.setFocus();
//				return;
//			}
//		}
		super.okPressed();
	}

	protected Point getInitialSize() {
		return new Point(350, 250);
	}

	protected Control createDialogArea(Composite parent) {
		setTitle(M_menubar.CreateContextMenuDialog_2);
		setMessage(M_menubar.CreateContextMenuDialog_3, IMessageProvider.NONE);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("ID:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(200, 3));
		
		new Label(container, SWT.NONE).setText(M_menubar.CreateContextMenuDialog_4);
		levelCombo = new Combo(container, SWT.READ_ONLY);
		levelCombo.setLayoutData(createGridData(100, 1));
		levelCombo.add(M_menubar.CreateContextMenuDialog_5);
		levelCombo.setData(M_menubar.CreateContextMenuDialog_5, "1"); //$NON-NLS-2$
		levelCombo.add(M_menubar.CreateContextMenuDialog_6);
		levelCombo.setData(M_menubar.CreateContextMenuDialog_6, "2"); //$NON-NLS-2$
		levelCombo.add(M_menubar.CreateContextMenuDialog_7);
		levelCombo.setData(M_menubar.CreateContextMenuDialog_7, "3"); //$NON-NLS-2$
		levelCombo.select(0);
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	public Text getIdText() {
		return idText;
	}

	public String getSubMenuId() {
		return subMenuId;
	}

	public int getLevel() {
		return level;
	}

}
