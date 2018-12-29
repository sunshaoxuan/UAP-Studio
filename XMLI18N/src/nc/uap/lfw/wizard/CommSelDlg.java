package nc.uap.lfw.wizard;

import nc.uap.lfw.mlr.MLRPropertyCache;
import nc.uap.lfw.mlr.MLRes;
import nc.uap.lfw.mlr.MLResSubstitution;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ProjConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * 公共资源对话框
 * 
 * @author dingrf
 *
 */
public class CommSelDlg extends TitleAreaDialog{
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider{
		public Image getColumnImage(Object element, int columnIndex){
			return null;
		}
		
		public String getColumnText(Object element, int columnIndex){
			String columnText = "";
			if (element instanceof MLResSubstitution){
				MLResSubstitution substitution = (MLResSubstitution)element;
				if (columnIndex == 0)
					columnText = Helper.unwindEscapeChars(substitution.getValue());
				else if (columnIndex == 1)
					columnText = substitution.getExtKey();
				else if (columnIndex == 2)
					columnText = Helper.unwindEscapeChars(substitution.getEnglishValue());
				else if (columnIndex == 3)
					columnText = Helper.unwindEscapeChars(substitution.getTwValue());
			}
			return columnText;
		}
	}

	private class TableModifier implements ICellModifier {

		public boolean canModify(Object element, String property){
			return true;
		}

		public Object getValue(Object element, String property){
			if (element instanceof MLResSubstitution){
				MLResSubstitution ele = (MLResSubstitution)element;
				if (CommSelDlg.colNames[0].equals(property))
					return Helper.unwindEscapeChars(ele.getValue());
				if (CommSelDlg.colNames[1].equals(property))
					return Helper.unwindEscapeChars(ele.getExtKey());
				if (CommSelDlg.colNames[2].equals(property))
					return Helper.unwindEscapeChars(ele.getEnglishValue());
				if (CommSelDlg.colNames[3].equals(property))
					return Helper.unwindEscapeChars(ele.getTwValue());
			}
			return "";
		}

		public void modify(Object element, String property, Object value){
			if (element instanceof TableItem){
				Object o = ((TableItem)element).getData();
				if (o instanceof MLResSubstitution)				{
					MLResSubstitution sub = (MLResSubstitution)o;
					if (CommSelDlg.colNames[0].equals(property)){
						sub.setValue((String)value);
						tableViewer.refresh();
						isNeedSave = true;
					} 
					else if (CommSelDlg.colNames[1].equals(property)){
						sub.setExtKey((String)value);
						tableViewer.refresh(o);
						isNeedSave = true;
					} 
					else if (CommSelDlg.colNames[2].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setEnglishValue(v);
						tableViewer.refresh();
						isNeedSave = true;
					} 
					else if (CommSelDlg.colNames[3].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setTwValue(v);
						tableViewer.refresh();
						isNeedSave = true;
					}
				}
			}
		}
	}


	private static final String colNames[] = {"多语资源", "资源ID", "英语", "繁体"};
	private TableViewer tableViewer;
//	private boolean isSingleSel;
	/**是否需要保存*/
	private boolean isNeedSave;
	private MLRPropertyCache commCache;
	private Button newBtn;
	private Button deleteBtn;
	private Button saveBtn;
	private List<MLResSubstitution> substitutionList;
	private MLResSubstitution selectMLRes;
	
	private MLRRefactoring mlrRefactoring;
	
	public CommSelDlg(Shell parentShell,MLRRefactoring mlrRefactoring){
		super(parentShell);
		commCache = new MLRPropertyCache(mlrRefactoring.getProject(), mlrRefactoring.getResouceHomePath());
		tableViewer = null;
//		isSingleSel = false;
		isNeedSave = false;
		this.mlrRefactoring = mlrRefactoring;
		getCommRes();
		
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(600, 400);
	}
	

	protected Control createDialogArea(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		setMessage("公共多语资源");
		
		Composite btnComp = new Composite(container, SWT.NONE);
		btnComp.setLayout(new GridLayout(3, false));
		
		newBtn = new Button(btnComp, 8);
		newBtn.setText("新增");
		newBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				doneNewBtnClicked();
			}
		});
		deleteBtn = new Button(btnComp, 8);
		deleteBtn.setText("删除");
		deleteBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				doneDeleteBtnClicked();
			}
		});
		
		saveBtn = new Button(btnComp, 8);
		saveBtn.setText("保存");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				doneSaveBtnClicked();
			}
		});
		
		Composite gridComp = new Composite(container, SWT.NONE);
		gridComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		gridComp.setLayout(new FillLayout());
		
		
		tableViewer = new TableViewer(gridComp, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		
		Table table = tableViewer.getTable();

		TableLayout tl = new TableLayout();
		table.setLayout(tl);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ColumnLayoutData columnLayoutData[] = new ColumnLayoutData[colNames.length];
		columnLayoutData[0] = new ColumnWeightData(40, true);
		columnLayoutData[1] = new ColumnWeightData(40, true);
		columnLayoutData[2] = new ColumnWeightData(40, true);
		columnLayoutData[3] = new ColumnWeightData(40, true);
		for (int i = 0; i < colNames.length; i++){
			TableColumn tc = new TableColumn(table, 0, i);
			tc.setText(colNames[i]);
			tl.addColumnData(columnLayoutData[i]);
			tc.setResizable(columnLayoutData[i].resizable);
		}

		CellEditor editors[] = new CellEditor[colNames.length];
		editors[0] = new TextCellEditor(table);
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);
		editors[3] = new TextCellEditor(table);
		tableViewer.setCellEditors(editors);
		tableViewer.setColumnProperties(colNames);
		tableViewer.setCellModifier(new TableModifier());
		TableLabelProvider provider = new TableLabelProvider();
		tableViewer.setLabelProvider(provider);
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement){
				if(inputElement instanceof List)
					return ((List) inputElement).toArray();
				else 
					return new Object[0];
//				return substitution;
			}

			@Override
			public void dispose(){
			}

			@Override
			public void inputChanged(Viewer viewer1, Object obj, Object obj1){
			}

		});
//		tableViewer.setInput(new Object());
		tableViewer.setInput(substitutionList);
		return container;
	}

	@Override
	protected void okPressed() {
		if (isNeedSave)
			saveMLRes();
		IStructuredSelection commSelection = (IStructuredSelection)tableViewer.getSelection();
		Object commSelectedElement = commSelection.getFirstElement();
		selectMLRes =  (MLResSubstitution)commSelectedElement;
		super.okPressed();
	}
	/**
	 * 取公共多语资源
	 * 
	 * @return
	 */
	private void getCommRes(){
		List<MLRes> simpchnResList = (List<MLRes>) commCache.findLocalMLResList(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_SIMPCHN, "common");
		List<MLRes> tradchnResList = (List<MLRes>) commCache.findLocalMLResList(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_TRADCHN, "common");
		List<MLRes> englishResList = (List<MLRes>) commCache.findLocalMLResList(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_ENGLISH, "common");
		Map<String,MLResSubstitution> subMap = new HashMap<String,MLResSubstitution>();
//		List<MLResSubstitution> list = new ArrayList<MLResSubstitution>();
		for (MLRes res : simpchnResList){
			MLResSubstitution sub = new MLResSubstitution(null,null);
			sub.setExtKey(res.getResID());
			sub.setSimpchnRes(res);
			subMap.put(res.getResID(), sub);
		}
		for (MLRes res : tradchnResList){
			if (subMap.containsKey(res.getResID())){
				MLResSubstitution s = subMap.get(res.getResID());
				s.setTradchnRes(res);
			}
			else{
				MLResSubstitution sub = new MLResSubstitution(null,null);
				sub.setExtKey(res.getResID());
				sub.setTradchnRes(res);
				subMap.put(res.getResID(), sub);
			}
		}
		for (MLRes res : englishResList){
			if (subMap.containsKey(res.getResID())){
				MLResSubstitution s = subMap.get(res.getResID());
				s.setEnglishRes(res);
			}
			else{
				MLResSubstitution sub = new MLResSubstitution(null,null);
				sub.setExtKey(res.getResID());
				sub.setEnglishRes(res);
				subMap.put(res.getResID(), sub);
			}
		}
		substitutionList = new ArrayList<MLResSubstitution>();
		for (Iterator<String> it = subMap.keySet().iterator(); it.hasNext();){
			String resID = (String)it.next();
			MLResSubstitution s = subMap.get(resID); 
			s.setState(1);
			substitutionList.add(s);
		}
	}
	
	/**
	 * 新增
	 * 
	 */
	private void doneNewBtnClicked() {
		MLResSubstitution s = new MLResSubstitution(null,null);
		s.setExtKey("");
		s.setValue("");
		s.setEnglishValue("");
		s.setTwValue("");
		substitutionList.add(s);
		tableViewer.setInput(substitutionList);
		isNeedSave = true;
//		  substitution
	}
	
	/**
	 * 
	 * 删除
	 */
	
	private void doneDeleteBtnClicked() {
		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		Object selectedElement = selection.getFirstElement();
		MLResSubstitution m =  (MLResSubstitution)selectedElement;
		substitutionList.remove(m);
		tableViewer.setInput(substitutionList);
		isNeedSave = true;
//		substitutionList
	}
	
	/**
	 * 保存
	 */
	private void doneSaveBtnClicked() {
		if (isNeedSave){
			saveMLRes();
		}
	}

	private void saveMLRes() {
		List<MLRes> simpchnResList = new ArrayList<MLRes>(); 
		List<MLRes> tradchnResList = new ArrayList<MLRes>(); 
		List<MLRes> englishResList = new ArrayList<MLRes>(); 
		for (MLResSubstitution m :substitutionList){
			MLRes simpchnRes = new MLRes("",m.getExtKey(),m.getValue()); 
			simpchnResList.add(simpchnRes);
			MLRes tradchnRes = new MLRes("",m.getExtKey(),m.getTwValue()); 
			tradchnResList.add(tradchnRes);
			MLRes englishRes = new MLRes("",m.getExtKey(),m.getEnglishValue()); 
			englishResList.add(englishRes);
		}
		commCache.saveCommMLRes(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_SIMPCHN, simpchnResList);
		commCache.saveCommMLRes(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_TRADCHN, tradchnResList);
		commCache.saveCommMLRes(mlrRefactoring.getCurrentPageNode().getBcpName(),ProjConstants.LANGCODE_ENGLISH, englishResList);
	}

	public MLResSubstitution getSelectMLRes() {
		return selectMLRes;
	}

	public void setSelectMLRes(MLResSubstitution selectMLRes) {
		this.selectMLRes = selectMLRes;
	}
 
}
