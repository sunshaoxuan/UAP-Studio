package nc.uap.lfw.build.dbcreate.impl;

import java.io.File;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.dbscript.itf.ScriptType;

public class DbCreateSqlFile implements ISqlFile {
	
	private File file;

	public DbCreateSqlFile(File file){
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}

	public ScriptType getScriptType() {
		return ScriptType.CREATE;
	}

	public void setFile(File file){
		this.file = file;
	}
}
