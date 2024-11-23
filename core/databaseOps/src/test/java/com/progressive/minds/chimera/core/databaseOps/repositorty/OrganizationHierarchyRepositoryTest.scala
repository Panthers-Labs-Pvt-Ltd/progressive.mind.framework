package com.progressive.minds.chimera.core.databaseOps.repositorty

import com.progressive.minds.chimera.core.databaseOps.modal.OrganizationHierarchy
import com.progressive.minds.chimera.core.databaseOps.repositorty.OrganizationHierarchyRepository._
import org.scalatest.funsuite.AnyFunSuiteLike

import java.math.BigInteger

class OrganizationHierarchyRepositoryTest extends AnyFunSuiteLike {

  test("testListAllOrganizationHierarchy") {
    val allOrg = listAllOrganizationHierarchy
    print(allOrg.size)
  }

  test("testListOrganizationHierarchyByColumns") {
    val filters = Map("org_id" -> "1", "org_name" -> "Natwest")
    val logs = listOrganizationHierarchyByColumns(filters)
    print(logs.size)
  }

  test("testEditOrganizationHierarchy") {

    val input = OrganizationHierarchy (org_id = BigInteger.valueOf(1), parent_org_id = "" , org_type_id = BigInteger.valueOf(1),
      org_name = "Natwest", coo_owner = "Coo Owner", ops_lead = "Ops Lead", tech_lead = "Tech Lead",
      bus_owner = "Business Owner", org_desc = "Org Description", org_email = "manish.kumar@outlook.com",
      org_ci = "SIAA0000")
    editOrganizationHierarchy(input)
  }

}
