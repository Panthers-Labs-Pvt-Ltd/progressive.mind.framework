package com.progressive.minds.chimera.core.databaseOps.modal

import java.math.BigInteger
import java.sql.Timestamp

case class OrganizationHierarchy (
                                   private var org_id : BigInteger ,
                                   private var parent_org_id : String ,
                                   private var org_type_id : BigInteger,
                                   private var org_name : String,
                                   private var coo_owner : String,
                                   private var ops_lead : String,
                                   private var tech_lead : String,
                                   private var bus_owner : String,
                                   private var org_desc : String,
                                   private var org_email : String,
                                   private var org_ci : String,
                                   private var status : String = "Y",
                                   private var inserted_user : String = "System",
                                   private var insert_dt : Timestamp = new Timestamp(System.currentTimeMillis()),
                                   private var modified_by : String = "System",
                                   private var update_dt : Timestamp = new Timestamp(System.currentTimeMillis())
                            )
{
  // Getters
  def getOrgId : BigInteger  =  org_id
  def getParentOrgId : String  =  parent_org_id
  def getOrgTypeId : BigInteger  =  org_type_id
  def getOrgName : String  =  org_name
  def getCooOwner : String  =  coo_owner
  def getOpsLead : String  =  ops_lead
  def getTechLead : String  =  tech_lead
  def getBusOwner : String  =  bus_owner
  def getOrgDesc : String  =  org_desc
  def getOrgEmail : String  =  org_email
  def getOrgCi : String  =  org_ci
  def getStatus : String  =  status
  def getInsertedUser : String  =  inserted_user
  def getInsertDt : Timestamp  =  insert_dt
  def getModifiedBy : String  =  modified_by
  def getUpdateDt : Timestamp  =  update_dt


  // Setters
  def setOrgId(value: BigInteger) : Unit = {  org_id = value }
  def setParentOrgId(value: String) : Unit = {  parent_org_id = value }
  def setOrgTypeId(value: BigInteger) : Unit = {  org_type_id = value }
  def setOrgName(value: String) : Unit = {  org_name = value }
  def setCooOwner(value: String) : Unit = {  coo_owner = value }
  def setOpsLead(value: String) : Unit = {  ops_lead = value }
  def setTechLead(value: String) : Unit = {  tech_lead = value }
  def setBusOwner(value: String) : Unit = {  bus_owner = value }
  def setOrgDesc(value: String) : Unit = {  org_desc = value }
  def setOrgEmail(value: String) : Unit = {  org_email = value }
  def setOrgCi(value: String) : Unit = {  org_ci = value }
  def setStatus(value: String) : Unit = {  status = value }
  def setInsertedUser(value: String) : Unit = {  inserted_user = value }
  def setInsertDt(value: Timestamp) : Unit = {  insert_dt = value }
  def setModifiedBy(value: String) : Unit = {  modified_by = value }
  def setUpdateDt(value: Timestamp) : Unit = {  update_dt = value }
}

