package nc.uap.lfw.build.pub.util.pdm.vo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
/**
 * 表关联VO类, 对应于数据库表间的外键关联
 * 
 * @author fanp
 */
public class ReferenceVO
{
    private String id;
    private String code;
    private String name;
    private TableVO parentTableVO;
    private TableVO childrenTableVO;
    private ColumnVO[] parentColumnVOs;
    private ColumnVO[] childrenColumnVOs;

    public ReferenceVO()
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

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public TableVO getParentTable()
    {
        return this.parentTableVO;
    }

    public void setParentTable(TableVO parent_table_vo)
    {
        this.parentTableVO = parent_table_vo;
    }

    public TableVO getChildrenTable()
    {
        return this.childrenTableVO;
    }

    public void setChildrenTable(TableVO children_table_vo)
    {
        this.childrenTableVO = children_table_vo;
    }

    public ColumnVO[] getParentColumns()
    {
        return this.parentColumnVOs;
    }

    public void setParentColumns(List<ColumnVO> parent_vos)
    {
        this.parentColumnVOs = parent_vos.toArray(new ColumnVO[0]);
    }

    public ColumnVO[] getChildrenColumns()
    {
        return this.childrenColumnVOs;
    }

    public void setChildrenColumns(List<ColumnVO> children_vos)
    {
        this.childrenColumnVOs = children_vos.toArray(new ColumnVO[0]);
    }

    public void toHtml(String html_file)
    {
        StringBuffer reference_declare = new StringBuffer();
        BufferedWriter out = null;
        /* ################################## 外键定义格式 ######################################
         <html>
           <head>
             <meta http-equiv="Content-Type" content="text/html; charset=GB2312">
             <title>%nc.vo.sdp.utility.parser.ReferenceVO::getId%</title>
             <link href="nc_dd.css" type="text/css" rel="stylesheet">
           </head>

           <body>
        <h3>外键: f_gl_detail</h3>
        <table class="Grid" border="1" cellspacing="1" width="100%">
          <tr>
            <td class="Header"><p align="center">唯一标识</p></td>
            <td class="Header"><p align="center">父表</p></td>
            <td class="Header"><p align="center">父表列</p></td>
            <td class="Header"><p align="center">子表</p></td>
            <td class="Header"><p align="center">子表列</p></td>
          </tr>
          <tr>
            <td><p align="left">f_gl_detail</p></td>
            <td><p align="left">gl_detail</p></td>
            <td><p align="left">pk_gl_detail</p></td>
            <td><p align="left">gl_detail_b</p></td>
            <td><p align="left">pk_gl_detail_b</p></td>
          </tr>
        </table>
          </body>
        </html>
        ####################################################################################### */
        reference_declare.append("<html>");
        reference_declare.append("<head>");
        reference_declare.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">");
        reference_declare.append("<title>" + getId() + "</title>");
        reference_declare.append("<link href=\"nc_dd.css\" type=\"text/css\" rel=\"stylesheet\">");
        reference_declare.append("</head>");
        reference_declare.append("<body>");
        reference_declare.append("<h3>外键: " + getId() + "</h3>");
        reference_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");

        //输出表格"标题行"
        reference_declare.append("<tr>");
        reference_declare.append("<td class=\"Header\"><p align=\"center\">唯一标识</p></td>");
        reference_declare.append("<td class=\"Header\"><p align=\"center\">父表</p></td>");
        reference_declare.append("<td class=\"Header\"><p align=\"center\">父表列</p></td>");
        reference_declare.append("<td class=\"Header\"><p align=\"center\">子表</p></td>");
        reference_declare.append("<td class=\"Header\"><p align=\"center\">子表列</p></td>");
        reference_declare.append("</tr>");

        //生成表格"数据行"
        reference_declare.append("<tr>");
        reference_declare.append("<td><p align=\"left\">" + getId() + "</p></td>");
        reference_declare.append("<td><p align=\"left\">" + getParentTable().getCode() + "</p></td>");
        reference_declare.append("<td><p align=\"left\">" + getParentColumns()[0].getCode() + "</p></td>");
        reference_declare.append("<td><p align=\"left\">" + getChildrenTable().getCode() + "</p></td>");
        reference_declare.append("<td><p align=\"left\">" + getChildrenColumns()[0].getCode() + "</p></td>");
        reference_declare.append("</tr>");
        reference_declare.append("</table>");
        reference_declare.append("</body>");
        reference_declare.append("</html>");

        /* 在htmls目录下创建与传入ReferenceVO对应的html文件 */
        try
        {
            File ReferenceHTMLFile = new File(html_file);
            if (ReferenceHTMLFile.exists())
                ReferenceHTMLFile.delete();
            ReferenceHTMLFile.createNewFile();
            out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(ReferenceHTMLFile))));
            out.write(reference_declare.toString());
            out.flush();
        }
        catch(IOException ioe)
        {
            MainPlugin.getDefault().logError("生成htmls目录下的对应HTML外键定义文件失败",ioe);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch(IOException ioe)
            {
            	MainPlugin.getDefault().logError("关闭htmls目录下的对应HTML外键定义文件输出流失败",ioe);
            }
        }
    }
}

