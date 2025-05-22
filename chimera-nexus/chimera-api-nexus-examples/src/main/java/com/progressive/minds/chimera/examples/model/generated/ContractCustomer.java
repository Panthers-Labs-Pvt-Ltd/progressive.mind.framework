package com.progressive.minds.chimera.examples.model.generated;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;

@Data
public class ContractCustomer {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0818145+05:30", comments="Source field: test.contract_customer.id")
    private Integer id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0818145+05:30", comments="Source field: test.contract_customer.contract_id")
    private Integer contractId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.email")
    private String email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.created_at")
    private Date createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.customer_id")
    private Integer customerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.contract_start_date")
    private Date contractStartDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.contract_end_date")
    private Date contractEndDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0824893+05:30", comments="Source field: test.contract_customer.contract_terms")
    private String contractTerms;
}