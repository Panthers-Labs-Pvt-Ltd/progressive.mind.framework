package com.progressive.minds.chimera.core.datahubutils.emitter;

import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import datahub.client.Callback;
import datahub.shaded.io.netty.handler.codec.http.HttpResponse;

public class RestEmitterUtil {
    private static RestEmitter emitter;
/*
    public static Emitter getEmitter() {
        emitter = RestEmitter.create(b -> b
                .server("http://localhost:8080") // Replace with your DataHub server URL
                .token("YOUR_AUTH_TOKEN")        // Replace with your token if required
        );

        return emitter;
    }
     public static String SampleEmitter(MetadataChangeProposal mcpw, String dataProduct) throws IOException, ExecutionException, InterruptedException {
        // MetadataWriteResponse requestFuture = emitter.emit(mcpw, null).get();
         Emitter emitter = getEmitter();
// Non-blocking using callback
         final HttpResponse httpResponse = new HttpResponse();
        emitter.emit(mcpw, new Callback() {
            @Override
            public void onCompletion(MetadataWriteResponse response) {
                if (response.isSuccess()) {
                    System.out.println(String.format(dataProduct + "Successfully emitted metadata event for %s", mcpw.getEntityUrn()));
                } else {
                    // Get the underlying http response
                     httpResponse = (HttpResponse) response.getUnderlyingResponse();
                    System.out.println(String.format(dataProduct + "Failed to emit metadata event for %s, aspect: %s with status code: %d",
                            mcpw.getEntityUrn(), mcpw.getAspectName(), httpResponse.getStatus()));
                }
            }
            @Override
            public void onFailure(Throwable exception) {
                System.out.println(
                        String.format(dataProduct + "Failed to emit metadata event for %s, aspect: %s due to %s", mcpw.getEntityUrn(),
                                mcpw.getAspectName(), exception.getMessage()));
            }
        });
        return httpResponse.status().reasonPhrase().toString();
    }
    public static String emitProposal(MetadataChangeProposal proposal, String Type) {
        Emitter emitter = getEmitter();
        Future<MetadataWriteResponse> response = null;
        try {
            response = emitter.emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success")) {
                System.out.println("Domain created successfully!");
            } else {
                System.out.println(returnCode);
            }
            return returnCode;
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }*/
}

