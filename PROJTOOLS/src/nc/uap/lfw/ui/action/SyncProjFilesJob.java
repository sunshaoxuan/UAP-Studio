package nc.uap.lfw.ui.action;

import java.io.File;
import java.util.List;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.internal.project.SynCtlFilesAction;
import nc.uap.lfw.internal.util.PartMergeUtil;
import nc.uap.lfw.lang.M_action;

import org.eclipse.core.resources.IProject;

public class SyncProjFilesJob {
	public static final String SYNC_DIR = "sync";
	public static void deleteProjectFiles(IProject proj){
		deleteModuleFiles(proj);
	}
	public static boolean syncProject(IProject proj){
		String[] prePaths = null;
		if(LfwCommonTool.isBCPProject(proj)) {
			prePaths = LfwCommonTool.getBCPNames(proj);
		}
//		else
//			prePaths = new String[]{""}; //$NON-NLS-1$
		//同步文件
		if(prePaths == null || prePaths.length == 0){ //$NON-NLS-1$
			WEBProjPlugin.getDefault().logError(M_action.SyncWebFilesAction_2);
			return false;
		}
		else{
			boolean part = syncFiles(prePaths, proj);
			return part;
		}
	}
	
	/**
	 * 删除hotweb和modules下模块对应信息
	 * @param proj
	 */
	private static void deleteModuleFiles(IProject proj) {
		String ctx = LfwCommonTool.getLfwProjectCtx(proj);
		String webDir = LfwCommonTool.getHotwebs() + ctx + "/" + SYNC_DIR + "/"+ LfwCommonTool.getProjectModuleName(proj);
		FileUtilities.deleteFiles(webDir);
//		String moduleDir = LfwCommonTool.getUapHome() + "/modules/" + LfwCommonTool.getProjectModuleName(proj) + "/META-INF";
//		FileUtilities.deleteFiles(moduleDir);
	}
	
	/**
	 * 同步文件方法
	 * @param proj 
	 */
	private static boolean syncFiles(String[] paths, IProject proj) {
		boolean part = false;
		String projPath = proj.getLocation().toString();
		String moduleName = LfwCommonTool.getProjectModuleName(proj);
		for (int i = 0; i < paths.length; i++) {
			try {
				String path = paths[i];
				if(!path.equals("")) //$NON-NLS-1$
					path = "/" + path; //$NON-NLS-1$
				String ctx = getCtxPath(projPath, path);
				if(ctx == null){
					ctx = LfwCommonTool.getLfwProjectCtx(proj);
				}
				else{
					part = true;
				}
				//如果没有找到webContext，不同步。
				if (ctx == null || ctx.equals("")){ //$NON-NLS-1$
					WEBProjPlugin.getDefault().logError(M_action.SyncWebFilesAction_4+projPath);
					continue;
				}
//				else
//				String projName = LfwCommonTool.getProjectModuleName(proj);
				String toDir = LfwCommonTool.getHotwebs() + ctx;
				WEBProjPlugin.getDefault().logInfo(M_action.SyncWebFilesAction_5 + path + M_action.SyncWebFilesAction_6 + toDir + M_action.SyncWebFilesAction_7 + projPath + "/" + paths[i] + "/web"); //$NON-NLS-4$ //$NON-NLS-5$
				copyFileFromDir(toDir, projPath + "/" + path + "/web", proj, path); //$NON-NLS-1$ //$NON-NLS-2$
//				FileUtilities.copyFileFromDir(toDir, projPath + "/" + paths[i] + "/web");
				toDir = LfwCommonTool.getUapHome() + "/modules/" + moduleName+ "/META-INF"; //$NON-NLS-1$ //$NON-NLS-2$
				WEBProjPlugin.getDefault().logInfo(M_action.SyncWebFilesAction_8 + path + M_action.SyncWebFilesAction_9 + toDir + M_action.SyncWebFilesAction_10 + projPath + "/" + paths[i] + "/META-INF"); //$NON-NLS-4$ //$NON-NLS-5$
				FileUtilities.copyFileFromDir(toDir, projPath + "/" + paths[i] + "/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$
				
				toDir = LfwCommonTool.getUapHome() + "/modules/" + moduleName+"/lib"; //$NON-NLS-1$ //$NON-NLS-2$
				WEBProjPlugin.getDefault().logInfo(M_action.SyncWebFilesAction_8 + path + M_action.SyncWebFilesAction_9 + toDir + M_action.SyncWebFilesAction_10 + projPath + "/" + paths[i] + "/lib"); //$NON-NLS-4$ //$NON-NLS-5$
				FileUtilities.copyFileFromDir(toDir, projPath + "/" + paths[i] + "/lib"); 
				
				toDir = LfwCommonTool.getHotwebs() + ctx + "/frame";
				WEBProjPlugin.getDefault().logInfo(M_action.SyncWebFilesAction_8 + path + M_action.SyncWebFilesAction_9 + toDir + M_action.SyncWebFilesAction_10 + projPath + "/" + paths[i] + "/web/frame");
				copyFrameFileFromDir(toDir, projPath + "/" + path + "/web/frame", proj, path);
				//同步控制类模板
				
				String ctldir =ctx+ "/ctrl"; //$NON-NLS-1$
				String filepath =projPath + "/" + paths[i] + "/resources/"+ctldir; //$NON-NLS-1$ //$NON-NLS-2$
				if(ctldir!=null){
                  SynCtlFilesAction scfa = new SynCtlFilesAction();
                  scfa.SynCtlFiles(filepath, ctx);
				}
			} 
			catch (Exception e) {
				WEBProjPlugin.getDefault().logError(e);
			}
		}
		try {
			FileUtilities.copyFileFromDir(LfwCommonTool.getUapHome() + "/modules/" + moduleName+ "/META-INF", projPath + "/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} 
		catch (Exception e) {
			WEBProjPlugin.getDefault().logError(e);
		}
		return part;
	}
	
	private static String getCtxPath(String projPath, String path) {
		File dir = new File(projPath + path + "/web/WEB-INF/"); //$NON-NLS-1$
		if(dir.exists()){
			File[] fs = dir.listFiles();
			for (int j = 0; j < fs.length; j++) {
				if(fs[j].getName().endsWith(".part")){ //$NON-NLS-1$
					return PartMergeUtil.getMainProjNameByPart(fs[j].getName());
//					return fs[j].getName().substring(0,fs[j].getName().length()-5);
				}
			}
		}
		return null;
	}
	private static void copyFrameFileFromDir(String toPath, String fromPath,IProject proj,String bcp) throws Exception{
		File file = new File(fromPath);
		if(file.listFiles()==null)
			return;
		for(File subFile:file.listFiles()){
			if(subFile.isFile()){
				if(subFile.getPath().indexOf("\\frame\\device_")>-1){
					String fpath = LfwCommonTool.getHotwebs()+"lfw/frame"+subFile.getPath().substring(subFile.getPath().indexOf("\\frame\\")+6,subFile.getPath().lastIndexOf("\\"));
					FileUtilities.copyFileToDir(fpath, subFile,subFile.getName());
				}
				else{
					String fpath = toPath + subFile.getPath().substring(subFile.getPath().indexOf("\\frame\\")+6,subFile.getPath().lastIndexOf("\\"));
					FileUtilities.copyFileToDir(fpath, subFile,subFile.getName());
				}
			}
			else{
				copyFrameFileFromDir(toPath,subFile.getPath(),proj,bcp);
			}
		}
	}
	
	private static void copyFileFromDir(String toPath, String fromPath,IProject proj,String bcp) throws Exception{
		File file = new File(fromPath);
		if (file.isDirectory()) {
			copyFileToDir(toPath, FileUtilities.listFile(file), proj, bcp);
		}
	}
	
	private static void copyFileToDir(String toDir, String[] allFiles, IProject proj, String bcp) throws Exception {
		if (toDir == null || toDir.equals("")) {
			return;
		}
		File targetFile = new File(toDir);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		} 
		else if(!targetFile.isDirectory()){
			return;
		}
		for (String path : allFiles) {
			File file = new File(path);
			if (file.isDirectory()) {
				if (file.getName().equals("html")) {
					FileUtilities.copyFileFromDir(toDir  + "/" +  SYNC_DIR + "/" + LfwCommonTool.getProjectModuleName(proj) + "/" + bcp + "/" + file.getName(), file.getPath());
				}
				else if(!LfwCommonTool.getLfwProjectCtx(proj).equals("lfw")&&file.getName().equals("frame")){
					continue;
				}
				else{
					copyFileToDir(toDir + "/" + file.getName(), FileUtilities.listFile(file), proj, bcp);
				}					
			} else {
				FileUtilities.copyFileToDir(toDir, file, "");
			}
		}
	}

	public static void mergeWebXml(List<IProject> partList) {
		// 合并web.xml
		for (int i=0 ; i< partList.size() ; i++){
			IProject proj = partList.get(i); 
			String[] prePaths = null;
			if(LfwCommonTool.isBCPProject(proj)) {
				prePaths = LfwCommonTool.getBCPNames(proj);
			}
			if(prePaths != null && prePaths.length > 0){ //$NON-NLS-1$
				mergeWebXml(prePaths, proj);
			}
		}
	}
	
	/**
	 * 合并web.xml
	 * 
	 * @param prePaths
	 * @param proj
	 */
	private static void mergeWebXml(String[] paths, IProject proj) {
		String projPath = proj.getLocation().toString();
		for (int i = 0; i < paths.length; i++) {
			try {
				String path = paths[i];
				if(!path.equals("")) //$NON-NLS-1$
					path = "/" + path; //$NON-NLS-1$
				File dir = new File(projPath + path + "/web/WEB-INF/"); //$NON-NLS-1$
				if(dir.exists()){
					File[] fs = dir.listFiles();
					for (int j = 0; j < fs.length; j++) {
						if(fs[j].getName().endsWith(".part")){ //$NON-NLS-1$
							WEBProjPlugin.getDefault().logInfo(M_action.SyncWebFilesAction_3 + fs[j].getAbsolutePath());
							PartMergeUtil.mergeWebXml(proj, fs[j]);
							break;
						}
					}
				}
			} 
			catch (Exception e) {
				WEBProjPlugin.getDefault().logError(e);
			}
		}
	}
}
