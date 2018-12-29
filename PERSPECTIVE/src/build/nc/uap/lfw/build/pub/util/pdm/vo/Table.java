package nc.uap.lfw.build.pub.util.pdm.vo;


/**
 * 表结构元数据信息VO类
 * 
 * @author fanp
 */
public class Table
{
    /*表名*/
    private String m_tableName;
    /*表字段*/
    private TableField[] m_tableFields;
    /*主键*/
    private String[] m_primaryKey;

    public Table()
    {
    }

    public Table(String tableName)
    {
        setTableName(tableName);
    }

    /**
    * 返回表字段
    */
    public TableField[] getTableFields()
    {
        return m_tableFields;
    }

    /*
    * 返回表名
    */
    public String getTableName()
    {
        return m_tableName;
    }

    /*
    * 返回表主键字段名称
    */
    public String[] getTablePrimaryKey()
    {
        return m_primaryKey;
    }

    public void setTableFields(TableField[] newTableFields)
    {
        m_tableFields = newTableFields;
    }

    public void setTableName(String newTableName)
    {
        m_tableName = newTableName;
    }

    public void setTablePrimaryKey(String[] newPrimaryKey)
    {
        m_primaryKey = newPrimaryKey;
    }
}
