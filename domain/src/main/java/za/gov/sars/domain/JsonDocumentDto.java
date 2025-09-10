/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import za.gov.sars.common.Properties;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author S2026064
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "objectType",
    "objectName",
    "contentType",
    "author",
    "properties",
    "content",
    "fileSize"})
@Generated("jsonschema2pojo")
public class JsonDocumentDto {

    @JsonProperty("objectType")
    private String objectType;
    @JsonProperty("objectName")
    private String objectName;
    @JsonProperty("contentType")
    private String contentType;
    @JsonProperty("author")
    private String author;
    @JsonProperty("properties")
    private List<Properties> properties = new ArrayList<>();
    @JsonProperty("fileSize")
    private Integer fileSize;
    @JsonProperty("content")
    private String content;
    private Date uploadedDate;

    @JsonProperty("properties")
    public List<Properties> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

    @JsonProperty(value = "contentType")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("contentType")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("objectName")
    public String getObjectName() {
        return objectName;
    }

    @JsonProperty("objectName")
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("objectType")
    public String getObjectType() {
        return objectType;
    }

    @JsonProperty("objectType")
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
     
}
