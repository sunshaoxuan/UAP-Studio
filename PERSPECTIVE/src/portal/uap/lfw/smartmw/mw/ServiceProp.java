package uap.lfw.smartmw.mw;

public class ServiceProp implements java.io.Serializable {
    private static final long serialVersionUID = -7349365293311204874L;

    public String name = null;

    public String serviceClassName = null;

    public int accessDemandRight = 0;

    public boolean startService = false;

    public boolean keyService = true;

    public String serviceOptions = "start|stop";
}