# Pipeline API

Here's an improved version with corrected grammar and better clarity:

## The default profile is set to **dev**. If you want to use a different profile, please update the `.env` file in the **resources** folder accordingly.

### For the default profile, basic authentication is enabled:
**Username:** `chimera`  
**Password:** `password`

## Base URL

All the API endpoints described below are available at the base URL:

http://localhost:8080/api/v1/pipelines

## Endpoints

### 1. POST /api/v1/pipelines

#### Description

This endpoint allows you to create a new pipeline by providing the necessary details in the request
body.

#### Request Payload

```json
{
  "id": 1010,
  "pipelineName": "Sample Pipeline",
  "pipelineDescription": "This is a sample data pipeline",
  "processMode": "Batch",
  "runFrequency": "Daily",
  "createdTimestamp": "2025-01-09T12:00:00",
  "createdBy": "admin",
  "updatedTimestamp": "2025-01-09T12:30:00",
  "updatedBy": "admin",
  "activeFlag": "Y"
}

Response
Status Code: 201 (Created)
Response Body:

{
  "message": "Pipeline created successfully with ID: 1"
}

2. GET /api/v1/pipelines/{id}
Description
This endpoint retrieves the details of a pipeline by its ID.

Request Example

GET http://localhost:8080/api/v1/pipelines/1

Response
Status Code: 200 (OK)
Response Body:

  {
        "id": 201,
        "pipelineName": "New Test Pipeline",
        "pipelineDescription": "This is a sample test data pipeline",
        "processMode": "Batch",
        "tags": null,
        "orgHierName": null,
        "createdTimestamp": "2025-01-09T12:00:00.000+00:00",
        "createdBy": "admin",
        "updatedTimestamp": "2025-01-09T12:30:00.000+00:00",
        "updatedBy": "admin",
        "activeFlag": "Y"
    }

Status Code: 404 (Not Found) if the pipeline does not exist.

3. GET /api/v1/pipelines
Description
This endpoint retrieves the list of all pipelines stored in the system.

Request Example
bash
Copy code

GET http://localhost:8080/api/v1/pipelines

Response
Status Code: 200 (OK)
Response Body:

[
   {
        "id": 201,
        "pipelineName": "New Test Pipeline",
        "pipelineDescription": "This is a sample test data pipeline",
        "processMode": "Batch",
        "tags": null,
        "orgHierName": null,
        "createdTimestamp": "2025-01-09T12:00:00.000+00:00",
        "createdBy": "admin",
        "updatedTimestamp": "2025-01-09T12:30:00.000+00:00",
        "updatedBy": "admin",
        "activeFlag": "Y"
    }
]

4. PUT /api/v1/pipelines/{id}
Description
This endpoint allows you to update an existing pipeline by its ID.

PUT http://localhost:8080/api/v1/pipelines/1

{
  "name": "Updated Weekly Pipeline",
  "frequency": "Monthly",
  "schedule": "10:00 AM every 1st Monday"
}

Response
Status Code: 200 (OK)
Response Body:

{
  "message": "Pipeline updated successfully."
}

Error Handling
The API returns the following standard error response for issues:

400 Bad Request: If the input is invalid.
404 Not Found: If the requested pipeline ID does not exist.
500 Internal Server Error: If an unexpected error occurs.

### Now End points are secured using Key-Clock

//TO =-DO
<-----------------IN PROGRESS----------------------->
http://localhost:3000/realms/chimera-api-service-realm/protocol/openid-connect/token

Get the access token

Add as Bearer Token in every API call. 


FLYWAY is integrated













