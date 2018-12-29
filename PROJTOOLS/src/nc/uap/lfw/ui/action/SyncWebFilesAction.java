package nc.uap.lfw.ui.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_action;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 同步Web文件Action
 *
 */
public class SyncWebFilesAction implements IObjectActionDelegate {
	public static final String SYNC_DIR = "sync";
	private ISelection	fSelection;
	public void run(IAction action){
		if (fSelection instanceof IStructuredSelection){
			Job job = new Job(M_action.SyncWebFilesAction_0){
				public IStatus run(IProgressMonitor monitor){
					IProject[] projs = LfwCommonTool.getOpenedLfwProjects();
					monitor.beginTask(M_action.SyncWebFilesAction_1, projs.length);
					List<IProject> partList = new ArrayList<IProject>();
					//有多个工程为一个模块的情况，因此需要一次性先清除
					for (int i = 0; i < projs.length; i++) {
						IProject proj = projs[i];
						SyncProjFilesJob.deleteProjectFiles(proj);
					}
					for (int i = 0; i < projs.length; i++) {
						IProject proj = projs[i];
						boolean part = SyncProjFilesJob.syncProject(proj);
						if(part)
							partList.add(proj);
						monitor.worked(1);
					}
					
					SyncProjFilesJob.mergeWebXml(partList);
					
					monitor.done();
					return Status.OK_STATUS;
				}

				
			};
			job.schedule();
		}
	}


//	
//	public void toXml(String path, String compenentId){
//		try {
//			Document doc = XmlCommonTool.createDocument();
//			Element rootNode = doc.createElement("Component");
//			doc.appendChild(rootNode);
//			rootNode.setAttribute("id", compenentId);
//			rootNode.setAttribute("pack", "");
//			
//			File folder = new File(path);
//			File file = new File(folder,"component.cp");
//			if(!folder.exists()){
//				folder.mkdirs();
//			}
//	    	XmlCommonTool.documentToXml(doc, file);
//	    	
//		} catch (Exception e) {
//			CommonPlugin.getPlugin().logError(e.getMessage());
//		}
//		
//	}

	public void selectionChanged(IAction action, ISelection selection){
		this.fSelection = selection;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart){
		
	}
}
