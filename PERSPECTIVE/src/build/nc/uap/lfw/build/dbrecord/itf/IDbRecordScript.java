package nc.uap.lfw.build.dbrecord.itf;

import java.util.List;

import nc.uap.lfw.build.dao.IQueryInfo;

/**
 * 预置脚本导出条件信息接口。
 * 
 * @author PH
 */
public interface IDbRecordScript extends IQueryInfo, IDbRecordExportInfo{
	
	/**
	 * 获取子记录项信息。
	 * 
	 * @return 子记录项信息
	 */
	public List<? extends IDbRecordScript> getChilds();

}
