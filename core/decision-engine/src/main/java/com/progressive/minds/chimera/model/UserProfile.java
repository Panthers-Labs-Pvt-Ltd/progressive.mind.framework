package com.progressive.minds.chimera.model;

import jakarta.annotation.Generated;
import java.util.Date;
import lombok.Data;

@Data
public class UserProfile {
  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-30T17:51:03.5957719+05:30",
      comments = "Source field: test.USER_PROFILE.id")
  private Long id;

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-30T17:51:03.5967702+05:30",
      comments = "Source field: test.USER_PROFILE.name")
  private String name;

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-30T17:51:03.5967702+05:30",
      comments = "Source field: test.USER_PROFILE.created_at")
  private Date createdAt;
}
