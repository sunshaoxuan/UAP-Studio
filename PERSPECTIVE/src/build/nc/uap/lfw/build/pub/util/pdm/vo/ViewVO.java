package nc.uap.lfw.build.pub.util.pdm.vo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import nc.lfw.lfwtools.perspective.MainPlugin;

/**
 * 视图VO类, 对应于View数据库对象
 * 
 * @author fanp
 */
public class ViewVO
{
    String id;
    String code;
    String name;
    String querySql;

    public ViewVO()
    {
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setQuery(String query)
    {
        this.querySql = translate(query);
    }

    public String getQuery()
    {
        return this.querySql;
    }

    private String translate(String str)
    {
        if(str == null)
            return null;

        return str.replaceAll("&#39;", "'");
    }

    public void toHtml(String html_file)
    {
        StringBuffer view_declare = new StringBuffer();
        BufferedWriter out = null;
        /* ################################## 视图定义格式 ####################################
         <html>
           <head>
             <meta http-equiv="Content-Type" content="text/html; charset=GB2312">
             <title>%nc.vo.sdp.utility.parser.ViewVO::getName%</title>
             <link href="nc_dd.css" type="text/css" rel="stylesheet">
           </head>

           <body>
        <h2>视图: View_1</h2>
        <table class="Form" border="1" cellspacing="1" width="100%">
          <tr>
            <td class="Header" width="25%"><p align="left">名称</p></td>
            <td width="75%"><p align="left">View_1</a></p></td>
          </tr>
          <tr>
            <td class="Header" width="25%"><p align="left">编码</p></td>
            <td width="75%"><p align="left">View_1</a></p></td>
          </tr>
        </table>

        <table class="Text" border="1" cellpadding="3" cellspacing="1" width="100%">
          <tr>
            <td><p align="left">select DISTINCT bd_defdoc.docname,trm_dept_req_dt.trm_method from trm_dept_req_dt<br/>left join bd_defdoc on (bd_defdoc.pk_defdoc=trm_dept_req_dt.trm_method and bd_defdoc.pk_defdoclist = 'TRM00000000000000003')</p></td>
          </tr>
        </table>
         </body>
        </html>
        ################################################################################### */
        view_declare.append("<html>");
        view_declare.append("<head>");
        view_declare.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">");
        view_declare.append("<title>" + getName() + "</title>");
        view_declare.append("<link href=\"nc_dd.css\" type=\"text/css\" rel=\"stylesheet\">");
        view_declare.append("</head>");
        view_declare.append("<body>");
        view_declare.append("<h2>视图: " + getName() + "</h2>");
        view_declare.append("<table class=\"Form\" border=\"1\" cellspacing=\"1\" width=\"100%\">");
        view_declare.append("<tr>");
        view_declare.append("<td class=\"Header\" width=\"25%\"><p align=\"left\">名称</p></td>");
        view_declare.append("<td width=\"75%\"><p align=\"left\">" + getName() + "</a></p></td>");
        view_declare.append("</tr>");
        view_declare.append("<tr>");
        view_declare.append("<td class=\"Header\" width=\"25%\"><p align=\"left\">编码</p></td>");
        view_declare.append("<td width=\"75%\"><p align=\"left\">" + getCode() + "</a></p></td>");
        view_declare.append("</tr>");
        view_declare.append("</table>");
        view_declare.append("<table class=\"Text\" border=\"1\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\">");
        view_declare.append("<tr>");
        view_declare.append("<td><p align=\"left\">" + getQuery() + "</p></td>");
        view_declare.append("</tr>");
        view_declare.append("</table>");
        view_declare.append("</body>");
        view_declare.append("</html>");

        /* 在htmls目录下创建与传入ViewVO对应的html文件 */
        try
        {
            File ViewHTMLFile = new File(html_file);
            if (ViewHTMLFile.exists())
                ViewHTMLFile.delete();
            ViewHTMLFile.createNewFile();
            out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(ViewHTMLFile))));
            out.write(view_declare.toString());
            out.flush();
        }
        catch(IOException ioe)
        {
        	MainPlugin.getDefault().logError("生成htmls目录下的对应HTML视图定义文件失败",ioe);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch(IOException ioe)
            {
            	MainPlugin.getDefault().logError("关闭htmls目录下的对应HTML视图定义文件输出流失败",ioe);
            }
        }
    }
}

