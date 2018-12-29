package uap.lfw.smartmw.mw;

public interface IMethodTransectionType {
    int NOT_SUPPORTED = 0;

    int REQUIRED = 1;

    int SUPPORT = 2;

    int REQUEST_NEW = 3;

    int MANDATORY = 4;

    int NEVER = 5;

    int USER_MANAGED_TRANSACTION = 6;

    int TRANSECTION_NONE = java.sql.Connection.TRANSACTION_NONE;

    int TRANSECTION_SERIALIZABLE = java.sql.Connection.TRANSACTION_SERIALIZABLE;

    int TRANSECTION_READ_COMMITED = java.sql.Connection.TRANSACTION_READ_COMMITTED;

    int TRANSECTION_READ_UNCOMMITED = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

    int TRANSECTION_REPEATABLE_READ = java.sql.Connection.TRANSACTION_REPEATABLE_READ;

    int BUSENESS_METHOD_BASE = 200;

}