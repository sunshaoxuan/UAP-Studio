package nc.uap.lfw.build.pub.util.pdm.itf;

/**
 * PDM解析接口
 * 
 * @author fanp
 */
public interface IParse
{
	/* 以下宏定义为"PDMParser"使用 */
    public static final int TABLE_TYPE = 0;
    public static final int INDEX_TYPE = 1;
    public static final int REFERENCE_TYPE = 2;
    public static final int VIEW_TYPE = 3;

    public static final String PDM_INSTRUCTION_TAG = "?PowerDesigner";
    public static final String PDM_NAME_TAG = "Name";
    public static final String PDM_VENDOR = "SYBASE";
    public static final String PDM_VERSION_TAG = "version";
    public static final String PDM_VERSION = "12.0.0.1700";
    public static final String PDM_FILE_TYPE_TAG = "signature";
    public static final String PDM_FILE_TYPE = "PDM_DATA_MODEL_XML";
    public static final String PDM_DB_TYPE_TAG = "Target";
    public static final String PDM_DB_TYPE = "Microsoft SQL Server 2005";
    public static final String PDM_BAK_FILE_TYPE = "pdb";

    public static final int KEY_PRIMARY = 0;
    public static final int KEY_FOREIGN = 1;

    public static final String PDM_NAME = "/Model/RootObject/Children/Model/Name";
    public static final String PDM_CODE = "/Model/RootObject/Children/Model/Code";

    public static final String TABLE = "/Model/RootObject/Children/Model/Tables/Table";
    public static final String TABLE_ID_ATTRIBUTE = "Id";

    public static final String COLUMN = "/Columns/Column";
    public static final String COLUMN_ID_ATTRIBUTE = "Id";

    public static final String KEY = "/Keys/Key";
    public static final String KEY_ID_ATTRIBUTE = "Id";
    public static final String KEY_COLUMN = "/Key.Columns/Column";
    public static final String KEY_COLUMN_REF = "o:Column";
    public static final String KEY_COLUMN_REF_ATTRIBUTE = "Ref";
    public static final String KEY_CONSTRAINT = "a:ConstraintName";

    public static final String PK_KEY = "o:Key";
    public static final String PK_REF_ATTRIBUTE = "Ref";

    public static final String CLUSTER_KEY = "/ClusterObject/Key";
    public static final String CLUSTER_KEY_ATTRIBUTE = "Ref";
    public static final String CLUSTER_INDEX_ATTRIBUTE = "Ref";
    public static final String IS_CLUSTER_KEY = "CLUSTER_KEY";
    public static final String IS_CLUSTER_INDEX = "CLUSTER_INDEX";

    public static final String INDEXES = "/Indexes/Index";
    public static final String INDEXES_ID_ATTRIBUTE = "Id";
    public static final String INDEXES_COLUMN = "/IndexColumns/IndexColumn";
    public static final String INDEXES_COLUMN_REF = "/Column/Column";
    public static final String INDEXES_COLUMN_REF_ATTRIBUTE = "Ref";

    public static final String REFERENCE = "/Model/RootObject/Children/Model/References/Reference";
    public static final String REFERENCE_ID_ATTRIBUTE = "Id";
    public static final String REFERENCE_PARENT_TABLE = "/Object1/Table";
    public static final String REFERENCE_CHILDREN_TABLE = "/Object2/Table";
    public static final String REFERENCE_TABLE_REF_ATTRIBUTE = "Ref";
    public static final String REFERENCE_PARENT_COLUMN = "/Object1/Column";
    public static final String REFERENCE_CHILDREN_COLUMN = "/Object2/Column";
    public static final String REFERENCE_COLUMN_REF_ATTRIBUTE = "Ref";
    public static final String REFERENCE_JOINS = "/Joins/ReferenceJoin";

    public static final String VIEW = "/Model/RootObject/Children/Model/Views/View";
    public static final String VIEW_ID_ATTRIBUTE = "Id";
    public static final String VIEW_QUERY_SQL = "/View.SQLQuery";

    public static final String COLUMN_DATATYPE_UNDEFINED = "<Undefined>";


    /* 以下宏定义为"HTMLParser"使用 */
    public static final String _SDP_NC_502 = "5.02";
    public static final String _SDP_NC_53 = "5.3";
    public static final String _SDP_NC_502_RESOURCE_ = "d:\\sdp\\nc\\5.02\\resource";
    public static final String _SDP_NC_502_TEMPLATE_ = "d:\\sdp\\nc\\5.02\\template";
    public static final String _SDP_NC_53_RESOURCE_ = "d:\\sdp\\nc\\5.3\\resource";
    public static final String _SDP_NC_53_TEMPLATE_ = "d:\\sdp\\nc\\5.3\\template";
    public static final String _SDP_REPORT_TEMPLATE_DOC_HOME_ = "NC_DD_DOC_HOME_TEMPLATE";
    public static final String _SDP_REPORT_TEMPLATE_FRAME_ = "NC_DD_FRAME_TEMPLATE";
    public static final String _SDP_REPORT_TEMPLATE_HEADER_ = "NC_DD_HEADER_TEMPLATE";
    public static final String _SDP_REPORT_TEMPLATE_NAVIGATOR_ = "NC_DD_NAVIGATOR_TEMPLATE";


    /* 以下宏定义为"TemplateParser"使用 */
    public static final String TEMPLATE_DYNAMIC_TRANSLATION_INDECATOR = "%";
}
