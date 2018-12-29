package nc.uap.portal.skin;

import nc.uap.portal.freemarker.Editor;


import nc.lfw.editor.common.tools.LFWPersTool;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * 样式编辑器
 * 
 * @author dingrf
 *
 */
public class SkinEditor extends MultiPageEditorPart {

	private IEditorSite site;
	
	private IEditorInput input;
	
	/**主题ID*/
	private String themeId;
	
	/**样式分类*/
	private String type;
	
	/**样式ID*/
	private String id;

	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}
	
	public void doSave(IProgressMonitor monitor) {
		for (int i = 0 ; i< getPageCount(); i ++){
			getEditor(i).doSave(monitor);
		}
	}

	@Override
	public void doSaveAs() {
		
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.site = site;
		this.input = input;
		SkinEditorInput pEditorInput = (SkinEditorInput) input;
		this.themeId = pEditorInput.getThemeId();
		this.type =  pEditorInput.getType();
		this.id = pEditorInput.getId();
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	
	@Override
	public void setFocus() {
	}

	@Override
	public IEditorInput getEditorInput() {
		return input;
	}

	@Override
	public IEditorSite getEditorSite() {
		return site;
	}

	@Override
	public IWorkbenchPartSite getSite() {
		return site;
	}

	/**
	 * 创新ftl页、css页、js页
	 * 
	 */
	@Override
	protected void createPages() {
		IProject project = LFWPersTool.getCurrentProject();
		IFile ftlfile = project.getFile("/src/portalspec/"+ project.getName() +"/portalspec/ftl/portaldefine/skin/" + themeId + "/" + type + "/"+ id +".ftl");
		createPage(ftlfile);

		IFile cssfile = project.getFile("/src/portalspec/"+ project.getName() +"/portalspec/ftl/portaldefine/skin/" + themeId + "/" + type + "/"+ id +".css");
		if (cssfile.exists()){
			createPage(cssfile);
		}
		IFile jsfile = project.getFile("/src/portalspec/"+ project.getName() +"/portalspec/ftl/portaldefine/skin/" + themeId + "/" + type + "/"+ id +".js");
		if (jsfile.exists()){
			createPage(jsfile);
		}
	}
	
	private void createPage(IFile file) {
		try {
			int index = addPage(new Editor(), new FileEditorInput(file));
			setPageText(index, file.getName()); 
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
				"Error creating nested text editor",null,e.getStatus()); //$NON-NLS-1$
		}
	}

}
