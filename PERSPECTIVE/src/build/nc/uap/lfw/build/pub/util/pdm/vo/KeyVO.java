package nc.uap.lfw.build.pub.util.pdm.vo;

import java.util.List;



/**
 * 键VO类, 对应于Primary Key, Foreign Key数据库对象
 * 
 * @author fanp
 */
public class KeyVO
{
    private int type;
    private String id;
    private String name;
    private String code;
    private String constraintName;
    private ColumnVO[] columnDefs;
    private boolean isClustered;

    public KeyVO(int type)
    {
        this.type = type;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public int getType()
    {
        return this.type;
    }

    public void setKeyId(String id)
    {
        this.id = id;
    }

    public String getKeyId()
    {
        return this.id;
    }

    public ColumnVO[] getColumnDefs()
    {
        return this.columnDefs;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setColumnDefs(List<ColumnVO> v)
    {
        columnDefs = v.toArray(new ColumnVO[0]);
    }

    public boolean isClustered()
    {
        return this.isClustered;
    }

    public void setClustered(boolean isClustered)
    {
        this.isClustered = isClustered;
    }
}

