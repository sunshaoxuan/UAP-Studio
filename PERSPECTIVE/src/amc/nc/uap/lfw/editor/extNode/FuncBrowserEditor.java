package nc.uap.lfw.editor.extNode;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.uap.lfw.editor.browser.SimpleBrowser;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.listener.EventEditorHandler;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ISaveablePart2;

import uap.lfw.lang.M_extNode;

public class FuncBrowserEditor extends LFWBaseEditor implements ISaveablePart2{
	
	private IEditorSite site;
	
	private IEditorInput input;
	
	private SimpleBrowser mozilla = null;

	public FuncBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	public SimpleBrowser createSimpleBrowser(Composite parent) {
		SimpleBrowser browser = new SimpleBrowser(parent, input);
		return browser;
	}
	
	public void execute(String script){
		mozilla.execute(script);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.site = site;
		this.input = input;
	}
	@Override
	public void setFocus() {
		LFWTool.webBrowserInitViews();
	}
	
	@Override
	public void createPartControl(Composite parent) {
//		initializeGraphicalViewer();
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
		mozilla = createSimpleBrowser(comp);
		mozilla.createBrowser();
		setEventHandler(new EventEditorHandler());
//		setDirtyFalse();
//		setDirtyTrue();
	}

	public static FuncBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor instanceof FuncBrowserEditor){
			return (FuncBrowserEditor)editor;
		}else {
			return null;
		}
	}
	

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public LFWAbstractViewPage createViewPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
		
	}


	@Override
	public IEditorInput getEditorInput() {
		return input;
	}
	@Override
	public IEditorSite getEditorSite() {
		return site;
	}

	public SimpleBrowser getMozilla() {
		return mozilla;
	}

	public void setMozilla(SimpleBrowser mozilla) {
		this.mozilla = mozilla;
	}

	public void setSite(IEditorSite site) {
		this.site = site;
	}
	
	public IEditorSite getSite() {
		return site;
	}

	public void setInput(IEditorInput input) {
		this.input = input;
	}
	
	public IEditorInput getInput() {
		return input;
	}

	@Override
	public int promptToSaveOnClose() {
		if(MessageDialog.openConfirm(null, M_extNode.FuncBrowserEditor_0000/*提示*/, M_extNode.FuncBrowserEditor_0001/*关闭编辑器未保存的流程内容将会丢失，是否确定关闭*/)){
			return ISaveablePart2.YES;
		}
		else return ISaveablePart2.CANCEL;
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		setDirtyTrue();
	}
	
//	/**
//	 * 关闭编辑器时执行
//	 */
//	 public void dispose() {		
//		 if(MessageDialog.openConfirm(null, "提示", "????")){
//			 super.dispose();
//		 }		 
//	}

}
