package com.progressive.minds.chimera.core.api_service.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class Log4jMDCFilter extends OncePerRequestFilter {

  private static final String CORRELATION_ID_HEADER = "X-CORRELATION-ID";
  private static final String MDC_CORRELATION_ID_KEY = "CORRELATION_ID";

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Retrieve the correlation ID from the header
      String correlationId = request.getHeader(CORRELATION_ID_HEADER);

      // If the header is not present or empty, generate a new UUID
      if (correlationId == null || correlationId.trim().isEmpty()) {
        correlationId = UUID.randomUUID().toString();
      }

      // Add the correlation ID to MDC
      MDC.put(MDC_CORRELATION_ID_KEY, correlationId);

      // Add the correlation ID to the response header
      response.setHeader(CORRELATION_ID_HEADER, correlationId);

      // Proceed with the filter chain
      filterChain.doFilter(request, response);
    } finally {
      // Ensure MDC is cleared after the request is processed
      MDC.remove(MDC_CORRELATION_ID_KEY);
    }
  }
}


