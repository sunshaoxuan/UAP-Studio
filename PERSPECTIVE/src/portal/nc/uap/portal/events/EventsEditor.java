package nc.uap.portal.events;

import java.util.EventObject;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.views.ILFWViewPage;
import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.events.page.EventsViewPage;
import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * 事件编辑器
 * 
 * @author dingrf
 * 
 */
public class EventsEditor extends LFWBaseEditor {

	/** 事件元素 */
	private EventsElementObj contents = new EventsElementObj();

	/** 属性模型视图 */
	private LFWAbstractViewPage viewPage;

	public LFWAbstractViewPage getViewPage() {
		return viewPage;
	}

	public EventsEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	/**
	 * 文件变化通知保存
	 */
	@Override
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}

	public void setFocus() {
		super.setFocus();
		IEditorInput input = getEditorInput();
		if (input instanceof EventsEditorInput) {
			EventsEditorInput eventsInput = (EventsEditorInput) input;
			try {
				TreeItem selectedTI = eventsInput.getCurrentTreeItem();
				if (selectedTI != null && !selectedTI.isDisposed()) {
					LFWAMCPersTool.getTree().setSelection(selectedTI);
					eventsInput.setCurrentTreeItem(selectedTI);
				} else {
					this.setDirtyFalse();
					MessageDialog.openError(null, "提示", "当前编辑器已失效,请关闭并重新打开!");
				}
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e);
			}
		}
	}

	/**
	 * 取当前活动的EventsEditor
	 * 
	 * @return
	 */
	public static EventsEditor getActiveEditor() {
		// IWorkbenchPage page = PortalPlugin.getDefault().getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		// if (page != null) {
		// editor = page.getActiveEditor();
		// }
		editor = LFWBaseEditor.getActiveEditor();
		if (editor != null && editor instanceof EventsEditor) {
			return (EventsEditor) editor;
		} else {
			return null;
		}
	}

	@Override
	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		EventsEditorInput pEditorInput = (EventsEditorInput) input;
		PortletApplicationDefinition portletApp = pEditorInput.getPortletApp();

		EventsElementObj portletAppEle = new EventsElementObj();
		portletAppEle.setPortletApp(portletApp);
		portletAppEle.setEventDefinitions((List<EventDefinition>) portletApp
				.getEventDefinitions());

		portletAppEle.setLocation(new Point(100, 100));
		contents = portletAppEle;
	}

	public void executComand(Command cmd) {
		super.getCommandStack().execute(cmd);
	}

	/**
	 * EventsEditor 图形初始化
	 */
	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(this.contents);
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new EventsEditPartFactory());
	}

	/**
	 * 保存
	 * 
	 * @param monitor
	 */
	public void doSave(IProgressMonitor monitor) {
		EventsElementObj portletAppEle = (EventsElementObj) contents;
		savePortletApp(portletAppEle.getPortletApp());
	}

	/**
	 * 保存portlet-app定义
	 * 
	 * @param portletApp
	 */
	@SuppressWarnings("unchecked")
	public void savePortletApp(PortletApplicationDefinition portletApp) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortletApplicationDefinition pa = PortalConnector.getPortletApp(
				projectPath, projectModuleName);
		if (pa == null) {
			pa = portletApp;
		} else {
			pa.setDefaultNamespace(portletApp.getDefaultNamespace());
			pa.getEventDefinitions().clear();
			pa.getEventDefinitions().addAll(
					(List) portletApp.getEventDefinitions());
		}
		PortalConnector.savePortletAppToXml(projectPath, projectModuleName, pa);
		getCommandStack().markSaveLocation();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class cls) {
		if (cls == ILFWViewPage.class) {
			this.viewPage = new EventsViewPage();
			return this.viewPage;
		}
		return super.getAdapter(cls);
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		return new EventsViewPage();
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
		// TODO 自动生成的方法存根

	}
}
