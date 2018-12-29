package nc.uap.lfw.build.pub.util.pdm.itf;

import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.vo.SubTable;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;


/**
 * 主-子表拓补结构描述接口
 * 
 * @author fanp
 */
public interface ITableHierarchy 
{
    /* 返回表名称 */
    public String getTableName();
    
	/* 返回sql脚本序号 */
    public String getSqlNo();

    /* 返回下级子表集合 */
    public List<SubTable> getSubTableSet();
    
    /* 返回表元数据 */
    public Table getTableMetaData();
    
    /* 查询指定表的元数据 */
    public Table lookup(String table_name);
}
