DROP TABLE IF EXISTS ORGANIZATION_HIERARCHY CASCADE;

CREATE TABLE IF NOT EXISTS ORGANIZATION_HIERARCHY (
ORG_HIER_NAME       VARCHAR(250),
ORG_TYPE_NAME       VARCHAR(250),
PARENT_ORG_NAME     VARCHAR(250),
COO_OWNER           VARCHAR(500),
OPS_LEAD            VARCHAR(500),
TECH_LEAD           VARCHAR(500),
BUS_OWNER           VARCHAR(500),
ORG_DESC            TEXT,
ORG_EMAIL           VARCHAR(500),
ORG_CI              VARCHAR(500),
USER_FIELD_1        VARCHAR(500),
USER_FIELD_2        VARCHAR(500),
USER_FIELD_3        VARCHAR(500),
USER_FIELD_4        VARCHAR(500),
USER_FIELD_5        VARCHAR(500),
CREATED_TIMESTAMP   TIMESTAMP default CURRENT_TIMESTAMP,
CREATED_BY          VARCHAR(255) default CURRENT_USER,
UPDATED_TIMESTAMP   TIMESTAMP,
UPDATED_BY          VARCHAR(255),
ACTIVE_FLAG         VARCHAR(1) default 'Y' :: CHARACTER VARYING,
CONSTRAINT check_org_hier_active_flag CHECK (ACTIVE_FLAG in ('Y', 'N')),
CONSTRAINT pk_org_hier PRIMARY KEY(ORG_HIER_NAME),
CONSTRAINT fk_hier_type_org_type_name FOREIGN KEY (ORG_TYPE_NAME) REFERENCES ORGANIZATION_TYPES (ORG_TYPE_NAME) ON DELETE CASCADE
);

INSERT INTO ORGANIZATION_HIERARCHY (org_hier_name, org_type_name, parent_org_name, coo_owner, ops_lead, tech_lead, bus_owner, org_desc, org_email, org_ci, user_field_1, user_field_2, user_field_3, user_field_4, user_field_5, created_by, updated_timestamp, updated_by) values
('Chimera', 'Panther Labs', null, 'Abhinav', 'Prashant', 'Manish', 'Mahim', 'Chimera Product', 'chimera@pantherlabs.com', null, null, null, null, null, null, 'PK', null, null);

select * from organization_hierarchy;