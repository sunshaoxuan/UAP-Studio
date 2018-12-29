package uap.lfw.smartmw.mw;


public class MiddleParameter implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = -6821493038486675635L;

    public String ncHome = System.getProperty("user.dir");
	
	public String ejbConfigDir = ncHome + "/ejbXMLs";
    
    public String extServiceConfigDir =  ncHome + "/ejbXMLs";

	public String jndiContextFactory = "";

	public String jndiProviderURL = "";

	public String serverName = "DefServer";
	
	public ServiceProp externServiceArray[] = {};

	public ServiceProp internalServiceArray[] = {};

	public EJBBeanDescriptor[] ejbBeans = {};

	public EJBDataSource[] dataSource = {};
    
    public boolean isEncode = false;

	public MiddleParameter cloneX() throws CloneNotSupportedException {
		return (MiddleParameter) this.clone();
	}

	public String TransactionManagerProxyClass = "nc.bs.mw.tran.IerpTransactionManagerProxy";
	public String UserTransactionClass = "nc.bs.mw.tran.IerpUserTransaction";
	public String TransactionManagerClass = "nc.bs.mw.tran.IerpTransactionManager";
	public String SqlDebugSetClass = "nc.bs.mw.sql.UFSqlObject";
	public String XADataSourceClass = "nc.bs.mw.ejbsql.IerpXADataSource";
    
	

	public MiddleParameter() {
		super();
	}

	public void addDataSource(EJBDataSource ds) {
		dataSource = (EJBDataSource[]) Convertor.addOrReplaceObjectToArray(
				dataSource, "dataSourceName", ds);
	}

	public void addEJBBean(EJBBeanDescriptor bd) {
		ejbBeans = (EJBBeanDescriptor[]) Convertor.addOrReplaceObjectToArray(
				ejbBeans, "beanName", bd);
	}

	public void addExternService(ServiceProp es) {
		externServiceArray = (ServiceProp[]) Convertor
				.addOrReplaceObjectToArray(externServiceArray, "name", es);
	}

	public EJBDataSource getDataSource(String name) {
		return (EJBDataSource) Convertor.getObjtFrmArry(dataSource,
				"dataSourceName", name);
	}

	public EJBBeanDescriptor[] getEjbBeans() {
		return ejbBeans;
	}

	public EJBBeanDescriptor getEjbBeans(String beanName) {
		return (EJBBeanDescriptor) Convertor.getObjtFrmArry(ejbBeans,
				"beanName", beanName);
	}

	public EJBBeanDescriptor getEjbBeansByRefName(String beanName) {
		return (EJBBeanDescriptor) Convertor.getObjtFrmArry(ejbBeans,
				"ejb_ref_name", beanName);
	}

	public ServiceProp getExternService(String name) {
		return (ServiceProp) Convertor.getObjtFrmArry(externServiceArray,
				"name", name);
	}

	
	public ServiceProp getInternalService(String name) {
		return (ServiceProp) Convertor.getObjtFrmArry(internalServiceArray,
				"name", name);

	}    
	
	public void removeDataSource(String source) {
		dataSource = (EJBDataSource[]) Convertor.removeObjectFromArray(
				dataSource, "dataSourceName", source);
	}

	public void removeEJBBean(String bean) {
		ejbBeans = (EJBBeanDescriptor[]) Convertor.removeObjectFromArray(
				ejbBeans, "beanName", bean);
	}

	public void removeExternService(String name) {
		externServiceArray = (ServiceProp[]) Convertor.removeObjectFromArray(
				externServiceArray, "name", name);
	}

	public boolean resetDataSource(EJBDataSource ds) {
		return Convertor.replaceObjectInArray(dataSource, "dataSourceName", ds);
	}

	public boolean resetEJBBean(EJBBeanDescriptor bd) {
		return Convertor.replaceObjectInArray(ejbBeans, "ejb_ref_name", bd);
	}
}