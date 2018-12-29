package nc.uap.lfw.build.pub.util.pdm.itf;

public interface ICast
{
	/* XML文档转换校验: 文档类型(DocType) */
    public static final String CAST_XML_DOC_TYPE_DATASOURCE = "SDP_DATA_SOURCE";
    public static final String CAST_XML_DOC_TYPE_SCRIPT_ITEM = "SDP_SCRIPT_ITEM";
    public static final String CAST_XML_DOC_TYPE_SCRIPT_ASSOCIATION = "SCRIPT_ASSOCIATION";
    public static final String CAST_XML_DOC_TYPE_TABLE_HIERARCHY = "SDP_MULTI-TABLE_HIERARCHY";

    /* 多表结构转换: 元素名称(Element Tag) */
    public static final String CAST_TABLE_HIERARCHY_TABLENAME = "tableName";
    public static final String CAST_TABLE_HIERARCHY_SUBTABLE = "subTable";
    public static final String CAST_TABLE_HIERARCHY_FOREIGNKEYCOLUMN = "foreignKeyColumn";
    public static final String CAST_TABLE_HIERARCHY_WHERECONDITION = "whereCondition";
    public static final String CAST_TABLE_HIERARCHY_SQLNO = "sqlNo";
    public static final String CAST_TABLE_HIERARCHY_SUBTABLEGROUP = "subTableGroup";

    /* 外部数据源转换: 数据源名称前缀(Datasource Prefix) */
    public static final String CAST_DATASOURCE_PREFIX = "DS";
}
