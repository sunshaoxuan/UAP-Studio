/**
 * 
 */
package nc.uap.lfw.editor.common.editor;

import java.util.HashMap;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.browser.MozillaBrowser;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.perspective.listener.EventEditorHandler;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * @author chouhl
 * 2011-11-17
 */
public abstract class LFWBrowserEditor extends LFWBaseEditor {
	
	private IEditorSite site;
	
	private IEditorInput input;
	
	private MozillaBrowser mozilla = null;
	
	private LfwView view = null;
	
	private UIMeta uimeta = null;
	
	private String pageMetaId = null;
	
	private String widgetId = null;
	
	private String nodePath = null;
	
	private Map<String, UIElement> elementMap = null;
	
	public Map<String, UIElement> getElementMap() {
		if(elementMap == null){
			elementMap = new HashMap<String, UIElement>();
		}
		return elementMap;
	}

	public void setElementMap(Map<String, UIElement> elementMap) {
		this.elementMap = elementMap;
	}

	public abstract MozillaBrowser createMozillaBrowser(Composite parent);
	
	public void execute(String script){
		mozilla.execute(script);
	}
	
	public String getSessionId(){
		return mozilla.getSessionId();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.site = site;
		this.input = input;
	}
	
	@Override
	public void doSaveAs() {
		setDirtyFalse();
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void setFocus() {
//		LFWTool.webBrowserInitViews();
		
		LfwBaseEditorInput browserEditorInput = (LfwBaseEditorInput)this.getInput();
		TreeItem selectedItem = browserEditorInput.getCurrentTreeItem();
		if(!selectedItem.isDisposed()){
			LFWAMCPersTool.getTree().setSelection(selectedItem);
		}
	}
	
	
	
	@Override
	public void createPartControl(Composite parent) {
		initializeGraphicalViewer();
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
		mozilla = createMozillaBrowser(comp);
		mozilla.createBrowser();
		setEventHandler(new EventEditorHandler());
		setDirtyFalse();
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		this.nodePath = LFWAMCPersTool.getCurrentFolderPath();
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		return new LFWBrowserViewPage();
	}
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()){
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
	
	@Override
	public IEditorInput getEditorInput() {
		return input;
	}
	@Override
	public IEditorSite getEditorSite() {
		return site;
	}

	public MozillaBrowser getMozilla() {
		return mozilla;
	}

	public void setMozilla(MozillaBrowser mozilla) {
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
	
	public static LFWBrowserEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof LFWBrowserEditor){
			return (LFWBrowserEditor)editor;
		}else {
			return null;
		}
	}
	
	@Override
	protected TreeItem getSelectedTreeItem(LFWDirtoryTreeItem dirTreeItem,
			LfwBaseEditorInput editorInput) {
		return null;
	}
	
	@Override
	public void dispose() {
//		mozilla.dispose();
		super.dispose();
//		LFWAMCConnector.clearSessionCache(getSessionId());
	}
	
	public LfwView getView() {
		return view;
	}

	public void setView(LfwView view) {
		this.view = view;
	}

	public String getPageMetaId() {
		return pageMetaId;
	}

	public void setPageMetaId(String pageMetaId) {
		this.pageMetaId = pageMetaId;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public UIMeta getUimeta() {
		return uimeta;
	}

	public void setUimeta(UIMeta uimeta) {
		this.uimeta = uimeta;
	}
	
}
