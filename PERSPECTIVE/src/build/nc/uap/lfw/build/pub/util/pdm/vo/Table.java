package nc.uap.lfw.build.pub.util.pdm.vo;


/**
 * ��ṹԪ������ϢVO��
 * 
 * @author fanp
 */
public class Table
{
    /*����*/
    private String m_tableName;
    /*���ֶ�*/
    private TableField[] m_tableFields;
    /*����*/
    private String[] m_primaryKey;

    public Table()
    {
    }

    public Table(String tableName)
    {
        setTableName(tableName);
    }

    /**
    * ���ر��ֶ�
    */
    public TableField[] getTableFields()
    {
        return m_tableFields;
    }

    /*
    * ���ر���
    */
    public String getTableName()
    {
        return m_tableName;
    }

    /*
    * ���ر������ֶ�����
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
