package nc.uap.lfw.build.pub.util.pdm.dbrecord;

/**
 * �ⲿ����ԴVOת������
 * 
 * ������ʽ: ��DBRecordConfigUtil��DataSource.xmlת���õ�
 * 
 * @author fanp
 */
public class UnitDataSource
{
	/* ����Դ���� */
    public String dataSourceName;
    /* ���ݿ������� */
    public String driverClassName;
    /* ���ݿ����Ӵ� */
    public String databaseUrl;
    /* ���ݿ��½�û��� */
    public String user;
    /* ���ݿ��½�û����� */
    public String password;
    /* ���ݿ���������� */
    public String maxCon;
    /* ���ݿ���С������ */
    public String minCon;
    /* ����������Դ�� */
    public String dataSourceClassName;
    /* ����������Դ�� */
    public String xaDataSourceClassName;
    /* ���ݿ����� */
    public String databaseType;
    /* ���ݿ�汾 */
    public String databaseVersion;

	/**
	 * ���캯��
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

