package com.progressive.minds.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mybatis.starter")
public class MyBatisStarterProperties {
  private boolean generatorEnabled = false;
}
