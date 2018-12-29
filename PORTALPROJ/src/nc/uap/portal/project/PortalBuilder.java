package nc.uap.portal.project;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.SpaceMode;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.portal.core.Messages;
import nc.uap.portal.core.PortalProjConnector;
import nc.uap.portal.wizards.PortalProjPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 *LFW项目Builder 
 *
 *dingrf
 */
public class PortalBuilder extends IncrementalProjectBuilder
{
	/** NC_HOME 目录*/
	private String NC_HOME = ProjCoreUtility.getNcHomeFolderPath().toString();

	/** hotwebs 目录*/
	private String PORTALHOME = NC_HOME + "/portalhome"; //$NON-NLS-1$
	
	public PortalBuilder(){
		PortalProjPlugin.getDefault().logInfo(Messages.PortalBuilder_1);
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException
	{
		PortalProjPlugin.getDefault().logInfo(Messages.PortalBuilder_2);
		IProject project = getProject();
		if (!ProjCoreUtility.isModuleProject(project))
			return null;
		IResourceDelta delta = null;
		if (kind != FULL_BUILD)
			delta = getDelta(project);
		if (delta == null || kind == FULL_BUILD){}
		else{
			delta.accept(new DeltaVisitor(monitor));
		}
		return null;
	}

	class DeltaVisitor implements IResourceDeltaVisitor{
		
		private static final String SRC_PORTALSPEC = "src/portalspec/"; //$NON-NLS-1$
		private IProgressMonitor monitor;

		public DeltaVisitor(IProgressMonitor monitor)
		{
			PortalProjPlugin.getDefault().logInfo(Messages.PortalBuilder_0);
			this.monitor = monitor;
		}

		public boolean visit(IResourceDelta delta)
		{
			IResource resource = delta.getResource();
			if (resource instanceof IFile){
				IFile candidate = (IFile) resource;
				String filePath =  candidate.getProjectRelativePath().toString();
				if(!filePath.contains(SRC_PORTALSPEC))
					return true;
				boolean isBcp = LfwCommonTool.isBCPProject(getProject());
				String moduleName = getProjectModuleName(getProject(), filePath, isBcp);
				if(moduleName == null)
					return true;
				if(!filePath.contains("/portalspec/web")){ //$NON-NLS-1$
					String starts = SRC_PORTALSPEC;
					if (isBcp)
						starts = moduleName + "/" + SRC_PORTALSPEC; //$NON-NLS-1$
					//是否为已部署过的portalspec下的文件

					try{
						String targetPath = PORTALHOME + "/" + moduleName + "/portalspec"  //$NON-NLS-1$ //$NON-NLS-2$
							+ filePath.substring(0,filePath.lastIndexOf("/")).replace(starts + moduleName + "/portalspec", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (delta.getKind() != IResourceDelta.REMOVED){
							compilePortalFile(targetPath, candidate, monitor);
						}
						else{
							removePortalFile(targetPath, candidate, monitor);
						}
						
						//重新部署发生改变的文件
							deployPortalSpec(candidate, moduleName, starts);
					}
					catch(Exception e){
						PortalProjPlugin.getDefault().logError(e.getMessage());
					}
				}
				else{
					String fromPath = null;
					if(isBcp)
						fromPath = getProject().getLocation().toString() + "/" + moduleName + "/src/portalspec/" + moduleName + "/portalspec/web"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					else
						fromPath = getProject().getLocation().toString() + "/src/portalspec/" + moduleName + "/portalspec/web"; //$NON-NLS-1$ //$NON-NLS-2$
					File fromDir = new File(fromPath);
					if (fromDir.exists()){
//						String nchome = LfwCommonTool.getNCHome();
						String hotwebs = LfwCommonTool.getHotwebs(); //$NON-NLS-1$
						//将所有portal项目的web下目录拷贝到hotwebs/portal/apps下
//						String toPath = hotwebs + "/portal/apps/" + moduleName; 
						String toPath = hotwebs+"/portal/apps/"+moduleName;
						File toDir = new File(toPath);
						if (!toDir.exists())
							toDir.mkdirs();
						try {
							FileUtilities.copyFileFromDir(toPath, fromPath);
						} catch (Exception e) {
							PortalProjPlugin.getDefault().logError(e);
						}
					}
				}
			}
			return true;
		}


		private String getProjectModuleName(IProject project, String filePath, boolean isBcp) {
			String moduleName = LfwCommonTool.getProjectModuleName(project);
			if (filePath.startsWith(SRC_PORTALSPEC +moduleName + "/portalspec/")){ //$NON-NLS-1$
				return moduleName; 
			}
			else if (isBcp){
				String[] bcpNames = LfwCommonTool.getBCPNames(project);
				if(bcpNames != null){
					for (int i = 0; i < bcpNames.length; i++) {
						if (filePath.startsWith(bcpNames[i] + "/src/portalspec/" + bcpNames[i] + "/portalspec/")){ //$NON-NLS-1$ //$NON-NLS-2$
							return bcpNames[i];
						}
					}
				}
			}
			return null; 
		}
	}

//	/**
//	 * 判断是否为portalspec下文件发生变化
//	 * 
//	 * @param file  发生改变的文件或文件夹
//	 */
//	private boolean isDeployedPortalFile(String moduleName, String filePath, String starts){
//		//portalspec中web目录不做此处理
//		if (filePath.startsWith(starts + moduleName + "/portalspec/") &&  //$NON-NLS-1$
//				!filePath.startsWith(starts + moduleName + "/portalspec/web")){ //$NON-NLS-1$
//			File dir = new File(PORTALHOME + "/" + moduleName + "/portalspec"); //$NON-NLS-1$ //$NON-NLS-2$
//			if (dir.exists())
//				return true;
//			else
//				return false;
//		}
//		return false;
//	}

 	/**
 	 * 拷贝portalspec目录下文件
 	 */
	private void compilePortalFile(String targetPath, IFile file, IProgressMonitor monitor){
		try{
			String filePath = targetPath; 
			File f = new File(filePath);
			if (!f.exists()){
				f.mkdirs();
			}
			PortalProjPlugin.getDefault().logInfo(Messages.PortalBuilder_3 + filePath);
			FileUtilities.copyFile(file.getLocation().toString(), filePath + "/" + file.getName()); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			PortalProjPlugin.getDefault().logError(e);
		}
	}
	
	/**
	 * 删除portalspec目录下文件
	 */
	private void removePortalFile(String targetPath, IFile file, IProgressMonitor monitor)
	{
		File f = new File(targetPath + "/" + file.getName()); //$NON-NLS-1$
		f.delete();
	}
	
	/**
	 * 部署PortalSpec
	 * 
	 * @param filePath
	 * @param moduleName
	 */
	private void deployPortalSpec(IFile candidate, String moduleName, String starts){
		String filePath = candidate.getProjectRelativePath().toString();
		if (filePath.equals(starts + moduleName+"/portalspec/portal.xml")){ //$NON-NLS-1$
			PortalProjConnector.deployPortal(moduleName);
		}
		else if (filePath.equals(starts + moduleName+"/portalspec/portlet.xml")){ //$NON-NLS-1$
			PortalProjConnector.deployPortletApp(moduleName);
		}
		else if (filePath.equals(starts + moduleName+"/portalspec/display.xml")){ //$NON-NLS-1$
			PortalProjConnector.deployDisplay(moduleName);
		}
		else if (filePath.equals(starts + moduleName+"/portalspec/plugin.xml")){ //$NON-NLS-1$
			PortalProjConnector.deployPtPlugin(moduleName);
		}
		else if (filePath.endsWith(".pml")){ //$NON-NLS-1$
			PortalProjConnector.deployPage(moduleName, candidate.getName().split("\\.")[0]); //$NON-NLS-1$
			PortalProjPlugin.getDefault().logInfo(candidate.getName().split("\\.")[0]+Messages.PortalBuilder_4); //$NON-NLS-1$
		}
		else if (filePath.startsWith(starts + moduleName+"/portalspec/manager/")){ //$NON-NLS-1$
			PortalProjConnector.deployManagerApps(moduleName);
		}
		else if (filePath.startsWith(starts + moduleName+"/portalspec/ftl/portaldefine/skin")){ //$NON-NLS-1$
			PortalProjConnector.deploySkin(moduleName);
		}
	}
}