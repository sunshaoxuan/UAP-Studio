package nc.uap.lfw.build.pub.util.pdm.itf;

/**
 * Oracle 8i2,9i,10g���ݿ�ű��ӿ�
 * 
 * @author fanp
 */
public interface IOracle
{
    /* �����ֶ����򷽷� */
    public static final String DDL_INDEX_COLUMN_SORTING = "asc";

    /* ts, dr�Զ����ֶζ��� */
    public static final String DDL_ORACLE_TS = "ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')";
    public static final String DDL_ORACLE_DR = "dr number(10) default 0";

    /* �ű��ָ��� */
    public static final String DDL_ORACLE_ENDSCRIPT = "/";

    /* ���������������ͼ�ű�ģ�� */
    public static final String DDL_ORACLE_CREATETABLE_TEMPLATE = "create table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (%nc.sdp.build.pub.util.pdm.vo.TableVO::getColumnDef_Oracle paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getPrimaryKeyDef_Oracle paraClass={} hostobject=0% %nc.sdp.build.pub.util.pdm.vo.TableVO::getCustomizedColumnDef_Oracle paraClass={} hostobject=0%)";
    public static final String DDL_ORACLE_CREATEINDEX_TEMPLATE = "create %nc.sdp.build.pub.util.pdm.vo.IndexVO::getUnique paraClass={} hostobject=1% index %nc.sdp.build.pub.util.pdm.vo.IndexVO::getCode paraClass={} hostobject=1% on %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% (%nc.sdp.build.pub.util.pdm.vo.IndexVO::getColumnDef_Oracle paraClass={} hostobject=1%)";
    public static final String DDL_ORACLE_CREATEREFERENCE_TEMPLATE = "alter table %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=0% add constraint %nc.sdp.build.pub.util.pdm.vo.ReferenceVO::getCode paraClass={} hostobject=1% foreign key (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=2%) references %nc.sdp.build.pub.util.pdm.vo.TableVO::getCodeDef paraClass={} hostobject=3% (%nc.sdp.build.pub.util.pdm.vo.ColumnVO::getCode paraClass={} hostobject=4%)";
    public static final String DDL_ORACLE_CREATEVIEW_TEMPLATE = "create view %nc.sdp.build.pub.util.pdm.vo.ViewVO::getCode paraClass={} hostobject=0% as %nc.sdp.build.pub.util.pdm.vo.ViewVO::getQuery paraClass={} hostobject=0%";

    /* ���ݿ���󳤶�У���׼ */
    public static final int ORACLE_PRIMARYKEY_LENGTH = 30;
    public static final int ORACLE_INDEX_LENGTH = 30;
    public static final int ORACLE_REFERENCE_LENGTH = 30;
}

