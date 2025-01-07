package com.progressive.minds.chimera.core.datalineage.facets;

import io.openlineage.flink.shaded.com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.openlineage.flink.shaded.com.fasterxml.jackson.annotation.JsonAnySetter;
import io.openlineage.flink.shaded.com.fasterxml.jackson.annotation.JsonCreator;
import io.openlineage.flink.shaded.com.fasterxml.jackson.annotation.JsonProperty;
import io.openlineage.flink.shaded.com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.openlineage.flink.shaded.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import io.openlineage.client.OpenLineage;


public  final class CustomFacet {

@JsonDeserialize(
        as = FileJobFacet.class
)
@JsonPropertyOrder({"_producer", "_schemaURL", "_deleted", "filename"})
public static final class FileJobFacet implements OpenLineage.JobFacet {
    private final URI _producer;
    private final URI _schemaURL;
    private final Boolean _deleted;

    private final String fileName;
    private final String fileType;
    private final String fileExtension;
    private final String fileDelimiter;
    private final String fileQualifier;
    private final String fileSize;
    private final String compressionType;


    @JsonAnySetter
    private final Map<String, Object> additionalProperties;

    @JsonCreator
    public FileJobFacet(@JsonProperty("_producer") URI _producer, @JsonProperty("filename") String fileName,
                        @JsonProperty("filetype") String fileType, @JsonProperty("extension") String fileExtension,
                        @JsonProperty("delimiter")  String fileDelimiter,
                        @JsonProperty("qualifier") String fileQualifier,  @JsonProperty("size")  String fileSize,
                        @JsonProperty("compression") String compressionType) {

        this._schemaURL = URI.create("https://openlineage.io/spec/facets/2.0.5/FileJobFacet.json#/$defs/FileJobFacet");
        this._deleted = null;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileDelimiter = fileDelimiter;
        this._producer = _producer;
        this.fileType = fileType;
        this.fileQualifier = fileQualifier;
        this.fileSize = fileSize;
        this.compressionType = compressionType;
        this.additionalProperties = new LinkedHashMap<>();
    }


    public URI get_producer() {
        return this._producer;
    }

    public URI get_schemaURL() {
        return this._schemaURL;
    }

    public Boolean get_deleted() {
        return this._deleted;
    }

    public String getFileName() {
        return this.fileName;
    }
    public String getFileType() {
        return this.fileType;
    }
    public String getFileExtension() {
        return this.fileExtension;
    }
    public String getFileDelimiter() {
        return this.fileDelimiter;
    }
    public String getFileQualifier() {
        return this.fileQualifier;
    }
    public String getFileSize() {
        return this.fileSize;
    }
    public String getCompressionType() {
        return this.compressionType;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    void withAdditionalProperties() {
    }
}
}