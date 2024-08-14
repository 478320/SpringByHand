package org.springframework.coreTransactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理对象，储存数据库连接
 */
public class TransactionalManager {

    DataSource dataSource;

    // 线程变量，保证不同用户拿到不同的连接
    ThreadLocal<Connection> connection = new ThreadLocal<>();

    public TransactionalManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionalManager() {
    }

    public Connection getConnection() throws SQLException {
        if (connection.get() != null) {
            return connection.get();
        } else {
            // 获取新链接
            connection.set(dataSource.getConnection());
        }
        return connection.get();
    }

    @Override
    public String toString() {
        return "TransactionalManager{" +
                "dataSource=" + dataSource +
                ", connection=" + connection +
                '}';
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
