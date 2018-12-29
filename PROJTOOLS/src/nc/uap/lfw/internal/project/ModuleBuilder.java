/*
 * Created on 2005-8-16
 * @author 何冠宇
 */
package nc.uap.lfw.internal.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.internal.util.FullCopyVisitor;
import nc.uap.lfw.internal.util.PartMergeUtil;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_internal;
import nc.uap.lfw.ui.action.SyncProjFilesJob;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 *LFW项目Builder 
 *
 */
public class ModuleBuilder extends IncrementalProjectBuilder{
	
	public ModuleBuilder(){
		WEBProjPlugin.getDefault().logInfo(M_internal.ModuleBuilder_0);
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException{
		IProject project = getProject();
		if (!ProjCoreUtility.isModuleProject(project))
			return null;
		IResourceDelta delta = null;
		if (kind != FULL_BUILD)
			delta = getDelta(project);
		if (delta == null || kind == FULL_BUILD){
			boolean bcpProj = LfwCommonTool.isBCPProject(project);
			IFolder rootFolder = project.getFolder("META-INF"); //$NON-NLS-1$
		    if (bcpProj || rootFolder.exists()){
		    	WEBProject webProj = ProjCoreUtility.createLfwProject(project);
		        IPath target = LfwCommonTool.getModulesFoder();
		        if(rootFolder.exists())
		        	copyFolderTo(rootFolder, target);
		        if(bcpProj){
		        	String[] bcpNames = LfwCommonTool.getBCPNames(project);
		        	if(bcpNames != null && bcpNames.length > 0){
		        		for(int i = 0; i < bcpNames.length ; i ++){
		        			IPath bcpPath = target.append("META-INF"); //$NON-NLS-1$
		        			IFolder folder = project.getFolder(bcpNames[i] + "/META-INF"); //$NON-NLS-1$
		        			copyFolderTo(folder, bcpPath);
		        		}
		        	}
		        }
		    }
		}
		else{
			delta.accept(new DeltaVisitor(this, monitor));
		}
		return null;
	}

	class DeltaVisitor implements IResourceDeltaVisitor{
		
		private IProgressMonitor monitor;
		private ModuleBuilder moduleBuilder;

		public DeltaVisitor(ModuleBuilder moduleBuilder, IProgressMonitor monitor){
			WEBProjPlugin.getDefault().logInfo(M_internal.ModuleBuilder_1);
			this.moduleBuilder = moduleBuilder;
			this.monitor = monitor;
		}

		public boolean visit(IResourceDelta delta){
			IResource resource = delta.getResource();
			if (resource instanceof IFile){
				processModuleNature(delta, resource);
				IFile candidate = (IFile) resource;
				//web目录下文件改变
				if (isWebFolder(candidate)){
					/*目标路径*/
					String targetPath = ""; //$NON-NLS-1$
					/* WEB-INF 相对路径*/
					String webinfPath = "/web/WEB-INF"; //$NON-NLS-1$
					/* 文件在web中的相对 路径 */
					String fileFullPath = candidate.getFullPath().toString();
					String filePath =  fileFullPath.substring(fileFullPath.indexOf("/web/")+ "/web".length() , fileFullPath.lastIndexOf("/")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					
					/*是组件工程*/
					if (!"web".equals(candidate.getProjectRelativePath().segment(0))){ //$NON-NLS-1$
						webinfPath = candidate.getProjectRelativePath().segment(0) + webinfPath;
					}
					
					String nchome = LfwCommonTool.getUapHome();
					String hotwebs = nchome + "/hotwebs"; //$NON-NLS-1$
					
					String path = candidate.getLocationURI().getPath();
					String ctx = LfwCommonTool.getLfwProjectCtx(getProject());
					if(path.indexOf("/html/")<0){
						// 在web-inf中查找*.part 取到主项目名
						String mainProjName = getMainProjName(webinfPath);
						if (mainProjName == null){
							// 在.module_prj 中找到 module.webContext
							if (ctx == null || ctx.equals("")){ //$NON-NLS-1$
								WEBProjPlugin.getDefault().logError(M_internal.ModuleBuilder_2+getProject().getLocation().toString());
								return true;
							}
							targetPath =  hotwebs + "/" + ctx + filePath;
						}
						else
							targetPath = hotwebs + "/" + mainProjName + filePath; 
					}
					else{
						targetPath = hotwebs + "/" + ctx + "/"+SyncProjFilesJob.SYNC_DIR + "/" + LfwCommonTool.getProjectModuleName(candidate.getProject())+"/"+candidate.getProjectRelativePath().segment(0)+filePath;
					}
					WEBProjPlugin.getDefault().logInfo(M_internal.ModuleBuilder_3 + candidate.getName() + ",type:" + delta.getKind()); //$NON-NLS-2$
					
					/*删除webtemp*/
//					if (filePath.startsWith("/html/nodes/")){
//						String[] folders = filePath.replace("/html/nodes/", "").split("/");
//						String page = "";
//						for (int i = 0 ; i< folders.length ; i ++){
//							page += "/" + folders[i]; 
//							String tempPath = targetPath.replace(filePath, "/webtemp/html/nodes"+page);
//							deleteTempFile(tempPath);
//						}
//					}
					//修改，新增文件
					if (delta.getKind() != IResourceDelta.REMOVED){
						compileWebFile(targetPath, candidate, monitor);
						if (candidate.getName().endsWith(".part")){ //$NON-NLS-1$
							/* 修改part文件时，进行web.xml合并 */
							File fromFile = new File(candidate.getLocation().toString());
							PartMergeUtil.mergeWebXml(getProject(), fromFile);
						}
						else if (candidate.getName().equals("web.xml")){ //$NON-NLS-1$
							PartMergeUtil.mergeParts(getProject());
						}
						return false;
					}
					//删除文件
					else{
						removeWebFile(targetPath, candidate, monitor);
						// 删除part文件时，在web.xml中剔除相关配置
						if (candidate.getName().endsWith(".part")){ //$NON-NLS-1$
							PartMergeUtil.deletePart(getProject(), candidate.getName().replace(".part", "")); //$NON-NLS-1$ //$NON-NLS-2$
						}
						return false;
					}
				}
				if(isCtlFiles(candidate)){
					
				     
					try {
						int i =candidate.getLocation().toString().lastIndexOf('/');
						SynCtlFilesAction scfa = new SynCtlFilesAction();
						String ctx = LfwCommonTool.getLfwProjectCtx(getProject());
						scfa.SynCtlFiles(candidate.getLocation().toString().substring(0,i),ctx);
					} catch (IOException e) {
						WEBProjPlugin.getDefault().logError(e);
					} catch (Exception e) {
						WEBProjPlugin.getDefault().logError(e);
					}
				}
			}
			return true;
		}

		private void processModuleNature(IResourceDelta delta, IResource resource) {
			 IFile candidate = (IFile)resource;
		     IPath projectRelativePath = candidate.getProjectRelativePath();
		     if (("META-INF".equals(projectRelativePath.segment(0))) || ("META-INF".equals(projectRelativePath.segment(1)))){ //$NON-NLS-1$ //$NON-NLS-2$
		    	if (delta.getKind() != 2){
		    		IFile fromFile = (IFile) resource;
		    		WEBProject webProj = ProjCoreUtility.createLfwProject(this.moduleBuilder.getProject());
		    		String moduleName = LfwCommonTool.getProjectModuleName(this.moduleBuilder.getProject());
			        IPath target = LfwCommonTool.getModulesFoder();
			        IPath toPath = target.append(moduleName).append("META-INF").append(projectRelativePath.lastSegment()); //$NON-NLS-1$
		           
		            File toDir = toPath.removeLastSegments(1).toFile();
		            File toFile = toPath.toFile();

		            if(!toDir.exists())
		            	toDir.mkdirs();
		            InputStream in = null;
		            OutputStream out = null;
		            try {
		                in = fromFile.getContents();
		                out = new FileOutputStream(toFile);
		                IOUtils.copy(in, out);
		            } 
		            catch (Exception e) {
						WEBProjPlugin.getDefault().logError(e);
					} 
		            finally {
		                IOUtils.closeQuietly(in);
		                IOUtils.closeQuietly(out);
		            }
		    	}
		     }
		}
	}

	/**
	 * 判断是否为web目录下文件改变
	 * 
	 * @param file  发生改变的文件
	 */
	private boolean isWebFolder(IFile file){
		if ("web".equals(file.getProjectRelativePath().segment(0))){ //$NON-NLS-1$
			return true;
		} 
		
		else{
			try {
				if ( (getProject().hasNature(WEBProjConstants.MODULE_NATURE)) && ("web".equals(file.getProjectRelativePath().segment(1)))){ //$NON-NLS-1$
					String[] names = LfwCommonTool.getBCPNames(getProject());
					for (int i = 0 ; i < names.length ; i++){
						if (names[i].equals(file.getProjectRelativePath().segment(0)))
							return true;
					}
				}
			} 
			catch (Exception e) {
				WEBProjPlugin.getDefault().logError(e);
			}
		}
		return false;
	}
	public boolean isCtlFiles(IFile file) {
		String path =file.getLocation().toString();
		String ctx = LfwCommonTool.getLfwProjectCtx(getProject());
		String pctx = ctx+"/ctrl"; //$NON-NLS-1$
		int i = path.lastIndexOf('/');
		String pathi=path.substring(0,i);
		int j = pathi.lastIndexOf('/');
		String pathj =pathi.substring(0, j);
		int k = pathj.lastIndexOf('/');
		String pathk = path.substring(k+1,i);
		String a =null;
		if(pctx.equals(pathk))return true;
		return false;
	}
	/**
	 * 取WEB-INF下的*.part名称
	 */
 	private String getMainProjName(String webinfPath){
		IFolder webinfFolder = getProject().getFolder(webinfPath);
		File webinfFile = new File(webinfFolder.getLocation().toString());
		if(!webinfFile.exists()){
			WEBProjPlugin.getDefault().logError(webinfFolder.getLocation().toString() + M_internal.ModuleBuilder_4);
			return null;
		}
		File[] files = webinfFile.listFiles();
		for (int i=0 ; i < files.length ; i++){
			if (files[i].isFile() && files[i].getName().endsWith(".part")) //$NON-NLS-1$
				return PartMergeUtil.getMainProjNameByPart(files[i].getName());
			
//					String fileName = files[i].getName().substring(0,files[i].getName().length() - 5);
//					int pos = fileName.lastIndexOf(".");
//					if (pos == -1){
//						WEBProjPlugin.getDefault().logError("错误：" + files[i].getAbsolutePath() + " part文件名称格式应为 :模块id.主项目id.part");
//						return null;
//					}
//					else{
//						fileName =  fileName.substring(pos, fileName.length());
//						return "/" + fileName;
////						return "/" + files[i].getName().substring(0,files[i].getName().length() - 5);
//					}
		}
		return null;
 	}
	
 	/**
 	 * 拷贝web目录下文件
 	 */
	private void compileWebFile(String targetPath, IFile file, IProgressMonitor monitor){
		if(file.getName().equals("look-and-feel.xml")){
			return;
		}
		try{
			String filePath = targetPath; 
			if(filePath.indexOf("/frame/device_")>-1){
				String ipath = file.getFullPath().toString();
				String fpath = LfwCommonTool.getHotwebs()+"lfw/frame"+ipath.substring(ipath.indexOf("/frame/")+6);
				WEBProjPlugin.getDefault().logInfo(M_internal.ModuleBuilder_5 + fpath);
				FileUtilities.copyFile(file.getLocation().toString(),fpath);
				return;
			}
			File f = new File(filePath);
			if (!f.exists()){
				f.mkdirs();
			}
			WEBProjPlugin.getDefault().logInfo(M_internal.ModuleBuilder_5 + filePath);
			FileUtilities.copyFile(file.getLocation().toString(), filePath + "/" + file.getName()); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			WEBProjPlugin.getDefault().logError(e);
		}
	}
	
	/**
	 * 删除web目录下文件
	 */
	private void removeWebFile(String targetPath, IFile file, IProgressMonitor monitor){
		File f = new File(targetPath + "/" + file.getName()); //$NON-NLS-1$
		f.delete();
		File folder = f.getParentFile();
		while(folder.exists()&&folder.listFiles().length==0){
			folder.delete();
			folder = folder.getParentFile();
		}			
	}
	
	private void copyFolderTo(IFolder from, IPath to){
	    try{
	      IResourceVisitor visitor = new FullCopyVisitor(to);
	      from.accept(visitor);
	    }
	    catch (CoreException e){
	    	WEBProjPlugin.getDefault().logError(e);
	    }
	}
}