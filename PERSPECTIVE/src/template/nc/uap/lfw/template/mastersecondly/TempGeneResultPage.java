package nc.uap.lfw.template.mastersecondly;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.template.CreateTempWindowPage;
import nc.uap.lfw.template.NewTempleteWindowWizard;
import nc.uap.lfw.template.ResourceShowUtil;
import nc.uap.lfw.template.ResultVO;
import nc.uap.lfw.template.TempGeneUtil;
import nc.uap.lfw.template.TempNextUsedWizardPage;
import nc.uap.lfw.template.tools.LfwTemplateTool;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TempGeneResultPage extends TempNextUsedWizardPage implements Runnable{

	
	private TableViewer tableViewer;
	private Label labelExecuting;
	private ProgressBar progressBarExecuting;
	private Application app = null;
	private String projectPath = LFWAMCPersTool.getProjectPath();
	private IProject project = LFWPersTool.getCurrentProject();
	private String tempType = null;
	private Map<String,Object> dataMap = new HashMap<String, Object>();
	
	private List<ResultVO> allResVOList = new ArrayList<ResultVO>();
	
	/**
	 * Create the wizard.
	 */
	public TempGeneResultPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(uap.lfw.lang.M_template.TempGeneResultPage_0001/*显示运行结果*/);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		tableViewer = new TableViewer(container, SWT.BORDER| SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(0);
		tableColumn.setAlignment(SWT.CENTER);
		tableColumn.setText("");
		// 创建表头
		TableColumn tableColumn0 = new TableColumn(table, SWT.NONE);
		tableColumn0.setWidth(100);
//		tableColumn0.setAlignment(SWT.CENTER);
		tableColumn0.setText("Step");

		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(500);
//		tableColumn1.setAlignment(SWT.CENTER);
		tableColumn1.setText("Resource");

		TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(100);
//		tableColumn2.setAlignment(SWT.CENTER);
		tableColumn2.setText("Complete");
		// 设置表头和表格线可见
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		labelExecuting = new Label(container, SWT.NONE);
		labelExecuting.setText("");
		labelExecuting.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		progressBarExecuting = new ProgressBar(container, SWT.NONE);
		progressBarExecuting.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		// 设置数据
		tableViewer.setContentProvider(new MyContentProvider());
		// 设置视图
		tableViewer.setLabelProvider(new MyLabelProvider());
		
	}

	
	public class MyContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object[] getElements(Object inputElement) {
			// 将初始化数据的入口对象转换成表格使用的数组对象
			return ((List) inputElement).toArray();
		}

	}
	public class MyLabelProvider implements ITableLabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {

		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			ResultVO allResourceVO = (ResultVO) element;
			if (columnIndex == 3) {// 如果是第三列
				if( "ok".equals(allResourceVO.getResult())){
				
					return ImageProvider.succeed;
				}else if( "error".equals(allResourceVO.getResult())){
				
					return ImageProvider.fail;
				}else if( "running".equals(allResourceVO.getResult())){
					
					return ImageProvider.wait;
				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// 类型转换，element代表表格中的一行
			ResultVO allResourceVO = (ResultVO) element;
			if (columnIndex == 1) {// 如果是第一列
				return allResourceVO.getStep() + ""; //$NON-NLS-1$
			} else if (columnIndex == 2) {// 如果是第二列
				return allResourceVO.getResource() + ""; //$NON-NLS-1$
			} else if (columnIndex == 3) {// 如果是第三列
				return null; //$NON-NLS-1$
			} else
				return null;
		}
	}
	
	public void setVisible(boolean visible){
		super.setVisible(visible);
		if(visible){
			this.setPageComplete(false);
			
		}
	}
	
	/**
	 * 设置NEXT按钮不可用
	 */
	public boolean canFlipToNextPage() {
		return false;
	}

	/**
	 * 设置BACK按钮
	 */
//	public IWizardPage getPreviousPage() {
//		return null;
//	}
	
	@Override
	public void run() {	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		TempGeneUtil tempGen = new TempGeneUtil(allResVOList, app, tableViewer, labelExecuting, progressBarExecuting, (NewTempleteWindowWizard)getWizard(), tempType);
		tempGen.setProjectPath(projectPath);
		tempGen.setProject(project);
		tempGen.setCurrentPage(this);
		tempGen.setDataMap(dataMap);
		tempGen.doRunGeneProcess();
	}
	
	public void setAllResList() {
		CreateTempWindowPage startPage = (CreateTempWindowPage)((NewTempleteWindowWizard)getWizard()).getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		tempType = startPage.getTempTitle();
		new ResourceShowUtil(allResVOList, tempType).setTableViewNeedShowAllRes();
		tableViewer.setInput(allResVOList);
		progressBarExecuting.setMaximum(allResVOList.size());
		app = LFWAMCPersTool.getCurrentApplication();		
	}

	public ProgressBar getProgressBarExecuting() {
		return progressBarExecuting;
	}

	@Override
	protected boolean nextButtonClick() {
		return true;
	}

	@Override
	protected boolean previousButtonClick() {
		if(MessageDialog.openConfirm(null, "", uap.lfw.lang.M_template.TempGeneResultPage_0000/*此操作会撤销已经生成的结果,返回未模式化之前的状态,是否确认返回上一步*/)){
			for(WindowConfig winConf:app.getWindowList()){
				LfwWindow win = LFWAMCConnector.getWindowById(winConf.getId());
				String path = win.getFoldPath();
				File uiFile = ((LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem().getParentItem().getParentItem()).getFile();
				String winFullId = win.getFullId();
				File winFile = new File(uiFile,"nodes/"+winFullId.replace(".", "/"));
				FileUtilities.deleteFile(winFile);
			}
			app.setWindowList(new ArrayList<WindowConfig>(1));
			Application app1 = (Application)app.clone();
			for(Connector conn:app1.getConnectors()){
				app.removeConnector(conn.getId());
			}
			app.setDefaultWindowId(null);
			LFWSaveElementTool.saveApplication(app);
			try {
				LFWPersTool.getCurrentProject().refreshLocal(2, null);
			} catch (CoreException e) {
				MainPlugin.getDefault().logError(e);
			}
			String nodeCode = (String)dataMap.get("nodeCode");
			LfwTemplateTool.deleteTemplate(nodeCode);
			if(tempType.equals(M_template.MasterSecondlyFlowFactory_0)){
				String flwTypePk = (String)dataMap.get("flwTypePk");
				if(flwTypePk!=null)
					LfwTemplateTool.deleteFlw(flwTypePk);
			}
			return true;
		}
		return false;
	}
	

}
