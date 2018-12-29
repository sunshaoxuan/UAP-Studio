package nc.uap.lfw.multilang;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_multilang;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ImageFactory;

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
import org.eclipse.jface.viewers.ICellModifier;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class ExternalizeMLRWizardPage extends UserInputWizardPage{
	
	List<PageNode> pageNodes =  null;
	private TableViewer tableViewer;
	private MLRRefactoring mlrRefactoring;
	private SourceViewer fSourceViewer;
	private Text prefixText;
	private Button selCommBtn;
	
	private Button includeChineseBtn = null;
	  private Button includeLetterBtn = null;
	  private Button includeDigitBtn = null;
	  private Button includeOtherBtn = null;
	  
	/**左边树节点*/
	private TreeViewer treeViewer;
	
	public static final String colNames[] = {"",M_multilang.ExternalizeMLRWizardPage_0, M_multilang.ExternalizeMLRWizardPage_1, M_multilang.ExternalizeMLRWizardPage_2, M_multilang.ExternalizeMLRWizardPage_3, M_multilang.ExternalizeMLRWizardPage_4,M_multilang.ExternalizeMLRWizardPage_5}; //$NON-NLS-1$
	
	private class MyCellModifier implements ICellModifier{

		public boolean canModify(Object element, String property){
//			return (element instanceof MLResSubstitution)  && (((MLResSubstitution)element).getState()!=3)  && (ExternalizeMLRWizardPage.colNames[2].equals(property) || ExternalizeMLRWizardPage.colNames[4].equals(property) || ExternalizeMLRWizardPage.colNames[5].equals(property));
			return (element instanceof MLResSubstitution)  
				&& (((MLResSubstitution)element).getState()!=3 && ((MLResSubstitution)element).getState()!=4 && ((MLResSubstitution)element).getState()!=5) 
				&& (ExternalizeMLRWizardPage.colNames[2].equals(property)||ExternalizeMLRWizardPage.colNames[4].equals(property)||ExternalizeMLRWizardPage.colNames[5].equals(property));
		}

		public Object getValue(Object element, String property){
			if (element instanceof MLResSubstitution){
				MLResSubstitution ele = (MLResSubstitution)element;
				if (ExternalizeMLRWizardPage.colNames[0].equals(property))
					return Integer.valueOf(ele.getState());
//				if (ExternalizeMLRWizardPage.colNames[1].equals(property))
//					return ele.getOldKey().equals("")?"新增":"修改";
				if (ExternalizeMLRWizardPage.colNames[2].equals(property))
					return Helper.unwindEscapeChars(ele.getValue());
				if (ExternalizeMLRWizardPage.colNames[3].equals(property))
					if (ele.getState() == 0)
						return Helper.unwindEscapeChars(ele.getKey());
					else
						return ""; //$NON-NLS-1$
				if (ExternalizeMLRWizardPage.colNames[4].equals(property))
					return Helper.unwindEscapeChars(ele.getEnglishValue());
				if (ExternalizeMLRWizardPage.colNames[5].equals(property))
					return Helper.unwindEscapeChars(ele.getTwValue());
//				if (ExternalizeMLRWizardPage.colNames[6].equals(property))
//					return ele.getFilePath();
			}
			return ""; //$NON-NLS-1$
		}

		public void modify(Object element, String property, Object value){
			if (element instanceof TableItem){
				Object o = ((TableItem)element).getData();
				if (o instanceof MLResSubstitution)				{
					MLResSubstitution sub = (MLResSubstitution)o;
//					if (ExternalizeMLRWizardPage.colNames[0].equals(property)){
//						sub.setState(((Integer)value).intValue());
//						mlrRefactoring.updateKey();
//						tableViewer.refresh();
//					} 
					if (ExternalizeMLRWizardPage.colNames[2].equals(property)){
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
					else if (ExternalizeMLRWizardPage.colNames[3].equals(property)){
						sub.setKey((String)value);
						tableViewer.refresh(o);
					} 
					else if (ExternalizeMLRWizardPage.colNames[4].equals(property)){
						String v = Helper.windEscapeChars((String)value);
						sub.setEnglishValue(v);
						tableViewer.refresh();
					} 
					else if (ExternalizeMLRWizardPage.colNames[5].equals(property)){
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
//				if (columnIndex == 0)
//					return ImageFactory.getImageBySubstitutionState(sub);
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex){
			String columnText = ""; //$NON-NLS-1$
			if (element instanceof MLResSubstitution){
				MLResSubstitution substitution = (MLResSubstitution)element;
//				if (ExternalizeMLRWizardPage.colNames[1].equals(property))
//				return ele.getOldKey().equals("")?"新增":"修改";
				
				if (columnIndex == 1)
					columnText = substitution.getState()==0?M_multilang.ExternalizeMLRWizardPage_6:substitution.getState()==1?M_multilang.ExternalizeMLRWizardPage_7:  
						substitution.getState()==2?M_multilang.ExternalizeMLRWizardPage_8:substitution.getState()==3?M_multilang.ExternalizeMLRWizardPage_9:substitution.getState()==4?M_multilang.ExternalizeMLRWizardPage_10:
							substitution.getState()==5?M_multilang.ExternalizeMLRWizardPage_11:M_multilang.ExternalizeMLRWizardPage_12;
				if (columnIndex == 2)
					columnText = Helper.unwindEscapeChars(substitution.getValue());
				else if (columnIndex == 3)
					columnText = substitution.getRealKey();
				else if (columnIndex == 4)
					columnText = Helper.unwindEscapeChars(substitution.getEnglishValue());
				else if (columnIndex == 5)
					columnText = Helper.unwindEscapeChars(substitution.getTwValue());
				else if (columnIndex == 6)
					columnText = substitution.getPageNode().getFile().getFullPath().lastSegment().toString();
			}
			return columnText;
		}
	}


	public ExternalizeMLRWizardPage(MLRRefactoring mlrRefactoring) {
		super(M_multilang.ExternalizeMLRWizardPage_13);
		this.mlrRefactoring = mlrRefactoring;
		tableViewer = null;
		fSourceViewer = null;
		prefixText = null;
		this.mlrRefactoring.setPage(this);
		this.mlrRefactoring.setResModuleName(this.mlrRefactoring.getFolder().getFullPath().segments()[1]);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, 0);
		container.setLayout(new GridLayout(2,false));
		
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//生成左边树
		createTreeViewer(container);
		
		SashForm form = new SashForm(container, 512);
		GridData layout = new GridData(GridData.FILL_BOTH);
		layout.widthHint=800;
		form.setLayoutData(layout);
		
		createTableViewer(form);
		createSourceViewer(form);
		
		setControl(container);
//		loadNodes();
//		System.out.print(pageNodes);
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
//			nodeIdChanged();
		}
		
	}
	
	private void createTableViewer(Composite parent){
		Composite comp = new Composite(parent, 0);
		GridLayout gl = new GridLayout(2, false);
		comp.setLayout(gl);
		Composite optionComp = new Composite(comp, 0);
//	    optionComp.setLayoutData(compGD);
	    optionComp.setLayout(new GridLayout(4, false));
	    OptionBtnSelectionAdapt selAdapt = new OptionBtnSelectionAdapt();
		this.includeChineseBtn = new Button(optionComp, 32);
	    this.includeChineseBtn.setText(M_multilang.ExternalizeMLRWizardPage_14);
	    this.includeChineseBtn.setSelection(this.mlrRefactoring.isIncludeChinese());
	    this.includeChineseBtn.addSelectionListener(selAdapt);
	    this.includeLetterBtn = new Button(optionComp, 32);
	    this.includeLetterBtn.setText(M_multilang.ExternalizeMLRWizardPage_15);
	    this.includeLetterBtn.setSelection(this.mlrRefactoring.isIncludeLetter());
	    this.includeLetterBtn.addSelectionListener(selAdapt);
	    this.includeDigitBtn = new Button(optionComp, 32);
	    this.includeDigitBtn.setText(M_multilang.ExternalizeMLRWizardPage_16);
	    this.includeDigitBtn.setSelection(this.mlrRefactoring.isIncludeDigit());
	    this.includeDigitBtn.addSelectionListener(selAdapt);
	    this.includeOtherBtn = new Button(optionComp, 32);
	    this.includeOtherBtn.setText(M_multilang.ExternalizeMLRWizardPage_17);
	    this.includeOtherBtn.setSelection(this.mlrRefactoring.isIncludeOther());
	    this.includeOtherBtn.addSelectionListener(selAdapt);
	    selAdapt.widgetSelected(null);
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
		columnLayoutData[6] = new ColumnWeightData(80, true);
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
				ExternalizeMLRWizardPage.this.selectionChanged(event);
			}
		});
	}
	
	private class OptionBtnSelectionAdapt extends SelectionAdapter
	  {
		
	    public void widgetSelected(SelectionEvent e)
	    {
	      boolean chinese =includeChineseBtn.getSelection();
	      boolean letter = includeLetterBtn.getSelection();
	      boolean digital = includeDigitBtn.getSelection();
	      boolean otherSign = includeOtherBtn.getSelection();
	      mlrRefactoring.updateSubstitutionsByOption(chinese, letter, digital, otherSign);
	      if (tableViewer != null)
//	        ExternalizeMLRWizardPage2.access$5(this.this$0).refresh();
	    	 tableViewer.refresh();
	    }
	  }
	
	private void selectionChanged(SelectionChangedEvent event)
	{
		IStructuredSelection selection = (IStructuredSelection)event.getSelection();
		updateSourceView(selection);
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
//				prefixText.setText(mlrRefactoring.getPrefix());
				refreshSoruseView();
			}
			
			Region region = first.getElement().getPosition();
			fSourceViewer.setSelectedRange(region.getOffset(), region.getLength());
			fSourceViewer.revealRange(region.getOffset(), region.getLength());
		}
	}
	
	public void refreshSoruseView(){
		if (mlrRefactoring.getCurrentPageNode() != null){
			org.eclipse.jface.text.IDocument document = new Document(mlrRefactoring.getContents().get(mlrRefactoring.getCurrentPageNode().getPath()));
			JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
			tools.setupJavaDocumentPartitioner(document);
			fSourceViewer.setDocument(document);
		}
	}
	public  PageNode[] loadNodes(){
		
		pageNodes = new ArrayList<PageNode>();
		List<PageNode> topNodes = new ArrayList<PageNode>();
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
		mlrRefactoring.createRawSubstitution(pageNodes);
		return topNodes.toArray(new PageNode[0]); 
	}
	
	public List<PageNode> getPageNodes() {
		return pageNodes;
	}

	public void setPageNodes(List<PageNode> pageNodes) {
		this.pageNodes = pageNodes;
	}

	private void createSourceViewer(Composite parent){
		Composite c = new Composite(parent, 0);
		c.setLayoutData(new GridData(1808));
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		c.setLayout(gl);
		Label l = new Label(c, 0);
		l.setText(M_multilang.ExternalizeMLRWizardPage_18);
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
//			if (mlrRefactoring.getCurrentPageNode() != null){
//				String contents = mlrRefactoring.getContents().get(mlrRefactoring.getCurrentPageNode().getPath());
//				org.eclipse.jface.text.IDocument document = new Document(contents);
//				tools.setupJavaDocumentPartitioner(document);
//				fSourceViewer.setDocument(document);
//			}
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
	 * 扫描文件夹
	 * @param pageNodes
	 * @param dir
	 */
	private void scanDir(List<PageNode> pageNodes, IFolder dir,PageNode parentNode){
		if(judgeIsJavaFolder(dir)){
			//加载目录下的 Java文件
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
				MainPlugin.getDefault().logError(e.getMessage(),e);
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
					String fileName = file.getName();
					if (fileName.endsWith(".java")){ //$NON-NLS-1$
						PageNode pageNode = new PageNode(file,parent.getResModuleName());
						pageNode.setParent(parent);
						pageNode.setRoot(parent.getRoot());
						pageNode.setBcpName(parent.getBcpName());
						String pathStr = file.getLocation().toString();
						String[] fullPath = file.getFullPath().segments();
						List<String> pathList =  Arrays.asList(fullPath);
						String prefix = ""; //$NON-NLS-1$
						pageNode.setPrefix(prefix);
						parent.getChildrens().add(pageNode);
						pageNodes.add(pageNode);
					}
				}
			}
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		
		
	}
	/**
	 * 判断是否含java文件
	 * @param fold
	 * @return
	 */
	private boolean judgeIsJavaFolder(IFolder dir) {
		try {
			for (int i = 0 ; i<dir.members().length ; i++){
				IResource resorce = dir.members()[i];
				if (resorce instanceof IFile){
					String fileName = resorce.getName();
					if (fileName.endsWith(".java")){ //$NON-NLS-1$
						return true;
					}
				}
			}
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
			return false;
		}
		return false;
	}

	public MLRRefactoring getMlrRefactoring() {
		return mlrRefactoring;
	}
	
	

}
