package nc.uap.lfw.build.transport;

import java.io.File;
import java.io.IOException;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.dbscript.itf.ScriptType;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.itf.ISDP;

import org.apache.commons.io.FileUtils;

/**
 * 将源文件或目录复制到目标目录。
 * 
 * @author PH
 */
public class Transporter implements ITransporter {
	
	private File destFile;
	
	private ScriptType scriptType;
	
	
	public Transporter(File destFile) {
		this.destFile = destFile;
	}

	public Transporter(File destFile, ScriptType scriptType) {
		this.destFile = destFile;
		this.scriptType = scriptType;
	}


	public void handle(ISqlFile[] arySrcFile) {
		if(arySrcFile != null && arySrcFile.length>0){
			for(int i=0; i<arySrcFile.length; i++){
				File srcFile = arySrcFile[i].getFile();
				if( srcFile != null && srcFile.exists() && destFile != null && destFile.exists() && destFile.isDirectory()){
					File realDestFile = destFile;
					if(scriptType == ScriptType.RECORD){
						realDestFile = new File(destFile, getRelativeItemScriptPath(srcFile));
					}
					try {
						if(srcFile.isFile()){
							FileUtils.copyFileToDirectory(srcFile, realDestFile);
						} else{
							FileUtils.copyDirectoryToDirectory(srcFile, realDestFile);
						}
					} catch (IOException e) {
						MainPlugin.getDefault().logError("Failed to copy file " + srcFile.getAbsolutePath() + " to " + realDestFile.getAbsolutePath(), e);
						throw new SdpBuildRuntimeException("脚本复制到" + realDestFile.getAbsolutePath() + "失败。");
					}
				}				
			}
		}
	}
	
	public String getOperateDesc() {
		return "脚本复制";
	}

	private String getRelativeItemScriptPath(File scriptRoot) {
		String path = scriptRoot.getParentFile().getAbsolutePath();
		int initPos = path.lastIndexOf(File.separator + ISDP._SCRIPT_INIT_
				+ File.separator);
		if (initPos != -1) {
			return path.substring(initPos + ISDP._SCRIPT_INIT_.length() + 1);
		} else {
			return path;
		}
	}

}
