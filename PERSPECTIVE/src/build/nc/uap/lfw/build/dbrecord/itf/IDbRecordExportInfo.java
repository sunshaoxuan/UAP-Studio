package nc.uap.lfw.build.dbrecord.itf;

/**
 * Ԥ�ýű�������Ϣ��
 * 
 * @author PH
 */
public interface IDbRecordExportInfo {
	
	/**
	 * ��ȡ����Sql�ļ������кš�
	 * 
	 * @return
	 */
	public String getSqlNo();
	
	/**
	 * ��ȡ�����ֶΣ�ֻ֧�ֵ����ֶΡ�
	 * 
	 * @return �����ֶ�
	 */
	public String getGrpField();
	
	/**
	 * ������
	 * 
	 * @return
	 */
	String getTableName();
	
	/**
	 * ��������
	 * 
	 * @return
	 */
	public String getTableDesc();

}
