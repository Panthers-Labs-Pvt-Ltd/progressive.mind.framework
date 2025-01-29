import com.linkedin.common.AuditStamp;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.DataPlatformUrn;
import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.common.urn.UrnUtils;
import com.linkedin.data.schema.Name;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.template.StringArray;
import com.linkedin.data.template.StringMap;
import com.linkedin.dataset.DatasetProperties;
import com.linkedin.schema.*;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import datahub.event.MetadataChangeProposalWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.linkedin.data.schema.RecordDataSchema.RecordType;
import static java.time.LocalTime.now;

public class DatasetAdd {

    public DatasetAdd() {}

    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException, URISyntaxException {
        DatasetUrn datasetUrn = UrnUtils.toDatasetUrn("Snowflake", "sampledatasets", "PROD");
        CorpuserUrn userUrn = new CorpuserUrn("ingestion");
        AuditStamp lastModified = new AuditStamp().setTime(1640692800000L).setActor(userUrn);
        String rawSchema = "{ \"type\": \"record\", \"name\": \"Example\", \"fields\": [{\"name\": \"field1\", \"type\": \"string\"}]}";

        SchemaMetadata schemaMetadata =
                new SchemaMetadata()
                        .setSchemaName("custssodmer")
                        .setPlatform(new DataPlatformUrn("Snowflake"))
                        .setCluster("myClusterName")
                        .setVersion(0L)
                        .setHash("")
                        .setPlatformSchema(
                                SchemaMetadata.PlatformSchema.create(
                                        new OtherSchema().setRawSchema(rawSchema)))
                        .setLastModified(lastModified)
                        .setDataset(datasetUrn);


        Name nm = new Name("myVar_1.some_var2.moreVars_3");
        //

      // RecordType  rt = new DataSchema.Type().name().

        StringMap sm = new StringMap();
        sm.put("key1","val1");
        sm.put("key2","val2");
        sm.put("key3","val3");

        StringArray sa = new StringArray();
        sa.add("tag1");
        sa.add("tag3");


        SchemaFieldArray fields = new SchemaFieldArray();

        SchemaField field1 =
                new SchemaField()
                        .setFieldPath("address.zipcode")
                        .setType(
                                new SchemaFieldDataType()
                                        .setType(SchemaFieldDataType.Type.create(new StringType())))
                        .setNativeDataType("VARCHAR(50)")
                        .setDescription(
                                "This is the zipcode of the address. Specified using extended form and limited to addresses in the United States")
                        .setLastModified(lastModified);
        fields.add(field1);

        SchemaField field2 =
                new SchemaField()
                        .setFieldPath("address.street")
                        .setType(
                                new SchemaFieldDataType()
                                        .setType(SchemaFieldDataType.Type.create(new StringType())))
                        .setNativeDataType("VARCHAR(100)")
                        .setDescription("Street corresponding to the address")
                        .setLastModified(lastModified);
        fields.add(field2);

        SchemaField field3 =
                new SchemaField()
                        .setFieldPath("last_sold_date")
                        .setType(
                                new SchemaFieldDataType().setType(SchemaFieldDataType.Type.create(new DateType())))
                        .setNativeDataType("Date")
                        .setDescription("Date of the last sale date for this property")
                        .setLastModified(lastModified);
        fields.add(field3);

        schemaMetadata.setFields(fields);


        RecordDataSchema rs = new RecordDataSchema(nm, RecordType.RECORD);
       rs.setDoc("Schema DOC");
        //rs.setFields("");


        DatasetProperties ds = new DatasetProperties()
                .setDescription("DatasetProperties description")
                .setCustomProperties(sm)
                .setExternalUrl(new Url("www.yahoo.com"))
                .setName("Sample Datasets")
                .setQualifiedName("Set DatasetProperties Qualified Name")
                .setTags(sa)
                .setUri(new URI(""))
                ;

System.out.println("===="+ ds);
        MetadataChangeProposalWrapper mcpw =
                MetadataChangeProposalWrapper.builder()
                        .entityType("dataset")
                        .entityUrn(datasetUrn)
                        .upsert()
                        .aspect(ds)
                        .build();
         String DATAHUB_AUTH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhY3RvclR5cGUiOiJVU0VSIiwiYWN0b3JJZCI6ImRhdGFodWIiLCJ0eXBlIjoiUEVSU09OQUwiLCJ2ZXJzaW9uIjoiMiIsImp0aSI6IjY5ZDRlY2QyLTM2NDItNDk3YS05OTIwLThiZjQyM2I3Yjg3OSIsInN1YiI6ImRhdGFodWIiLCJleHAiOjE3Mzk4OTE4NzgsImlzcyI6ImRhdGFodWItbWV0YWRhdGEtc2VydmljZSJ9.lWCSLgZ096DdlI6lsqTgicd-uKYi4RNf1pOHaCCM-WI";

        String token = DATAHUB_AUTH_TOKEN;
        RestEmitter emitter = RestEmitter.create(b -> b.server("http://localhost:8080").token(token));
        Future<MetadataWriteResponse> response = emitter.emit(mcpw, null);
        System.out.println(response.get().getResponseContent());
    }
}
