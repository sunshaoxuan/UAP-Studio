package nc.uap.lfw.build.pub.util.pdm.vo;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.itf.ITableHierarchy;

/**
 * ���ز��ṹ��ϵ�е��ӱ�VO��
 * 
 * @author fanp
 */
public class SubTable implements ITableHierarchy
{
	/*�ӱ�����*/
	private String tableName = null;

	/*�ӱ������*/
	private String foreignKeyColumn = null;

	/*�ӱ�������*/
	private String whereCondition = null;

	/*�ӱ�����*/
	private List<SubTable> subTableSet = null;
	
	/*�ű����*/
	private String sqlNo = null;
	
	/*Ԫ����*/
	private Table metaData = null;

    /**
     * ���캯��
     */
    public SubTable() 
    {
    	subTableSet = new ArrayList<SubTable>();
    }

    /**
     * �÷�������SubTable���¸����ӱ�(���Լ�)������ָ�����Ԫ����
     * @param table_name
     * @return (1)tableVO(�ҵ���Ӧ��Ԫ����); (2)null(δ�ҵ���Ӧ��Ԫ����)
     */
    public Table lookup(String table_name)
    {
    	if(getTableName().equalsIgnoreCase(table_name))
    		return getTableMetaData();
    	
    	if(getSubTableSet() == null || getSubTableSet().size() == 0)
    		return null;
    	
    	for(int i=0; i<getSubTableSet().size(); i++)
    	{
    		SubTable subTable = (SubTable)getSubTableSet().get(i);
    		Table tableVO = subTable.lookup(table_name);
            if(tableVO != null)
            	return tableVO;
    	}//for
    	return null;
    }

    public String getSqlNo()
    {
    	return sqlNo;
    }

    public void setSqlNo(String newSqlNo)
    {
    	sqlNo = newSqlNo;
    }

    public String getTableName() 
    {
	    return tableName;
    }
    
    public String getForeignKeyColumn() 
    {
	    return foreignKeyColumn;
    }
    
    public void setTableName(String newTableName) 
    {
    	tableName = newTableName;
    }
    
    public void setForeignKeyColumn(String newForeignKeyColumn) 
    {
    	foreignKeyColumn = newForeignKeyColumn;
    }
    
    public String getWhereCondition() 
    {
	    return whereCondition;
    }

    public void setWhereCondition(String newWhereCondition)
    {
    	whereCondition = newWhereCondition;
    }

    public List<SubTable> getSubTableSet() 
    {
	    return subTableSet;
    }
    
    public void setSubTableSet(List<SubTable> newSubTableSet)
    {
    	subTableSet = newSubTableSet;
    }
    
    public Table getTableMetaData()
    {
    	return metaData;
    }
    
    public void setTableMetaData(Table table_vo)
    {
    	this.metaData = table_vo;
    }
}

