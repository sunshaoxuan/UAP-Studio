package nc.uap.portal.perspective;

import nc.uap.lfw.perspective.LFWViewSheet;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * 创建PortalPerspective
 * 
 * @author dingrf
 *
 */
public class PortalPerspectiveFactory implements IPerspectiveFactory {
	protected IPageLayout	layout;

	// views
	private static final String ID_MDE_NAVIGATOR = "nc.uap.studio.navigate.mde.viewMDE";

	private static final String ID_CONSOLE_VIEW = "org.eclipse.ui.console.ConsoleView";

	private static final String ID_SEARCH_VIEW = "org.eclipse.search.ui.views.SearchView";

	private static final String ID_PACKAGES = "org.eclipse.jdt.ui.PackageExplorer";

	private static final String ID_PROGRESS_VIEW = "org.eclipse.ui.views.ProgressView";

	private static final String ID_SOURCE_VIEW = "org.eclipse.jdt.ui.SourceView"; //$NON-NLS-1$

	private static final String ID_JAVADOC_VIEW = "org.eclipse.jdt.ui.JavadocView"; //$NON-NLS-1$

	private static final String ID_TYPE_HIERARCHY = "org.eclipse.jdt.ui.TypeHierarchy";

	private static final String ID_PDE_LOG_VIEW = "org.eclipse.pde.runtime.LogView";
	public void createInitialLayout(IPageLayout layout) {
		this.layout = layout;
		String editorArea = layout.getEditorArea();
//		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
//		left.addView("nc.uap.lfw.perspective.project.LFWExplorerTreeView");
		
		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, layout.getEditorArea());
//		folder.addView("org.eclipse.ui.navigator.ProjectExplorer");
		//folder.addView(JavaUI.ID_TYPE_HIERARCHY);

		folder.addView("nc.uap.portal.perspective.PortalExplorerTreeView");
		folder.addView(JavaUI.ID_PACKAGES);
		folder.addView("com.yonyou.studio.common.disk.ui.view.UAPDiskCreatorTreeView");
		folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, editorArea);
		bottom.addView(LFWViewSheet.class.getCanonicalName());
		bottom.addView("org.eclipse.ui.console.ConsoleView");

		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorArea);
		right.addView(IPageLayout.ID_PROP_SHEET);
		
		
		//addActionSets();
		//addNewWizardShortcuts();
		//addViewShortcuts();
		
		// ActionSet
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet");
		layout.addActionSet("org.eclipse.jdt.ui.JavaActionSet");
		layout.addActionSet("org.eclipse.jdt.ui.JavaElementCreationActionSet");
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - mde
		layout.addShowViewShortcut(ID_MDE_NAVIGATOR);
		// views - java
		layout.addShowViewShortcut(ID_PACKAGES);
		layout.addShowViewShortcut(ID_TYPE_HIERARCHY);
		layout.addShowViewShortcut(ID_SOURCE_VIEW);
		layout.addShowViewShortcut(ID_JAVADOC_VIEW);
		// views - search && debug
		layout.addShowViewShortcut(ID_SEARCH_VIEW);
		layout.addShowViewShortcut(ID_CONSOLE_VIEW);
		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(ID_PROGRESS_VIEW);
//		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		layout.addShowViewShortcut(ID_PDE_LOG_VIEW); //$NON-NLS-1$

		// new actions - Business Component Project Wizard
		layout.addNewWizardShortcut("nc.uap.mde.NewBCProjectWizard");
		layout.addNewWizardShortcut("nc.uap.mde.ui.bcp.wizard.addnewbcp");
		// new actions - Java project creation wizard
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewJavaWorkingSetWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$

		// Perspective - NC模型设计器
		layout.addPerspectiveShortcut("ncmdp.perspective.PerspectiveFactory");
		// Perspective - Java & Debug
//		layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
		layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective");
	}
}
