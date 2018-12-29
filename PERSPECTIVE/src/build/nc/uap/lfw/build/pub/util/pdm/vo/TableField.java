package nc.uap.lfw.build.pub.util.pdm.vo;

/**
 * ���ֶ�Ԫ������ϢVO��
 * 
 * @author fanp
 */
public class TableField
{
    /*�ֶ�����*/
    private String m_fieldName;
    /*��󳤶�*/
    private int m_maxLength;
    /*Ĭ��ֵ*/
    private String m_defaultValue;
    /*ע��*/
    private String m_note;
    /*�Ƿ�����Ϊ��*/
    private boolean m_nullAllowed;
    /*����*/
    private int m_index;
    /*��������(��java.sql.Types���Ӧ)*/
    private int m_dataType;
    /*������������*/
    private String m_dataTypeName;

    public TableField()
    {
    }

    public int getDataType()
    {
        return m_dataType;
    }

    public String getDataTypeName()
    {
        return m_dataTypeName;
    }

    public String getDefaultValue()
    {
        return m_defaultValue;
    }

    public String getFieldName()
    {
        return m_fieldName;
    }

    public int getIndex()
    {
        return m_index;
    }

    public int getMaxLength()
    {
        return m_maxLength;
    }

    public String getNote()
    {
        return m_note;
    }

    public boolean isNullAllowed()
    {
        return m_nullAllowed;
    }

    public void setDataType(int newDataType)
    {
        m_dataType = newDataType;
    }

    public void setDataTypeName(String newDataTypeName)
    {
        m_dataTypeName = newDataTypeName;
    }

    public void setDefaultValue(String newDefaultValue)
    {
        m_defaultValue = newDefaultValue;
    }

    public void setFieldName(String newFieldName)
    {
        m_fieldName = newFieldName;
    }

    public void setIndex(int newIndex)
    {
        m_index = newIndex;
    }

    public void setMaxLength(int newMaxLength)
    {
        m_maxLength = newMaxLength;
    }

    public void setNote(String newNote)
    {
        m_note = newNote;
    }

    public void setNullAllowed(boolean newNullAllowed)
    {
        m_nullAllowed = newNullAllowed;
    }
}
