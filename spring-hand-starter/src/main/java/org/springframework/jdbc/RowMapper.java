package org.springframework.jdbc;

import java.sql.ResultSet;

/**
 * 处理返回结果接口
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws Exception;
}

