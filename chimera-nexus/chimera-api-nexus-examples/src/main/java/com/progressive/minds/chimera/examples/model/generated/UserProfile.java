package com.progressive.minds.chimera.examples.model.generated;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;

@Data
public class UserProfile {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0496839+05:30", comments="Source field: test.USER_PROFILE.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0532108+05:30", comments="Source field: test.USER_PROFILE.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0532108+05:30", comments="Source field: test.USER_PROFILE.created_at")
    private Date createdAt;
}