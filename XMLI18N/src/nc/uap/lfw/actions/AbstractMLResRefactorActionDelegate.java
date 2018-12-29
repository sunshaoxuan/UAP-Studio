package nc.uap.lfw.actions;

import nc.uap.lfw.tool.ProjConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;

/**
 * 多语资源Action基类
 * 
 * @author dingrf
 *
 */
public abstract class AbstractMLResRefactorActionDelegate implements IWorkbenchWindowActionDelegate{

	/**eclipse当前的IWorkbenchWindow*/
	private IWorkbenchWindow window = null;
	
	/**选中的多语目标文件*/
	protected ISelection selection = null;

	@Override
	public void dispose(){
	}

	@Override
	public void init(IWorkbenchWindow window){
		this.window = window;
	}

	protected Shell getShell(){
		return window.getShell();
	}

	/**
	 * 得到选中的xml文件
	 * 
	 * @param selection
	 * @return
	 */
	protected IFile getXmlFile(ISelection selection){
		if (selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection)selection;
			if (ss.isEmpty() || ss.size() != 1)
				return null;
			Object first = ss.getFirstElement();
			if (first instanceof IFile){
				if(((IFile)first).getName().endsWith(".xml")){
					return (IFile)first;
				}
			}
		} 
		return null;
	}

	//暂不支持textSelection
//	protected Region getSelectionRegion(){
//		if (selection != null && (selection instanceof TextSelection)){
//			TextSelection textSel = (TextSelection)selection;
//			return new Region(textSel.getOffset(), textSel.getLength());
//		} 
//		else{
//			return null;
//		}
//	}

	/**
	 * 选择LFW项目时，菜单可用
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		this.selection = selection;
		action.setEnabled(true);
//		action.setEnabled(getXmlFile(selection) != null);
		//选中lfw项目时，可用
		action.setEnabled(getLfwProject(selection) != null);
	}

	/**
	 * 是否为lfw项目
	 * @return
	 */
	protected IProject getLfwProject(ISelection selection) {
		//TODO 现在只有选文件，文件夹时才好用
		if (selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection)selection;
			if (ss.isEmpty() || ss.size() != 1)
				return null;
			Object first = ss.getFirstElement();
//			if (first instanceof PackageFragmentRoot){
//				((PackageFragmentRoot)first).
//			}
			IProject project = null;
			if (first instanceof JavaProject){
				project = ((JavaProject)first).getProject();
			}
			else if (first instanceof IResource){
				project = ((IResource)first).getProject();
			}
			try {
				if (project != null && project.hasNature(ProjConstants.MODULE_NATURE))
					return project;
			} catch (CoreException e) {
				return null;
			}
		}
		return null; 
	}
	protected IFolder getFolder(ISelection selection) {
		if (selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection)selection;
			if (ss.isEmpty() || ss.size() != 1)
				return null;
			Object first = ss.getFirstElement();
//			if (first instanceof PackageFragmentRoot){
//				((PackageFragmentRoot)first).
//			}
			IFolder folder = null;
		    if (first instanceof IFile){
				folder = (IFolder) ((IResource)first).getParent();
			}
		    if (first instanceof IFolder){
				folder = (IFolder) first;
			}
			try {
				if (folder != null){
					return folder;
				}
					
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
