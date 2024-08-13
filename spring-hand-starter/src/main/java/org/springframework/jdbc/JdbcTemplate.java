package org.springframework.jdbc;


import org.springframework.annotation.Autowired;
import org.springframework.coreTransactional.TransactionalManager;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class JdbcTemplate {

    @Override
    public String toString() {
        return "JdbcTemplate{" +
                "dataSource=" + dataSource +
                ", transactionalManager=" + transactionalManager +
                '}';
    }

    private DataSource dataSource;

    private TransactionalManager transactionalManager;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcTemplate() {
    }

    public JdbcTemplate(DataSource dataSource, TransactionalManager transactionalManager) {
        this.dataSource = dataSource;
        this.transactionalManager = transactionalManager;
    }

    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    private int invokeUpdate(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeUpdate();
    }

    private ResultSet getResultSet(PreparedStatement preparedStatement, Object[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i, values[i]);
        }
        return preparedStatement.executeQuery();
    }

    private List<Object> pareseResultSet(ResultSet resultSet, RowMapper rowMapper) throws Exception {
        ArrayList<Object> list = new ArrayList<>();
        int num = 1;
        while (resultSet.next()) {
            list.add(rowMapper.mapRow(resultSet, num++));
        }
        return list;
    }

    private void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    private void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public List<Object> executeQuery(String sql, Object[] values, RowMapper rowMapper) throws Exception {
        Connection connection;
        //创建连接
        if (transactionalManager == null){
            connection = dataSource.getConnection();
        }else {
            connection = transactionalManager.getConnection();
        }
        //创建语句集
        PreparedStatement preparedStatement = this.getPreparedStatement(connection, sql);
        //得到结果集
        ResultSet resultSet = this.getResultSet(preparedStatement, values);
        //解析结果集
        List<Object> objects = this.pareseResultSet(resultSet, rowMapper);
        //关闭结果集
        this.closeResultSet(resultSet);
        //关闭语句集
        this.closePreparedStatement(preparedStatement);
        //关闭连接
        this.closeConnection(connection);
        return objects;
    }

    public int update(String sql ) throws SQLException {
        //创建连接
        Connection connection;
        if (transactionalManager == null){
            connection = dataSource.getConnection();
        }else {
            connection = transactionalManager.getConnection();
        }
        //创建语句集
        PreparedStatement preparedStatement = this.getPreparedStatement(connection, sql);

        int count = invokeUpdate(preparedStatement);

        //关闭语句集
        this.closePreparedStatement(preparedStatement);

        return count;
    }


}
