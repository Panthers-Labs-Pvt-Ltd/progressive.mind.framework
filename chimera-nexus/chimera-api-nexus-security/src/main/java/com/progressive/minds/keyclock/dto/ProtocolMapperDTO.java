package com.progressive.minds.keyclock.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProtocolMapperDTO {
  private String name;
  private String protocol;
  private String protocolMapper;
  private Map<String, String> config = new HashMap<>();
}
