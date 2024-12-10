package com.progressive.minds.chimera.core.orchestrator.activity.impl;

import com.progressive.minds.chimera.core.orchestrator.activity.DecisionServiceActivity;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DecisionServiceActivityImpl implements DecisionServiceActivity {

  @Override
  public String execute(String dataSource) {
    try {
      // Make REST call to decision engine
      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7001/data/decision")) // Add http://
                  .POST(HttpRequest.BodyPublishers.ofString(dataSource))
                  .build(),
              HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (Exception e) {
      throw new RuntimeException("Failed to access decision engine", e);
    }
  }
}
