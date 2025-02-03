import com.linkedin.common.url.Url;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.metadata.key.GlossaryTermKey;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import datahub.shaded.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.linkedin.events.metadata.ChangeType.CREATE;


public class ManageGlossaryKey {
    void createGlossaryTermKey() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:ManishWebWorld" );
        GlossaryTermKey gtk = new GlossaryTermKey();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(gtk);
        ByteString byteString1 = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));
        String DATAHUB_URL = "http://localhost:8080";
        String DATAHUB_AUTH_TOKEN = "";

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString1);  // Set the serialized aspect

        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType("glossaryTerm");
        proposal.setAspectName("glossaryTermKey");
        proposal.setAspect(genericAspect);
        proposal.setChangeType(CREATE);
        genericAspect.setContentType("application/json");
        Emitter emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)   );
        Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
        String returnCode = response.get().getResponseContent();
        if (returnCode.contains("success"))
        {System.out.println("Domain created successfully!");}
        else
        {System.out.println(returnCode);}

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setDefinition("Manish And Anuja Definitions")
                .setTermSource("EXTERNAL")
                .setSourceUrl(new Url("http://google.com/manish"))
                .setSourceRef("MANUAL");

        String jsonStringtermInfo = objectMapper.writeValueAsString(gtk);
        ByteString byteStringtermInfo = ByteString.unsafeWrap(jsonStringtermInfo.getBytes("UTF-8"));

        GenericAspect genericAspecttermInfo = new GenericAspect();
        genericAspect.setValue(byteStringtermInfo);  // Set the serialized aspect

        MetadataChangeProposal proposaltermInfo = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType("glossaryTerm");
        proposal.setAspectName("glossaryTermInfo");
        proposal.setAspect(genericAspecttermInfo);
        proposal.setChangeType(CREATE);
        genericAspect.setContentType("application/json");

        Future<MetadataWriteResponse> responsetermInfo = emitter.emit(proposaltermInfo, null);
        String returnCodetermInfo = responsetermInfo.get().getResponseContent();
        if (returnCodetermInfo.contains("success"))
        {System.out.println("glossaryTermInfo created successfully!" + returnCodetermInfo);}
        else
        {System.out.println(returnCodetermInfo );}


    }
}
