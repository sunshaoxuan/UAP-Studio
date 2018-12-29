package nc.uap.lfw.build.dbcreate.impl;

import java.io.Writer;
import java.util.List;

import nc.uap.lfw.build.dbcreate.pdm.Pdm.ViewInfo;
import nc.uap.lfw.build.orm.mapping.IFkConstraint;
import nc.uap.lfw.build.orm.mapping.IIndex;
import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * DDL生成器接口。
 * 
 * @author PH
 */
public interface IDdlGenerator {
	
	public void geneCreateTableDdl(List<ITable> tables, Writer writer);
	
	public void geneCreateIndexDdl(List<IIndex> indexs, Writer writer);
	
	public void geneAddConstraintDdl(List<IFkConstraint> constraints, Writer writer);
	
	public void geneCreateViewDdl(List<ViewInfo> views, Writer writer);

}
