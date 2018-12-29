package nc.uap.lfw.build.pub.util.pdm.itf;

import java.io.File;

import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.pub.util.pdm.SDPConnection;


/**
 * 预置脚本导出公共接口
 * 
 * 需实现接口方法:
 * 
 * @author fanp
 */
public interface IExport 
{
    public static final int SQL_EXPORT_FULL_PRODUCT = 0;
    public static final int SQL_EXPORT_FULL_MODULE = 1;
    public static final int SQL_EXPORT_PARTIAL = 2;
    public static final int SQL_EXPORT_WRONG_PARAMETER = -1;
    public static final int SQL_EXPORT_WRONG_PARAMETER_VALUE = -2;
    public static final int SQL_EXPORT_NO_DATASOURCE = -3;
    public static final int SQL_EXPORT_NO_CONNECTION = -4;
    public static final int SQL_EXPORT_NO_SCRIPT_ITEMSET = -5;

    public static final String SQL_EXPORT_DB_TYPE_SQLSERVER = "SQLSERVER";
    public static final String SQL_EXPORT_DB_TYPE_ORACLE = "ORACLE";
    public static final String SQL_EXPORT_DB_TYPE_DB2 = "DB2";
    
    public static final String SQL_EXPORT_COMMON_MAP = "common.map";
    public static final String SQL_EXPORT_MODULE_MAP = "module.map";
    public static final String SQL_EXPORT_MODULE_QUERYENGINE = "QE.properties";
    
    public static final String SQL_EXPORT_DIR_MAP = "<DIRMAP>";
    public static final String SQL_EXPORT_FILE_MAP = "<FILEMAP>";

    /**
     * 该方法导出指定产品的全部初始化脚本
     * 
     * @param
     * @return
     */
    public void exportProductScript();
    
    /**
     * 该方法导出指定模块的全部初始化脚本
     * 
     * @param
     * @return
     */
    public void exportModuleScript();

    /**
     * 该方法导出指定预置脚本
     * 
     * @param uds, con
     * @return
     */
    public File exportScript(/*UnitDataSource uds,*/ SDPConnection con) throws SDPBuildException;
}
