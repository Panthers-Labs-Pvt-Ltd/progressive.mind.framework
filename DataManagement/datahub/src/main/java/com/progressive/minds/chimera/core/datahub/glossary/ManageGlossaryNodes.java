package com.progressive.minds.chimera.core.datahub.glossary;

import com.linkedin.common.urn.GlossaryNodeUrn;
import com.linkedin.data.template.StringMap;
import com.linkedin.glossary.GlossaryNodeInfo;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.ownership.ManageOwners;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryNodeGroup;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.progressive.minds.chimera.core.datahub.Constants.GLOSSARY_NODE_ENTITY_NAME;
import static com.progressive.minds.chimera.core.datahub.Constants.GLOSSARY_NODE_INFO_ASPECT_NAME;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageGlossaryNodes {

    public String createGlossaryNode(List<GlossaryNodeGroup> inGlossaryNodes) throws IOException, ExecutionException, InterruptedException {
        // Loop through each GlossaryNodeGroup in the input list
        for (GlossaryNodeGroup nodeGroup : inGlossaryNodes) {
            // Create a new map for custom properties, or use an empty one
            StringMap MapCustomProperties = new StringMap();

            // Create a GlossaryNodeUrn based on the node group's name
            GlossaryNodeUrn glossaryNodeUrn = new GlossaryNodeUrn(nodeGroup.name);


            // Create a new GlossaryNodeInfo instance
            GlossaryNodeInfo glossaryNodeInfo = new GlossaryNodeInfo()
                    .setName(nodeGroup.name)
                    .setId(glossaryNodeUrn.toString())
                    .setDefinition(nodeGroup.definition);

            // If there is a parentGlossaryNodeGroup, handle it recursively
            if (nodeGroup.parentGlossaryNodeGroup != null && nodeGroup.parentGlossaryNodeGroup.name != null
                    && !nodeGroup.parentGlossaryNodeGroup.name.isEmpty()) {
                // Create a list with the parent node group for the recursive call
                glossaryNodeInfo.setParentNode(new GlossaryNodeUrn(nodeGroup.parentGlossaryNodeGroup.name));
                List<GlossaryNodeGroup> parentList = List.of(nodeGroup.parentGlossaryNodeGroup);
                createGlossaryNode(parentList);  // Recursive call with parent node
            }
            if (nodeGroup.parentGlossaryNodeGroup != null && nodeGroup.parentGlossaryNodeGroup.Ownership != null
                    && !nodeGroup.parentGlossaryNodeGroup.Ownership.isEmpty()
                    && (nodeGroup.Ownership == null || nodeGroup.Ownership.isEmpty())) {

                // Add parent ownership to the current (child) term's ownership
                ManageOwners.addOwners(glossaryNodeUrn, GLOSSARY_NODE_ENTITY_NAME, "ownership", "UPSERT",
                        nodeGroup.parentGlossaryNodeGroup.Ownership);
            }


            // If custom properties are provided, add them to the map
            if (nodeGroup.customProperties != null && !nodeGroup.customProperties.isEmpty()) {
                MapCustomProperties.putAll(nodeGroup.customProperties);
                glossaryNodeInfo.setCustomProperties(MapCustomProperties);
            }


            MetadataChangeProposal proposal = createProposal(String.valueOf(glossaryNodeUrn), GLOSSARY_NODE_ENTITY_NAME,
                    GLOSSARY_NODE_INFO_ASPECT_NAME, "UPSERT", glossaryNodeInfo);
                emitProposal(proposal, GLOSSARY_NODE_ENTITY_NAME);

            if (nodeGroup.Ownership != null && !nodeGroup.Ownership.isEmpty()) {
                ManageOwners.addOwners(glossaryNodeUrn, GLOSSARY_NODE_ENTITY_NAME, "ownership", "UPSERT",
                        nodeGroup.Ownership);
            }
        }
        // Return a message indicating how many glossary nodes were processed
        return inGlossaryNodes.size() + " glossary nodes processed.";
    }
}
