/**
 * 
 */
package nc.uap.lfw.internal.project;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.lang.M_internal;

/**
 * @author guomq1
 *
 */


public class SynCtlFilesAction {

	public void SynCtlFiles(String path,String ctx) throws Exception{
		//同步控制类模板
		String ctldir =ctx+ "/ctrl"; //$NON-NLS-1$
		if(ctldir!=null){
		    String toDir_ctrl = LfwCommonTool.getUapHome()+"/resources/"+ctldir; //$NON-NLS-1$
			WEBProjPlugin.getDefault().logInfo(M_internal.SynCtlFilesAction_0 + path + M_internal.SynCtlFilesAction_1 + toDir_ctrl + M_internal.SynCtlFilesAction_2+ "/" + path); //$NON-NLS-4$
			FileUtilities.copyFileFromDir(toDir_ctrl, path);
		    }
	}
}
