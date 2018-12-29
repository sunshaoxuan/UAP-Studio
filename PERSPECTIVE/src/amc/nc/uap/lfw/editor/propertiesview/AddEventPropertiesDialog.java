/**
 * 
 */
package nc.uap.lfw.editor.propertiesview;

import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.editor.TableViewContentProvider;
import nc.uap.lfw.perspective.listener.EventTableViewLabelProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author chouhl
 *
 */
public class AddEventPropertiesDialog extends DialogWithTitle {

	private List<EventConf> acceptEventList = null;
	
	private EventConf eventConf = null;
	
	private static String[] IS_SERVER = {M_editor.AddEventPropertiesDialog_0,M_editor.AddEventPropertiesDialog_1};
	
	public AddEventPropertiesDialog(List<EventConf> acceptEventList) {
		super(null, WEBPersConstants.ADD);
		this.acceptEventList = acceptEventList;
	}
	
	public AddEventPropertiesDialog(Shell parentShell, String title, List<EventConf> acceptEventList) {
		super(parentShell, title);
		this.acceptEventList = acceptEventList;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, false));

		ViewForm vf = new ViewForm(container, SWT.NONE);
		vf.setLayoutData(gd);
		TableViewer tv = new TableViewer(vf, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		
		Table table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		createColumn(table, layout, 80, SWT.NONE, M_editor.AddEventPropertiesDialog_2);
		createColumn(table, layout, 80, SWT.NONE, M_editor.AddEventPropertiesDialog_3);
		createColumn(table, layout, 70, SWT.CENTER, M_editor.AddEventPropertiesDialog_4);
		createColumn(table, layout, 50, SWT.CENTER, M_editor.AddEventPropertiesDialog_5);
		createColumn(table, layout, 80, SWT.NONE, M_editor.AddEventPropertiesDialog_6);
		
		table.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){}
	
			public void widgetSelected(SelectionEvent e){
				setEventConf((EventConf)e.item.getData());
			}
			
		});
		
		table.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				okPressed();
			}
			
			public void mouseDown(MouseEvent e){}
			
			public void mouseUp(MouseEvent e){}
			
		});
		
		tv.setContentProvider(new TableViewContentProvider());
		tv.setLabelProvider(new EventTableViewLabelProvider(false));
		
		tv.setInput(acceptEventList);

		tv.setColumnProperties(new String[]{"name", "params", "isOnServer", "submitRule", "script"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		// …Ë÷√±‡º≠¡–
		CellEditor[] cellEditor = new CellEditor[5];
		cellEditor[0] = null;
		cellEditor[1] = null;
		cellEditor[2] = new ComboBoxCellEditor(tv.getTable(), IS_SERVER, SWT.READ_ONLY);
		cellEditor[3] = null;//new SubmitRuleCellEditor(tv.getTable(), tv, jsListener);
		cellEditor[4] = null;//new ExtendParameterCellEidtor(tv.getTable(), tv, jsListener);
		
		tv.setCellEditors(cellEditor);
		vf.setContent(tv.getControl());
		return container;
	}
	
	@Override
	protected void okPressed() {
		if(eventConf == null){
			MessageDialog.openInformation(null, M_editor.AddEventPropertiesDialog_7, M_editor.AddEventPropertiesDialog_8);
			return;
		}
		super.okPressed();
	}
	
	private void createColumn(Table table, TableLayout layout, int width,
			int align, String text) {
		layout.addColumnData(new ColumnWeightData(width));
		new TableColumn(table, align).setText(text);
	}

	public EventConf getEventConf() {
		return eventConf;
	}

	public void setEventConf(EventConf eventConf) {
		this.eventConf = eventConf;
	}
	
}
