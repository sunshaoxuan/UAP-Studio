package nc.uap.lfw.build.pub.util.pdm.itf;

/**
 * Microsoft SQL Server数据库脚本接口
 * 
 * @author fanp
 */
public interface ISQLServer
{
    /* ts, dr自定义字段定义 */
    public static final String DDL_SQLSERVER_TS = "ts char(19) null default convert(char(19),getdate(),20)";
    public static final String DDL_SQLSERVER_DR = "dr smallint null default 0";

    /* 脚本分割符 */
    public static final String DDL_SQLSERVER_ENDSCRIPT = "go";

    /* 表、索引、外键、视图脚本模板 */
    //public static final String DDL_SQLSERVER_CREATETABLE_TEMPLATE = "create table %nc.vo.sdp.build.TableVO::getCode paraClass={} hostobject=0% (%nc.vo.sdp.build.TableVO::getColumnDef paraClass={} hostobject=0% %nc.vo.sdp.build.TableVO::getCustomizedColumnDef_SqlServer paraClass={} hostobject=0% %nc.vo.sdp.build.TableVO::getPrimaryKeyDef_SqlServer paraClass={} hostobject=0%)";
    public static final String DDL_SQLSERVER_CREATETABLE_TEMPLATE = "create table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (\r\n%nc.sdp.build.pub.util.pdm.vo.TableVO::getColumnDef paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getPrimaryKeyDef_SqlServer paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getCustomizedColumnDef_SqlServer paraClass={} hostobject=0%)";
    public static final String DDL_SQLSERVER_CREATEINDEX_TEMPLATE = "create %nc.sdp.build.pub.util.pdm.vo.IndexVO::getUnique paraClass={} hostobject=1% index %nc.sdp.build.pub.util.pdm.vo.IndexVO::getCode paraClass={} hostobject=1% on %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (%nc.sdp.build.pub.util.pdm.vo.IndexVO::getColumnDef_SqlServer paraClass={} hostobject=1%)";
    public static final String DDL_SQLSERVER_CREATEREFERENCE_TEMPLATE = "alter table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% add constraint %nc.sdp.build.pub.util.pdm.vo.ReferenceVO::getCode paraClass={} hostobject=1% foreign key (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=2%) references %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=3% (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=4%)";
    public static final String DDL_SQLSERVER_CREATEVIEW_TEMPLATE = "create view %nc.sdp.build.pub.util.pdm.vo.ViewVO::getCode paraClass={} hostobject=0% as %nc.sdp.build.pub.util.pdm.vo.ViewVO::getQuery paraClass={} hostobject=0%";

    /* 数据库对象长度校验标准 */
    public static final int SQLSERVER_PRIMARYKEY_LENGTH = 30;
}

