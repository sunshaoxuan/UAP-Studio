package nc.uap.lfw.build.pub.util.pdm.dbrecord;

/**
 * 外部数据源VO转换子类
 * 
 * 工作方式: 由DBRecordConfigUtil对DataSource.xml转换得到
 * 
 * @author fanp
 */
public class UnitDataSource
{
	/* 数据源名称 */
    public String dataSourceName;
    /* 数据库驱动类 */
    public String driverClassName;
    /* 数据库连接串 */
    public String databaseUrl;
    /* 数据库登陆用户名 */
    public String user;
    /* 数据库登陆用户口令 */
    public String password;
    /* 数据库最大连接数 */
    public String maxCon;
    /* 数据库最小连接数 */
    public String minCon;
    /* 非事务数据源类 */
    public String dataSourceClassName;
    /* 带事务数据源类 */
    public String xaDataSourceClassName;
    /* 数据库类型 */
    public String databaseType;
    /* 数据库版本 */
    public String databaseVersion;

	/**
	 * 构造函数
	 * 
	 * @return
	 */
    public UnitDataSource()
    {
    }

    public void setDataSourceName(String dataSourceName) 
    {
        this.dataSourceName = dataSourceName;
    }

    public void setDriverClassName(String driverClassName) 
    {
        this.driverClassName = driverClassName;
    }

    public void setDatabaseUrl(String databaseUrl) 
    {
        this.databaseUrl = databaseUrl;
    }

    public void setUser(String user) 
    {
        this.user = user;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public void setMaxCon(String maxCon) 
    {
        this.maxCon = maxCon;
    }

    public void setMinCon(String minCon) 
    {
        this.minCon = minCon;
    }

    public void setDataSourceClassName(String dataSourceClassName) 
    {
        this.dataSourceClassName = dataSourceClassName;
    }

    public void setXaDataSourceClassName(String xaDataSourceClassName) 
    {
        this.xaDataSourceClassName = xaDataSourceClassName;
    }

    public void setDatabaseType(String databaseType) 
    {
        this.databaseType = databaseType;
    }

    public void setDatabaseVersion(String databaseVersion)
    {
    	this.databaseVersion = databaseVersion;
    }

    public String getDataSourceName() 
    {
        return dataSourceName;
    }

    public String getDriverClassName() 
    {
        return driverClassName;
    }

    public String getDatabaseUrl() 
    {
        return databaseUrl;
    }

    public String getUser() 
    {
        return user;
    }

    public String getPassword() 
    {
        return password;
    }

    public String getMaxCon() 
    {
        return maxCon;
    }

    public String getMinCon() 
    {
        return minCon;
    }

    public String getDataSourceClassName() 
    {
        return dataSourceClassName;
    }

    public String getXaDataSourceClassName() 
    {
        return xaDataSourceClassName;
    }

    public String getDatabaseType() 
    {
        return databaseType;
    }

    public String getDatabaseVersion()
    {
        return databaseVersion;
    }
}

