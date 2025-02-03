package com.progressive.minds.chimera.core.datahub.glossary;

import com.progressive.minds.chimera.core.datahub.modal.GlossaryNodeGroup;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

class ManageGlossaryNodesTest {

    @Test
    public void createGlossaryNodeTest() {
        List<GlossaryNodeGroup> inGlossaryNodes = new ArrayList<>();

        // Initialize customProperties map (can be null, depending on your use case)
        Map<String, String> customProperties = new HashMap<>();
        customProperties.put("customKey1", "customValue1");

        // Create a new GlossaryNodeGroup instance
        GlossaryNodeGroup glossaryNodeGroup = new GlossaryNodeGroup(
                "Glossary Name",              // name
                "Definition of the term",     // definition
                null,                          // parentTermName
                null,                          // sourceURL
                customProperties              // customProperties
        );

        // Add the created GlossaryNodeGroup object to the list
        inGlossaryNodes.add(glossaryNodeGroup);

        // Optional: Print or log the list to verify
        System.out.println(inGlossaryNodes.size() + " glossary node(s) created.");
    }

    @Test public void recursiveTest() throws IOException, ExecutionException, InterruptedException, URISyntaxException {
        Map<String, String> customProperties1 = new HashMap<>();
        customProperties1.put("customKey1", "customValue1");

        Map<String, String> customProperties2 = new HashMap<>();
        customProperties2.put("customKey2", "customValue2");

        List<Owners> owners = new ArrayList<>();

        Owners owner1 = new Owners();
        owner1.setName("Alice");
        owner1.setType("CUSTOM");
        owners.add(owner1);

        Owners owner2 = new Owners();
        owner2.setName("Alice");
        owner2.setType("CUSTOM");
        owners.add(owner2);

        Owners owner3 = new Owners();
        owner3.setName("Alice");
        owner3.setType("CUSTOM");
        owners.add(owner3);

        GlossaryNodeGroup parentNode = new GlossaryNodeGroup();
        parentNode.name = "Parent Term";
        parentNode.definition = "Definition of Parent Term";
        parentNode.Ownership = owners;
        parentNode.customProperties = customProperties2;

        GlossaryNodeGroup node1 = new GlossaryNodeGroup();
        node1.name = "Term 1";
        node1.definition = "Definition of Term 1";
        node1.customProperties = customProperties1;
        node1.parentGlossaryNodeGroup = parentNode;
        node1.Ownership = owners;

        GlossaryNodeGroup node2 = new GlossaryNodeGroup();
        node2.name = "Term 2";
        node2.definition = "Definition of Term 2";
        node2.customProperties = customProperties2;
        node2.parentGlossaryNodeGroup = parentNode;

        // Create the ManageGlossaryNodes instance and process the nodes
        ManageGlossaryNodes manageGlossaryNodes = new ManageGlossaryNodes();
        List<GlossaryNodeGroup> nodes = List.of(node1, node2);
        String result = manageGlossaryNodes.createGlossaryNode(nodes);

        // Print the result
        System.out.println(result);
    }
}