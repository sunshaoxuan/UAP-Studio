package nc.uap.portal.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;


import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.wizards.NewWebModuleProjectWizard;
import nc.uap.portal.core.PortalProjConstants;

/**
 * 新建Portal（LFW工程）
 * 
 * @author dingrf
 *
 */
public class NewLFWPortalModuleProjectWizard extends NewWebModuleProjectWizard {

	/**portal工程的默认依赖模块*/
	private final String DEFAULT_DEPENDS = "pserver"; 
	
	public NewLFWPortalModuleProjectWizard(){
		setWindowTitle(PortalProjConstants.KEY_NEW_PORTALPRJ_MAINPAGE_TITLE);
	}
	

	protected void addPathPage() {
		fNewModuleWebPage = new PortalModuleWebContextPage(PortalProjConstants.KEY_NEW_PORTALPRJ_MAINPAGE_TITLE);
		addPage(fNewModuleWebPage);
	}
	
	@Override
	public boolean performFinish() {
		boolean finishFlag =  super.performFinish();
		if(finishFlag){
			return this.doPortalOperate();
		}
		return true;
	}
	
	/**
	 * portal本身的操作
	 * @return
	 */
	private boolean doPortalOperate(){
//		IProject project = super.getProject();
//		if(project == null)
//			return false;
//	
//		IJavaProject javaProject = JavaCore.create(project);
//		try {
//			List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
////			list.add(ProjCoreUtility.createSourceEntry(project, "src/portalspec"));
//			list.addAll(Arrays.asList(javaProject.getRawClasspath()));
//			javaProject.setRawClasspath(list.toArray(new IClasspathEntry[0]), null);
//		} catch (CoreException e) {
//			PortalProjPlugin.getDefault().logError(e.getMessage(),e);
//			return false;
//		}
//
//		try {
//			String moduleName = LfwCommonTool.getProjectModuleName(project);
//			String localFilePath = project.getLocation().toString() + "/src/portalspec/" + 
//				moduleName + "/portalspec";
//			writePortal(localFilePath,moduleName);
//			
//			project.refreshLocal(IProject.DEPTH_INFINITE, null);
//		} catch (Exception e) {
//			PortalProjPlugin.getDefault().logError(e.getMessage(),e);
//			return false;
//		}
		IWorkbenchWindow win = PortalProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		try {
			win.getWorkbench().showPerspective("nc.uap.portal.perspective.PortalPerspectiveFactory", win);			
			LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
			if(view != null){
				view.refreshTree();			
			}
		} catch (WorkbenchException e) {
			PortalProjPlugin.getDefault().logError(e.getMessage());
		}
//		IWorkbenchPage wbp = PortalProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		IViewPart view = wbp.findView("nc.uap.portal.perspective.PortalExplorerTreeView");
//		((PackageExplorerPart) view).openInActivePerspective();
//		((PackageExplorerPart) view).collapseAll();
//		TreeViewer tree = ((PackageExplorerPart) view).getTreeViewer();
//		String proName = project.getName();
//		JavaProject jProject = new JavaProject();
//		jProject.setProject(project);
//		Object[] expandObj = new Object[1];
//		expandObj[0] = jProject;
//		tree.setExpandedElements(expandObj);
//		((PackageExplorerPart) view).openInActivePerspective();
//		((PackageExplorerPart) view).collapseAll();
//		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, layout.getEditorArea());
////		folder.addView("org.eclipse.ui.navigator.ProjectExplorer");
//		//folder.addView(JavaUI.ID_TYPE_HIERARCHY);
//
//		folder.addView("nc.uap.portal.perspective.PortalExplorerTreeView");
//		folder.addView(JavaUI.ID_PACKAGES);
//		folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		return true;
	}
	
	protected void performBasicOperation() throws InvocationTargetException,InterruptedException {
		super.
		getContainer().run(false, true, new PortalProjectCreationOperation(fProjectProvider));
	}
	
// 	/**
// 	 * 生成portal文件
// 	 * @param filePath
// 	 * @param module
// 	 * @return
// 	 * @throws Exception 
// 	 * @throws UnsupportedEncodingException 
// 	 */
// 	private void writePortal(String filePath, String module) throws UnsupportedEncodingException, Exception
// 	{
// 		StringBuffer buffer = new StringBuffer();
// 		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
// 		buffer.append("\n<portal>");
// 		buffer.append("\n<module>"+module+"</module>");
// 		buffer.append("\n<depends>"+DEFAULT_DEPENDS+"</depends>");
// 		buffer.append("\n</portal>");
// 		//ByteArrayInputStream stream = new ByteArrayInputStream(buffer.toString().getBytes());
// 		File f = new File(filePath);
// 		if (!f.exists()){
// 			f.mkdirs();
// 		}
// 		FileUtilities.saveFile(filePath + "/portal.xml", buffer.toString().getBytes("UTF-8"));
// 	}
}
