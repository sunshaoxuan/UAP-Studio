package nc.uap.portal.page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleEvent;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OleListener;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * PortalPage Editor
 * 
 * @author dingrf
 *
 */
public class PortalPageEditor extends EditorPart {

	private IEditorSite site;
	private IEditorInput input;
	private boolean isDirty = false;
	private PortalPageTreeItem pageTreeItem;
	private OleAutomation automation;
	private PortalPageEditor portalPageEditor;
	
	public PortalPageTreeItem getPageTreeItem() {
		return pageTreeItem;
	}

	public OleAutomation getAutomation() {
		return automation;
	}

	public void doSave(IProgressMonitor monitor) {
		int[] methodIDs = automation
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\"saveXml\" returntype=\"xml\"><arguments></arguments></invoke>";
		Variant[] methodArgs = {
				new Variant(arg) };
		automation.invoke(methodIDs[0], methodArgs);
	}

	public void doSaveAs() {
		isDirty = false;
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.site = site;
		this.input = input;
		this.pageTreeItem = ((PortalPageEditorInput)input).getpageTreeItem();
		this.portalPageEditor = this;
	}

	
	public boolean isDirty() {
		return isDirty;
	}
	
	public void setDirty(Boolean isDirty) {
		this.isDirty = isDirty;
		this.firePropertyChange(PROP_DIRTY);
	}

	
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}	

	public static PortalPageEditor getActiveEditor(){
		IWorkbenchPage page = PortalPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof PortalPageEditor){
			return (PortalPageEditor)editor;
		}else {
			return null;
		}
		
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		OleFrame oleFrame = new OleFrame(parent, SWT.NONE);
		oleFrame.setLayoutData(new GridData(GridData.FILL_BOTH));
		OleControlSite controlSite = new OleControlSite(oleFrame, SWT.NONE,
		"ShockwaveFlash.ShockwaveFlash");
		controlSite.doVerb(OLE.OLEIVERB_SHOW);
		automation = new OleAutomation(controlSite);
		controlSite.addEventListener(150, new OleListener(){

			public void handleEvent(OleEvent event) {
				Variant command = event.arguments[0];
				Variant arguments = event.arguments[1];
				
				/**
				 * 调用方法参数规则：
				 * param1:this
				 * param3:callback(回调函数)
				 */
				try {
					String method = command.getString();
					// String method = "loadPortletCates";
					String arg1 = arguments.getString();
					String[] stringArgs = arg1.split("&");
					Object[] args = new Object[stringArgs.length + 1];
					args[0] = portalPageEditor;

					for (int i = 0; i < stringArgs.length; i++) {
						args[i+1] =  stringArgs[i].substring(stringArgs[i].indexOf("=")+1, stringArgs[i].length());
					}

					IPageDataProvider pageDataProvider = (IPageDataProvider)Class.forName("nc.uap.portal.page.PageDataProviderImpl").newInstance();
					Class<?>[] argsClass = new Class[args.length];
					int i = 0;
					for (Object o : args) {
						argsClass[i] = o.getClass();
						i++;
					}
					Method m = IPageDataProvider.class.getMethod(method, argsClass);
					m.invoke(pageDataProvider, args);
				} catch (Exception e) {
					PortalPlugin.getDefault().logError(e.getMessage(), e);
				}
			}

		});
		int[] methodIDs = automation
		.getIDsOfNames(new String[] { "LoadMovie" });
		String swfClassPath = "nc/uap/portal/page/portletdesign.swf";
		File f = new File(LFWPersTool.getCurrentProject().getLocation().toString() + "/bin/");
		if (!f.exists())
			f.mkdirs();
		String swfFilePath = f.getAbsolutePath() + "/portletdesign.swf";
		File swfFile = new File(swfFilePath);
		final byte[] bufsize = new byte [1024*8];
		if(swfFile.exists()){
			swfFile.delete();
		}
		InputStream ins = null;
		OutputStream os = null;
		try {
			ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(swfClassPath);
			os = new FileOutputStream(swfFile);
			int len = -1;
			while ((len = ins.read(bufsize)) != -1) {
				os.write(bufsize, 0, len);
				os.flush();
			}
		} catch (Exception e) {
			PortalPlugin.getDefault().logError(e.getMessage(), e);
		}
		finally{
			try {
				if(ins != null)
					ins.close();
			} catch (IOException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
			try {
				if(os != null)
					os.close();
			} catch (IOException e) {
				PortalPlugin.getDefault().logError(e.getMessage(), e);
			}
		}
		Variant[] methodArgs = { new Variant(0),
				new Variant(swfFilePath+"?model=eclipse")};
		automation.invoke(methodIDs[0], methodArgs);
		
		this.setDirty(true);
	}

	
	public void setFocus() {
	}

	public IEditorInput getEditorInput() {
		return input;
	}
	
	public IEditorSite getEditorSite() {
		return site;
	}
	
	public IWorkbenchPartSite getSite() {
		return site;
	}

}
