package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class ContractCustomerDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source Table: test.contract_customer")
    public static final ContractCustomer contractCustomer = new ContractCustomer();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.id")
    public static final SqlColumn<Integer> id = contractCustomer.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.contract_id")
    public static final SqlColumn<Integer> contractId = contractCustomer.contractId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.name")
    public static final SqlColumn<String> name = contractCustomer.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.email")
    public static final SqlColumn<String> email = contractCustomer.email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.created_at")
    public static final SqlColumn<Date> createdAt = contractCustomer.createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.customer_id")
    public static final SqlColumn<Integer> customerId = contractCustomer.customerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.contract_start_date")
    public static final SqlColumn<Date> contractStartDate = contractCustomer.contractStartDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.contract_end_date")
    public static final SqlColumn<Date> contractEndDate = contractCustomer.contractEndDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source field: test.contract_customer.contract_terms")
    public static final SqlColumn<String> contractTerms = contractCustomer.contractTerms;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0834988+05:30", comments="Source Table: test.contract_customer")
    public static final class ContractCustomer extends AliasableSqlTable<ContractCustomer> {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<Integer> contractId = column("contract_id", JDBCType.INTEGER);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);

        public final SqlColumn<Date> createdAt = column("created_at", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> customerId = column("customer_id", JDBCType.INTEGER);

        public final SqlColumn<Date> contractStartDate = column("contract_start_date", JDBCType.DATE);

        public final SqlColumn<Date> contractEndDate = column("contract_end_date", JDBCType.DATE);

        public final SqlColumn<String> contractTerms = column("contract_terms", JDBCType.VARCHAR);

        public ContractCustomer() {
            super("\"test\".\"contract_customer\"", ContractCustomer::new);
        }
    }
}