package com.progressive.minds.chimera.core.databaseOps.modal

import java.math.BigInteger
import java.util.Date

class OrganizationHierarchy (
      private var _orgId : BigInteger,
      private var _parentOrgId : String,
      private var _orgTypeId : BigInteger,
      private var _orgName : String,
      private var _cooOwner : String,
      private var _opsLead : String,
      private var _techLead : String,
      private var _busOwner : String,
      private var _orgDesc : String,
      private var _orgEmail : String,
      private var _orgCi : String,
      private var _userField1 : String,
      private var _userField2 : String,
      private var _userField3 : String,
      private var _userField4 : String,
      private var _userField5 : String,
      private var _status : String,
      private var _insertedUser : String,
      private var _insertDt : Date,
      private var _modifiedBy : String,
      private var _updateDt : Date
                            )
{
  // Getters
  def orgId : BigInteger  =  _orgId
  def parentOrgId : String  =  _parentOrgId
  def orgTypeId : BigInteger  =  _orgTypeId
  def orgName : String  =  _orgName
  def cooOwner : String  =  _cooOwner
  def opsLead : String  =  _opsLead
  def techLead : String  =  _techLead
  def busOwner : String  =  _busOwner
  def orgDesc : String  =  _orgDesc
  def orgEmail : String  =  _orgEmail
  def orgCi : String  =  _orgCi
  def userField1 : String  =  _userField1
  def userField2 : String  =  _userField2
  def userField3 : String  =  _userField3
  def userField4 : String  =  _userField4
  def userField5 : String  =  _userField5
  def status : String  =  _status
  def insertedUser : String  =  _insertedUser
  def insertDt : Date  =  _insertDt
  def modifiedBy : String  =  _modifiedBy
  def updateDt : Date  =  _updateDt


  // Setters
  def setOrgId(value: BigInteger) : Unit = {  _orgId = value }
  def setParentOrgId(value: String) : Unit = {  _parentOrgId = value }
  def setOrgTypeId(value: BigInteger) : Unit = {  _orgTypeId = value }
  def setOrgName(value: String) : Unit = {  _orgName = value }
  def setCooOwner(value: String) : Unit = {  _cooOwner = value }
  def setOpsLead(value: String) : Unit = {  _opsLead = value }
  def setTechLead(value: String) : Unit = {  _techLead = value }
  def setBusOwner(value: String) : Unit = {  _busOwner = value }
  def setOrgDesc(value: String) : Unit = {  _orgDesc = value }
  def setOrgEmail(value: String) : Unit = {  _orgEmail = value }
  def setOrgCi(value: String) : Unit = {  _orgCi = value }
  def setUserField1(value: String) : Unit = {  _userField1 = value }
  def setUserField2(value: String) : Unit = {  _userField2 = value }
  def setUserField3(value: String) : Unit = {  _userField3 = value }
  def setUserField4(value: String) : Unit = {  _userField4 = value }
  def setUserField5(value: String) : Unit = {  _userField5 = value }
  def setStatus(value: String) : Unit = {  _status = value }
  def setInsertedUser(value: String) : Unit = {  _insertedUser = value }
  def setInsertDt(value: Date) : Unit = {  _insertDt = value }
  def setModifiedBy(value: String) : Unit = {  _modifiedBy = value }
  def setUpdateDt(value: Date) : Unit = {  _updateDt = value }
}

