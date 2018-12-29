package nc.uap.lfw.build.dbscript.itf;

import java.io.File;

/**
 * 描述脚本文件的接口，含具体文件和脚本类型信息
 * @author syang
 *
 */


public interface ISqlFile {
	public File getFile();
	public ScriptType getScriptType();

}
