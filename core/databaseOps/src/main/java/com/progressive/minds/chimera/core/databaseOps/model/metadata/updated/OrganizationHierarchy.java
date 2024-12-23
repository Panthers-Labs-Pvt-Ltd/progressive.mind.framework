package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import com.progressive.minds.chimera.core.databaseOps.annotation.Column;
import com.progressive.minds.chimera.core.databaseOps.annotation.Id;
import com.progressive.minds.chimera.core.databaseOps.annotation.Table;

import java.util.Date;
import java.util.UUID;

@Table(name = "organization_hierarchy")
public class OrganizationHierarchy {

    @Id
    @Column(name = "org_id")
    private UUID orgId;

    @Column(name ="parent_org_id")
    private UUID parentOrgId;

    @Column(name ="org_type_id")
    private UUID orgTypeId;

    @Column(name ="org_name")
    private String orgName;

    @Column(name ="coo_owner")
    private String cooOwner;

    @Column(name ="ops_lead")
    private String opsLead;

    @Column(name ="tech_lead")
    private String techLead;

    @Column(name ="bus_owner")
    private String busOwner;

    @Column(name ="org_desc")
    private String orgDesc;

    @Column(name ="org_email")
    private String orgEmail;

    @Column(name ="org_ci")
    private String orgCi;

    @Column(name ="user_field_1")
    private String userField1;

    @Column(name ="user_field_2")
    private String userField2;

    @Column(name ="user_field_3")
    private String userField3;

    @Column(name ="user_field_4")
    private String userField4;

    @Column(name ="user_field_5")
    private String userField5;

    @Column(name ="status")
    private String status;

    @Column(name ="inserted_user")
    private String insertedUser;

    @Column(name ="insert_dt")
    private Date insertDt;

    @Column(name ="modified_by")
    private String modifiedBy;

    @Column(name ="update_dt")
    private Date updateDt;

    // Getters and Setters

    public UUID getOrgId() {
        return orgId;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public UUID getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(UUID parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public UUID getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(UUID orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCooOwner() {
        return cooOwner;
    }

    public void setCooOwner(String cooOwner) {
        this.cooOwner = cooOwner;
    }

    public String getOpsLead() {
        return opsLead;
    }

    public void setOpsLead(String opsLead) {
        this.opsLead = opsLead;
    }

    public String getTechLead() {
        return techLead;
    }

    public void setTechLead(String techLead) {
        this.techLead = techLead;
    }

    public String getBusOwner() {
        return busOwner;
    }

    public void setBusOwner(String busOwner) {
        this.busOwner = busOwner;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgCi() {
        return orgCi;
    }

    public void setOrgCi(String orgCi) {
        this.orgCi = orgCi;
    }

    public String getUserField1() {
        return userField1;
    }

    public void setUserField1(String userField1) {
        this.userField1 = userField1;
    }

    public String getUserField2() {
        return userField2;
    }

    public void setUserField2(String userField2) {
        this.userField2 = userField2;
    }

    public String getUserField3() {
        return userField3;
    }

    public void setUserField3(String userField3) {
        this.userField3 = userField3;
    }

    public String getUserField4() {
        return userField4;
    }

    public void setUserField4(String userField4) {
        this.userField4 = userField4;
    }

    public String getUserField5() {
        return userField5;
    }

    public void setUserField5(String userField5) {
        this.userField5 = userField5;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsertedUser() {
        return insertedUser;
    }

    public void setInsertedUser(String insertedUser) {
        this.insertedUser = insertedUser;
    }

    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public String toString() {
        return "OrganizationHierarchy{" +
                "orgId=" + orgId +
                ", parentOrgId=" + parentOrgId +
                ", orgTypeId=" + orgTypeId +
                ", orgName='" + orgName + '\'' +
                ", cooOwner='" + cooOwner + '\'' +
                ", opsLead='" + opsLead + '\'' +
                ", techLead='" + techLead + '\'' +
                ", busOwner='" + busOwner + '\'' +
                ", orgDesc='" + orgDesc + '\'' +
                ", orgEmail='" + orgEmail + '\'' +
                ", orgCi='" + orgCi + '\'' +
                ", userField1='" + userField1 + '\'' +
                ", userField2='" + userField2 + '\'' +
                ", userField3='" + userField3 + '\'' +
                ", userField4='" + userField4 + '\'' +
                ", userField5='" + userField5 + '\'' +
                ", status='" + status + '\'' +
                ", insertedUser='" + insertedUser + '\'' +
                ", insertDt=" + insertDt +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", updateDt=" + updateDt +
                '}';
    }
}
