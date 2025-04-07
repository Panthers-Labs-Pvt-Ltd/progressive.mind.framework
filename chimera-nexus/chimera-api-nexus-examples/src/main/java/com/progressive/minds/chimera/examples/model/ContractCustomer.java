package com.progressive.minds.chimera.examples.model;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractCustomer extends Customer{
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9124805+05:30", comments="Source field: test.contract_customer.contract_id")
    private Integer contractId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9130385+05:30", comments="Source field: test.contract_customer.customer_id")
    private Integer customerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9130385+05:30", comments="Source field: test.contract_customer.contract_start_date")
    private Date contractStartDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9130385+05:30", comments="Source field: test.contract_customer.contract_end_date")
    private Date contractEndDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9135535+05:30", comments="Source field: test.contract_customer.contract_terms")
    private String contractTerms;
}