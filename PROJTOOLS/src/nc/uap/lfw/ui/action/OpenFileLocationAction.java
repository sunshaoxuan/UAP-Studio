package nc.uap.lfw.ui.action;

import java.io.IOException;

import nc.uap.lfw.core.WEBProjPlugin;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class OpenFileLocationAction  implements IObjectActionDelegate{

	private ISelection fSelection;
	
	@Override
	public void run(IAction action) {
		if (fSelection instanceof IStructuredSelection) {
			String folderPath = null;
			Object element = ((IStructuredSelection) fSelection).getFirstElement();
			if(element instanceof IFile){
				IFile file = (IFile)element;
				String path = file.getLocation().toOSString();
				folderPath = path.substring(0,path.lastIndexOf("\\"));				
			}
			else if(element instanceof IFolder){
				IFolder folder = (IFolder)element;
				folderPath = folder.getLocation().toOSString();				
			}
			else if(element instanceof IProject){
				IProject project = (IProject)element;
				folderPath = project.getLocation().toOSString();				
			}
			try {
				if(folderPath!=null)
					Runtime.getRuntime().exec("explorer.exe "+ folderPath);
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.fSelection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
