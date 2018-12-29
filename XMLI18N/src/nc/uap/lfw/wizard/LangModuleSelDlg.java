package nc.uap.lfw.wizard;

import nc.uap.lfw.tool.ImageFactory;
import nc.uap.lfw.tool.ProjConstants;

import org.eclipse.jdt.internal.ui.refactoring.nls.MultiStateCellEditor;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

/**
 * 多语资源类别对话框
 * 
 * @author dingrf
 *
 */
public class LangModuleSelDlg extends TitleAreaDialog{
	
	/**
	 * 表格的LabelProvider
	 */
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider{

		public Image getColumnImage(Object element, int columnIndex){
			if (element instanceof String[]){
				String objs[] = (String[])element;
				if (columnIndex == 0){
					int i = Integer.parseInt(objs[0]);
					return ImageFactory.getCheckedImage(i != 0);
				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex){
			String columnText = "";
			if (element instanceof String[]){
				String objs[] = (String[])element;
				if (columnIndex == 1)
					columnText = objs[1];
			}
			return columnText;
		}
	}

	private class TableModifier implements ICellModifier {

		public boolean canModify(Object element, String property){
			return (element instanceof String[]) && LangModuleSelDlg.colNames[0].equals(property);
		}

		public Object getValue(Object element, String property){
			if (element instanceof String[]){
				String objs[] = (String[])element;
				if (LangModuleSelDlg.colNames[0].equals(property))
					return Integer.valueOf(Integer.parseInt(objs[0]));
				if (LangModuleSelDlg.colNames[1].equals(property))
					return objs[1];
			}
			return "";
		}

		public void modify(Object element, String property, Object value){
			if (element instanceof TableItem){
				Object o = ((TableItem)element).getData();
				if (o instanceof String[]){
					String objs[] = (String[])o;
					if (LangModuleSelDlg.colNames[0].equals(property))
						objs[0] = value.toString();
					tableViewer.refresh(o);
					if (isSingleSel && "1".equals(objs[0])){
						int count = input != null ? input.length : 0;
						for (int i = 0; i < count; i++){
							String ele[] = input[i];
							if (ele[0].equals("1") && !(ele==objs)){
								ele[0] = "0";
								tableViewer.refresh(ele);
							}
						}

					}
				}
			}
		}
	}


	private static final String colNames[] = {"", "多语资源类别"};
	private String input[][];
	private TableViewer tableViewer;
	private boolean isSingleSel = false;

	public LangModuleSelDlg(Shell parentShell, String input[][]){
		super(parentShell);
		this.input = input;
	}

	public LangModuleSelDlg(Shell parentShell, String input[][], boolean singleSel){
		super(parentShell);
		this.input = input;
		isSingleSel = singleSel;
	}

	protected Control createDialogArea(Composite parent){
		Composite container = new Composite(parent, 0);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(1808));
		setMessage("多语资源类别");
		
		tableViewer = new TableViewer(container, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		TableLayout tl = new TableLayout();
		table.setLayout(tl);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ColumnLayoutData columnLayoutData[] = new ColumnLayoutData[colNames.length];
		columnLayoutData[0] = new ColumnPixelData(18, false, true);
		columnLayoutData[1] = new ColumnWeightData(40, true);
		for (int i = 0; i < colNames.length; i++){
			TableColumn tc = new TableColumn(table, 0, i);
			tc.setText(colNames[i]);
			tl.addColumnData(columnLayoutData[i]);
			tc.setResizable(columnLayoutData[i].resizable);
		}

		CellEditor editors[] = new CellEditor[colNames.length];
		editors[0] = new MultiStateCellEditor(table, 2, 0);
		editors[1] = null;
		tableViewer.setCellEditors(editors);
		tableViewer.setColumnProperties(colNames);
		tableViewer.setCellModifier(new TableModifier());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement){
				return input;
			}

			@Override
			public void dispose(){
			}

			@Override
			public void inputChanged(Viewer viewer1, Object obj, Object obj1){
			}

		});
		tableViewer.addFilter(new ViewerFilter(){

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				String s[] = (String[])element;
				return !s[1].equals(ProjConstants.COMM);
			}
			
		});
		tableViewer.setInput(new Object());
		return container;
	}

}
