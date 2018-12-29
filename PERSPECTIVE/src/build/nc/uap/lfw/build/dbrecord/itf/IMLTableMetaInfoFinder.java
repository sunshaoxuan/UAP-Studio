package nc.uap.lfw.build.dbrecord.itf;

import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;

/**
 * 含多语信息表的元数据查询接口
 * @author syang
 *
 */

public interface IMLTableMetaInfoFinder {
	
	public MLTableMetaInfo[] getMLTableMetaInfo() throws SdpBuildRuntimeException;

}
