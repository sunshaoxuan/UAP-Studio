package nc.uap.lfw.build.pub.util.pdm.itf;

/**
 * SQL½Ó¿Ú
 * 
 * @author fanp
 */
public interface ISql
{
    public static final String _DB2_ = "DB2";
    public static final String _SQLSERVER_ = "SQLSERVER";
    public static final String _ORACLE_ = "ORACLE";
    public static final String _ALL_ = "ALL";

    public static final String DDL_DEFAULT = "default";
    public static final String DDL_CONSTRAINT = "constraint";
    public static final String DDL_PRIMARY_KEY = "primary key";
    public static final String DDL_FORIGN_KEY = "foreign key";
    public static final String DDL_CLUSTERED = "clustered";
    public static final String DDL_NON_CLUSTERED = "nonclustered";
    public static final String DDL_COLUMN = "column";
    public static final String DDL_NULL = "null";
    public static final String DDL_NOT_NULL = "not null";
    public static final String DDL_UNIQUE = "unique";

    public static final String DDL_BLOCK_BEGIN = "(";
    public static final String DDL_BLOCK_END = ")";
    public static final String DDL_DESCRIPTION_BEGIN = "/*";
    public static final String DDL_DESCRIPTION_END = "*/";

    public static final String DDL_NC_EXT_METHOD_SEPERATOR = "::";
    public static final String DDL_NC_EXT_PARAMETER = "paraClass=";
    public static final String DDL_NC_EXT_HOSTOBJECT = "hostobject=";
    public static final String DDL_NC_EXT_PARAMETER_BEGIN = "{";
    public static final String DDL_NC_EXT_PARAMETER_END = "}";
    public static final String DDL_NC_EXT_PARAMETER_SEPERATOR = ",";

    public static final int SCRIPT_SENSITIVE = 0;
    public static final int SCRIPT_LOWERCASE = 1;
    public static final int SCRIPT_UPPERCASE = 2;
    public static final int SCRIPT_SERIALIZED = 3;
    public static final int SCRIPT_DESERIALIZED = 4;
}
