package nc.uap.lfw.build.dbrecord.itf;

import java.util.List;

import nc.uap.lfw.build.dao.IQueryInfo;

/**
 * Ԥ�ýű�����������Ϣ�ӿڡ�
 * 
 * @author PH
 */
public interface IDbRecordScript extends IQueryInfo, IDbRecordExportInfo{
	
	/**
	 * ��ȡ�Ӽ�¼����Ϣ��
	 * 
	 * @return �Ӽ�¼����Ϣ
	 */
	public List<? extends IDbRecordScript> getChilds();

}
