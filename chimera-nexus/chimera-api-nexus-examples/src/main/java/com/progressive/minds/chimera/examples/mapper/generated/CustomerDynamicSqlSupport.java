package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class CustomerDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source Table: test.customer")
    public static final Customer customer = new Customer();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source field: test.customer.id")
    public static final SqlColumn<Integer> id = customer.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source field: test.customer.name")
    public static final SqlColumn<String> name = customer.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source field: test.customer.email")
    public static final SqlColumn<String> email = customer.email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source field: test.customer.created_at")
    public static final SqlColumn<Date> createdAt = customer.createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8413346+05:30", comments="Source Table: test.customer")
    public static final class Customer extends AliasableSqlTable<Customer> {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);

        public final SqlColumn<Date> createdAt = column("created_at", JDBCType.TIMESTAMP);

        public Customer() {
            super("\"test\".\"customer\"", Customer::new);
        }
    }
}