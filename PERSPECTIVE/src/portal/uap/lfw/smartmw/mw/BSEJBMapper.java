package uap.lfw.smartmw.mw;

public class BSEJBMapper {
    public static final String EJB_BEAN_SUFFIX = "EjbBean";

    public static final String EJB_HOME_SUFFIX = "Home";

    public static final String EJB_HOME_LOCAL_SUFFIX = "Home_Local";

    public static final String EJB_OBJECT_SUFFIX = "EjbObject";

    protected static final String BUSINESS_INTF_PREFIX = "I";

    private static BSEJBMapper instance;

    static {
        instance = new BSEJBMapper();
    }

    public static BSEJBMapper getInstance() {
        return instance;
    }

    /**
     * ��ȡEJB BEAN��������
     */
    public String getEJBBean(String bsIntfFullName) {
        StringBuffer ejbBeanBuf = new StringBuffer();
        ejbBeanBuf.append(handleClsName(bsIntfFullName));
        ejbBeanBuf.append(EJB_BEAN_SUFFIX);
        return ejbBeanBuf.toString();
    }

    /**
     * ��ȡEJB HOME��������
     */
    public String getEJBHome(String bsIntfFullName) {
        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleIntfName(bsIntfFullName));
        ejbHomeBuf.append(EJB_HOME_SUFFIX);
        return ejbHomeBuf.toString();

    }

    /**
     * ��ȡEJB HOME��������
     */
    public String getEJBHome_Local(String bsIntfFullName) {

        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleClsName(bsIntfFullName));
        ejbHomeBuf.append(EJB_HOME_LOCAL_SUFFIX);
        return ejbHomeBuf.toString();

    }

    /**
     * ��ȡEJB HOME��������
     */
    public String getEJBObject(String bsIntfFullName) {

        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleIntfName(bsIntfFullName));
        //�����BS�����EjbObject��׺
        if (!bsIntfFullName.endsWith("BO")) {
            ejbHomeBuf.append(EJB_OBJECT_SUFFIX);
        }
        return ejbHomeBuf.toString();

    }

    /**
     * ��ȡEJB������
     */
    public String getEjbPackage(String bsIntfFullName) {

        int idx = bsIntfFullName.lastIndexOf(".");
        String bsIntfPackage = "";
        //���û�а�������û��
        if (-1 != idx) {
            bsIntfPackage = bsIntfFullName.substring(0, idx);
        }
        return bsIntfPackage;
    }

    /**
     * �Խӿ�û�д�I�Ľӿڽ��д���,����ҵ��ӿڹ淶��Ϊʵ�����ṩ����
     * @return ��������������
     */
    private String handleClsName(String bsIntfFullName) {
        int idx = bsIntfFullName.lastIndexOf(".");
        int len = bsIntfFullName.length();
        return bsIntfFullName.substring(idx + 1, len);

    }

    /**
     * �Խӿ�û�д�I�Ľӿڽ��д���,����ҵ��ӿڹ淶��Ϊ�ӿ��ṩ����
     * �Ѿ�����ʹ��
     * @return �������Ľӿ�����
     */
    private String handleIntfName(String bsIntfFullName) {
        int idx = bsIntfFullName.lastIndexOf(".");
        int len = bsIntfFullName.length();
        StringBuffer bsIntfName = new StringBuffer();

        if (bsIntfFullName.endsWith("BO")) {
            bsIntfName.append(bsIntfFullName.substring(idx + 1, len - 2));
        } else {
            bsIntfName.append(bsIntfFullName.substring(idx + 1, len));
        }

        return bsIntfName.toString();

    }

}
