package uap.lfw.smartmw.mw;


/**
 * 数据源描述
 */
public class EJBDataSource implements java.io.Serializable {

    private static final long serialVersionUID = 4706953654430490928L;

    public java.lang.String dataSourceName;

    public java.lang.String driverClassName;

    public java.lang.String databaseUrl;

    public java.lang.String user;

    public java.lang.String password;

    /* 最大连接数 */
    public int maxCon = 15;

    public int minCon = 5;

    public java.lang.String dataSourceClassName;

    public java.lang.String xaDataSourceClassName;

    public java.lang.String databaseType;

    public int conIncrement = 0;

    public int conInUse = 0;

    public int conIdle = 0;

    public JdbcProperty[] jdbcProperty = {};

    public void addJdbcProperty(JdbcProperty p) {
        jdbcProperty = (JdbcProperty[]) Convertor.addOrReplaceObjectToArray(jdbcProperty, "propertyName", p);
    }

    public JdbcProperty getJdbcProperty(String name) {
        return (JdbcProperty) Convertor.getObjtFrmArry(jdbcProperty, "propertyName", name);
    }

    public void removeJdbcProperty(String prop) {
        jdbcProperty = (JdbcProperty[]) Convertor.removeObjectFromArray(jdbcProperty, "propertyName", prop);
    }

    public boolean resetJdbcProperty(JdbcProperty p) {
        return Convertor.replaceObjectInArray(jdbcProperty, "propertyName", p);
    }

    public void setMinCon(int n) {
        minCon = n;
    }

    public int getMinCon() {
        return minCon;
    }

    /**
     * EJBDataSource 构造子注解。
     */
    public EJBDataSource() {
        super();
    }

    public java.lang.String getDatabaseUrl() {
        return databaseUrl;
    }

    public java.lang.String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public java.lang.String getDataSourceName() {
        return dataSourceName;
    }

    public java.lang.String getDriverClassName() {
        return driverClassName;
    }

    public int getMaxCon() {
        return maxCon;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public java.lang.String getType() {
        return databaseType;
    }

    public java.lang.String getUser() {
        return user;
    }

    public java.lang.String getXaDataSourceClassName() {
        return xaDataSourceClassName;
    }

    public void setDatabaseUrl(java.lang.String newDatabaseUrl) {
        databaseUrl = newDatabaseUrl;
    }

    public void setDataSourceClassName(java.lang.String newDataSourceClassName) {
        dataSourceClassName = newDataSourceClassName;
    }

    public void setDataSourceName(java.lang.String newDataSourceName) {
        dataSourceName = newDataSourceName;
    }

    public void setDriverClassName(java.lang.String newDriverClassName) {
        driverClassName = newDriverClassName;
    }

    public void setMaxCon(int newMaxCon) {
        maxCon = newMaxCon;
    }

    public void setPassword(java.lang.String newPassword) {
        password = newPassword;
    }

    public void setType(java.lang.String dbType) {
        databaseType = dbType;
    }

    public void setUser(java.lang.String newUser) {
        user = newUser;
    }

    public void setXaDataSourceClassName(java.lang.String newXaDataSourceClassName) {
        xaDataSourceClassName = newXaDataSourceClassName;
    }

    public String toString() {
        StringBuffer sbInfo = new StringBuffer();

        sbInfo.append("DataSource Name: \t\t");
        sbInfo.append(dataSourceName);
        sbInfo.append(";\r\n");

        sbInfo.append("Database URL: \t\t");
        sbInfo.append(databaseUrl);
        sbInfo.append(";\r\n");

        sbInfo.append("Connect User: \t");
        sbInfo.append(user);
        sbInfo.append(";\r\n");

        sbInfo.append("Max Connection: \t");
        sbInfo.append(maxCon);
        sbInfo.append(";\r\n");

        sbInfo.append("Database Type: \t\t");
        sbInfo.append(databaseType);
        sbInfo.append(";\r\n");

        return sbInfo.toString();
    }

    public JdbcProperty[] getJdbcProperties() {
        return jdbcProperty;
    }

    public void setJdbcProperties(JdbcProperty[] jdbcProperties) {
        this.jdbcProperty = jdbcProperties;
    }
}