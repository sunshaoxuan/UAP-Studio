package nc.uap.lfw.build.dbrecord.itf;

import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;

/**
 * ��������Ϣ���Ԫ���ݲ�ѯ�ӿ�
 * @author syang
 *
 */

public interface IMLTableMetaInfoFinder {
	
	public MLTableMetaInfo[] getMLTableMetaInfo() throws SdpBuildRuntimeException;

}
