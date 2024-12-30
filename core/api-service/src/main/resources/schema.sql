CREATE TABLE PIPELINES (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    frequency VARCHAR(255),
    schedule VARCHAR(255)
);
