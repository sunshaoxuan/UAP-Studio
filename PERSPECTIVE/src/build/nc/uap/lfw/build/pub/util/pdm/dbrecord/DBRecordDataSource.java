package nc.uap.lfw.build.pub.util.pdm.dbrecord;
/**
 * �ⲿ����Դ�ľۺ�VO��
 * 
 * ������ʽ: 
 * �ۺ����ⲿ����Դ�����ļ�(DataSource.xml)�ж����ȫ����λ����Դ, 
 * ��DBRecordConfigUtil��DataSource.xmlת���õ���
 * 
 * @author fanp
 */
public class DBRecordDataSource
{
    public UnitDataSource[] unitDataSource;

	/**
	 * ���캯��
	 * 
	 * @return
	 */
    public DBRecordDataSource()
    {
    }

    public void setUnitDataSource(UnitDataSource[] unitDataSource) 
    {
        this.unitDataSource = unitDataSource;
    }

    public UnitDataSource[] getUnitDataSource() 
    {
        return unitDataSource;
    }
}
