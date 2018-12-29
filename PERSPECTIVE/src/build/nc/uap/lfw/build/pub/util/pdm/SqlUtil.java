package nc.uap.lfw.build.pub.util.pdm;

import nc.uap.lfw.build.pub.util.pdm.vo.TableField;

/**
 * Sql������
 * 
 * �ɷ��ʷ���: replaceQuotMark, isNumber, isString
 *
 * @author fanp
 */
public class SqlUtil 
{
    /**
     *  �÷�����ָ���ַ����а��������÷���("'")�滻Ϊ"''"
     * 
     * @return java.lang.String
     * @param strValue java.lang.String
     */
    public static String replaceQuotMark(String strValue) 
    {

    	String oldMark = "'";
    	String strResult = strValue;

    	if(strValue != null && strValue.length() > 0 && strValue.indexOf(oldMark) >= 0)
    	{
    		boolean hasOneQuoMard = true;
    		int pos = 0;
    		while (hasOneQuoMard) 
    		{
    			pos = strResult.indexOf(oldMark, pos);
    			if (pos < 0)
    				break;
    			if (pos >= strResult.length() - 1) 
    			{
    				strResult = strResult.substring(0, strResult.length()) + oldMark;
    				hasOneQuoMard = false;
    			} 
    			else 
    			{
    				if (strResult.substring(pos + 1, pos + 2).equals(oldMark)) 
    				{
    					pos += 2;
    				} 
    				else 
    				{
    					strResult = strResult.substring(0, pos + 1) + oldMark + strResult.substring(pos + 1);
    					pos += 2;
    				}
    			}
    		}
    	}
    	return strResult;
    }
    
    /**
     * �÷����жϴ��������Ƿ�Ϊ"������"
     * @param field
     * @return
     */
    public static boolean isNumber(TableField field)
    {
        boolean isNum = false;

        switch(field.getDataType())
        {
        case java.sql.Types.TINYINT:
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
        case java.sql.Types.BIGINT:
        case java.sql.Types.FLOAT:
        case java.sql.Types.REAL:
        case java.sql.Types.DOUBLE:
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        	isNum = true;
            break;

        default:
            isNum = false;
            break;
        }

        return isNum;
    }

    /**
     * �÷����жϴ��������Ƿ�Ϊ�ַ���
     * @param field
     * @return
     */
    public static boolean isString(TableField field)
    {
        boolean isNum = false;

        switch(field.getDataType())
        {
        case java.sql.Types.CHAR:
        case java.sql.Types.VARCHAR:
        case java.sql.Types.LONGVARCHAR:
        	isNum = true;
            break;
            
        default:
        	isNum = false;
            break;
        }

        return isNum;
    }
}
