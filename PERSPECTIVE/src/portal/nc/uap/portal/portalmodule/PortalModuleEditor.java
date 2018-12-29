package nc.uap.portal.portalmodule;


import java.util.EventObject;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 *  PortalModule Editor
 * 
 * @author dingrf
 * 
 */
public class PortalModuleEditor extends GraphicalEditor {

	/**	PortalModule Model*/
	private PortalModuleElementObj contents = new PortalModuleElementObj();
	
	public PortalModuleEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	
	public static PortalModuleEditor getActiveEditor() {
		IWorkbenchPage page = PortalPlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if (page != null) {
			editor = page.getActiveEditor();
		}
		if (editor != null && editor instanceof PortalModuleEditor) {
			return (PortalModuleEditor) editor;
		} else {
			return null;
		}
	}
	
	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	public boolean isSaveAsAllowed() {
		return false;
	}
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		PortalModuleEditorInput pEditorInput = (PortalModuleEditorInput) input;
		PortalModule portalModule = pEditorInput.getPortalModule();
		
		PortalModuleElementObj portalEle = new PortalModuleElementObj();
		portalEle.setPortalModule(portalModule);
		portalEle.setLocation(new Point(100, 100));
		contents = portalEle;
	}
	
	public void executComand(Command cmd){
		super.getCommandStack().execute(cmd);
	}
	
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(this.contents);
	}
	
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}

	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new PortalModuleEditPartFactory());
	}

	public void doSave(IProgressMonitor monitor) {
		PortalModuleElementObj themeEle = (PortalModuleElementObj) contents;
		savePortalModule(themeEle.getPortalModule());
	}

	/**
	 * save PortalModule to file
	 */
	public void savePortalModule(PortalModule portalModule) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortalConnector.savePortalToXml(projectPath, projectModuleName, portalModule);
		getCommandStack().markSaveLocation();
	}
}
