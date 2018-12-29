package nc.uap.lfw.wizard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_mlr;
import nc.uap.lfw.mlr.MLResSubstitution;
import nc.uap.lfw.plugin.common.CommonPlugin;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ImageFactory;
import nc.uap.lfw.tool.ProjConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.refactoring.nls.MultiStateCellEditor;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * XML外部化多语资源
 * 
 * @author dingrf
 *
 */
public class ExternalizeMLRWizardPage2 extends UserInputWizardPage{
	
	private class MyCellModifier implements ICellModifier{

		public boolean canModify(Object element, String property){
//			return (element instanceof MLResSubstitution)  && (((MLResSubstitution)element).getState()!=3)  && (ExternalizeMLRWizardPage2.colNames[2].equals(property) || ExternalizeMLRWizardPage2.colNames[4].equals(property) || ExternalizeMLRWizardPage2.colNames[5].equals(property));
			return (element instanceof MLResSubstitution)  
				&& (((MLResSubstitution)element).getState()!=3 && ((MLResSubstitution)element).getState()!=4 && ((MLResSubstitution)element).getState()!=5) 
				&& (ExternalizeMLRWizardPage2.colNames[2].equals(property));
		}

		public Object getValue(Object element, String property){
			if (element instanceof MLResSubstitution){
				MLResSubstitution ele = (MLResSubstitution)element;
				if (ExternalizeMLRWizardPage2.colNames[0].equals(property))
					return Integer.valueOf(ele.getState());
//				if (ExternalizeMLRWizardPage2.colNames[1].equals(property))
//					return ele.getOldKey().equals("")?"新增":"修改";
				if (ExternalizeMLRWizardPage2.colNames[2].equals(property))
					return Helper.unwindEscapeChars(ele.getValue());
				if (ExternalizeMLRWizardPage2.colNames[3].equals(property))
					if (ele.getState() == 0)
						return Helper.unwindEscapeChars(ele.getKey());
					else
						return ""; //$NON-NLS-1$
				if (ExternalizeMLRWizardPage2.colNames[4].equals(property))
					return Helper.unwindEscapeChars(ele.getSimpValue());
				if (ExternalizeMLRWizardPage2.colNames[5].equals(property))
					return Helper.unwindEscapeChars(ele.getEnglishValue());
				if (ExternalizeMLRWizardPage2.colNames[6].equals(property))
					return Helper.unwindEscapeChars(ele.getTwValue());
				if (ExternalizeMLRWizardPage2.colNames[7].equals(property))
					return ele.getFilePath();
			}
			return ""; //$NON-NLS-1$
		}

		public void modify(Object element, String property, Object value){
			if (element instanceof TableItem){
				Object o = ((TableItem)element).getData();
				if (o instanceof MLResSubstitution)				{
					MLResSubstitution sub = (MLResSubstitution)o;
//					if (ExternalizeMLRWizardPage2.colNames[0].equals(property)){
//						sub.setState(((Integer)value).intValue());
//						mlrRefactoring.updateKey();
//						tableViewer.refresh();
//					} 
					if (ExternalizeMLRWizardPage2.colNames[2].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						String old = sub.getValue();
						sub.setValue(v);
						if (!old.equals(v)){
							MLResSubstitution substitutions[] = mlrRefactoring.getSubstitutions(); 
							for (int i = 0 ; i< substitutions.length ; i++){
								if (substitutions[i].getRealKey().equals(sub.getRealKey()))
									substitutions[i].setValue(v);
							}
//							mlrRefactoring.updateSubstitution(sub);
//							mlrRefactoring.updateKey();
							tableViewer.refresh();
						}
					} 
					else if (ExternalizeMLRWizardPage2.colNames[3].equals(property)){
						sub.setKey((String)value);
						tableViewer.refresh(o);
					} 
					else if (ExternalizeMLRWizardPage2.colNames[4].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setSimpValue(v);
						tableViewer.refresh();
					} 
					else if (ExternalizeMLRWizardPage2.colNames[5].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setEnglishValue(v);
						tableViewer.refresh();
					} 
					else if (ExternalizeMLRWizardPage2.colNames[6].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setTwValue(v);
						tableViewer.refresh();
					}
				}
			}
		}
	}

	private class MyLabelProvider extends LabelProvider implements ITableLabelProvider{

		public Image getColumnImage(Object element, int columnIndex){
			if (element instanceof MLResSubstitution){
				MLResSubstitution sub = (MLResSubstitution)element;
				if (columnIndex == 0)
					return ImageFactory.getImageBySubstitutionState(sub);
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex){
			String columnText = ""; //$NON-NLS-1$
			if (element instanceof MLResSubstitution){
				MLResSubstitution substitution = (MLResSubstitution)element;
//				if (ExternalizeMLRWizardPage2.colNames[1].equals(property))
//				return ele.getOldKey().equals("")?"新增":"修改";
				
				if (columnIndex == 1)
					columnText = substitution.getState()==0?M_mlr.ExternalizeMLRWizardPage2_0:substitution.getState()==1?M_mlr.ExternalizeMLRWizardPage2_1:  
						substitution.getState()==2?M_mlr.ExternalizeMLRWizardPage2_2:substitution.getState()==3?M_mlr.ExternalizeMLRWizardPage2_3:substitution.getState()==4?M_mlr.ExternalizeMLRWizardPage2_4:
							substitution.getState()==5?M_mlr.ExternalizeMLRWizardPage2_5:substitution.getState()==7?M_mlr.ExternalizeMLRWizardPage2_6:M_mlr.ExternalizeMLRWizardPage2_7;
				if (columnIndex == 2)
					columnText = Helper.unwindEscapeChars(substitution.getValue());
				else if (columnIndex == 3)
					columnText = substitution.getRealKey();
				else if (columnIndex == 4)
					columnText = Helper.unwindEscapeChars(substitution.getSimpValue());
				else if (columnIndex == 5)
					columnText = Helper.unwindEscapeChars(substitution.getEnglishValue());
				else if (columnIndex == 6)
					columnText = Helper.unwindEscapeChars(substitution.getTwValue());
				else if (columnIndex == 7)
					columnText = substitution.getPageNode().getFile().getFullPath().toString();
			}
			return columnText;
		}
	}
	
	/**
	 *树的ContentProvider 
	 */
	class TreeContentProvider implements ITreeContentProvider{

		public TreeContentProvider() {
		}

		public Object[] getChildren(Object parentElement) {
			PageNode p = (PageNode) parentElement;
			List<PageNode> list = p.getChildrens();
			
			return list.toArray(list.toArray(new PageNode[0]));
		}

		public Object getParent(Object element) {
			PageNode p = (PageNode) element;
			if(p.getParent() != null)
				return p.getParent();
			return null;
		}

		public boolean hasChildren(Object element) {
			PageNode p = (PageNode) element;
			if (p.getChildrens() == null)
				return false;
			else
				return true;
		}

		public Object[] getElements(Object inputElement) {
			if(inputElement != null){
				return (Object[]) inputElement;
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/**
	 *树的LabelProvider 
	 */
	class LabelContentProvider extends LabelProvider{

		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			PageNode p = (PageNode) element;
			return p.getName();
//			return "[" + vo.getFuncCode() + "]" + vo.getFuncName();
		}
		
	}

	/**
	 *树的changed事件 
	 * 
	 */
	class MySelectionChangeListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			nodeIdChanged();
		}
		
	}
	
	private void nodeIdChanged(){
		Tree tree = treeViewer.getTree();
		TreeItem treeItem = (TreeItem) tree.getSelection()[0];
		Object object = treeItem.getData();
		if(object instanceof PageNode){
			if (((PageNode)object).isFile()){
				mlrRefactoring.setCurrentPageNode((PageNode)object);
				prefixText.setText(mlrRefactoring.getPrefix());
				tableViewer.refresh();
				refreshSoruseView();
			} 
		}
	}
	

	public static final String colNames[] = {"",M_mlr.ExternalizeMLRWizardPage2_8, M_mlr.ExternalizeMLRWizardPage2_9, M_mlr.ExternalizeMLRWizardPage2_10,M_mlr.ExternalizeMLRWizardPage2_11, M_mlr.ExternalizeMLRWizardPage2_12, M_mlr.ExternalizeMLRWizardPage2_13,M_mlr.ExternalizeMLRWizardPage2_14}; //$NON-NLS-1$
//	public static final String colNames[] = {"","状态", "多语资源", "资源ID", "文件"};
	/**左边树节点*/
	private TreeViewer treeViewer;
	/**page列表*/
	List<PageNode> pageNodes =  null;

	private TableViewer tableViewer;
	private MLRRefactoring mlrRefactoring;
	private SourceViewer fSourceViewer;
	private Text prefixText;
	private Button selCommBtn;
//	private Button projectBtn;
//	private Button moduleBtn;

	
	public ExternalizeMLRWizardPage2(MLRRefactoring mlrRefactoring){
		super(M_mlr.ExternalizeMLRWizardPage2_15);
		this.mlrRefactoring = mlrRefactoring;
		tableViewer = null;
		fSourceViewer = null;
		prefixText = null;
		this.mlrRefactoring.setPage2(this);
	}

	public void createControl(Composite parent){
		Composite container = new Composite(parent, 0);
//		container.setVisible(false);
//		container.setLayout(new FillLayout(512));
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
//		container.setBackground(new org.eclipse.swt.graphics.Color(null,125,25,41));
//		Composite comp = new Composite(container, SWT.NONE);
//		comp.setBackground(new org.eclipse.swt.graphics.Color(null,125,25,41));
//		comp.setLayout(new FillLayout());
		//生成左边树
		createTreeViewer(container);
		
		SashForm form = new SashForm(container, 512);
		GridData layout = new GridData(GridData.FILL_BOTH);
		layout.widthHint=800;
		form.setLayoutData(layout);
//		form.setBackground(new org.eclipse.swt.graphics.Color(null,125,125,125));
//		form.setLayout(new FillLayout());
		createTableViewer(form);
		createSourceViewer(form);
		Button btn = new Button(container, SWT.CHECK);
		btn.setText(M_mlr.ExternalizeMLRWizardPage2_16);
//		GridData griddata = new GridData();
//		griddata.horizontalSpan = 2;
//		griddata.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
//		btn.setLayoutData(griddata);
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.getSource()).getSelection())
					mlrRefactoring.setCleanRes(true);
				else mlrRefactoring.setCleanRes(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		Button btn2 = new Button(container, SWT.PUSH);
		btn2.setText(M_mlr.ExternalizeMLRWizardPage2_17);
		btn2.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,List<String>> valueMap = mlrRefactoring.getNoChinValueMap();
				Iterator<String> iter = valueMap.keySet().iterator();
				while(iter.hasNext()){
					String path =iter.next();
					List<String> valueList = valueMap.get(path);
					for(String value:valueList){
						CommonPlugin.getPlugin().logError(M_mlr.ExternalizeMLRWizardPage2_18+path+M_mlr.ExternalizeMLRWizardPage2_19+value);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO 自动生成的方法存根
				
			}			
		});
		setControl(container);
		
	}

	/**
	 *左边树 
	 * @param comp
	 */
	private void createTreeViewer(Composite container){
		treeViewer = new TreeViewer(container,SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = treeViewer.getTree();
		GridData layout = new GridData(GridData.FILL_VERTICAL);
		layout.widthHint=150;
		tree.setLayoutData(layout);
		//tree.setLinesVisible(true);
		//tree.setHeaderVisible(true);
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new LabelContentProvider());
		treeViewer.setInput(loadNodes());
		treeViewer.addSelectionChangedListener(new MySelectionChangeListener(){
			
		});
	}
	
	private void createTableViewer(Composite parent){
		Composite comp = new Composite(parent, 0);
		GridLayout gl = new GridLayout(2, false);
		comp.setLayout(gl);
		Label prefixLbl = new Label(comp, SWT.NONE);
		prefixLbl.setText(M_mlr.ExternalizeMLRWizardPage2_20);
		prefixText = new Text(comp, 2052);
		prefixText.setLayoutData(new GridData(768));
		if (mlrRefactoring.getPrefix() != null)
			prefixText.setText(mlrRefactoring.getPrefix());
		prefixText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e){
				donePrefixTextModify(e);
			}
		});
		prefixText.setEditable(false);
		
		Composite btnComp = new Composite(comp, SWT.NONE);
		GridData compGD = new GridData(768);
		compGD.horizontalSpan = 2;
		compGD.verticalSpan = 1;
		btnComp.setLayoutData(compGD);
		btnComp.setLayout(new GridLayout(3, false));
		selCommBtn = new Button(btnComp, SWT.None);
//		selCommBtn.setLayoutData(createGridData(200, 1));
		selCommBtn.setText(M_mlr.ExternalizeMLRWizardPage2_21);
		selCommBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				doneSelCommBtnClicked();
			}
		});
//		projectBtn = new Button(btnComp,SWT.RADIO);
//		projectBtn.setText("工程");
//		projectBtn.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e){
//				mlrRefactoring.setExpType("project");
//			}
//		});
//		moduleBtn = new Button(btnComp,SWT.RADIO);
//		moduleBtn.setText("模块");
//		moduleBtn.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e){
//				mlrRefactoring.setExpType("module");
//			}
//		});
//		moduleBtn.setSelection(true);
		Label msn = new Label(comp, SWT.NONE);
		msn.setAlignment(SWT.RIGHT);
		msn.setText(M_mlr.ExternalizeMLRWizardPage2_22);
		
//		tableViewer = new TableViewer(comp, 0x18b02);
		tableViewer = new TableViewer(comp, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		GridData gd = new GridData(1808);
		gd.horizontalSpan = 2;
		table.setLayoutData(gd);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ColumnLayoutData columnLayoutData[] = new ColumnLayoutData[colNames.length];
		columnLayoutData[0] = new ColumnPixelData(18, false, true);
		columnLayoutData[1] = new ColumnWeightData(20, true);
		columnLayoutData[2] = new ColumnWeightData(40, true);
		columnLayoutData[3] = new ColumnWeightData(20, true);
		columnLayoutData[4] = new ColumnWeightData(40, true);
		columnLayoutData[5] = new ColumnWeightData(40, true);
		columnLayoutData[6] = new ColumnWeightData(40, true);
		columnLayoutData[7] = new ColumnWeightData(80, true);
		for (int i = 0; i < colNames.length; i++){
			TableColumn tc = new TableColumn(table, 0, i);
			tc.setText(colNames[i]);
			layout.addColumnData(columnLayoutData[i]);
			tc.setResizable(columnLayoutData[i].resizable);
		}

		CellEditor editors[] = new CellEditor[colNames.length];
		editors[0] = new MultiStateCellEditor(table, 3, 1);
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);
		editors[3] = new TextCellEditor(table);
		editors[4] = new TextCellEditor(table);
		editors[5] = new TextCellEditor(table);
		editors[6] = new TextCellEditor(table);
		editors[7] = new TextCellEditor(table);
		tableViewer.setCellEditors(editors);
		tableViewer.setColumnProperties(colNames);
		tableViewer.setCellModifier(new MyCellModifier());
		tableViewer.setLabelProvider(new MyLabelProvider());
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement){
				return mlrRefactoring.getSubstitutions();
			}
			public void dispose(){}
			public void inputChanged(Viewer viewer1, Object obj, Object obj1){}
		});
		//TODO
		if(mlrRefactoring.getSubstitutions()!=null) tableViewer.setInput(new Object());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event){
				ExternalizeMLRWizardPage2.this.selectionChanged(event);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				//修复警告
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				MLResSubstitution first = (MLResSubstitution)selection.getFirstElement();
				if (first.getState() == 5){
					if (!MessageDialog.openConfirm(new Shell(), M_mlr.ExternalizeMLRWizardPage2_23, M_mlr.ExternalizeMLRWizardPage2_24))
						return;
					String s = first.getSelectKey();
					String cx = mlrRefactoring.getContents().get(first.getFilePath());
					String k =""; //$NON-NLS-1$
					if (!first.getKey().equals("")) //$NON-NLS-1$
						k = first.getKeyWithPrefix();
					else
					k = mlrRefactoring.getKey(cx, first.getPageNode());
					String s2 = s.replaceFirst(" ", " " +  ProjConstants.I18NNAME + "=\"" + k + "\" " + ProjConstants.LANG_DIR + "=\"" + first.getRealLangModule() +"\" "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					cx = cx.replace(s, s2);
					mlrRefactoring.getContents().put(first.getFilePath(), cx);
					first.setState(6);
					if (first.getKey().equals("")) //$NON-NLS-1$
						first.setKey(k.replace(first.getPrefix() , "")); //$NON-NLS-1$
//					first.setValue("");
//					first.setLangModule(mlrRefactoring.getResModuleName());
					tableViewer.refresh();
					mlrRefactoring.updateSubPostion(null);
					refreshSoruseView();
				}
				else if (first.getState() == 6){
					if (!MessageDialog.openConfirm(new Shell(), M_mlr.ExternalizeMLRWizardPage2_23, M_mlr.ExternalizeMLRWizardPage2_25))
						return;
					String s1 = first.getOldKey();
					String s2 = first.getSelectKey();
					first.setSelectKey(s1);
					String cx = mlrRefactoring.getContents().get(first.getFilePath());
//					String k = mlrRefactoring.getKey(cx, first.getPageNode());
//					String s2 = s.replaceFirst(" ", " " +  ProjConstants.I18NNAME + "=\"" + k + "\" " + ProjConstants.LANG_DIR + "=\"" + mlrRefactoring.getResModuleName() +"\" ");
					cx = cx.replace(s2, s1);
					mlrRefactoring.getContents().put(first.getFilePath(), cx);
					first.setState(5);
//					first.setKey(k.replace(first.getPrefix() , ""));
//					first.setLangModule(mlrRefactoring.getResModuleName());
					tableViewer.refresh();
					mlrRefactoring.updateSubPostion(null);
					refreshSoruseView();
				}
			} 
			
		});
//		tableViewer.addFilter(new ViewerFilter(){
//
//			@Override
//			public boolean select(Viewer viewer, Object parentElement,
//					Object element) {
//				MLResSubstitution s = (MLResSubstitution)element;
//				if (mlrRefactoring.getCurrentPageNode() ==null)
//					return false;
//				else
//					return s.getFilePath().equals(mlrRefactoring.getCurrentPageNode().getPath());
//			}
//			
//		});
	}

	protected void donePrefixTextModify(ModifyEvent e){
		mlrRefactoring.setPrefix(prefixText.getText().trim());
//		mlrRefactoring.updateKey();
		tableViewer.refresh();
		refreshSoruseView();
		
	}

	private void selectionChanged(SelectionChangedEvent event)
	{
		IStructuredSelection selection = (IStructuredSelection)event.getSelection();
		updateSourceView(selection);
	}

	private void createSourceViewer(Composite parent){
		Composite c = new Composite(parent, 0);
		c.setLayoutData(new GridData(1808));
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		c.setLayout(gl);
		Label l = new Label(c, 0);
		l.setText(M_mlr.ExternalizeMLRWizardPage2_26);
		l.setLayoutData(new GridData());
		JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
		int styles = 0x10b02;
		org.eclipse.jface.preference.IPreferenceStore store = JavaPlugin.getDefault().getCombinedPreferenceStore();
		fSourceViewer = new JavaSourceViewer(c, null, null, false, styles, store);
		JavaSourceViewerConfiguration config = new JavaSourceViewerConfiguration(tools.getColorManager(), store, null, null);
		fSourceViewer.configure(config);
		fSourceViewer.getControl().setFont(JFaceResources.getFont("org.eclipse.jdt.ui.editors.textfont")); //$NON-NLS-1$
		try{
//			String contents = mlrRefactoring.getCu().getBuffer().getContents();
			if (mlrRefactoring.getCurrentPageNode() != null){
				String contents = mlrRefactoring.getContents().get(mlrRefactoring.getCurrentPageNode().getPath());
				org.eclipse.jface.text.IDocument document = new Document(contents);
				tools.setupJavaDocumentPartitioner(document);
				fSourceViewer.setDocument(document);
			}
			fSourceViewer.setEditable(false);
			GridData gd = new GridData(1808);
			gd.heightHint = convertHeightInCharsToPixels(10);
			gd.widthHint = convertWidthInCharsToPixels(40);
			fSourceViewer.getControl().setLayoutData(gd);
		}
		catch (Exception e)
		{
			MessageDialog.openError(getShell(), "Error", (new StringBuilder()).append(e.getClass()).append(": ").append(e.getMessage()).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}


	/**
	 * 加载树节点
	 * @return
	 */
	@SuppressWarnings("unused")
	public  PageNode[] loadNodes(){
		pageNodes = new ArrayList<PageNode>();
		List<PageNode> topNodes = new ArrayList<PageNode>();
//		String[] names = LfwCommonTool.getBCPNames(mlrRefactoring.getProject());
//		String[] names = LfwCommonTool.getBCPNames(mlrRefactoring.getFolder());
//		String[] names = null;
		try {
//			if (names == null){
				IFolder fileFolder = mlrRefactoring.getFolder();
				String module = ""; //$NON-NLS-1$
				if(LfwCommonTool.isBCPProject(mlrRefactoring.getProject())) 
					module = fileFolder.getFullPath().segments()[1];
				else module = mlrRefactoring.getProject().getName().toLowerCase();
				PageNode pageNode = new PageNode(fileFolder,module + "_nodes",mlrRefactoring.getProject().getName().toLowerCase()); //$NON-NLS-1$
				if(LfwCommonTool.isBCPProject(mlrRefactoring.getProject()))
				{
					pageNode.setPrefix(fileFolder.getFullPath().segments()[1]+"-"); //$NON-NLS-1$
					pageNode.setBcpName(fileFolder.getFullPath().segments()[1]);
				}					
				else pageNode.setPrefix(pageNode.getRoot());
				topNodes.add(pageNode);
				pageNodes.add(pageNode);
				scanDir(pageNodes, fileFolder,pageNode);
//				IFolder fileFolder = mlrRefactoring.getProject().getFolder("/web/html/nodes");
//				IFolder fileFolder = mlrRefactoring.getFolder();
//				for (int i = 0 ; i<fileFolder.members().length ; i++){
//					IResource resorce = fileFolder.members()[i];
//					if (resorce instanceof IFolder){
//						IFolder f =(IFolder)resorce;
//						//加载目录
//						PageNode pageNode = new PageNode(f,mlrRefactoring.getProject().getName().toLowerCase() + "_nodes");
//						topNodes.add(pageNode);
//						pageNodes.add(pageNode);
//						scanDir(pageNodes, f,pageNode);
//					}
//				}
//			}
//			else{
//				for (int count = 0 ; count < names.length ; count ++){
//					IFolder fileFolder = mlrRefactoring.getProject().getFolder("/"+names[count]+"/web/html/nodes");
//					for (int i = 0 ; i<fileFolder.members().length ; i++){
//						IResource resorce = fileFolder.members()[i];
//						if (resorce instanceof IFolder){
//							IFolder f =(IFolder)resorce;
//							PageNode pageNode = new PageNode(f,names[count].toLowerCase()+"_nodes",mlrRefactoring.getProject().getName().toLowerCase());
////							if(LfwCommonTool.isBCPProject(mlrRefactoring.getProject()))
////								pageNode.setPrefix(fileFolder.getFullPath().segments()[1]+"-");
////							else pageNode.setPrefix(pageNode.getRoot());
//							pageNode.setBcpName(names[count].toLowerCase());
//							topNodes.add(pageNode);
//							pageNodes.add(pageNode);
//							scanDir(pageNodes, f,pageNode);
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		} 
		
		mlrRefactoring.createRawSubstitution(pageNodes);
		return topNodes.toArray(new PageNode[0]); 
	}

	/**
	 * 扫描文件夹
	 * @param pageNodes
	 * @param dir
	 */
	private void scanDir(List<PageNode> pageNodes, IFolder dir,PageNode parentNode){
		if(judgeIsPMFolder(dir)){
			//加载目录下的 um,pm,wd文件
			scanFiles(dir,pageNodes,parentNode);
		}
		else{
			try {
				for (int i = 0 ; i<dir.members().length ; i++){
					IResource resorce = dir.members()[i];
					if (resorce instanceof IFolder){
						IFolder f = (IFolder)resorce;
						//加载目录
						PageNode pageNode = new PageNode(f,parentNode.getResModuleName(),parentNode.getRoot());
						pageNode.setParent(parentNode);
						pageNode.setRoot(parentNode.getRoot());
						pageNode.setBcpName(parentNode.getBcpName());

						if(LfwCommonTool.isBCPProject(mlrRefactoring.getProject()))
							pageNode.setPrefix(f.getFullPath().segments()[1]+"-"); //$NON-NLS-1$
						else pageNode.setPrefix(pageNode.getRoot()+"-"); //$NON-NLS-1$
						parentNode.getChildrens().add(pageNode);
						pageNodes.add(pageNode);
						scanDir(pageNodes, (IFolder)resorce,pageNode);
					}
				}
			} catch (CoreException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			} 
		}
	}

	/**
	 * 加载目录下的 um,pm,wd文件
	 * 
	 * @param dir
	 * @param pageNodes
	 * @param parent
	 */
	private void scanFiles(IFolder dir,List<PageNode> pageNodes,PageNode parent) {
		try {
			for (int i = 0 ; i<dir.members().length ; i++){
				IResource resorce = dir.members()[i];
				if (resorce instanceof IFolder){
					IFolder folder = (IFolder)resorce;
					PageNode pageNode = new PageNode(folder,parent.getResModuleName(),parent.getRoot());
					pageNode.setParent(parent);
					pageNode.setRoot(parent.getRoot());
					pageNode.setBcpName(parent.getBcpName());
					parent.getChildrens().add(pageNode);
					pageNodes.add(pageNode);
					scanFiles(folder,pageNodes,pageNode);
				}
				else{
					IFile file = (IFile)resorce;
					String appPath = "web/html/applications/"; //$NON-NLS-1$
					String winPath = "web/html/nodes/"; //$NON-NLS-1$
					String publicPath = "web/html/views/"; //$NON-NLS-1$
					String fileName = file.getName();
					if (fileName.endsWith(".um") || fileName.endsWith(".pm") || fileName.endsWith(".wd")||fileName.endsWith(".app")){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						PageNode pageNode = new PageNode(file,parent.getResModuleName());
						pageNode.setParent(parent);
						pageNode.setRoot(parent.getRoot());
						pageNode.setBcpName(parent.getBcpName());
						String pathStr = file.getLocation().toString();
						String[] fullPath = file.getFullPath().segments();
						List<String> pathList =  Arrays.asList(fullPath);
						String prefix = ""; //$NON-NLS-1$
						if(pathStr.indexOf(appPath)>-1){
							prefix = "a_"+pathList.get(pathList.indexOf("applications")+1)+"-"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							pageNode.setType("Application"); //$NON-NLS-1$
						}
						else if(pathStr.indexOf(winPath)>-1){
							if(pathStr.endsWith(".um")&&dir.findMember("pagemeta.pm")!=null) //$NON-NLS-1$ //$NON-NLS-2$
								prefix = "w_"+pathList.get(pathList.size()-2)+"-"; //$NON-NLS-1$ //$NON-NLS-2$
							if(pathStr.endsWith(".um")&&dir.findMember("widget.wd")!=null) //$NON-NLS-1$ //$NON-NLS-2$
								prefix = "w_"+pathList.get(pathList.size()-3)+"-"; //$NON-NLS-1$ //$NON-NLS-2$
							if(pathStr.endsWith(".pm")) //$NON-NLS-1$
								prefix = "w_"+pathList.get(pathList.size()-2)+"-"; //$NON-NLS-1$ //$NON-NLS-2$
							if(pathStr.endsWith(".wd")) //$NON-NLS-1$
								prefix = "w_"+pathList.get(pathList.size()-3)+"-"; //$NON-NLS-1$ //$NON-NLS-2$
							pageNode.setType("Window"); //$NON-NLS-1$
						}
						else if(pathStr.indexOf(publicPath)>-1){
							prefix = "p_"+pathList.get(pathList.size()-2)+"-"; //$NON-NLS-1$ //$NON-NLS-2$
							pageNode.setType("Public"); //$NON-NLS-1$
						}
						pageNode.setPrefix(prefix);
//						if(LfwCommonTool.isBCPProject(mlrRefactoring.getProject()))
//							pageNode.setPrefix(file.getFullPath().segments()[1]+"-");
//						else pageNode.setPrefix(pageNode.getRoot());
						parent.getChildrens().add(pageNode);
						pageNodes.add(pageNode);
					}
				}
			}
		} catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		}
		
		
	}

	/**
	 * 判断是否含pm文件
	 * @param fold
	 * @return
	 */
	private boolean judgeIsPMFolder(IFolder dir) {
		try {
			for (int i = 0 ; i<dir.members().length ; i++){
				IResource resorce = dir.members()[i];
				if (resorce instanceof IFile){
					String fileName = resorce.getName();
					if (fileName.endsWith(".um") || fileName.endsWith(".pm") || fileName.endsWith(".wd")||fileName.endsWith(".app")){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						return true;
					}
				}
			}
		} catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
			return false;
		}
		return false;
	}
	
	public void refreshSoruseView(){
		if (mlrRefactoring.getCurrentPageNode() != null){
			org.eclipse.jface.text.IDocument document = new Document(mlrRefactoring.getContents().get(mlrRefactoring.getCurrentPageNode().getPath()));
			JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
			tools.setupJavaDocumentPartitioner(document);
			fSourceViewer.setDocument(document);
		}
	}
	
	/**
	 * 更新源码区选中范围
	 * 
	 * @param selection
	 */
	private void updateSourceView(IStructuredSelection selection){
		MLResSubstitution first = (MLResSubstitution)selection.getFirstElement();
		if (first != null){
			//TODO
			if ((mlrRefactoring.getCurrentPageNode() == null) || (mlrRefactoring.getCurrentPageNode() != null && mlrRefactoring.getCurrentPageNode() != first.getPageNode())){
				mlrRefactoring.setCurrentPageNode(first.getPageNode());
				prefixText.setText(mlrRefactoring.getPrefix());
				refreshSoruseView();
			}
			
			Region region = first.getElement().getPosition();
			fSourceViewer.setSelectedRange(region.getOffset(), region.getLength());
			fSourceViewer.revealRange(region.getOffset(), region.getLength());
			
			//公共资源是否可用
//			if (first.getState() ==0 || first.getState() ==6 ||first.getState() ==1)
//				selCommBtn.setEnabled(true);
//			else
//				selCommBtn.setEnabled(false);
		}
	}

	public void updateTableViewer(){
		tableViewer.refresh();
	}

	/**
	 * 从公共资源选择
	 */
	protected void doneSelCommBtnClicked(){
		
		CommSelDlg dlg = new CommSelDlg(getShell(), mlrRefactoring);
		int result = dlg.open();
		if (result == 0){
			MLResSubstitution commRes =  dlg.getSelectMLRes();
			if (commRes == null)
				return;
			IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
			Object selectedElement = selection.getFirstElement();
			if (selectedElement == null)
				return;
			MLResSubstitution m =  (MLResSubstitution)selectedElement;
			updateMLResSubstitution(m,commRes);
			updateTableViewer();
			refreshSoruseView();
		}
		
	}
	
	private void updateMLResSubstitution(MLResSubstitution substitution,MLResSubstitution commRes){
		String oldLangModule = substitution.getLangModule();
		String oldKey = substitution.getRealKey();
		substitution.setCommKey(commRes.getExtKey());
		substitution.getPageNode().setChanged(true);
//		substitution.setPrefix("");
		substitution.setValue(commRes.getValue());
		substitution.setEnglishValue(commRes.getEnglishValue());
		substitution.setExtKey(commRes.getExtKey());
		substitution.setTwValue(commRes.getTwValue());
		substitution.setState(3);
		substitution.setLangModule(ProjConstants.COMM);
		
		String contents = mlrRefactoring.getContents().get(mlrRefactoring.getCurrentPageNode().getPath());
		int mlrPos =  contents.indexOf(ProjConstants.I18NNAME + "=\"" + oldKey + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0 ; i< mlrRefactoring.getSubstitutions().length ; i ++){
			if (contents.indexOf(ProjConstants.I18NNAME) != mlrPos){
				contents = contents.replaceFirst(ProjConstants.I18NNAME, "%i18n%"); //$NON-NLS-1$
				contents = contents.replaceFirst(ProjConstants.LANG_DIR, "%dir%"); //$NON-NLS-1$
				mlrPos =  contents.indexOf(ProjConstants.I18NNAME + "=\"" + oldKey + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else{
				contents = contents.replaceFirst(ProjConstants.I18NNAME+ "=\""+ oldKey + "\"", ProjConstants.I18NNAME+ "=\""+ substitution.getRealKey() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				contents = contents.replaceFirst(ProjConstants.LANG_DIR+ "=\""+ oldLangModule + "\"", ProjConstants.LANG_DIR+ "=\""+ substitution.getLangModule() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				break;
			}
		}
		contents = contents.replaceAll("%dir%", ProjConstants.LANG_DIR); //$NON-NLS-1$
		contents = contents.replaceAll("%i18n%", ProjConstants.I18NNAME); //$NON-NLS-1$
		mlrRefactoring.getContents().put(mlrRefactoring.getCurrentPageNode().getPath(), contents);
		mlrRefactoring.updateSubPostion(null);
		mlrRefactoring.setSourceFileChanged(true);
//		String contents = mlrRefactoring.getContents().replace(ProjConstants.LANG_DIR+ "=\"" + oldLangModule + "\"", ProjConstants.LANG_DIR+ "=\"" + "common"+ "\"");
//		mlrRefactoring.setContents(contents);
//		String newKey = substitution.getKeyWithPrefix();
//		substitution.getElement().setFPosition(contents.indexOf(newKey), newKey.length());
		
	}

	public List<PageNode> getPageNodes() {
		return pageNodes;
	}


	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
//	public void setPageNodes(List<PageNode> pageNodes) {
//		this.pageNodes = pageNodes;
//	}

	public MLRRefactoring getMlrRefactoring() {
		return mlrRefactoring;
	}
	
	
	
}
