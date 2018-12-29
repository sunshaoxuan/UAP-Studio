package nc.uap.lfw.build.pub.util.pdm.vo;

/**
 * PowerDesigner 工具校验VO类
 * 校验属性: dbtype, fileType, version
 * 
 * @author fanp
 */
public class PowerDesignerVO
{
    private String name;
    /* 数据库类型[校验属性] */
    private String dbtype;
    /* PDM文件类型[校验属性] */
    private String fileType;
    /* PowerDesigner数据库设计工具版本号[校验属性] */
    private String version;

    public PowerDesignerVO()
    {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getDbtype() {
        return dbtype;
    }

    public String getFileType() {
        return fileType;
    }

    public String getVersion() {
        return version;
    }
}
