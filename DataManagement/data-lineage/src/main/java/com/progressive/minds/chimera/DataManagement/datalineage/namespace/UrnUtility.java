package com.progressive.minds.chimera.DataManagement.datalineage.namespace;
import com.linkedin.common.FabricType;
import com.linkedin.common.urn.*;

public class UrnUtility {

    public static String createUrn(String entityType, String... args) {
        try {
            switch (entityType.toLowerCase()) {
                case "dataset":
                    DataPlatformUrn DataPlatform = DataPlatformUrn.createFromString(args[0]);
                    String DatasetName = args[1];
                    FabricType origin =  FabricType.valueOf(args[2]);
                    return new DatasetUrn(DataPlatform, DatasetName, origin).toString();
                case "corpuser":
                    return CorpuserUrn.createFromString("urn:li:corpuser:" + args[0]).toString();
                case "pipeline":
                    String orchestrator = args[0];
                    String flowId = args[1];
                    String cluster = args[2];
                    String dataflowUrn= String.format("urn:li:dataFlow:(%s,%s,%s)", orchestrator, flowId, cluster);
                    return DataFlowUrn.createFromString(dataflowUrn).toString();
                case "datajob":
                    DataFlowUrn flow = DataFlowUrn.createFromString(args[0]);
                    String jobId = args[1];
                    String dataJob= String.format("urn:li:dataJob:(%s,%s)", flow, jobId);
                    return new DataJobUrn(flow, jobId).toString();
                case "datajoburn":
                    return DataJobUrn.createFromString(args[0]).toString(); // full URN string
                case "tag":
                    return TagUrn.createFromString(args[0]).toString(); // full URN string
                case "glossaryterm":
                    return GlossaryTermUrn.createFromString(args[0]).toString(); // full URN string
                default:
                    throw new IllegalArgumentException("Unsupported URN type: " + entityType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create URN for type " + entityType + " with args " + String.join(", ", args), e);
        }
    }
}
