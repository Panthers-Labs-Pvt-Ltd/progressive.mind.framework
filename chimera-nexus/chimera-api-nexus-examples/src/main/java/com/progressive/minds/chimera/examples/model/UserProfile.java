package com.progressive.minds.chimera.examples.model;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;

@Data
public class UserProfile {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.8887778+05:30", comments="Source field: test.USER_PROFILE.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.8903231+05:30", comments="Source field: test.USER_PROFILE.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.8903231+05:30", comments="Source field: test.USER_PROFILE.created_at")
    private Date createdAt;
}