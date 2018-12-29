package nc.uap.lfw.build.pub.util.pdm.itf;

/**
 * IBM DB2 8.x数据库脚本接口
 * 
 * @author fanp
 */
public interface IDB2
{
    /* 索引字段排序方法 */
    public static final String DDL_INDEX_COLUMN_SORTING = "asc";

    /* ts, dr自定义字段定义 */
    public static final String DDL_DB2_TS = "ts char(19) null";
    public static final String DDL_DB2_DR = "dr smallint null default 0";

    /* 脚本分隔符 */
    public static final String DDL_DB2_ENDSCRIPT = ";";

    /* 表、索引、外键、视图脚本模板 */
    public static final String DDL_DB2_CREATETABLE_TEMPLATE = "create table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (%nc.sdp.build.pub.util.pdm.vo.TableVO::getColumnDef paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getPrimaryKeyDef_DB2 paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getCustomizedColumnDef_DB2 paraClass={} hostobject=0%)";
    public static final String DDL_DB2_CREATEINDEX_TEMPLATE = "create %nc.sdp.build.pub.util.pdm.vo.IndexVO::getUnique paraClass={} hostobject=1% index %nc.sdp.build.pub.util.pdm.vo.IndexVO::getCode paraClass={} hostobject=1% on %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (%nc.sdp.build.pub.util.pdm.vo.IndexVO::getColumnDef_DB2 paraClass={} hostobject=1%)";
    public static final String DDL_DB2_CREATEREFERENCE_TEMPLATE = "alter table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% add constraint %nc.sdp.build.pub.util.pdm.vo.ReferenceVO::getCode paraClass={} hostobject=1% foreign key (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=2%) references %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=3% (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=4%)";
    public static final String DDL_DB2_CREATEVIEW_TEMPLATE = "create view %nc.sdp.build.pub.util.pdm.vo.ViewVO::getCode paraClass={} hostobject=0% as %nc.sdp.build.pub.util.pdm.vo.ViewVO::getQuery paraClass={} hostobject=0%";

    /* 数据库对象长度校验标准 */
    public static final int DB2_INDEX_LENGTH = 18;
    public static final int DB2_PRIMARYKEY_LENGTH = 18;
    public static final int DB2_REFERENCE_LENGTH = 18;
}

