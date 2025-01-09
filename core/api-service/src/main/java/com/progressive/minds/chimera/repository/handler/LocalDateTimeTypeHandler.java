package com.progressive.minds.chimera.repository.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Example with LocalDateTime java8 Handler
 */
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    /**
     * @see TypeHandler#setParameter(PreparedStatement, int, Object, JdbcType)
     */
    @Override
    public void setParameter(PreparedStatement ps, int index, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        final LocalDateTime datetime = parameter;
        if (datetime != null) {
            ps.setTimestamp(index, Timestamp.valueOf(parameter));
        } else {
            ps.setTimestamp(index, null);
        }
    }

    /**
     * @see TypeHandler#getResult(ResultSet, String)
     */
    @Override
    public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(columnName);
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        } else {
            return null;
        }
    }

    /**
     * @see TypeHandler#getResult(ResultSet, int)
     */
    @Override
    public LocalDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(columnIndex);
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        } else {
            return null;
        }
    }

    /**
     * @see TypeHandler#getResult(CallableStatement, int)
     */
    @Override
    public LocalDateTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
        final Timestamp timestamp = cs.getTimestamp(columnIndex);
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        } else {
            return null;
        }
    }
}
