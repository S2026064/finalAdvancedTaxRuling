/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;


import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "object_name"
})
@Generated("jsonschema2pojo")
public class Properties {

    @JsonProperty("property_name")
    private String property_name;
    
//    @JsonIgnore
//    private Map<String, Object> any = new LinkedHashMap<String, Object>();

    @JsonProperty("property_name")
    public String getPropertyName() {
        return property_name;
    }

    @JsonProperty("property_name")
    public void setPropertyName(String property_name) {
        this.property_name = property_name;
    }
     

//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.any;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.any.put(name, value);
//    }

    
}
