package nc.uap.lfw.build.pub.util.pdm.vo;


import java.util.List;

import nc.uap.lfw.build.pub.util.pdm.itf.IDB2;
import nc.uap.lfw.build.pub.util.pdm.itf.IOracle;
import nc.uap.lfw.build.pub.util.pdm.itf.ISql;

/**
 * 索引VO类, 对应于索引数据库对象
 * 
 * @author fanp
 */
public class IndexVO implements ISql, IDB2, IOracle
{
    private String id;
    private String name;
    private String code;
    boolean isUnique;
    /**
     * 索引中包含相应的ColumnVO
     */
    List<ColumnVO> columnDefs;

    public IndexVO()
    {
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isUnique()
    {
        return this.isUnique;
    }

    public void setUnique(boolean isUnique)
    {
        this.isUnique = isUnique;
    }

    public String getUnique()
    {
        String str = null;
        if(isUnique())
            str = ISql.DDL_UNIQUE;
        else
            str = "";
        return str;
    }

    public ColumnVO[] getColumnDefs()
    {
        if(columnDefs == null)
            return null;

        return columnDefs.toArray(new ColumnVO[0]);
    }

    public String getColumnDef_SqlServer()
    {
        if(getColumnDefs() == null)
            return "";

        StringBuffer sb = new StringBuffer();
        ////////////////////////////////////////////////////////////////////////
        //create unique  index ntb_idx_18 on ntb_md_dim_m (
        //   objcode,
        //   pk_parent
        //)
        ////////////////////////////////////////////////////////////////////////
        ColumnVO[] columnVOs = getColumnDefs();
        for(int i=0; i<columnVOs.length; i++)
        {
            ColumnVO columnVO = columnVOs[i];
            sb.append(columnVO.getCode());
            if(i != columnVOs.length-1)
            {
                sb.append(",");
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    public String getColumnDef_DB2()
    {
        if(getColumnDefs() == null)
            return "";

        StringBuffer sb = new StringBuffer();
        ////////////////////////////////////////////////////////////////////////
        //create unique index i_flow_sysinit_1 on pub_sysinit (
        //   initcode             asc,
        //   pk_org               asc
        //)
        ////////////////////////////////////////////////////////////////////////
        ColumnVO[] columnVOs = getColumnDefs();
        for(int i=0; i<columnVOs.length; i++)
        {
            ColumnVO columnVO = columnVOs[i];
            sb.append(columnVO.getCode());
            sb.append(" ");
            sb.append(IDB2.DDL_INDEX_COLUMN_SORTING);
            if(i != columnVOs.length-1)
            {
                sb.append(",");
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    public String getColumnDef_Oracle()
    {
        if(getColumnDefs() == null)
            return "";

        StringBuffer sb = new StringBuffer();
        ////////////////////////////////////////////////////////////////////////
        //create unique index idx_dimfolder on bi_dim_folder (
        //   pk_resmng_dir             asc,
        //   dir_name                  asc
        //)
        ////////////////////////////////////////////////////////////////////////
        ColumnVO[] columnVOs = getColumnDefs();
        for(int i=0; i<columnVOs.length; i++)
        {
            ColumnVO columnVO = columnVOs[i];
            sb.append(columnVO.getCode());
            sb.append(" ");
            sb.append(IOracle.DDL_INDEX_COLUMN_SORTING);
            if(i != columnVOs.length-1)
            {
                sb.append(",");
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    public void setColumnDefs(List<ColumnVO> columnDefs)
    {
        this.columnDefs = columnDefs;
    }
}

