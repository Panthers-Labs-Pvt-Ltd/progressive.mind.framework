# Pipeline API

## Base URL
All the API endpoints described below are available at the base URL:

http://localhost:8080/api/v1/pipelines


## Endpoints

### 1. POST /api/v1/pipelines
#### Description
This endpoint allows you to create a new pipeline by providing the necessary details in the request body.

#### Request Payload
```json
{
  "name": "Weekly Pipeline",
  "frequency": "Weekly",
  "schedule": "10:00 AM every Monday"
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
  "id": 1,
  "name": "Weekly Pipeline",
  "createdDate": "2024-12-29T10:00:00",
  "lastModifiedDate": "2024-12-29T10:00:00",
  "frequency": "Weekly",
  "schedule": "10:00 AM every Monday"
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
    "id": 1,
    "name": "Weekly Pipeline",
    "createdDate": "2024-12-29T10:00:00",
    "lastModifiedDate": "2024-12-29T10:00:00",
    "frequency": "Weekly",
    "schedule": "10:00 AM every Monday"
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

5. POST /api/v1/pipelines/add
Description
This is an additional POST endpoint to create and add a pipeline to the system.

Request Payload

{
  "name": "Daily Pipeline",
  "frequency": "Daily",
  "schedule": "10:00 AM"
}

Response
Status Code: 201 (Created)
Response Body:

{
  "message": "Pipeline added with ID: 2"
}

Error Handling
The API returns the following standard error response for issues:

400 Bad Request: If the input is invalid.
404 Not Found: If the requested pipeline ID does not exist.
500 Internal Server Error: If an unexpected error occurs.









