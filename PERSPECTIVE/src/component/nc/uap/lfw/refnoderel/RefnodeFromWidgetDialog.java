package nc.uap.lfw.refnoderel;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.lang.M_refnoderel;

import org.eclipse.jface.dialogs.MessageDialog;
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

public class RefnodeFromWidgetDialog extends DialogWithTitle {

	private Table table = null;
	private TableViewer tv = null;
	private IRefNode refnode = null;
	
	public IRefNode getRefnode() {
		return refnode;
	}


	public void setRefnode(IRefNode refnode) {
		this.refnode = refnode;
	}


	public RefnodeFromWidgetDialog(Shell parentShell,  String title) {
		super(parentShell, title);
	}
	

	protected void okPressed() {
		TableItem[] items = table.getSelection();
		if(items != null && items.length > 0){
			TableItem item = items[0];
			IRefNode refnode = (IRefNode)item.getData();
			setRefnode(refnode);
			if(item != null){
				super.okPressed();
			}
		}else{
			MessageDialog.openConfirm(this.getShell(), M_refnoderel.RefnodeFromWidgetDialog_0, M_refnoderel.RefnodeFromWidgetDialog_1);
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
		new TableColumn(table,SWT.NONE).setText(M_refnoderel.RefnodeFromWidgetDialog_2);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		RefNodeContent ref = new RefNodeContent();
		tv.setContentProvider(ref);
		tv.setLabelProvider(ref);
		List<IRefNode> refNodeList = getRefNode();
		tv.setInput(refNodeList);
		return container;
	}
	
	private List<IRefNode> getRefNode(){
		List<IRefNode> list = new ArrayList<IRefNode>();
		LfwView widget = LFWPersTool.getCurrentWidget();
		IRefNode[] refNodes = widget.getViewModels().getRefNodes();
		for(IRefNode node : refNodes){
			list.add(node);
		}
		return list;
	}
}
	