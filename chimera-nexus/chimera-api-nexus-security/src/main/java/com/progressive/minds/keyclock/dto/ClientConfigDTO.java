package com.progressive.minds.keyclock.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ClientConfigDTO {
  private String clientId;
  private boolean enabled = true;
  private boolean publicClient;
  private boolean standardFlowEnabled;
  private boolean directAccessGrantsEnabled;
  private List<ProtocolMapperDTO> protocolMappers = new ArrayList<>();
  private Map<String, String> attributes = new HashMap<>();
  private List<String> webOrigins = new ArrayList<>();
  private List<String> redirectUris = new ArrayList<>();
}
