package nc.uap.lfw.build.pub.util.pdm.itf;

import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.vo.SubTable;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;


/**
 * ��-�ӱ��ز��ṹ�����ӿ�
 * 
 * @author fanp
 */
public interface ITableHierarchy 
{
    /* ���ر����� */
    public String getTableName();
    
	/* ����sql�ű���� */
    public String getSqlNo();

    /* �����¼��ӱ��� */
    public List<SubTable> getSubTableSet();
    
    /* ���ر�Ԫ���� */
    public Table getTableMetaData();
    
    /* ��ѯָ�����Ԫ���� */
    public Table lookup(String table_name);
}
