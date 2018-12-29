package nc.uap.lfw.build.pub.util.pdm.dbrecord;
/**
 * 外部数据源的聚合VO类
 * 
 * 工作方式: 
 * 聚合了外部数据源配置文件(DataSource.xml)中定义的全部单位数据源, 
 * 由DBRecordConfigUtil对DataSource.xml转换得到。
 * 
 * @author fanp
 */
public class DBRecordDataSource
{
    public UnitDataSource[] unitDataSource;

	/**
	 * 构造函数
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
