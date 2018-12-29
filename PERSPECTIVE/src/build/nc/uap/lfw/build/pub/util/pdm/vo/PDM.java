package nc.uap.lfw.build.pub.util.pdm.vo;

import java.util.List;

/**
 * PDM顶级VO类
 * 
 * @author fanp
 */
public class PDM
{
    private String version;
    private String name;
    private String code;
//    private Stack stack;
    private List<TableVO> tableSet;
    private List<ReferenceVO> referenceSet;
    private List<ViewVO> viewSet;

    private boolean isRefNeeded;

    public PDM()
    {
//        if(stack == null)
//            stack = new Stack();
    }

    public String getVersion()
    {
        return this.version;
    }

    public void setVersion(String version)
    {
        this.version = version;
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

    public void setTableSet(List<TableVO> table_set)
    {
        this.tableSet = table_set;
    }

    public List<TableVO> getTableSet()
    {
        return this.tableSet;
    }

    public boolean isReferenceNeeded() {
        return isRefNeeded;
    }

    public void setReferenceSet(List<ReferenceVO> reference_set)
    {
        this.referenceSet = reference_set;
    }

    public void setViewSet(List<ViewVO> view_set)
    {
        this.viewSet = view_set;
    }

    public void setIsReferenceNeeded(boolean hasReference) {
        this.isRefNeeded = hasReference;
    }

	public List<ReferenceVO> getReferenceSet() {
		return referenceSet;
	}

	public List<ViewVO> getViewSet() {
		return viewSet;
	}

//    public void assembly() throws SDPBuildException
//    {
//    	try
//    	{
//            push(viewSet, IParse.VIEW_TYPE);
//            push(referenceSet, IParse.REFERENCE_TYPE);
//            push(tableSet, IParse.TABLE_TYPE);
//    	}
//    	catch(SDPBuildException sdp_build_exception)
//    	{
//    		throw sdp_build_exception;
//    	}
//    }

//    public void push(Vector v, int type) throws SDPBuildException
//    {
//        if(v != null)
//        {
//            //入栈前对象类型检查
//            for (int i = 0; i < v.size(); i++)
//            {
//                if (type == IParse.TABLE_TYPE)
//                {
//                    if (!(v.elementAt(i) instanceof TableVO))
//                    {
//                    	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2001));
//                    }
//                }
//
//                if (type == IParse.REFERENCE_TYPE)
//                {
//                    if (!(v.elementAt(i) instanceof ReferenceVO))
//                    {
//                    	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2001));
//                    }
//                }
//
//                if (type == IParse.VIEW_TYPE)
//                {
//                    if (!(v.elementAt(i) instanceof ViewVO))
//                    {
//                    	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2001));
//                    }
//                }
//            } //for
//            stack.push(v);
//        }
//        else
//        {
//            //压入占位对象(Vector Object)
//            stack.push(new Vector());
//        }
//    }
//
//    public Vector pop()
//    {
//        if(!stack.isEmpty())
//            return (Vector)stack.pop();
//        else
//            return null;
//    }
}
