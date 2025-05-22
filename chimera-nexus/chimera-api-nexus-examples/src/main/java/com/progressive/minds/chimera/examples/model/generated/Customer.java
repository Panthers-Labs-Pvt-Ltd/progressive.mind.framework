package com.progressive.minds.chimera.examples.model.generated;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;

@Data
public class Customer {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8403347+05:30", comments="Source field: test.customer.id")
    private Integer id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8403347+05:30", comments="Source field: test.customer.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8403347+05:30", comments="Source field: test.customer.email")
    private String email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8403347+05:30", comments="Source field: test.customer.created_at")
    private Date createdAt;
}