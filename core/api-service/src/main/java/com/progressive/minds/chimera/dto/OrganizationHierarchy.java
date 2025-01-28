package com.progressive.minds.chimera.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class OrganizationHierarchy {

  @NotBlank(message = "Organization Hierarchy Name cannot be blank")
  private String orgHierName;

  @NotBlank(message = "Organization Type Name cannot be blank")
  private String orgTypeName;

  private String parentOrgName;

  private String cooOwner;

  private String opsLead;

  private String techLead;

  private String busOwner;

  private String orgDesc;

  private String orgEmail;

  private String orgCi;

  private String userField1;

  private String userField2;

  private String userField3;

  private String userField4;

  private String userField5;

  private Timestamp createdTimestamp;

  private String createdBy;

  private Timestamp updatedTimestamp;

  private String updatedBy;

  private String activeFlag;
}
