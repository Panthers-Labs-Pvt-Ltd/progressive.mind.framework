package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.Date;
import java.util.UUID;

public class OrganizationTypes {

    private UUID orgTypeId;
    private String orgTypeName;
    private String orgTypeDesc;
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

    public UUID getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(UUID orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public String getOrgTypeDesc() {
        return orgTypeDesc;
    }

    public void setOrgTypeDesc(String orgTypeDesc) {
        this.orgTypeDesc = orgTypeDesc;
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
        return "OrganizationTypes{" +
                "orgTypeId=" + orgTypeId +
                ", orgTypeName='" + orgTypeName + '\'' +
                ", orgTypeDesc='" + orgTypeDesc + '\'' +
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
