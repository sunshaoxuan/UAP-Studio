package nc.uap.lfw.build.pub.util.pdm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.UnitDataSource;


/**
 * 外部数据源连接类
 * 
 * 工作方式: 多例
 * 
 * 可访问方法: isActive, bind, commit, rollback, close, getPhysicalConnection, 
 * createStatement, prepareStatement, getLastUsedByThreadTime, getUsedByThreadTime
 * 
 * @author fanp
 */
public class SDPConnection
{
    private java.sql.PreparedStatement stmt;
    /* 是否启用事务 */
    private boolean m_bUseXa = false;
    /* 无事务数据库连接(数据库厂商的java.sql.Connection接口实现类) */
    private java.sql.Connection m_Connection;
    /* 支持事务数据库连接(数据库厂商的java.sql.XAConnection接口实现类) */
    private javax.sql.XAConnection m_XAConnection;
    /* 单次数据库物理连接最长尝试时间 */
    public static final int timeout = 20*1000;
    /* 自动提交 */
    private boolean m_bAutoCommit = true;
    /* 连接当前是否可用 */
    private boolean m_Active = true;
    /* 是否支持批量执行 */
    private boolean m_DataBatchOperateSupported = true;
    /* 连接建立时间 */
    private long m_BirthTime = 0L;
    /* 连接使用开始时间 */
    private long m_UsedByThreadTime = 0L;
    /* 连接使用终止时间 */
    private long m_LastUsedByThreadTime = 0L;
    /* 所属数据源 */
    private UnitDataSource m_DataSource;
    /* 所属线程 */
    public Thread m_LastUseMeThread;

    private static final java.text.SimpleDateFormat tsFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SDPConnection(boolean hasTransaction, UnitDataSource data_source, Thread thread) throws SDPBuildException
    {
        m_DataSource = data_source;

        if(!hasTransaction)
        {
            /* 创建非事务的数据库连接 */
            m_bUseXa = false;
            try
            {
            	Class.forName(data_source.getDriverClassName());
                long time = 0;
                while(time < timeout)
                {
                    m_Connection = DriverManager.getConnection(m_DataSource.getDatabaseUrl(), m_DataSource.getUser(), m_DataSource.getPassword());
                    if(m_Connection != null)
                        break;
                    else
                    {
                        //延迟500ms后再次连接
                        try
                        {
                            thread.sleep(500);
                            time += 500;
                        }
                        catch(Exception e)
                        {
                            MainPlugin.getDefault().logError(e.getMessage(),e);
                        }
                    }
                }//while

                /* 如连接超时则放弃连接数据库, 异常退出 */
                if (m_Connection == null && time >= timeout)
                	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4002));
            }
            catch(Exception e)
            {
            	MainPlugin.getDefault().logError("连接基准库时发生错误", e);
            	/* 遇到其他数据库连接错误 */
            	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4003));
            }
        }
        else
        {
            /* 创建支持事务的数据库连接 */
            m_bUseXa = true;
            //to do...
        }

        m_BirthTime = System.currentTimeMillis();
        m_Active = false;
        m_LastUseMeThread = null;
        m_UsedByThreadTime = 0L;
        m_LastUsedByThreadTime = 0L;
        m_bAutoCommit = true;
        m_DataBatchOperateSupported = true;
    }

    public void bind(Thread thread) throws SDPBuildException
    {
        if(!m_Active)
        {
            this.m_UsedByThreadTime = System.currentTimeMillis();
            this.m_LastUseMeThread = thread;
            this.m_LastUsedByThreadTime = 0L;
            this.m_Active = true;
        }
        else
        	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4004));
    }

    public boolean isActive()
    {
        return m_Active;
    }

    public void clearWarnings() throws java.sql.SQLException
    {
        m_Connection.clearWarnings();
    }

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException
    {
        return m_Connection.getWarnings();
    }

    public int getTransactionIsolation() throws java.sql.SQLException
    {
        return m_Connection.getTransactionIsolation();
    }

    public void setTransactionIsolation(int level) throws java.sql.SQLException
    {
        m_Connection.setTransactionIsolation(level);
    }

    public void close() throws java.sql.SQLException
    {
        if (m_bUseXa)
        {
            /* 释放支持事务连接
             * -----------------------------------------------------------------------------
             * 在m_xaResource commit()之后, m_connection关闭;
             * 由于事物没有提交, 所以连接处于使用状态。当提交后m_connection关闭, m_connection=null;
             * 在保持连接处于无用状态到一定时间之后m_xaconnection关闭;
             * 当前IerpConnection仍然处于使用状态，当和本连接关联的事务提交后或回滚后
             * 被使用它的IerpTransaction setInuse(false)
            if (m_xaResource != null)
            {
                try
                {
                    m_ierpTransactionManager.getTransaction().delistResource(m_xaResource, javax.transaction.xa.XAResource.TMSUCCESS);
                }
                catch (javax.transaction.SystemException e)
                {
                    e.printStackTrace();
                }
            }
            */
        }
        else
        {
            /* 提交 */
            m_Connection.commit();

            /* stmt改为由试图获取连接的Thread代码执行物理创建、物理关闭
            if(stmt != null)
            {
                stmt.close();
                stmt = null;
            }
            */
        }

        /* 状态改为"空闲(idle), 归还连接池" */
        m_Active = false;
        m_LastUseMeThread = null;
        m_LastUsedByThreadTime = System.currentTimeMillis();
    }

    /**
     * 该方法返回当前数据库联接所对应的数据源
     * @return
     */
    public UnitDataSource getDataSource()
    {
    	return m_DataSource;
    }

    public void commit() throws java.sql.SQLException
    {
        m_Connection.commit();
    }

    public void rollback(Savepoint savepoint) throws SQLException
    {
        m_Connection.rollback(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return m_Connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws java.sql.SQLException
    {
        return m_Connection.createStatement(resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return m_Connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public java.sql.CallableStatement prepareCall(String sql) throws java.sql.SQLException
    {
        return m_Connection.prepareCall(sql);
    }

    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException
    {
        return m_Connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public java.sql.PreparedStatement prepareStatement(String sql) throws java.sql.SQLException
    {
        return m_Connection.prepareStatement(sql);
    }

    public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException
    {
        return m_Connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public Connection getPhysicalConnection()
    {
        return m_Connection;
    }

    public long getLastUsedByThreadTime()
    {
        return m_LastUsedByThreadTime;
    }

    public long getUsedByThreadTime()
    {
        return m_UsedByThreadTime;
    }
}