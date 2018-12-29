package nc.uap.lfw.build.dbcreate.impl;

import java.io.File;

public interface IDataDictionaryGenerator {
	
	public void generate(File pdmFile, boolean geneReference, File sqlRoot);

}
