package nc.uap.lfw.build.pub.util.pdm.vo;

/**
 * PowerDesigner ����У��VO��
 * У������: dbtype, fileType, version
 * 
 * @author fanp
 */
public class PowerDesignerVO
{
    private String name;
    /* ���ݿ�����[У������] */
    private String dbtype;
    /* PDM�ļ�����[У������] */
    private String fileType;
    /* PowerDesigner���ݿ���ƹ��߰汾��[У������] */
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
