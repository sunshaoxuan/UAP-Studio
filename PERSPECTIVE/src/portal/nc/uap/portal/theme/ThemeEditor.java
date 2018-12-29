package nc.uap.portal.theme;


import java.util.ArrayList;
import java.util.EventObject;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Theme;
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
 *  Theme Editor
 * 
 * @author dingrf
 * 
 */
public class ThemeEditor extends GraphicalEditor {

	/**LookAndFeel Model*/
	private ThemeElementObj contents = new ThemeElementObj();
	
	public ThemeEditor() {
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
	
	public static ThemeEditor getActiveEditor() {
		IWorkbenchPage page = PortalPlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if (page != null) {
			editor = page.getActiveEditor();
		}
		if (editor != null && editor instanceof ThemeEditor) {
			return (ThemeEditor) editor;
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
		ThemeEditorInput pEditorInput = (ThemeEditorInput) input;
		LookAndFeel lookAndFeel = pEditorInput.getLookAndFeel();
		
		ThemeElementObj themeEle = new ThemeElementObj();
		themeEle.setLookAndFeel(lookAndFeel);
		if (lookAndFeel.getTheme()==null){
			lookAndFeel.setTheme(new ArrayList<Theme>());
		}
		themeEle.setThemes(lookAndFeel.getTheme());
		
		themeEle.setLocation(new Point(100, 100));
		contents = themeEle;
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
		getGraphicalViewer().setEditPartFactory(new ThemeEditPartFactory());
	}

	public void doSave(IProgressMonitor monitor) {
		ThemeElementObj themeEle = (ThemeElementObj) contents;
		saveLookAndFeel(themeEle.getLookAndFeel());
	}

	/**
	 * ±£´æ LookAndFeel 
	 * 
	 */
	public void saveLookAndFeel(LookAndFeel lookAndFeel) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortalConnector.saveLookAndFeelToXml(projectPath, projectModuleName, lookAndFeel);
		getCommandStack().markSaveLocation();
	}
}
