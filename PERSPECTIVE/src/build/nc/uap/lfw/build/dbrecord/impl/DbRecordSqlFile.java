package nc.uap.lfw.build.dbrecord.impl;

import java.io.File;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.dbscript.itf.ScriptType;

public class DbRecordSqlFile implements ISqlFile {
	private File file;
	
	public DbRecordSqlFile(File file){
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public ScriptType getScriptType() {
		return ScriptType.RECORD;
	}

}
