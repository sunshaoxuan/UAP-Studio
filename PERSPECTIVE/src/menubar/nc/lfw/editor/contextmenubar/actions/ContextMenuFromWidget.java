package nc.lfw.editor.contextmenubar.actions;

import java.util.Arrays;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 关联右键菜单对话框
 * @author zhangxya
 *
 */
public class ContextMenuFromWidget  extends TitleAreaDialog {

	private Table table = null;
	private TableViewer tv = null;
	private ContextMenuComp contextMenuComp;
	
	public ContextMenuComp getContextMenuComp() {
		return contextMenuComp;
	}



	public void setContextMenuComp(ContextMenuComp contextMenuComp) {
		this.contextMenuComp = contextMenuComp;
	}



	public ContextMenuFromWidget(Shell parentShell) {
		super(parentShell);
	}
	
	
	
	protected void okPressed() {
		TableItem[] items = table.getSelection();
		if(items != null && items.length > 0){
			TableItem item = items[0];
			ContextMenuComp contextMenu = (ContextMenuComp)item.getData();
			setContextMenuComp(contextMenu);
			if(item != null){
				super.okPressed();
			}
		}else{
			MessageDialog.openConfirm(this.getShell(), M_menubar.ContextMenuFromWidget_0, M_menubar.ContextMenuFromWidget_1);
			table.setFocus();
		}
	}
	
	protected Point getInitialSize() {
		return new Point(350,500); 
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		tv = new TableViewer(container, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		table = tv.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(100));
		new TableColumn(table,SWT.NONE).setText(M_menubar.ContextMenuFromWidget_2);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		ContextMenuContentProvider contextProvider = new ContextMenuContentProvider();
		tv.setContentProvider(contextProvider);
		tv.setLabelProvider(contextProvider);
		setTitle(M_menubar.ContextMenuFromWidget_3); 
		setMessage(M_menubar.ContextMenuFromWidget_4);
		ContextMenuComp[] contexts = LFWPersTool.getCurrentWidget().getViewMenus().getContextMenus();
		tv.setInput(Arrays.asList(contexts));
		return container;
	}
}
	