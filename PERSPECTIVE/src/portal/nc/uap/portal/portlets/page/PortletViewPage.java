package nc.uap.portal.portlets.page;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.PortletElementPart;
import nc.uap.portal.portlets.page.InitParamPropertiesView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * Portal页面实现类
 * 
 * @author dingrf
 * 
 */
public class PortletViewPage extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private TabItem supportsTabItem = null;
	private TabItem preferenceTabItem = null;
	private TabItem processingEventTabItem = null;
	private TabItem publishingEventTabItem = null;
	private SupportsPropertiesView  supportsView = null;
	private PreferencePropertiesView  preferenceView = null;
	private ProcessingEventPropertiesView  processingEventView = null;
	private PublishingEventPropertiesView  publishingEventView = null;
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// InitParam属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText("InitParam");
		
		//Supports属性页
		supportsTabItem = new TabItem(voTabFolder, SWT.NONE);
		supportsTabItem.setText("Supports");
		
		//preferences属性页
		preferenceTabItem = new TabItem(voTabFolder, SWT.NONE);
		preferenceTabItem.setText("PortletPreferences");

		//processingEvent属性页
		processingEventTabItem = new TabItem(voTabFolder, SWT.NONE);
		processingEventTabItem.setText("SupportedprocessingEvent");

		//processingEvent属性页
		publishingEventTabItem = new TabItem(voTabFolder, SWT.NONE);
		publishingEventTabItem.setText("SupportedpublishingingEvent");

		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		
		setCellPropertiesView(new InitParamPropertiesView(voTabFolder, SWT.NONE));
	}

	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet)
			return;
		if (part == null || selection == null) {
			return;
		} 
		else if (part instanceof PortletEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof PortletElementPart) {
				// (DatasetElementObj) dsobj = (DatasetElementObj)sel.;
				PortletElementPart eEle = (PortletElementPart) sel;
				Object model = eEle.getModel();
				if (model instanceof PortletElementObj) {
					PortletElementObj obj = (PortletElementObj) model;
					
					//initparam
					List<InitParam> initParams = obj.getInitParams();
					((InitParamPropertiesView) getCellPropertiesView()).getTv().setInput(initParams);
					((InitParamPropertiesView) getCellPropertiesView()).setPortletElementPart(eEle);
					((InitParamPropertiesView) getCellPropertiesView()).getTv().expandAll();
					sl.topControl = voTabFolder;
					setCellPropTabItem(cellPropTabItem);
					addCellPropItemControl();
					//supports
					List<Supports> supports = obj.getSupports();
					supportsView = new SupportsPropertiesView(voTabFolder, SWT.NONE);
					supportsView.getTv().setInput(supports);
					supportsView.setPortletElementPart(eEle);
					supportsView.getTv().expandAll();
					supportsTabItem.setControl(supportsView);
					//perferences
					List<Preference> preference = obj.getPreferences();
					preferenceView = new PreferencePropertiesView(voTabFolder, SWT.NONE);
					preferenceView.getTv().setInput(preference);
					preferenceView.setPortletElementPart(eEle);
					preferenceView.getTv().expandAll();
					preferenceTabItem.setControl(preferenceView);
					//processingEvent
					List<EventDefinitionReference> processingEvent = obj.getSupportedProcessingEvents();
					processingEventView = new ProcessingEventPropertiesView(voTabFolder, SWT.NONE);
					processingEventView.getTv().setInput(processingEvent);
					processingEventView.setPortletElementPart(eEle);
					processingEventView.getTv().expandAll();
					processingEventTabItem.setControl(processingEventView);
					//publishingEvent
					List<EventDefinitionReference> publishingEvent = obj.getSupportedPublishingEvents();
					publishingEventView = new PublishingEventPropertiesView(voTabFolder, SWT.NONE);
					publishingEventView.getTv().setInput(publishingEvent);
					publishingEventView.setPortletElementPart(eEle);
					publishingEventView.getTv().expandAll();
					publishingEventTabItem.setControl(publishingEventView);

					comp.layout();
					if (!comp.isVisible())
						comp.setVisible(true);
					
				}
			} else {
				comp.setVisible(false);
			}
		}
	}
}
