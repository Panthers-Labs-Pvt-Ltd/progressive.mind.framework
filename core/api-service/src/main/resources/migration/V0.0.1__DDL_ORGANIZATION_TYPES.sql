DROP TABLE IF EXISTS Organization_Types CASCADE;
CREATE TABLE IF NOT EXISTS Organization_Types (
    ORG_TYPE_NAME       VARCHAR(50),
    ORG_TYPE_DESC       TEXT,
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
    CONSTRAINT pk_orgganization_types PRIMARY KEY (ORG_TYPE_NAME),
    CONSTRAINT check_active_flag CHECK (ACTIVE_FLAG in ('Y', 'N'))
);

INSERT INTO organization_types (org_type_name, org_type_desc, user_field_1, user_field_2, user_field_3, user_field_4, user_field_5, created_by, updated_timestamp, updated_by) values
('Panther Labs', 'Panther Labs', null,null,null,null,null,'PK',null,null);

select * from organization_types;

