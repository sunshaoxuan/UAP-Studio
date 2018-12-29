package nc.uap.lfw.perspective;
//import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class PerspectiveFactory implements IPerspectiveFactory {

	protected IPageLayout	layout;
	public void createInitialLayout(IPageLayout layout) {
		this.layout = layout;
		String editorArea = layout.getEditorArea();
//		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
//		left.addView("nc.uap.lfw.perspective.project.LFWExplorerTreeView");
		
		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, layout.getEditorArea());
//		folder.addView("org.eclipse.ui.navigator.ProjectExplorer");
		folder.addView(JavaUI.ID_PACKAGES);
		//folder.addView(JavaUI.ID_TYPE_HIERARCHY);
		folder.addView("nc.uap.lfw.perspective.project.LFWExplorerTreeView");
		folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, editorArea);
		bottom.addView(LFWViewSheet.class.getCanonicalName());

		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorArea);
		right.addView(IPageLayout.ID_PROP_SHEET);
		
		//addActionSets();
		//addNewWizardShortcuts();
		//addViewShortcuts();
		
	}
	
	private void addActionSets()
	{
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
	}

	private void addNewWizardShortcuts()
	{
		// new actions - Java project creation wizard
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");
		//
	}

	private void addViewShortcuts()
	{
		// views - java
		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		//layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
		//layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);
		//layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);
		// views - search
		//layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);
		// views - debugging
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		//layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		//layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		//layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
	//	layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
	}
}
