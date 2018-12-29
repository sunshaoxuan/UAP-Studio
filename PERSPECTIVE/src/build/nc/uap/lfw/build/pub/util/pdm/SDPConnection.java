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
 * �ⲿ����Դ������
 * 
 * ������ʽ: ����
 * 
 * �ɷ��ʷ���: isActive, bind, commit, rollback, close, getPhysicalConnection, 
 * createStatement, prepareStatement, getLastUsedByThreadTime, getUsedByThreadTime
 * 
 * @author fanp
 */
public class SDPConnection
{
    private java.sql.PreparedStatement stmt;
    /* �Ƿ��������� */
    private boolean m_bUseXa = false;
    /* ���������ݿ�����(���ݿ⳧�̵�java.sql.Connection�ӿ�ʵ����) */
    private java.sql.Connection m_Connection;
    /* ֧���������ݿ�����(���ݿ⳧�̵�java.sql.XAConnection�ӿ�ʵ����) */
    private javax.sql.XAConnection m_XAConnection;
    /* �������ݿ��������������ʱ�� */
    public static final int timeout = 20*1000;
    /* �Զ��ύ */
    private boolean m_bAutoCommit = true;
    /* ���ӵ�ǰ�Ƿ���� */
    private boolean m_Active = true;
    /* �Ƿ�֧������ִ�� */
    private boolean m_DataBatchOperateSupported = true;
    /* ���ӽ���ʱ�� */
    private long m_BirthTime = 0L;
    /* ����ʹ�ÿ�ʼʱ�� */
    private long m_UsedByThreadTime = 0L;
    /* ����ʹ����ֹʱ�� */
    private long m_LastUsedByThreadTime = 0L;
    /* ��������Դ */
    private UnitDataSource m_DataSource;
    /* �����߳� */
    public Thread m_LastUseMeThread;

    private static final java.text.SimpleDateFormat tsFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SDPConnection(boolean hasTransaction, UnitDataSource data_source, Thread thread) throws SDPBuildException
    {
        m_DataSource = data_source;

        if(!hasTransaction)
        {
            /* ��������������ݿ����� */
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
                        //�ӳ�500ms���ٴ�����
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

                /* �����ӳ�ʱ������������ݿ�, �쳣�˳� */
                if (m_Connection == null && time >= timeout)
                	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4002));
            }
            catch(Exception e)
            {
            	MainPlugin.getDefault().logError("���ӻ�׼��ʱ��������", e);
            	/* �����������ݿ����Ӵ��� */
            	throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4003));
            }
        }
        else
        {
            /* ����֧����������ݿ����� */
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
            /* �ͷ�֧����������
             * -----------------------------------------------------------------------------
             * ��m_xaResource commit()֮��, m_connection�ر�;
             * ��������û���ύ, �������Ӵ���ʹ��״̬�����ύ��m_connection�ر�, m_connection=null;
             * �ڱ������Ӵ�������״̬��һ��ʱ��֮��m_xaconnection�ر�;
             * ��ǰIerpConnection��Ȼ����ʹ��״̬�����ͱ����ӹ����������ύ���ع���
             * ��ʹ������IerpTransaction setInuse(false)
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
            /* �ύ */
            m_Connection.commit();

            /* stmt��Ϊ����ͼ��ȡ���ӵ�Thread����ִ��������������ر�
            if(stmt != null)
            {
                stmt.close();
                stmt = null;
            }
            */
        }

        /* ״̬��Ϊ"����(idle), �黹���ӳ�" */
        m_Active = false;
        m_LastUseMeThread = null;
        m_LastUsedByThreadTime = System.currentTimeMillis();
    }

    /**
     * �÷������ص�ǰ���ݿ���������Ӧ������Դ
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