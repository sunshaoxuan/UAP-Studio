package nc.uap.lfw.build.pub.util.pdm.vo;

/**
 * 数据库表格列VO类, 对应于Column数据库对象
 * 
 * @author fanp
 */
public class ColumnVO
{
    private String id;
    private String name;
    private String code;
    private String comment;
    private String description;
    private String dataType;
    private String length;
    private String defaultValue;
    private String mandatory;

    public ColumnVO()
    {
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDataType()
    {
        return this.dataType;
    }

    public void setLength(String length)
    {
        this.length = length;
    }

    public String getLength()
    {
        return this.length;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getdefaultValue()
    {
        return this.defaultValue;
    }

    public void setMandatory(String mandatory)
    {
        this.mandatory = mandatory;
    }

    public String getMandatory()
    {
        return this.mandatory;
    }
}

