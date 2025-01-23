package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.domain.Domains;
import com.linkedin.mxe.MetadataChangeProposal;
import org.junit.jupiter.api.Test;

import java.time.Instant;

//import static com.progressive.minds.chimera.core.datahub.referances.DataHubUtils.SYSTEM_USER;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

class ManageGlobalTagsTest {
    @Test
    void TagTest() throws Exception {

        String[] tagNames = {"Verified","Unauth"};
       String retval= ManageGlobalTags.addTags(Urn.createFromString("urn:li:dataProduct:manishdataproduct11"),
                "dataProduct", "UPSERT", tagNames);
        System.out.println(retval.toString());
    }

    @Test
    void addDomainToAsstes() throws Exception {
        String dataProductUrnString = "urn:li:dataProduct:manishdataproduct11";
        String domainUrnString = "urn:li:domain:manishwebworld";

        UrnArray UA = new UrnArray();
        UA.add(Urn.createFromString(domainUrnString));

        Domains domains = new Domains().setDomains(UA);

            MetadataChangeProposal proposal = createProposal(dataProductUrnString,
                "dataProduct", "domains","UPSERT", domains);

        String retval =  emitProposal(proposal, "domains");
        System.out.println(retval.toString());
    }

    @Test
    void addGlosseryToAsstes() throws Exception {

        String assetUrnString = "urn:li:dataProduct:manishdataproduct11";
        String glossaryTermUrnString = "urn:li:glossaryTerm:SavingAccount";

        Urn assetUrn = Urn.createFromString(assetUrnString);
        GlossaryTermUrn glossaryTermUrn = GlossaryTermUrn.createFromString(glossaryTermUrnString);

        GlossaryTermAssociation termAssociation = new GlossaryTermAssociation()
                .setUrn(glossaryTermUrn);

        GlossaryTermAssociationArray GTAA = new GlossaryTermAssociationArray();
        GTAA.add(termAssociation);

        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn("System"))
                .setTime(Instant.now().toEpochMilli());

        GlossaryTerms glossaryTerms = new GlossaryTerms()
                .setTerms(GTAA).setAuditStamp(createdStamp);


        MetadataChangeProposal proposal = createProposal(assetUrnString,
                "dataProduct", "glossaryTerms","UPSERT", glossaryTerms);

        String retval =  emitProposal(proposal, "glossaryTerms");
        System.out.println(retval.toString());
    }
}