### Keycloak Configuration

Set up Keycloak as the identity provider for Chimera.

#### 1. Create a new realm in Keycloak
- Name the realm: `chimera`

#### 2. Create a new client in the realm
- Client ID: `chimera_api_client`
- **Settings:**
    - Set the **Admin URL** to `http://localhost:8080/auth`
    - Set **Client Authentication** to `ON`
    - Set **Direct Access Grants Enabled** to `ON`
    - Set **Standard Flow Enabled** to `ON`
- **Credentials:**
    - Select the **Client ID and Secret**
    - Generate the **Client Secret**
- **Roles:**
    - Add a new role: `chimera_user`
    - Add a new role: `chimera_admin`

#### 3. Create a new user in the realm
- Create users with the following details:
    - **Username:** `user_test`, `admin_test`
    - **Email, First Name, Last Name:** Set accordingly
    - **Password:** Set a password for each user
- **Assign Roles:**
    - Go to the **Role Mappings** tab
    - Assign the role `chimera_user` to `user_test`
    - Assign the role `chimera_admin` to `admin_test`

#### 4. Retrieve the Token Endpoint URL
- Go to **Realm Settings**
- Navigate to **Endpoints → OpenID Connect Configuration**
- Copy the **Token Endpoint URL**

---

### Open Postman and Create a New Request

#### 1. Set up a `POST` request
- **URL:** Use the copied `Token Endpoint` URL

#### 2. Configure the Request Body
- Navigate to the **Body** tab
- Select **x-www-form-urlencoded**
- Add the following key-value pairs:

  | Key              | Value                                                  |
  |------------------|--------------------------------------------------------|
  | `client_id`      | `chimera_api_client`                                   |
  | `client_secret`  | Copy the client secret from the client credentials tab |
  | `username`       | `user_test`                                            |
  | `password`       | `password`                                             |
  | `grant_type`     | `password`                                             |

- Click on **Send**

#### 3. Use the Access Token
- You will receive an **access token** in the response.
- Use this token to authenticate requests to the Chimera API.

