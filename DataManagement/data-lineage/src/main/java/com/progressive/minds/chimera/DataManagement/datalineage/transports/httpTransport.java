package com.progressive.minds.chimera.DataManagement.datalineage.transports;

import com.progressive.minds.chimera.DataManagement.datalineage.SharedLogger;
import io.openlineage.client.OpenLineageClient;

import io.openlineage.client.transports.ApiKeyTokenProvider;
import io.openlineage.client.transports.HttpConfig;
import io.openlineage.client.transports.HttpTransport;

import javax.annotation.Nullable;
import java.lang.constant.Constable;
import java.net.URI;
import java.util.Map;

/**
 * Allows sending events to HTTP endpoint, using ApacheHTTPClient.
 * Configuration-
 * type - string, must be "http". Required.
 * url - string, base url for HTTP requests. Required.
 * endpoint - string specifying the endpoint to which events are sent, appended to url. Optional, default: /api/v1/lineage.
 * urlParams - dictionary specifying query parameters send in HTTP requests. Optional.
 * timeoutInMillis - integer specifying timeout (in milliseconds) value used while connecting to server.Optional, default: 5000.
 * auth - dictionary specifying authentication options. Optional, by default no authorization is used. If set, requires the type property.
 *      type - string specifying the "api_key" or the fully qualified class name of your TokenProvider. Required if auth is provided.
 *      apiKey - string setting the Authentication HTTP header as the Bearer. Required if type is api_key.
 * headers - dictionary specifying HTTP request headers. Optional.
 * compression - string, name of algorithm used by HTTP client to compress request body. Optional, default value null, allowed values: gzip. Added in v1.13.0.
 */
public class httpTransport implements OpenLineageTransportTypes.HTTPAsTransport, SharedLogger {
    String LoggerTag = "[Open Lineage] - HTTPAsTransport";
    public OpenLineageClient set(URI url, @Nullable String endpoint,
                                 @Nullable Integer timeoutInMillis, @Nullable  String APIKey,
                                 @Nullable  Map<String, String> urlParams, @Nullable
                                 Map<String, String> headers, @Nullable HttpConfig.Compression compression)
    {
        LineageLogger.logInfo( "Setting HTTP As Open Lineage Transport Type");

      /*  Map<String, String> queryParams = Map.of(
                "param0", "value0",
                "param1", "value1"
        );

        Map<String, String> headers = Map.of(
                "X-Some-Extra-Header", "abc"
        );

*/
        ApiKeyTokenProvider apiKeyTokenProvider = new ApiKeyTokenProvider();
        apiKeyTokenProvider.setApiKey(APIKey);

        String endpointValue = (endpoint != null) ? endpoint : "/api/v1/lineage";
        int timeoutInMillisValue = (timeoutInMillis != null) ? timeoutInMillis : 5000;
        Constable compressionValue = (compression != null) ? compression : "HttpConfig.Compression.GZIP";

        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setUrl(url);
        httpConfig.setEndpoint(endpointValue );
        httpConfig.setUrlParams(urlParams);
        httpConfig.setAuth(apiKeyTokenProvider);
        httpConfig.setTimeoutInMillis(timeoutInMillisValue);
        httpConfig.setHeaders(headers);
        httpConfig.setCompression((HttpConfig.Compression) compressionValue);

        OpenLineageClient client = OpenLineageClient.builder()
                .transport(
                        new HttpTransport(httpConfig))
                .build();

        return client;
    }
}
