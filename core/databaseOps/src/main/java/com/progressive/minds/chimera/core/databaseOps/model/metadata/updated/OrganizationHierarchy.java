package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.Date;
import java.util.UUID;

public class OrganizationHierarchy {

    private UUID orgId;
    private UUID parentOrgId;
    private UUID orgTypeId;
    private String orgName;
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
    private String status;
    private String insertedUser;
    private Date insertDt;
    private String modifiedBy;
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
