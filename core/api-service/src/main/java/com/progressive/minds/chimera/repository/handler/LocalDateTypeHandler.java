package com.progressive.minds.chimera.repository.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.LocalDate;

@MappedTypes(LocalDate.class)
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    /**
     * @see TypeHandler#setParameter(PreparedStatement, int, Object, JdbcType)
     */
    @Override
    public void setParameter(final PreparedStatement ps, final int index, final LocalDate parameter, final JdbcType jdbcType) throws SQLException {
        final LocalDate date = parameter;
        if (date != null) {
            ps.setDate(index, Date.valueOf(parameter));
        } else {
            ps.setDate(index, null);
        }
    }

    /**
     * @see TypeHandler#getResult(ResultSet, String)
     */
    @Override
    public LocalDate getResult(final ResultSet rs, final String columnName) throws SQLException {
        final Date date = rs.getDate(columnName);
        if (date != null) {
            return date.toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * @see TypeHandler#getResult(ResultSet, int)
     */
    @Override
    public LocalDate getResult(final ResultSet rs, final int columnIndex) throws SQLException {
        final Date date = rs.getDate(columnIndex);
        if (date != null) {
            return date.toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * @see TypeHandler#getResult(CallableStatement, int)
     */
    @Override
    public LocalDate getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        final Date date = cs.getDate(columnIndex);
        if (date != null) {
            return date.toLocalDate();
        } else {
            return null;
        }
    }
}
